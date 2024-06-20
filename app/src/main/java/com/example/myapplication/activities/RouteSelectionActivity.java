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
import com.example.myapplication.objects.Route;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RouteSelectionActivity extends AppCompatActivity {
    private LinearLayout routesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_routes);

        routesContainer = findViewById(R.id.routes_container);

        fetchRoutes();
    }

    private void fetchRoutes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outdoorgame.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<Route>> call = apiInterface.getRoutes();

        call.enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(@NonNull Call<List<Route>> call, @NonNull Response<List<Route>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Route> routes = response.body();
                    createButtonsForRoutes(routes);
                } else {
                    Toast.makeText(RouteSelectionActivity.this, "Failed to load routes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Route>> call, @NonNull Throwable t) {
                Toast.makeText(RouteSelectionActivity.this, "Try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createButtonsForRoutes(List<Route> routes) {
        for (Route route : routes) {
            Button button = new Button(this);
            button.setText(route.getName());
            button.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            button.setOnClickListener(v -> {
                Intent intent = new Intent(RouteSelectionActivity.this, RouteActivity.class);
                intent.putExtra("route", (new Gson()).toJson(route));
                startActivity(intent);
            });
            routesContainer.addView(button);
        }
    }
}