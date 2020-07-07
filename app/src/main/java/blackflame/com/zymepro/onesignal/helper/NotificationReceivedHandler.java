package blackflame.com.zymepro.onesignal.helper;



import static com.android.volley.VolleyLog.TAG;

import android.content.Context;
import android.util.Log;

import blackflame.com.zymepro.db.CommonPreference;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import org.json.JSONObject;

public class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
  Context context;
  String imageUrl;
  NotificationHandler notificationHandler;
  public NotificationReceivedHandler(Context context){
    this.context=context;
  }


  @Override
  public void notificationReceived(OSNotification notification) {
    JSONObject data = notification.payload.additionalData;

    Log.e(TAG, "notificationReceived: onesignal" + data);
//    Log.e(TAG, "notificationReceived: context"+context );
//
    CommonPreference.initializeInstance(context);
    boolean hasLoggedIn = CommonPreference.getInstance().getIsLoggedIn();
    boolean isOneSignalEnable=CommonPreference.getInstance().getOneSignalNotification();
    if (hasLoggedIn && isOneSignalEnable) {

      notificationHandler = new NotificationHandler(context);
      try {
        long time_notification = Long.parseLong(data.getString("time"));
        long current_time = System.currentTimeMillis();
        Log.e(TAG, "notificationReceived: " + ((current_time - time_notification) / 1000));
        if (((current_time - time_notification) / 1000) > 604800) {
          return;
        }
        notificationHandler.handleMessage(data);
      } catch (Exception ex) {

        ex.printStackTrace();

        Log.e(TAG, "notificationReceived:cause " + ex.getCause());

      }


    }
  }









}