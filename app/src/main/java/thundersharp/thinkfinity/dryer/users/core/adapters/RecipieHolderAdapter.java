package thundersharp.thinkfinity.dryer.users.core.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.DeviceConfig;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.model.JobSheetModel;
import thundersharp.thinkfinity.dryer.users.core.model.PublicRecipe;
import thundersharp.thinkfinity.dryer.users.core.model.StdResponseModel;

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

        holder.send_to_device.setOnClickListener(g -> new AlertDialog.Builder(g.getContext())
                .setTitle("Confirm Send ?")
                .setTitle("Confirm to send this recipe to the current selected device ?")
                .setNegativeButton("NO", (d, r)->d.dismiss())
                .setPositiveButton("SEND", (e,d)->{
                    String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT+ "/api/v1/mqtt/jobsheet/publish";
                    JobSheetModel data = new JobSheetModel(DeviceConfig.getDeviceConfig(g.getContext()).initializeStorage().getCurrentDevice().getId(),
                            recipe.getRecipe_id(),
                            recipe.getRecipe_name(),
                            recipe.getRecipe_temperature(),
                            (int)recipe.getRecipe_humidity(),
                            recipe.getRecipe_time(),
                            true);
                    ApiUtils
                            .getInstance(g.getContext())
                            .postRawJsonWithAuthHeaderToken(url, data.toJson(), StdResponseModel.class, new ApiUtils.ApiResponseCallback<StdResponseModel>() {
                                @Override
                                public void onSuccess(StdResponseModel result) {
                                    if (result.isSuccess()){
                                        e.dismiss();
                                        ThinkfinityUtils.createErrorMessage(g.getContext(), "Sent the recipe successfully").show();
                                    }else {
                                        e.dismiss();
                                        ThinkfinityUtils.createErrorMessage(g.getContext(), "Failed : "+result.getMessage()).show();
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    ThinkfinityUtils.createErrorMessage(g.getContext(), errorMessage).show();
                                }
                            });
                })
                .show());
        holder.edit_n_send.setOnClickListener(t -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(t.getContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_edit_and_send);
            TextInputEditText etRecipeName, etAuthor, etTemperature, etHumidity, etTime, etDescription;
            Button btnSend, btnCancel;

            etRecipeName = bottomSheetDialog.findViewById(R.id.et_recipe_name);
            etAuthor = bottomSheetDialog.findViewById(R.id.et_author);
            etTemperature = bottomSheetDialog.findViewById(R.id.et_temperature);
            etHumidity = bottomSheetDialog.findViewById(R.id.et_humidity);
            etTime = bottomSheetDialog.findViewById(R.id.et_time);
            etDescription = bottomSheetDialog.findViewById(R.id.et_description);

            btnSend = bottomSheetDialog.findViewById(R.id.btn_send);
            btnCancel = bottomSheetDialog.findViewById(R.id.btn_cancel);

            // Set the data in the views
            if (etRecipeName != null) etRecipeName.setText(recipe.getRecipe_name());
            if (etAuthor != null) etAuthor.setText(recipe.getRecipe_author());
            if (etTemperature != null) etTemperature.setText(String.valueOf(recipe.getRecipe_temperature()));
            if (etHumidity != null) etHumidity.setText(String.valueOf(recipe.getRecipe_humidity()));
            if (etTime != null) etTime.setText(String.valueOf(recipe.getRecipe_time()));
            if (etDescription != null) etDescription.setText(recipe.getRecipe_description());

            if (btnSend != null) {
                btnSend.setOnClickListener(v -> {
                    JobSheetModel updatedData = new JobSheetModel(
                            DeviceConfig.getDeviceConfig(t.getContext()).initializeStorage().getCurrentDevice().getId(),
                            recipe.getRecipe_id(),
                            etRecipeName != null ? etRecipeName.getText().toString() : recipe.getRecipe_name(),
                            etTemperature != null ? Double.parseDouble(etTemperature.getText().toString()) : recipe.getRecipe_temperature(),
                            etHumidity != null ? (int) Double.parseDouble(etHumidity.getText().toString()) : (int) recipe.getRecipe_humidity(),
                            etTime != null ? Double.parseDouble(etTime.getText().toString()) : recipe.getRecipe_time(),
                            true
                    );

                    String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT + "/api/v1/mqtt/jobsheet/publish";
                    ApiUtils.getInstance(t.getContext())
                            .postRawJsonWithAuthHeaderToken(url, updatedData.toJson(), StdResponseModel.class, new ApiUtils.ApiResponseCallback<StdResponseModel>() {
                                @Override
                                public void onSuccess(StdResponseModel result) {
                                    if (result.isSuccess()) {
                                        bottomSheetDialog.dismiss();
                                        ThinkfinityUtils.createErrorMessage(t.getContext(), "Sent the recipe successfully").show();
                                    } else {
                                        bottomSheetDialog.dismiss();
                                        ThinkfinityUtils.createErrorMessage(t.getContext(), "Failed: " + result.getMessage()).show();
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    ThinkfinityUtils.createErrorMessage(t.getContext(), errorMessage).show();
                                }
                            });
                });
            }
            if (btnCancel != null) {
                btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
            }


            bottomSheetDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return filteredRecipeData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textRecipeName;
        private final TextView textRecipeDescription;
        private final TextView temp;
        private final TextView humidity;
        private final TextView time;
        private final TextView textRecipeDates;
        private final TextView textRecipeModification;
        private final TextView textViewAuthor;
        private final AppCompatButton send_to_device, edit_n_send;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            textRecipeName = itemView.findViewById(R.id.text_recipe_name);
            textRecipeDescription = itemView.findViewById(R.id.text_recipe_description);
            temp = itemView.findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humidity);
            time = itemView.findViewById(R.id.time);
            textRecipeDates = itemView.findViewById(R.id.text_recipe_dates);
            textRecipeModification = itemView.findViewById(R.id.text_recipe_modification);
            textViewAuthor = itemView.findViewById(R.id.text_recipe_author);
            send_to_device = itemView.findViewById(R.id.send_to_device);
            edit_n_send = itemView.findViewById(R.id.edit);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = LayoutInflater.from(v.getContext());
            View dialogView = inflater.inflate(R.layout.dialog_recipe_image,null, false);
            builder.setView(dialogView);

            Glide.with(v.getContext())
                    .load(filteredRecipeData.get(getAdapterPosition()).getRecipe_image())
                    .into((PhotoView) dialogView.findViewById(R.id.recipe_image));
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}