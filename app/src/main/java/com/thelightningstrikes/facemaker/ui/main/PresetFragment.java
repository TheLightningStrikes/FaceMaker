package com.thelightningstrikes.facemaker.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.thelightningstrikes.facemaker.MainActivity;
import com.thelightningstrikes.facemaker.R;
import com.thelightningstrikes.facemaker.database.model.FacePreset;

import java.util.ArrayList;

public class PresetFragment extends Fragment {
    private static final String TAG = "PresetFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<FacePreset> facePresetNames;
    private MainActivity mainActivity;
    private int width;
    float density = 0;

    static PresetFragment newInstance(int index) {
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
        density = getContext().getResources().getDisplayMetrics().density;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_preset, container, false);
        LinearLayout linearLayout = root.findViewById(R.id.preset_layout);
        width = root.getWidth();
        createButtons(facePresetNames, linearLayout);
        return root;
    }

    private void createButtons(ArrayList<FacePreset> facePresets, LinearLayout linearLayout) {
        for (int i = 0; i < facePresets.size(); i++) {
            final FacePreset fp = facePresets.get(i);
            Button button = new Button(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = (int) (30 * density);
            button.setLayoutParams(layoutParams);
            button.setText(fp.getPreset_name());
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mainActivity.isBluetoothModuleConnected()) {
                        mainActivity.bluetoothHelper.sendData(fp.getArduino_value());
                    }
                }
            });
            createImage(fp, linearLayout);
            linearLayout.addView(button);
        }
    }

    private void createImage(FacePreset fp, LinearLayout linearLayout) {
        TableLayout tableLayout = new TableLayout(getContext());
        TableRow tableRow = new TableRow(getContext());
        String faceValue = fp.getFace_value();
        char[] leftLEDMatrixValues = faceValue.substring(0, faceValue.length()/2).toCharArray();
        char[] rightLEDMatrixValues = faceValue.substring(faceValue.length()/2).toCharArray();

        // Todo: If the left LED matrix is similar to the right LED matrix, maybe it's a good idea to store only one of their codes. Could be faster/more efficient.
        TableLayout leftLEDMatrix = createLEDMatrixTableLayout(leftLEDMatrixValues, leftLEDMatrixValues.length/8);
        TableLayout rightLEDMatrix = createLEDMatrixTableLayout(rightLEDMatrixValues, rightLEDMatrixValues.length/8);
        leftLEDMatrix.setGravity(Gravity.START);
        rightLEDMatrix.setGravity(Gravity.END);

        View view = new View(getContext());
        tableLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.LEDMatrixBackgroundColor));
        tableLayout.setColumnStretchable(1, true);

        tableRow.addView(leftLEDMatrix);
        tableRow.addView(view);
        tableRow.addView(rightLEDMatrix);
        tableLayout.addView(tableRow);
        linearLayout.addView(tableLayout);
    }

    private TableLayout createLEDMatrixTableLayout(char[] values, int rows) {
        TableLayout tableLayout = new TableLayout(getContext());
        Log.i(TAG, ""+width);
        // Loop madness
        for (int i = 0; i < rows; i++) {
            TableRow tr = new TableRow(getContext());
            // Hey, I mean, it works
            for (int j = i * 8; j < ((i*8) + 8); j++) {
                ImageView imageView = new ImageView(getContext());
                int imageSize = (int) (18* density);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(imageSize, imageSize);
                imageView.setLayoutParams(layoutParams);
                switch (values[j]) {
                    case '0': imageView.setImageResource(R.drawable.led_off);
                        break;
                    case '1': imageView.setImageResource(R.drawable.led_on);
                        break;
                    default:break;
                }
                tr.addView(imageView);
            }
            tableLayout.addView(tr);
        }
        return tableLayout;
    }
}
