package com.example.pedrp.roommanagement;

public class RoomContextState {

    private int id;
    private String lightStatus;
    private int lightLevel;
    private int noiseLevel;

    public RoomContextState(int id, String status, int light, int noise) {
        super();
        this.id = id;
        this.lightStatus = status;
        this.lightLevel = light;
        this.noiseLevel = noise;
    }

    public int getId() {
        return this.id;
    }

    public String getLightStatus() {
        return this.lightStatus;
    }

    public int getLightLevel() {
        return this.lightLevel;
    }

    public int getNoiseLevel() {
        return this.noiseLevel;
    }
}