package com.example.myapplication.objects;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PoI {
    private ObjectId id;

    private String name;

    private Integer qrId;

    private String localization;

    private String hint;

    private Integer poiId;

    public PoI(ObjectId id, String name, Integer qrId, String localization, String hint, Integer poiId) {
        this.id = id;
        this.name = name;
        this.qrId = qrId;
        this.localization = localization;
        this.hint = hint;
        this.poiId = poiId;
    }

    public String getName() {
        return name;
    }

    public String getHint() {
        return hint;
    }

    public ObjectId getId() {
        return id;
    }

    public Integer getQRId() {
        return qrId;
    }

    public Integer getPoiId() {
        return poiId;
    }
}