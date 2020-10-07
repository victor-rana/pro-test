package blackflame.com.zymepro.ui.profile.payment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.instamojo.android.Instamojo;

import org.json.JSONException;
import org.json.JSONObject;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.UtilityMethod;

public class PaymentSuccess extends BaseActivity implements AppRequest {
  final float growTo = 1.2f;
  final long duration = 1000;
  Button btn_home;
  TextView month,tv_payment_id;
  String transactionId;
  RelativeLayout rlSuccess;
  ImageView success;
  private String TAG="PaymentSuccess";

  Dialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment_success);
    initViews();

  }



  private void initViews(){
   success=findViewById(R.id.iv_success);
    btn_home=findViewById(R.id.button_home);
    month=findViewById(R.id.tv_increased_month);
    tv_payment_id=findViewById(R.id.tv_payment_id);
    rlSuccess=findViewById(R.id.rlStatus);
    String payment_id=getIntent().getStringExtra("payment_id");
    transactionId=getIntent().getStringExtra("transactionID");
    tv_payment_id.setText("Your payment ID : "+payment_id);
    month.setText("Subscription time increased by "+CommonPreference.getInstance().getSubscriptionMonth()+" months");

    btn_home.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });


    JSONObject data=new JSONObject();
    try {

      data.put("transaction_id",transactionId);
      data.put("status","successful");
    } catch (JSONException e) {
      e.printStackTrace();
    }

      progressDialog= UtilityMethod.showProgress(PaymentSuccess.this);


    ApiRequests.getInstance().update_order(GlobalReferences.getInstance().baseActivity,PaymentSuccess.this,data);


  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, Constants.RequestParam requestParam) {
    super.onRequestStarted(listener, requestParam);
  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, Constants.RequestParam requestParam) {
    super.onRequestCompleted(listener, requestParam);
  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, Constants.RequestParam requestParam) {
    super.onRequestFailed(listener, requestParam);
  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {
    super.onRequestStarted(listener, requestParam);
  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {
    super.onRequestCompleted(listener, requestParam);


    try {
      JSONObject jsonProfile = listener.getJsonResponse();
      String msg=jsonProfile.getString("status");




          if (progressDialog != null)
            progressDialog.dismiss();
          rlSuccess.setVisibility(View.VISIBLE);
          month.setText("Subscription order placed successfully. Please allow 24-48 hours for updated subscription to reflect in your account");
          CommonPreference.getInstance().setSubscriptionMonth(0);

          ScaleAnimation grow = new ScaleAnimation(1, growTo, 1, growTo,
                  Animation.RELATIVE_TO_SELF, 0.5f,
                  Animation.RELATIVE_TO_SELF, 0.5f);
          grow.setDuration(duration / 2);
          ScaleAnimation shrink = new ScaleAnimation(growTo, 1, growTo, 1,
                  Animation.RELATIVE_TO_SELF, 0.5f,
                  Animation.RELATIVE_TO_SELF, 0.5f);
          shrink.setDuration(duration / 2);
          shrink.setStartOffset(duration / 2);
          AnimationSet growAndShrink = new AnimationSet(true);
          growAndShrink.setInterpolator(new LinearInterpolator());
          growAndShrink.addAnimation(grow);
          growAndShrink.addAnimation(shrink);
          success.startAnimation(growAndShrink);




    }catch (Exception ex){

      Log.e(TAG, "onResponse: "+ex.getMessage() );

    }
  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {
    super.onRequestFailed(listener, requestParam);
  }

  @Override
  public void indexScreen() {
    Analytics.index(PaymentSuccess.this,"PaymentSuccess");
  }
}
