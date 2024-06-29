package thundersharp.thinkfinity.dryer.users.core.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.boot.utils.TimeUtils;
import thundersharp.thinkfinity.dryer.users.core.model.LogModel;

public class LogsViewer extends RecyclerView.Adapter<LogsViewer.ViewHolder> implements Filterable {

    List<LogModel> data;

    public LogsViewer(List<LogModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log_viewer, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogModel device = data.get(position);

        try {
            holder.date.setText(device.getTimestamp());
            holder.message.setText(device.getMessage());
            holder.level_one.setText(device.getLog_level().substring(0, 1));
            holder.level.setText("Log Level: " + device.getLog_level());

            if (device.getLog_level().equalsIgnoreCase("INFO")) {
                holder.rec.setBackgroundColor(holder.itemView.getResources().getColor(R.color.logLevelInfo));
                holder.level.setTextColor(holder.itemView.getResources().getColor(R.color.logLevelInfo));

            } else if (device.getLog_level().equalsIgnoreCase("WARN")) {
                holder.rec.setBackgroundColor(holder.itemView.getResources().getColor(R.color.logLevelWarning));
                holder.level.setTextColor(holder.itemView.getResources().getColor(R.color.logLevelWarning));

            } else if (device.getLog_level().equalsIgnoreCase("ERROR")) {
                holder.rec.setBackgroundColor(holder.itemView.getResources().getColor(R.color.logLevelError));
                holder.level.setTextColor(holder.itemView.getResources().getColor(R.color.logLevelError));

            }
        }catch (Exception e){
            ThinkfinityUtils.createErrorMessage(holder.itemView.getContext(),e.getMessage()).show();
        }
    }

    @Override
    public int getItemCount() {
        if (data != null) return data.size(); else return 0;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date, message, level, level_one;
        private RelativeLayout rec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            message = itemView.findViewById(R.id.message);
            level = itemView.findViewById(R.id.log_level);
            level_one = itemView.findViewById(R.id.log_lev_on);
            rec = itemView.findViewById(R.id.bg);
        }

    }

}
