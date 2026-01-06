package com.busarrival2.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.busarrival2.app.R;
import com.busarrival2.app.utils.FirebaseHelper;
import com.google.firebase.FirebaseApp;

/**
 * SplashActivity
 * Initial loading screen that checks authentication status
 * and navigates to appropriate screen
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    // Splash screen display duration in milliseconds
    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_splash);
            
            // Initialize Firebase
            try {
                FirebaseApp.initializeApp(this);
                Log.d(TAG, "Firebase initialized successfully");
            } catch (Exception e) {
                Log.e(TAG, "Firebase initialization error: " + e.getMessage());
            }

            // Delay navigation to show splash screen
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkAuthenticationAndNavigate();
                }
            }, SPLASH_DURATION);
            
        } catch (Exception e) {
            Log.e(TAG, "onCreate error: " + e.getMessage());
            e.printStackTrace();
            // If splash screen fails, go directly to login
            navigateToLogin();
        }
    }

    /**
     * Check if user is authenticated and navigate to appropriate screen
     */
    private void checkAuthenticationAndNavigate() {
        try {
            if (FirebaseHelper.isUserLoggedIn()) {
                // User is logged in, check role and navigate
                String userId = FirebaseHelper.getCurrentUserId();
                if (userId != null) {
                    // For now, just go to login - user will be redirected after login
                    // This avoids crash if user data isn't loaded yet
                    navigateToLogin();
                } else {
                    navigateToLogin();
                }
            } else {
                // User is not logged in, navigate to login screen
                navigateToLogin();
            }
        } catch (Exception e) {
            // If Firebase is not configured or any error occurs, go to login
            Log.e(TAG, "Authentication check error: " + e.getMessage());
            e.printStackTrace();
            navigateToLogin();
        }
    }

    /**
     * Navigate to Login Activity
     */
    private void navigateToLogin() {
        try {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Navigate to Main Activity
     */
    private void navigateToMain() {
        try {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Navigation error: " + e.getMessage());
            e.printStackTrace();
            navigateToLogin();
        }
    }
}
