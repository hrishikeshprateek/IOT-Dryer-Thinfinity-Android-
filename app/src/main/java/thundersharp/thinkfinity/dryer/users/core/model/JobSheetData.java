package thundersharp.thinkfinity.dryer.users.core.model;

import androidx.annotation.NonNull;

public class JobSheetData {
    private String DEVICE_ID;
    private String RECIPE_ID;
    private String RECIPE_NAME;
    private double TEMPERATURE;
    private double HUMIDITY;
    private double TIME;
    private boolean ACTION;

    public JobSheetData() {
    }

    public JobSheetData(String DEVICE_ID, String RECIPE_ID, String RECIPE_NAME, double TEMPERATURE, double HUMIDITY, double TIME, boolean ACTION) {
        this.DEVICE_ID = DEVICE_ID;
        this.RECIPE_ID = RECIPE_ID;
        this.RECIPE_NAME = RECIPE_NAME;
        this.TEMPERATURE = TEMPERATURE;
        this.HUMIDITY = HUMIDITY;
        this.TIME = TIME;
        this.ACTION = ACTION;
    }

    public String getDEVICE_ID() {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID) {
        this.DEVICE_ID = DEVICE_ID;
    }

    public String getRECIPE_ID() {
        return RECIPE_ID;
    }

    public void setRECIPE_ID(String RECIPE_ID) {
        this.RECIPE_ID = RECIPE_ID;
    }

    public String getRECIPE_NAME() {
        return RECIPE_NAME;
    }

    public void setRECIPE_NAME(String RECIPE_NAME) {
        this.RECIPE_NAME = RECIPE_NAME;
    }

    public double getTEMPERATURE() {
        return TEMPERATURE;
    }

    public void setTEMPERATURE(double TEMPERATURE) {
        this.TEMPERATURE = TEMPERATURE;
    }

    public double getHUMIDITY() {
        return HUMIDITY;
    }

    public void setHUMIDITY(double HUMIDITY) {
        this.HUMIDITY = HUMIDITY;
    }

    public double getTIME() {
        return TIME;
    }

    public void setTIME(double TIME) {
        this.TIME = TIME;
    }

    public boolean isACTION() {
        return ACTION;
    }

    public void setACTION(boolean ACTION) {
        this.ACTION = ACTION;
    }
}
