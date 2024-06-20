package com.example.myapplication.objects;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Achievement {
    private ObjectId achievementId;
    private Integer time;
    private Integer userId;
    private String date;
    private Integer poiId;

    public Achievement(ObjectId achievementId, Integer time, Integer userId, String date, Integer poiId) {
        this.achievementId = achievementId;
        this.time = time;
        this.userId = userId;
        this.date = date;
        this.poiId = poiId;
    }

    public Integer getPoiId() {
        return poiId;
    }

    public Integer getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public Integer getUserId() {
        return userId;
    }
}