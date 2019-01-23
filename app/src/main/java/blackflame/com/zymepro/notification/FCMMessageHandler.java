package blackflame.com.zymepro.notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.text.TextUtils;
import android.util.Log;
import blackflame.com.zymepro.BuildConfig;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.splash.SplashScreen;
import blackflame.com.zymepro.util.jwt.KeyGenerator;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class FCMMessageHandler extends FirebaseMessagingService {
  public static final int MESSAGE_NOTIFICATION_ID = 435345;
  private static final String TAG = FCMMessageHandler.class.getSimpleName();
  private NotificationUtils notificationUtils;
  String imageUrl;
  public static int NOTIFICATION_ID = 1;
  public static final String KEY_NOTIFICATION_REPLY = "KEY_NOTIFICATION_REPLY";
  private static final String KEY_TEXT_REPLY = "key_text_reply";

  // mRequestCode allows you to update the notification later on.
  int mRequestCode = 1000;
  String title_notification,token_server;

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    CommonPreference.initializeInstance(FCMMessageHandler.this);

    //  CommonPreference.initializeInstance(getApplicationContext());
    // Log.e(TAG, "onMessageReceived:id "+remoteMessage.getMessageId() );
    //Log.e(TAG, "onMessageReceived:from "+remoteMessage.getFrom() );
    Log.e(TAG, "Data Payload: pre" + remoteMessage.getData().toString());

//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
//
//            try {
//                //setInputNotification();
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                handleDataMessage(json);
//            } catch (Exception e) {
//                Log.e(TAG, "onMessageReceived: "+e.getCause() );
//                Log.e(TAG, "onMessageReceived: "+e.getMessage() );
//              //  Log.e(TAG, "Exception: " + e.getMessage());
//            }
//        }

  }

  private void handleDataMessage(JSONObject json) {

    try {
      //JSONObject data = json.getJSONObject("data");
      Intent resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
      String title = json.getString("title");
      token_server=CommonPreference.getInstance().getFcmToken();
      sendNotificatinAcceptance(title);
      Bundle bundle = new Bundle();
      if(json.has("message")){
        String   timestamp = json.getString("time");
        String message=json.getString("message");
        bundle.putString("screen",json.getString("screen"));
        bundle.putString("message",json.getString("message"));
        bundle.putString("identifier", json.getString("identifier"));
        if (json.has("image")){
          imageUrl = "http://getzyme.com/assets/img/" + json.getString("image");
        }
        resultIntent.putExtras(bundle);
        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);

      }else {


        if (json.has("image")) {
          imageUrl = "http://getzyme.com/assets/img/" + json.getString("image");
        }
        String   timestamp = json.getString("time");
        String   address = json.getString("address");

        bundle.putString("title", title);
        bundle.putString("time", timestamp);
        bundle.putString("sound", json.getString("sound"));
        bundle.putString("address", address);
        // bundle.putString("type",json.getString("type"));
        bundle.putString("car_id", json.getString("car_id"));
        bundle.putString("screen", json.getString("screen"));
        bundle.putString("identifier", json.getString("identifier"));
        if (json.has("IMEI")) {
          bundle.putString("IMEI", json.getString("IMEI"));
        }
        if (json.has("registration_number")) {
          bundle.putString("registration_number", json.getString("registration_number"));
        }
        if (json.has("car_count")) {
          bundle.putString("car_count", json.getString("car_count"));
        }
        if (json.has("summary")) {
          bundle.putString("summary", json.getString("summary"));
        }


        resultIntent.putExtras(bundle);


        //resultIntent.putExtra("article_data", articleData);
        //resultIntent.putExtra("image", imageUrl);

        // check for image attachment
        if (TextUtils.isEmpty(imageUrl)) {
          // Log.e(TAG, "handleDataMessage: withoutImage");
          showNotificationMessage(getApplicationContext(), title, address, timestamp, resultIntent);
        } else {
          // image is present, show notification with image
          // Log.e(TAG, "handleDataMessage: withImage");
          showNotificationMessageWithBigImage(getApplicationContext(), title, address, timestamp, resultIntent, imageUrl);
        }
      }

    } catch (JSONException e) {
      //Log.e(TAG, "Json Exception: " + e.getMessage());
      // System.out.println("in 1st CATCH");
    } catch (Exception e) {
      //Log.e(TAG, "Exception: " + e.getMessage());
      // System.out.println("in 2nd CATCH");
    }
  }

  /**
   * Showing notification with text only
   */
  private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
    notificationUtils = new NotificationUtils(context);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    Log.e(TAG, "showNotificationMessage: "+message );
  }

  /**
   * Showing notification with text and image
   */
  private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
    notificationUtils = new NotificationUtils(context);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);

  }


  public static CharSequence getReplyMessage(Intent intent) {
    Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
    if (remoteInput != null) {
      return remoteInput.getCharSequence(KEY_TEXT_REPLY);
    }
    return null;
  }


  private void sendNotificatinAcceptance(final String token) {
    // Add custom implementation, as needed.
    title_notification=token;
    final String email=getSharedPreferences("pref_login_pro",0).getString("email_pro","00");

    final StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
        "http://api.getzyme.xyz/notification/API/v1/push-notification/delivery",
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
        Log.e(TAG, "onErrorResponse: "+error.getCause() );


      }
    }) {

      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("email_id",email);
        params.put("title",title_notification);
        params.put("time",String.valueOf(System.currentTimeMillis()));
        params.put("token",token_server);
        return params;
      }
      @Override
      public Map<String, String> getHeaders() {
        HashMap<String,String> param=new HashMap<>();
        // Log.e(TAG, "getHeaders: "+CommonPreference.getInstance().getToken() );
        param.put("x-access-token",CommonPreference.getInstance().getToken());
        //param.put("x-access-token","xyz");

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
  public void startActivity(String className, Bundle extras, Context context) {
    Class cls = null;
    try {
      cls = Class.forName(className);
    } catch (ClassNotFoundException e) {
      //means you made a wrong input in firebase console
    }
    Intent intent = new Intent(context, cls);
    if (null != extras) {
      intent.putExtras(extras);
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
  }



}
