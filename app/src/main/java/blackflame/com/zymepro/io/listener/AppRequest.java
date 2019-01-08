package blackflame.com.zymepro.io.listener;

import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import org.json.JSONObject;

public interface AppRequest extends Response.Listener<JSONObject>{

    public <T> void onRequestStarted(BaseTask<T> listener,
        Constants.RequestParam requestParam);

    public <T> void onRequestCompleted(BaseTask<T> listener,
        Constants.RequestParam requestParam);

    public <T> void onRequestFailed(BaseTask<T> listener,
        Constants.RequestParam requestParam);

    public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener,
        Constants.RequestParam requestParam);

    public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener,
        Constants.RequestParam requestParam);

    public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener,
        Constants.RequestParam requestParam);

    void onResponse(JSONObject response);
}
