package com.thelightningstrikes.facemaker.helper;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JSONHelper {
    private final static String TAG = "JSONHelper";
    private String json = "{}";
    private JSONObject jsonObject = new JSONObject();

    public JSONObject readJSONfromAssets(Context context, String filePath) {
        try {
            InputStream is = context.getAssets().open(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();//
            json = stringBuilder.toString();
        }
        catch(IOException e) {
            Log.e(TAG, "Could not access file: " + e.getMessage());
        }

        try {
            jsonObject  = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "Error while creating JSON from file.");
        }

        return jsonObject;
    }
}
