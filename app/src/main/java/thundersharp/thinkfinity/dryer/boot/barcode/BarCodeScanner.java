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
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;


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



        flashLight.setOnClickListener(i -> {
            if (flashStatus) {
                flashLight.setImageDrawable(getDrawable(R.drawable.outline_flashlight_on_24));
                //toggleFlash(false);
                flashStatus = false;
            } else {
                flashLight.setImageDrawable(getDrawable(R.drawable.outline_flashlight_off_24));
                // toggleFlash(true);
                flashStatus = true;
            }
        });
        //initialiseDetectorsAndSources();

    }

    public void toggleFlash(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
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
                //Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog) {
                                dialog = false;
                                alertDialog.show();
                                scanValue = barcodes.valueAt(0).displayValue;
                                if (scanValue.startsWith("https://spekteraigs.page.link/QR/")) {
                                    new AlertDialog.Builder(BarCodeScanner.this)
                                            .setMessage(scanValue)
                                            .setPositiveButton("OK", ((dialogInterface, i) -> {
                                                alertDialog.dismiss();
                                                dialog = true;
                                            })).setCancelable(false)
                                            .show();

                                } else {
                                    new AlertDialog.Builder(BarCodeScanner.this)
                                            .setMessage("Not a valid Spekter events QR Code!!")
                                            .setPositiveButton("OK", ((dialogInterface, i) -> {
                                                alertDialog.dismiss();
                                                dialog = true;
                                            })).setCancelable(false)
                                            .show();
                                }

                            }
                        }
                    });

                    Log.d("TAG", barcodes.valueAt(0).rawValue);
                }
            }
        });
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onPause() {
        super.onPause();
        flashStatus = false;
        flashLight.setImageDrawable(getDrawable(R.drawable.outline_flashlight_off_24));
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}