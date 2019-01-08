package blackflame.com.zymepro.io.http;

import blackflame.com.zymepro.io.listener.AppRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseTaskJson<T> extends JsonObjectRequest {
    protected static final String PROTOCOL_CHARSET = "utf-8";

    @Override
    public String getBodyContentType() {
        return super.getBodyContentType();
    }

    @Override
    public byte[] getBody() {
        return super.getBody();
    }

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
     VolleyError error;
   // HashMap<String,String> params;

    public BaseTaskJson(int method, String url, JSONObject jsonObject, ErrorListener listener,
                        AppRequest appRequest) {
        super(method, url,jsonObject,appRequest,listener);
       // this.params =params;
        setShouldCache(true);
        //this.tag = requestTag;
        this.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DEFAULT_MAX_RETRIES,
                DEFAULT_BACKOFF_MULT));
    }

    public static String getProtocolContentType() {
        return PROTOCOL_CONTENT_TYPE;
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

    public Map<String, String> getmBundle() {
        return mBundle;
    }

    public void setmBundle(Map<String, String> mBundle) {
        this.mBundle = mBundle;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }


    public void setError(VolleyError error){
        this.error=error;
    }


    public VolleyError getVolleyError(){
        return this.error;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        super.deliverResponse(response);
    }
}
