package thundersharp.thinkfinity.dryer.users.core.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.users.core.model.SupportTicket;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<SupportTicket> tickets;
    private TicketClickListener ticketClickListener;

    public interface TicketClickListener {
        void onTicketClick(SupportTicket ticket);
    }

    public TicketAdapter(List<SupportTicket> tickets, TicketClickListener ticketClickListener) {
        this.tickets = tickets;
        this.ticketClickListener = ticketClickListener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        SupportTicket ticket = tickets.get(position);
        holder.ticketTitle.setText(ticket.getSupportTittle());
        holder.ticketDescription.setText(ticket.getDescription());
        holder.itemView.setOnClickListener(v -> ticketClickListener.onTicketClick(ticket));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        TextView ticketTitle, ticketDescription;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketTitle = itemView.findViewById(R.id.ticketTitle);
            ticketDescription = itemView.findViewById(R.id.ticketDescription);
        }
    }
}