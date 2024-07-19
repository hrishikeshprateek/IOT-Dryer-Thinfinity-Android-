package thundersharp.thinkfinity.dryer.users.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.model.UserProfileData;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName, profileEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        StorageHelper storageHelper = StorageHelper.getInstance(this).initUserJWTDataStorage();
        renderViews();

        String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT+"/api/v1/user/get/profile/"+ storageHelper.getRawToken();
        ApiUtils
                .getInstance(this)
                .fetchDataNoList(url, UserProfileData.class, new ApiUtils.ApiResponseCallback<UserProfileData>() {
                    @Override
                    public void onSuccess(UserProfileData result) {
                        setUI(result);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void renderViews() {
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
    }

    private void setUI(UserProfileData result) {
        profileName.setText(String.format("Hi, %s", result.getName()));
        profileEmail.setText(result.getEmail());
    }
}