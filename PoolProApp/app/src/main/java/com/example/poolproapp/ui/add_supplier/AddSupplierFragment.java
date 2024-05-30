package com.example.poolproapp.ui.add_supplier;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.poolproapp.Pool;
import com.example.poolproapp.R;
import com.example.poolproapp.Supplier;
import com.example.poolproapp.backend.SupplierDbHelper;
import com.example.poolproapp.databinding.FragmentAddSupplierBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddSupplierFragment extends Fragment {

    private FragmentAddSupplierBinding binding;

    private int poolId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddSupplierViewModel addViewModel = new ViewModelProvider(this).get(AddSupplierViewModel.class);

        poolId = getArguments().getInt("poolId", -1);

        binding = FragmentAddSupplierBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up the button to navigate to the new fragment
        Button button = binding.buttonAdd;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add pool to DB
                SupplierDbHelper dbHelper = new SupplierDbHelper(getContext());

                String emailValue = binding.email.getText().toString();

                // Check if fields are empty
                if (emailValue.isEmpty()) {
                    binding.email.setError("Field cannot be left empty");
                    return;
                }

                String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

                Pattern pattern = Pattern.compile(EMAIL_REGEX);
                Matcher matcher = pattern.matcher(emailValue);

                if(! matcher.matches()){
                    binding.email.setError("Email entered is not valid");
                    return;
                }


                // Use -1 as a dummy value as the id will be generated automatically
                Supplier supplier = new Supplier(-1, emailValue);
                // Actual id is returned after insert is successful
                long id = dbHelper.insertSupplier(supplier);

                getParentFragmentManager().popBackStack();


                // Navigate back to the home page
                Bundle bundle = new Bundle();
                bundle.putInt("poolId", poolId); // Pass the pool ID to the menu fragment
                Navigation.findNavController(requireView()).navigate(R.id.action_addSupplierFragment_to_navigation_request, bundle);


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
