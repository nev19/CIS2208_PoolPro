package com.example.poolproapp.ui.acidity;

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
import com.example.poolproapp.ui.acidity.AcidityFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AcidityFragment extends Fragment {

    private static final String ARG_POOL_ID = "poolId";
    private PoolDbHelper poolDbHelper;
    private int poolId;

    public AcidityFragment() {
        // Required empty public constructor
    }

    public static AcidityFragment newInstance(int poolId) {
        AcidityFragment fragment = new AcidityFragment();
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
            Log.d("AcidityFragment", "Pool ID: " + poolId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_acidity, container, false);

        // Retrieve the poolId and log it
        if (getArguments() != null) {
            poolId = getArguments().getInt(ARG_POOL_ID, -1);
            Log.d("AcidityFragment", "Pool ID: " + poolId);
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
        Spinner acidityLevelSpinner = rootView.findViewById(R.id.acidity_level_spinner);
        Spinner desiredAcidityLevelSpinner = rootView.findViewById(R.id.desired_acidity_level_spinner);
        EditText suggestedMlsEditText = rootView.findViewById(R.id.suggested_ml);

        // Populate the first Spinner
        List<String> acidityLevels = new ArrayList<>();
        for (int i = 4; i <= 10; i += 1) {
            acidityLevels.add(String.valueOf(i));
        }

        ArrayAdapter<String> acidityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, acidityLevels);
        acidityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acidityLevelSpinner.setAdapter(acidityAdapter);

        // Populate the second Spinner
        List<String> desiredAcidityLevels = new ArrayList<>();
        for (int i = 6; i <= 8; i++) {
            desiredAcidityLevels.add(String.valueOf(i));
        }

        ArrayAdapter<String> desiredAcidityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, desiredAcidityLevels);
        desiredAcidityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desiredAcidityLevelSpinner.setAdapter(desiredAcidityAdapter);

        // Preselect values for the Spinners
        acidityLevelSpinner.setSelection(acidityAdapter.getPosition("8"));
        desiredAcidityLevelSpinner.setSelection(desiredAcidityAdapter.getPosition("7"));

        // Set listeners to update the result when the selected values change
        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateSuggestedMls(acidityLevelSpinner, desiredAcidityLevelSpinner, suggestedMlsEditText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        };

        acidityLevelSpinner.setOnItemSelectedListener(onItemSelectedListener);
        desiredAcidityLevelSpinner.setOnItemSelectedListener(onItemSelectedListener);

        return rootView;
    }


    private void updateSuggestedMls(Spinner acidityLevelSpinner, Spinner desiredAcidityLevelSpinner, EditText suggestedMlsEditText) {
        String currentLevelStr = acidityLevelSpinner.getSelectedItem().toString();
        String desiredLevelStr = desiredAcidityLevelSpinner.getSelectedItem().toString();

        double desiredLevel = Double.parseDouble(desiredLevelStr);
        double currentLevel = "5+".equals(currentLevelStr) ? 5.5 : Double.parseDouble(currentLevelStr); // Use 5.5 for "5+"


        double difference = currentLevel - desiredLevel;
        String result;

        if(difference <= 0){
            result = "Do not apply acid. Wait for acidity to decrease.";
        }else{
            Pool pool = poolDbHelper.getPoolById(poolId);

            double numMls = ( pool.getCapacity() / 38000) * (difference * 950 );
            DecimalFormat df = new DecimalFormat("0");

            result = String.valueOf(df.format(numMls)) + " millilitres";
        }

        suggestedMlsEditText.setText(String.valueOf(result));
    }
}
