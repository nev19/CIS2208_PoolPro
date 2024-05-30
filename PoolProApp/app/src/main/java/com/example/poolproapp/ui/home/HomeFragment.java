package com.example.poolproapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poolproapp.R;
import com.example.poolproapp.Pool;
import com.example.poolproapp.backend.PoolDbHelper;
import com.example.poolproapp.databinding.FragmentHomeBinding;
import com.example.poolproapp.ui.home.PoolsAdapter;

import androidx.annotation.Nullable;

import java.util.List;

public class HomeFragment extends Fragment implements PoolsAdapter.OnItemClickListener {

    private FragmentHomeBinding binding;
    private PoolsAdapter adapter;

    private List<Pool> pools;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        RecyclerView recyclerView = binding.recyclerViewPools;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPools();

        // Set up the button to navigate to the new fragment
        Button buttonAddPool = binding.buttonAddPool;
        buttonAddPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_addFragment);
            }
        });

        return root;
    }

    private void loadPools() {
        PoolDbHelper dbHelper = new PoolDbHelper(getContext());
        pools = dbHelper.getPools(); // Assign the result to the member variable
        dbHelper.close();

        // Pass this (HomeFragment) as the listener when creating a new instance of PoolsAdapter
        adapter = new PoolsAdapter(pools, this);
        binding.recyclerViewPools.setAdapter(adapter);

        // Set the text based on the number of pools
        if (pools != null && pools.size() > 0) {
            binding.textHome.setText("All Pools");
        } else {
            binding.textHome.setText("No pools have been added yet...");
        }
    }


    // Implement onItemClick method of the interface
    @Override
    public void onItemClick(Pool pool, int poolId) {
        // Navigate to menu fragment here
        Bundle bundle = new Bundle();
        bundle.putInt("poolId", poolId); // Pass the pool ID to the menu fragment
        Navigation.findNavController(requireView()).navigate(R.id.action_navigation_home_to_menuFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }






    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PoolsAdapter(pools, this); // Pass the listener
        binding.recyclerViewPools.setAdapter(adapter);
    }

}
