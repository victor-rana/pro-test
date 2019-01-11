package blackflame.com.zymepro.ui.activation;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityActivation extends BaseActivity implements AppRequest {
  private TextView tv_counter;
  private String body,msg,status_message;
  private boolean isResponsereached=true,
      isGpsResponsereached=true,
      isDeviceActive=false,
      isGpsActive=false,
  isConnectionEstablished;
  private CountDownTimer timerDeviceActivate,
      timerGpsActivate;
  private Context context;
  private String imei;
  private TextView tv_over,tv_take_time;
  private int mInterval = 5000; // 5 seconds by default, can be changed later
  private Handler mHandler;
  String connection_status;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_activation);
    GlobalReferences.getInstance().baseActivity=ActivityActivation.this;


    mHandler = new Handler();
    initViews();

  }

  private void initViews(){
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    CommonPreference.initializeInstance(ActivityActivation.this);
    tv_counter= findViewById(R.id.activation_description);
    tv_over=findViewById(R.id.tv_after_over);
    tv_take_time=findViewById(R.id.tv_take_time);
    imei= CommonPreference.getInstance().getImeiForAPI();
    if(imei==null){
      imei=CommonPreference.getInstance().getImei();
    }else if(imei.equals("A")){
      imei=CommonPreference.getInstance().getImei();
    }
    getLastStatus(imei);

    startRepeatingTask();
  }

  private void getLastStatus(String imei){
    ApiRequests.getInstance().get_last_connection(GlobalReferences.getInstance().baseActivity,this,imei);
  }


  Runnable mStatusChecker = new Runnable() {
    @Override
    public void run() {
      try {
        if (!isConnectionEstablished){
          getLastStatus(imei);
        }else if (!isGpsActive){
          String carId=CommonPreference.getInstance().getCarId();
          getGpsStatus(carId);
        }

        //updateStatus(); //this function can change value of mInterval.
      } finally {
        // 100% guarantee that this always happens, even if
        // your update method throws an exception
        mHandler.postDelayed(mStatusChecker, mInterval);
      }
    }
  };

  void startRepeatingTask() {
    mStatusChecker.run();
  }

  void stopRepeatingTask() {
    mHandler.removeCallbacks(mStatusChecker);
  }


  private void getGpsStatus(String carId){
    ApiRequests.getInstance().get_gps_status(GlobalReferences.getInstance().baseActivity,this,carId);
  }




  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    try {

      if (listener.getTag().equals("last_connection")) {
        JSONObject jObject = listener.getJsonResponse();
         connection_status = jObject.getString("connection_status");
         if (connection_status.equals("ESTABLISHED")){
           isConnectionEstablished=true;
           String carId=CommonPreference.getInstance().getCarId();
           getGpsStatus(carId);
         } else if (listener.getTag().equals("gps_status")){
           JSONObject carData = jObject.getJSONObject("msg");
           //distance_last = carData.getInt("distance");
           // trip_status = carData.getBoolean("custom_trip");
           JSONArray array = carData.getJSONArray("last_location");
           if(array.length()>0){
             isGpsActive=true;
             tv_counter.setText("Zyme Pro activated!");
//             Intent intent=new Intent(ActivityActivation.this,ActivityConfirmation.class);
//             intent.putExtra("status",1);
//             CommonPreference.getInstance().setDeviceActivated(true);
//             // edit.apply();
//             startActivity(intent);
             finish();
           }else{
             isGpsActive=false;
           }
         }
      }
    }catch (JSONException ex){

    }
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
  public void onBackPressed() {
    stopRepeatingTask();
    super.onBackPressed();
  }


}
