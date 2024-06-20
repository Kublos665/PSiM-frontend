package com.example.myapplication.objects;

import org.bson.types.ObjectId;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Route {
    private ObjectId routeId;
    private String routeName;
    private List<Integer> poiId;

    public Route(ObjectId routeId, String routeName, List<Integer> poiId) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.poiId = poiId;
    }

    public ObjectId getRouteId() {
        return routeId;
    }

    public String getName() {
        return routeName;
    }

    public List<Integer> getPoiId() {
        return poiId;
    }
}