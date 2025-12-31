package com.busarrival.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.busarrival.app.R;
import com.busarrival.app.utils.FirebaseHelper;

/**
 * SplashActivity
 * Initial loading screen that checks authentication status
 * and navigates to appropriate screen
 */
public class SplashActivity extends AppCompatActivity {

    // Splash screen display duration in milliseconds
    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay navigation to show splash screen
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAuthenticationAndNavigate();
            }
        }, SPLASH_DURATION);
    }

    /**
     * Check if user is authenticated and navigate to appropriate screen
     */
    private void checkAuthenticationAndNavigate() {
        if (FirebaseHelper.isUserLoggedIn()) {
            // User is logged in, navigate to main screen
            navigateToMain();
        } else {
            // User is not logged in, navigate to login screen
            navigateToLogin();
        }
    }

    /**
     * Navigate to Login Activity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Navigate to Main Activity
     */
    private void navigateToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
