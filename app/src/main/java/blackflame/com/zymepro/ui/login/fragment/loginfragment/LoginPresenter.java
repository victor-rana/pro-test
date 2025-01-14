package blackflame.com.zymepro.ui.login.fragment.loginfragment;

import android.util.Log;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.util.ToastUtils;
import org.json.JSONObject;

public class LoginPresenter implements  LoginInteractor.OnLoginFinishedListener  {
  private User user;
  private View view;
  LoginInteractor interactor;
  public LoginPresenter(View view,LoginInteractor interactor) {

    this.user = new User();
    this.view = view;
    this.interactor=interactor;
  }
  public void validateCredentials(String username, String password) {
    if (view != null) {
        if (username != null && username.contains("ankeshth@gmail.com")){
            view.showDemoProgressBar();
        }else {
            view.showProgressBar();
        }


    }
    interactor.login(username, password, this);
  }


  public void storeData(JSONObject data,String email){

           try {
             String status = data.getString("status");

           if (status.equals("ERROR")) {
            String msg = data.getString("msg");
            // btn_login.setVisibility(View.VISIBLE);
            // btn_login.setClickable(true);
            view.setPasswordError();
            ToastUtils.showShort(msg);
            //ActivityConstants.showAlert(AppIntro.this, msg);
          } else {
            JSONObject jsonData = data.getJSONObject("msg");
            Log.e("Login", "onResponse: " + data);

            String topicName = jsonData.getString("topic_name");
            String mobileNumber = jsonData.getString("mobile_number");
            boolean isActivate = jsonData.getBoolean("device_activation");
            String coupon_code = jsonData.getString("coupon_code");
            int carCount = jsonData.getInt("car_count");
            CommonPreference.getInstance().setEmail(email.toLowerCase());
            CommonPreference.getInstance().setDeviceActivated(isActivate);
            CommonPreference.getInstance().setMobile(mobileNumber);
            CommonPreference.getInstance().setReferCode(coupon_code);

            if (data.has("is_demo_user") && data.getBoolean("is_demo_user")){
              CommonPreference.getInstance().setIsDemoUser(data.getBoolean("is_demo_user"));
              CommonPreference.getInstance().setExpiryTime(data.getString("login_expiry"));
            }
            CommonPreference.getInstance().setToken(data.getString("token"));
            if (jsonData.has("name")) {
              CommonPreference.getInstance().setUserName(jsonData.getString("name"));
            }
            CommonPreference.getInstance().setDeviceCount(carCount);
            if (carCount == 1) {
              CommonPreference.getInstance().setDeviceLinked(true);
              if (jsonData.has("IMEI")) {
                CommonPreference.getInstance().setImei(jsonData.getString("IMEI"));
              }
            }

            if (jsonData.has("is_demo_user") && jsonData.getBoolean("is_demo_user")){
              CommonPreference.getInstance().setIsDemoUser(jsonData.getBoolean("is_demo_user"));
              CommonPreference.getInstance().setTokenExpTime("login_expiry");

            }
            CommonPreference.getInstance().setIsLoggedIn(true);
            CommonPreference.getInstance().setSubscriptionTopic(topicName);
            view.navigateToHome();

          }
        }catch (Exception ex){

        }


  }


  @Override
  public void onEmailError() {
    if (view != null) {
      view.setEmailError();
      view.hideProgressBar();
    }
  }



  @Override
  public void onPasswordError() {
    if (view != null) {
      view.setPasswordError();
      view.hideProgressBar();
    }
  }

  @Override
  public void onSuccess() {
    if (view != null) {
      view.doApiCall();
    }
  }

  @Override
  public void onDemoLogin(String email, String pwd) {
     view.doDemoLogin(email,pwd);
  }



  public interface View{
    void setPasswordError();
    void setEmailError();
    void showProgressBar();
    void showDemoProgressBar();
    void hideProgressBar();
    void navigateToHome();
    void doApiCall();
    void doDemoLogin(String email,String pwd);
  }
}
