package blackflame.com.zymepro.dialog;

import static blackflame.com.zymepro.Prosingleton.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import blackflame.com.zymepro.R;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NerdModeDialog extends Dialog implements
    android.view.View.OnClickListener {
  TextView tv_dialog_latitude,tv_dialog_rpm,
      tv_dialog_throttle,tv_dialog_gps,tv_dialog_gsm,tv_dialog_load,
      tv_dialog_fuel_trim,tv_dialog_map,tv_dialog_imap,tv_dialog_iat,
      tv_dialog_coolant,tv_dialog_batery,tv_dialog_car_speed,tv_dialog_longitude,tv_dialog_avg_time;
  Chronometer tv_last_ping;
  int ping_count;
  float   total_count;

  public NerdModeDialog(@NonNull Context context) {
    super(context);
  }


  public Activity c;
  public Dialog d;
  public Button yes, no;
  View view_clicked;
  long starttime = 0;
  public NerdModeDialog(Activity a,View view) {
    super(a);

    this.c = a;
    view_clicked=view;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.layout_nerd_mode);
    ViewGroup.LayoutParams params = getWindow().getAttributes();
    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);


    tv_dialog_batery= findViewById(R.id.dialog_tv_battery);
    tv_dialog_coolant= findViewById(R.id.dialog_tv_coolant);
    tv_dialog_latitude= findViewById(R.id.tv_dialog_latitude);
    tv_dialog_longitude= findViewById(R.id.tv_dialog_longitude);
    tv_dialog_rpm= findViewById(R.id.dialog_tv_rpm);
    tv_dialog_throttle= findViewById(R.id.dialog_tv_throttle);
    tv_dialog_gps= findViewById(R.id.dialog_gps_satellite);
    tv_dialog_load= findViewById(R.id.dialog_tv_load);
    tv_dialog_imap= findViewById(R.id.dialog_imap);
    tv_dialog_map= findViewById(R.id.dialog_map);
    tv_dialog_iat= findViewById(R.id.dialog_iat);
    tv_dialog_gsm= findViewById(R.id.dialog_gsm_signal);
    tv_dialog_car_speed= findViewById(R.id.dialog_car_speed);
    ImageView imageView_cancel= findViewById(R.id.dialog_cancel);
    tv_last_ping= findViewById(R.id.dialog_cm_last_ping);
    tv_dialog_avg_time= findViewById(R.id.dialog_avg_ping_time);

    imageView_cancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
        view_clicked.setVisibility(View.GONE);
      }
    });
    tv_last_ping.setBase(SystemClock.elapsedRealtime());

    tv_last_ping.start();



  }

  public void upDateView(JSONObject object){
    try {
      Log.e(TAG, "upDateView: "+"call" );
      ping_count++;
      Log.e(TAG, "upDateView:count "+(SystemClock.elapsedRealtime() - tv_last_ping.getBase()) );
      total_count+= (SystemClock.elapsedRealtime() - tv_last_ping.getBase())/1000;
      Log.e(TAG, "upDateView: counttotal "+total_count);
      tv_last_ping.stop();

      Log.e(TAG, "upDateView: avg "+(total_count/ping_count));

      float average=(total_count/ping_count);

      tv_last_ping.setBase(SystemClock.elapsedRealtime());

      tv_last_ping.start();
      tv_dialog_batery.setText(object.getString("voltage")+" V");
      tv_dialog_coolant.setText(Integer.valueOf(object.getString("coolant"))+" °C");
      tv_dialog_avg_time.setText(String.format("%.2f",average )+" s");


      tv_dialog_gsm.setText(object.getString("gsm_signal_quality"));
      tv_dialog_map.setText(object.getString("air_flow")+" g/s");
      tv_dialog_imap.setText(object.getString("IMAP")+" kpa");
      String engine_load=object.getString("load");
      //Scanner in = new Scanner(engine_load).useDelimiter("[^0-9]+");
      tv_dialog_load.setText(String.valueOf(returnInt(engine_load))+" %");
      tv_dialog_car_speed.setText(object.getString("speed")+" Km/h");
      tv_dialog_iat.setText(object.getString("IAT")+" °C");
      String rpm=object.getString("rpm");
      tv_dialog_rpm.setText(String.valueOf(returnInt(rpm))+" RPM");
      tv_dialog_gps.setText(object.getString("gps_satellite_count"));
      JSONArray coordinates=object.getJSONArray("coordinates");
      if(coordinates.length()>0){
        tv_dialog_latitude.setText(String.valueOf(coordinates.getDouble(0))+" °N");
        tv_dialog_longitude.setText(String.valueOf(String.valueOf(coordinates.getDouble(1)))+" °E");
      }
      tv_dialog_throttle.setText(String.valueOf(object.getInt("absolute_throttle_position"))+" %");








    }catch (JSONException ex){

    }

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {

    }
    dismiss();
  }

  private int returnInt(String value){
    Scanner in = new Scanner(value).useDelimiter("[^0-9]+");
    return  in.nextInt();}
}
