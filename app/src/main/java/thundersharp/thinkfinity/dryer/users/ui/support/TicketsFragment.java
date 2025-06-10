package thundersharp.thinkfinity.dryer.users.ui.support;

import static thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.List;
import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.adapters.TicketAdapter;
import thundersharp.thinkfinity.dryer.users.core.model.SupportTicket;
import thundersharp.thinkfinity.dryer.users.core.model.SupportTicketServer;

public class TicketsFragment extends Fragment {

    private RecyclerView ticketsRecyclerView;
    private CircularProgressIndicator loadingSpinner;

    public TicketsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);
        StorageHelper storageHelper = StorageHelper.getInstance(requireContext()).initUserJWTDataStorage();
        String token = storageHelper.getRawToken();

        if (token == null) {
            Toast.makeText(requireContext(), "Token is null", Toast.LENGTH_SHORT).show();
            return view;
        }

        ticketsRecyclerView = view.findViewById(R.id.ticketsRecyclerView);
        loadingSpinner = view.findViewById(R.id.loadingSpinner);

        fetchTickets(token);

        return view;
    }

    private void fetchTickets(String token) {
        loadingSpinner.setVisibility(View.VISIBLE);
        String url = HOST_BASE_ADDR_WITH_PORT + "/api/v1/support/tickets/" + token;

        ApiUtils.getInstance(requireContext()).fetchData(url, SupportTicketServer.class, new ApiUtils.ApiResponseCallback<List<SupportTicketServer>>() {
            @Override
            public void onSuccess(List<SupportTicketServer> result) {
                loadingSpinner.setVisibility(View.GONE);
                TicketAdapter adapter = new TicketAdapter(result, getContext());
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