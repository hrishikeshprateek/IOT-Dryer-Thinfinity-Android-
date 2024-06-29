package thundersharp.thinkfinity.dryer.users.core.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
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

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.utils.TimeUtils;
import thundersharp.thinkfinity.dryer.users.core.model.Device;

public class DeviceViwer extends RecyclerView.Adapter<DeviceViwer.ViewHolder> implements Filterable {

    List<Device> data;

    public DeviceViwer(List<Device> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_holder, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Device device = data.get(position);

        holder.name.setText("Device Name: "+device.getDevice_name());
        holder.uuid.setText("Id: "+device.getId());
        holder.time.setText("Activated On"+ TimeUtils.getTimeFromTimeStamp(device.getSubscriptionActivatedOn()));
        holder.endsSub.setText("Expires on: "+TimeUtils.getTimeFromTimeStamp(device.getSubscriptionActiveTill()));

        holder.cardColor.setCardBackgroundColor(Color.parseColor(device.isEnabled()? "#5185ff" : "#F44336"));
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

        private final TextView name, uuid, time, endsSub;
        private final AppCompatButton action;
        private final CardView cardColor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            uuid = itemView.findViewById(R.id.uuid);
            time = itemView.findViewById(R.id.lastActive);
            action = itemView.findViewById(R.id.action);
            endsSub = itemView.findViewById(R.id.endsSub);
            cardColor = itemView.findViewById(R.id.cardColor);

            action.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(action.getContext())
                    .setMessage("Are you sure you want to change the active device ? This action requires a immediate restart for the changes to take affect !!")
                    .setCancelable(true)
                    .setTitle("Change to "+data.get(getAdapterPosition()).getDevice_name()+ " with Id "+data.get(getAdapterPosition()).getId())
                    .setNegativeButton("NO",(r,s) -> r.dismiss())
                    .setPositiveButton("CHANGE", (view,d) -> {
                        DeviceConfig
                                .getDeviceConfig(v.getContext())
                                .initializeStorage()
                                .setCurrentDevice(data.get(getAdapterPosition()));
                        //Home.onRestart.onRestartRequested();
                    })
                    .show();

        }
    }

}
