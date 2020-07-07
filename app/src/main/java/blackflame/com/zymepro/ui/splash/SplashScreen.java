package blackflame.com.zymepro.ui.splash;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.notification.NotificationUtils;
import blackflame.com.zymepro.onesignal.helper.NotificationHelperMethod;
import blackflame.com.zymepro.onesignal.helper.OneSignalUtility;
import blackflame.com.zymepro.ui.home.MainActivity;
import blackflame.com.zymepro.ui.login.LoginActivity;
import blackflame.com.zymepro.util.Analytics;

public class SplashScreen extends BaseActivity {
  private static final String TAG =SplashScreen.class.getCanonicalName() ;
  Intent intent;
  Bundle bundle;
  boolean hasLoggedin;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    CommonPreference.initializeInstance(SplashScreen.this);
    hasLoggedin = CommonPreference.getInstance().getIsLoggedIn();
    if (hasLoggedin) {
      boolean isIdentifier = getIntent().hasExtra("identifier");


      if (getIntent().hasExtra("identifier") && getIntent().getStringExtra("identifier")
          .equals("NOTIFICATION")) {

        bundle = getIntent().getExtras();
        String screen = bundle.getString("screen");
        Log.d(TAG, "onCreate: "+screen);
        Intent intent_directed = NotificationHelperMethod
            .getActivityToLaunch(screen, SplashScreen.this);
        intent_directed.putExtra("bundle", bundle);
        intent_directed
            .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_directed);
        new NotificationUtils(SplashScreen.this);
        finish();
        return;
      }
    }

    setContentView(R.layout.activity_splash_screen);
    new Handler().postDelayed(new Runnable() {

      /*
       * Showing splash screen with a timer. This will be useful when you
       * want to show case your app logo / company
       */

      @Override
      public void run() {
        // This method will be executed once the timer is over
        // Start your app main activity
        //  SharedPreferences preferences = getSharedPreferences(ActivityConstants.SHAREDPREFNAMELOGIN, 0);
        hasLoggedin = CommonPreference.getInstance().getIsLoggedIn();
        //     preferences.getBoolean(ActivityConstants.HASLOGGEDIN, false);
        if (hasLoggedin) {
          OneSignalUtility.updateTags();
          Intent i = new Intent(SplashScreen.this, MainActivity.class);
          startActivity(i);
        } else {
          OneSignalUtility.removeTags();
          Intent i = new Intent(SplashScreen.this, LoginActivity.class);
          startActivity(i);
        }

        // close this activity
        finish();
      }
    }, 3000);

  }

  @Override
  public void indexScreen() {
    Analytics.index(SplashScreen.this,"SplashScreen");
  }
}
