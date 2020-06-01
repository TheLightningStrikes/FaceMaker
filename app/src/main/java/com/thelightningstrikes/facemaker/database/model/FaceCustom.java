package com.thelightningstrikes.facemaker.database.model;

public class FaceCustom {
    private int id;
    private String preset_name;
    private String face_value;
    private String arduino_value;

    public FaceCustom() {
    }

    public FaceCustom(String preset_name, String face_value) {
        this.preset_name = preset_name;
        this.face_value = face_value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPreset_name(String preset_name) {
        this.preset_name = preset_name;
    }

    public void setFace_value(String face_value) {
        this.face_value = face_value;
    }

    public void setArduino_value(String arduino_value) {
        this.arduino_value = arduino_value;
    }

    public int getId() {
        return this.id;
    }

    public String getPreset_name() {
        return this.preset_name;
    }

    public String getFace_value() {
        return this.face_value;
    }

    public String getArduino_value() {
        return this.arduino_value;
    }
}
