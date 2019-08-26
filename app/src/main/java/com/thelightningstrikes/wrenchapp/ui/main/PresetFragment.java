package com.thelightningstrikes.wrenchapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.thelightningstrikes.wrenchapp.R;

public class PresetFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String[] presets = new String[20];

    public static PresetFragment newInstance(int index) {
        PresetFragment fragment = new PresetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_preset, container, false);
        LinearLayout linearLayout = root.findViewById(R.id.preset_layout);
        System.out.println(linearLayout.getOrientation());

        createButtons(presets.length, linearLayout);
        return root;
    }

    public void createButtons(int amount, LinearLayout linearLayout) {
        Button[] buttons = new Button[amount];
        for (int i = 0; i < amount; i++) {
            Button button = new Button(getContext());
            button.setText(String.valueOf(i+1));
            linearLayout.addView(button);
        }
    }
}
