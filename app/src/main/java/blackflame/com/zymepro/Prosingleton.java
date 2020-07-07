package blackflame.com.zymepro;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatDelegate;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.onesignal.helper.NotificationOpenedHandler;
import blackflame.com.zymepro.onesignal.helper.NotificationReceivedHandler;
import blackflame.com.zymepro.util.Utils;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.onesignal.OneSignal;



/**
 * Created by Prashant on 29-03-2017.
 */

public class Prosingleton extends Application {

    public static final String TAG = Prosingleton.class
            .getSimpleName();
    private static Context context;
    private static Prosingleton mInstance;
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
       // MultiDex.install(this);

    }
    @Override
    public void onCreate() {
        super.onCreate();

       // Fabric.with(this, new Crashlytics());
        mInstance = this;
        FirebaseApp.initializeApp(this);

        Utils.init(this);
        CommonPreference.initializeInstance(this);
       Prosingleton.context = getApplicationContext();
        MultiDex.install(this);


        // Make sure we use vector drawables
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        OneSignal.startInit(this)
            .setNotificationOpenedHandler(new NotificationOpenedHandler())
            .setNotificationReceivedHandler(new NotificationReceivedHandler(context) )
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init();
        Stetho.initializeWithDefaults(this);


    }



    public static Context getAppContext() {
        return Prosingleton.context;
    }

    public static synchronized Prosingleton getInstance() {
        return mInstance;
    }




}
