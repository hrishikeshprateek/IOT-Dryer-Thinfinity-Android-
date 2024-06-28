package thundersharp.thinkfinity.dryer.users.ui.support;

import static thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT;
import static thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils.signOut;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.progressindicator.CircularProgressIndicator;
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
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.adapters.TicketAdapter;
import thundersharp.thinkfinity.dryer.users.core.model.Device;
import thundersharp.thinkfinity.dryer.users.core.model.SupportTicket;

public class SupportHome extends AppCompatActivity {

    private TextInputEditText customerName, contactNumber, supportTitle, alternateContact, additionalDescription;
    private Spinner deviceSpinner, supportTypeSpinner;
    private MaterialCheckBox escalateToOem, includeDeviceLogs;
    private MaterialButton createTicketButton;
    private RecyclerView ticketsRecyclerView;
    private CircularProgressIndicator loadingSpinner;
    private List<Device> activeDeviceData = new ArrayList<>();
    private List<SupportTicket> tickets = new ArrayList<>();
    private Map<String, Object> decodedToken;
    private RequestQueue requestQueue;
    private StorageHelper storageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_home);

        requestQueue = Volley.newRequestQueue(this);
        storageHelper = StorageHelper.getInstance(this).initUserJWTDataStorage();
        String token = storageHelper.getRawToken();

        Toast.makeText(this, "Token is "+token, Toast.LENGTH_SHORT).show();
        if (token == null) {
            Toast.makeText(this, "Token is null "+token, Toast.LENGTH_SHORT).show();
            return;
        }

        decodedToken = JSONUtils.extractClaimsFromToken(token);

        customerName = findViewById(R.id.customerName);
        contactNumber = findViewById(R.id.contactNumber);
        supportTitle = findViewById(R.id.supportTitle);
        alternateContact = findViewById(R.id.alternateContact);
        additionalDescription = findViewById(R.id.additionalDescription);

        deviceSpinner = findViewById(R.id.deviceSpinner);
        supportTypeSpinner = findViewById(R.id.supportTypeSpinner);

        escalateToOem = findViewById(R.id.escalateToOem);
        includeDeviceLogs = findViewById(R.id.includeDeviceLogs);

        createTicketButton = findViewById(R.id.createTicketButton);
        ticketsRecyclerView = findViewById(R.id.ticketsRecyclerView);
        loadingSpinner = findViewById(R.id.loadingSpinner);

        customerName.setText((String) decodedToken.get(ThinkfinityUtils.USER_NAME_JWT));
        contactNumber.setText((String) decodedToken.get(ThinkfinityUtils.USER_PHONE_JWT));

        fetchDevices(token);
        fetchTickets(token);

        createTicketButton.setOnClickListener(v -> createSupportTicket(token));

    }
    private void handleTokenExpiration() {
        Toast.makeText(this, "Your session has expired. Please log in again.", Toast.LENGTH_SHORT).show();
        signOut(this);
    }

    private void fetchDevices(String token) {
        String url = HOST_BASE_ADDR_WITH_PORT + "/api/v1/user/get/user/device/status/" + token;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject deviceObject = response.getJSONObject(i);
                            Device device = new Gson().fromJson(deviceObject.toString(), Device.class);
                            activeDeviceData.add(device);
                        }
                        ArrayAdapter<Device> adapter = new ArrayAdapter<>(SupportHome.this, android.R.layout.simple_spinner_item, activeDeviceData);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        deviceSpinner.setAdapter(adapter);
                    } catch (JSONException e) {
                        Log.e("API Error", "Error parsing device data", e);
                    }
                },
                error -> handleTokenExpiration()
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void fetchTickets(String token) {
        loadingSpinner.setVisibility(View.VISIBLE);
        String url = HOST_BASE_ADDR_WITH_PORT + "/api/v1/support/tickets/" + token;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    loadingSpinner.setVisibility(View.GONE);
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject ticketObject = response.getJSONObject(i);
                            SupportTicket ticket = new Gson().fromJson(ticketObject.toString(), SupportTicket.class);
                            tickets.add(ticket);
                        }
                        TicketAdapter adapter = new TicketAdapter(tickets, SupportHome.this::showTicketDetails);
                        ticketsRecyclerView.setLayoutManager(new LinearLayoutManager(SupportHome.this));
                        ticketsRecyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(SupportHome.this, "Failed to fetch tickets", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    loadingSpinner.setVisibility(View.GONE);
                    Log.e("API Error", "Error fetching tickets", error);
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void createSupportTicket(String token) {
        SupportTicket newSupportTicket = new SupportTicket(
                (String) decodedToken.get(ThinkfinityUtils.USER_UID_JWT),
                Objects.requireNonNull(customerName.getText()).toString(),
                escalateToOem.isChecked(),
                includeDeviceLogs.isChecked(),
                Objects.requireNonNull(contactNumber.getText()).toString(),
                System.currentTimeMillis(),
                ((Device) deviceSpinner.getSelectedItem()).getId(),
                Objects.requireNonNull(supportTitle.getText()).toString(),
                supportTypeSpinner.getSelectedItem().toString(),
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                response -> {
                    Toast.makeText(SupportHome.this, "Support ticket created successfully", Toast.LENGTH_SHORT).show();
                    fetchTickets(token);
                },
                error -> {
                    Log.e("API Error", "Error creating ticket", error);
                    Toast.makeText(SupportHome.this, "Failed to create support ticket", Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void showTicketDetails(SupportTicket ticket) {
        Toast.makeText(this, "The status of the current ticket " + ticket.getDeviceID() + " is " + ticket.getStatus(), Toast.LENGTH_SHORT).show();
    }

    private String jwtDecode(String token) {
        // Implement your JWT decode logic here
        return "{}";
    }

}