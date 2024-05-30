package com.example.poolproapp.ui.request;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poolproapp.Pool;
import com.example.poolproapp.R;
import com.example.poolproapp.backend.EmailExecutor;
import com.example.poolproapp.backend.EmailService;
import com.example.poolproapp.backend.PoolDbHelper;
import com.example.poolproapp.backend.SupplierDbHelper;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<String> supplierEmails = new ArrayList<>();

    private String mParam1;
    private String mParam2;
    String poolOwner;
    String poolPhone;
    String poolName;
    private int poolId;

    public RequestFragment() {
        // Required empty public constructor
    }

    public static RequestFragment newInstance(String param1, String param2) {
        RequestFragment fragment = new RequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_request, container, false);

        EditText poolOwnerEditText = rootView.findViewById(R.id.owner_edit_text);
        EditText poolPhoneEditText = rootView.findViewById(R.id.phone_edit_text);

        poolId = getArguments().getInt("poolId", -1);

        // calling methods below to get data
        poolOwner = getPoolOwnerFromDatabase(poolId);
        poolPhone = getPoolPhoneFromDatabase(poolId);
        poolName = getPoolNameFromDatabase(poolId);

        // setting the text views
        poolOwnerEditText.setText(poolOwner);
        poolPhoneEditText.setText(poolPhone);

        Spinner litreSpinner = rootView.findViewById(R.id.litre_spinner);
        Spinner emailSpinner = rootView.findViewById(R.id.email_spinner);
        Button addSupplierEmailButton = rootView.findViewById(R.id.add_supplier_email_button);
        Button sendEmailButton = rootView.findViewById(R.id.send_email_button);

        // Populate the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.litre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        litreSpinner.setAdapter(adapter);

        // Fetch supplier emails from the database and update the spinner
        getSupplierEmails();

        // Check if the list of supplier emails is empty
        if (supplierEmails.isEmpty()) {
            // If empty, display a message indicating no emails added yet
            supplierEmails.add("No Supplier Emails added yet");
            emailSpinner.setEnabled(false);
            sendEmailButton.setEnabled(false);
        }

        // Populate the email spinner
        ArrayAdapter<String> emailAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, supplierEmails);
        emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emailSpinner.setAdapter(emailAdapter);



        sendEmailButton.setOnClickListener(v -> {
            String email = emailSpinner.getSelectedItem().toString();
            String litres = litreSpinner.getSelectedItem().toString();

            // Create the email message
            String emailMessage = "Dear Water Supplier, \n\nThe Pool: " +
                    poolName + " would like to request a delivery for " + litres + " litres of water.\n" +
                    "You may contact the owner: " + poolOwner + " on " + poolPhone + "\n\nBest regards from the PoolPro app.";

            // Run the email sending code asynchronously
            EmailExecutor.sendEmailAsync(email, "Request for Water Delivery", emailMessage);

            Toast.makeText(getContext(), "Email has been Sent", Toast.LENGTH_LONG).show();
        });
        addSupplierEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("poolId", poolId); // Pass the pool ID to the menu fragment
                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_request_to_addSupplier, bundle);
            }
        });

        return rootView;
    }

    private String getPoolOwnerFromDatabase(int poolId) {
        // Implement your database retrieval logic here and return the pool name
        // For example:
        PoolDbHelper dbHelper = new PoolDbHelper(getContext());
        Pool pool = dbHelper.getPoolById(poolId);
        String poolOwner = pool.getOwner();
        dbHelper.close();
        return poolOwner;
    }

    private String getPoolPhoneFromDatabase(int poolId) {
        // Implement your database retrieval logic here and return the pool name
        // For example:
        PoolDbHelper dbHelper = new PoolDbHelper(getContext());
        Pool pool = dbHelper.getPoolById(poolId);
        String poolPhone = pool.getPhone();
        dbHelper.close();
        return poolPhone;
    }

    private String getPoolNameFromDatabase(int poolId) {
        // Implement your database retrieval logic here and return the pool name
        // For example:
        PoolDbHelper dbHelper = new PoolDbHelper(getContext());
        Pool pool = dbHelper.getPoolById(poolId);
        String poolName = pool.getName();
        dbHelper.close();
        return poolName;
    }

    // Method to fetch supplier emails from the database
    private void getSupplierEmails() {
        SupplierDbHelper dbHelper = new SupplierDbHelper(getContext());
        supplierEmails.clear(); // Clear the list to avoid duplicates
        supplierEmails.addAll(dbHelper.getSupplierEmails());
    }



}
