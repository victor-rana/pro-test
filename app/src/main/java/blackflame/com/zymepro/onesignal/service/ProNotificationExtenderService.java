package blackflame.com.zymepro.onesignal.service;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

public class ProNotificationExtenderService extends NotificationExtenderService {
  @Override
  protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
//   final OverrideSettings overrideSettings = new OverrideSettings();
//
//    overrideSettings.extender = new NotificationCompat.Extender() {
//      @Override
//      public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
//        // Sets the background notification color to Green on Android 5.0+ devices.
//        return builder.setColor(new BigInteger("FF00FF00", 16).intValue());
//      }
//      OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
//    };


    // Read properties from result.

    // Return true to stop the notification from displaying.
    return true;
  }
}
