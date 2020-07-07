package blackflame.com.zymepro.notification.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;
import blackflame.com.zymepro.notification.ProFirebaseMessagingService;

public class NotificationReceiver extends WakefulBroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {

    // cancel any further alarms
    AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    alarmMgr.cancel(alarmIntent);
    completeWakefulIntent(intent);
    //context.startService(new Intent(ProFirebaseMessagingService.class.getName()));
    Intent startintent=new Intent(ProFirebaseMessagingService.class.getName());
    startintent.setPackage("blackflame.com.zymepro");
    startWakefulService(context,startintent);

    // start the GcmTaskService
    // ProFCMMessageHandler.showNotification(context);
  }
}
