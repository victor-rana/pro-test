package blackflame.com.zymepro.ui.profile.updateuser;

import android.content.Intent;
import android.os.Handler;
import com.google.android.material.textfield.TextInputEditText;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.profile.ActivityProfile;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.ToastUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateUser extends BaseActivity implements UserPresenter.View,AppRequest {
  TextInputEditText et_name, et_mobile, et_old_password, et_new_password, et_verify_password;
  TextView tv_save_profile, tv_save_password;
  boolean isPasswordMatched=false;
  FrameLayout progressBarHolder;
  UserPresenter presenter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_user);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new UserPresenter(this);
    initViews();
  }


  private void initViews(){
    et_name = findViewById(R.id.update_name);
    et_mobile = findViewById(R.id.update_mobile);
    et_old_password = findViewById(R.id.update_old_password);
    et_new_password = findViewById(R.id.update_new_password);
    et_verify_password = findViewById(R.id.update_verify_password);
    tv_save_password = findViewById(R.id.tv_save_password);
    tv_save_profile = findViewById(R.id.tv_save_profile);
    progressBarHolder=findViewById(R.id.progressBarHolder);
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Update profile");

    et_name.setText(getIntent().getStringExtra("name"));
    et_mobile.setText(getIntent().getStringExtra("mobile"));
    et_name.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        tv_save_profile.setAlpha(1);
        tv_save_profile.setFocusable(true);
        tv_save_profile.setClickable(true);


      }
    });

    et_mobile.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        tv_save_profile.setAlpha(1);
        tv_save_profile.setFocusable(true);
        tv_save_profile.setClickable(true);


      }
    });
    et_new_password.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {

        if(s.length()<6){
          et_new_password.setError("Password should be greater than 6 digit");
        }

      }
    });

    et_verify_password.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        String password_new = et_new_password.getText().toString();
        if (password_new != null && s.length()>=6 &&password_new.equals(s.toString())) {
          isPasswordMatched=true;
          tv_save_password.setAlpha(1);
          tv_save_password.setClickable(true);
          tv_save_password.setFocusable(true);

        } else {
          et_verify_password.setError("Password did't match");
          tv_save_password.setAlpha(0.5f);
          tv_save_password.setClickable(false);
          tv_save_password.setFocusable(false);

        }
      }
    });
    updatePassword();
    updateProfile();


  }

  private void updateProfile() {

    tv_save_profile.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        final String mobile = et_mobile.getText().toString();
        final String name=et_name.getText().toString();
        presenter.validateUser(name,mobile);
      }
    });
  }

  private void updatePassword(){
    tv_save_password.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        final String old_password=et_old_password.getText().toString();
        final String new_password=et_new_password.getText().toString();
        String verify_password=et_verify_password.getText().toString();
        presenter.validatePassword(old_password,new_password,verify_password);
      }
    });



  }


  @Override
  public void setNameError() {
    et_name.setError("Please enter your name");
  }

  @Override
  public void setMobileError() {
    et_mobile.setError("Please enter valid mobile number");
  }

  @Override
  public void setOldPasswordError() {
    et_old_password.setError("Password should be atleast 6 digit");

  }
  @Override
  public void setNewPasswordError() {
    et_new_password.setError("Please enter new password");
  }

  @Override
  public void rePasswordError() {
    et_verify_password.setError("Password did't matched");
  }

  @Override
  public void updateUser(String name, String mobile) {
    try{
      JSONObject data=new JSONObject();
      data.put("name",name);
      data.put("mobile_number",mobile);
      ApiRequests.getInstance().updateUser(GlobalReferences.getInstance().baseActivity,UpdateUser.this,data);
    }catch (JSONException ex){

    }
  }

  @Override
  public void updatePassword(String old, String new_password) {
try{
  JSONObject data=new JSONObject();
  data.put("old_password",old);
  data.put("new_password",new_password);
  ApiRequests.getInstance().updatePassword(GlobalReferences.getInstance().baseActivity,UpdateUser.this,data);
}catch (JSONException ex){


}

  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {
    doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    try{
      String tag=listener.getTag();
      JSONObject data=listener.getJsonResponse();
      if (tag !=null && tag.equals("update_profile")){
        String status=data.getString("status");
        final String msg=data.getString("msg");
        if(status.equals("SUCCESS")){
          new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              ToastUtils.showShort(msg);
              Intent intent=new Intent(GlobalReferences.getInstance().baseActivity,ActivityProfile.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intent);
              finish();
            }
          });
        }else if(status.equals("ERROR")){
          ToastUtils.showShort(msg);
        }

      }else if(tag != null && tag.equals("update_password")){
        String status=data.getString("status");
        final String msg=data.getString("msg");

        if (status.equals("ERROR")){
          new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              ToastUtils.showShort(msg);
            }
          });

        }else if(status.equals("SUCCESS")){

          new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              ToastUtils.showShort(msg);
            }
          });

        }
      }



    }catch (Exception ex){}
  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void indexScreen() {
    Analytics.index(UpdateUser.this,"UpdateUser");
  }
}
