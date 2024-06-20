package com.example.myapplication;

import com.example.myapplication.objects.Achievement;
import com.example.myapplication.objects.Comment;
import com.example.myapplication.objects.PoI;
import com.example.myapplication.objects.PostResponse;
import com.example.myapplication.objects.Route;
import com.example.myapplication.objects.User;

import org.bson.types.ObjectId;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/{username}")
    Call<User> getUserId(@Path("username") String username);

    @POST("users/login")
    Call<PostResponse> loginUser(@Body User user);

    @GET("routes")
    Call<List<Route>> getRoutes();

    @GET("achievements/{userId}")
    Call<List<Achievement>> getAchievements(@Path("userId") Integer userId);

    @GET("achievements")
    Call<List<Achievement>> getAllAchievements();

    @GET("points-of-interest/{poiId}")
    Call<PoI> getPoI(@Path("poiId") Integer poiId);

    @GET("points-of-interest")
    Call<List<PoI>> getAllPoI();

    @GET("comments/{poiId}")
    Call<List<Comment>> getComments(@Path("poiId") Integer poiId);
}