package com.example.poolproapp.ui.chlorine;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.poolproapp.Pool;
import com.example.poolproapp.R;
import com.example.poolproapp.backend.PoolDbHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChlorineFragment extends Fragment {

    private static final String ARG_POOL_ID = "poolId";
    private PoolDbHelper poolDbHelper;
    private int poolId;

    public ChlorineFragment() {
        // Required empty public constructor
    }

    public static ChlorineFragment newInstance(int poolId) {
        ChlorineFragment fragment = new ChlorineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POOL_ID, poolId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            poolId = getArguments().getInt(ARG_POOL_ID, -1);
            Log.d("ChlorineFragment", "Pool ID: " + poolId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chlorine, container, false);

        // Retrieve the poolId and log it
        if (getArguments() != null) {
            poolId = getArguments().getInt(ARG_POOL_ID, -1);
            Log.d("ChlorineFragment", "Pool ID: " + poolId);
        }
        poolDbHelper = new PoolDbHelper(getContext());

        // Find the TextView for the pool name
        TextView poolNameTextView = rootView.findViewById(R.id.pool_name_text_view);

        // Set the text of the pool name TextView
        if (poolId != -1) {
            Pool pool = poolDbHelper.getPoolById(poolId);
            if (pool != null) {
                String poolName = pool.getName();
                poolNameTextView.setText(poolName);
            } else {
                poolNameTextView.setText("Pool not found");
            }
        } else {
            poolNameTextView.setText("Pool ID not provided");
        }

        // Find the Spinners and EditText for the result
        Spinner chlorineLevelSpinner = rootView.findViewById(R.id.chlorine_level_spinner);
        Spinner desiredChlorineLevelSpinner = rootView.findViewById(R.id.desired_chlorine_level_spinner);
        EditText suggestedGramsEditText = rootView.findViewById(R.id.suggested_grams);

        // Populate the first Spinner
        List<String> chlorineLevels = new ArrayList<>();
        for (int i = 0; i <= 7; i += 1) {
            chlorineLevels.add(String.valueOf(i));
        }
        chlorineLevels.add("7+");

        ArrayAdapter<String> chlorineAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, chlorineLevels);
        chlorineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chlorineLevelSpinner.setAdapter(chlorineAdapter);

        // Populate the second Spinner
        List<String> desiredChlorineLevels = new ArrayList<>();
        for (int i = 2; i <= 7; i++) {
            desiredChlorineLevels.add(String.valueOf(i));
        }

        ArrayAdapter<String> desiredChlorineAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, desiredChlorineLevels);
        desiredChlorineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desiredChlorineLevelSpinner.setAdapter(desiredChlorineAdapter);

        // Set listeners to update the result when the selected values change
        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateSuggestedGrams(chlorineLevelSpinner, desiredChlorineLevelSpinner, suggestedGramsEditText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        };

        chlorineLevelSpinner.setOnItemSelectedListener(onItemSelectedListener);
        desiredChlorineLevelSpinner.setOnItemSelectedListener(onItemSelectedListener);

        return rootView;
    }


    private void updateSuggestedGrams(Spinner chlorineLevelSpinner, Spinner desiredChlorineLevelSpinner, EditText suggestedGramsEditText) {
        String currentLevelStr = chlorineLevelSpinner.getSelectedItem().toString();
        String desiredLevelStr = desiredChlorineLevelSpinner.getSelectedItem().toString();

        double desiredLevel = Double.parseDouble(desiredLevelStr);
        double currentLevel = "7+".equals(currentLevelStr) ? 7.5 : Double.parseDouble(currentLevelStr); // Use 5.5 for "5+"


        double difference = desiredLevel - currentLevel;
        String result;

        if(difference <= 0){
            result = "Do not add chlorine. Wait for it to neutralise.";
        }else{
            Pool pool = poolDbHelper.getPoolById(poolId);

            double numGrams = ( pool.getCapacity() / 3800) * (difference * 5.6);
            DecimalFormat df = new DecimalFormat("0");

            result = String.valueOf(df.format(numGrams)) + " grams";
        }

        suggestedGramsEditText.setText(String.valueOf(result));
    }
}
