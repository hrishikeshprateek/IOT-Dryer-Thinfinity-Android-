package thundersharp.thinkfinity.dryer.boot;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ApiUtils {

    private static ApiUtils instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private ApiUtils(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiUtils getInstance(Context context) {
        if (instance == null) {
            instance =new ApiUtils(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void fetchData(String url, Class<T> modelClass, final ApiResponseCallback<List<T>> callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        Type type = TypeToken.getParameterized(List.class, modelClass).getType();
                        List<T> list = gson.fromJson(response.getJSONArray("data").toString(), type);
                        callback.onSuccess(list);
                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }
                },
                error -> callback.onError(error.getMessage()));

        getRequestQueue().add(jsonObjectRequest);
    }

    public <T> void fetchDataNoList(String url, Class<T> modelClass, final ApiResponseCallback<T> callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Gson gson = new Gson();
                        callback.onSuccess(gson.fromJson(response.getJSONObject("data").toString(), modelClass));
                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }
                },
                error -> callback.onError(error.getMessage()));
        getRequestQueue().add(jsonObjectRequest);
    }
    public interface ApiResponseCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }
}