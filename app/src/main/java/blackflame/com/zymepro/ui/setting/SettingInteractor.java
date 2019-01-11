package blackflame.com.zymepro.ui.setting;

import static blackflame.com.zymepro.util.UtilityMethod.getTime;

import android.util.Log;
import android.view.View;
import blackflame.com.zymepro.db.SettingPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingInteractor {


  View view;
  public SettingInteractor(View v){
    this.view=v;
  }

  public  void parseSetting(JSONObject data){
    try {
      JSONObject object_setting=data.getJSONObject("msg");
      if(object_setting.has("geofence_setting")) {
        view.setGeofence(object_setting.getBoolean("geofence_setting"));
      }
      if(object_setting.has("overspeed_setting")) {
        view.setOverSpeed(object_setting.getBoolean("overspeed_setting"));
      }
      if (object_setting.has("theft_setting")) {
        view.setTheft(object_setting.getBoolean("theft_setting"));
      }
      if (object_setting.has("rash_driving_setting")) {
        view.setRashDriving(object_setting.getBoolean("rash_driving_setting"));
      }
      if (object_setting.has("towing_setting")) {
        view.setTowing(object_setting.getBoolean("towing_setting"));
      }
      if (object_setting.has("unplug_setting")) {
        view.setUnplugged(object_setting.getBoolean("unplug_setting"));
      }
      if (object_setting.has("fatigue_driving_setting")) {
        view.setFatigue(object_setting.getBoolean("fatigue_driving_setting"));
      }
      if (object_setting.has("collision_setting")) {
        view.setCollision(object_setting.getBoolean("collision_setting"));
      }
      if (object_setting.has("notification")) {
        JSONObject object_notification = object_setting.getJSONObject("notification");
        if (object_notification.has("start")) {
          view.setTripStart(object_notification.getBoolean("start"));
        }
        if (object_notification.has("end")) {
          view.setTripEnd(object_notification.getBoolean("end"));

        }
      }
      if(object_setting.has("overspeed_limit")) {
        int overspeed=object_setting.getInt("overspeed_limit");
        if(overspeed!=40){
          view.setSpeed(overspeed);

        }
      }


      if(object_setting.has("high_coolant_temp")){
        view.setCoolant(object_setting.getInt("high_coolant_temp"));
      }
      if(object_setting.has("low_battery_voltage")){
        view.setVolatage((float)object_setting.getDouble("low_battery_voltage"));
      }

      if(object_setting.has("long_idling_interval")){
        int  idling=object_setting.getInt("long_idling_interval");
        view.setLondIdling(idling);
      }

      if (object_setting.has("theft")) {
        JSONObject object_theft = object_setting.getJSONObject("theft");
        if(object_theft.has("startTime")){
          Log.e("Setting", "parseSetting: "+object_theft.getInt("startTime")+""+getTime(object_theft.getInt("startTime")) );
          view.setStartTime(getTime(object_theft.getInt("startTime")));
        }
        if (object_theft.has("endTime")){
          view.setEndTime(getTime(object_theft.getInt("endTime")));
        }
      }
      if(object_setting.has("sos_1")){
        JSONArray array_sos=object_setting.getJSONArray("sos_1");
        SettingPreferences.getInstance().setSOSArray(array_sos.toString());
        int length=array_sos.length();
        for (int i=0;i<length;i++){
          JSONObject object=array_sos.getJSONObject(i);

          if (!object.getString("name").equals("null")) {
            String name=object.getString("name");
            String mobile=object.getString("number");
            //if(length==4) {
            if (i == 0) {
              view.setFirstSos(mobile,name);
            }
            if (i == 1) {
              view.setSecondSos(mobile,name);
            }
            if (i == 2) {
              view.setThirdSos(mobile,name);
            }
            if (i == 3) {
              view.setForthSos(mobile,name);
            }
//

//
          }

        }
      }



    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  interface View{
    void setGeofence(boolean val);
    void setFatigue(boolean val);
    void setRashDriving(boolean val);
    void setCollision(boolean val);
    void setUnplugged(boolean val);
    void setRealTime(boolean val);
    void setTowing(boolean val);
    void setTripStart(boolean val);
    void setTripEnd(boolean val);
    void setOverSpeed(boolean val);
    void setTheft(boolean val);
    void setStartTime(String time);
    void setEndTime(String time);
    void setSpeed(int speed);
    void setCoolant(int coolant);
    void setVolatage(float voltage);
    void setLondIdling(int idling);
    void setFirstSos(String mobile,String name);
    void setSecondSos(String mobile,String name);
    void setThirdSos(String mobile,String name);
    void setForthSos(String mobile,String name);






  }


}
