package blackflame.com.zymepro.onesignal.helper;

import static android.content.ContentValues.TAG;

import android.util.Log;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import org.json.JSONObject;

public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

  @Override
  public void notificationOpened(OSNotificationOpenResult result) {
    OSNotificationAction.ActionType actionType = result.action.type;
    JSONObject data = result.notification.payload.additionalData;

    Log.e(TAG, "notificationOpened: "+data );
    String customKey;


  }
}
