package thundersharp.thinkfinity.dryer.users.core.model;

import org.json.JSONObject;

public class JobSheetModel {
    private String DEVICE_ID;
    private String RECIPE_ID;
    private String RECIPE_NAME;
    private double TEMPERATURE;
    private int HUMIDITY;
    private double TIME; // Use long to store timestamp
    private boolean ACTION;

    public JobSheetModel(String DEVICE_ID, String RECIPE_ID, String RECIPE_NAME, double TEMPERATURE, int HUMIDITY, double TIME, boolean ACTION) {
        this.DEVICE_ID = DEVICE_ID;
        this.RECIPE_ID = RECIPE_ID;
        this.RECIPE_NAME = RECIPE_NAME;
        this.TEMPERATURE = TEMPERATURE;
        this.HUMIDITY = HUMIDITY;
        this.TIME = TIME;
        this.ACTION = ACTION;
    }

    // Getters and Setters
    public String getDEVICE_ID() { return DEVICE_ID; }
    public void setDEVICE_ID(String DEVICE_ID) { this.DEVICE_ID = DEVICE_ID; }

    public String getRECIPE_ID() { return RECIPE_ID; }
    public void setRECIPE_ID(String RECIPE_ID) { this.RECIPE_ID = RECIPE_ID; }

    public String getRECIPE_NAME() { return RECIPE_NAME; }
    public void setRECIPE_NAME(String RECIPE_NAME) { this.RECIPE_NAME = RECIPE_NAME; }

    public double getTEMPERATURE() {
        return TEMPERATURE;
    }

    public void setTEMPERATURE(double TEMPERATURE) {
        this.TEMPERATURE = TEMPERATURE;
    }

    public int getHUMIDITY() {
        return HUMIDITY;
    }

    public void setHUMIDITY(int HUMIDITY) {
        this.HUMIDITY = HUMIDITY;
    }

    public double getTIME() {
        return TIME;
    }

    public void setTIME(double TIME) {
        this.TIME = TIME;
    }

    public void setTIME(long TIME) { this.TIME = TIME; }

    public boolean getACTION() { return ACTION; }
    public void setACTION(boolean ACTION) { this.ACTION = ACTION; }

    // Method to convert JobSheetModel to JSONObject
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DEVICE_ID", getDEVICE_ID());
            jsonObject.put("RECIPE_ID", getRECIPE_ID());
            jsonObject.put("RECIPE_NAME", getRECIPE_NAME());
            jsonObject.put("TEMPERATURE", getTEMPERATURE());
            jsonObject.put("HUMIDITY", getHUMIDITY());
            jsonObject.put("TIME", getTIME());
            jsonObject.put("ACTION", getACTION());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}