package blackflame.com.zymepro.notification;


import android.annotation.TargetApi;
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
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import blackflame.com.zymepro.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class NotificationUtils extends ContextCompat {
    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;
    // Sets an ID for the notification, so it can be updated.
    CharSequence name = "ZymePro";
    public static final String PRIMARY_CHANNEL = "ZymePro";
    public static final String SECONDARY_CHANNEL = "ZymePro_Theft";
    private NotificationManager manager;
     NotificationCompat.Builder mBuilder;
    public NotificationUtils(Context mContext) {

        this.mContext = mContext;


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

        private void showNotification(final @NonNull Notification notification, final int notificationId) {
            Log.e(TAG, "showNotification: generate"+"call" );
            //final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
            final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);


           notificationManager.notify(notificationId, notification);
            Log.e(TAG, "showNotification: "+"after call" );
        }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {


            showNotificationMessage(title, message, timeStamp, intent, null);


    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl) {
        // Check for empty push message

        Log.e(TAG, "showNotificationMessage: "+title );

       //showNotification(getNotification1("Hello","Hi"),getRequestCode()); ;
        if (TextUtils.isEmpty(message))
            return;
        // notification icon
        final int icon = R.mipmap.icon;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        getRequestCode(),
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

            if (title.contains("Theft") || title.contains("theft")){
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

                //Log.e(TAG, "showNotificationMessage:SoundType Theft" );
            }else{
               // Log.e(TAG, "showNotificationMessage:SoundType Normal" );
               alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + mContext.getPackageName() + "/raw/notification");
            }




        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                Log.e(TAG, "showNotificationMessage: Imageurl "+imageUrl );
                
                
                
            //   new  generatePictureStyleNotification(mBuilder, icon, imageUrl,title, message, timeStamp, resultPendingIntent, alarmSound).execute();
                Bitmap bitmap = getBitmapFromURL(imageUrl);
                
              

                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                } else {
//                     if (android.os.Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
//                         showSmallNotificationNew(icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//
//
//                     }else
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                }
            }
        } else {

            if(intent.getStringExtra("summary")!=null){
                 Log.e(TAG, "showNotificationMessage: "+"android pre O" );
                    showTripEndNotification(icon, title, message, intent.getStringExtra("summary"), timeStamp, resultPendingIntent, alarmSound);
                //}
            }else {

                    Log.e(TAG, "showNotificationMessage: "+"android pre O" );
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                //}
            }
           // playNotificationSound();
        }
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

                   // bigText.bigText(message);
                   // bigText.setSummaryText(message);
                    bigText.setBigContentTitle(title);

        Notification notification;


        notification = mBuilder.setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigText)
                .setWhen(Long.parseLong(timeStamp))
                .setSmallIcon(R.mipmap.icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .setPriority(Notification.PRIORITY_MAX)

                .build();

        showNotification(notification,getRequestCode());
    }
    private void showTripEndNotification( int icon, String title, String message, String summary, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

            bigText.bigText(Html.fromHtml(summary));
            //bigText.setSummaryText(summary);
            bigText.setBigContentTitle(title);
            Notification notification;
            NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,PRIMARY_CHANNEL);
            notification = builder.setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(bigText)
                    .setWhen(Long.parseLong(timeStamp))
                    .setSmallIcon(R.mipmap.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();

        showNotification(notification,getRequestCode());
    }

    private static int getRequestCode() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(9000);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(Long.parseLong(timeStamp))
                .setSmallIcon(R.mipmap.icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        showNotification(notification,getRequestCode());

    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(NotificationCompat.Builder mBuilder, int icon,String image, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        try {
            URL url = new URL(image);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
    
            Log.e(TAG, "getBitmapFromURL: "+myBitmap );
            showBigNotification(myBitmap,mBuilder,icon,title,message,timeStamp,resultPendingIntent,alarmSound);
    
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Bitmap getBitmapFromURL(String image) {
        try {
            URL url = new URL(image);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            
            Log.e(TAG, "getBitmapFromURL: "+myBitmap );
            
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Send a notification.
     *
     * @param id The ID of the notification
     * @param notification The notification object
     */
    public void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

    /**
     * Get the notification manager.
     *
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getManager() {
            if (manager == null) {
                manager = (NotificationManager) mContext. getSystemService(Context.NOTIFICATION_SERVICE);
            }
            return manager;
        }
    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {
        
        private Context mContext;
        private String title, message, imageUrl;
        NotificationCompat.Builder builder;
        int icon;
        Uri alarmSound;
        PendingIntent pendingIntent;
        String timestamp;
        
        public generatePictureStyleNotification(NotificationCompat.Builder mBuilder, int icon,String image, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
            super();
            this.alarmSound=alarmSound;
            this.pendingIntent=resultPendingIntent;
            this.timestamp=timeStamp;
            this.title = title;
            this.message = message;
            this.imageUrl = image;
            this.builder=mBuilder;
            this.icon=icon;
        }
        
        @Override
        protected Bitmap doInBackground(String... params) {
            
            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                showBigNotification(myBitmap,builder,icon,title,message,timestamp,pendingIntent,alarmSound);
                Log.e(TAG, "doInBackground: call"+myBitmap );
                return myBitmap;
            } catch (MalformedURLException e) {
                Log.e(TAG, "doInBackground: " );
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            Log.e(TAG, "onPostExecute: "+result );
           // showBigNotification(result,builder,icon,title,message,timestamp,pendingIntent,alarmSound);
         
        }
    }




}
