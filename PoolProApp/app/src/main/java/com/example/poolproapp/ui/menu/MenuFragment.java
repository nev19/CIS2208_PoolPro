package com.example.poolproapp.ui.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poolproapp.Pool;
import com.example.poolproapp.R;
import com.example.poolproapp.backend.PoolDbHelper;
import com.example.poolproapp.databinding.FragmentMenuBinding;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private RecyclerView recyclerView;
    private GridButtonAdapter adapter;
    private List<Integer> imageResources; // List of image resources

    private int poolId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the binding layout
        binding = FragmentMenuBinding.inflate(inflater, container, false);

        // Get the root view of the binding
        View rootView = binding.getRoot();

        // Find the TextView from the layout
        TextView poolNameTextView = rootView.findViewById(R.id.pool_name_text_view);
        TextView poolCapacityTextView = rootView.findViewById(R.id.pool_capacity);



        // Find the RecyclerView from the binding
        recyclerView = rootView.findViewById(R.id.recycler_view);

        // Set up the RecyclerView with a GridLayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns grid

        // Initialize the list of image resources
        imageResources = new ArrayList<>();
        // Add your image resources to the list
        imageResources.add(R.drawable.chlorine);
        imageResources.add(R.drawable.acidity);
        imageResources.add(R.drawable.request);
        imageResources.add(R.drawable.notif);

        // Add more images as needed

        poolId = getArguments().getInt("poolId", -1); // -1 is the default value if poolId is not found

        // Fetch the pool name from the database using the poolId
        String poolName = getPoolNameFromDatabase(poolId);

        String poolCapacity = "Pool Capacity: "+String.valueOf((int)getPoolCapacityFromDatabase(poolId)) + " litres";

        // Set the pool name to the TextView
        poolNameTextView.setText(poolName);

        poolCapacityTextView.setText(poolCapacity);

        // Print out the pool ID
        if (poolId != -1) {
            Log.d("MenuFragment", "Pool ID: " + poolId);
        } else {
            Log.d("MenuFragment", "Pool ID not found.");
        }

        // Create and set the adapter for the RecyclerView, passing the poolId to it
        adapter = new GridButtonAdapter(getContext(), imageResources, poolId);
        recyclerView.setAdapter(adapter);

        Button removeButton = rootView.findViewById(R.id.button_remove_pool);
        removeButton.setOnClickListener(v -> {
            removePool(poolId);
            Navigation.findNavController(v).navigate(R.id.action_menuFragment_to_homeFragment);
        });

        // Return the root view of the binding
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void removePool(int poolId) {
        PoolDbHelper dbHelper = new PoolDbHelper(getContext());
        dbHelper.deletePool(poolId);
        dbHelper.close();
    }

    // Method to fetch the pool name from the database using the poolId
    private String getPoolNameFromDatabase(int poolId) {
        // Implement your database retrieval logic here and return the pool name
        // For example:
        PoolDbHelper dbHelper = new PoolDbHelper(getContext());
        Pool pool = dbHelper.getPoolById(poolId);
        String poolName = pool.getName();
        dbHelper.close();
        return poolName;
    }

    private double getPoolCapacityFromDatabase(int poolId) {
        // Implement your database retrieval logic here and return the pool name
        // For example:
        PoolDbHelper dbHelper = new PoolDbHelper(getContext());
        Pool pool = dbHelper.getPoolById(poolId);
        double poolCapacity = pool.getCapacity();
        dbHelper.close();
        return poolCapacity;
    }
}
