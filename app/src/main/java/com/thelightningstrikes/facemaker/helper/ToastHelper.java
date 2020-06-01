package com.thelightningstrikes.facemaker.helper;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    private Context context;
    private int duration = Toast.LENGTH_LONG;
    public ToastHelper(Context c) {
        context = c;
    }

    public void makeToast(CharSequence text) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
