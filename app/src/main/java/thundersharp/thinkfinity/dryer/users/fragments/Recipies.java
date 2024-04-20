package thundersharp.thinkfinity.dryer.users.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.SpringServerHelper;
import thundersharp.thinkfinity.dryer.users.core.adapters.RecipieHolderAdapter;
import thundersharp.thinkfinity.dryer.users.core.interfaces.OnServerEvents;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recipies} factory method to
 * create an instance of this fragment.
 */
public class Recipies extends Fragment {

    EditText search_bar_edit_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipies, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rec);
        search_bar_edit_text = view.findViewById(R.id.search_bar_edit_text);

        ImageButton search_bar_voice_icon = view.findViewById(R.id.search_bar_voice_icon);
        search_bar_voice_icon.setOnClickListener(v->{

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search for recipes");

            try {
                startActivityForResult(intent, 1);
            } catch (Exception exception) {
                Toast.makeText(view.getContext(), "EXCEPTION: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


        SpringServerHelper
                .getInstance(view.getContext())
                .getPublicRecipes(10, new OnServerEvents() {
                    @Override
                    public void onQuerySuccessful(JSONObject response) {
                        try {
                            JSONArray dataExtracted = response.getJSONArray("data");
                            recyclerView.setAdapter(new RecipieHolderAdapter(dataExtracted));
                        } catch (JSONException e) {
                            ThinkfinityUtils.createErrorMessage(view.getContext(),e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onQueryFailure(Exception e) {
                        ThinkfinityUtils.createErrorMessage(view.getContext(),e.getMessage());
                    }
                });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                search_bar_edit_text.setText(Objects.requireNonNull(result).get(0));
            }
        }

    }
}