package thundersharp.thinkfinity.dryer.boot.helpers;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import thundersharp.thinkfinity.dryer.boot.interfaces.OnAuthEvent;
import thundersharp.thinkfinity.dryer.boot.models.LoginResponse;
import thundersharp.thinkfinity.dryer.boot.serverStat.BootServerUtil;

public class AuthHelper {

    private static AuthHelper authHelper;
    private final WeakReference<Context> contextWeakReference;
    public static AuthHelper getInstance(Context context){
        return new AuthHelper(new WeakReference<>(context));
    }

    public void performLogin(String userId, String passWord, OnAuthEvent onAuthEvent){
        executeVolleyCommands(onAuthEvent,userId,passWord);
    }

    public AuthHelper(WeakReference<Context> contextWeakReference) {
        this.contextWeakReference = contextWeakReference;
    }

    public Context getContext() {
        return contextWeakReference.get();
    }

    private void executeVolleyCommands(OnAuthEvent onLiveDataFetchListener, String username, String password) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(contextWeakReference.get());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BootServerUtil.baseUri+"auth/v1/login", response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    onLiveDataFetchListener.OnLoginSuccess(new LoginResponse(jsonObject.getJSONObject("data").getString("idToken"),
                            jsonObject.getJSONObject("data").getString("email")));
                } catch (JSONException e) {
                    onLiveDataFetchListener.OnLoginFailure(e);
                    e.printStackTrace();
                }

            }, error -> {
                error.printStackTrace();
                String errorMessage = "Unknown error";
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMessage = new String(error.networkResponse.data, StandardCharsets.UTF_8);

                    onLiveDataFetchListener
                            .OnLoginFailure(new Exception("HTTP ERROR CODE : " + error.networkResponse.statusCode+"\n\nMessage : "+errorMessage));
                }
            }) {

                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject requestBody = new JSONObject();
                    try {
                        requestBody.put("email", username);
                        requestBody.put("password", password);
                    } catch (JSONException e) {
                        onLiveDataFetchListener.OnLoginFailure(e);
                        e.printStackTrace();
                    }
                    return requestBody.toString().getBytes();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type","application/json; charset=UTF-8");
                    return headers;
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            onLiveDataFetchListener.OnLoginFailure(e);
        }
    }
}
