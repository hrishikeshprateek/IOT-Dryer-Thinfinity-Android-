package thundersharp.thinkfinity.dryer.users.core.model;

import java.util.Map;

public class Wrapper {
    private Map<String, RealTimeDeviceSSEData> data;

    public Map<String, RealTimeDeviceSSEData> getData() {
        return data;
    }

    public void setData(Map<String, RealTimeDeviceSSEData> data) {
        this.data = data;
    }
}