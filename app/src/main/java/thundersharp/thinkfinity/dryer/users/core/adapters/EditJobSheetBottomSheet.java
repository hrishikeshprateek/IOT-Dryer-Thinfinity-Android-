package thundersharp.thinkfinity.dryer.users.core.adapters;

import static thundersharp.thinkfinity.dryer.users.UsersHome.viewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.model.Device;
import thundersharp.thinkfinity.dryer.users.core.model.JobSheetModel;
import thundersharp.thinkfinity.dryer.users.core.model.StdResponseModel;

public class EditJobSheetBottomSheet extends BottomSheetDialogFragment {

    private TextInputEditText etRecipeId, etRecipeName, etTemperature, etHumidity, etTime;
    private RadioGroup rgAction;
    private RadioButton rbActionYes, rbActionNo;
    private AutoCompleteTextView customerAutoTV;
    private int selectedIndex = 0;
    private List<Device> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_edit_job, container, false);

        customerAutoTV = view.findViewById(R.id.customerTextView);
        etRecipeId = view.findViewById(R.id.et_recipe_id);
        etRecipeName = view.findViewById(R.id.et_recipe_name);
        etTemperature = view.findViewById(R.id.et_temperature);
        etHumidity = view.findViewById(R.id.et_humidity);
        etTime = view.findViewById(R.id.et_time);
        rgAction = view.findViewById(R.id.rg_action);
        rbActionYes = view.findViewById(R.id.rb_action_yes);
        rbActionNo = view.findViewById(R.id.rb_action_no);
        AppCompatButton btnSave = view.findViewById(R.id.btn_save);
        AlertDialog dialog = ThinkfinityUtils.createDefaultProgressBar(requireActivity());

        btnSave.setOnClickListener(v -> {
            try {
                dialog.show();
                if (validateFields()) {
                    String deviceId = data.get(getItemSelectedPos()).getId();
                    String recipeId = Objects.requireNonNull(etRecipeId.getText()).toString();
                    String recipeName = Objects.requireNonNull(etRecipeName.getText()).toString();
                    double temperature = Double.parseDouble(Objects.requireNonNull(etTemperature.getText()).toString());
                    int humidity = Integer.parseInt(Objects.requireNonNull(etHumidity.getText()).toString());
                    double time = Double.parseDouble(Objects.requireNonNull(etTime.getText()).toString());
                    boolean action = ((RadioButton) view.findViewById(rgAction.getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase("Immediate Effect");

                    JobSheetModel jobSheet = new JobSheetModel(deviceId, recipeId, recipeName, temperature, humidity, time, action);

                    String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT + "/api/v1/mqtt/jobsheet/publish";
                    ApiUtils.getInstance(requireActivity())
                            .postRawJsonWithAuthHeaderToken(url, jobSheet.toJson(), StdResponseModel.class, new ApiUtils.ApiResponseCallback<StdResponseModel>() {
                                @Override
                                public void onSuccess(StdResponseModel result) {
                                    Toast.makeText(requireActivity(), "Data Successfully Published !!",Toast.LENGTH_LONG).show();
                                    dismiss(); // Close the bottom sheet
                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    ThinkfinityUtils.createErrorMessage(requireActivity(), errorMessage).show();
                                    dialog.dismiss();
                                }
                            });


                }
            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        });

        ArrayList<String> customerList = new ArrayList<>();
        for (Device dataInd : data) {
            customerList.add(dataInd.getId() + " (" + dataInd.getDevice_name() + ")");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, customerList);
        customerAutoTV.setAdapter(adapter);
        customerAutoTV.setOnItemClickListener((parent, viewR, position, id) -> {
            selectedIndex = (position + 1);
        });
        view.findViewById(R.id.btn_history).setOnClickListener(o -> {
            viewPager.setCurrentItem(3);
            dismiss();
        });
        return view;
    }

    private boolean validateFields() {
        if (customerAutoTV.getText().toString().isEmpty()) {
            customerAutoTV.setError("Please select a device");
            return false;
        }
        if (Objects.requireNonNull(etRecipeId.getText()).toString().isEmpty()) {
            etRecipeId.setError("Please enter recipe ID");
            return false;
        }
        if (Objects.requireNonNull(etRecipeName.getText()).toString().isEmpty()) {
            etRecipeName.setError("Please enter recipe name");
            return false;
        }
        if (Objects.requireNonNull(etTemperature.getText()).toString().isEmpty()) {
            etTemperature.setError("Please enter temperature");
            return false;
        }
        if (Objects.requireNonNull(etHumidity.getText()).toString().isEmpty()) {
            etHumidity.setError("Please enter humidity");
            return false;
        }
        if (Objects.requireNonNull(etTime.getText()).toString().isEmpty()) {
            etTime.setError("Please enter time");
            return false;
        }
        if (rgAction.getCheckedRadioButtonId() == -1) {
            Toast.makeText(requireActivity(), "Please select an action", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public int getItemSelectedPos() {
        return selectedIndex;
    }

    public void setDeviceData(List<Device> data) {
        this.data = data;
    }
}