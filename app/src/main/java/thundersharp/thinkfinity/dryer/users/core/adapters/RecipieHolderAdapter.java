package thundersharp.thinkfinity.dryer.users.core.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import thundersharp.thinkfinity.dryer.R;

public class RecipieHolderAdapter extends RecyclerView.Adapter<RecipieHolderAdapter.ViewHolder> {

    JSONArray recipeData;

    public RecipieHolderAdapter(JSONArray recipeData) {
        this.recipeData = recipeData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_holder,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject recipe = recipeData.getJSONObject(position);

            holder.text_recipe_name.setText(recipe.getString("recipe_name"));
            holder.humidity.setText(recipe.getInt("recipe_humidity")+" %");
            holder.temp.setText(recipe.getDouble("recipe_temperature")+" C");
            holder.text_recipe_dates.setText(recipe.getString("created_at"));
            holder.text_recipe_description.setText(recipe.getString("recipe_description"));
            holder.time.setText(recipe.getDouble("recipe_time")+" Hrs");
            holder.text_recipe_modification.setText(recipe.getString("updated_at"));
            holder.textView_author.setText(recipe.getString("recipe_author"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return recipeData.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text_recipe_name;
        private TextView text_recipe_description;
        private TextView temp;
        private TextView humidity;
        private TextView time;
        private TextView text_recipe_dates;
        private TextView text_recipe_modification;
        private TextView textView_author;
        private AppCompatButton edit;
        private AppCompatButton send_to_device;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_recipe_name = itemView.findViewById(R.id.text_recipe_name);
            text_recipe_description = itemView.findViewById(R.id.text_recipe_description);
            temp = itemView.findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humidity);
            time = itemView.findViewById(R.id.time);
            text_recipe_dates = itemView.findViewById(R.id.text_recipe_dates);
            text_recipe_modification = itemView.findViewById(R.id.text_recipe_modification);
            edit = itemView.findViewById(R.id.edit);
            send_to_device = itemView.findViewById(R.id.send_to_device);
            textView_author = itemView.findViewById(R.id.text_recipe_author);
        }
    }
}
