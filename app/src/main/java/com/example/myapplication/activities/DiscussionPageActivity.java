package com.example.myapplication.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ApiInterface;
import com.example.myapplication.R;
import com.example.myapplication.objects.Comment;
import com.example.myapplication.objects.User;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiscussionPageActivity extends AppCompatActivity {

    private LinearLayout commentsLayout;
    private Gson gson = new Gson();
    private Map<Integer, String> userMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_page);

        commentsLayout = findViewById(R.id.comments_layout);

        String poiJson = getIntent().getStringExtra("poi");
        if (poiJson != null) {
            com.example.myapplication.objects.PoI poi = gson.fromJson(poiJson, com.example.myapplication.objects.PoI.class);
            if (poi != null) {
                fetchComments(poi.getPoiId());
            } else {
                Toast.makeText(this, "Failed to get POI data", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Failed to get POI data", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void fetchComments(int poiId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outdoorgame.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<Comment>> call = apiInterface.getComments(poiId);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Comment> comments = response.body();
                    fetchUsersAndDisplayComments(comments);
                } else {
                    Toast.makeText(DiscussionPageActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Comment>> call, @NonNull Throwable t) {
                Toast.makeText(DiscussionPageActivity.this, "Try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUsersAndDisplayComments(List<Comment> comments) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://outdoorgame.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<User>> call = apiInterface.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();
                    for (User user : users) {
                        userMap.put(user.getUserId(), user.getUsername());
                    }
                    displayComments(comments);
                } else {
                    Toast.makeText(DiscussionPageActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                Toast.makeText(DiscussionPageActivity.this, "Try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayComments(List<Comment> comments) {
        for (Comment comment : comments) {
            addCommentToLayout(comment);
        }
    }

    private void addCommentToLayout(Comment comment) {
        TextView textView = new TextView(this);
        String username = userMap.get(comment.getUserId());
        if (username != null) {
            String formattedComment = username + ": " + comment.getBody();
            textView.setText(formattedComment);
            commentsLayout.addView(textView);
        } else {
            Toast.makeText(this, "Failed to get username for user ID: " + comment.getUserId(), Toast.LENGTH_SHORT).show();
        }
    }
}
