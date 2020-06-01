package com.thelightningstrikes.facemaker;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.thelightningstrikes.facemaker.helper.BluetoothHelper;
import com.thelightningstrikes.facemaker.helper.DatabaseHelper.*;
import com.thelightningstrikes.facemaker.database.model.FacePreset;
import com.thelightningstrikes.facemaker.helper.ToastHelper;
import com.thelightningstrikes.facemaker.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    private final static String TAG = "MainActivity";
    private FaceMakerDbHelper dbHelper;
    public ArrayList<FacePreset> facePresets = new ArrayList<>();
    public BluetoothHelper bluetoothHelper;
    private ToastHelper toastHelper;
    SQLiteDatabase dbReader;
    SQLiteDatabase dbWriter;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set view
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Set up database
        dbHelper = new FaceMakerDbHelper(this);
        dbWriter = dbHelper.getWritableDatabase();
        dbReader = dbHelper.getReadableDatabase();
        getFacePresets();

        toastHelper = new ToastHelper(getApplicationContext());

        turnOnBluetooth();
    }

    public void getFacePresets() {
        Cursor cursor = dbReader.query(
                FacePresetReaderContract.FacePresetEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while (cursor.moveToNext()){
            FacePreset fp = new FacePreset();
            fp.setPreset_name(cursor.getString(cursor.getColumnIndex(FacePresetReaderContract.FacePresetEntry.COLUMN_NAME_PRESET_NAME)));
            fp.setFace_value(cursor.getString(cursor.getColumnIndex(FacePresetReaderContract.FacePresetEntry.COLUMN_NAME_FACE_VALUE)));
            fp.setArduino_value(cursor.getString(cursor.getColumnIndex(FacePresetReaderContract.FacePresetEntry.COLUMN_NAME_ARDUINO_VALUE)));
            facePresets.add(fp);
        }
        cursor.close();
    }

    public void turnOnBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            CharSequence text = "Your device does not support Bluetooth. This app requires Bluetooth to work.";
            toastHelper.makeToast(text);
        }
        else {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothHelper = new BluetoothHelper(bluetoothAdapter);
            }
            else {
                // Ask to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    public Boolean isBluetoothModuleConnected() {
        if (bluetoothHelper != null) {
            return bluetoothHelper.isConnected();
        }
        else {
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothHelper = new BluetoothHelper(bluetoothAdapter);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                CharSequence text = "This app requires Bluetooth to work. Please turn on Bluetooth and restart the app.";
                Log.i(TAG, "User denied enabling Bluetooth.");
                toastHelper.makeToast(text);
            }
        }
    }

    // Closes the client socket and causes the thread to finish.
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
        bluetoothHelper.close();
        finish();
    }

}