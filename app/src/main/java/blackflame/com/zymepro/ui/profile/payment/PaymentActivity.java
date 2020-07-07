package blackflame.com.zymepro.ui.profile.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import android.widget.Toast;
import blackflame.com.zymepro.BuildConfig;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import com.instamojo.android.Instamojo;

import blackflame.com.zymepro.util.Analytics;
import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends BaseActivity implements OnClickListener,AppRequest {
  private TextView nameBox,  phoneBox;
  private TextView emailBox,amount,gst_value,amount_payable,tv_month;
  String orderId,paymentId;
  InstapayListener listener;
  String imei;
  AlphaAnimation inAnimation;
  AlphaAnimation outAnimation;
  TextView textView_Title;
  private String currentEnv = null;
  String TAG=PaymentActivity.class.getCanonicalName();
  Button button;
  FrameLayout progressBarHolder;

  RadioButton rb_three,rb_six,rb_twelve;

  LinearLayout btn_three_month,btn_six_month,btn_twelve_month;
  boolean isChecked=false;
  private ProgressDialog dialog;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
  }
  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"");
    Intent intent=getIntent();
    String email=intent.getStringExtra("email");
    String name=intent.getStringExtra("name");
    String mobile=intent.getStringExtra("mobile");
    imei=intent.getStringExtra("imei");
    Instamojo.initialize(getApplicationContext());
    progressBarHolder =  findViewById(R.id.progressBarHolder);
    btn_six_month=findViewById(R.id.btn_six_month);
    btn_three_month=findViewById(R.id.btn_three_month);
    btn_twelve_month=findViewById(R.id.btn_twelve_month);


    button = findViewById(R.id.pay);
    nameBox =  findViewById(R.id.name);
    nameBox.setText(name);

    emailBox =  findViewById(R.id.email);
    emailBox.setText(email);
    phoneBox =  findViewById(R.id.phone);
    phoneBox.setText(mobile);
    amount=findViewById(R.id.tv_amount);
    amount_payable=findViewById(R.id.tv_amount_payable);
    gst_value=findViewById(R.id.tv_gst);
    tv_month=findViewById(R.id.tv_renewal_days);
    rb_six=findViewById(R.id.radio_six_month);
    rb_three=findViewById(R.id.radio_three_month);
    rb_twelve=findViewById(R.id.radio_twelve_month);
    Instamojo.setBaseUrl(Constants.INSTA_URL_TEST);
    dialog = new ProgressDialog(this);
    dialog.setIndeterminate(true);
    dialog.setMessage("Please wait...");
    dialog.setCancelable(false);

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
     case  R.id.pay:
       if (isChecked) {
         //fetchTokenAndTransactionID();
         String name = nameBox.getText().toString();
         final String email = emailBox.getText().toString();
         String phone = phoneBox.getText().toString();
         String amount = amount_payable.getText().toString().replace("₹ ", "").replace("/-","").trim();
         // Log.e("Tag", "onClick: "+name+email+phone+amount );
         callInstamojoPay(email, phone, amount, textView_Title.getText().toString(), name);
       }else{
         Toast
             .makeText(PaymentActivity.this, "Please select subscription duration", Toast.LENGTH_SHORT).show();
       }

       break;
      case R.id.btn_six_month:
        rb_twelve.setChecked(false);
        rb_three.setChecked(false);
        rb_six.setChecked(true);
        amount.setText("₹ 600/-");
        tv_month.setText("180 days");
        int gst=calculateGst(600);
        gst_value.setText("₹ "+gst+"/-");
        amount_payable.setText("₹ "+getTotal(gst,600)+"/-");
        CommonPreference.getInstance().setSubscriptionMonth(6);
        isChecked=true;
        break;
      case R.id.btn_three_month:
        rb_six.setChecked(false);
        rb_twelve.setChecked(false);
        rb_three.setChecked(true);
        amount.setText("₹ 400/-");
        tv_month.setText("90 days");
        int gst_three=calculateGst(400);
        gst_value.setText("₹ "+gst_three+"/-");
        amount_payable.setText("₹ "+getTotal(gst_three,400)+"/-");
        CommonPreference.getInstance().setSubscriptionMonth(3);
        isChecked=true;

        break;
      case R.id.btn_twelve_month:
        rb_six.setChecked(false);
        rb_three.setChecked(false);
        rb_twelve.setChecked(true);
        tv_month.setText("365 days");
        amount.setText("₹ 900/-");
        int gst_twelve=calculateGst(900);
        gst_value.setText("₹ "+gst_twelve+"/-");
        amount_payable.setText("₹ "+getTotal(gst_twelve,900)+"/-");
        CommonPreference.getInstance().setSubscriptionMonth(12);
        isChecked=true;
        break;

    }

  }

  private int getTotal(int amount,int gst){
    return amount+gst;
  }

  private  int calculateGst(int amount){
    return (18*amount)/100;
  }
  private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
    final Activity activity = this;
    InstamojoPay instamojoPay = new InstamojoPay();
    IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
    Instamojo.initialize(PaymentActivity.this);
    registerReceiver(instamojoPay, filter);
    JSONObject pay = new JSONObject();
    try {
      pay.put("email", email);
      pay.put("phone", phone);
      pay.put("purpose", purpose);
      pay.put("amount", amount);
      pay.put("name", buyername);
      pay.put("webhook",BuildConfig.WEBHOOK_URL);
      pay.put("send_sms", true);
      pay.put("send_email", true);
      pay.put("description",purpose);

      Log.e(TAG, "callInstamojoPay: "+pay );
    } catch (JSONException e) {
      e.printStackTrace();
    }
    initListener();

    instamojoPay.start(activity, pay, listener);
  }
  private void showProgress(){
    progressBarHolder.setAnimation(inAnimation);
    progressBarHolder.bringToFront();
    progressBarHolder.setVisibility(View.VISIBLE);

  }

  private void initListener() {
    listener = new instamojo.library.InstapayListener() {
      @Override
      public void onSuccess(String response) {


        if (response.contains("success")) {

          String[] data = response.split(":");
          Log.e("Payment", "afterReplace: " + data);


          for (int i = 0; i < data.length; i++) {
            String[] data_parse = data[i].split("=");
            if (data_parse[0].equals("orderId")) {
              orderId = data_parse[1];
            }
            if (data_parse[0].equals("paymentId")) {
              paymentId = data_parse[1];
            }
          }

          if (paymentId!=null) {
            showProgress();
            //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();


            updateStatus("CREDIT",orderId,paymentId);

         //   new PaymentUpdater(PaymentActivity.this, CommonPreference.getInstance().getSubscriptionImei(), paymentId, orderId,CommonPreference.getInstance().getSubscriptionMonth(),amount_payable.getText().toString().replace("₹ ", "").replace("/-","").trim(),gst_value.getText().toString().replace("₹ ", "").replace("/-","").trim());
          }



        }




      }

      @Override
      public void onFailure(int code, String reason) {

        updateStatus("FAILED",UUID.randomUUID().toString(),"--");

//        new PaymentFailedUpdater(PaymentActivity.this, CommonPreference.getInstance().getSubscriptionImei(), "--", UUID
//            .randomUUID().toString(),CommonPreference.getInstance().getSubscriptionMonth(),amount_payable.getText().toString().replace("₹ ", "").replace("/-","").trim(),gst_value.getText().toString().replace("₹ ", "").replace("/-","").trim());

      }
    };
  }




  private void updateStatus(String status,String orderId,String paymentId){
    try{
      JSONObject params=new JSONObject();
      params.put("IMEI", CommonPreference.getInstance().getSubscriptionImei());
        params.put("order_id", orderId);
        params.put("payment_status",status);
        params.put("payment_id", paymentId);
        params.put("order_id", orderId);
        params.put("payment_status",status);
        params.put("payment_id", paymentId);
      params.put("month_count",CommonPreference.getInstance().getSubscriptionMonth());
      params.put("amount",amount_payable.getText().toString().replace("₹ ", "").replace("/-","").trim());
      params.put("tax",gst_value.getText().toString().replace("₹ ", "").replace("/-","").trim());
      if (status.equals("CREDIT")){
        ApiRequests.getInstance().save_payment_status(GlobalReferences.getInstance().baseActivity,PaymentActivity.this,params);

      }else{
        ApiRequests.getInstance().save_payment_failed(GlobalReferences.getInstance().baseActivity,PaymentActivity.this,params);

      }

    }catch (JSONException ex){}
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
    if (listener.getTag().equals("payment_success")){
      progressBarHolder.setVisibility(View.GONE);

      Intent intent=new Intent(PaymentActivity.this,PaymentSuccess.class);
      intent.putExtra("payment_id",paymentId);
      startActivity(intent);
      finish();

    }else if (listener.getTag().equals("payment_failed")){
      progressBarHolder.setVisibility(View.GONE);

      Intent intent=new Intent(PaymentActivity.this,PaymentFailed.class);
      startActivity(intent);
      finish();
    }
  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    outAnimation = new AlphaAnimation(1f, 0f);
    outAnimation.setDuration(200);
    if (progressBarHolder!=null) {
      progressBarHolder.setAnimation(outAnimation);
      progressBarHolder.setVisibility(View.GONE);
    }

    Intent intent =new Intent(PaymentActivity.this,PaymentFailed.class);
    startActivity(intent);
  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void indexScreen() {
    Analytics.index(PaymentActivity.this,"PaymentActivity");

  }
}
