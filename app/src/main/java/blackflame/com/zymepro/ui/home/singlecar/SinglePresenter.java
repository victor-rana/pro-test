package blackflame.com.zymepro.ui.home.singlecar;

import static blackflame.com.zymepro.ui.home.singlecar.SingleInteractor.TAG;
import static blackflame.com.zymepro.util.UtilityMethod.getDate;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.util.LogUtils;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class SinglePresenter implements SingleInteractor.OnHandledListener {

View view;
SingleInteractor interactor;
String registration;
ArrayList<CarcountModel> list;

  public SinglePresenter(View view,SingleInteractor interactor) {
    this.view = view;
    this.interactor=interactor;
  }
  public  void handleBundle(Bundle bundle){

      interactor.handleData(bundle,this);

  }

  void handleTripData(JSONObject object){
    String status=null,total_time;
    int time=0,distance=0;
    double latitude,longitude;
      try {
        if (object.has("trip_time")) {
          time = object.getInt("trip_time");
        }
        if (object.has("distance")) {
          distance = object.getInt("distance");
        }
        if (object.has("status")) {
          status = object.getString("status");
        }
        double averageSpeed = (((double) distance) / time)*(18/5);
        double avgspeed = (int) (Math.round(averageSpeed * 100.00) / 100.00);
        Double double_temp_averageSpeed = Double.valueOf(avgspeed);

        final int final_average_speed = double_temp_averageSpeed.intValue();
        int formatted_time = (int) time / 60;
        if (formatted_time <= 0) {
          total_time=time + " sec";
        } else {
          total_time=formatted_time + " min";
        }
        float distance_for_calculation = (float) distance / 1000;
        double formatted_distance = Math.round(distance_for_calculation * 10) / 10.0;
        view.setTripData(status,final_average_speed,total_time,formatted_distance);
        final int coolant = object.getInt("coolant");
       int rpm = object.getInt("rpm");
       String  voltage = object.getString("voltage");
       int speed_realtime = object.getInt("speed");
       view.setRealtimeData(coolant,rpm,speed_realtime,voltage,status);
        JSONArray array = object.getJSONArray("coordinates");
        int length = array.length();
        if (length>0){
          latitude = array.getDouble(0);
          longitude = array.getDouble(1);
          view.updateRealtimeLocation(latitude,longitude);
        }

        if (status !=null){
          if (status.equals("PARKED")){
            view.setParkedCard();
          } else if(status.equals("HALT")){
            view.setParkedCard();

          }else if(status.equals("ACTIVE")){
            view.setActiveCard();

          }
        }

      } catch (JSONException e) {
        e.printStackTrace();
        //  Log.e(TAG, "messageArrived: "+e.getMessage() );
      }

  }


  void parseRealtimeData(String topic,JSONObject data){
    handleTripData(data);
    view.updateNerdMode(data);

  }

  public void handleActivation(){

    boolean isDeviceLinked = CommonPreference.getInstance().getDeviceLinked();
    boolean isDeviceActivated = CommonPreference.getInstance().getDeviceActivated();
    boolean isThisDeviceActivate = CommonPreference.getInstance().getThisDeviceLinked();
    String imeiforserver = CommonPreference.getInstance().getImeiForAPI();

    int count = CommonPreference.getInstance().getDeviceCount();

    if (isDeviceLinked && !isDeviceActivated) {

      view.navigationToActivation(imeiforserver);

    } else if (isDeviceLinked && !isThisDeviceActivate) {
     view.navigationToActivation(imeiforserver);

    } else {
      view.navigateToRegistration();
    }

  }


  public void parseAddress(JSONObject response){
    try {
      if (response.has("msg") ) {
        String addre = null;

        addre=response.getString("msg");



        view.updateAddress(addre);

        LogUtils.error("Alerts",addre);


      }


    } catch (JSONException e) {
      e.printStackTrace();

      Log.e(TAG, "parseAddress: "+e.getMessage() );

    }
  }

  public void parseLocation(JSONObject response){
    try {

      JSONObject carData = response.getJSONObject("msg");
      //final String status=carData.getString("status");
      JSONArray array = carData.getJSONArray("last_location");
      double lastLatitude = array.getDouble(0);
      double lastLongitude = array.getDouble(1);

      view.navigateToPitstop(lastLatitude,lastLongitude);


    } catch (JSONException e) {
      e.printStackTrace();

    }
  }
  public void parseData(JSONObject data,String registration){
    try {
      String registration_number=null;
      String car_last_status=null;
      String selected_model=null;

      String hit_status = data.getString("status");
      if (hit_status.equals("SUCCESS")) {
        JSONObject carData = data.getJSONObject("msg");

        if (carData.has("onesignal_status")) {
          CommonPreference.getInstance().setOneSignalNotification(carData.getBoolean("onesignal_status"));

        }
        JSONArray array_car_list = carData.getJSONArray("car_list");
        int length = array_car_list.length();
        CommonPreference.getInstance().setDeviceCount(length);
        if (length == 1) {
          JSONObject object = array_car_list.getJSONObject(0);
           registration_number = object.getString("registration_number");
          setRegistration(registration_number);
          String brand = object.getString("brand");
          selected_model = object.getString("model");
          car_last_status = object.getString("status");
          handleLastStatus(car_last_status);
          CommonPreference.getInstance().setImeiForAPI(object.getString("IMEI"));
          CommonPreference.getInstance().setImei(object.getString("IMEI"));
          CommonPreference.getInstance().setCarId(object.getString("id"));
          CommonPreference.getInstance().setRegNumber(registration_number);
          CommonPreference.getInstance().setModel(selected_model);
          String  last_update_time = getDate(object.getString("last_active_time"));
          JSONArray array = object.getJSONArray("last_location");
          view.setLastUpdateTime(last_update_time);
          double   lastLatitude = array.getDouble(0);
          double  lastLongitude = array.getDouble(1);

        view.setSelectedModel(selected_model,registration_number);
        view.setCarLocation(lastLatitude,lastLongitude);
        view.loadAddress(lastLatitude,lastLongitude);

        } else {
           list=new ArrayList<>();
          CarcountModel  model0 = new CarcountModel();
          model0.setBrand("All");
          model0.setModel("Cars");
          model0.setRegistration("View all cars");
          list.add(model0);
          for (int i = 0; i < length; i++) {
            JSONObject object = array_car_list.getJSONObject(i);
            CarcountModel carcountModel = new CarcountModel();
            if (object.has("registration_number")) {
              registration_number = object.getString("registration_number");
              carcountModel.setRegistration(registration_number);
            }
            if (object.has("IMEI")) {
              carcountModel.setImei(object.getString("IMEI"));
            }
            if (object.has("model")) {
              carcountModel.setModel(object.getString("model"));

            }
            if (object.has("brand")) {
              carcountModel.setBrand(object.getString("brand"));
            }
            if (object.has("status")) {
              carcountModel.setStatus(object.getString("status"));
            }
            list.add(carcountModel);
            if (registration_number != null && registration_number.equals(registration)) {
              this.registration = registration_number;
              view.setRegistration(registration);
              car_last_status = object.getString("status");
              selected_model = object.getString("model");
              view.setSelectedModel(selected_model,registration);
              CommonPreference.getInstance().setCarId(object.getString("id"));
              CommonPreference.getInstance().setImeiForAPI(object.getString("IMEI"));
              CommonPreference.getInstance().setRegNumber(registration);
              CommonPreference.getInstance().setModel(object.getString("model"));
              handleLastStatus(car_last_status);
              if (object.has("last_active_time")) {
              String    last_update_time = getDate(object.getString("last_active_time"));
              view.setLastUpdateTime(last_update_time);
              }
              if (object.has("last_location")) {
                JSONArray array = object.getJSONArray("last_location");
                if (array.length() > 0) {
              double lastLatitude = array.getDouble(0);
              double lastLongitude = array.getDouble(1);
              view.setCarLocation(lastLatitude,lastLongitude);
              view.loadAddress(lastLatitude,lastLongitude);

                }
              }
            }
          }
          view.setListData(list);
        }

      }
    }catch (JSONException ex){

  //    Log.e(TAG, "parseData: "+ex.getCause() );
    }
  }


  public void handleLastStatus(String status){
    if (status !=null){
      switch (status){
        case "PARKED":
          view.setCardClickable(false);
          view.setParkedCard();
          CommonPreference.getInstance().setThisDeviceLinked(true);
          CommonPreference.getInstance().setDeviceActivated(true);
          break;
        case "ACTIVE":
          view.setCardClickable(false);
          view.setActiveCard();
          CommonPreference.getInstance().setDeviceActivated(true);

          break;
        case "NOT_ACTIVATED":
          view.setCardClickable(true);
          CommonPreference.getInstance().setThisDeviceLinked(false);
          CommonPreference.getInstance().setDeviceLinked(true);
          CommonPreference.getInstance().setDeviceActivated(false);
          view.setActivationCard();
          break;
        case "UNPLUGGED":
          view.setCardClickable(false);
          view.setUnplugged();
          break;
        case "UNLINKED":
          view.setCardClickable(false);
          view.setUnlinked();
          break;
      }
    }
  }

  @Override
  public void onSetActivationCard() {
    view.setActivationCard();

  }

  @Override
  public void onParkedCardListener() {

  }

  @Override
  public void registerMqtt(String imei) {
    view.registerMqtt(imei);

  }

  @Override
  public void setRealtimeTimeCard() {

  }

  @Override
  public void setMulticarList() {
    view.setMulticar();

  }

  @Override
  public void setCardClickable() {
    view.setActivationCard();

  }

  @Override
  public void setNotClickable() {
    view.setParkedCard();

  }

  @Override
  public void hideMulticarList() {
    view.setSingleCar();

  }

  @Override
  public void setRegistration(String reg) {
    this.registration=reg;
    view.setRegistration(reg);

  }

  @Override
  public void setSelectedImei(String imei) {
    view.setSelectedImei(imei);

  }

  public interface View{
    void setParkedCard();
    void setActivationCard();
    void setMulticar();
    void setSingleCar();
    void setSelectedImei(String imei);
    void setRegistration(String registration);
    void registerMqtt(String imei);
    void navigateToRegistration();
    void navigationToActivation(String imei);
    void setCarLocation(double latitude,double longitude);
    void setSelectedModel(String model,String registration);
    void setCardClickable(boolean value);
    void setUnlinked();
    void setUnplugged();
    void setListData(ArrayList<CarcountModel> data);
    void setActiveCard();
    void setLastUpdateTime(String time);
    void updateAddress(String address);
    void loadAddress(double latitude,double longitude);
    void setTripData(String status,double avgSpeed,String time,double distance);
    void setRealtimeData(int coolant,int rpm,int speed,String voltage,String status);
    void updateRealtimeLocation(double latitude,double longitude);
    void updateNerdMode(JSONObject jsonObject);
    void navigateToPitstop(double latitude,double longitude);


  }




}
