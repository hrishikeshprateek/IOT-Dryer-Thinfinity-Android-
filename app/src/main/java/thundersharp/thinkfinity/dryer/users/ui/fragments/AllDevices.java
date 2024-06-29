package thundersharp.thinkfinity.dryer.users.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.adapters.DeviceViwer;
import thundersharp.thinkfinity.dryer.users.core.model.Device;

public class AllDevices extends Fragment {

    private RecyclerView recyclerView;
    private StorageHelper storageHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_devices, container, false);
        storageHelper = StorageHelper.getInstance(getContext()).initUserJWTDataStorage();

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        fetchDeviceData();

        return view;
    }

    private void fetchDeviceData() {
        String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT+ "/api/v1/user/get/user/device/status/" + storageHelper.getRawToken();

        ApiUtils.getInstance(getContext())
                .fetchData(url, Device.class, new ApiUtils.ApiResponseCallback<List<Device>>() {
                    @Override
                    public void onSuccess(List<Device> result) {
                        recyclerView.setAdapter(new DeviceViwer(result));
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}