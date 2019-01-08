package blackflame.com.zymepro.ui.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainInteractor {

  interface  OnPerformtaskListener{
    public void onOpenRateUs();
    public void openDefaultState();
    public void openSinglecar(Bundle bundle);
  }



  public void checkData(Bundle bundle,OnPerformtaskListener listener){
    String car_count=bundle.getString("car_count");
    if (car_count !=null){
      listener.openSinglecar(bundle);

    } else{
      listener.openDefaultState();
      if (bundle.containsKey("rate_message")){
        String rate=bundle.getString("rate_message");
        if (rate !=null  ){
         listener.onOpenRateUs();
        }
      }
    }

  }



}
