package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ApiInterface;
import com.example.myapplication.R;
import com.example.myapplication.objects.PoI;
import com.example.myapplication.objects.Route;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoingRouteActivity extends AppCompatActivity {

    private TextView textTimer, textHint;
    private EditText codeTextbox;
    private MaterialButton buttonAccept;
    private Handler handler;
    private long startTime = 0L;
    private int currentPoiIndex = 0;
    private List<PoI> poiList = new ArrayList<>();
    private Route route;
    private long elapsedTime = 0L;

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int seconds = (int) (timeInMilliseconds / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000);
            textTimer.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliseconds / 10));
            handler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doing_route);

        textTimer = findViewById(R.id.text_timer);
        textHint = findViewById(R.id.text_hint);
        codeTextbox = findViewById(R.id.code_textbox);
        buttonAccept = findViewById(R.id.text_accept);
        handler = new Handler();

        route = new Gson().fromJson(getIntent().getStringExtra("route"), Route.class);
        fetchRoutePoIs(route);

        buttonAccept.setOnClickListener(v -> {
            String code = codeTextbox.getText().toString().trim();

            if (validateCode(code)) {
                currentPoiIndex++;
                if (currentPoiIndex < poiList.size()) {
                    displayCurrentPoI();
                } else {
                    handler.removeCallbacks(updateTimerThread);
                    elapsedTime = SystemClock.uptimeMillis() - startTime;
                    Intent intent = new Intent(DoingRouteActivity.this, RouteCompleteActivity.class);
                    intent.putExtra("elapsed_time", elapsedTime);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(DoingRouteActivity.this, "Wrong code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startRoute() {
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimerThread, 10);
    }

    private void fetchRoutePoIs(Route route) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outdoorgame.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        List<Integer> poiIds = route.getPoiId();

        for (Integer poiId : poiIds) {
            apiInterface.getPoI(poiId).enqueue(new Callback<PoI>() {
                @Override
                public void onResponse(@NonNull Call<PoI> call, @NonNull Response<PoI> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        poiList.add(response.body());

                        if (poiList.size() == poiIds.size()) {
                            displayCurrentPoI();
                            startRoute();
                        }
                    } else {
                        Toast.makeText(DoingRouteActivity.this, "Failed to load PoI", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PoI> call, @NonNull Throwable t) {
                    Toast.makeText(DoingRouteActivity.this, "Try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void displayCurrentPoI() {
        PoI currentPoI = poiList.get(currentPoiIndex);
        textHint.setText("Hint: " + currentPoI.getHint());
        codeTextbox.setText("");
    }

    private boolean validateCode(String code) {
        PoI currentPoI = poiList.get(currentPoiIndex);
        String QRCode = currentPoI.getQRId().toString();
        return code.equals(QRCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimerThread);
    }
}
