package com.busarrival2.app.models;

/**
 * BusRoute Model Class
 * Represents a bus route in the system
 */
public class BusRoute {
    private String routeId;
    private String routeNumber;
    private String routeName;
    private String startPoint;
    private String endPoint;
    private boolean active;

    // Default constructor required for Firebase
    public BusRoute() {
    }

    // Constructor with parameters
    public BusRoute(String routeId, String routeNumber, String routeName, 
                    String startPoint, String endPoint, boolean active) {
        this.routeId = routeId;
        this.routeNumber = routeNumber;
        this.routeName = routeName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.active = active;
    }

    // Getters and Setters
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return routeNumber + " - " + routeName;
    }
}
