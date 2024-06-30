package thundersharp.thinkfinity.dryer.users.core.helpers;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SSEClient {

    private final Context context;
    private final String url;
    private RequestQueue requestQueue;
    private volatile boolean isRunning;
    private Thread sseThread;

    public SSEClient(Context context, String url) {
        this.context = context;
        this.url = url;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void start(final SSEListener listener) {
        isRunning = true;

        sseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(SSEClient.this.url);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(0); // 30 seconds
                    urlConnection.setConnectTimeout(30000); // 30 seconds

                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while (isRunning && (line = reader.readLine()) != null) {
                        if (line.startsWith("data:")) {
                            final String data = line.substring(5).trim();
                            listener.onMessage(data);
                        }
                    }
                } catch (IOException e) {
                    listener.onError(e);
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });

        sseThread.start();
    }

    public void stop() {
        isRunning = false;
        if (sseThread != null) {
            sseThread.interrupt();
        }
    }

    public interface SSEListener {
        void onMessage(String message);
        void onError(Throwable t);
    }
}