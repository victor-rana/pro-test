package blackflame.com.zymepro.ui.login.fragment.signupfragment;


import android.text.TextUtils;
import blackflame.com.zymepro.util.AndroidUtils;

public class SignUpInteractor {
  interface OnSignUpFinishedListener {
    void onEmailError();

    void onPasswordError();
    void onNameError();
    void onMobileError();

    void onSuccess();
  }

  public void signUp(final String email, final String password,final String name,final String mobile, final SignUpInteractor.OnSignUpFinishedListener listener) {
    // Mock login. I'm creating a handler to delay the answer a couple of seconds

    if (TextUtils.isEmpty(name)){
      listener.onNameError();
      return;

    }
    if (!AndroidUtils.validateEmail(email)) {
      listener.onEmailError();
      return;
    }

    if (!AndroidUtils.matchLength(mobile,10)&& AndroidUtils.noSpecialCharacters(mobile)){
      listener.onMobileError();
      return;
    }

    if (!AndroidUtils.matchMinLength(password,6)) {
      listener.onPasswordError();
      return;
    }
    listener.onSuccess();
  }



}
