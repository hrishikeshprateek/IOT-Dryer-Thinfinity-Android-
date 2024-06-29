package thundersharp.thinkfinity.dryer.boot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;

import androidx.appcompat.app.AppCompatActivity;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.Utils;
import thundersharp.thinkfinity.dryer.boot.Enums.Roles;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.models.UserAuthData;
import thundersharp.thinkfinity.dryer.oem.OemHome;
import thundersharp.thinkfinity.dryer.users.UsersHome;

public class ActivityIntro extends AppCompatActivity {

    private UserAuthData userAuthData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        userAuthData = StorageHelper.getInstance(ActivityIntro.this).initUserJWTDataStorage()
                .getStoredJWTData();

        findViewById(R.id.getStarted).setOnClickListener((r) -> {
            startActivity(new Intent(this, MasterLogin.class));
            finish();
        });

        findViewById(R.id.checkServer).setOnClickListener((r) -> {
            Utils.performServerCheck(this);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(userAuthData.toString());
        if (userAuthData.getUser_id() != null){
            startUserLevelAppBootup(userAuthData);
        }
    }

    private void startUserLevelAppBootup(UserAuthData userAuthData) {
        if (userAuthData.role == Roles.USER) Utils.startActivity(this, UsersHome.class);
        else if (userAuthData.role == Roles.MANAGER) Utils.startActivity(this, UserManager.class);
        else if (userAuthData.role == Roles.OEM) Utils.startActivity(this, OemHome.class);
    }

}