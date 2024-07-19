package thundersharp.thinkfinity.dryer.boot.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.nsd.NsdServiceInfo;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;

public class ThinkfinityUtils {
    public static NsdServiceInfo nsdServiceInfoGlobal = null;
    public static final String HOST_BASE_ADDR_WITH_PORT = "http://api.aqozone.com";
    public static final String BOOT_MODE_FOR_SCANNER = "SCANNER_MODE";
    public static final String SECURE_SHARED_PREF = "secret_shared_prefs";
    public static final String USER_NAME_JWT = "name";
    public static final String USER_EMAIL_JWT = "email";
    public static final String USER_PHONE_JWT = "phone_number";
    public static final String USER_UID_JWT = "user_id";

    public static void showSnackbar(View view, String message) {
        Snackbar.make(view.getContext(), view,
                        message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.WHITE)
                .setTextColor(Color.parseColor("#BD3C50"))
                .show();
    }

    public static boolean isLoggedIn(Activity activity){
        return (StorageHelper.getInstance(activity).initUserJWTDataStorage().getStoredJWTData().getUser_id() !=null);
    }

    public static void signOut(Activity activity){
        StorageHelper.getInstance(activity).initUserJWTDataStorage().logOut(activity);
    }


    public static void startAnotherActivity(Activity packageContext, Class<?> cls){
        packageContext.startActivity(new Intent(packageContext,cls));
        packageContext.finish();
    }

    @SuppressLint("InflateParams")
    public static AlertDialog createDefaultProgressBar(Activity activity){
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        b.setView(LayoutInflater.from(activity).inflate(R.layout.progress_bar,null));
        b.setCancelable(false);
        AlertDialog alertDialog = b.create();

        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return alertDialog;
    }

    public static AlertDialog createErrorMessage(Context activity, String message){
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        b.setMessage(message);
        b.setCancelable(true);
        b.setPositiveButton("OK",((dialog, which) -> dialog.dismiss()));

        return b.create();
    }
}
