package thundersharp.thinkfinity.dryer.users.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.DefaultSliderView;

import java.util.Arrays;
import java.util.List;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.ui.support.SupportHome;

public class Devicedashboard extends Fragment {

    //GET http://localhost:80{{auth}}
    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_devicedashboard, container, false);

        SliderLayout slider = view.findViewById(R.id.slider);

        List<String> listUrl = Arrays.asList(
                "https://aqozone.com/wp-content/uploads/2022/11/vbbbb.webp",
                "https://aqozone.com/wp-content/uploads/2022/11/gbh.jpg",
                "https://aqozone.com/wp-content/uploads/2022/11/vbnnn.webp",
                "https://aqozone.com/wp-content/uploads/2022/11/gbb.jpg");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();

        for (int i = 0; i < listUrl.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(getActivity());
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(listUrl.get(i))
                    .description(null)
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(null);

            //add your extra information
            sliderView.bundle(new Bundle());

            slider.addSlider(sliderView);
        }

        slider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);

        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.stopCyclingWhenTouch(false);

        view.findViewById(R.id.support_center).setOnClickListener(t->startActivity(new Intent(getActivity(), SupportHome.class)));

        syncData();
        return view;
    }

    private void syncData(){
        String uri = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT+
                "/api/v1/user/get/user/dashboard/count/data/"+
                StorageHelper.getInstance(getContext()).initUserJWTDataStorage().getRawToken();
        ApiUtils
                .getInstance(getContext())
                .fetchDataNoList(uri, Devicedashboard.class, new ApiUtils.ApiResponseCallback<Devicedashboard>() {
                    @Override
                    public void onSuccess(Devicedashboard result) {
                        Toast.makeText(getContext(), result.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}