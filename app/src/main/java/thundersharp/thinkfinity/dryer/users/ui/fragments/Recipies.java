package thundersharp.thinkfinity.dryer.users.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import thundersharp.thinkfinity.dryer.R;
import thundersharp.thinkfinity.dryer.boot.ApiUtils;
import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.serverStat.BootServerUtil;
import thundersharp.thinkfinity.dryer.boot.utils.ThinkfinityUtils;
import thundersharp.thinkfinity.dryer.users.core.SpringServerHelper;
import thundersharp.thinkfinity.dryer.users.core.adapters.RecipieHolderAdapter;
import thundersharp.thinkfinity.dryer.users.core.interfaces.OnServerEvents;
import thundersharp.thinkfinity.dryer.users.core.model.PublicRecipe;
import thundersharp.thinkfinity.dryer.users.ui.activities.PrivateRecipes;

public class Recipies extends Fragment {

    private EditText search_bar_edit_text;
    private ExecutorService executorService;
    private RecipieHolderAdapter adapter;
    private RecyclerView recyclerView;
    private StorageHelper storageHelper;
    private LazyLoader lazyLoader;
    private RelativeLayout fab_private;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipies, container, false);
        executorService = Executors.newSingleThreadExecutor();
        storageHelper = StorageHelper.getInstance(requireActivity()).initUserJWTDataStorage();

        recyclerView = view.findViewById(R.id.rec);
        lazyLoader = view.findViewById(R.id.loader);
        fab_private = view.findViewById(R.id.fab_private);
        search_bar_edit_text = view.findViewById(R.id.search_bar_edit_text);

        ImageButton search_bar_voice_icon = view.findViewById(R.id.search_bar_voice_icon);
        search_bar_voice_icon.setOnClickListener(v -> {

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

        fab_private.setOnClickListener(r -> startActivity(new Intent(requireActivity(), PrivateRecipes.class)));


        setupSearchBar();
        fetchData(-1);
        return view;
    }


    private void setupSearchBar() {
        search_bar_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null) adapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void fetchData(int count) {
        lazyLoader.setVisibility(View.VISIBLE);
        String url = BootServerUtil.baseUri+"api/v1/user/recipes/getAll/?count="+count+"&auth="+ storageHelper.getRawToken();
        executorService.execute(() -> ApiUtils
                .getInstance(requireContext())
                .fetchData(url, PublicRecipe.class, new ApiUtils.ApiResponseCallback<List<PublicRecipe>>() {
                    @Override
                    public void onSuccess(List<PublicRecipe> result) {
                        adapter = new RecipieHolderAdapter(result);
                        recyclerView.setAdapter(adapter);
                        lazyLoader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        ThinkfinityUtils.createErrorMessage(requireContext(), errorMessage).show();
                    }
                }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                search_bar_edit_text.setText(Objects.requireNonNull(result).get(0));
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}