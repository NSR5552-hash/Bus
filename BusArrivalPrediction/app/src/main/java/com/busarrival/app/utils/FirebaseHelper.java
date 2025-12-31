package com.busarrival.app.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
     * Get reference to Users node in database
     * @return DatabaseReference to users
     */
    public static DatabaseReference getUsersReference() {
        return getDatabase().getReference("users");
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
}
