package blackflame.com.zymepro.ui.home.singlecar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import blackflame.com.zymepro.db.CommonPreference;

public class SingleInteractor {
static String TAG=SingleInteractor.class.getCanonicalName();
  public interface OnHandledListener{
     void onSetActivationCard();
     void onParkedCardListener();
     void registerMqtt(String imei);
     void setRealtimeTimeCard();
     void setMulticarList();
     void setCardClickable();
     void setNotClickable();
     void hideMulticarList();
     void setRegistration(String reg);
     void setSelectedImei(String imei);

  }


  public static void handleData(Bundle bundle,OnHandledListener listener){
    String imei=null;
    int count=0;


    if (bundle != null) {
      int coming = bundle.getInt("coming");
      Log.e(TAG, "onCreateView:coming "+coming );
      if (coming == 1) {
        String car_count=bundle.getString("car_count");
        if (car_count !=null){
          count = Integer.valueOf(car_count);
         String  registration_selected = bundle.getString("registration_number");
          imei = bundle.getString("IMEI");
          listener.hideMulticarList();
          listener.setRegistration(registration_selected);
          if (imei ==null){
            imei=CommonPreference.getInstance().getImei();
          }
          listener.setSelectedImei(imei);
          listener.registerMqtt(imei);
        }
      } else {
        count=CommonPreference.getInstance().getDeviceCount();

        imei=bundle.getString("IMEI");
        String registration_number=bundle.getString("registration_number");
        listener.setRegistration(registration_number);
        if (imei ==null){
         imei= CommonPreference.getInstance().getImei();
          listener.setSelectedImei(imei);

        }

        listener.registerMqtt(imei);

      }

    }

    if (imei ==null){
      imei=CommonPreference.getInstance().getImei();
      listener.registerMqtt(imei);
    }


    boolean isDeviceActivated=CommonPreference.getInstance().getDeviceActivated();

    if (isDeviceActivated){
      listener.setCardClickable();
    }else{
      listener.setNotClickable();
      listener.onSetActivationCard();
    }


  }







}
