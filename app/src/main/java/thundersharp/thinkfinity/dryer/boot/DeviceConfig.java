package thundersharp.thinkfinity.dryer.boot;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import thundersharp.thinkfinity.dryer.users.core.model.Device;

public class DeviceConfig {

    private static DeviceConfig deviceConfig;
    private SharedPreferences sharedPreferences;
    private final WeakReference<Context> context;

    public static DeviceConfig getDeviceConfig(Context context){
        return deviceConfig == null ? deviceConfig = new DeviceConfig(new WeakReference<>(context)) : deviceConfig;
    }

    public DeviceConfig initializeStorage(){
        if (sharedPreferences == null)
            sharedPreferences = context.get().getSharedPreferences("DEVICE_CONFIG",Context.MODE_PRIVATE);
        return this;
    }

    public void setCurrentDevice(Device device){
        String data = new Gson().toJson(device);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("data",data);
        editor.apply();
    }

    public Device getCurrentDevice(){
        return new Gson()
                .fromJson(sharedPreferences.getString("data", null), Device.class);
    }

    public DeviceConfig(WeakReference<Context> context) {
        this.context = context;
    }
}
