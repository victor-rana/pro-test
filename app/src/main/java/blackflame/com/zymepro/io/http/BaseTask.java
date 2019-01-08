package blackflame.com.zymepro.io.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseTask<T> extends Request<T> {
    protected static final String PROTOCOL_CHARSET = "utf-8";
    /**
     * Content type for request.
     */
    protected static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);
    static final int MY_SOCKET_TIMEOUT_MS = 30000;
    static final int DEFAULT_MAX_RETRIES = 1;
    static final int DEFAULT_BACKOFF_MULT = 0;
    protected Map<String, String> mBundle;
    String tag;
    int position;
    int quantity;
    String subCategory;
    JSONObject jsonResponse;
    JSONArray jsonArrayResponse;
    String response;

    VolleyError volleyError;



    public BaseTask(int method, String url, ErrorListener listener, String requestTag, Map<String, String> mParams) {
        super(method, url, listener);
        setShouldCache(true);
        this.tag = requestTag;
        this.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DEFAULT_MAX_RETRIES,
                DEFAULT_BACKOFF_MULT));
    }
    public BaseTask(int method, String url, ErrorListener listener, String requestTag) {
        super(method, url, listener);
        setShouldCache(true);
        this.tag = requestTag;
        this.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DEFAULT_MAX_RETRIES,
                DEFAULT_BACKOFF_MULT));
    }


    public  void setError(VolleyError error){
        this.volleyError=error;

    }

    public VolleyError getVolleyError(){
        return this.volleyError;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BaseTask(int method, String url, ErrorListener listener, String requestTag, Map<String, String> mParams, int position, boolean isquanity) {
        super(method, url, listener);
        setShouldCache(true);
        this.tag = requestTag;

        if (isquanity) {
            this.quantity = position;
        } else {
            this.position = position;
        }
        this.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DEFAULT_MAX_RETRIES,
                DEFAULT_BACKOFF_MULT));
    }

    public Map<String, String> getmBundle() {
        return mBundle;
    }

    public void setmBundle(Map<String, String> mBundle) {
        this.mBundle = mBundle;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public JSONObject getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(JSONObject jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    public JSONArray getJsonArrayResponse() {
        return jsonArrayResponse;
    }

    public void setJsonArrayResponse(JSONArray jsonResponse) {
        this.jsonArrayResponse = jsonResponse;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
