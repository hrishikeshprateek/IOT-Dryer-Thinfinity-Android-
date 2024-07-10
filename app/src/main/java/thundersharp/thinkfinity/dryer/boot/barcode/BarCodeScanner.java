package thundersharp.thinkfinity.dryer.boot.barcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import thundersharp.thinkfinity.dryer.JSONUtils;
import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.Utils;
import thundersharp.thinkfinity.dryer.boot.Enums.Roles;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.models.UserAuthData;
import thundersharp.thinkfinity.dryer.boot.ui.MasterLogin;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.oem.OemHome;
import thundersharp.thinkfinity.dryer.users.UsersHome;


public class BarCodeScanner extends AppCompatActivity {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private boolean isFlashAvailable = false;
    private boolean flashStatus = false;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ImageView flashLight;
    private boolean dialog = true;
    private String scanValue = null;
    private CameraManager mCameraManager;
    private String mCameraId;
    private androidx.appcompat.app.AlertDialog alertDialog;
    private UserAuthData userAuthData;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scanner);

        isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        surfaceView = findViewById(R.id.surfaceView);
        flashLight = findViewById(R.id.flash_toogle);
        alertDialog = ThinkfinityUtils.createDefaultProgressBar(this);

        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        flashLight.setOnClickListener(i -> {
            //toggleFlash(!flashStatus);
        });
    }

    public void toggleFlash(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
            flashStatus = status;
            flashLight.setImageDrawable(getDrawable(status ? R.drawable.outline_flashlight_off_24 : R.drawable.outline_flashlight_on_24));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(300, 300)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarCodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(BarCodeScanner.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (dialog) {
                            dialog = false;
                            alertDialog.show();
                            scanValue = barcodes.valueAt(0).displayValue;
                            try {
                                String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT +"/api/v1/user/get/profile/"+scanValue;

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                        response -> {
                                            try {
                                                boolean success = response.getBoolean("success");
                                                if (!success){
                                                    showUnDismissibleDialog(response.getString("message"));
                                                }else {
                                                    new AlertDialog.Builder(BarCodeScanner.this)
                                                            .setTitle("Confirm link ?")
                                                            .setMessage("Confirm link of account with email: "+response.getJSONObject("data").getString("email")+  " and Name: "+response.getJSONObject("data").getString("name"))
                                                            .setCancelable(false)
                                                            .setNegativeButton("NO", (r,e) ->{
                                                                dialog = true;
                                                                r.dismiss();
                                                            })
                                                            .setPositiveButton("LINK", (e,i)->{
                                                                Map<String, Object> claims = JSONUtils.extractClaimsFromToken(scanValue);

                                                                StorageHelper storageHelper = StorageHelper
                                                                        .getInstance(BarCodeScanner.this)
                                                                        .initUserJWTDataStorage();

                                                                storageHelper.storeJWTData(claims);
                                                                storageHelper.saveRawToken(scanValue);

                                                                userAuthData = StorageHelper
                                                                        .getInstance(BarCodeScanner.this)
                                                                        .initUserJWTDataStorage()
                                                                        .getStoredJWTData();

                                                                startUserLevelAppBootup(userAuthData);
                                                            })
                                                            .show();
                                                }
                                                alertDialog.dismiss();

                                            } catch (Exception e) {
                                                showUnDismissibleDialog(e.getMessage());
                                            }
                                        },
                                        error -> showUnDismissibleDialog(error.getMessage()));

                                Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

                            }catch (Exception e){
                                showUnDismissibleDialog(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });

                    Log.d("TAG", barcodes.valueAt(0).rawValue);
                }
            }
        });
    }

    private void startUserLevelAppBootup(UserAuthData userAuthData) {
        if (userAuthData.role == Roles.USER) Utils.startActivity(BarCodeScanner.this, UsersHome.class);
        else if (userAuthData.role == Roles.MANAGER) Utils.startActivity(BarCodeScanner.this, UserManager.class);
        else if (userAuthData.role == Roles.OEM) Utils.startActivity(BarCodeScanner.this, OemHome.class);
        finish();
    }

    void showUnDismissibleDialog(String message){
        new AlertDialog.Builder(BarCodeScanner.this)
                .setMessage(message)
                .setPositiveButton("OK", ((dialogInterface, i) -> {
                    alertDialog.dismiss();
                    dialog = true;
                })).setCancelable(false)
                .show();

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onPause() {
        super.onPause();
        if (flashStatus) {
            //toggleFlash(false);
        }
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}