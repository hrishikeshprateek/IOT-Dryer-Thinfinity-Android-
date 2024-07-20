package thundersharp.thinkfinity.dryer.users.core.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

import thundersharp.thinkfinity.dryer.R;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {
    private List<String> timeline;

    public TimelineAdapter(List<String> timeline) {
        this.timeline = timeline;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_event, parent, false);
        return new TimelineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        holder.timelineEvent.setText(timeline.get(position));
    }

    @Override
    public int getItemCount() {
        return timeline.size();
    }

    static class TimelineViewHolder extends RecyclerView.ViewHolder {
        TextView timelineEvent;
        TimelineView timeline;

        TimelineViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineEvent = itemView.findViewById(R.id.timelineEvent);
            timeline = itemView.findViewById(R.id.timeline);
            timeline.initLine(viewType);
        }
    }
}