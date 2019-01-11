package blackflame.com.zymepro.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.home.multicar.MulticarFragment;
import blackflame.com.zymepro.ui.home.navigation.NavigationFragment;
import blackflame.com.zymepro.ui.home.singlecar.SingleCarFragment;
import blackflame.com.zymepro.ui.login.fragment.loginfragment.LoginFragment;
import blackflame.com.zymepro.ui.setting.SettingActivity;
import blackflame.com.zymepro.util.ActivityUtils;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements MainPresenter.View ,AppRequest,NavigationFragment.FragmentDrawerListener {

  private static final String TAG =MainActivity.class.getCanonicalName() ;
  Toolbar toolbar;
DrawerLayout drawerLayout;
ImageView setting,refresh,share_live_tracking;
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


      }
    });
    share_live_tracking=findViewById(R.id.toolbar_live_share);
    share_live_tracking.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });







  }
  @Override
  public void openDefaultState() {

   int devicecount=CommonPreference.getInstance().getDeviceCount();
    if(devicecount>1){
      MulticarFragment f1 = new MulticarFragment();
      addFragmentWithBackStack(f1,false,null);
    }else{
      SingleCarFragment f1 = new SingleCarFragment();
      addFragmentWithBackStack(f1,false,null);
    }



  }

  @Override
  public void openSingleCar(Bundle bundle) {
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

  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void onDrawerItemSelected(View view, int position) {

  }
}
