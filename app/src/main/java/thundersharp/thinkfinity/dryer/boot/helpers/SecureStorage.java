package thundersharp.thinkfinity.dryer.boot.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.GeneralSecurityException;

import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;

public class SecureStorage {
    private static SecureStorage secureStorage;
    private WeakReference<Context> contextWeakReference;
    SharedPreferences sharedPreferences;
    public static SecureStorage getInstance(WeakReference<Context> contextWeakReference){
        return secureStorage == null ? secureStorage = new SecureStorage(contextWeakReference) : secureStorage;
    }

    public SecureStorage(WeakReference<Context> contextWeakReference) {
        this.contextWeakReference = contextWeakReference;
    }

    public SecureStorage initlizeSecureStorage() throws GeneralSecurityException, IOException {
        sharedPreferences = EncryptedSharedPreferences.create(
                ThinkfinityUtils.SECURE_SHARED_PREF,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                getContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
        return this;
    }

    public void saveEncryptedKey(String key){

    }

    public Context getContext(){
        return contextWeakReference.get();
    }
}
