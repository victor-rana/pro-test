package blackflame.com.zymepro.notification;

import static com.android.volley.VolleyLog.TAG;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import blackflame.com.zymepro.BuildConfig;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.util.jwt.KeyGenerator;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ProFirebaseMessagingService extends IntentService {
  String token_send;
  public ProFirebaseMessagingService() {
    // Used to name the worker thread, important only for debugging.
    super("pro-service");
    FirebaseApp.initializeApp(ProFirebaseMessagingService.this);

  }
  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    // Make a call to Instance API
    FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();

    try {
      // request token that will be used by the server to send push notifications
      String token = instanceID.getToken();
      // Log.e("ProfirebaseMessaginf", "FCM Registration Token: " + token);
      CommonPreference.initializeInstance(getApplicationContext());
      CommonPreference.getInstance().setFcmToken(token);

      // pass along this data

      sendRegistrationToServer(token);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void sendRegistrationToServer(String token) {
    // Add custom implementation, as needed.
    token_send=token;
    final String email=CommonPreference.getInstance().getEmail();
    // getSharedPreferences(ActivityConstants.SHAREDPREFNAMELOGIN,0).getString(ActivityConstants.EMAIL,"00");

    final StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
        "http://api.getzyme.xyz/notification/API/v1/token",
        new Response.Listener<String>() {

          @Override
          public void onResponse(String response) {
            try {
              JSONObject jObject = new JSONObject(response);
//                            Log.e("ProFireBaseMessaging", "onResponse: "+response );

            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        }, new Response.ErrorListener() {


      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }) {

      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("email_id",email);
        params.put("token",token_send);
        params.put("app_name","ZYME_PRO");
        // params.put("source","android");
        return params;
      }
      @Override
      public Map<String, String> getHeaders() {
        HashMap<String,String> param=new HashMap<>();
        Log.e(TAG, "getHeaders: "+CommonPreference.getInstance().getToken() );
        param.put("x-access-token",CommonPreference.getInstance().getToken());
        param.put("x-app-version",String.valueOf(BuildConfig.VERSION_CODE));
        param.put("x-client-token", KeyGenerator.getKey(CommonPreference.getInstance().getClientToken()));
        return param;
      }



    };
    jsonObjReq.setShouldCache(false);
    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
        10000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    //  ZymeSingleton.getInstance().addToRequestQueue(jsonObjReq,  tag_json_obj);
    RequestQueue requestQueue = Volley.newRequestQueue(Prosingleton.getAppContext());
    requestQueue.add(jsonObjReq);



  }
}
