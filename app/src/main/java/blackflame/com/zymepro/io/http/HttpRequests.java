package blackflame.com.zymepro.io.http;

import android.content.Context;
import android.util.Log;
import blackflame.com.zymepro.BuildConfig;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.util.LogUtils;
import blackflame.com.zymepro.util.jwt.KeyGenerator;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

public class HttpRequests extends BaseTask<JSONObject> {
    private static HttpRequests instance = null;
    private Map<String, String> headers = new HashMap<String, String>();
    private Context mContext;
    private AppRequest appRequest;
    private Map<String, String> mParams = new HashMap<String, String>();
    private Constants.RequestParam requestParam;

    public HttpRequests(int method, Constants.RequestParam requestParam, Response.ErrorListener listener,
                        AppRequest appRequest, Map<String, String> mParams) {

        super(method, requestParam.getBaseComleteUrl(), listener, requestParam.getRequestTag(), mParams);
        this.appRequest = appRequest;
        this.mParams = mParams;
        setHeaders("authorization", GlobalReferences.getInstance().pref.getAccessToken()+"");
       // headers.put("authorization", GlobalReferences.getInstance().pref.getAccessToken()+"");
        this.requestParam = requestParam;
    }

    public HttpRequests(int method, String url, String requestTag, Response.ErrorListener listener,
                        AppRequest appRequest, Map<String, String> mParams) {

        super(method, url, listener, requestTag, mParams);
        this.appRequest = appRequest;
        this.mParams = mParams;


    }
    public static HttpRequests getInstance(int method, Constants.RequestParam requestParam, Response.ErrorListener listener, AppRequest appRequest, Map<String, String> mParams, boolean isPayU) {
        if (instance == null) {
            instance = new HttpRequests(method, requestParam, listener, appRequest, mParams);
        }
        return instance;

    }


    @Override
    public Map<String, String> getHeaders() {
        headers.put("Content-Type", "application/json; charset=utf-8");
        if (getTag().equals("sign_in")){
            headers.put("x-app-version",String.valueOf(BuildConfig.VERSION_CODE));
            String uuid= UUID.randomUUID().toString();
            CommonPreference.getInstance().setCLientToken(uuid);
            String token=KeyGenerator.getKey(uuid);
            headers.put("x-client-token", token);
            LogUtils.error("Login",token);
        }else {
            headers.put("x-app-version", String.valueOf(BuildConfig.VERSION_CODE));

            Log.e("Header", "getHeaders: " + CommonPreference.getInstance().getToken());
            headers.put("x-access-token", CommonPreference.getInstance().getToken());

            headers.put("x-app-version", String.valueOf(BuildConfig.VERSION_CODE));
            String client_token = KeyGenerator.getKey(CommonPreference.getInstance().getClientToken());
            headers.put("x-client-token", client_token);

        }
        return headers;
    }

    public void setHeaders(String title, String content) {
        headers.put(title, content);
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }
        return volleyError;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        try {

            if(response!=null) {
            setJsonResponse(response);
            appRequest.onRequestCompleted(this, requestParam);
            }
            else
                appRequest.onRequestFailed(this,requestParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        JSONObject json = null;
        try {
            json = new JSONObject(new String(response.data));
        } catch (final Exception e) {

            e.printStackTrace();
        }
        Log.e("##Json response = ", json + "");
        return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
    }
}
