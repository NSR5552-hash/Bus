package com.busarrival2.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.busarrival2.app.R;
import com.busarrival2.app.utils.FirebaseHelper;
import com.busarrival2.app.utils.ValidationHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * LoginActivity
 * Handles user authentication and login functionality
 */
public class LoginActivity extends AppCompatActivity {

    // UI Components
    private EditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegisterLink;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        mAuth = FirebaseHelper.getAuth();

        // Initialize UI components
        initializeViews();

        // Set click listeners
        setClickListeners();
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        // New layout doesn't use TextInputLayout, just EditText
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Set click listeners for UI components
     */
    private void setClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
    }

    /**
     * Attempt to login with provided credentials
     */
    private void attemptLogin() {
        // Get input values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Clear previous errors (no TextInputLayout in new design)

        // Validate inputs
        if (!validateInputs(email, password)) {
            return;
        }

        // Show progress
        showProgress(true);

        // Firebase authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);

                        if (task.isSuccessful()) {
                            // Login successful
                            checkUserRoleAndNavigate();
                        } else {
                            // Login failed
                            String errorMessage = "Login failed. Please check your credentials";
                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    /**
     * Validate login inputs
     * @param email User email
     * @param password User password
     * @return true if valid, false otherwise
     */
    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        // Validate email
        if (ValidationHelper.isEmpty(email)) {
            Toast.makeText(this, getString(R.string.error_empty_field), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!ValidationHelper.isValidEmail(email)) {
            Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Validate password
        if (ValidationHelper.isEmpty(password)) {
            Toast.makeText(this, getString(R.string.error_empty_field), Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (!ValidationHelper.isValidPassword(password, 6)) {
            Toast.makeText(this, getString(R.string.error_password_short), Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    /**
     * Check user role and navigate to appropriate screen
     */
    private void checkUserRoleAndNavigate() {
        String userId = FirebaseHelper.getCurrentUserId();
        android.util.Log.d("LoginActivity", "=== CHECKING USER ROLE ===");
        android.util.Log.d("LoginActivity", "User ID: " + userId);
        android.util.Log.d("LoginActivity", "Database path: user/" + userId);
        
        FirebaseHelper.getUserReference(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        android.util.Log.d("LoginActivity", "Snapshot exists: " + snapshot.exists());
                        android.util.Log.d("LoginActivity", "Snapshot value: " + snapshot.getValue());
                        
                        if (snapshot.exists()) {
                            String role = snapshot.child("role").getValue(String.class);
                            android.util.Log.d("LoginActivity", "Role found: " + role);
                            
                            if ("admin".equals(role)) {
                                android.util.Log.d("LoginActivity", "✅ ADMIN DETECTED! Navigating to AdminActivity");
                                // Check if sample data exists, if not create it
                                checkAndCreateSampleData();
                                navigateToAdmin();
                            } else {
                                android.util.Log.d("LoginActivity", "Regular user detected. Navigating to MainActivity");
                                navigateToMain();
                            }
                        } else {
                            android.util.Log.e("LoginActivity", "❌ Snapshot does not exist! Defaulting to MainActivity");
                            // Default to main activity
                            navigateToMain();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        android.util.Log.e("LoginActivity", "❌ Database error: " + error.getMessage());
                        Toast.makeText(LoginActivity.this,
                                getString(R.string.error_load_data),
                                Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    }
                });
    }

    /**
     * Navigate to Register Activity
     */
    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Navigate to Main Activity
     */
    private void navigateToMain() {
        Toast.makeText(this, getString(R.string.success_login), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Navigate to Admin Activity
     */
    private void navigateToAdmin() {
        Toast.makeText(this, getString(R.string.success_login), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Check if sample data exists in database, create if not
     */
    private void checkAndCreateSampleData() {
        FirebaseHelper.getRoutesReference()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists() || snapshot.getChildrenCount() == 0) {
                            // No routes exist, create sample data
                            FirebaseHelper.createSampleData();
                            Toast.makeText(LoginActivity.this,
                                    "Sample data created for testing",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Ignore error, continue anyway
                    }
                });
    }

    /**
     * Show or hide progress bar
     * @param show true to show, false to hide
     */
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }
}
