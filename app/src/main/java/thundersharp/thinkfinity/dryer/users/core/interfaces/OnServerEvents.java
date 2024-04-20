package thundersharp.thinkfinity.dryer.users.core.interfaces;

import org.json.JSONObject;

public interface OnServerEvents {
    void onQuerySuccessful(JSONObject data);
    void onQueryFailure(Exception e);
}
