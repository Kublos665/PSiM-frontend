package com.example.myapplication.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ApiInterface;
import com.example.myapplication.R;
import com.example.myapplication.objects.Achievement;
import com.example.myapplication.objects.User;
import com.google.gson.Gson;

import org.bson.types.ObjectId;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AchievementsActivity extends AppCompatActivity {

    private LinearLayout achievementsContainer;
    private int achievementCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_achievements);
        achievementCounter = 0;

        achievementsContainer = findViewById(R.id.achievements_container);

        User user = new Gson().fromJson(getIntent().getStringExtra("user"), User.class);
        String username = "Jan";
        fetchUserIdAndAchievements(username);
    }

    private void fetchUserIdAndAchievements(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outdoorgame.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<User> call = apiInterface.getUserId(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    fetchAchievements(user.getUserId());
                } else {
                    Toast.makeText(AchievementsActivity.this, "Failed to fetch user ID", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("AchievementsActivity", "Error fetching user ID", t);
                Toast.makeText(AchievementsActivity.this, "Error fetching user ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAchievements(Integer userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outdoorgame.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Achievement>> call = apiInterface.getAchievements(1);

        call.enqueue(new Callback<List<Achievement>>() {
            @Override
            public void onResponse(@NonNull Call<List<Achievement>> call, @NonNull Response<List<Achievement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Achievement> achievements = response.body();
                    displayAchievements(achievements);
                } else {
                    Toast.makeText(AchievementsActivity.this, "Failed to load achievements", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Achievement>> call, @NonNull Throwable t) {
                Log.e("AchievementsActivity", "Error fetching achievements", t);
                Toast.makeText(AchievementsActivity.this, "Try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAchievements(List<Achievement> achievements) {
        achievementsContainer.removeAllViews();

        for (Achievement achievement : achievements) {
            achievementCounter++;
            TextView textView = new TextView(this);
            textView.setText(achievementCounter + ". PoI ID: " + achievement.getPoiId().toString() +
                            "\n   Time: " + achievement.getTime() + "\n   Date: " + achievement.getDate());
            achievementsContainer.addView(textView);
        }
    }
}
