package thundersharp.thinkfinity.dryer.boot.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Map;

import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.ui.MasterLogin;
import thundersharp.thinkfinity.dryer.boot.models.UserAuthData;

public class StorageHelper {

    private final WeakReference<Context> contextWeakReference;
    private SharedPreferences sharedPreferences;

    public StorageHelper(WeakReference<Context> contextWeakReference) {
        this.contextWeakReference = contextWeakReference;
    }

    public static StorageHelper getInstance(Context context) {
        return new StorageHelper(new WeakReference<>(context));
    }

    public StorageHelper initUserJWTDataStorage() {
        sharedPreferences = getContextWeakReference().getSharedPreferences("JWTData", Context.MODE_PRIVATE);
        return this;
    }

    public UserAuthData getStoredJWTData() {
        if (sharedPreferences != null) {
            return new UserAuthData(
                    sharedPreferences.getString("name", null),
                    sharedPreferences.getInt("role", 2),
                    sharedPreferences.getString("user_id", null),
                    sharedPreferences.getString("email", null));
        } else return null;
    }

    public String getRawToken() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString("auth_token_raw", null);
        } else return null;
    }

    public boolean saveRawToken(String token) {
        if (sharedPreferences == null) return false;
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("auth_token_raw", token);
            editor.apply();
            return true;
        }
    }


    public boolean storeJWTData(Map<String, Object> data) {
        if (sharedPreferences == null) return false;
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putString("name", (String) data.get("name"));
            editor.putInt("role", ((Integer) data.get("role")));
            editor.putString("user_id", (String) data.get("user_id"));
            editor.putString("email", (String) data.get("email"));
            editor.apply();
            return true;
        }
    }

    public boolean logOut(Activity supportHome) {
        DeviceConfig.getDeviceConfig(supportHome).initializeStorage().clearConfig();
        if (sharedPreferences == null) return false;
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            supportHome.startActivity(new Intent(supportHome, MasterLogin.class));
            supportHome.finish();
            return true;
        }
    }

    public Context getContextWeakReference() {
        return contextWeakReference.get();
    }
}