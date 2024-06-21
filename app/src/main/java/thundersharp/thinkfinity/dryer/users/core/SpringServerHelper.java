package thundersharp.thinkfinity.dryer.users.core;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import thundersharp.thinkfinity.dryer.boot.helpers.StorageHelper;
import thundersharp.thinkfinity.dryer.boot.serverStat.BootServerUtil;
import thundersharp.thinkfinity.dryer.users.core.interfaces.OnServerEvents;

public class SpringServerHelper {

    private final WeakReference<Context> contextWeakReference;
    private StorageHelper storageHelper;

    public SpringServerHelper(WeakReference<Context> contextWeakReference) {
        storageHelper = StorageHelper.getInstance(contextWeakReference.get()).initUserJWTDataStorage();
        this.contextWeakReference = contextWeakReference;
    }

    public static SpringServerHelper getInstance(Context context){
        return new SpringServerHelper(new WeakReference<>(context));
    }

    public SpringServerHelper getPublicRecipes(int count, OnServerEvents onServerEvents){
        System.out.println(BootServerUtil.baseUri+"api/v1/user/recipes/getAll/?count="+count+"&auth="+ storageHelper.getRawToken());
        executeVolleyCommands(onServerEvents, BootServerUtil.baseUri+"api/v1/user/recipes/getAll/?count="+count+"&auth="+ storageHelper.getRawToken());
        return this;
    }

    private void executeVolleyCommands(OnServerEvents onServerStatus, String baseURL) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(contextWeakReference.get());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, baseURL, response -> {
                try {
                    onServerStatus
                            .onQuerySuccessful(new JSONObject(response));
                } catch (JSONException e) {
                    onServerStatus.onQueryFailure(e);
                }
            }, error -> {
                error.printStackTrace();
                try {
                    onServerStatus
                            .onQueryFailure(new Exception("ERROR : " + error.networkResponse.statusCode));
                } catch (Exception e) {
                    onServerStatus.onQueryFailure(e);
                }
            }) {

                @Override
                public String getPostBodyContentType() {
                    return "application/json;charset=UTF-8";
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            onServerStatus.onQueryFailure(e);
        }
    }
}
