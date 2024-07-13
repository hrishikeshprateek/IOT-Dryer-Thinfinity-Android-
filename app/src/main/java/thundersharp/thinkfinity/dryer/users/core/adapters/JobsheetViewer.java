package thundersharp.thinkfinity.dryer.users.core.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.utils.TimeUtils;
import thundersharp.thinkfinity.dryer.users.UsersHome;
import thundersharp.thinkfinity.dryer.users.core.model.Device;
import thundersharp.thinkfinity.dryer.users.core.model.JobSheetData;

public class JobsheetViewer extends RecyclerView.Adapter<JobsheetViewer.ViewHolder> implements Filterable {

    List<JobSheetData> data;

    public JobsheetViewer(List<JobSheetData> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_jobshrrt_holder, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobSheetData device = data.get(position);

        holder.action.setText(device.isACTION() ? "Done" : "Halted");
        holder.time.setText(String.format(Locale.US, "%.1f", device.getTIME()));
        holder.humid.setText(String.format("%s", device.getHUMIDITY()));
        holder.name.setText(device.getRECIPE_NAME());
        holder.temp.setText(String.format("%s", device.getTEMPERATURE()));


        holder.action.setTextColor(Color.parseColor(device.isACTION() ? "#000000" : "#ffffff"));
        holder.time.setTextColor(Color.parseColor(device.isACTION() ? "#000000" : "#ffffff"));
        holder.humid.setTextColor(Color.parseColor(device.isACTION() ? "#000000" : "#ffffff"));
        holder.name.setTextColor(Color.parseColor(device.isACTION() ? "#000000" : "#ffffff"));
        holder.temp.setTextColor(Color.parseColor(device.isACTION() ? "#000000" : "#ffffff"));

        holder.cardColor.setCardBackgroundColor(Color.parseColor(device.isACTION() ? "#ffffff" : "#A11545"));
    }

    @Override
    public int getItemCount() {
        if (data != null) return data.size(); else return 0;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

     class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView name, temp, time, humid, action;
        private final CardView cardColor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            humid = itemView.findViewById(R.id.humid);
            time = itemView.findViewById(R.id.time);
            temp = itemView.findViewById(R.id.temp);
            action = itemView.findViewById(R.id.action);
            cardColor = itemView.findViewById(R.id.cardColor);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }

}
