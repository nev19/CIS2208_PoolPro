package com.example.poolproapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.poolproapp.backend.SupplierDbHelper;
import com.example.poolproapp.backend.TipDbHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.poolproapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String PREF_FIRST_RUN = "first_run";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        //this is just temporary, used to delete all emails
        SupplierDbHelper temp = new SupplierDbHelper(this);
        temp.deleteAllSuppliers();
        */

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_tips, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Check if it's the first run of the app
        SharedPreferences prefs = getSharedPreferences("your_preference_name", Context.MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean(PREF_FIRST_RUN, true);
        if (isFirstRun) {
            insertInitialTips();  // Insert initial records
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(PREF_FIRST_RUN, false);
            editor.apply();  // Mark first run as completed
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void insertInitialTips() {
        Log.d("XXXXXXXX","Tips inserted");
        TipDbHelper dbHelper = new TipDbHelper(this);

        // Check if records already exist before insertion
        if (dbHelper.getTips().isEmpty()) {
            // Insert example records
            dbHelper.insertTip(new Tip(1,"Regularly Clean Skimmer and Pump Baskets: Ensure skimmer and pump baskets are free from debris to maintain proper water circulation."));
            dbHelper.insertTip(new Tip(2,"Monitor and Adjust pH Levels: Keep pH levels between 7.2 and 7.8 to prevent irritation and equipment damage."));
            dbHelper.insertTip(new Tip(3,"Shock the Pool Weekly: Shocking the pool eliminates bacteria and contaminants, keeping the water clean and clear."));
            dbHelper.insertTip(new Tip(4,"Maintain Proper Chlorine Levels: Chlorine kills bacteria and algae, so ensure levels are within recommended ranges."));
            dbHelper.insertTip(new Tip(5,"Brush Pool Walls and Tiles: Brushing walls and tiles prevents algae buildup and maintains a clean appearance."));
            dbHelper.insertTip(new Tip(6,"Check and Clean Filters: Regularly clean or replace pool filters to ensure optimal filtration."));
            dbHelper.insertTip(new Tip(7,"Skim Surface and Vacuum Pool: Skim the pool surface to remove leaves and debris, and vacuum the pool floor to remove dirt and sediment."));
            dbHelper.insertTip(new Tip(8,"Monitor Water Level: Keep water levels consistent to ensure proper skimming and filtration."));
            dbHelper.insertTip(new Tip(9,"Trim Surrounding Vegetation: Trim trees and bushes near the pool to prevent leaves and debris from falling into the water."));
            dbHelper.insertTip(new Tip(10,"Keep Pool Cover Clean: Remove debris from pool covers to prevent it from falling into the water when removed."));
            dbHelper.insertTip(new Tip(11,"Monitor Water Temperature: Keep an eye on water temperature and adjust as needed for comfort and chemical effectiveness."));
            dbHelper.insertTip(new Tip(12,"Regularly Inspect Pool Equipment: Check pool pumps, heaters, and other equipment for leaks, damage, or malfunctions."));

        }

        dbHelper.close();
    }
}