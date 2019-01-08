package blackflame.com.zymepro.io.http;

import android.content.Context;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.io.listener.AppRequest;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.HashMap;
import org.json.JSONObject;


public class ApiRequests {
    public static final String PREFERENCES_FILE = "CatalogPreference";
    private static ApiRequests apiRequests = null;
    private final VolleyErrorListener error;
    public RequestQueue mRequestQueue;
    private HashMap<String, String> mParams;
    private Constants.RequestParam requestParam;

    public static ApiRequests getInstance() {
        return new ApiRequests();
    }

    public ApiRequests() {
        if (mParams == null) {
            mParams = new HashMap<String, String>();
        } else {
            mParams.clear();
        }
        mRequestQueue = RequestManager.getnstance(GlobalReferences.getInstance().baseActivity);
        error = new VolleyErrorListener();
    }

    public void sign_up(Context context, AppRequest appRequest, JSONObject jsonObject) {
        if (context != null) {

            requestParam = Constants.RequestParam.SIGN_UP;
            String url = Constants.RequestParam.SIGN_UP.getBaseComleteUrl();
            String requestTag = "sign_up";
            HttpRequestsJson requests = new HttpRequestsJson(Request.Method.POST, url, jsonObject, error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag(requestParam.getRequestTag());
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }
    public void login(Context context, AppRequest appRequest, JSONObject jsonObject) {
        if (context != null) {

            requestParam = Constants.RequestParam.SIGN_IN;
            String url = Constants.RequestParam.SIGN_IN.getBaseComleteUrl();
            String requestTag = "sign_up";
            HttpRequestsJson requests = new HttpRequestsJson(Request.Method.POST, url, jsonObject, error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag(requestParam.getRequestTag());
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }

    public void get_status(Context context, AppRequest appRequest) {
        if (context != null) {
            requestParam = RequestParam.STATUS;
            String url = RequestParam.STATUS.getBaseComleteUrl();
            HttpRequests requests = new HttpRequests(Request.Method.GET, url, requestParam.getRequestTag(), error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag(requestParam.getRequestTag());
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }

    public void get_address(Context context, AppRequest appRequest,double lat,double lon) {
        if (context != null) {
            requestParam = RequestParam.STATUS;
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=true+&key=AIzaSyApjqH7n5NtCjitmRG7ZiFgAsnPICWlrOM";;
            HttpRequests requests = new HttpRequests(Request.Method.GET, url, "address", error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, "address");
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag("address");
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }

    public void logout(Context context, AppRequest appRequest, JSONObject jsonObject) {
        if (context != null) {

            requestParam = RequestParam.SIGN_OUT;
            String url = RequestParam.SIGN_OUT.getBaseComleteUrl();
            String requestTag = "sign_up";
            HttpRequestsJson requests = new HttpRequestsJson(Request.Method.POST, url, jsonObject, error, appRequest, mParams);
            error.setRequestLister(appRequest, requests, requestParam.getRequestTag());
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(requestParam.getRequestTag());
            }
            requests.setTag(requestParam.getRequestTag());
            mRequestQueue.add(requests);
            appRequest.onRequestStarted(requests, requestParam);
        }
    }

    /**
     * Will be reponsible for listening errors
     * <p/>
     * *
     */
    class VolleyErrorListener implements Response.ErrorListener {
        private AppRequest listener;
        private BaseTask<?> task;
        private BaseTaskJson<JSONObject> task1;
        private String api_name;

        void setRequestLister(AppRequest listener, BaseTask<?> task, String api_name) {
            this.listener = listener;
            this.task = task;
            this.api_name = api_name;
        }

        void setRequestLister(AppRequest listener, BaseTaskJson<JSONObject> task, String api_name) {
            this.listener = listener;
            this.task1 = task;
            this.api_name = api_name;
        }

        @Override
        public void onErrorResponse(VolleyError error) {

            String json = null;
            NetworkResponse response = error.networkResponse;
            if (error.getMessage()!= null) {
                json = new String(error.getMessage());
              //  json = trimMessage(json, "message");
                //   if (json != null)
                //Utility.displayToastMessage(response.statusCode, json + " on " + api_name == null ? "" : api_name);

            } else {
                // Utility.displayMessage(500, "Unexpected server error on " + api_name == null ? "" : api_name);
            }
            if (listener != null) {
                if (task != null) {
                    try {
                        task.setError(error);
                        task.setJsonResponse(new JSONObject(json));
                        listener.onRequestFailed(task, requestParam);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if (task1 != null) {
                    try {
                        try {
                            task1.setError(error);
                            task1.setJsonResponse(new JSONObject(json));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        listener.onRequestFailed(task1, requestParam);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

        }
    }
}