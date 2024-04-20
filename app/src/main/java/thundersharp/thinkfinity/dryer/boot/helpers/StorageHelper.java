package thundersharp.thinkfinity.dryer.boot.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.util.Map;

import thundersharp.thinkfinity.dryer.boot.models.UserAuthData;

public class StorageHelper {

    private final WeakReference<Context> contextWeakReference;
    private SharedPreferences sharedPreferences;

    public StorageHelper(WeakReference<Context> contextWeakReference) {
        this.contextWeakReference = contextWeakReference;
    }

    public static StorageHelper getInstance(Context context){
        return new StorageHelper(new WeakReference<>(context));
    }

    public StorageHelper initUserJWTDataStorage(){
        sharedPreferences = getContextWeakReference().getSharedPreferences("JWTData",Context.MODE_PRIVATE);
        return this;
    }

    public UserAuthData getStoredJWTData(){
        if (sharedPreferences != null) {
            return new UserAuthData(
                    sharedPreferences.getString("name",null),
                    sharedPreferences.getInt("role",2),
                    sharedPreferences.getString("user_id",null),
                    sharedPreferences.getString("email",null));
        }else return null;
    }

    public boolean storeJWTData(Map<String, Object> data){
        if (sharedPreferences == null) return false;
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putString("name",(String) data.get("name"));
            editor.putInt("role",((Integer) data.get("role")));
            editor.putString("user_id", (String) data.get("user_id"));
            editor.putString("email", (String) data.get("email"));
            editor.apply();
            return true;
        }
    }

    public boolean logOut(){
        if (sharedPreferences == null) return false;
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            return true;
        }
    }

    public Context getContextWeakReference() {
        return contextWeakReference.get();
    }
}