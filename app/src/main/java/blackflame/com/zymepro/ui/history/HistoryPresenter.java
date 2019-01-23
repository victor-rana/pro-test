package blackflame.com.zymepro.ui.history;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import blackflame.com.zymepro.ui.history.model.TripHistory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryPresenter {
  View view;
  public HistoryPresenter(View view){
    this.view=view;
  }

  public void parseData(JSONObject data){
    try {

      ArrayList<TripHistory> list=new ArrayList<>();


      JSONObject dataObject=data.getJSONObject("msg");

      JSONObject trip_total_object=dataObject.getJSONObject("trip_data");
      JSONArray array=dataObject.getJSONArray("trip_list");
      int length=array.length();
      //Log.e("array", "onResponse: array"+length );
      if(length>0) {
        for (int i = 0; i < length; i++) {
          TripHistory tripHistoryhelper = new TripHistory();
          JSONObject jsonObject = array.getJSONObject(i);
          if (jsonObject.has("start_address")) {
            tripHistoryhelper.setStartAddress(jsonObject.getString("start_address"));
          }
          if (jsonObject.has("end_address")) {
            tripHistoryhelper.setEndAddress(jsonObject.getString("end_address"));
          }
          if (jsonObject.has("start_time")) {
            tripHistoryhelper.setStartTime(jsonObject.getString("start_time"));
          }
          if (jsonObject.has("end_time")) {
            tripHistoryhelper.setEndTime(jsonObject.getString("end_time"));
          }

          if (jsonObject.has("trip_id")) {
            tripHistoryhelper.setTripId(jsonObject.getInt("trip_id"));
          }
          if (jsonObject.has("trip_time")) {
            tripHistoryhelper.setTime_consumed(jsonObject.getInt("trip_time"));
          }
          if (jsonObject.has("distance")) {
            tripHistoryhelper.setTotal_distance(jsonObject.getInt("distance"));
          }
          if (jsonObject.has("trip_time")) {
            tripHistoryhelper.setTotal_time(jsonObject.getInt("trip_time"));
          }
          if (jsonObject.has("idle_time")){
            tripHistoryhelper.setTotal_idle_time(jsonObject.getInt("idle_time"));
          }
          if (jsonObject.has("max_speed")){
            tripHistoryhelper.setMax_speed(jsonObject.getInt("max_speed"));
          }

          tripHistoryhelper.setTrip_count(length-i);

          list.add(tripHistoryhelper);
          //Log.e("size", "onResponse: "+list_helper.size() );
          Collections
              .sort(list, new Comparator<TripHistory>() {
                @Override
                public int compare(TripHistory o1, TripHistory o2) {
                  return o2.getStartTime().compareTo(o1.getStartTime());
                }
              });
        }


        view.setData(list);
        int tempDistance = trip_total_object.getInt("total_distance");
        float temp_distance_format = (float) tempDistance / 1000;
      int  totaltime = trip_total_object.getInt("total_trip_time");
     int   fuelcost = trip_total_object.getInt("total_fuel_cost");
     int   tempFuelConsumed = trip_total_object.getInt("total_fuel_consumed");
        double fuel_litre = (double) tempFuelConsumed / 1000;
        final double finalFuelCost = Math.round(fuel_litre * 100.00) / 100.00;
     float   total_distance = Math.round(temp_distance_format * 100.00f) / 100.00f;

     view.hideNoTrip();

     view.setCost("Rs" + " " + fuelcost);
     view.setFuelConsumed(String.valueOf(finalFuelCost).concat(" lt"));
     view.setTotalDistance(total_distance + " km");
     view.setTotalTime(String.valueOf(totaltime/60)+" min");
      }else{
        view.setNoTrip();
        view.setTotalTime("--");
        view.setTotalDistance("--");
        view.setFuelConsumed("--");
        view.setCost("--");

      }
    } catch (JSONException e) {
      e.printStackTrace();


      //    Log.e("Error", "onResponse: "+e.getMessage() );
    }



  }

  interface View{
    void setData(ArrayList<TripHistory> list);
    void setTotalDistance(String distance);
    void setFuelConsumed(String fuel);
    void setCost(String cost);
    void setTotalTime(String time);
    void setNoTrip();
    void hideNoTrip();


  }


}
