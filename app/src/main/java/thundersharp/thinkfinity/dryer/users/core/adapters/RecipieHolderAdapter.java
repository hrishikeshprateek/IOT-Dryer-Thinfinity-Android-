package thundersharp.thinkfinity.dryer.users.core.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.users.core.model.PublicRecipe;

public class RecipieHolderAdapter extends RecyclerView.Adapter<RecipieHolderAdapter.ViewHolder> {

    private List<PublicRecipe> recipeData;
    private List<PublicRecipe> filteredRecipeData;

    public RecipieHolderAdapter(List<PublicRecipe> recipeData) {
        this.recipeData = recipeData;
        this.filteredRecipeData = new ArrayList<>(recipeData);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<PublicRecipe> newRecipeData) {
        this.recipeData = newRecipeData;
        filter("");
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        filteredRecipeData.clear();
        for (PublicRecipe recipe : recipeData) {
            if (recipe.getRecipe_name().toLowerCase().contains(query.toLowerCase()) ||
                    String.valueOf(recipe.getRecipe_temperature()).toLowerCase().contains(query.toLowerCase()) ||
                    String.valueOf(recipe.getRecipe_humidity()).toLowerCase().contains(query.toLowerCase()) ||
                    recipe.getRecipe_author().toLowerCase().contains(query.toLowerCase())) {
                filteredRecipeData.add(recipe);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_holder, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PublicRecipe recipe = filteredRecipeData.get(position);

        holder.textRecipeName.setText(recipe.getRecipe_name());
        holder.humidity.setText(recipe.getRecipe_humidity() + " %");
        holder.temp.setText(recipe.getRecipe_temperature() + " C");
        holder.textRecipeDates.setText(recipe.getCreated_at());
        holder.textRecipeDescription.setText(recipe.getRecipe_description());
        holder.time.setText(recipe.getRecipe_time() + " Hrs");
        holder.textRecipeModification.setText(recipe.getUpdated_at());
        holder.textViewAuthor.setText(recipe.getRecipe_author());
    }

    @Override
    public int getItemCount() {
        return filteredRecipeData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textRecipeName;
        private final TextView textRecipeDescription;
        private final TextView temp;
        private final TextView humidity;
        private final TextView time;
        private final TextView textRecipeDates;
        private final TextView textRecipeModification;
        private final TextView textViewAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textRecipeName = itemView.findViewById(R.id.text_recipe_name);
            textRecipeDescription = itemView.findViewById(R.id.text_recipe_description);
            temp = itemView.findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humidity);
            time = itemView.findViewById(R.id.time);
            textRecipeDates = itemView.findViewById(R.id.text_recipe_dates);
            textRecipeModification = itemView.findViewById(R.id.text_recipe_modification);
            textViewAuthor = itemView.findViewById(R.id.text_recipe_author);
        }
    }
}