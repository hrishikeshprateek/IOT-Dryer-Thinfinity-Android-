package thundersharp.thinkfinity.dryer.users.core.model;

public class RealTimeDeviceSSEData {
    private int HUMIDITY;
    private int TEMP;
    private String DEVICE_ID;
    private long LOG_TIME;

    // Getters and Setters
    public int getHUMIDITY() {
        return HUMIDITY;
    }

    public void setHUMIDITY(int HUMIDITY) {
        this.HUMIDITY = HUMIDITY;
    }

    public int getTEMP() {
        return TEMP;
    }

    public void setTEMP(int TEMP) {
        this.TEMP = TEMP;
    }

    public String getDEVICE_ID() {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID) {
        this.DEVICE_ID = DEVICE_ID;
    }

    public long getLOG_TIME() {
        return LOG_TIME;
    }

    public void setLOG_TIME(long LOG_TIME) {
        this.LOG_TIME = LOG_TIME;
    }
}
