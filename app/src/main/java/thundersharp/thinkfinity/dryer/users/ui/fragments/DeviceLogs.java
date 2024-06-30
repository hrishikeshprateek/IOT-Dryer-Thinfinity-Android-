package thundersharp.thinkfinity.dryer.users.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.adapters.LogsViewer;
import thundersharp.thinkfinity.dryer.users.core.model.LogModel;


public class DeviceLogs extends Fragment {

    private ExecutorService executorService;
    private StorageHelper storageHelper;
    private DeviceConfig deviceConfig;
    private RecyclerView rec;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_logs, container, false);
        executorService = Executors.newSingleThreadExecutor();
        storageHelper = StorageHelper.getInstance(requireActivity()).initUserJWTDataStorage();
        deviceConfig = DeviceConfig.getDeviceConfig(requireActivity()).initializeStorage();

        rec = view.findViewById(R.id.rec);

        if (deviceConfig.getCurrentDevice() != null)
            fetchData(view);
        return view;
    }

    void fetchData(View view){
        executorService.execute(() -> {
            String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT+"/api/v1/logs/device/id/"+storageHelper.getRawToken()+"?deviceId="+deviceConfig.getCurrentDevice().getId();
            ApiUtils
                    .getInstance(requireActivity())
                    .fetchDataRawWithoutFormat(url, LogModel.class, new ApiUtils.ApiResponseCallback<List<LogModel>>() {
                        @Override
                        public void onSuccess(List<LogModel> result) {
                            rec.setAdapter(new LogsViewer(result));
                        }

                        @Override
                        public void onError(String errorMessage) {
                            requireActivity().runOnUiThread(() -> {
                                System.out.println(errorMessage);
                                Toast.makeText(requireActivity(), "ERROR : "+errorMessage, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
        });
    }
}