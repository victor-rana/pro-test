package blackflame.com.zymepro.ui.tripdetails;
import blackflame.com.zymepro.util.UtilityMethod;
import java.text.DecimalFormat;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsPresenter {
  View view;

    public DetailsPresenter(View view){
      this.view=view;
    }



  public void parseData(JSONObject object){
    try {
      int distance=0;

      JSONObject jsonObject_TripData=object.getJSONObject("msg");
      JSONObject tripObject=jsonObject_TripData.getJSONObject("trip_data");
      int time=tripObject.getInt("trip_time");
      String startTime=tripObject.getString("start_time");
      String endTime=tripObject.getString("end_time");
      if(tripObject.has("max_speed")) {
        String max_speed = tripObject.getString("max_speed");
        if(max_speed==null){
          view.setMaxSpeed("--");
        }else {
          view.setMaxSpeed(max_speed + " km/h");
        }

      }
      if(tripObject.has("max_rpm")) {
       String max_rpm = tripObject.getString("max_rpm");
        if(max_rpm==null){
          view.setMaxRpm("--");
          }else{
          view.setMaxRpm(max_rpm +" RPM");
        }
      }

      if (tripObject.has("alert_count")) {
        String alerts = tripObject.getString("alert_count");

        if(alerts==null) {
          view.setAlerts("--");

        }else{
          view.setAlerts(alerts+" #");
        }
      }
      if(tripObject.has("idle_time")) {
       String idle_time = tripObject.getString("idle_time");
        if(idle_time==null){
          view.setIdleTime("--");

        }else{
          view.setIdleTime(UtilityMethod.timeConversion(Integer.valueOf(idle_time),true));
        }
      }

      if(tripObject.has("distance")){
        distance = tripObject.getInt("distance");
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        double distance_round= Double.valueOf(twoDForm.format((double)distance/1000));
        view.setDistance(""+distance_round+" km");
      }

      if(!tripObject.isNull("fuel_cost")) {
       double fuelCost = tripObject.getDouble("fuel_cost");
       view.setFuelcost("Rs ".concat(String.valueOf((int)fuelCost)));
      }else{
        view.setFuelcost("--");
      }


        if(time>0) {
          int averageSpeed = (distance) / time;
          int average_Speed = averageSpeed * 18 / 5;
        double  speed_temp = (int) Math.round(average_Speed * 100.00) / 100.00;
          int speed = (int) speed_temp;
        view.setAverageSpeed(""+speed+" km/hr");
        }

        view.setTripTime(UtilityMethod.timeConversion(time,true));




    } catch (JSONException e) {
      e.printStackTrace();
    }




  }

  interface View {
    void setData();
    void setMaxRpm(String rpm);
    void setIdleTime(String idleTime);
    void setDistance(String distance);
    void setAlerts(String count);
    void setFuelcost(String fuelcost);
    void setTripTime(String time);
    void setMaxSpeed(String speed);
    void setAverageSpeed(String avg_speed);


  }

}
