package blackflame.com.zymepro.ui.setting;

import static blackflame.com.zymepro.util.UtilityMethod.get12HourTime;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings.Global;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.db.SettingPreferences;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.util.LogUtils;
import blackflame.com.zymepro.view.custom.RippleLayout;
import blackflame.com.zymepro.view.custom.SwitchMultiButton;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import java.util.Calendar;
import org.json.JSONObject;

public class SettingActivity extends BaseActivity implements OnCheckedChangeListener,OnClickListener,AppRequest,SettingInteractor.View {
  SwitchCompat switchCompat_trip_end,
      switchCompat_over_speed,
      switchCompat_theft_alert,
      switchCompat_trip_start,
      switchCompat_geofence_breach,
      switchCompat_towing,
      switchCompat_rash_driving,
      switchCompat_collision_alert,
      switchCompat_unplugged,
      switchCompat_fatigue,
      switchCompat_live_traffic;
  TextView editext_start_time,
      editText_end_time;
  Toolbar toolbar_setting;
  TextView textView_title;
  TextView mob1,mob2,
      mob3,mob4,
      tv_name1,
      tv_name2,
      tv_name3,tv_name4;
  private TimePickerDialog tpd;
  boolean isnum1=false,isnum2=false,isnum3=false,isnum4=false;
  LinearLayout linearLayout_theft_alarm;
  int start_selected_hour,start_selected_minute,end_selected_hour,end_selected_minute;
  int start_second,end_second;
  private static final int RESULT_PICK_CONTACT = 2;
  RippleLayout geofenceLayout;
  TextView textview_set_geofence,textview_model,textView_longidling,textView_overspeeding;
  LinearLayout linearLayout_trip_notif,
      linearLayout_over_speeding,
      linearLayout_alert_notif,linearLayout_container_alertnotif;
  ImageView iv_tripnotif,iv_alertnotif;
  SeekBar seekBar_overspeeding;
  SwitchMultiButton switchMultiButton;
  RelativeLayout rl_sos_first,rl_sos_second,rl_sos_third,rl_sos_fourth;
  ImageView imageView_one,imageView_two,imageView_three,imageView_four;
  TextView textView_name_first,textView_name_second,textView_name_third,textView_name_forth,textView_battery,textView_coolant;
  ImageView iv_battery_dec,iv_battery_inc,iv_coolant_inc,iv_coolant_dec;
  float battery_voltage=12.8f;
  int  coolant=108;
  int long_idling=5;
  ImageView iv_idling_dec,iv_idling_inc;
  TextView tv_seeting_save;
  TextView tv_custom_map;
  SettingInteractor presenter;
  String carId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new SettingInteractor(this);
    SettingPreferences.initializeInstance(this);
    carId=CommonPreference.getInstance().getCarId();

    initViews();
  }
  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_setting);
    TextView title=findViewById(R.id.toolbar_title_setting);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Setting");
    editext_start_time = findViewById(R.id.setting_edittext_time_start);
    iv_tripnotif = findViewById(R.id.iv_tripnotification);
    iv_alertnotif = findViewById(R.id.iv_alert_notification);
    linearLayout_theft_alarm = findViewById(R.id.layout_theft_alarm);
    switchCompat_geofence_breach = findViewById(R.id.setting_switch_geofence_breach);
    switchCompat_towing = findViewById(R.id.setting_switch_towing);
    switchCompat_collision_alert = findViewById(R.id.setting_switch_collision_alert);
    switchCompat_rash_driving = findViewById(R.id.setting_switch_rash_driving);
    switchCompat_unplugged = findViewById(R.id.setting_switch_unplugged);
    switchCompat_fatigue = findViewById(R.id.setting_switch_fatigue_driving);
    switchCompat_over_speed = findViewById(R.id.setting_switch_over_speeding);
    switchCompat_theft_alert = findViewById(R.id.setting_switch_theft_alert);
    switchCompat_trip_start = findViewById(R.id.setting_switch_trip_start);
    switchCompat_trip_end = findViewById(R.id.setting_switch_trip_end);
    switchCompat_live_traffic=findViewById(R.id.setting_switch_life_traffic);
    editText_end_time = findViewById(R.id.setting_edittext_time_end);
    textview_set_geofence = findViewById(R.id.btn_setGeofence);
    textView_longidling = findViewById(R.id.textview_longidling);
    geofenceLayout = findViewById(R.id.geofenceset);
    textview_model = findViewById(R.id.toolbar_setting_model);
    tv_seeting_save = findViewById(R.id.toolbar_setting_save);
    tv_custom_map=findViewById(R.id.tv_custom_map);
    textView_battery = findViewById(R.id.tv_battry_voltage);
    textView_coolant = findViewById(R.id.tv_coolant_value);
    iv_battery_dec = findViewById(R.id.iv_battery_dec);
    iv_battery_inc = findViewById(R.id.iv_battery_inc);
    iv_coolant_dec = findViewById(R.id.iv_coolant_dec);
    iv_coolant_inc = findViewById(R.id.iv_coolant_inc);
    iv_idling_dec = findViewById(R.id.iv_idling_dec);
    iv_idling_inc = findViewById(R.id.iv_idling_inc);
    linearLayout_trip_notif = findViewById(R.id.btn_trip_notif);
    linearLayout_alert_notif = findViewById(R.id.btn_alert_notif);
    linearLayout_container_alertnotif = findViewById(R.id.alert_notif_container);
    linearLayout_over_speeding = findViewById(R.id.layout_overspeeding_alarm);
    textView_name_first = findViewById(R.id.tv_name_first);
    textView_name_second = findViewById(R.id.tv_name_second);
    textView_name_third = findViewById(R.id.tv_name_third);
    textView_name_forth = findViewById(R.id.tv_name_forth);
    tv_name1 = findViewById(R.id.title_add_one);
    tv_name2 = findViewById(R.id.title_add_two);
    tv_name3 = findViewById(R.id.title_add_three);
    tv_name4 = findViewById(R.id.title_add_four);
    mob1 = findViewById(R.id.sos_number1);
    mob2 = findViewById(R.id.sos_number2);
    mob3 = findViewById(R.id.sos_number3);
    mob4 = findViewById(R.id.sos_number4);
    imageView_one = findViewById(R.id.image_one);
    imageView_two = findViewById(R.id.image_two);
    imageView_three = findViewById(R.id.image_three);
    imageView_four = findViewById(R.id.image_four);
    seekBar_overspeeding = findViewById(R.id.seekbar_overspeeding);
    textView_overspeeding = findViewById(R.id.textview_overspeeding);
    switchMultiButton = ((SwitchMultiButton) findViewById(R.id.switchmultibutton1)).setText("Night Mode", "Satellite", "More").setOnSwitchListener(onSwitchListener);
    rl_sos_first= findViewById(R.id.rl_sos_first);
    rl_sos_second= findViewById(R.id.rl_sos_second);
    rl_sos_third= findViewById(R.id.rl_sos_third);
    rl_sos_fourth= findViewById(R.id.rl_sos_fourth);
    rl_sos_first.setOnClickListener(this);
    rl_sos_second.setOnClickListener(this);
    rl_sos_third.setOnClickListener(this);
    rl_sos_fourth.setOnClickListener(this);

    switchCompat_geofence_breach.setOnCheckedChangeListener(this);
    switchCompat_towing.setOnCheckedChangeListener(this);
    switchCompat_collision_alert.setOnCheckedChangeListener(this);
    switchCompat_rash_driving.setOnCheckedChangeListener(this);
    switchCompat_unplugged.setOnCheckedChangeListener(this);
    switchCompat_fatigue.setOnCheckedChangeListener(this);
    switchCompat_over_speed.setOnCheckedChangeListener(this);
    switchCompat_theft_alert.setOnCheckedChangeListener(this);
    switchCompat_trip_start.setOnCheckedChangeListener(this);
    switchCompat_trip_end.setOnCheckedChangeListener(this);
    switchCompat_live_traffic.setOnCheckedChangeListener(this);
    iv_coolant_inc.setOnClickListener(this);
    iv_coolant_dec.setOnClickListener(this);
    iv_idling_dec.setOnClickListener(this);
    iv_idling_inc.setOnClickListener(this);

    editext_start_time.setOnClickListener(this);
    editText_end_time.setOnClickListener(this);
    textView_longidling.setText(String.valueOf(long_idling));
    iv_battery_inc.setOnClickListener(this);
    iv_battery_dec.setOnClickListener(this);
    tv_seeting_save.setOnClickListener(this);
    textview_set_geofence.setOnClickListener(this);
    linearLayout_alert_notif.setOnClickListener(this);

    if (CommonPreference.getInstance().getLiveTraffic()){
      switchCompat_live_traffic.setChecked(CommonPreference.getInstance().getLiveTraffic());
    }

    getSetting();


    seekBar_overspeeding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
          textView_overspeeding.setText(String.valueOf(progress * 5 + 40) + " Km/h");
        }
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    String map_type = CommonPreference.getInstance().getMapType();
    if(map_type!=null) {

      switch (map_type) {
        case "MORE":
          switchMultiButton.setSelectedTab(2);
          break;
        case "NIGHT":
          switchMultiButton.setSelectedTab(0);
          tv_custom_map.setVisibility(View.GONE);
          break;
        case "SATELLITE":
          switchMultiButton.setSelectedTab(1);
          tv_custom_map.setVisibility(View.GONE);
          break;
        default:
          switchMultiButton.setSelectedTab(2);
          tv_custom_map.setVisibility(View.VISIBLE);
          break;
      }
    }


  }

  private SwitchMultiButton.OnSwitchListener onSwitchListener = new SwitchMultiButton.OnSwitchListener() {
    @Override
    public void onSwitch(int tab, final String tabText,boolean isUser) {
      Log.e("tag", "onSwitch: position"+tab );
      final int position=tab;

      switch (position){
        case 0:
          CommonPreference.getInstance().setMapType("NIGHT");
          tv_custom_map.setVisibility(View.GONE);
          break;
        case 1:
          CommonPreference.getInstance().setMapType("SATELLITE");
          tv_custom_map.setVisibility(View.GONE);
          break;
        case 2:
          CommonPreference.getInstance().setMapType("MORE");
          tv_custom_map.setVisibility(View.VISIBLE);
          break;

      }

    }
  };

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    switch(buttonView.getId()){
      case R.id.setting_switch_collision_alert:
        SettingPreferences.getInstance().setCollision(isChecked);
        break;
      case R.id.setting_switch_geofence_breach:
        SettingPreferences.getInstance().setGeofence(isChecked);
        if(isChecked){
          geofenceLayout.setVisibility(View.VISIBLE);
        }else{
          geofenceLayout.setVisibility(View.GONE);
        }
        break;
      case R.id.setting_switch_towing:
        SettingPreferences.getInstance().setTowing(isChecked);
        break;
      case  R.id.setting_switch_rash_driving:
        SettingPreferences.getInstance().setRashDriving(isChecked);
        break;
      case R.id.setting_switch_unplugged:
        SettingPreferences.getInstance().setUnplug(isChecked);
        break;
      case R.id.setting_switch_fatigue_driving:
        SettingPreferences.getInstance().setFatigue(isChecked);
        break;
      case R.id.setting_switch_over_speeding:
        SettingPreferences.getInstance().setOverspeeding(isChecked);

        if(isChecked){
          linearLayout_over_speeding.setVisibility(View.VISIBLE);
        }else {
          linearLayout_over_speeding.setVisibility(View.GONE);

        }
        break;
      case R.id.setting_switch_theft_alert:
        SettingPreferences.getInstance().setTheft(isChecked);
        if(isChecked){
          linearLayout_theft_alarm.setVisibility(View.VISIBLE);
        }else {
          linearLayout_theft_alarm.setVisibility(View.GONE);

        }
        break;
      case R.id.setting_switch_trip_start:
        SettingPreferences.getInstance().setTripStart(isChecked);
        break;
      case R.id.setting_switch_trip_end:
        SettingPreferences.getInstance().setTripEnd(isChecked);
        break;
      case R.id.setting_switch_life_traffic:
        CommonPreference.getInstance().setShouldLiveTraffic(isChecked);
        break;

    }

  }

  @Override
  public void onClick(View v) {
    int id=v.getId();
    switch(id) {
      case R.id.btn_alert_notif:
        linearLayout_container_alertnotif.setVisibility(linearLayout_container_alertnotif.isShown()
            ? View.GONE
            : View.VISIBLE);
        iv_alertnotif.setImageDrawable(linearLayout_container_alertnotif.isShown()
            ? getResources().getDrawable(R.drawable.ic_up_arrow)
            : getResources().getDrawable(R.drawable.ic_down_arrow_white));

        break;
      case R.id.btn_trip_notif:
        break;


      case R.id.toolbar_setting_save:
        upLoadSetting();
        break;
      case R.id.iv_idling_dec:
        if(long_idling>1){
          long_idling=long_idling-1;
          textView_longidling.setText(String.valueOf(long_idling));

        }
        break;
      case R.id.iv_idling_inc:
        long_idling=long_idling+1;
        textView_longidling.setText(String.valueOf(long_idling));
        break;
      case R.id.iv_coolant_dec:
        if(coolant>100){
          coolant-=1;
          textView_coolant.setText(coolant+"");
        }
        break;
      case R.id.iv_coolant_inc:
        if(coolant<115){
          coolant+=1;
          textView_coolant.setText(coolant+"");
        }
        break;
      case R.id.btn_setGeofence:
        break;

      case R.id.iv_battery_inc:
        if(battery_voltage<14.0f){
          battery_voltage=battery_voltage+0.1f;
          textView_battery.setText(String.format("%.1f", battery_voltage));
        }
        break;


      case R.id.iv_battery_dec:
        if(battery_voltage>11){
          battery_voltage=battery_voltage-0.1f;
          textView_battery.setText(String.format("%.1f", battery_voltage));
        }
        break;


      case R.id.setting_edittext_time_end:
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(SettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
          @Override
          public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            end_selected_minute = selectedMinute;
            end_selected_hour = selectedHour;
            editText_end_time.setText(get12HourTime(selectedHour,selectedMinute));
          }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select end time");
        mTimePicker.show();
        break;

      case R.id.setting_edittext_time_start:
        Calendar currentTime = Calendar.getInstance();
        int mhour = currentTime.get(Calendar.HOUR_OF_DAY);
        int min = currentTime.get(Calendar.MINUTE);
        TimePickerDialog TimePicker;
        mTimePicker = new TimePickerDialog(SettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
          @Override
          public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            start_selected_minute = selectedMinute;
            start_selected_hour = selectedHour;
            String time=get12HourTime(selectedHour,selectedMinute);
            editext_start_time.setText(time);
          }
        }, mhour, min, false);//Yes 24 hour time
        mTimePicker.setTitle("Select start time");
        mTimePicker.show();
        break;

      case R.id.rl_sos_first:
        String number=mob1.getText().toString().trim();
        if (number.equals("Contact")) {
          clearTag();
          rl_sos_first.setTag("1");
          pickIntent();
        }else{

          new AwesomeInfoDialog(this)
              .setTitle("Alert")
              .setMessage("Are you sure you want to change your sos contact")
              .setColoredCircle(R.color.colorAccent)
              .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
              .setCancelable(true)
              .setPositiveButtonText("Edit")
              .setPositiveButtonbackgroundColor(R.color.colorAccent)
              .setPositiveButtonTextColor(R.color.white)
              .setNegativeButtonText("Delete")
              .setNegativeButtonbackgroundColor(R.color.colorAccent)
              .setNegativeButtonTextColor(R.color.white)
              .setPositiveButtonClick(new Closure() {
                @Override
                public void exec() {
                  //click
                  clearTag();
                  rl_sos_first.setTag("1");
                  //btn_Save.setVisibility(View.VISIBLE);

                  pickIntent();
                }
              })
              .setNegativeButtonClick(new Closure() {
                @Override
                public void exec() {
                  //click

                  tv_name1.setText("Add");
                  mob1.setText("Contact");
                  textView_name_first.setVisibility(View.GONE);
                  imageView_one.setVisibility(View.VISIBLE);
                  clearTag();
                  SettingPreferences.getInstance().setName1("Add");
                  SettingPreferences.getInstance().setMobile1("Contact");
                  onRestart();
                }
              })
              .show();


        }

        break;
      case R.id.rl_sos_second:
        String number1=mob2.getText().toString().trim();
        if (number1.equals("Contact")) {
          clearTag();
          rl_sos_second.setTag("2");
          //btn_Save.setVisibility(View.VISIBLE);
          pickIntent();
        }else{
          new AwesomeInfoDialog(this)
              .setTitle("Alert")
              .setMessage("Are you sure you want to change your sos contact")
              .setColoredCircle(R.color.colorAccent)
              .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
              .setCancelable(true)
              .setPositiveButtonText("Edit")
              .setPositiveButtonbackgroundColor(R.color.colorAccent)
              .setPositiveButtonTextColor(R.color.white)
              .setNegativeButtonText("Delete")
              .setNegativeButtonbackgroundColor(R.color.colorAccent)
              .setNegativeButtonTextColor(R.color.white)
              .setPositiveButtonClick(new Closure() {
                @Override
                public void exec() {
                  //click
                  clearTag();
                  rl_sos_second.setTag("2");
                  pickIntent();
                }
              })
              .setNegativeButtonClick(new Closure() {
                @Override
                public void exec() {
                  //click

                  tv_name2.setText("Add");
                  mob2.setText("Contact");
                  textView_name_second.setVisibility(View.GONE);
                  imageView_two.setVisibility(View.VISIBLE);
                  clearTag();
                  SettingPreferences.getInstance().setName2("Add");
                  SettingPreferences.getInstance().setMobile2("Contact");
                }
              })
              .show();
        }
        break;
      case R.id.rl_sos_third:
        String number2=mob3.getText().toString().trim();
        if (number2.equals("Contact")) {
          clearTag();
          rl_sos_third.setTag("3");
          //btn_Save.setVisibility(View.VISIBLE);
          pickIntent();
        }else{
          new AwesomeInfoDialog(this)
              .setTitle("Alert")
              .setMessage("Are you sure you want to change your sos contact")
              .setColoredCircle(R.color.colorAccent)
              .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
              .setCancelable(true)
              .setPositiveButtonText("Edit")
              .setPositiveButtonbackgroundColor(R.color.colorAccent)
              .setPositiveButtonTextColor(R.color.white)
              .setNegativeButtonText("Delete")
              .setNegativeButtonbackgroundColor(R.color.colorAccent)
              .setNegativeButtonTextColor(R.color.white)
              .setPositiveButtonClick(new Closure() {
                @Override
                public void exec() {
                  //click
                  clearTag();
                  rl_sos_third.setTag("3");
                  //btn_Save.setVisibility(View.VISIBLE);
                  pickIntent();
                }
              })
              .setNegativeButtonClick(new Closure() {
                @Override
                public void exec() {
                  //click
                  tv_name3.setText("Add");
                  mob3.setText("Contact");
                  textView_name_third.setVisibility(View.GONE);
                  imageView_three.setVisibility(View.VISIBLE);
                  clearTag();
                  SettingPreferences.getInstance().setName3("Add");
                  SettingPreferences.getInstance().setMobile3("Contact");
                }
              })
              .show();


        }
        break;
      case R.id.rl_sos_fourth:
        String number4=mob4.getText().toString().trim();
        if (number4.equals("Contact")) {
          clearTag();
          rl_sos_fourth.setTag("4");
          // btn_Save.setVisibility(View.VISIBLE);

          pickIntent();
        }else{
          //String number_1=mob1.getText().toString();

          new AwesomeInfoDialog(this)
              .setTitle("Alert")
              .setMessage("Are you sure you want to change your sos contact")
              .setColoredCircle(R.color.colorAccent)
              .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
              .setCancelable(true)
              .setPositiveButtonText("Edit")
              .setPositiveButtonbackgroundColor(R.color.colorAccent)
              .setPositiveButtonTextColor(R.color.white)
              .setNegativeButtonText("Delete")
              .setNegativeButtonbackgroundColor(R.color.colorAccent)
              .setNegativeButtonTextColor(R.color.white)
              .setPositiveButtonClick(new Closure() {
                @Override
                public void exec() {
                  //click
                  clearTag();
                  rl_sos_fourth.setTag("4");
                  // btn_Save.setVisibility(View.VISIBLE);
                  pickIntent();
                }
              })
              .setNegativeButtonClick(new Closure() {
                @Override
                public void exec() {
                  //click
                  tv_name4.setText("Add");
                  mob4.setText("Contact");
                  textView_name_forth.setVisibility(View.GONE);
                  imageView_four.setVisibility(View.VISIBLE);
                  clearTag();
                  SettingPreferences.getInstance().setName4("Add");
                  SettingPreferences.getInstance().setMobile4("Contact");
                }
              })
              .show();
        }
        break;




    }



  }
  private void clearTag(){
    rl_sos_first.setTag("");
    rl_sos_second.setTag("");
    rl_sos_third.setTag("");
    rl_sos_fourth.setTag("");
  }
  private void pickIntent(){

    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
    startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {
  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {


    try{
      JSONObject data=listener.getJsonResponse();
      presenter.parseSetting(data);

    }catch (Exception ex){

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
    LogUtils.error("setting",""+listener.getJsonResponse());
  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
  }

  @Override
  public void onResponse(JSONObject response) {

  }
  public void upLoadSetting(){
    try{
      JSONObject params=new JSONObject();
      params.put("car_id",CommonPreference.getInstance().getCarId());
      String mobile1=mob1.getText().toString();
      String mobile2=mob2.getText().toString();
      String mobile3=mob3.getText().toString();
      String mobile4=mob4.getText().toString();
      boolean isTheft=SettingPreferences.getInstance().getTheft();

      if(isTheft){
        start_second=start_selected_hour*3600+start_selected_minute*60;
        end_second=end_selected_hour*3600+end_selected_minute*60;
      }
      String minute_text=textView_longidling.getText().toString();
      if(minute_text!=null){
          params.put("long_idling_interval",String.valueOf(Integer.valueOf(minute_text)));
        }
      params.put("long_idling_setting","ENABLED");
      boolean isOverSpeeding=SettingPreferences.getInstance().getOverspeeding();
      if(isOverSpeeding){
       String  over_speed_text=textView_overspeeding.getText().toString();
        String overspeed=over_speed_text.substring(0,over_speed_text.length()-5);
        params.put("overspeed_limit",overspeed);
      }
      params.put("overspeed_setting",isOverSpeeding?"ENABLED":"DISABLED");
      params.put("geofence_setting",SettingPreferences.getInstance().getGeofence()?"ENABLED":"DISABLED");
      params.put("theft_setting",isTheft?"ENABLED":"DISABLED");
      if(isTheft) {
        SettingPreferences.getInstance().setStartTime(editext_start_time.getText().toString());
        SettingPreferences.getInstance().setEndtTime(editText_end_time.getText().toString());
        params.put("start_time",String.valueOf(start_second));
        params.put("end_time",String.valueOf(end_second));
      }
      params.put("fatigue_driving_setting",SettingPreferences.getInstance().getFatigue()?"ENABLED":"DISABLED");
      params.put("rash_driving_setting",SettingPreferences.getInstance().getRashDriving()?"ENABLED":"DISABLED");
      params.put("towing_setting",SettingPreferences.getInstance().getTowing()?"ENABLED":"DISABLED");
      params.put("unplug_setting",SettingPreferences.getInstance().getUnplug()?"ENABLED":"DISABLED");
      params.put("start_notification",SettingPreferences.getInstance().getTripStart()?"ENABLED":"DISABLED");
      params.put("end_notification",SettingPreferences.getInstance().getTripEnd()?"ENABLED":"DISABLED");
      params.put("collision_setting",SettingPreferences.getInstance().getCollision()?"ENABLED":"DISABLED");
      params.put("low_battery_voltage_setting","ENABLED");
      params.put("low_battery_voltage",textView_battery.getText().toString());
      params.put("high_coolant_temp_setting","ENABLED");
      params.put("high_coolant_temp",textView_coolant.getText().toString());

      if(!(mobile1.equals("Contact"))){
        isnum1=true;
        String name=tv_name1.getText().toString();
        String mobile=mob1.getText().toString();
        SettingPreferences.getInstance().setName1(name);
        SettingPreferences.getInstance().setSos(true);
        SettingPreferences.getInstance().setMobile1(mobile);

        params.put("sos_name_1",name);
        params.put("sos_1",mobile);
      }
      if(!(mobile2.equals("Contact"))){
        isnum2=true;

        String name=tv_name2.getText().toString();
        String mobile=mob2.getText().toString();
        SettingPreferences.getInstance().setName2(name);
        SettingPreferences.getInstance().setMobile2(mobile);
        SettingPreferences.getInstance().setSos(true);
        params.put("sos_name_2",name);
        params.put("sos_2",mobile);
      }

      if(!(mobile3.equals("Contact"))){
        isnum3=true;
        String name=tv_name3.getText().toString();
        String mobile=mob3.getText().toString();
        SettingPreferences.getInstance().setName3(name);
        SettingPreferences.getInstance().setSos(true);
        SettingPreferences.getInstance().setMobile3(mobile);
        params.put("sos_name_3",name);
        params.put("sos_3",mobile);
      }
      if(!(mobile4.equals("Contact"))){
        isnum4=true;
        String name=tv_name4.getText().toString();
        String mobile=mob4.getText().toString();
        SettingPreferences.getInstance().setName4(name);
        SettingPreferences.getInstance().setMobile4(mobile);
        SettingPreferences.getInstance().setSos(true);
        params.put("sos_name_4",tv_name4.getText().toString());
        params.put("sos_4",mob4.getText().toString());
      }
      ApiRequests.getInstance().uploadSetting(GlobalReferences.getInstance().baseActivity,SettingActivity.this,params);
    }catch (Exception ex){


    }  }
  public void getSetting(){
    ApiRequests.getInstance().get_setting(GlobalReferences.getInstance().baseActivity,SettingActivity.this,carId);
  }
  @Override
  public void setGeofence(boolean val) {
    switchCompat_geofence_breach.setChecked(val);
  }
  @Override
  public void setFatigue(boolean val) {
    switchCompat_fatigue.setChecked(val);
  }

  @Override
  public void setRashDriving(boolean val) {
    switchCompat_rash_driving.setChecked(val);

  }

  @Override
  public void setCollision(boolean val) {
    switchCompat_collision_alert.setChecked(val);

  }

  @Override
  public void setUnplugged(boolean val) {
    switchCompat_unplugged.setChecked(val);

  }

  @Override
  public void setRealTime(boolean val) {


  }

  @Override
  public void setTowing(boolean val) {
    switchCompat_towing.setChecked(val);

  }

  @Override
  public void setTripStart(boolean val) {
    switchCompat_trip_start.setChecked(val);

  }

  @Override
  public void setTripEnd(boolean val) {
    switchCompat_trip_end.setChecked(val);

  }

  @Override
  public void setOverSpeed(boolean val) {
    switchCompat_over_speed.setChecked(val);

  }

  @Override
  public void setTheft(boolean val) {
    switchCompat_theft_alert.setChecked(val);

  }

  @Override
  public void setStartTime(String time) {
    editext_start_time.setText(time);

  }

  @Override
  public void setEndTime(String time) {

    editText_end_time.setText(time);
  }

  @Override
  public void setSpeed(int speed) {
    if(speed!=40){
      final int progress=120/(speed-40);
      seekBar_overspeeding.setProgress(0);
      seekBar_overspeeding.setMax(16);
      seekBar_overspeeding.post(new Runnable() {
        @Override
        public void run() {
          seekBar_overspeeding.setProgress(16-progress);
        }
      });
    }

    textView_overspeeding.setText(speed+" Km/h");


  }

  @Override
  public void setCoolant(int coolant) {
    textView_coolant.setText(String.valueOf(coolant));
  }

  @Override
  public void setVolatage(float voltage) {
    textView_battery.setText(String.valueOf(voltage));
  }
  @Override
  public void setLondIdling(int idling) {
    textView_longidling.setText(String.valueOf(idling));

  }

  @Override
  public void setFirstSos(String mobile, String name) {
    if(name!=null&&name.length()>0){
      textView_name_first.setVisibility(View.VISIBLE);
      textView_name_first.setText(String.valueOf(name.charAt(0)).toUpperCase());
      imageView_one.setVisibility(View.GONE);
    }
    tv_name1.setText(name);
    mob1.setText(mobile);
  }

  @Override
  public void setSecondSos(String mobile, String name) {
    if(name!=null&&name.length()>0){
      textView_name_second.setVisibility(View.VISIBLE);
      textView_name_second.setText(String.valueOf(name.charAt(0)).toUpperCase());
      imageView_two.setVisibility(View.GONE);
    }
    tv_name2.setText(name);
    mob2.setText(mobile);
  }

  @Override
  public void setThirdSos(String mobile, String name) {
    if(name!=null&&name.length()>0){
      textView_name_third.setVisibility(View.VISIBLE);
      textView_name_third.setText(String.valueOf(name.charAt(0)).toUpperCase());
      imageView_three.setVisibility(View.GONE);
    }
    tv_name3.setText(name);
    mob3.setText(mobile);
  }

  @Override
  public void setForthSos(String mobile, String name) {
    if(name!=null&&name.length()>0){
      textView_name_forth.setVisibility(View.VISIBLE);
      textView_name_forth.setText(String.valueOf(name.charAt(0)).toUpperCase());
      imageView_four.setVisibility(View.GONE);
    }
    tv_name4.setText(name);
    mob4.setText(mobile);
  }



  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // check whether the result is ok
    if (resultCode == RESULT_OK) {
      // Check for the request code, we might be usign multiple startActivityForReslut
      switch (requestCode) {
        case RESULT_PICK_CONTACT:
          contactPicked(data);
          break;
      }
    } else {
//            Log.e("MainActivity", "Failed to pick contact");
    }
  }
  private void contactPicked(Intent data) {
    Cursor cursor = null;
    try {
      String phoneNo = null ;
      String name = null;
      // getData() method will have the Content Uri of the selected contact
      Uri uri = data.getData();
      //Query the content uri
      cursor = getContentResolver().query(uri, null, null, null, null);
      cursor.moveToFirst();
      // column index of the phone number
      int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
      // column index of the contact name
      int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
      phoneNo = cursor.getString(phoneIndex);
      name = cursor.getString(nameIndex);

      if(rl_sos_first.getTag()!=null&&rl_sos_first.getTag().equals("1")){
        tv_name1.setText(name);
        mob1.setText(phoneNo);
        imageView_one.setVisibility(View.GONE);
        textView_name_first.setVisibility(View.VISIBLE);
        if(name!=null&&name.length()>0){
          Log.e("tag", "contactPicked: "+name.charAt(0) );

          textView_name_first.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }

      }
      if(rl_sos_second.getTag()!=null&&rl_sos_second.getTag().equals("2")){
        tv_name2.setText(name);
        mob2.setText(phoneNo);
        imageView_two.setVisibility(View.GONE);
        textView_name_second.setVisibility(View.VISIBLE);
        if(name!=null&&name.length()>0){
          Log.e("tag", "contactPicked: "+name.charAt(0) );
          textView_name_second.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }
      }
      if(rl_sos_third.getTag()!=null&&rl_sos_third.getTag().equals("3")){
        tv_name3.setText(name);
        mob3.setText(phoneNo);
        imageView_three.setVisibility(View.GONE);
        textView_name_third.setVisibility(View.VISIBLE);
        if(name!=null&&name.length()>0){
          Log.e("tag", "contactPicked: "+name.charAt(0) );
          textView_name_third.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }
      }
      if(rl_sos_fourth.getTag()!=null&&rl_sos_fourth.getTag().equals("4")){
        tv_name4.setText(name);
        mob4.setText(phoneNo);
        imageView_four.setVisibility(View.GONE);
        textView_name_forth.setVisibility(View.VISIBLE);
        if(name!=null&&name.length()>0){
          Log.e("tag", "contactPicked: "+name.charAt(0) );
          textView_name_forth.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }
      }
      clearTag();


    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
