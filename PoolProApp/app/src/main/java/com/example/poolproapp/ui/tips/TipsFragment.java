package com.example.poolproapp.ui.tips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.poolproapp.R;
import com.example.poolproapp.Tip;
import com.example.poolproapp.backend.SupplierDbHelper;
import com.example.poolproapp.backend.TipDbHelper;

import java.util.Random;

public class TipsFragment extends Fragment {

    private TextView tipTextView;
    private TipDbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tips, container, false);
        tipTextView = rootView.findViewById(R.id.tip_text_view);
        dbHelper = new TipDbHelper(getContext());

        // Get total number of tips
        int totalTipsCount = dbHelper.getTotalTipsCount();

        // Generate a random number between 0 and totalTipsCount - 1
        int randomTipId = new Random().nextInt(totalTipsCount);

        // Get a random tip from the database
        Tip randomTip = dbHelper.getTipById(randomTipId);

        // Display the random tip in the TextView
        if (randomTip != null) {
            tipTextView.setText(randomTip.getMessage());
        } else {
            tipTextView.setText("Regularly inspect and repair any damaged or loose tiles in and around your pool.");
        }


        return rootView;
    }
}
