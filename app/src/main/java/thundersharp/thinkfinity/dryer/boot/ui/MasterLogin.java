package thundersharp.thinkfinity.dryer.boot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import thundersharp.thinkfinity.dryer.JSONUtils;
import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.Utils;
import thundersharp.thinkfinity.dryer.boot.Enums.Roles;
import thundersharp.thinkfinity.dryer.boot.helpers.AuthHelper;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.interfaces.OnAuthEvent;
import thundersharp.thinkfinity.dryer.boot.models.LoginResponse;
import thundersharp.thinkfinity.dryer.boot.models.UserAuthData;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.oem.OemHome;
import thundersharp.thinkfinity.dryer.users.UsersHome;

public class MasterLogin extends AppCompatActivity {

    private EditText username, password;
    private UserAuthData userAuthData;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_login);
        alertDialog = ThinkfinityUtils.createDefaultProgressBar(this);

        findViewById(R.id.checkServerL).setOnClickListener((r) -> {
            Utils.performServerCheck(this);
        });

        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        findViewById(R.id.sendotp).setOnClickListener(i -> {
            alertDialog.show();
            if (Utils.validateInputs(username,password)){
                AuthHelper
                        .getInstance(this)
                        .performLogin(username.getText().toString(), password.getText().toString(), new OnAuthEvent() {
                            @Override
                            public void OnLoginSuccess(LoginResponse loginResponse) {
                                Toast.makeText(MasterLogin.this, loginResponse.token, Toast.LENGTH_SHORT).show();
                                Map<String, Object> claims = JSONUtils.extractClaimsFromToken(loginResponse.token);

                                StorageHelper storageHelper = StorageHelper
                                        .getInstance(MasterLogin.this)
                                        .initUserJWTDataStorage();

                                storageHelper.storeJWTData(claims);
                                storageHelper.saveRawToken(loginResponse.token);

                                userAuthData = StorageHelper
                                        .getInstance(MasterLogin.this)
                                        .initUserJWTDataStorage()
                                        .getStoredJWTData();

                                startUserLevelAppBootup(userAuthData);
                                alertDialog.dismiss();
                            }

                            @Override
                            public void OnLoginFailure(Exception e) {
                                alertDialog.dismiss();
                                new AlertDialog.Builder(MasterLogin.this)
                                        .setCancelable(true)
                                        .setMessage(e.getMessage())
                                        .setTitle("ERROR")
                                        .setPositiveButton("OK",(u,i) -> u.dismiss())
                                        .show();
                            }
                        });
            }else alertDialog.dismiss();
        });

    }

    private void startUserLevelAppBootup(UserAuthData userAuthData) {
        if (userAuthData.role == Roles.USER) Utils.startActivity(MasterLogin.this, UsersHome.class);
        else if (userAuthData.role == Roles.MANAGER) Utils.startActivity(MasterLogin.this, UserManager.class);
        else if (userAuthData.role == Roles.OEM) Utils.startActivity(MasterLogin.this, OemHome.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,ActivityIntro.class));
        finish();
    }

}