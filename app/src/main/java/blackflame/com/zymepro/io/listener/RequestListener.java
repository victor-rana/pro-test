package blackflame.com.zymepro.io.listener;

import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import org.json.JSONObject;


public class RequestListener implements AppRequest {
    @Override
    public <T> void onRequestStarted(BaseTask<T> listener, Constants.RequestParam requestParam) {
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener, Constants.RequestParam requestParam) {
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener, Constants.RequestParam requestParam) {

    }

    @Override
    public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {

    }

    @Override
    public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {

    }

    @Override
    public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
