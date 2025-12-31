package com.busarrival.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.busarrival.app.R;
import com.busarrival.app.utils.FirebaseHelper;
import com.busarrival.app.utils.ValidationHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
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
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
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

        // Clear previous errors
        tilEmail.setError(null);
        tilPassword.setError(null);

        // Validate inputs
        if (!validateInputs(email, password)) {
            return;
        }

        // Show progress
        showProgress(true);

        // Attempt Firebase authentication
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
                            Toast.makeText(LoginActivity.this,
                                    getString(R.string.error_login),
                                    Toast.LENGTH_SHORT).show();
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
            tilEmail.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationHelper.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        // Validate password
        if (ValidationHelper.isEmpty(password)) {
            tilPassword.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationHelper.isValidPassword(password, 6)) {
            tilPassword.setError(getString(R.string.error_password_short));
            isValid = false;
        }

        return isValid;
    }

    /**
     * Check user role and navigate to appropriate screen
     */
    private void checkUserRoleAndNavigate() {
        String userId = FirebaseHelper.getCurrentUserId();
        
        FirebaseHelper.getUserReference(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String role = snapshot.child("role").getValue(String.class);
                            
                            if ("admin".equals(role)) {
                                navigateToAdmin();
                            } else {
                                navigateToMain();
                            }
                        } else {
                            // Default to main activity
                            navigateToMain();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
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
     * Show or hide progress bar
     * @param show true to show, false to hide
     */
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }
}
