package blackflame.com.zymepro.ui.login.fragment.signupfragment;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.login.fragment.signupfragment.SignUpInteractor.OnSignUpFinishedListener;

import blackflame.com.zymepro.util.ToastUtils;
import org.json.JSONObject;

public class SignUpPresenter implements OnSignUpFinishedListener {
  private  View view;
  private SignUpInteractor interactor;

  public SignUpPresenter(View view,SignUpInteractor interactor) {
    this.view = view;
    this.interactor=interactor;
  }

  public void validateCredentials(String username, String password,String email,String mobile) {
    if (view != null) {
      view.showProgressBar();
    }
    interactor.signUp(email, password,username,mobile, this);
  }
  public void storeData(JSONObject jsonProfile,String email,String mobile,String name){

    try {
      String status = jsonProfile.getString("status");
      if (status.equals("ERROR")) {
        String msg = jsonProfile.getString("msg");
        view.setMobileError();
        ToastUtils.showShort(msg);
      } else {
        JSONObject object = jsonProfile.getJSONObject("msg");
        String topicName = object.getString("topic_name");
        String referCode = object.getString("coupon_code");
        CommonPreference.getInstance().setIsLoggedIn(true);
        CommonPreference.getInstance().setEmail(email.toLowerCase());
        CommonPreference.getInstance().setSubscriptionTopic(topicName);
        CommonPreference.getInstance().setMobile(mobile);
        CommonPreference.getInstance().setReferCode(referCode);
        CommonPreference.getInstance().setUserName(name);
        CommonPreference.getInstance().setInstallTime(System.currentTimeMillis());
        CommonPreference.getInstance().setToken(object.getString("token"));
        // Commit the edits!
        view.navigateToHome();
      }


    }catch (Exception ex){

    }

  }

  @Override
  public void onEmailError() {
    view.setEmailError();

  }

  @Override
  public void onPasswordError() {
    view.setPasswordError();

  }

  @Override
  public void onNameError() {
    view.setNameError();

  }

  @Override
  public void onMobileError() {
    view.setMobileError();

  }

  @Override
  public void onSuccess() {
    view.doApiCall();

  }

  public interface  View{
    void setPasswordError();
    void setEmailError();
    void setMobileError();
    void setNameError();
    void showProgressBar();
    void hideProgressBar();
    void navigateToHome();
    void doApiCall();
  }

}
