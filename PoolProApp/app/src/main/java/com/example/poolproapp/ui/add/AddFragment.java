package com.example.poolproapp.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.poolproapp.Pool;
import com.example.poolproapp.R;
import com.example.poolproapp.databinding.FragmentAddBinding;
import com.example.poolproapp.backend.PoolDbHelper;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddViewModel addViewModel = new ViewModelProvider(this).get(AddViewModel.class);

        binding = FragmentAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up the button to navigate to the new fragment
        Button button = binding.buttonAdd;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add pool to DB
                PoolDbHelper dbHelper = new PoolDbHelper(getContext());

                String nameValue = binding.name.getText().toString();
                String ownerValue = binding.owner.getText().toString();
                String phoneValue = binding.phone.getText().toString();
                String capacityText = binding.capacity.getText().toString();

                // Check if fields are empty
                if (nameValue.isEmpty()) {
                    binding.name.setError("Field cannot be left empty");
                    return;
                }

                if (ownerValue.isEmpty()) {
                    binding.owner.setError("Field cannot be left empty");
                    return;
                }

                if (phoneValue.isEmpty()) {
                    binding.phone.setError("Field cannot be left empty");
                    return;
                }
                // Validate phone number (must be exactly 8 digits)
                if (phoneValue.length() != 8 || !phoneValue.matches("\\d{8}")) {
                    binding.phone.setError("Phone number must be exactly 8 digits");
                    return;
                }

                if (capacityText.isEmpty()) {
                    binding.capacity.setError("Field cannot be left empty");
                    return;
                }

                // Parse the capacity value
                double capacityValue;
                try {
                    capacityValue = Double.parseDouble(capacityText);
                } catch (NumberFormatException e) {
                    binding.capacity.setError("Invalid number format");
                    return;
                }

                // Use -1 as a dummy value as the id will be generated automatically
                Pool pool = new Pool(-1, nameValue, ownerValue, phoneValue, capacityValue);
                // Actual id is returned after insert is successful
                long id = dbHelper.insertPool(pool);

                if (id != -1) {
                    // Navigate back to the home page
                    Navigation.findNavController(v).navigate(R.id.action_addFragment_to_navigation_home);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
