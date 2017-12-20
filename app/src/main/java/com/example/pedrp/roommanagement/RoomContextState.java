package com.example.pedrp.roommanagement;

public class RoomContextState {

    private int id;
    private String lightStatus;
    private String ringerStatus;
    private int lightLevel;
    private int noiseLevel;

    public RoomContextState(int id, String lightStatus, String ringerStatus, int light, int noise) {
        super();
        this.id = id;
        this.lightStatus = lightStatus;
        this.ringerStatus = ringerStatus;
        this.lightLevel = light;
        this.noiseLevel = noise;
    }

    public int getId() {
        return this.id;
    }

    public String getLightStatus() {
        return this.lightStatus;
    }

    public String getRingerStatus() {
        return this.ringerStatus;
    }

    public int getLightLevel() {
        return this.lightLevel;
    }

    public int getNoiseLevel() {
        return this.noiseLevel;
    }
}