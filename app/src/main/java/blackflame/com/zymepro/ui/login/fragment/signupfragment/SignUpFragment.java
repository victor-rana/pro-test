package blackflame.com.zymepro.ui.login.fragment.signupfragment;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.home.MainActivity;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import org.json.JSONObject;

public class SignUpFragment extends CommonFragment implements AppRequest,SignUpPresenter.View {
TextInputEditText et_name,et_email,et_mobile,et_password;
ProgressBar progressBar;
SignUpPresenter presenter;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signup, null);
    initViews(view);
    presenter=new SignUpPresenter(this,new SignUpInteractor());
    return view;
  }
  private  void initViews(View view){
    et_email=view.findViewById(R.id.et_email_signup);
    et_mobile=view.findViewById(R.id.et_mobile_signup);
    et_name=view.findViewById(R.id.et_signup_name);
    et_password=view.findViewById(R.id.et_password_signup);
    progressBar=view.findViewById(R.id.progressBar_data_loading_signup);
    view.findViewById(R.id.btn_signup).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        validateCredentials();
      }
    });
  }
  private void validateCredentials() {
    presenter.validateCredentials(et_name.getText().toString(),et_password.getText().toString(),et_email.getText().toString(), et_mobile.getText().toString());
  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    showProgressBar();


  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
      hideProgressBar();

    presenter.storeData(listener.getJsonResponse(),et_email.getText().toString(),et_mobile.getText().toString(),et_name.getText().toString());
  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void setPasswordError() {
    et_password.setError("Please enter minimum 6 digit password.");
  }

  @Override
  public void setEmailError() {
    et_email.setError("Please enter valid email id");

  }

  @Override
  public void setMobileError() {
    et_mobile.setError("Please enter valid mobile number");

  }

  @Override
  public void setNameError() {
    et_name.setError("Please enter your name");
  }

  @Override
  public void showProgressBar() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgressBar() {
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void navigateToHome() {
    Intent intent = new Intent(GlobalReferences.getInstance().baseActivity, MainActivity.class);
    startActivity(intent);
    GlobalReferences.getInstance().baseActivity.finish();
  }

  @Override
  public void doApiCall() {
    if (NetworkUtils.isConnected()) {
      JSONObject jsonObject = new JSONObject();
      try {
        jsonObject.put(Constants.EMAIL, et_email.getText().toString().trim());
        jsonObject.put(Constants.PASSWORD, et_password.getText().toString().trim());
        jsonObject.put(Constants.NAME,et_name.getText().toString().trim());
        jsonObject.put(Constants.MOBILE,et_mobile.getText().toString().trim());
      }catch (Exception e){
        e.printStackTrace();
      }
      ApiRequests.getInstance().sign_up(GlobalReferences.getInstance().baseActivity,SignUpFragment.this,jsonObject);
    } else {
      ToastUtils.showShort(R.string.no_internet);
    }
  }
}
