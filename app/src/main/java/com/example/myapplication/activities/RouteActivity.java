package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.objects.Route;
import com.google.gson.Gson;

public class RouteActivity extends AppCompatActivity {

    private Button buttonStart;
    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_start_route);

        buttonStart = findViewById(R.id.button_start);
        route = new Gson().fromJson(getIntent().getStringExtra("route"), Route.class);
        buttonStart.setOnClickListener(v -> startRoute());
    }

    private void startRoute() {
        Intent intent = new Intent(RouteActivity.this, DoingRouteActivity.class);
        intent.putExtra("route", (new Gson()).toJson(route));
        startActivity(intent);
        finish();
    }
}
