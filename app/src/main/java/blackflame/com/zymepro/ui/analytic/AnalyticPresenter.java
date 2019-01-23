package blackflame.com.zymepro.ui.analytic;

import blackflame.com.zymepro.ui.analytic.listener.AnalyticResponseListener;
import blackflame.com.zymepro.util.UtilityMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnalyticPresenter {
  AnalyticResponseListener listener;
  public AnalyticPresenter(AnalyticResponseListener listener){
    this.listener=listener;
  }
  public void parseData(JSONObject data){
    try {
      JSONObject object_msg=data.getJSONObject("msg");
      String[] date=new String[7];
      int[] max_rpm =new int[7];
      int [] max_speed=new int[7];
      int[] alert_count=new int[14];
      float[] distnace=new float[7];
      float[] active_time=new float[7];
      float [] idle_time=new float[7];
      float[] alert_day=new float[7];
      float[] pie_data_time=new float[4];
      float[] pie_data_distance=new float[4];
      float [] trip_count=new float[7];

      //String total_time_formatted,total_idle_Time;

      int total_alerts=0,avg_speed=0,distance_temp=0,time_temp=0,total_trip_count=0,total_distance=0,total_drive_time=0,total_idle_count=0;
      int count_overspeed=0,count_acceleration=0,count_braking=0,count_lane_change=0,count_sharp=0,count_rash_driving=0,
          count_collision=0,count_theft=0,count_unplug=0,count_geofence=0,
          count_towing_alarm=0,count_low_battery=0,count_high_collant=0,count_long_idling=0;
      JSONArray arr_date = object_msg.getJSONArray("date_arr");
      JSONArray arr_distance=object_msg.getJSONArray("distance_arr");
      JSONArray arr_alert_count=object_msg.getJSONArray("alert_count_arr");
      JSONArray arr_max_speed=object_msg.getJSONArray("max_speed_arr");
      JSONArray arr_max_rpm=object_msg.getJSONArray("max_rpm_arr");
      JSONArray arr_idle_time=object_msg.getJSONArray("idle_time_arr");
      JSONArray arr_active_time=object_msg.getJSONArray("active_time_arr");
      JSONArray arr_trip_count=object_msg.getJSONArray("trip_count_arr");


      for(int k=0;k<7;k++){
        time_temp+=arr_active_time.getInt(k);
        distance_temp+=arr_distance.getInt(k);

      }

      pie_data_distance[0]=getPercentage(time_temp,object_msg.getInt("low_speed_distance"));
      pie_data_distance[1]=getPercentage(time_temp,object_msg.getInt("normal_speed_distance"));
      pie_data_distance[2]=getPercentage(time_temp,object_msg.getInt("high_speed_distance"));
      pie_data_distance[3]=getPercentage(time_temp,object_msg.getInt("over_speed_distance"));
      pie_data_time[0]=getPercentage(time_temp,object_msg.getInt("low_speed_time"));
      pie_data_time[1]=getPercentage(time_temp,object_msg.getInt("normal_speed_time"));
      pie_data_time[2]=getPercentage(time_temp,object_msg.getInt("high_speed_time"));
      pie_data_time[3]=getPercentage(time_temp,object_msg.getInt("over_speed_time"));

//


      for (int i = 0; i < 7; i++) {
        String formattedDate=UtilityMethod.getDateChart(arr_date.getString(i));
        //Log.e(TAG, "onResponse: "+formattedDate );
        date[i]=formattedDate;
        max_rpm[i]=arr_max_rpm.getInt(i);
        max_speed[i]=arr_max_speed.getInt(i);
        int trip=arr_trip_count.getInt(i);
        total_trip_count+=trip;
        trip_count[i]=trip;
        int distance=arr_distance.getInt(i);
        total_distance+=distance;
        distnace[i]=(float)distance/1000;
        int active_temp=arr_active_time.getInt(i);
        total_drive_time+=active_temp;
        active_time[i]=(float) active_temp/60;
        //Log.e(TAG, "onResponse: "+(float) active_temp/60 );
        int idle_temp=arr_idle_time.getInt(i);
        total_idle_count+=idle_temp;
        idle_time[i]=(float) idle_temp/60;

      }

      int index=0;
      for (int j=0;j<arr_alert_count.length();j++){
        JSONObject alert=arr_alert_count.getJSONObject(j);
        int count,total_count=0;
        count=alert.getInt("OVERSPEED");
        count_overspeed+=count;
        total_count+=count;
        count=alert.getInt("SUDDEN_ACCELERATION");
        count_acceleration+=count;
        total_count+=count;
        count=alert.getInt("SUDDEN_BRAKING");
        count_braking+=count;
        total_count+=count;
        count=alert.getInt("SUDDEN_LANE_CHANGE");

        count_lane_change+=count;
        total_count+=count;
        count=alert.getInt("SHARP_TURN");
        count_sharp+=count;
        total_count+=count;
        count=alert.getInt("COLLISION_ALARM");
        count_collision+=count;
        total_count+=count;
        count=alert.getInt("THEFT");
        count_theft+=count;
        total_count+=count;
        count=alert.getInt("UNPLUG");
        count_unplug+=count;
        total_count+=count;
        count=alert.getInt("GEOFENCE");
        count_geofence+=count;
        total_count+=count;
        count=alert.getInt("TOWING_ALARM");
        count_towing_alarm+=count;
        total_count+=count;
        count=alert.getInt("LOW_BATTERY");

        count_low_battery+=count;
        total_count+=count;
        count=alert.getInt("HIGH_COOLANT");

        count_high_collant+=count;
        total_count+=count;
        count=alert.getInt("LONG_IDLING");
        count_long_idling+=count;
        total_count+=count;
        total_alerts+=total_count;
        alert_day[index]=total_count;
        index++;


      }

      alert_count[0]=count_overspeed;
      alert_count[1]=count_acceleration;
      alert_count[2]=count_braking;
      alert_count[3]=count_lane_change;
      alert_count[4]=count_sharp;
      alert_count[5]=count_rash_driving;
      alert_count[6]=count_collision;
      alert_count[7]=count_theft;
      alert_count[8]=count_unplug;
      alert_count[9]=count_geofence;
      alert_count[10]=count_towing_alarm;
      alert_count[11]=count_low_battery;
      alert_count[12]=count_high_collant;
      alert_count[13]=count_long_idling;

      listener.onDateData(date);
      listener.onMaxSpeedData(max_speed);
      listener.onMaxRpmData(max_rpm);
      listener.onDistanceData(distnace);
      listener.onAlertData(alert_count);
      listener.onActiveTime(active_time,idle_time);
      listener.onAlertCount(alert_day);
      listener.onPieData(pie_data_time,pie_data_distance);
      listener.onTripCount(trip_count);
      if (total_drive_time>0){
        avg_speed=(int)(total_distance*3.6/total_drive_time);
      }

      String distance=String.format("%.2f",(float)total_distance/1000);



      listener.onSummaryData(total_trip_count,distance,total_alerts,avg_speed,UtilityMethod.calculateTime(total_drive_time),UtilityMethod.calculateTime(total_idle_count));

    }catch (JSONException ex){
    }
  }
  private float getPercentage(int total,float current){
    return current/total *100 ;
  }





}
