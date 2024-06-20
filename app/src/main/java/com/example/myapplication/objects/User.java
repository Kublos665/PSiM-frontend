package com.example.myapplication.objects;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {
    private ObjectId id;
    private String username;
    private String password;
    private Integer userId;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(ObjectId id, String username, String password, Integer userId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
