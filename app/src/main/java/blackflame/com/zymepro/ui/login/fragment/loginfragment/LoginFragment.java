package blackflame.com.zymepro.ui.login.fragment.loginfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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

public class LoginFragment extends CommonFragment implements LoginPresenter.View, AppRequest {
  ProgressBar progressBar;
  TextInputEditText email,password;
  LoginPresenter presenter;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, null);
    presenter = new LoginPresenter(this, new LoginInteractor());
    initViews(view);

    return view;
  }


  private void initViews(View view){
    progressBar=view.findViewById(R.id.progressBar_data_loading_login);
    email=view.findViewById(R.id.et_email_login);
    password=view.findViewById(R.id.et_password_login);
    view.findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        validateCredentials();

      }
    });

  }
  private void validateCredentials() {
    presenter.validateCredentials(email.getText().toString(), password.getText().toString());
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
      progressBar.setVisibility(View.VISIBLE);

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
  try {
     progressBar.setVisibility(View.GONE);
    JSONObject object = listener.getJsonResponse();

     presenter.storeData(object,email.getText().toString());

//      Intent intent = new Intent(GlobalReferences.getInstance().baseActivity, MainActivity.class);
//      startActivity(intent);
//      GlobalReferences.getInstance().baseActivity.finish();
//    }
//
//
//
//   }catch (Exception ex){
//
  }catch (Exception ex){

  }




  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {





  }


  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void setPasswordError() {
    password.setError("Please enter valid password");
  }

  @Override
  public void setEmailError() {
    email.setError("Please enter valid email");
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
        jsonObject.put(Constants.EMAIL, email.getText().toString().trim());
        jsonObject.put(Constants.PASSWORD, password.getText().toString().trim());
      }catch (Exception e){
        e.printStackTrace();
      }

      ApiRequests.getInstance().login(GlobalReferences.getInstance().baseActivity,LoginFragment.this,jsonObject);
    } else {
      ToastUtils.showShort(R.string.no_internet);
    }


  }
}
