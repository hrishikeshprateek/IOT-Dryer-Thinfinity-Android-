package thundersharp.thinkfinity.dryer.boot.serverStat;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;

public class BootServerUtil {

    private final WeakReference<Context> context;
    public static String baseUri = "http://192.168.0.115:8080/";

    public static BootServerUtil getInstance(Context context){
        return new BootServerUtil(new WeakReference<>(context));
    }

    public void checkIfServerIsReachable(OnServerStatus onServerStatus){
        executeVolleyCommands(onServerStatus,baseUri+"alive");
    }

    public BootServerUtil(WeakReference<Context> context) {
        this.context = context;
    }

    public WeakReference<Context> getContext() {
        return context;
    }

    private void executeVolleyCommands(OnServerStatus onLiveDataFetchListener, String baseURL) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context.get());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, baseURL,
                    onLiveDataFetchListener::onServerAlive, error -> {
                error.printStackTrace();
                try {
                    onLiveDataFetchListener
                            .onServerUnReachable(new Exception("ERROR : " + error.networkResponse.statusCode));
                } catch (Exception e) {
                    onLiveDataFetchListener.onServerUnReachable(e);
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
            onLiveDataFetchListener.onServerUnReachable(e);
        }
    }
}
