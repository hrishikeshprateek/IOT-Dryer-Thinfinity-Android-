package thundersharp.thinkfinity.dryer.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.Utils;
import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.Enums.BootMode;
import thundersharp.thinkfinity.dryer.boot.barcode.BarCodeScanner;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.models.UserAuthData;
import thundersharp.thinkfinity.dryer.boot.ui.ActivityIntro;
import thundersharp.thinkfinity.dryer.boot.ui.MasterLogin;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.interfaces.onRestart;
import thundersharp.thinkfinity.dryer.users.core.model.Device;
import thundersharp.thinkfinity.dryer.users.ui.ProfileActivity;
import thundersharp.thinkfinity.dryer.users.ui.SettingsActivity;
import thundersharp.thinkfinity.dryer.users.ui.fragments.About;
import thundersharp.thinkfinity.dryer.users.ui.fragments.AllDevices;
import thundersharp.thinkfinity.dryer.users.ui.fragments.DeviceLogs;
import thundersharp.thinkfinity.dryer.users.ui.fragments.Devicedashboard;
import thundersharp.thinkfinity.dryer.users.ui.fragments.JobsheetRecord;
import thundersharp.thinkfinity.dryer.users.ui.fragments.Recipies;

public class UsersHome extends AppCompatActivity implements onRestart {

    public static onRestart onRestart;

    private TabLayout tabLayout;
    public static ViewPager viewPager;
    private ImageView dropDown;
    private TextToSpeech t1;
    private AppCompatButton set_device;

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_home);
        onRestart = this;

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        dropDown = findViewById(R.id.dropdown_menu);
        ImageView scanner = findViewById(R.id.scanner);

        set_device = findViewById(R.id.set_device);
        AppCompatButton ping_server = findViewById(R.id.cPaye);

        UserAuthData storageHelper = StorageHelper.getInstance(this).initUserJWTDataStorage().getStoredJWTData();
        Device deviceConfig = DeviceConfig.getDeviceConfig(this).initializeStorage().getCurrentDevice();

        TextView selected_name = findViewById(R.id.selected_name);
        TextView device_uuid = findViewById(R.id.device_uuid);

        if (storageHelper != null){
            selected_name.setText("Welcome back, "+storageHelper.getName());
        }
        if (deviceConfig != null){
            device_uuid.setText("Your current selected device is "+deviceConfig.getId());
        }

        t1 = new TextToSpeech(this, status -> {
            if(status != TextToSpeech.ERROR) {
                t1.setLanguage(Locale.US);
            }
        });
        setupTabs();

        scanner.setOnClickListener(v -> startActivity(new Intent(this, BarCodeScanner.class).putExtra(ThinkfinityUtils.BOOT_MODE_FOR_SCANNER,
                BootMode.BOOT_FOR_DEVICE_CHANGE)));
        ping_server.setOnClickListener(o -> Utils.performServerCheck(this));

        dropDown.setOnClickListener(o->{
            PopupMenu popupMenu = new PopupMenu(UsersHome.this, dropDown);

            // Inflating popup menu from popup_menu.xml file
            popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                // Toast message on menu item clicked
                if (menuItem.getItemId() == R.id.react_native){
                    new AlertDialog.Builder(UsersHome.this)
                            .setTitle("Logout")
                            .setMessage("Do you really want to logout from current device ??")
                            .setPositiveButton("LOGOUT",(r,i) -> {
                                ThinkfinityUtils.signOut(this);
                                startActivity(new Intent(UsersHome.this, MasterLogin.class));
                                finish();
                            })
                            .setNegativeButton("NO", (r,i) -> r.dismiss())
                            .setCancelable(false)
                            .show();

                }else if (menuItem.getItemId() == R.id.java) {
                    startActivity(new Intent(UsersHome.this, SettingsActivity.class));
                }else if (menuItem.getItemId() == R.id.android) {
                    startActivity(new Intent(UsersHome.this, ProfileActivity.class));
                }
                return true;
            });

            popupMenu.show();
        });

    }

    private synchronized void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Dashboard"));
        tabLayout.addTab(tabLayout.newTab().setText("Recipes"));
        tabLayout.addTab(tabLayout.newTab().setText("Device Logs"));
        tabLayout.addTab(tabLayout.newTab().setText("Job-sheet Record"));
        tabLayout.addTab(tabLayout.newTab().setText("All Devices"));
        tabLayout.addTab(tabLayout.newTab().setText("About App"));

        gettabs(0);
    }

    @Override
    public void onRestartRequested() {
        Intent intent = new Intent(getApplicationContext(), ActivityIntro.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void gettabs(Integer pos) {
        ViewPagerAdapter viewPagerAdapter;
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Devicedashboard(), "Dashboard");
        viewPagerAdapter.addFragment(new Recipies(), "Recipes");
        viewPagerAdapter.addFragment(new DeviceLogs(), "Device Logs");
        viewPagerAdapter.addFragment(new JobsheetRecord(), "Job-sheet Record");
        viewPagerAdapter.addFragment(new AllDevices(), "All Devices");
        viewPagerAdapter.addFragment(new About(), "About App");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        set_device.setOnClickListener(v -> {
            createDialog();
            //viewPager.setCurrentItem(4);
        });
        if (pos != null)
            viewPager.setCurrentItem(pos);
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_change_device, null);
        builder.setView(customLayout);

        AppCompatButton btn_select = customLayout.findViewById(R.id.btn_select);
        AppCompatButton btn_scan = customLayout.findViewById(R.id.btn_scan);

        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_scan.setOnClickListener(v -> {
            startActivity(new Intent(this, BarCodeScanner.class).putExtra(ThinkfinityUtils.BOOT_MODE_FOR_SCANNER,
                    BootMode.BOOT_FOR_DEVICE_CHANGE));
            dialog.dismiss();
        });

        btn_select.setOnClickListener(v -> {
            viewPager.setCurrentItem(4);
            dialog.dismiss();
        });


        dialog.show();
    }
}