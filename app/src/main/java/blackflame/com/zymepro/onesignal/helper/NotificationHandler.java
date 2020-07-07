package blackflame.com.zymepro.onesignal.helper;

import static blackflame.com.zymepro.onesignal.helper.NotificationHelperMethod.getBitmapFromURL;
import static blackflame.com.zymepro.onesignal.helper.NotificationHelperMethod.getRequestCode;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import blackflame.com.zymepro.R;

import blackflame.com.zymepro.ui.splash.SplashScreen;
import org.json.JSONObject;

public class NotificationHandler extends ContextCompat {
  private static String TAG = NotificationHandler.class.getSimpleName();
  private Context mContext;
  // Sets an ID for the notification, so it can be updated.
  CharSequence name = "ZymePro";
  public static final String PRIMARY_CHANNEL = "ZymePro";
  public static final String SECONDARY_CHANNEL = "ZymePro_Theft";
  private NotificationManager manager;
  NotificationCompat.Builder mBuilder;
  String imageUrl;
  public NotificationHandler(Context mContext) {
    this.mContext = mContext;
    Log.e(TAG, "NotificationHandler: Called" );

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL, mContext.getString(R.string.notification_channel_app_name),
          NotificationManager.IMPORTANCE_HIGH);
      chan1.setLightColor(Color.GREEN);
      chan1.setShowBadge(true);
      chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
      AudioAttributes att = new AudioAttributes.Builder()
          .setUsage(AudioAttributes.USAGE_NOTIFICATION)
          .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
          .build();
      chan1.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
          + "://" + mContext.getPackageName() + "/raw/notification"),att);
      getManager().createNotificationChannel(chan1);

      NotificationChannel chan2 = new NotificationChannel(SECONDARY_CHANNEL,
          mContext.getString(R.string.notification_channel_alert_name), NotificationManager.IMPORTANCE_HIGH);
      chan2.setLightColor(Color.BLUE);
      chan2.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
      chan2.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
          + "://" + mContext.getPackageName() + "/raw/theft"),att);
      getManager().createNotificationChannel(chan2);

    }

  }

  public void handleMessage(JSONObject jsonObject)  {
    try {

      Log.e(TAG, "handleMessage: " + jsonObject);
      Log.e(TAG, "handleMessage: " + mContext);

      Intent resultIntent = new Intent(mContext.getApplicationContext(),SplashScreen.class);
      resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
      Bundle bundle = NotificationHelperMethod.getBundle(jsonObject);
      resultIntent.putExtras(bundle);

      if (jsonObject.has("image")) {
          imageUrl=jsonObject.getString("image");

        //imageUrl = "http://getzyme.com/assets/img/" + jsonObject.getString("image");
      }

      showNotification(resultIntent, bundle);
    }catch (Exception ex){
      Log.e(TAG, "handleMessage: "+ex.getMessage() );
    }
  }

  private NotificationManager getManager() {
    if (manager == null) {
      manager = (NotificationManager) mContext. getSystemService(Context.NOTIFICATION_SERVICE);
    }
    return manager;
  }

  private void showNotification(Intent intent,Bundle bundle){

    String message=bundle.getString("message");
    String title=bundle.getString("title");
    if (bundle.containsKey("image")) {
      imageUrl = bundle.getString("image");
    }
    if (TextUtils.isEmpty(message)){
      return;
    }
    final int icon=R.mipmap.icon;
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    final PendingIntent resultPendingIntent =
        PendingIntent.getActivity(
            mContext,
            getRequestCode(),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        );

    if (title!=null && title.contains("Theft") || title.contains("theft")){
      Log.e(TAG, "showNotificationMessage:AlertType Theft" );

      mBuilder = new NotificationCompat.Builder(mContext,SECONDARY_CHANNEL);
    }else{
      Log.e(TAG, "showNotificationMessage:AlertType Normal" );
      mBuilder = new NotificationCompat.Builder(mContext,PRIMARY_CHANNEL);
    }
    Uri alarmSound;
    if (title.contains("Theft") || title.contains("theft")){

      alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
          + "://" + mContext.getPackageName() + "/raw/theft");

    }else{
      alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
          + "://" + mContext.getPackageName() + "/raw/notification");
    }
    if (!TextUtils.isEmpty(imageUrl)){
      if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
        Bitmap bitmap = getBitmapFromURL(imageUrl);
        if (bitmap != null) {
          showPictureNotification(mBuilder,bitmap,bundle,resultPendingIntent,alarmSound);
          } else {
         showSmallNotification(mBuilder, resultPendingIntent ,bundle, alarmSound);
        }
      }
    }else {

      if(bundle.containsKey("summary")&&bundle.getString("summary")!=null){
        Log.e(TAG, "showNotificationMessage: "+"android pre O" );
        showBigNotification(mBuilder, resultPendingIntent,bundle, alarmSound);
        //}
      }else {
        Log.e(TAG, "showNotificationMessage: "+"android pre O" );
        showSmallNotification(mBuilder,  resultPendingIntent,bundle, alarmSound);
        //}
      }

    }

  }

  private void showSmallNotification(NotificationCompat.Builder builder,PendingIntent intent,Bundle bundle,Uri alarmSound){
    Log.e(TAG, "showSmallNotification: small notification called" );

    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
    String title=bundle.getString("title");

    bigText.setBigContentTitle(title);
    Notification notification;
    notification = builder.setTicker(title).setWhen(0)
        .setAutoCancel(true)
        .setContentTitle(title)
        .setContentIntent(intent)
        .setSound(alarmSound)
        .setStyle(bigText)
        .setWhen(Long.parseLong(bundle.getString("time")))
        .setSmallIcon(R.mipmap.icon)
        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon))
        .setContentText(bundle.getString("message"))
        .setPriority(Notification.PRIORITY_MAX)
        .build();

    generateNotification(notification,getRequestCode());


  }
  private void showBigNotification(NotificationCompat.Builder builder,PendingIntent intent,Bundle bundle,Uri alarmSound){

    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

    bigText.bigText(Html.fromHtml(bundle.getString("summary")));


    //bigText.setSummaryText(summary);
    bigText.setBigContentTitle(bundle.getString("title"));
    Notification notification;
    notification = builder.setTicker(bundle.getString("title")).setWhen(0)
        .setAutoCancel(true)
        .setContentTitle(bundle.getString("title"))
        .setContentIntent(intent)
        .setSound(alarmSound)
        .setStyle(bigText)
        .setWhen(Long.parseLong(bundle.getString("time")))
        .setSmallIcon(R.mipmap.icon)
        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon))
        .setContentText(bundle.getString("message"))
        .setPriority(Notification.PRIORITY_MAX)
        .build();

    generateNotification(notification,getRequestCode());


  }

  private void showPictureNotification(NotificationCompat.Builder mBuilder,Bitmap bitmap, Bundle bundle, PendingIntent resultPendingIntent, Uri alarmSound) {
    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
    bigPictureStyle.setBigContentTitle(bundle.getString("title"));
    bigPictureStyle.setSummaryText(Html.fromHtml(bundle.getString("message")).toString());
    bigPictureStyle.bigPicture(bitmap);
    Notification notification;
    notification = mBuilder.setSmallIcon(R.mipmap.icon).setTicker(bundle.getString("title")).setWhen(0)
        .setAutoCancel(true)
        .setContentTitle(bundle.getString("title"))
        .setContentIntent(resultPendingIntent)
        .setSound(alarmSound)
        .setStyle(bigPictureStyle)
        .setWhen(Long.parseLong(bundle.getString("time")))
        .setSmallIcon(R.mipmap.icon)
        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon))
        .setContentText(bundle.getString("message"))
        .build();
    NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(getRequestCode(), notification);
  }



  private void generateNotification(final @NonNull Notification notification, final int notificationId) {
    Log.e(TAG, "showNotification: generate"+"call" );
    //final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
    final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);


    notificationManager.notify(notificationId, notification);
    Log.e(TAG, "showNotification: "+"after call" );
  }


}
