package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class RouteCompleteActivity extends AppCompatActivity {

    private TextView textTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_finish_route);

        textTime = findViewById(R.id.text_route_time);

        long elapsedTime = getIntent().getLongExtra("elapsed_time", 0);

        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int milliseconds = (int) (elapsedTime % 1000);

        textTime.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliseconds / 10));
    }
}
