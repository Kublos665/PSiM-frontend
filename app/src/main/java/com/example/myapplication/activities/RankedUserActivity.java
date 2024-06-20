package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ApiInterface;
import com.example.myapplication.R;
import com.example.myapplication.objects.Achievement;
import com.example.myapplication.objects.User;

import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RankedUserActivity extends AppCompatActivity {
    private LinearLayout usersContainer;
    private Map<Integer, Integer> userAchievementsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rankings);

        usersContainer = findViewById(R.id.ranking_layout);

        fetchUsersAndAchievements();
    }

    private void fetchUsersAndAchievements() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outdoorgame.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<User>> usersCall = apiInterface.getUsers();
        Call<List<Achievement>> achievementsCall = apiInterface.getAllAchievements();

        usersCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    achievementsCall.enqueue(new Callback<List<Achievement>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Achievement>> call, @NonNull Response<List<Achievement>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Achievement> achievements = response.body();
                                calculateAchievementsCount(users, achievements);
                                displayRankedUsers(users);
                            } else {
                                Toast.makeText(RankedUserActivity.this, "Failed to load achievements", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Achievement>> call, @NonNull Throwable t) {
                            Toast.makeText(RankedUserActivity.this, "Failed to load achievements", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(RankedUserActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Toast.makeText(RankedUserActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateAchievementsCount(List<User> users, List<Achievement> achievements) {
        userAchievementsCount = new HashMap<>();
        for (User user : users) {
            userAchievementsCount.put(Integer.parseInt(String.valueOf(user.getUserId())), 0);
        }
        for (Achievement achievement : achievements) {
            Integer userId = achievement.getUserId();
            if (userAchievementsCount.containsKey(userId)) {
                userAchievementsCount.put(Integer.parseInt(String.valueOf(userId)), userAchievementsCount.get(userId) + 1);
            }
        }
    }

    private void displayRankedUsers(List<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return Integer.compare(userAchievementsCount.get(user2.getUserId()), userAchievementsCount.get(user1.getUserId()));
            }
        });

        createTextViewsForUsers(users);
    }

    private void createTextViewsForUsers(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            TextView textView = new TextView(this);

            String text = String.format("%d. User: %s\nAchievements unlocked: %d",
                    i + 1, user.getUsername(), userAchievementsCount.get(user.getUserId()));

            textView.setText(text);
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            usersContainer.addView(textView);
        }
    }
}
