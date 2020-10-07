package blackflame.com.zymepro.ui.home;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import android.widget.Toast;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.db.SettingPreferences;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.notification.ProFirebaseMessagingService;
import blackflame.com.zymepro.onesignal.helper.OneSignalUtility;
import blackflame.com.zymepro.ui.about.AboutUs;
import blackflame.com.zymepro.ui.breakdown.BreakdownActivity;
import blackflame.com.zymepro.ui.dashcam.dashcamvideo.DashcamVideo;
import blackflame.com.zymepro.ui.document.ActivityDocuments;
import blackflame.com.zymepro.ui.geotag.GeoTagActivity;
import blackflame.com.zymepro.ui.home.multicar.MulticarFragment;
import blackflame.com.zymepro.ui.home.navigation.NavigationFragment;
import blackflame.com.zymepro.ui.home.singlecar.SingleCarFragment;
import blackflame.com.zymepro.ui.liveshare.LiveShareActivity;
import blackflame.com.zymepro.ui.login.LoginActivity;
import blackflame.com.zymepro.ui.message.MessageFromTeam;
import blackflame.com.zymepro.ui.pastnotification.PastNotification;
import blackflame.com.zymepro.ui.refer.ReferActivity;
import blackflame.com.zymepro.ui.setting.SettingActivity;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements MainPresenter.View ,AppRequest,NavigationFragment.FragmentDrawerListener {
  private static final String TAG =MainActivity.class.getCanonicalName() ;
  Toolbar toolbar;
DrawerLayout drawerLayout;
public   ImageView setting,refresh,share_live_tracking;
MainPresenter presenter;
NavigationFragment navigationFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
    presenter=new MainPresenter(this,new MainInteractor());
    Bundle bundle=getIntent().getBundleExtra("bundle");

    Log.d("Activity ==>",""+bundle);

    if (bundle != null) {
      presenter.routeFromNotification(bundle);

    }else{
      openDefaultState();
    }

   // addFragmentWithBackStack(new SingleCarFragment(),false,null);



  }

  private void initViews(){
    toolbar=findViewById(R.id.toolbar);
    drawerLayout=findViewById(R.id.drawer_layout);
    setting=findViewById(R.id.toolbar_setting);
    navigationFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
    navigationFragment.setUp(GlobalReferences.getInstance().baseActivity,R.id.navigation_drawer,drawerLayout,toolbar);
    navigationFragment.setDrawerListener(this);

    setting.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, SettingActivity.class));

      }
    });

    refresh=findViewById(R.id.toolbar_refresh);
    refresh.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        MulticarFragment fragment = (MulticarFragment) getSupportFragmentManager().findFragmentById(R.id.singlecarfragment);
        fragment.refreshData();


      }
    });
    share_live_tracking=findViewById(R.id.toolbar_live_share);
    share_live_tracking.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,LiveShareActivity.class);
        intent.putExtra("carId",CommonPreference.getInstance().getCarId());
        startActivity(intent);
      }
    });


    if (checkPlayServices()) {
      Intent intent = new Intent(MainActivity.this, ProFirebaseMessagingService.class);
      startService(intent);
    }


    updateSetting();



  }

  private boolean checkPlayServices() {
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
      if (apiAvailability.isUserResolvableError(resultCode)) {

        if(!(MainActivity.this ).isFinishing()){
          apiAvailability.getErrorDialog(this, resultCode, 100)
              .show();
        }
      } else {
//                Log.i("MainActivity", "This device is not supported.");
        finish();
      }
      return false;
    }
    return true;
  }
  @Override
  public void openDefaultState() {

   int devicecount=CommonPreference.getInstance().getDeviceCount();
    if(devicecount>1){
      MulticarFragment f1 = new MulticarFragment();
      addFragmentWithBackStack(f1,false,null,"MultiCar");
    }else{
      SingleCarFragment f1 = new SingleCarFragment();
      addFragmentWithBackStack(f1,false,null,"SingleCar");
    }

  }


  private void updateSetting(){
    OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
    OneSignal.setSubscription(true);
    OneSignalUtility.updateTags();
    boolean isEnabled = status.getPermissionStatus().getEnabled();
    boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
    boolean subscriptionSetting = status.getSubscriptionStatus().getUserSubscriptionSetting();
    String userID = status.getSubscriptionStatus().getUserId();
    CommonPreference.getInstance().setPlayerId(userID);

    String pushToken = status.getSubscriptionStatus().getPushToken();
    try{
      JSONObject object=new JSONObject();
      object.put("player_id",userID);
      JSONObject object_common=new JSONObject();
      object_common.put("is_enabled",isEnabled);
      object_common.put("is_subscribed",isSubscribed);
      object_common.put("subscription_setting",subscriptionSetting);
      object.put("push_token",pushToken);
      object.put("notification_setting",object_common.toString());
      Log.e(TAG, "updateTags: "+object );
      ApiRequests.getInstance().update_mobile_setting(GlobalReferences.getInstance().baseActivity,MainActivity.this,object);

    } catch (JSONException ex){


    }
  }







  @Override
  public void openSingleCar(Bundle bundle) {
    Log.d("open single car",bundle.toString());
  SingleCarFragment singleCarFragment=new SingleCarFragment();
  singleCarFragment.setArguments(bundle);
  addFragmentWithBackStack(singleCarFragment,false,bundle);
  }

  @Override
  public void openRateUs() {
    Log.e(TAG, "showRateAppDialog: called" );

//    RateApp.Config config = new RateApp.Config();
//    config.setMessage(message);
//    config.setUrl("https://play.google.com/store/apps/details?id=blackflame.com.zymepro");
//    RateApp.init(config);
//    RateApp.showRateDialog(MainActivity.this);
  }

  @Override
  public void navigateToHome() {

  }

  @Override
  public void doLogout() {

    if (NetworkUtils.isConnected()) {
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put(Constants.EMAIL, CommonPreference.getInstance().getEmail());
        jsonObject.put("token",CommonPreference.getInstance().getFcmToken() );
      }catch (Exception e){
        e.printStackTrace();
      }

      ApiRequests.getInstance().logout(GlobalReferences.getInstance().baseActivity,MainActivity.this,jsonObject);
    } else {
      ToastUtils.showShort(R.string.no_internet);
    }

  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    if (listener.getTag().equals("logout")){
      try{
        JSONObject jObject = listener.getJsonResponse();
        String hit_status = jObject.getString("status");


        if (hit_status.equals("SUCCEESS") || hit_status.equals("SUCCESS")) {
          SettingPreferences.initializeInstance(this);
          SettingPreferences.getInstance().clear();
          CommonPreference.getInstance().clear();

          Intent intent = new Intent(this, LoginActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("logout", 1);
//        OneSignalUtility.removeTags();
//        OneSignal.setSubscription(false);
          startActivity(intent);
          finish();
        }

      }catch (JSONException ex){
        Log.d("Exception ex",""+ex.getCause());

      }
    }
  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {

    doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());

  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    if (listener.getTag().equals("logout")){
      try{
        JSONObject jObject = listener.getJsonResponse();
        String hit_status = jObject.getString("status");


        if (hit_status.equals("SUCCEESS") || hit_status.equals("SUCCESS")) {
          SettingPreferences.initializeInstance(this);
          SettingPreferences.getInstance().clear();
          CommonPreference.getInstance().clear();

          Intent intent = new Intent(this, LoginActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          intent.putExtra("logout", 1);
//        OneSignalUtility.removeTags();
//        OneSignal.setSubscription(false);
          startActivity(intent);
          finish();
        }

      }catch (JSONException ex){
        Log.d("Exception ex",""+ex.getCause());

      }
    }
  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void indexScreen() {

    Analytics.index(MainActivity.this,"MainActivity");

  }

  @Override
  public void onDrawerItemSelected(View view, int position) {
        switch (position){

          case 0:

              int  devicecount=CommonPreference.getInstance().getDeviceCount();
                  if(devicecount>1){
                    MulticarFragment f1 = new MulticarFragment();
                    addFragmentWithBackStack(f1,false,null);

                  }else{

                    SingleCarFragment f1 = new SingleCarFragment();
                    addFragmentWithBackStack(f1,false,null);

                  }

            break;
          case 1:
            Intent past_notification = new Intent(this, PastNotification.class);
            startActivity(past_notification);
            break;
          case 2:
            Intent document = new Intent(this, ActivityDocuments.class);
            startActivity(document);
            break;

          case 3:
            Intent geotag = new Intent(this, GeoTagActivity.class);
            startActivity(geotag);
            break;
          case 4:
            Intent breakdown=new Intent(this,BreakdownActivity.class);
            startActivity(breakdown);
            break;
          case 5:
            Intent dashcam=new Intent(this,DashcamVideo.class);
            startActivity(dashcam);

            break;
          case 6:

            Intent message=new Intent(this,MessageFromTeam.class);
            startActivity(message);

            break;
          case 7:
            Intent refer=new Intent(this,ReferActivity.class);
            startActivity(refer);

            break;
          case 8:
            boolean isConnected = NetworkUtils.isConnected();
            if (isConnected) {
              Intent i = new Intent(Intent.ACTION_SEND);
              i.setType("message/rfc822");
              i.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@getzyme.com"});

              try {
                startActivity(Intent.createChooser(i, "Send mail..."));
              } catch (android.content.ActivityNotFoundException ex) {
                ToastUtils.showShort("There are no email clients installed.");
                }
            }else{
              ToastUtils.showShort("No internet");
            }
            break;


          case 9:
            boolean isConnected_tonetwork = NetworkUtils.isConnected();
            if (isConnected_tonetwork) {
              launchMarket();
            } else {
              ToastUtils.showShort("No internet.");
            }
            break;
          case 10:
            Intent aboutus = new Intent(this, AboutUs.class);
            startActivity(aboutus);
            break;
          case 11:
            new AwesomeInfoDialog(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setColoredCircle(R.color.colorAccent)
                .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText("Yes")
                .setPositiveButtonClick(new Closure() {
                  @Override
                  public void exec() {
                    runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                        doLogout();

                      }
                    });
                  }
                })
                .setNegativeButtonText("No")
                .setNegativeButtonClick(new Closure() {
                  @Override
                  public void exec() {

                  }
                })
                .show();
            break;
              }
  }





  private void launchMarket() {
    Uri uri = Uri.parse("market://details?id=" + getPackageName());
    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
    try {
      startActivity(myAppLinkToMarket);
    } catch (ActivityNotFoundException e) {
      Toast.makeText(this, " Unable to find market app", Toast.LENGTH_LONG).show();
    }
  }
}
