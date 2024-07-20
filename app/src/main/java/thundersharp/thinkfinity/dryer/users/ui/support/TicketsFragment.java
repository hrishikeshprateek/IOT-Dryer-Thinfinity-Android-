package thundersharp.thinkfinity.dryer.users.ui.support;

import static thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;
import java.util.Map;

import thundersharp.thinkfinity.dryer.JSONUtils;
import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.adapters.TicketAdapter;
import thundersharp.thinkfinity.dryer.users.core.model.SupportTicket;

public class TicketsFragment extends Fragment {

    private RecyclerView ticketsRecyclerView;
    private CircularProgressIndicator loadingSpinner;
    private RequestQueue requestQueue;
    private Map<String, Object> decodedToken;
    private StorageHelper storageHelper;

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);

        requestQueue = Volley.newRequestQueue(requireContext());
        storageHelper = StorageHelper.getInstance(requireContext()).initUserJWTDataStorage();
        String token = storageHelper.getRawToken();

        if (token == null) {
            Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show();
            return view;
        }

        decodedToken = JSONUtils.extractClaimsFromToken(token);

        ticketsRecyclerView = view.findViewById(R.id.ticketsRecyclerView);
        loadingSpinner = view.findViewById(R.id.loadingSpinner);

        fetchTickets(token);

        return view;
    }

    private void fetchTickets(String token) {
        loadingSpinner.setVisibility(View.VISIBLE);
        String url = HOST_BASE_ADDR_WITH_PORT + "/api/v1/support/tickets/" + token;

        ApiUtils.getInstance(requireContext()).fetchData(url, SupportTicket.class, new ApiUtils.ApiResponseCallback<List<SupportTicket>>() {
            @Override
            public void onSuccess(List<SupportTicket> result) {
                loadingSpinner.setVisibility(View.GONE);
                TicketAdapter adapter = new TicketAdapter(result, TicketsFragment.this::showTicketDetails);
                ticketsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                ticketsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                loadingSpinner.setVisibility(View.GONE);
                ThinkfinityUtils.createErrorMessage(requireContext(), errorMessage).show();
            }
        });
    }

    private void showTicketDetails(SupportTicket ticket) {
        Toast.makeText(requireContext(), "The status of the current ticket " + ticket.getDeviceID() + " is " + ticket.getStatus(), Toast.LENGTH_SHORT).show();
    }
}