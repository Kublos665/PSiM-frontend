package com.example.myapplication.objects;

import org.bson.types.ObjectId;

import java.util.Date;

public class Comment {
    private ObjectId id;
    private String body;
    private String date;
    private Integer poiId;
    private Integer userId;

    public Comment(ObjectId id, String body, String date, Integer poiId, Integer userId) {
        this.id = id;
        this.body = body;
        this.date = date;
        this.poiId = poiId;
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public Integer getUserId() {
        return userId;
    }
}
