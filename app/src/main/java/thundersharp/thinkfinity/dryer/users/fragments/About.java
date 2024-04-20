package thundersharp.thinkfinity.dryer.users.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import thundersharp.thinkfinity.dryer.R;

public class About extends Fragment {

    TextView
            pakage,
            appnwmeval,
            vername,
            vercode,
            firstinstall,
            lastupg,
            minsdk,
            targetsdk,
            uid,
            sysapp,
            apkpath,
            perholder;
    PackageManager packageManager;
    String info, path, label;
    PackageInfo packageInfo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_about, container, false);
        //info = applicationInfo.packageName;
        //path = applicationInfo.publicSourceDir;
        packageManager = getActivity().getPackageManager();


        pakage = root.findViewById(R.id.pakage);
        appnwmeval = root.findViewById(R.id.appnwmeval);
        vername = root.findViewById(R.id.vername);
        vercode = root.findViewById(R.id.vercode);
        firstinstall = root.findViewById(R.id.firstinstall);
        lastupg = root.findViewById(R.id.lastupg);
        minsdk = root.findViewById(R.id.minsdk);
        targetsdk = root.findViewById(R.id.targetsdk);
        uid=root.findViewById(R.id.uid);
        sysapp = root.findViewById(R.id.sysapp);
        perholder = root.findViewById(R.id.perholder);

        perholder.setText(getPermissionsByPackageName("thundersharp.thinkfinity.dryer"));

        try {
            packageInfo = packageManager.getPackageInfo("thundersharp.thinkfinity.dryer", 0);
            vername.setText("Version :" + packageInfo.versionName);
            pakage.setText("Spekter AIGS");
            uid.setText(packageInfo.applicationInfo.uid+"");
            minsdk.setText("Min sdk : "+packageInfo.applicationInfo.minSdkVersion);
            targetsdk.setText("Target sdk : "+packageInfo.applicationInfo.targetSdkVersion);
            appnwmeval.setText("thundersharp.aigs.spectre");
            //vername.setText(packageInfo.versionName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                vercode.setText(String.valueOf((int) packageInfo.getLongVersionCode()));
            } else {
                vercode.setText(String.valueOf(packageInfo.versionCode));

            }
            String installDateString  = getDate(packageInfo.firstInstallTime, "MM/dd/yyyy hh:mm:ss");
            String updateDateString  = getDate(packageInfo.lastUpdateTime, "MM/dd/yyyy hh:mm:ss");
            firstinstall.setText(installDateString);
            lastupg.setText(updateDateString);

            //Everything here

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return root;
    }

    protected String getPermissionsByPackageName(String packageName){
        // Initialize a new string builder instance
        StringBuilder builder = new StringBuilder();

        try {
            // Get the package info
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            // Permissions counter
            int counter = 1;

            // Loop through the package info requested permissions
            for (int i = 0; i < packageInfo.requestedPermissions.length; i++) {
                if ((packageInfo.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    String permission =packageInfo.requestedPermissions[i];
                    // To make permission name shorter
                    //permission = permission.substring(permission.lastIndexOf(".")+1);
                    builder.append(permission + "\n\n");
                    counter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}