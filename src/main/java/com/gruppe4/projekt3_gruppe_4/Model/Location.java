package com.gruppe4.projekt3_gruppe_4.Model;

public class Location {
    private final int locationId;
    private final String name;

    public Location(int locationId, String name) {
        this.locationId = locationId;
        this.name = name;
    }

    public int getLocationId() { return locationId; }
    public String getName() { return name; }
}
