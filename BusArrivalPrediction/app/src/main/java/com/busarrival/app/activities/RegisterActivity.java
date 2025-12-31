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
import com.busarrival.app.models.User;
import com.busarrival.app.utils.FirebaseHelper;
import com.busarrival.app.utils.ValidationHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * RegisterActivity
 * Handles user registration functionality
 */
public class RegisterActivity extends AppCompatActivity {

    // UI Components
    private TextInputLayout tilFullName, tilEmail, tilPhone, tilPassword, tilConfirmPassword;
    private TextInputEditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView tvLoginLink;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        tilFullName = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Set click listeners for UI components
     */
    private void setClickListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });
    }

    /**
     * Attempt to register new user
     */
    private void attemptRegister() {
        // Get input values
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Clear previous errors
        clearErrors();

        // Validate inputs
        if (!validateInputs(fullName, email, phone, password, confirmPassword)) {
            return;
        }

        // Show progress
        showProgress(true);

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful, save user data
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                saveUserData(firebaseUser.getUid(), fullName, email, phone);
                            }
                        } else {
                            // Registration failed
                            showProgress(false);
                            Toast.makeText(RegisterActivity.this,
                                    getString(R.string.error_register),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Validate registration inputs
     */
    private boolean validateInputs(String fullName, String email, String phone, 
                                   String password, String confirmPassword) {
        boolean isValid = true;

        // Validate full name
        if (ValidationHelper.isEmpty(fullName)) {
            tilFullName.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationHelper.isValidName(fullName)) {
            tilFullName.setError("Please enter a valid name");
            isValid = false;
        }

        // Validate email
        if (ValidationHelper.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationHelper.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        // Validate phone
        if (ValidationHelper.isEmpty(phone)) {
            tilPhone.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationHelper.isValidPhone(phone)) {
            tilPhone.setError(getString(R.string.error_invalid_phone));
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

        // Validate confirm password
        if (ValidationHelper.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_empty_field));
            isValid = false;
        } else if (!ValidationHelper.doPasswordsMatch(password, confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.error_password_mismatch));
            isValid = false;
        }

        return isValid;
    }

    /**
     * Save user data to Firebase Realtime Database
     */
    private void saveUserData(String userId, String fullName, String email, String phone) {
        User user = new User(userId, fullName, email, phone, "user");

        FirebaseHelper.getUserReference(userId)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showProgress(false);

                        if (task.isSuccessful()) {
                            // Data saved successfully
                            Toast.makeText(RegisterActivity.this,
                                    getString(R.string.success_register),
                                    Toast.LENGTH_SHORT).show();
                            navigateToMain();
                        } else {
                            // Failed to save data
                            Toast.makeText(RegisterActivity.this,
                                    "Registration successful but failed to save data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Clear all error messages
     */
    private void clearErrors() {
        tilFullName.setError(null);
        tilEmail.setError(null);
        tilPhone.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);
    }

    /**
     * Navigate to Login Activity
     */
    private void navigateToLogin() {
        finish();
    }

    /**
     * Navigate to Main Activity
     */
    private void navigateToMain() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Show or hide progress bar
     */
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
    }
}
