package blackflame.com.zymepro.io.http;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import blackflame.com.zymepro.BuildConfig;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.db.Pref;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.util.LogUtils;
import blackflame.com.zymepro.util.jwt.KeyGenerator;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

;


public class HttpRequestsJson extends BaseTaskJson {
    private static HttpRequestsJson instance = null;
    private Map<String, String> headers = new HashMap<String, String>();
    private Context mContext;
    private AppRequest appRequest;
    private Map<String, String> mParams = new HashMap<String, String>();
    private Constants.RequestParam requestParam;

    public HttpRequestsJson(int method, String url, JSONObject jsonObject, Response.ErrorListener listener,
                            AppRequest appRequest, HashMap<String, String> params) {

        //super(method, url, listener, requestTag, requestListener);
        super(method, url, jsonObject, listener, appRequest);

        this.appRequest = appRequest;
        Log.e("Content tye", "dfddf");
        LogUtils.info("Login",jsonObject.toString()+appRequest);

        this.requestParam = requestParam;
    }


    public static HttpRequestsJson getInstance(int method, Constants.RequestParam requestParam, Response.ErrorListener listener,
                                               AppRequest appRequest, Map<String, String> mParams, boolean isPayU) {
        if (instance == null) {
            //instance = new HttpRequestsJson(method, requestParam, listener, appRequest, mParams);
        }
        return instance;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        String req_id=UUID.randomUUID().toString();
        headers.put("req_id",req_id);
        if (getTag().equals("sign_in")){
            headers.put("x-app-version",String.valueOf(BuildConfig.VERSION_CODE));

            CommonPreference.getInstance().setCLientToken(req_id);
            String token=KeyGenerator.getKey(req_id);
            headers.put("x-client-token", token);
            LogUtils.error("Login",token);
        }else{
            headers.put("x-app-version", String.valueOf(BuildConfig.VERSION_CODE));

            Log.e("Header", "getHeaders: " + CommonPreference.getInstance().getToken());
            headers.put("x-access-token", CommonPreference.getInstance().getToken());

            headers.put("x-app-version", String.valueOf(BuildConfig.VERSION_CODE));
            String client_token = KeyGenerator.getKey(CommonPreference.getInstance().getClientToken());
            headers.put("x-client-token", client_token);
        }
        return headers;
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

            if (response != null) {
                setJsonResponse(response);
                appRequest.onRequestCompleted(this, requestParam);
            }
            else
                appRequest.onRequestFailed(this, requestParam);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "deliverResponse: "+e.getMessage());
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
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

    @Override
    public int getPosition() {
        return super.getPosition();
    }
}



