package thundersharp.thinkfinity.dryer.users;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.Utils;
import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.models.UserAuthData;
import thundersharp.thinkfinity.dryer.boot.ui.ActivityIntro;
import thundersharp.thinkfinity.dryer.boot.ui.MasterLogin;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.interfaces.onRestart;
import thundersharp.thinkfinity.dryer.users.core.model.Device;
import thundersharp.thinkfinity.dryer.users.ui.fragments.About;
import thundersharp.thinkfinity.dryer.users.ui.fragments.AllDevices;
import thundersharp.thinkfinity.dryer.users.ui.fragments.DeviceLogs;
import thundersharp.thinkfinity.dryer.users.ui.fragments.Devicedashboard;
import thundersharp.thinkfinity.dryer.users.ui.fragments.Recipies;

public class UsersHome extends AppCompatActivity implements onRestart {

    public static onRestart onRestart;

    private TabLayout tabLayout;
    public static ViewPager viewPager;
    private LinearLayout linearLayout;
    private ImageView dropDown;
    private TextToSpeech t1;
    private AppCompatButton set_device, ping_server;

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_home);
        onRestart = this;

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        linearLayout = findViewById(R.id.container);
        dropDown = findViewById(R.id.dropdown_menu);
        set_device = findViewById(R.id.set_device);
        ping_server = findViewById(R.id.cPaye);

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

                } else if (menuItem.getItemId() == R.id.android) {
                    new AlertDialog.Builder(UsersHome.this)
                            .setTitle("About")
                            .setView(R.layout.dialog_layout)
                            .setPositiveButton("OK",(r,i) -> r.dismiss())
                            .setCancelable(false)
                            .show();
                }
                return true;
            });
            // Showing the popup menu
            popupMenu.show();
        });

    }

    private synchronized void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Recent Activity"));
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
        tabLayout.addTab(tabLayout.newTab().setText("Payload Logs"));
        tabLayout.addTab(tabLayout.newTab().setText("Devices"));

        gettabs(0);
    }

    @Override
    public void onRestartRequested() {
        Intent intent = new Intent(getApplicationContext(), ActivityIntro.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

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
        viewPagerAdapter.addFragment(new AllDevices(), "All Devices");
        viewPagerAdapter.addFragment(new About(), "About");

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

        set_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });
        if (pos != null)
            viewPager.setCurrentItem(pos);
    }
}