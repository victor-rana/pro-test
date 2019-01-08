package blackflame.com.zymepro.ui.home;

import android.os.Bundle;

public class MainPresenter implements MainInteractor.OnPerformtaskListener {
  View view;
  MainInteractor interactor;

  public MainPresenter(View view,MainInteractor interactor){
    this.interactor=interactor;
    this.view=view;
  }


  public void routeFromNotification(Bundle bundle){

  }

  @Override
  public void onOpenRateUs() {

  }

  @Override
  public void openDefaultState() {

  }


  @Override
  public void openSinglecar(Bundle bundle) {

  }

  public interface View{

    void openDefaultState();
    void openSingleCar(Bundle bundle);
    void openRateUs();
    void navigateToHome();
    void doLogout();
    }


}
