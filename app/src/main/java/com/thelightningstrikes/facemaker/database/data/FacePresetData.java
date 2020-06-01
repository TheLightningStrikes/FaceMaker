package com.thelightningstrikes.facemaker.database.data;

import android.content.Context;
import android.util.Log;

import com.thelightningstrikes.facemaker.database.model.FacePreset;
import com.thelightningstrikes.facemaker.helper.JSONHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FacePresetData {
    private final static String TAG = "FacePresetData";
    private List<FacePreset> data = new ArrayList<>();

    public FacePresetData(Context context) {
        try {
            JSONHelper jsonHelper = new JSONHelper();
            JSONObject jsonObject  = jsonHelper.readJSONfromAssets(context, "data/FacePresetData.json");
            JSONArray FacePresetArray = jsonObject.getJSONArray("values");
            for (int i = 0; i < FacePresetArray.length(); i++) {
                FacePreset fp = new FacePreset();
                JSONObject jsonfp = FacePresetArray.getJSONObject(i);
                fp.setPreset_name(jsonfp.getString("preset_name"));
                fp.setFace_value(jsonfp.getString("face_value"));
                fp.setArduino_value(jsonfp.getString("arduino_value"));
                data.add(fp);
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON from file: " + e.getMessage());
        }
    }

    public List<FacePreset> getData() {
        return data;
    }
}
