package com.busarrival2.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busarrival2.app.R;
import com.busarrival2.app.adapters.BusArrivalAdapter;
import com.busarrival2.app.models.BusRoute;
import com.busarrival2.app.models.BusSchedule;
import com.busarrival2.app.models.BusStop;
import com.busarrival2.app.utils.FirebaseHelper;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * MainActivity
 * Main user interface for viewing bus arrival predictions
 */
public class MainActivity extends AppCompatActivity {

    // UI Components
    private Toolbar toolbar;
    private Spinner spinnerRoute, spinnerStop;
    private MaterialButton btnGetArrivals;
    private RecyclerView recyclerViewArrivals;
    private MaterialCardView layoutEmptyState;
    private ProgressBar progressBar;

    // Data
    private List<BusRoute> routeList;
    private List<BusStop> stopList;
    private List<BusStop> filteredStopList;
    private BusRoute selectedRoute;
    private BusStop selectedStop;

    // Adapter
    private BusArrivalAdapter arrivalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_main);

            // Initialize UI components
            initializeViews();

            // Setup toolbar
            setupToolbar();

            // Initialize data lists
            routeList = new ArrayList<>();
            stopList = new ArrayList<>();
            filteredStopList = new ArrayList<>();

            // Set click listeners
            setClickListeners();

            // Load data from Firebase (with delay to ensure Firebase is ready)
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadRoutes();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showProgress(false);
                        Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 500);
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading main screen", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        try {
            android.util.Log.d("MainActivity", "Finding toolbar...");
            toolbar = findViewById(R.id.toolbar);
            
            android.util.Log.d("MainActivity", "Finding spinners...");
            spinnerRoute = findViewById(R.id.routeSpinner);
            spinnerStop = findViewById(R.id.stopSpinner);
            
            android.util.Log.d("MainActivity", "Finding button...");
            btnGetArrivals = findViewById(R.id.getArrivalButton);
            
            android.util.Log.d("MainActivity", "Finding recycler view...");
            recyclerViewArrivals = findViewById(R.id.arrivalsRecyclerView);
            
            android.util.Log.d("MainActivity", "Finding empty state...");
            layoutEmptyState = findViewById(R.id.emptyStateCard);
            
            android.util.Log.d("MainActivity", "Finding progress bar...");
            progressBar = findViewById(R.id.progressBar);

            // Setup RecyclerView
            android.util.Log.d("MainActivity", "Setting up RecyclerView...");
            if (recyclerViewArrivals != null) {
                recyclerViewArrivals.setLayoutManager(new LinearLayoutManager(this));
                android.util.Log.d("MainActivity", "RecyclerView setup complete");
            } else {
                android.util.Log.e("MainActivity", "RecyclerView is null!");
            }
            
            android.util.Log.d("MainActivity", "initializeViews completed successfully");
        } catch (Exception e) {
            android.util.Log.e("MainActivity", "Error in initializeViews: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
     * Set click listeners
     */
    private void setClickListeners() {
        btnGetArrivals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArrivalPredictions();
            }
        });

        spinnerRoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedRoute = routeList.get(position - 1);
                    filterStopsByRoute(selectedRoute.getRouteId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerStop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedStop = filteredStopList.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Load bus routes from Firebase
     */
    private void loadRoutes() {
        try {
            showProgress(true);

            FirebaseHelper.getRoutesReference()
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                routeList.clear();
                                
                                for (DataSnapshot routeSnapshot : snapshot.getChildren()) {
                                    BusRoute route = routeSnapshot.getValue(BusRoute.class);
                                    if (route != null && route.isActive()) {
                                        routeList.add(route);
                                    }
                                }

                                updateRouteSpinner();
                                loadStops();
                            } catch (Exception e) {
                                e.printStackTrace();
                                showProgress(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            showProgress(false);
                            Toast.makeText(MainActivity.this,
                                    "Failed to load routes. Please check your connection.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            showProgress(false);
            Toast.makeText(this, "Error loading routes", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Load bus stops from Firebase
     */
    private void loadStops() {
        FirebaseHelper.getStopsReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        stopList.clear();
                        
                        for (DataSnapshot stopSnapshot : snapshot.getChildren()) {
                            BusStop stop = stopSnapshot.getValue(BusStop.class);
                            if (stop != null) {
                                stopList.add(stop);
                            }
                        }

                        showProgress(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showProgress(false);
                        Toast.makeText(MainActivity.this,
                                getString(R.string.error_load_data),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Update route spinner with loaded data
     */
    private void updateRouteSpinner() {
        List<String> routeNames = new ArrayList<>();
        routeNames.add("Select a route");
        
        for (BusRoute route : routeList) {
            routeNames.add(route.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, routeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoute.setAdapter(adapter);
    }

    /**
     * Filter stops by selected route
     */
    private void filterStopsByRoute(String routeId) {
        filteredStopList.clear();
        
        for (BusStop stop : stopList) {
            if (stop.getRouteId().equals(routeId)) {
                filteredStopList.add(stop);
            }
        }

        updateStopSpinner();
    }

    /**
     * Update stop spinner with filtered data
     */
    private void updateStopSpinner() {
        List<String> stopNames = new ArrayList<>();
        stopNames.add("Select a stop");
        
        for (BusStop stop : filteredStopList) {
            stopNames.add(stop.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stopNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStop.setAdapter(adapter);
    }

    /**
     * Get arrival predictions for selected route and stop
     */
    private void getArrivalPredictions() {
        if (selectedRoute == null || selectedStop == null) {
            Toast.makeText(this, getString(R.string.select_route_stop),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);

        FirebaseHelper.getSchedulesReference()
                .orderByChild("routeId")
                .equalTo(selectedRoute.getRouteId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<BusSchedule> schedules = new ArrayList<>();
                        
                        for (DataSnapshot scheduleSnapshot : snapshot.getChildren()) {
                            BusSchedule schedule = scheduleSnapshot.getValue(BusSchedule.class);
                            if (schedule != null && 
                                schedule.getStopId().equals(selectedStop.getStopId())) {
                                schedules.add(schedule);
                            }
                        }

                        displayArrivals(schedules);
                        showProgress(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showProgress(false);
                        Toast.makeText(MainActivity.this,
                                getString(R.string.error_load_data),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Display arrival predictions
     */
    private void displayArrivals(List<BusSchedule> schedules) {
        if (schedules.isEmpty()) {
            showEmptyState(true);
            return;
        }

        // Calculate next arrivals
        List<Integer> arrivalMinutes = new ArrayList<>();
        
        for (BusSchedule schedule : schedules) {
            int minutes = calculateNextArrival(schedule);
            if (minutes >= 0) {
                arrivalMinutes.add(minutes);
            }
        }

        if (arrivalMinutes.isEmpty()) {
            showEmptyState(true);
            return;
        }

        // Setup adapter
        arrivalAdapter = new BusArrivalAdapter(this, selectedRoute, selectedStop, arrivalMinutes);
        recyclerViewArrivals.setAdapter(arrivalAdapter);

        showEmptyState(false);
    }

    /**
     * Calculate next arrival time in minutes
     */
    private int calculateNextArrival(BusSchedule schedule) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date arrivalTime = sdf.parse(schedule.getArrivalTime());
            
            Calendar now = Calendar.getInstance();
            Calendar arrival = Calendar.getInstance();
            arrival.setTime(arrivalTime);
            arrival.set(Calendar.YEAR, now.get(Calendar.YEAR));
            arrival.set(Calendar.MONTH, now.get(Calendar.MONTH));
            arrival.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

            // If arrival time has passed, calculate next occurrence based on frequency
            int frequency = schedule.getFrequency();
            while (arrival.before(now)) {
                arrival.add(Calendar.MINUTE, frequency);
            }

            long diffInMillis = arrival.getTimeInMillis() - now.getTimeInMillis();
            return (int) (diffInMillis / (1000 * 60));

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Show or hide empty state
     */
    private void showEmptyState(boolean show) {
        if (show) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            recyclerViewArrivals.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            recyclerViewArrivals.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Show or hide progress bar
     */
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Logout user
     */
    private void logout() {
        FirebaseHelper.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
