package com.busarrival.app.models;

/**
 * BusStop Model Class
 * Represents a bus stop in the system
 */
public class BusStop {
    private String stopId;
    private String stopName;
    private double latitude;
    private double longitude;
    private String routeId;

    // Default constructor required for Firebase
    public BusStop() {
    }

    // Constructor with parameters
    public BusStop(String stopId, String stopName, double latitude, 
                   double longitude, String routeId) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.routeId = routeId;
    }

    // Getters and Setters
    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Override
    public String toString() {
        return stopName;
    }
}
