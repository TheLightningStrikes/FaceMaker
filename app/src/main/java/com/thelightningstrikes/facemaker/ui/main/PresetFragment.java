package com.thelightningstrikes.facemaker.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.thelightningstrikes.facemaker.MainActivity;
import com.thelightningstrikes.facemaker.R;
import com.thelightningstrikes.facemaker.database.model.FacePreset;

import java.util.ArrayList;

public class PresetFragment extends Fragment {
    private static final String TAG = "PresetFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<FacePreset> facePresetNames;
    private static final String[] presets = new String[20];
    private MainActivity mainActivity;

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
        mainActivity = (MainActivity) getActivity();
        facePresetNames = mainActivity.facePresets;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_preset, container, false);
        LinearLayout linearLayout = root.findViewById(R.id.preset_layout);
        System.out.println(linearLayout.getOrientation());

        createButtons(facePresetNames, linearLayout);
        return root;
    }

    private void createButtons(final ArrayList<FacePreset> facePresets, LinearLayout linearLayout) {
        Button[] buttons = new Button[facePresets.size()];
        for (int i = 0; i < facePresets.size(); i++) {
            final FacePreset fp = facePresets.get(i);
            Button button = new Button(getContext());
            button.setText(fp.getPreset_name());
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mainActivity.isBluetoothModuleConnected()) {
                        mainActivity.bluetoothHelper.sendData(fp.getArduino_value());
                    }
                }
            });
            linearLayout.addView(button);
        }
    }
}
