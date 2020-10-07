package blackflame.com.zymepro.base;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;


import org.json.JSONException;
import org.json.JSONObject;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.db.Pref;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.receiver.ConnectivityReceiver;
import blackflame.com.zymepro.ui.home.MainActivity;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public abstract class BaseActivity extends BaseActivityParent implements AppRequest, ConnectivityReceiver.ConnectivityReceiverListener {

  private static final String TAG = BaseActivity.class.getCanonicalName();
  protected FragmentManager fragmentManager;
  protected FragmentTransaction fragmentTransaction;
  IntentFilter intentFilter;
  ConnectivityReceiver receiver;
  AlertDialog dialog_show;

  @Override
  public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {

    super.onCreate(savedInstanceState, persistentState);
    GlobalReferences.getInstance().baseActivity =this;
    GlobalReferences.getInstance().pref  = new Pref(this);
    CommonPreference.initializeInstance(this);
    //fragmentManager =getSupportFragmentManager();

    intentFilter = new IntentFilter();
    intentFilter.addAction(CONNECTIVITY_ACTION);
    receiver = new ConnectivityReceiver();
    IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    this.registerReceiver(receiver, intentFilter);

//    Tovuti.from(this).monitor(new Monitor.ConnectivityListener(){
//      @Override
//      public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast){
//        Log.e(TAG, "onConnectivityChanged: "+isConnected );
//        showSnack(isConnected);
//      }
//    });

    ReactiveNetwork.observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(isConnectedToInternet -> {
              // do something with isConnectedToInternet value
              showSnack(isConnectedToInternet);



            })
    ;

  }
  public void setToolbar(Toolbar toolbar,TextView tv,String title){
    tv.setText(title);

    if (toolbar != null) {
      try {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
      }catch (NullPointerException e){}
    }
  }


  public void doGlobalLogout(VolleyError error,JSONObject data){

    try{

      NetworkResponse response = error.networkResponse;


      if (data.has("auth") && !data.getBoolean("auth")){
        Toast.makeText(BaseActivity.this,"Error base" +data ,Toast.LENGTH_SHORT).show();
        if (NetworkUtils.isConnected()) {
          JSONObject jsonObject = new JSONObject();
          try {
            jsonObject.put(Constants.EMAIL, CommonPreference.getInstance().getEmail());
            jsonObject.put("token",CommonPreference.getInstance().getFcmToken() );

            Toast.makeText(this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
          }catch (Exception e){
            e.printStackTrace();
          }

          ApiRequests.getInstance().logout(GlobalReferences.getInstance().baseActivity, BaseActivity.this,jsonObject);
        } else {
          ToastUtils.showShort(R.string.no_internet);
        }

      }
    }catch (Exception e){

      Log.e(TAG, "onRequestFailed: "+e );

    }



  }







  @Override
  protected void onPostResume() {
    super.onPostResume();
  }

  public void addFragmentWithBackStack(Fragment fragment,int id, boolean addToBackStack){
    try {
      GlobalReferences.getInstance().mCommonFragment = (CommonFragment) fragment;
      String backStateName = fragment.getClass().getName();
      String fragmentTag = backStateName;
      FragmentManager manager = getSupportFragmentManager();
      if(manager!=null) {

        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
          //barber.flycuts.com.flycutsbarber.fragment not in back stack, create it.

          FragmentTransaction ft = manager.beginTransaction();
          //  ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
          ft.replace(id, fragment, fragmentTag);

          // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
          if (addToBackStack)
            ft.addToBackStack(backStateName);
          ft.commitAllowingStateLoss();
        } else {

        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  public void addFragmentWithBackStack(Fragment fragment, boolean addToBackStack,Bundle bundle,String tag){
    try {
      String backStateName = fragment.getClass().getName();
      String fragmentTag = tag;
      Log.e(TAG, "addFragmentWithBackStack: name"+fragmentTag );
      final FragmentManager manager = getSupportFragmentManager();
      if(manager!=null) {

        Log.e("manager.findFra)", manager.findFragmentByTag(fragmentTag) + "");

        Fragment fragment1 = manager.findFragmentByTag(fragmentTag);

        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.

          FragmentTransaction ft = manager.beginTransaction();
          if (bundle!=null) {
            fragment.setArguments(bundle);
          }
          //  ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
          ft.replace(R.id.singlecarfragment, fragment, fragmentTag);

          // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
          if (addToBackStack)
            ft.addToBackStack(backStateName);
          ft.commitAllowingStateLoss();
        } else {
          if (fragmentTag.equals("SingleCar")){
            FragmentTransaction ft = manager.beginTransaction();
            if (bundle!=null) {
              fragment.setArguments(bundle);
            }

            ft.replace(R.id.singlecarfragment, fragment, fragmentTag);
            if (addToBackStack)
              ft.addToBackStack(backStateName);
            ft.commitAllowingStateLoss();
          }


          Log.e("alreadyyy", "aleadyyy");
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }




  @Override
  protected void onResume() {
    super.onResume();
    onPostResume();


      fragmentManager =getSupportFragmentManager();
    Log.e("manager",getSupportFragmentManager()+"");
  }

    @Override
    protected void onPause() {

        super.onPause();
    }

  @Override
  protected void onStop() {
    super.onStop();
//    Tovuti.from(this).stop();

  }

  public void replaceFragmentWithAnimation(Fragment fragment, String tag){
    Fragment currentFragment = (Fragment)GlobalReferences.getInstance().mCommonFragment;
    FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
    fragTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.enter_from_right, R.anim.enter_from_right,R.anim.enter_from_right);
    fragTransaction.detach(currentFragment);
    fragTransaction.attach(currentFragment);
    fragTransaction.commitAllowingStateLoss();
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item){

    int id = item.getItemId();
    switch (id) {
      case android.R.id.home:

        finish();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, Constants.RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, Constants.RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, Constants.RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {

  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void onNetworkConnectionChanged(boolean isConnected) {
    showSnack(isConnected);

  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    try {

      unregisterReceiver(receiver);
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }


  private  void showSnack(boolean isConnected) {
    String message;
    int color;
    if (isConnected) {
      message = "Connected to Internet";
      color = Color.WHITE;
      if (dialog_show!=null){
        dialog_show.hide();
      }
    } else {
      message = "Sorry! Not connected to internet";
      color = Color.RED;
      //  final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
      final AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
      dialog.setMessage(message);
      dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          System.exit(0);
        }
      });
      dialog.setNegativeButton("Setting", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          try {
            //Intent intent = new Intent(Intent.ACTION_MAIN);
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
//                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//                        startActivity(intent);
          }catch (ActivityNotFoundException ex){
            dialogInterface.dismiss();
          }
        }
      });
      dialog.setCancelable(false);
      dialog_show=dialog.create();
      if(!(BaseActivity.this).isFinishing()) {
        dialog_show.show();
      }
    }


    // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }


  public abstract void indexScreen();

}
