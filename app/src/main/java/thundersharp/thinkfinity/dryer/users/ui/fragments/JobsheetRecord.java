package thundersharp.thinkfinity.dryer.users.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.adapters.JobsheetViewer;
import thundersharp.thinkfinity.dryer.users.core.model.JobSheetData;

public class JobsheetRecord extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_jobsheet_record, container, false);

        DeviceConfig deviceConfig = DeviceConfig.getDeviceConfig(requireActivity()).initializeStorage();
        if (deviceConfig.getCurrentDevice() == null){
            Toast.makeText(requireActivity(), "Select device first !!!", Toast.LENGTH_SHORT).show();
        }else {
            loadData(deviceConfig);
        }

        return view;
    }

    private void loadData(DeviceConfig deviceConfig){
        String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT+"/api/vi/device/get/all/jobSheetRecords/"+deviceConfig.getCurrentDevice().getId();
        ApiUtils
                .getInstance(requireActivity())
                .fetchDataWithAuthHeaderToken(url, JobSheetData.class, new ApiUtils.ApiResponseCallback<List<JobSheetData>>() {
                    @Override
                    public void onSuccess(List<JobSheetData> result) {

                        ((RecyclerView) requireActivity().findViewById(R.id.recycler_job)).setAdapter(new JobsheetViewer(result));
                    }

                    @Override
                    public void onError(String errorMessage) {
                        ThinkfinityUtils.createErrorMessage(getActivity(), errorMessage).show();
                    }
                });
    }
}