package thundersharp.thinkfinity.dryer.users.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;

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
import thundersharp.thinkfinity.dryer.users.core.adapters.RecipieHolderAdapter;
import thundersharp.thinkfinity.dryer.users.core.model.PublicRecipe;

public class PrivateRecipes extends AppCompatActivity {

    private EditText search_bar_edit_text;
    private ExecutorService executorService;
    private RecipieHolderAdapter adapter;
    private RecyclerView recyclerView;
    private StorageHelper storageHelper;
    private LazyLoader lazyLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_recipes);

        executorService = Executors.newSingleThreadExecutor();
        storageHelper = StorageHelper.getInstance(this).initUserJWTDataStorage();

        recyclerView = findViewById(R.id.recF);
        lazyLoader = findViewById(R.id.loaderAS);

        search_bar_edit_text = findViewById(R.id.search_bar_edit_text);

        ImageButton search_bar_voice_icon = findViewById(R.id.search_bar_voice_icon);
        ((Toolbar) findViewById(R.id.tool)).setNavigationOnClickListener(d -> finish());
        search_bar_voice_icon.setOnClickListener(v -> {

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search for recipes");

            try {
                startActivityForResult(intent, 1);
            } catch (Exception exception) {
                Toast.makeText(this, "EXCEPTION: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        setupSearchBar();
        fetchData();
    }

    private void setupSearchBar() {
        search_bar_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (adapter != null) adapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void fetchData() {
        lazyLoader.setVisibility(View.VISIBLE);
        String url = ThinkfinityUtils.HOST_BASE_ADDR_WITH_PORT+"/api/v1/private/recipes/getAll/"+ storageHelper.getRawToken();
        executorService.execute(() -> ApiUtils
                .getInstance(this)
                .fetchDataWhenDataISObjectList(url, PublicRecipe.class, new ApiUtils.ApiResponseCallback<List<PublicRecipe>>() {
                    @Override
                    public void onSuccess(List<PublicRecipe> result) {
                        adapter = new RecipieHolderAdapter(result);
                        recyclerView.setAdapter(adapter);

                        lazyLoader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        ThinkfinityUtils.createErrorMessage(PrivateRecipes.this, errorMessage).show();
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