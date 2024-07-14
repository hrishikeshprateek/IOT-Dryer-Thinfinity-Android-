package thundersharp.thinkfinity.dryer.users.ui.fragments;

import static thundersharp.thinkfinity.dryer.users.UsersHome.viewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.itangqi.waveloadingview.WaveLoadingView;
import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.Enums.BootMode;
import thundersharp.thinkfinity.dryer.boot.barcode.BarCodeScanner;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.boot.utils.TimeUtils;

import thundersharp.thinkfinity.dryer.users.UsersHome;
import thundersharp.thinkfinity.dryer.users.core.helpers.SSEClient;
import thundersharp.thinkfinity.dryer.users.core.model.Device;
import thundersharp.thinkfinity.dryer.users.core.model.RealTimeDeviceSSEData;
import thundersharp.thinkfinity.dryer.users.core.model.UserDashbordData;
import thundersharp.thinkfinity.dryer.users.ui.support.SupportHome;

public class Devicedashboard extends Fragment implements SSEClient.SSEListener{

    private ExecutorService executorService;
    private WaveLoadingView temperature, humidity;
    private SSEClient sseClient;
    private StorageHelper storageHelper;
    private Device deviceConfig;
    private TextView ip,boot_time, cooked_rec, act_inact;
    private LinearProgressIndicator progressIndicator;
    private TextView elapsedTimeTextView;
    private TextView remainingTimeTextView;
    private long totalDurationInMillis;
    private long currentTimeInMillis;

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devicedashboard, container, false);
        deviceConfig = DeviceConfig.getDeviceConfig(getContext()).initializeStorage().getCurrentDevice();
        storageHelper = StorageHelper.getInstance(getActivity()).initUserJWTDataStorage();
        executorService = Executors.newSingleThreadExecutor();

        boot_time = view.findViewById(R.id.boot_time);
        cooked_rec = view.findViewById(R.id.cokked_rec);
        act_inact = view.findViewById(R.id.act_inact);

        temperature = view.findViewById(R.id.stallsVisited);
        humidity = view.findViewById(R.id.projectsReviwed);
        ip = view.findViewById(R.id.ip);
        ip.setText(getIPAddress(true));

        setupSlider(view);
        view.findViewById(R.id.support_center).setOnClickListener(t -> startActivity(new Intent(getActivity(), SupportHome.class)));
        syncData(view);

        if (deviceConfig == null){
            new AlertDialog.Builder(requireActivity())
                    .setMessage("No default device selected, select a device from the list first to proceed further. If your device is not shown up yet contact your manager !!")
                    .setPositiveButton("SELECT", (e,r)-> viewPager.setCurrentItem(4))
                    .setNegativeButton("SCAN",(r,t) -> {
                        startActivity(new Intent(requireActivity(), BarCodeScanner.class).putExtra(ThinkfinityUtils.BOOT_MODE_FOR_SCANNER,
                                BootMode.BOOT_FOR_DEVICE_CHANGE));
                        r.dismiss();
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }else {
            ((TextView)view.findViewById(R.id.device_name)).setText(deviceConfig.getDevice_name());
            ((TextView)view.findViewById(R.id.expires_on)).setText(TimeUtils.getTimeFromTimeStamp(deviceConfig.getSubscriptionActiveTill()));
            startSSE();

            progressIndicator = view.findViewById(R.id.progressIndicator);
            elapsedTimeTextView = view.findViewById(R.id.elapsedTimeTextView);
            remainingTimeTextView = view.findViewById(R.id.remainingTimeTextView);

            totalDurationInMillis = deviceConfig.getSubscriptionActiveTill() - deviceConfig.getSubscriptionActivatedOn();
            currentTimeInMillis = System.currentTimeMillis() - deviceConfig.getSubscriptionActivatedOn();

            ((CardView) view.findViewById(R.id.devices)).setOnClickListener(o -> viewPager.setCurrentItem(4));
            ((CardView) view.findViewById(R.id.public_rec)).setOnClickListener(o -> viewPager.setCurrentItem(1));
            ((CardView) view.findViewById(R.id.private_rec)).setOnClickListener(o -> viewPager.setCurrentItem(1));

            startTimer();
        }


        return view;
    }

    private void startTimer() {
        long interval = 1000; // 1 second

        CountDownTimer timer = new CountDownTimer(totalDurationInMillis - currentTimeInMillis, interval) {

            @Override
            public void onTick(long millisUntilFinished) {
                currentTimeInMillis += interval;
                updateProgress();
            }

            @Override
            public void onFinish() {
                updateProgress();
            }
        };

        timer.start();
    }

    private void updateProgress() {
        int progress = (int) ((currentTimeInMillis * 100) / totalDurationInMillis);
        progressIndicator.setProgress(progress);

        long elapsedSeconds = currentTimeInMillis / 1000;
        long elapsedMinutes = elapsedSeconds / 60;
        long elapsedHours = elapsedMinutes / 60;

        long remainingSeconds = (totalDurationInMillis - currentTimeInMillis) / 1000;
        long remainingMinutes = remainingSeconds / 60;
        long remainingHours = remainingMinutes / 60;

        elapsedTimeTextView.setText(String.format(Locale.getDefault(), "%d Hrs %d Mins %d Secs",
                elapsedHours, elapsedMinutes % 60, elapsedSeconds % 60));
        remainingTimeTextView.setText(String.format(Locale.getDefault(), "%d Hrs %d Mins %d Secs",
                remainingHours, remainingMinutes % 60, remainingSeconds % 60));
    }

    private void startSSE() {
        sseClient = new SSEClient(requireActivity(),
                ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT+"/api/v1/firebase/path?path=/REALTIME_DEVICE_STAT/"+deviceConfig.getId()+"&auth="+storageHelper.getRawToken());
        sseClient.start(this);
    }

    private void setupSlider(View view) {
        SliderLayout slider = view.findViewById(R.id.slider);

        List<String> listUrl = Arrays.asList(
                "https://aqozone.com/wp-content/uploads/2022/11/vbbbb.webp",
                "https://aqozone.com/wp-content/uploads/2022/11/gbh.jpg",
                "https://aqozone.com/wp-content/uploads/2022/11/vbnnn.webp",
                "https://aqozone.com/wp-content/uploads/2022/11/gbb.jpg");

        RequestOptions requestOptions = new RequestOptions().centerCrop();

        for (String url : listUrl) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());

            // initialize SliderLayout
            sliderView
                    .image(url)
                    .description(null)
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(null);

            // add your extra information
            sliderView.bundle(new Bundle());

            slider.addSlider(sliderView);
        }

        slider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.stopCyclingWhenTouch(false);
    }

    private void syncData(View view) {
        executorService.execute(() -> {
            String uri = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT +
                    "/api/v1/user/get/user/dashboard/count/data/" +
                    StorageHelper.getInstance(getContext()).initUserJWTDataStorage().getRawToken();

            ApiUtils
                    .getInstance(getContext())
                    .fetchDataNoList(uri, UserDashbordData.class, new ApiUtils.ApiResponseCallback<UserDashbordData>() {
                        @Override
                        public void onSuccess(UserDashbordData result) {
                            requireActivity().runOnUiThread(() -> {
                                ((TextView) view.findViewById(R.id.yourDevices)).setText(String.valueOf(result.getYourDeviceCount()));
                                ((TextView) view.findViewById(R.id.public_recipes)).setText(String.valueOf(result.getPublicRecipeCount()));
                                ((TextView) view.findViewById(R.id.private_recipes)).setText(String.valueOf(result.getPrivateRecipesCount()));
                                ((TextView) view.findViewById(R.id.jobsheet_hist)).setText(String.valueOf(result.getJobSheetCount()));
                                act_inact.setText(String.format(Locale.US,"%d/%d", result.getActiveDeviceCount(), result.getInactiveDeviceCount()));
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Error Occurred: " + errorMessage, Toast.LENGTH_SHORT).show());
                        }
                    });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }

        if (sseClient != null) {
            sseClient.stop();
            sseClient = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start the SSE connection if not already started
        if (sseClient == null && deviceConfig != null) {
            startSSE();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop the SSE connection
        if (sseClient != null) {
            sseClient.stop();
            sseClient = null;
        }
    }

    @Override
    public void onMessage(String message) {
        try {
            RealTimeDeviceSSEData realTimeDeviceSSEData = new Gson().fromJson(new JSONObject(message).getJSONObject(deviceConfig.getId()).toString(), RealTimeDeviceSSEData.class);
            if (realTimeDeviceSSEData != null) {
                requireActivity().runOnUiThread(() -> {
                    boot_time.setText(TimeUtils.getTimeFromTimeStamp(realTimeDeviceSSEData.getLOG_TIME()));
                    temperature.setProgressValue(realTimeDeviceSSEData.getTEMP());
                    humidity.setProgressValue(realTimeDeviceSSEData.getHUMIDITY());

                    temperature.setCenterTitle(realTimeDeviceSSEData.getTEMP()+" C");
                    humidity.setCenterTitle(realTimeDeviceSSEData.getHUMIDITY()+" rh");
                });
            }
        }catch (Exception e){
            requireActivity().runOnUiThread(() -> ThinkfinityUtils.createErrorMessage(requireActivity(), e.getMessage()).show());
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable t) {
        //requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}