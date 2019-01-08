package blackflame.com.zymepro;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import blackflame.com.zymepro.util.Utils;


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
        Utils.init(this);
        //CommonPreference.initializeInstance(this);


       Prosingleton.context = getApplicationContext();
        //AnalyticTracker.initialize(this);
       // AnalyticTracker.getInstance().get(AnalyticTracker.Target.APP);

        // Make sure we use vector drawables
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        //mRequestQueue = Volley.newRequestQueue(getAppContext().getApplicationContext());
//        Log.e(TAG, "onCreate: queue"+mRequestQueue );
        //getRequestQueue();

       // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
//        OneSignal.startInit(this)
//            .setNotificationOpenedHandler(new NotificationOpenedHandler())
//            .setNotificationReceivedHandler(new NotificationReceivedHandler(context) )
//            .inFocusDisplaying(OSInFocusDisplayOption.Notification)
//            .unsubscribeWhenNotificationsAreDisabled(true)
//            .init();

    }



    public static Context getAppContext() {
        return Prosingleton.context;
    }

    public static synchronized Prosingleton getInstance() {
        return mInstance;
    }




}