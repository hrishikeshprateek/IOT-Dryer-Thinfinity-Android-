package thundersharp.thinkfinity.dryer.users.ui.support;

import static thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import thundersharp.thinkfinity.dryer.JSONUtils;
import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.model.Device;
import thundersharp.thinkfinity.dryer.users.core.model.StdResponseModel;
import thundersharp.thinkfinity.dryer.users.core.model.SupportTicket;

public class FormFragment extends Fragment {

    private TextInputEditText customerName, contactNumber, supportTitle, alternateContact, additionalDescription;
    private AutoCompleteTextView customerAutoTV, supportTypeSpinner;
    private MaterialCheckBox escalateToOem, includeDeviceLogs;
    private MaterialButton createTicketButton;
    private RequestQueue requestQueue;
    private Map<String, Object> decodedToken;
    private List<Device> activeDeviceData;
    private StorageHelper storageHelper;
    private int selectedIndex = 0;

    List<String> support_ticket_type = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        requestQueue = Volley.newRequestQueue(requireContext());
        storageHelper = StorageHelper.getInstance(requireContext()).initUserJWTDataStorage();
        String token = storageHelper.getRawToken();

        if (token == null) {
            Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show();
            return view;
        }

        decodedToken = JSONUtils.extractClaimsFromToken(token);

        customerName = view.findViewById(R.id.customerName);
        contactNumber = view.findViewById(R.id.contactNumber);
        supportTitle = view.findViewById(R.id.supportTitle);
        alternateContact = view.findViewById(R.id.alternateContact);
        additionalDescription = view.findViewById(R.id.additionalDescription);

        customerAutoTV = view.findViewById(R.id.deviceSpinner);
        supportTypeSpinner = view.findViewById(R.id.deviceSpinnerT);

        support_ticket_type.add("Technical");
        support_ticket_type.add("Billing");
        support_ticket_type.add("General");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, support_ticket_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supportTypeSpinner.setAdapter(adapter);


        escalateToOem = view.findViewById(R.id.escalateToOem);
        includeDeviceLogs = view.findViewById(R.id.includeDeviceLogs);

        createTicketButton = view.findViewById(R.id.createTicketButton);

        customerName.setText((String) storageHelper.getStoredJWTData().getName());
        contactNumber.setText((String) decodedToken.get("phone_number"));

        fetchDevices(token);

        createTicketButton.setOnClickListener(v -> createSupportTicket(token));

        return view;
    }
    private void fetchDevices(String token) {
        String url = HOST_BASE_ADDR_WITH_PORT + "/api/v1/user/get/user/device/status/" + token;
        ApiUtils.getInstance(requireContext()).fetchData(url, Device.class, new ApiUtils.ApiResponseCallback<List<Device>>() {
            @Override
            public void onSuccess(List<Device> result) {
                activeDeviceData = result;
                List<String> data = new ArrayList<>();

                for (Device un : result){
                    data.add(un.getId()+" ("+un.getDevice_name()+")");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                customerAutoTV.setAdapter(adapter);
                customerAutoTV.setOnItemClickListener((parent, viewR, position, id) -> {
                    selectedIndex = (position + 1);
                });
            }

            @Override
            public void onError(String errorMessage) {
                ThinkfinityUtils.createErrorMessage(requireContext(), errorMessage).show();
            }
        });
    }

    private boolean validateFields() {
        // Check if all fields are filled
        if (customerName.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Customer Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (contactNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Contact Number is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (supportTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Support Title is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (supportTypeSpinner.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Support Type is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (alternateContact.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Alternate Contact is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (additionalDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Additional Description is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedIndex < 0 || selectedIndex >= activeDeviceData.size()) {
            Toast.makeText(requireContext(), "Please select a valid device", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createSupportTicket(String token) {
        if (!validateFields()) {
            return;
        }

        SupportTicket newSupportTicket = new SupportTicket(
                (String) decodedToken.get(ThinkfinityUtils.USER_UID_JWT),
                Objects.requireNonNull(customerName.getText()).toString(),
                escalateToOem.isChecked(),
                includeDeviceLogs.isChecked(),
                Objects.requireNonNull(contactNumber.getText()).toString(),
                System.currentTimeMillis(),
                activeDeviceData.get(selectedIndex).getId(),
                Objects.requireNonNull(supportTitle.getText()).toString(),
                supportTypeSpinner.getText().toString(),
                Objects.requireNonNull(alternateContact.getText()).toString(),
                Objects.requireNonNull(additionalDescription.getText()).toString(),
                "Created"
        );

        String url = HOST_BASE_ADDR_WITH_PORT + "/api/v1/support/new/create";
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject(new Gson().toJson(newSupportTicket));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiUtils
                .getInstance(requireActivity())
                .postRawJsonWithAuthHeaderToken(url, jsonRequest, StdResponseModel.class, new ApiUtils.ApiResponseCallback<StdResponseModel>() {
                    @Override
                    public void onSuccess(StdResponseModel result) {
                        ThinkfinityUtils.createErrorMessage(requireActivity(),"Support ticket created successfully").show();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(requireContext(), "Failed to create support ticket "+errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}