package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ApiInterface;
import com.example.myapplication.objects.PostResponse;
import com.example.myapplication.objects.User;
import com.example.myapplication.databinding.LoginPageBinding;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private LoginPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText editTextUsername = binding.editTextEmailAddress;
        EditText editTextPassword = binding.editTextPassword;
        Button buttonLogin = binding.buttonLogin;

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                User user = new User(username,password);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://outdoorgame.onrender.com/")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<PostResponse> call = apiInterface.loginUser(user);

                call.enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                        int responseCode = response.code();

                        if (responseCode == 200) {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Login failed: " + responseCode, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                        if (t instanceof IOException) {
                            Toast.makeText(MainActivity.this, "Network error. Check your connection and try again.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Unexpected error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}