package com.busarrival2.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.busarrival2.app.R;
import com.busarrival2.app.models.BusRoute;
import com.busarrival2.app.models.BusSchedule;
import com.busarrival2.app.models.BusStop;
import com.busarrival2.app.utils.FirebaseHelper;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

/**
 * AdminActivity
 * Admin dashboard for managing bus routes, stops, and schedules
 */
public class AdminActivity extends AppCompatActivity {

    // UI Components
    private Toolbar toolbar;
    private MaterialCardView cardManageRoutes, cardManageStops, 
                            cardManageSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_admin);

            // Initialize UI components
            initializeViews();

            // Setup toolbar
            setupToolbar();

            // Set click listeners
            setClickListeners();

            // Initialize sample data if needed (with delay to ensure Firebase is ready)
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        initializeSampleData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 500);
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading admin dashboard", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        cardManageRoutes = findViewById(R.id.manageRoutesCard);
        cardManageStops = findViewById(R.id.manageStopsCard);
        cardManageSchedules = findViewById(R.id.manageSchedulesCard);
        // cardViewUsers removed - not in new layout
    }

    /**
     * Setup toolbar with menu
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set click listeners for cards
     */
    private void setClickListeners() {
        cardManageRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddRouteDialog();
            }
        });

        cardManageStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStopDialog();
            }
        });

        cardManageSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddScheduleDialog();
            }
        });

        // cardViewUsers removed from new layout
    }

    /**
     * Show dialog to add new route
     */
    private void showAddRouteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Bus Route");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_route, null);
        builder.setView(dialogView);

        TextInputEditText etRouteNumber = dialogView.findViewById(R.id.etRouteNumber);
        TextInputEditText etRouteName = dialogView.findViewById(R.id.etRouteName);
        TextInputEditText etStartPoint = dialogView.findViewById(R.id.etStartPoint);
        TextInputEditText etEndPoint = dialogView.findViewById(R.id.etEndPoint);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String routeNumber = etRouteNumber.getText().toString().trim();
            String routeName = etRouteName.getText().toString().trim();
            String startPoint = etStartPoint.getText().toString().trim();
            String endPoint = etEndPoint.getText().toString().trim();

            if (!routeNumber.isEmpty() && !routeName.isEmpty()) {
                addRoute(routeNumber, routeName, startPoint, endPoint);
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /**
     * Show dialog to add new stop
     */
    private void showAddStopDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Bus Stop");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_stop, null);
        builder.setView(dialogView);

        TextInputEditText etStopName = dialogView.findViewById(R.id.etStopName);
        TextInputEditText etLatitude = dialogView.findViewById(R.id.etLatitude);
        TextInputEditText etLongitude = dialogView.findViewById(R.id.etLongitude);
        TextInputEditText etRouteId = dialogView.findViewById(R.id.etRouteId);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String stopName = etStopName.getText().toString().trim();
            String latStr = etLatitude.getText().toString().trim();
            String lonStr = etLongitude.getText().toString().trim();
            String routeId = etRouteId.getText().toString().trim();

            if (!stopName.isEmpty() && !routeId.isEmpty()) {
                double lat = latStr.isEmpty() ? 0.0 : Double.parseDouble(latStr);
                double lon = lonStr.isEmpty() ? 0.0 : Double.parseDouble(lonStr);
                addStop(stopName, lat, lon, routeId);
            } else {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /**
     * Show dialog to add new schedule
     */
    private void showAddScheduleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Bus Schedule");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_schedule, null);
        builder.setView(dialogView);

        TextInputEditText etRouteId = dialogView.findViewById(R.id.etRouteId);
        TextInputEditText etStopId = dialogView.findViewById(R.id.etStopId);
        TextInputEditText etArrivalTime = dialogView.findViewById(R.id.etArrivalTime);
        TextInputEditText etFrequency = dialogView.findViewById(R.id.etFrequency);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String routeId = etRouteId.getText().toString().trim();
            String stopId = etStopId.getText().toString().trim();
            String arrivalTime = etArrivalTime.getText().toString().trim();
            String freqStr = etFrequency.getText().toString().trim();

            if (!routeId.isEmpty() && !stopId.isEmpty() && !arrivalTime.isEmpty()) {
                int frequency = freqStr.isEmpty() ? 30 : Integer.parseInt(freqStr);
                addSchedule(routeId, stopId, arrivalTime, frequency);
            } else {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /**
     * Add new route to Firebase
     */
    private void addRoute(String routeNumber, String routeName, String startPoint, String endPoint) {
        String routeId = FirebaseHelper.getRoutesReference().push().getKey();
        BusRoute route = new BusRoute(routeId, routeNumber, routeName, startPoint, endPoint, true);

        FirebaseHelper.getRouteReference(routeId)
                .setValue(route)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, getString(R.string.success_save), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add route", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Add new stop to Firebase
     */
    private void addStop(String stopName, double latitude, double longitude, String routeId) {
        String stopId = FirebaseHelper.getStopsReference().push().getKey();
        BusStop stop = new BusStop(stopId, stopName, latitude, longitude, routeId);

        FirebaseHelper.getStopReference(stopId)
                .setValue(stop)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, getString(R.string.success_save), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add stop", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Add new schedule to Firebase
     */
    private void addSchedule(String routeId, String stopId, String arrivalTime, int frequency) {
        String scheduleId = FirebaseHelper.getSchedulesReference().push().getKey();
        BusSchedule schedule = new BusSchedule(scheduleId, routeId, stopId, arrivalTime,
                arrivalTime, frequency, Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri"));

        FirebaseHelper.getScheduleReference(scheduleId)
                .setValue(schedule)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, getString(R.string.success_save), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add schedule", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Initialize sample data for testing
     */
    private void initializeSampleData() {
        try {
            FirebaseHelper.getRoutesReference()
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                if (!snapshot.exists() || snapshot.getChildrenCount() == 0) {
                                    createSampleData();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Firebase error, sample data not created
                            android.util.Log.e("AdminActivity", "Firebase error: " + error.getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.e("AdminActivity", "initializeSampleData error: " + e.getMessage());
        }
    }

    /**
     * Create sample data for demonstration
     */
    private void createSampleData() {
        // Sample Route 1
        String route1Id = FirebaseHelper.getRoutesReference().push().getKey();
        BusRoute route1 = new BusRoute(route1Id, "101", "Muscat-Salalah Express",
                "Muscat", "Salalah", true);
        FirebaseHelper.getRouteReference(route1Id).setValue(route1);

        // Sample Stop 1
        String stop1Id = FirebaseHelper.getStopsReference().push().getKey();
        BusStop stop1 = new BusStop(stop1Id, "Al Khuwair", 23.5880, 58.3829, route1Id);
        FirebaseHelper.getStopReference(stop1Id).setValue(stop1);

        // Sample Schedule 1
        String schedule1Id = FirebaseHelper.getSchedulesReference().push().getKey();
        BusSchedule schedule1 = new BusSchedule(schedule1Id, route1Id, stop1Id,
                "08:00", "08:05", 30, Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri"));
        FirebaseHelper.getScheduleReference(schedule1Id).setValue(schedule1);

        Toast.makeText(this, "Sample data created", Toast.LENGTH_SHORT).show();
    }

    /**
     * Logout user
     */
    private void logout() {
        FirebaseHelper.signOut();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
