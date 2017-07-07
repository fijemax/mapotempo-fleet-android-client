package com.mapotempo.fleet.model.submodel;

import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.SubModelBase;

import java.util.Map;

public class Location extends SubModelBase
{
    public Location(Map map, DatabaseHandler databaseHandler) {
        super(map, databaseHandler);
    }

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected void fromMap(Map map) {
        this.lon = Double.valueOf(map.get("lon").toString());
        this.lat = Double.valueOf(map.get("lat").toString());
    }

    @Override
    public String toString() {
        return "(" + lat + " ; " + lon + ")";
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    private double lat;
    private double lon;
}
