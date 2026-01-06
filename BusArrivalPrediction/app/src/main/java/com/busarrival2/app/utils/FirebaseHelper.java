package com.busarrival2.app.utils;

import com.busarrival2.app.models.BusRoute;
import com.busarrival2.app.models.BusSchedule;
import com.busarrival2.app.models.BusStop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

/**
 * FirebaseHelper Class
 * Provides centralized access to Firebase services
 */
public class FirebaseHelper {

    // Firebase Authentication instance
    private static FirebaseAuth mAuth;
    
    // Firebase Database instance
    private static FirebaseDatabase mDatabase;

    /**
     * Get Firebase Authentication instance
     * @return FirebaseAuth instance
     */
    public static FirebaseAuth getAuth() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    /**
     * Get Firebase Database instance
     * @return FirebaseDatabase instance
     */
    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
        }
        return mDatabase;
    }

    /**
     * Get current logged in user
     * @return FirebaseUser or null if not logged in
     */
    public static FirebaseUser getCurrentUser() {
        return getAuth().getCurrentUser();
    }

    /**
     * Check if user is logged in
     * @return true if user is logged in, false otherwise
     */
    public static boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    /**
     * Get current user ID
     * @return User ID or null if not logged in
     */
    public static String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    /**
     * Get reference to User node in database
     * @return DatabaseReference to user
     */
    public static DatabaseReference getUsersReference() {
        return getDatabase().getReference("user");
    }

    /**
     * Get reference to BusRoutes node in database
     * @return DatabaseReference to busRoutes
     */
    public static DatabaseReference getRoutesReference() {
        return getDatabase().getReference("busRoutes");
    }

    /**
     * Get reference to BusStops node in database
     * @return DatabaseReference to busStops
     */
    public static DatabaseReference getStopsReference() {
        return getDatabase().getReference("busStops");
    }

    /**
     * Get reference to BusSchedules node in database
     * @return DatabaseReference to busSchedules
     */
    public static DatabaseReference getSchedulesReference() {
        return getDatabase().getReference("busSchedules");
    }

    /**
     * Get reference to specific user in database
     * @param userId The user ID
     * @return DatabaseReference to specific user
     */
    public static DatabaseReference getUserReference(String userId) {
        return getUsersReference().child(userId);
    }

    /**
     * Get reference to specific route in database
     * @param routeId The route ID
     * @return DatabaseReference to specific route
     */
    public static DatabaseReference getRouteReference(String routeId) {
        return getRoutesReference().child(routeId);
    }

    /**
     * Get reference to specific stop in database
     * @param stopId The stop ID
     * @return DatabaseReference to specific stop
     */
    public static DatabaseReference getStopReference(String stopId) {
        return getStopsReference().child(stopId);
    }

    /**
     * Get reference to specific schedule in database
     * @param scheduleId The schedule ID
     * @return DatabaseReference to specific schedule
     */
    public static DatabaseReference getScheduleReference(String scheduleId) {
        return getSchedulesReference().child(scheduleId);
    }

    /**
     * Sign out current user
     */
    public static void signOut() {
        getAuth().signOut();
    }

    /**
     * Create sample data in Firebase for testing
     * This should be called once when database is empty
     */
    public static void createSampleData() {
        // Create sample routes
        // Constructor: routeId, routeNumber, routeName, startPoint, endPoint, active
        BusRoute route1 = new BusRoute("R001", "Route 1", "City Center - Airport", "City Center", "Airport", true);
        BusRoute route2 = new BusRoute("R002", "Route 2", "University - Mall", "University", "Mall", true);
        BusRoute route3 = new BusRoute("R003", "Route 3", "Station - Beach", "Station", "Beach", true);

        getRouteReference("R001").setValue(route1);
        getRouteReference("R002").setValue(route2);
        getRouteReference("R003").setValue(route3);

        // Create sample stops for Route 1
        // Constructor: stopId, stopName, latitude, longitude, routeId
        BusStop stop1 = new BusStop("S001", "City Center Terminal", 25.2048, 55.2708, "R001");
        BusStop stop2 = new BusStop("S002", "Mall Station", 25.2138, 55.2789, "R001");
        BusStop stop3 = new BusStop("S003", "Airport Terminal", 25.2528, 55.3644, "R001");

        getStopReference("S001").setValue(stop1);
        getStopReference("S002").setValue(stop2);
        getStopReference("S003").setValue(stop3);

        // Create sample stops for Route 2
        BusStop stop4 = new BusStop("S004", "University Gate", 25.1972, 55.2744, "R002");
        BusStop stop5 = new BusStop("S005", "Central Park", 25.2050, 55.2850, "R002");
        BusStop stop6 = new BusStop("S006", "Shopping Mall", 25.2100, 55.2900, "R002");

        getStopReference("S004").setValue(stop4);
        getStopReference("S005").setValue(stop5);
        getStopReference("S006").setValue(stop6);

        // Create sample stops for Route 3
        BusStop stop7 = new BusStop("S007", "Train Station", 25.1900, 55.2600, "R003");
        BusStop stop8 = new BusStop("S008", "Marina Walk", 25.0800, 55.1400, "R003");
        BusStop stop9 = new BusStop("S009", "Beach Resort", 25.0700, 55.1300, "R003");

        getStopReference("S007").setValue(stop7);
        getStopReference("S008").setValue(stop8);
        getStopReference("S009").setValue(stop9);

        // Create sample schedules for Route 1
        // Constructor: scheduleId, routeId, stopId, arrivalTime, departureTime, frequency, daysOfWeek
        BusSchedule schedule1 = new BusSchedule("SCH001", "R001", "S001", "08:00", "08:05", 30, 
                Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        BusSchedule schedule2 = new BusSchedule("SCH002", "R001", "S002", "08:15", "08:20", 30, 
                Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        BusSchedule schedule3 = new BusSchedule("SCH003", "R001", "S003", "08:30", "08:35", 30, 
                Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));

        getScheduleReference("SCH001").setValue(schedule1);
        getScheduleReference("SCH002").setValue(schedule2);
        getScheduleReference("SCH003").setValue(schedule3);

        // Create sample schedules for Route 2
        BusSchedule schedule4 = new BusSchedule("SCH004", "R002", "S004", "09:00", "09:05", 20, 
                Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        BusSchedule schedule5 = new BusSchedule("SCH005", "R002", "S005", "09:10", "09:15", 20, 
                Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        BusSchedule schedule6 = new BusSchedule("SCH006", "R002", "S006", "09:20", "09:25", 20, 
                Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));

        getScheduleReference("SCH004").setValue(schedule4);
        getScheduleReference("SCH005").setValue(schedule5);
        getScheduleReference("SCH006").setValue(schedule6);

        // Create sample schedules for Route 3
        BusSchedule schedule7 = new BusSchedule("SCH007", "R003", "S007", "10:00", "10:05", 45, 
                Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        BusSchedule schedule8 = new BusSchedule("SCH008", "R003", "S008", "10:30", "10:35", 45, 
                Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        BusSchedule schedule9 = new BusSchedule("SCH009", "R003", "S009", "10:45", "10:50", 45, 
                Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));

        getScheduleReference("SCH007").setValue(schedule7);
        getScheduleReference("SCH008").setValue(schedule8);
        getScheduleReference("SCH009").setValue(schedule9);
    }
}
