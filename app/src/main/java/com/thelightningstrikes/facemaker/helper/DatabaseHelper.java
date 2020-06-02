package com.thelightningstrikes.facemaker.helper;

import android.content.Context;
import android.database.sqlite.*;
import android.provider.BaseColumns;
import android.util.Log;

import com.thelightningstrikes.facemaker.database.data.FacePresetData;
import com.thelightningstrikes.facemaker.database.model.FacePreset;

import java.util.List;

public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    /* Face Preset Table */
    private static final String CREATE_TABLE_FACE_PRESET =
            "CREATE TABLE " + FacePresetReaderContract.FacePresetEntry.TABLE_NAME + " (" +
                    FacePresetReaderContract.FacePresetEntry._ID + " INTEGER PRIMARY KEY," +
                    FacePresetReaderContract.FacePresetEntry.COLUMN_NAME_PRESET_NAME + " TEXT," +
                    FacePresetReaderContract.FacePresetEntry.COLUMN_NAME_FACE_VALUE + " TEXT," +
                    FacePresetReaderContract.FacePresetEntry.COLUMN_NAME_ARDUINO_VALUE + " TEXT)";

    private static final String DELETE_TABLE_FACE_PRESET =
            "DROP TABLE IF EXISTS " + FacePresetReaderContract.FacePresetEntry.TABLE_NAME;

    /* Face Custom Table */
    private static final String CREATE_TABLE_FACE_CUSTOM =
            "CREATE TABLE " + FaceCustomReaderContract.FaceCustomEntry.TABLE_NAME + " (" +
                    FaceCustomReaderContract.FaceCustomEntry._ID + " INTEGER PRIMARY KEY," +
                    FaceCustomReaderContract.FaceCustomEntry.COLUMN_NAME_CUSTOM_NAME + " TEXT," +
                    FaceCustomReaderContract.FaceCustomEntry.COLUMN_NAME_FACE_VALUE + " TEXT," +
                    FaceCustomReaderContract.FaceCustomEntry.COLUMN_NAME_ARDUINO_VALUE + " TEXT)";

    private static final String DELETE_TABLE_FACE_CUSTOM =
            "DROP TABLE IF EXISTS " + FaceCustomReaderContract.FaceCustomEntry.TABLE_NAME;

    public static class FaceMakerDbHelper extends SQLiteOpenHelper {
        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "FaceMaker.db";
        List<FacePreset> facePresets;

        public FaceMakerDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            facePresets = preparePresetData(context);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_FACE_PRESET);
            insertPresetData(db);
            db.execSQL(CREATE_TABLE_FACE_CUSTOM);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // I only update presets
            db.execSQL(DELETE_TABLE_FACE_PRESET);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        List<FacePreset> preparePresetData(Context context) {
            FacePresetData facePresetData = new FacePresetData(context);
            return facePresetData.getData();
        }

        String createFacePresetInsertStatement(FacePreset fp) {
            return "INSERT INTO " + FacePresetReaderContract.FacePresetEntry.TABLE_NAME
                    + "(" + FacePresetReaderContract.FacePresetEntry.COLUMN_NAME_PRESET_NAME + ", "
                    + FacePresetReaderContract.FacePresetEntry.COLUMN_NAME_FACE_VALUE + ", "
                    + FacePresetReaderContract.FacePresetEntry.COLUMN_NAME_ARDUINO_VALUE + ") "
                    + "VALUES ("
                    + fp.getPreset_name() + ", "
                    + fp.getFace_value() + ", "
                    + fp.getArduino_value() + ")";
        }

        void insertPresetData(SQLiteDatabase db) {
            for (int i = 0; i < facePresets.size(); i++) {
                db.execSQL(createFacePresetInsertStatement(facePresets.get(i)));
            }
        }
    }

    public final class FacePresetReaderContract {
        private FacePresetReaderContract() {}

        /* Inner class that defines the table contents */
        public class FacePresetEntry implements BaseColumns {
            public static final String TABLE_NAME = "FacePreset";
            public static final String COLUMN_NAME_PRESET_NAME = "preset_name";
            public static final String COLUMN_NAME_FACE_VALUE = "face_value";
            public static final String COLUMN_NAME_ARDUINO_VALUE = "arduino_value";
        }
    }

    public final class FaceCustomReaderContract {
        private FaceCustomReaderContract() {}

        /* Inner class that defines the table contents */
        class FaceCustomEntry implements BaseColumns {
            static final String TABLE_NAME = "FaceCustom";
            static final String COLUMN_NAME_CUSTOM_NAME = "custom_name";
            static final String COLUMN_NAME_FACE_VALUE = "face_value";
            static final String COLUMN_NAME_ARDUINO_VALUE = "arduino_value";
        }
    }
}
