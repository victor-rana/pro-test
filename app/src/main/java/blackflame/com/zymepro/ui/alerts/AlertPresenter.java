package blackflame.com.zymepro.ui.alerts;

import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import blackflame.com.zymepro.ui.alerts.model.Alert;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlertPresenter {
  View view;
  public AlertPresenter(View v){
    this.view=v;
  }









  public void parseData(JSONObject data){
    try {

      ArrayList<Alert> list=new ArrayList<>();

      JSONObject jsonObject=data.getJSONObject("msg");
      JSONArray alert_data = jsonObject.getJSONArray("alert_list");
      int length=alert_data.length();
      for (int i = 0; i < length; i++) {
        JSONObject object = alert_data.getJSONObject(i);
        Alert alert = new Alert();
        JSONArray array_location = object.getJSONArray("location");
        if (array_location.length() > 0) {
          alert.setLatitude(array_location.getDouble(0));
          alert.setLongitude(array_location.getDouble(1));
        }
        alert.setAlertName(object.getString("alert_type"));
        alert.setTime(object.getString("time"));
        alert.setAddress(object.getString("address"));
        list.add(alert);
      }

      view.setAlertList(list);

      final JSONObject alert_countData = jsonObject.getJSONObject("alert_count");
      if (alert_countData.length()>0) {
        if (alert_countData.has("LOW_BATTERY")) {
          view.setLowBattery(alert_countData.getInt("LOW_BATTERY"));
        }
        if (alert_countData.has("HIGH_COOLANT")) {
          view.setHighCoolant(alert_countData.getInt("HIGH_COOLANT"));

        }
        if (alert_countData.has("LONG_IDLING")) {
          view.setLongIdling(alert_countData.getInt("LONG_IDLING"));
        }
        if (alert_countData.has("COLLISION_ALARM")) {
          view.setCollision(alert_countData.getInt("COLLISION_ALARM"));
        }
        if (alert_countData.has("GEOFENCE")) {
          view.setGeofence(alert_countData.getInt("GEOFENCE"));
        }
        if (alert_countData.has("THEFT")) {
          view.setTheft(alert_countData.getInt("THEFT"));
        }
        if (alert_countData.has("TOWING_ALARM")) {
          view.setTowing(alert_countData.getInt("TOWING_ALARM"));
        }
        if (alert_countData.has("UNPLUG")) {
          view.setUnplug(alert_countData.getInt("UNPLUG"));
        }
        if (alert_countData.has("SUDDEN_ACCELERATION")) {
          view.setSuddenBraking(alert_countData.getInt("SUDDEN_ACCELERATION"));
        }
        if (alert_countData.has("OVERSPEED")) {
          view.setOverSpeed(alert_countData.getInt("OVERSPEED"));
        }
        if (alert_countData.has("SHARP_TURN")) {
          view.setSharpTurn(alert_countData.getInt("SHARP_TURN"));
        }
        if (alert_countData.has("SUDDEN_BRAKING")) {
          view.setSuddenBraking(alert_countData.getInt("SUDDEN_BRAKING"));
        }
        if (alert_countData.has("SUDDEN_LANE_CHANGE")) {
          view.setSuddenLane(alert_countData.getInt("SUDDEN_LANE_CHANGE"));
          
        }
      }else{

        view.setLowBattery(0);
        view.setHighCoolant(0);
        view.setLongIdling(0);
        view.setCollision(0);
        view.setGeofence(0);
        view. setTheft(0);
        view.setTowing(0);
        view.setUnplug(0);
        view.setAcceleration(0);
        view. setOverSpeed(0);
        view.setSharpTurn(0);
        view. setSuddenBraking(0);
        view.setSuddenLane(0);
      }




    }catch (JSONException ex){
      //   Log.e("Tag", "onResponse: "+ ex.getMessage() );

    }
  }





  interface View{
    void setAlertList(ArrayList<Alert> list);
    void setLowBattery(int count);
    void setHighCoolant(int count);
    void setLongIdling(int count);
    void setCollision(int count);
    void setGeofence(int count);
    void setTheft(int count);
    void setTowing(int count);
    void setUnplug(int count);
    void setAcceleration(int count);
    void setOverSpeed(int count);
    void setSharpTurn(int count);
    void setSuddenBraking(int count);
    void setSuddenLane(int count);
  }

}
