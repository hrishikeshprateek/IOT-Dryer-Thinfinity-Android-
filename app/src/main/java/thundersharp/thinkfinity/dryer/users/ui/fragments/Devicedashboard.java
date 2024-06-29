package thundersharp.thinkfinity.dryer.users.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.DefaultSliderView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.model.UserDashbordData;
import thundersharp.thinkfinity.dryer.users.ui.support.SupportHome;

public class Devicedashboard extends Fragment {

    private ExecutorService executorService;

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_devicedashboard, container, false);

        executorService = Executors.newSingleThreadExecutor();

        setupSlider(view);
        view.findViewById(R.id.support_center).setOnClickListener(t -> startActivity(new Intent(getActivity(), SupportHome.class)));
        syncData(view);

        return view;
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
    }
}