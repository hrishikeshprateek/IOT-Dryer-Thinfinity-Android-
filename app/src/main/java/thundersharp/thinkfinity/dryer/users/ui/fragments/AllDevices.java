package thundersharp.thinkfinity.dryer.users.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.adapters.DeviceViwer;
import thundersharp.thinkfinity.dryer.users.core.model.Device;

public class AllDevices extends Fragment {

    private RecyclerView recyclerView;
    private StorageHelper storageHelper;
    ExecutorService executorService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_devices, container, false);
        executorService = Executors.newCachedThreadPool();
        storageHelper = StorageHelper.getInstance(getContext()).initUserJWTDataStorage();

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        executorService.execute(this::fetchDeviceData);

        return view;
    }

    private void fetchDeviceData() {
        String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT+ "/api/v1/user/get/user/device/status/" + storageHelper.getRawToken();

        ApiUtils.getInstance(getContext())
                .fetchData(url, Device.class, new ApiUtils.ApiResponseCallback<List<Device>>() {
                    @Override
                    public void onSuccess(List<Device> result) {
                        requireActivity().runOnUiThread(() -> recyclerView.setAdapter(new DeviceViwer(result)));
                    }

                    @Override
                    public void onError(String errorMessage) {
                        requireActivity().runOnUiThread(() -> ThinkfinityUtils.createErrorMessage(getActivity(), errorMessage).show());
                    }
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