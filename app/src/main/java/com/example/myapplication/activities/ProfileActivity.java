package com.example.myapplication.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ApiInterface;
import com.example.myapplication.R;
import com.example.myapplication.objects.Achievement;
import com.example.myapplication.objects.User;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    private TextView textUser;
    private TextView textAchievements;
    private TextView textPlaytime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        textUser = findViewById(R.id.text_user);
        textAchievements = findViewById(R.id.text_achievements);
        textPlaytime = findViewById(R.id.text_playtime);

        String username = "Jan";
        fetchUser(username);
    }

    private void fetchUser(String username) {
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
                    textUser.setText("User: " + user.getUsername() + "\nID: " + user.getUserId());
                    fetchAchievements(user.getUserId());
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to fetch user ID", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("ProfileActivity", "Error fetching user ID", t);
                Toast.makeText(ProfileActivity.this, "Error fetching user ID", Toast.LENGTH_SHORT).show();
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
                    int playtime = 0;

                    for (Achievement achievement : achievements) {
                        playtime += achievement.getTime();
                    }

                    playtime /= 60;
                    textAchievements.setText("Number of achievements: " + achievements.size());
                    textPlaytime.setText("Playtime: " + playtime + " minutes");
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to fetch achievements", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Achievement>> call, @NonNull Throwable t) {
                Log.e("ProfileActivity", "Error fetching achievements", t);
                Toast.makeText(ProfileActivity.this, "Error fetching achievements", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
