package com.busarrival2.app.models;

import java.util.List;

/**
 * BusSchedule Model Class
 * Represents a bus schedule with timing information
 */
public class BusSchedule {
    private String scheduleId;
    private String routeId;
    private String stopId;
    private String arrivalTime; // Format: "HH:mm"
    private String departureTime; // Format: "HH:mm"
    private int frequency; // Frequency in minutes
    private List<String> daysOfWeek; // ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]

    // Default constructor required for Firebase
    public BusSchedule() {
    }

    // Constructor with parameters
    public BusSchedule(String scheduleId, String routeId, String stopId, 
                       String arrivalTime, String departureTime, int frequency, 
                       List<String> daysOfWeek) {
        this.scheduleId = scheduleId;
        this.routeId = routeId;
        this.stopId = stopId;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.frequency = frequency;
        this.daysOfWeek = daysOfWeek;
    }

    // Getters and Setters
    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    @Override
    public String toString() {
        return "BusSchedule{" +
                "scheduleId='" + scheduleId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", stopId='" + stopId + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
