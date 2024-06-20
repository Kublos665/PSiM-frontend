package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ApiInterface;
import com.example.myapplication.R;
import com.example.myapplication.objects.PoI;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiscussionsActivity extends AppCompatActivity {

    private LinearLayout discussionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_poi_discussions);

        discussionsContainer = findViewById(R.id.discussions_container);

        fetchPoIs();
    }

    private void fetchPoIs() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outdoorgame.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<PoI>> call = apiInterface.getAllPoI();

        call.enqueue(new Callback<List<PoI>>() {
            @Override
            public void onResponse(@NonNull Call<List<PoI>> call, @NonNull Response<List<PoI>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PoI> pois = response.body();
                    createButtonsForDiscussions(pois);
                } else {
                    Toast.makeText(DiscussionsActivity.this, "Failed to load discussions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PoI>> call, @NonNull Throwable t) {
                Toast.makeText(DiscussionsActivity.this, "Try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createButtonsForDiscussions(List<PoI> pois) {
        for (PoI poi : pois) {
            Button button = new Button(this);
            button.setText(poi.getName());
            button.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            button.setOnClickListener(v -> {
                Intent intent = new Intent(DiscussionsActivity.this, DiscussionPageActivity.class);
                intent.putExtra("poi", new Gson().toJson(poi));
                startActivity(intent);
            });
            discussionsContainer.addView(button);
        }
    }
}
