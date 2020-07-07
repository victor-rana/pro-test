package blackflame.com.zymepro.ui.alerts;

import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.alertdetails.AlertDetails;
import blackflame.com.zymepro.ui.alerts.model.Alert;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.LogUtils;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONObject;

public class AlertActivity extends BaseActivity implements OnClickListener,AppRequest,AlertPresenter.View,com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
  private ArrayList<Alert> list;
  private TextView textView_SelectDate,textview_model;
  private String carId;
  int comingfrom=-1;
  CardView overspeeding,hard_acceleration,
      sudden_braking,sudden_lane_change,
      sharp_turn,collision,theft,geofence,
      unplug,towing_alert,low_battery,
      high_coolant,long_idling;
  int count_overspeeding,count_acceleration,count_sudden_braking,count_sudden_lane,count_sharp_turn;
  int count_collision,count_theft,count_geofence,count_unplug,count_towing;
  int count_battery,count_high_coolant,count_long_idling;
  TextView tv_overspeeding,tv_hard_acceleration,
      tv_sudden_braking,tv_sudden_lane_change,
      tv_sharp_turn,tv_collision,tv_theft,tv_geofence,
      tv_unplug,tv_towing_alert,tv_low_battery,
      tv_high_coolant,tv_long_idling;
  ImageView previous,forward;
  SimpleDateFormat simpleDateFormat,df_ist;
  Calendar now;
  String formattedDate;
  AlphaAnimation inAnimation;
  AlphaAnimation outAnimation;

  FrameLayout progressBarHolder;
  LinearLayout buttonView;
  AlertPresenter presenter;
  Calendar [] highlightDays=new Calendar[1];

  Calendar lastSelectedDate;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alert);
    GlobalReferences.getInstance().baseActivity=this;
    CommonPreference.initializeInstance(AlertActivity.this);
    presenter=new AlertPresenter(this);
    list=new ArrayList<>();
    initViews();



  }

  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Alerts");



    this.overspeeding=findViewById(R.id.card_over_speeding);
    this.overspeeding.setOnClickListener(this);
    this.hard_acceleration=findViewById(R.id.card_hard_acceleration);
    this.hard_acceleration.setOnClickListener(this);
    this.sudden_braking=findViewById(R.id.card_sudden_braking);
    this.sudden_braking.setOnClickListener(this);
    this.sudden_lane_change=findViewById(R.id.card_sudden_lane);
    this.sudden_lane_change.setOnClickListener(this);
    this. sharp_turn=findViewById(R.id.card_sharp_turn);
    this. sharp_turn.setOnClickListener(this);
    this.collision=findViewById(R.id.card_collision);
    this.collision.setOnClickListener(this);
    this.theft=findViewById(R.id.card_theft);
    this.theft.setOnClickListener(this);
    this.geofence=findViewById(R.id.card_geofence);
    this.geofence.setOnClickListener(this);
    this.unplug=findViewById(R.id.card_unplug);
    this.unplug.setOnClickListener(this);
    this.towing_alert=findViewById(R.id.card_towing_alert);
    this.towing_alert.setOnClickListener(this);
    this.low_battery=findViewById(R.id.card_low_battery);
    this.low_battery.setOnClickListener(this);
    this.high_coolant=findViewById(R.id.card_high_coolant);
    this.high_coolant.setOnClickListener(this);
    this.long_idling=findViewById(R.id.card_long_idling);
    this.long_idling.setOnClickListener(this);
    this. tv_low_battery= findViewById(R.id.alert_count_battery);
    this. tv_high_coolant= findViewById(R.id.alert_count_high_coolant);
    this. tv_long_idling=  findViewById(R.id.alert_count_long_idling);
    this.tv_collision=  findViewById(R.id.alert_count_collision);
    this. tv_theft=  findViewById(R.id.alert_count_theft);
    this.tv_geofence= findViewById(R.id.alert_count_geofence);
    this.tv_unplug=  findViewById(R.id.alert_count_unplugged);
    this.tv_towing_alert=  findViewById(R.id.alert_count_towing_alert);
    this.tv_hard_acceleration= findViewById(R.id.count_hard_acceleration);
    this.tv_overspeeding= findViewById(R.id.driver_count_over_speed);
    this.tv_sharp_turn= findViewById(R.id.driver_count_sharp_turn);
    this.tv_sudden_braking=  findViewById(R.id.driver_count_sudden_brake);
    this.tv_sudden_lane_change=findViewById(R.id.driver_count_sudden_lane);
    progressBarHolder = findViewById(R.id.progressBarHolder);
    textView_SelectDate=findViewById(R.id.selectDate);
    textView_SelectDate.setOnClickListener(this);
    previous=findViewById(R.id.date_previous);
    forward=findViewById(R.id.date_forward);
    previous.setOnClickListener(this);
    forward.setOnClickListener(this);

    now = Calendar.getInstance();
    simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy");
    String currentDate=simpleDateFormat.format(now.getTime());
    textView_SelectDate.setText(currentDate);
    df_ist=new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.SSSZZZ");
    String ist_time=df_ist.format(now.getTime());
    loadAlert(ist_time);


  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.card_over_speeding:
        if(count_overspeeding>0)
          showDetail("OVERSPEED",count_overspeeding);
        else
          showToast();

        break;
      case R.id.card_hard_acceleration:
        if(count_acceleration>0)
          showDetail("SUDDEN_ACCELERATION",count_acceleration);
        else
          showToast();

        break;
      case R.id.card_sudden_braking:
        if(count_sudden_braking>0)
          showDetail("SUDDEN_BRAKING",count_sudden_braking);

        else
          showToast();

        break;
      case R.id.card_sudden_lane:
        if(count_sudden_lane>0)
          showDetail("SUDDEN_LANE_CHANGE",count_sudden_lane);
        else
          showToast();

        break;
      case R.id.card_sharp_turn:
        if(count_sharp_turn>0)
          showDetail("SHARP_TURN",count_sharp_turn);
        else
          showToast();

        break;
      case R.id.card_collision:
        if(count_collision>0)
          showDetail("COLLISION_ALARM",count_collision);
        else
          showToast();


        break;
      case R.id.card_theft:
        if(count_theft>0)
          showDetail("THEFT",count_theft);

        else
          showToast();

        break;
      case R.id.card_geofence:
        if(count_geofence>0)
          showDetail("GEOFENCE",count_geofence);

        else
          showToast();

        break;
      case R.id.card_unplug:

        if(count_unplug>0)
          showDetail("PULL_OUT_REMINDER",count_unplug);

        else
          showToast();

        break;
      case R.id.card_towing_alert:
        if(count_towing>0)
          showDetail("TOWING_ALARM",count_towing);
        else
          showToast();

        break;
      case R.id.card_low_battery:
        if(count_battery>0)
          showDetail("LOW_BATTERY",count_battery);

        else
          showToast();


        break;
      case R.id.card_high_coolant:
        if(count_high_coolant>0)
          showDetail("HIGH_COOLANT",count_high_coolant);

        else
          showToast();

        break;
      case R.id.card_long_idling:

        if(count_long_idling>0)

          showDetail("LONG_IDLING",count_long_idling);
        else
          showToast();
        break;
      case R.id.date_previous:
        now.add(Calendar.DATE, -1);
        if (!forward.isClickable()) {
          forward.setAlpha(1f);
          forward.setClickable(true);
        }

        lastSelectedDate=now;

        formattedDate = simpleDateFormat.format(now.getTime());
        textView_SelectDate.setText(formattedDate);



        if(NetworkUtils.isConnected()) {
          loadAlert(df_ist.format(now.getTime()));

        }
        break;
      case R.id.date_forward:


        now.add(Calendar.DATE, 1);


        if (isAfterToday(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DATE))){
          forward.setAlpha(0.5f);
          forward.setClickable(false);

          now.add(Calendar.DATE, -1);
          lastSelectedDate=now;
          formattedDate = simpleDateFormat.format(now.getTime());

          Toast.makeText(AlertActivity.this, "Can't select future date", Toast.LENGTH_SHORT).show();                }else {
          formattedDate = simpleDateFormat.format(now.getTime());

          Log.v("NEXT DATE : ", formattedDate);
          textView_SelectDate.setText(formattedDate);
          loadAlert(df_ist.format(now.getTime()));
        }

        break;


      case R.id.selectDate:
        forward.setAlpha(1f);

        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            AlertActivity.this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH));
        dpd.setAccentColor(Color.parseColor("#2da9e1"));
        dpd.setThemeDark(true);
        if (lastSelectedDate ==null){
          lastSelectedDate=now;
        }
        dpd.setTitle("Select Date");
        dpd.setMaxDate(now);
        highlightDays[0]=lastSelectedDate;
        dpd.setHighlightedDays(highlightDays);
        dpd.setYearRange(2017,2020);
        dpd.show((AlertActivity.this).getFragmentManager(), "DatePickerDialog");
        break;



    }

  }

  private void showDetail(String title,int count){
    Intent intent =new Intent(AlertActivity.this,AlertDetails.class);
    intent.putExtra("datalist",list);
    intent.putExtra("title",title);
    intent.putExtra("count",count);
    startActivity(intent);

  }
  public static boolean isAfterToday(int year, int month, int day) {
    Calendar today = Calendar.getInstance();
    Calendar myDate = Calendar.getInstance();

    myDate.set(year, month, day);

    return myDate.after(today);
  }
  private void showToast(){
    ToastUtils.showShort("There are no instances of this alert on the selected date.");
  }

  private void loadAlert(String time){
    String carId=CommonPreference.getInstance().getCarId();
    ApiRequests.getInstance().get_alert_list(GlobalReferences.getInstance().baseActivity,AlertActivity.this,carId,time);
  }



  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {
    inAnimation = new AlphaAnimation(0f, 1f);
    inAnimation.setDuration(200);
    progressBarHolder.setAnimation(inAnimation);
    progressBarHolder.setVisibility(View.VISIBLE);
    previous.setClickable(false);
    forward.setClickable(false);
    textView_SelectDate.setClickable(false);
  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    try{
      presenter.parseData(listener.getJsonResponse());
      outAnimation = new AlphaAnimation(1f, 0f);
      outAnimation.setDuration(200);
      progressBarHolder.setAnimation(outAnimation);
      //progressBarHolder.startAnimation(animation_out);
      progressBarHolder.setVisibility(View.GONE);
      previous.setClickable(true);
      forward.setClickable(true);
      textView_SelectDate.setClickable(true);
      LogUtils.error("Alert",listener.getResponse());

    }catch (Exception ex){

    }
  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {
    outAnimation = new AlphaAnimation(1f, 0f);
    outAnimation.setDuration(200);
    progressBarHolder.setAnimation(outAnimation);
    //progressBarHolder.startAnimation(animation_out);
    progressBarHolder.setVisibility(View.GONE);
    previous.setClickable(true);
    forward.setClickable(true);
    textView_SelectDate.setClickable(true);
    doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

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
    Analytics.index(AlertActivity.this,"AlertActivity");
  }

  @Override
  public void setAlertList(ArrayList<Alert> alert) {
    if (this.list !=null){
      this.list.clear();
      this.list.addAll(alert);
    }

  }

  @Override
  public void setLowBattery(int count) {
    count_battery=count;
    tv_low_battery.setText(String.valueOf(count));

  }

  @Override
  public void setHighCoolant(int count) {
    count_high_coolant=count;
    tv_high_coolant.setText(String.valueOf(count));

  }

  @Override
  public void setLongIdling(int count) {
    count_long_idling=count;
    tv_long_idling.setText(String.valueOf(count));


  }

  @Override
  public void setCollision(int count) {
    count_collision=count;
    tv_collision.setText(String.valueOf(count));

  }

  @Override
  public void setGeofence(int count) {
    count_geofence=count;
    tv_geofence.setText(String.valueOf(count));

  }

  @Override
  public void setTheft(int count) {
    count_theft=count;
    tv_theft.setText(String.valueOf(count));

  }

  @Override
  public void setTowing(int count) {
    count_towing=count;
    tv_towing_alert.setText(String.valueOf(count));

  }

  @Override
  public void setUnplug(int count) {
    count_unplug=count;
    tv_unplug.setText(String.valueOf(count));

  }

  @Override
  public void setAcceleration(int count) {
    count_acceleration=count;
    tv_hard_acceleration.setText(String.valueOf(count));

  }

  @Override
  public void setOverSpeed(int count) {
    count_overspeeding=count;
    tv_overspeeding.setText(String.valueOf(count));

  }

  @Override
  public void setSharpTurn(int count) {
    count_sharp_turn=count;
    tv_sharp_turn.setText(String.valueOf(count));

  }

  @Override
  public void setSuddenBraking(int count) {
    count_sudden_braking=count;
    tv_sudden_braking.setText(String.valueOf(count));

  }

  @Override
  public void setSuddenLane(int count) {
    count_sudden_lane=count;
    tv_sudden_lane_change.setText(String.valueOf(count));

  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    now.set(year, monthOfYear, dayOfMonth);
    String date_selecct=simpleDateFormat.format(now.getTime());
    textView_SelectDate.setText(date_selecct);

    lastSelectedDate=now;

    boolean isConnected=NetworkUtils.isConnected();
    if(isConnected) {
      loadAlert(df_ist.format(now.getTime()));
    }else{
      ToastUtils.showShort("No internet.");
    }


  }
}
