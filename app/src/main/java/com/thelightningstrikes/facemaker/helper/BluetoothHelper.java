package com.thelightningstrikes.facemaker.helper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.thelightningstrikes.facemaker.FaceMaker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;

public class BluetoothHelper {
    private final static String TAG = "BluetoothHelper";
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothSocket mmSocket = null;
    private BluetoothDevice mmDevice = null;
    private ToastHelper toastHelper;

    public BluetoothHelper(BluetoothAdapter bluetoothAdapter) {
        toastHelper = new ToastHelper(FaceMaker.getAppContext());
        findBluetoothModule(bluetoothAdapter);
    }

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        int MESSAGE_READ = 0;
        int MESSAGE_WRITE = 1;
        int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    private void findBluetoothModule(BluetoothAdapter bluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            // Search for HC-06 pass 1234
            boolean found = false;
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                Log.e(TAG, deviceName);
                if (deviceName.contentEquals("WrenchMask")) {
                    connectBluetoothModule(device);
                    Log.e(TAG, "HC-06 found.");
                    found = true;
                    break;
                }
            }
            if (!found) {
                Log.e(TAG, "HC-06 not found.");
                CharSequence text = "The HC-06 is not paired to this device. Please pair and reopen the app.";

                toastHelper.makeToast(text);
            }
        } else {
            // No paired devices
            CharSequence text = "No devices are paired to this device. Please pair a device with Bluetooth.";
            toastHelper.makeToast(text);
        }
    }

    private void connectBluetoothModule(BluetoothDevice device) {
        mmDevice = device;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        }
        catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }
        // The connection attempt succeeded.
        CharSequence text = "HC-06 successfully connected! Socket: " + mmSocket.isConnected();
        toastHelper.makeToast(text);
    }

    public void readData() {
        InputStream mmInStream = null;
        try {
            mmInStream = mmSocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }

        byte[] mmBuffer = new byte[1024];
        int numBytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                numBytes = mmInStream.read(mmBuffer);
                Log.d(TAG, "Received message from Bluetooth module: " + numBytes);
            } catch (IOException | NullPointerException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    public void sendData(String value) {
        OutputStream mmOutStream = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            mmOutStream = mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        // Call this from the main activity to send data to the remote device.
        try {
            mmOutStream.write(value.getBytes());
            Log.i(TAG, "Sending BT message: " + Arrays.toString(value.getBytes()));
        }
        catch (IOException | NullPointerException e) {
            Log.e(TAG, "Error occurred when sending data", e);

            // Send a failure message back to the activity.
            CharSequence text = "Couldn't send data to the other device";
            toastHelper.makeToast(text);
        }
    }

    public Boolean isConnected() {
        if (mmSocket != null) {
            return mmSocket.isConnected();
        }
        else {
            return false;
        }
    }

    public void close() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}
