package thundersharp.thinkfinity.dryer.users.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;
import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.model.UserDashbordData;
import thundersharp.thinkfinity.dryer.users.core.model.UserProfileData;
import thundersharp.thinkfinity.dryer.users.ui.SettingsActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName, profileEmail;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        executorService = Executors.newSingleThreadExecutor();
        StorageHelper storageHelper = StorageHelper.getInstance(this).initUserJWTDataStorage();
        syncData();
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
        findViewById(R.id.settings).setOnClickListener(i -> startActivity(new Intent(this, SettingsActivity.class)));
        profileName.setText(String.format("Hi, %s", result.getName()));
        profileEmail.setText(result.getEmail());
        ((TextView)findViewById(R.id.text_address)).setText(result.getAddress());
        ((TextView)findViewById(R.id.phone_no)).setText(String.format("Phone no: %s", result.getPhone()));

        ((LinearLayout) findViewById(R.id.address)).setOnClickListener(i -> ThinkfinityUtils.createErrorMessage(this,"To Edit your address please contact your manager."));
        Glide.with(this).load((result.getPhotoUrl() == null ||
                result.getPhotoUrl().contains("https://example.com/")) ? Uri.parse("https://img.icons8.com/?size=100&id=20749&format=png&color=000000") :
                Uri.parse(result.getPhotoUrl())).into(((CircleImageView) findViewById(R.id.profilepic)));
    }

    private void syncData() {
        executorService.execute(() -> {
            String uri = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT +
                    "/api/v1/user/get/user/dashboard/count/data/" +
                    StorageHelper.getInstance(this).initUserJWTDataStorage().getRawToken();

            ApiUtils
                    .getInstance(this)
                    .fetchDataNoList(uri, UserDashbordData.class, new ApiUtils.ApiResponseCallback<UserDashbordData>() {
                        @Override
                        public void onSuccess(UserDashbordData result) {
                            runOnUiThread(() -> {
                                ((TextView) findViewById(R.id.text_your_devices)).setText(String.valueOf(result.getYourDeviceCount()));
                                ((TextView) findViewById(R.id.text_private_rec)).setText(String.valueOf(result.getPrivateRecipesCount()));
                                ((TextView) findViewById(R.id.text_jobsheets)).setText(String.valueOf(result.getJobSheetCount()));
                                //((TextView) view.findViewById(R.id.jobsheet_hist)).setText(String.valueOf(result.getJobSheetCount()));
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            runOnUiThread(() ->
                                    Toast.makeText(ProfileActivity.this, "Error Occurred: " + errorMessage, Toast.LENGTH_SHORT).show());
                        }
                    });
        });
    }
}