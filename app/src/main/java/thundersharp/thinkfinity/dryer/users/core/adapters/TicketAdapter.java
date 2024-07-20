package thundersharp.thinkfinity.dryer.users.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.users.core.model.SupportTicket;
import thundersharp.thinkfinity.dryer.users.core.model.SupportTicketServer;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<SupportTicketServer> tickets;
    private Context context;

    public TicketAdapter(List<SupportTicketServer> tickets, Context context) {
        this.tickets = tickets;
        this.context = context;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ticket_item, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        SupportTicketServer ticket = tickets.get(position);
        holder.ticketTitle.setText(ticket.getSupportTittle());
        holder.ticketDetails.setText(context.getString(R.string.ticket_details, ticket.getId(), ticket.getSupportType(), ticket.getDescription(), ticket.isCheckDeviceLogs() ? "The device logs will also be analysed" : "No device logs will be analysed", ticket.getAlternateContact(), ticket.getStatus()));

        if (ticket.getTimeline() != null && !ticket.getTimeline().isEmpty()) {
            holder.timelineRecyclerView.setVisibility(View.VISIBLE);
            TimelineAdapter timelineAdapter = new TimelineAdapter(ticket.getTimeline());
            holder.timelineRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.timelineRecyclerView.setAdapter(timelineAdapter);
        } else {
            holder.timelineRecyclerView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            // Handle item click and show ticket details
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView ticketTitle, ticketDetails;
        RecyclerView timelineRecyclerView;

        TicketViewHolder(View itemView) {
            super(itemView);
            ticketTitle = itemView.findViewById(R.id.ticketTitle);
            ticketDetails = itemView.findViewById(R.id.ticketDetails);
            timelineRecyclerView = itemView.findViewById(R.id.timelineRecyclerView);
        }
    }
}