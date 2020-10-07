package blackflame.com.zymepro.ui.profile.payment;

import android.app.Activity;
import android.app.Dialog;
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
import blackflame.com.zymepro.util.UtilityMethod;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends BaseActivity implements OnClickListener,AppRequest,Instamojo.InstamojoPaymentCallback {
  private TextView nameBox,  phoneBox;
  private TextView emailBox,amount,gst_value,amount_payable,tv_month;
  String orderId,paymentId;

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
  int amountOne,amountTwo,amountThree;
  TextView tvAmount_1,tvAmount_2,tvAmount_3,tvDuration_1,tvDuration_2,tvDuration_3;
  int selectedAmount;
  String carId;

  Dialog progressDialog;

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
    Instamojo.getInstance().initialize(this, Instamojo.Environment.TEST);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Subscription Renewal");
    Intent intent=getIntent();
    String email=intent.getStringExtra("email");
    String name=intent.getStringExtra("name");
    String mobile=intent.getStringExtra("mobile");
    imei=intent.getStringExtra("imei");
    carId=intent.getStringExtra("carId");

    progressBarHolder =  findViewById(R.id.progressBarHolder);
    btn_six_month=findViewById(R.id.btn_six_month);
    btn_three_month=findViewById(R.id.btn_three_month);
    btn_twelve_month=findViewById(R.id.btn_twelve_month);


    tvAmount_1=findViewById(R.id.tvAmount_1);
    tvAmount_2=findViewById(R.id.tvAmount_2);
    tvAmount_3=findViewById(R.id.tvAmount_3);
    tvDuration_1=findViewById(R.id.tvDuration_1);
    tvDuration_2=findViewById(R.id.tvDuration_2);
    tvDuration_3=findViewById(R.id.tvDuration_3);


    button = findViewById(R.id.pay);
    button.setOnClickListener(this::onClick);

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

    dialog = new ProgressDialog(this);
    dialog.setIndeterminate(true);
    dialog.setMessage("Please wait...");
    dialog.setCancelable(false);



    progressDialog= UtilityMethod.showProgress(PaymentActivity.this);
    ApiRequests.getInstance().getSubscriptionAmount(GlobalReferences.getInstance().baseActivity,PaymentActivity.this);



    btn_three_month.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        rb_twelve.setChecked(false);
        rb_three.setChecked(true);
        rb_six.setChecked(false);
        CommonPreference.getInstance().setSubscriptionMonth(3);
        isChecked=true;

        selectedAmount=amountOne;
      }
    });
    btn_six_month.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        rb_twelve.setChecked(false);
        rb_three.setChecked(false);
        rb_six.setChecked(true);

        CommonPreference.getInstance().setSubscriptionMonth(6);
        isChecked=true;
        selectedAmount=amountTwo;
      }
    });
    btn_twelve_month.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        rb_twelve.setChecked(true);
        rb_three.setChecked(false);
        rb_six.setChecked(false);

        CommonPreference.getInstance().setSubscriptionMonth(12);
        isChecked=true;
        selectedAmount=amountThree;
      }
    });



  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
     case  R.id.pay:
       if (isChecked && selectedAmount != 0) {
         //fetchTokenAndTransactionID();
         String name = nameBox.getText().toString();
         final String email = emailBox.getText().toString();
         String phone = phoneBox.getText().toString();
         String amount = amount_payable.getText().toString().replace("₹ ", "").replace("/-","").trim();
         // Log.e("Tag", "onClick: "+name+email+phone+amount );

        JSONObject data=new JSONObject();
         try {
           data.put("amount",selectedAmount+"");
           data.put("car_id",carId);
         } catch (JSONException e) {
           e.printStackTrace();
         }




         progressDialog=UtilityMethod.showProgress(PaymentActivity.this);

         ApiRequests.getInstance().create_order(GlobalReferences.getInstance().baseActivity,PaymentActivity.this,data);




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
  private void showProgress(){
    progressBarHolder.setAnimation(inAnimation);
    progressBarHolder.bringToFront();
    progressBarHolder.setVisibility(View.VISIBLE);

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

    if (progressDialog != null){
      progressDialog.dismiss();
    }

    if (listener.getTag().equals("amount")){
      JSONObject data= null;
      try {
        data = listener.getJsonResponse().getJSONObject("msg");
        JSONArray array_amount=data.getJSONArray("amount");
        amountOne= array_amount.getJSONObject(0).getInt("current_amount");
        amountTwo=array_amount.getJSONObject(1).getInt("current_amount");
        amountThree=array_amount.getJSONObject(2).getInt("current_amount");
        tvAmount_1.setText("₹ "+amountOne);
        tvAmount_2.setText("₹ "+amountTwo);
        tvAmount_3.setText("₹ "+amountThree);

        tvDuration_2.setText(array_amount.getJSONObject(1).getString("label"));

        tvDuration_1.setText(array_amount.getJSONObject(0).getString("label"));
        tvDuration_3.setText(array_amount.getJSONObject(2).getString("label"));
      } catch (JSONException e) {
        e.printStackTrace();
      }

    }
    else if (listener.getTag().equals("create_order")){
      try {
        Instamojo.getInstance().initiatePayment(PaymentActivity.this, listener.getJsonResponse().getJSONObject("msg").getString("order_id"), this);
      }catch (JSONException ex){
        Log.e(TAG, "onOrderCreated: "+ex.getCause() );
      }
    }

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

    if (progressDialog != null){
      progressDialog.dismiss();
    }
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
    }else if (listener.getTag().equals("create_order")){
      try {
        Log.e(TAG, "onRequestCompleted: "+listener.getJsonResponse() );
        orderId=listener.getJsonResponse().getJSONObject("msg").getString("order_id");
        Instamojo.getInstance().initiatePayment(PaymentActivity.this, listener.getJsonResponse().getJSONObject("msg").getString("order_id"), this);
      }catch (JSONException ex){
        Log.e(TAG, "onOrderCreated: "+ex.getCause() );
      }
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

    if (progressDialog != null){
      progressDialog.dismiss();
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

  @Override
  public void onInstamojoPaymentComplete(String orderID, String transactionID, String paymentID, String paymentStatus) {

    Log.e(TAG, "onInstamojoPaymentComplete: "+transactionID );

    if (progressDialog != null){
      progressDialog.dismiss();
    }

    Intent intent=new Intent(PaymentActivity.this,PaymentSuccess.class);
    intent.putExtra("payment_id",paymentID);
    intent.putExtra("transactionID",transactionID);
    startActivity(intent);
    finish();

  }

  @Override
  public void onPaymentCancelled() {

  }

  @Override
  public void onInitiatePaymentFailure(String s) {

    try{
      JSONObject params=new JSONObject();
      params.put("IMEI", CommonPreference.getInstance().getSubscriptionImei());
      params.put("order_id", orderId);
      params.put("payment_status",s);
      params.put("month_count",CommonPreference.getInstance().getSubscriptionMonth());
      params.put("amount",selectedAmount);


      ApiRequests.getInstance().save_payment_failed(GlobalReferences.getInstance().baseActivity,PaymentActivity.this,params);



    }catch (JSONException ex){}

  }
}
