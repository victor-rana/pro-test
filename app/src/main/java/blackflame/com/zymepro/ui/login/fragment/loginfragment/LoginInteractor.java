package blackflame.com.zymepro.ui.login.fragment.loginfragment;
import blackflame.com.zymepro.util.AndroidUtils;


public class LoginInteractor {
  interface OnLoginFinishedListener {
    void onEmailError();

    void onPasswordError();

    void onSuccess();
  }

  public void login(final String email, final String password, final OnLoginFinishedListener listener) {
    // Mock login. I'm creating a handler to delay the answer a couple of seconds

    if (!AndroidUtils.validateEmail(email)) {
      listener.onEmailError();
      return;
    }
    if (!AndroidUtils.matchMinLength(password,6)) {
      listener.onPasswordError();
      return;
    }
    listener.onSuccess();
  }

}