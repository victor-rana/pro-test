package blackflame.com.zymepro.ui.liveshare;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.liveshare.dialog.DialogDisclaimer;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.LogUtils;
import blackflame.com.zymepro.util.ToastUtils;
import blackflame.com.zymepro.util.UtilityMethod;
import blackflame.com.zymepro.view.custom.CircleSeekbar;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

public class LiveShareActivity extends BaseActivity implements OnClickListener,AppRequest {
  private TextView tv_time_selected;
  private CircleSeekbar mMinuteSeekbar;
  int hour=0,minute;
  boolean isPathFollow=true;
  String TAG=LiveShareActivity.class.getCanonicalName();
  RelativeLayout layout_timer,layout_select_time;
  TextView timer,share,deactivate,generate_link;
  ImageView iv_reset;
  String car_id;
  FrameLayout progressbar;
  SimpleDateFormat formatter;
  long duration;
  CountDownTimer countDownTimer;
  TextView url;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_live_share);
    GlobalReferences.getInstance().baseActivity=this;
    car_id=getIntent().getStringExtra("carId");
    formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    initViews();
    Log.e(TAG, "onCreate: "+car_id );
  }


  private void initViews(){
    Toolbar toolbar= findViewById(R.id.toolbar_common);
    TextView   title= findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Share Live Tracking");
    layout_timer=findViewById(R.id.layout_timer);
    layout_select_time=findViewById(R.id.layout_select_time);
    timer=findViewById(R.id.tv_timer);
    share=findViewById(R.id.tv_live_share);
    deactivate=findViewById(R.id.tv_live_deactivate);
    generate_link=findViewById(R.id.tv_generate_url);
    url=findViewById(R.id.tv_url);
    iv_reset=findViewById(R.id.iv_reset);

    progressbar=findViewById(R.id.progressBarHolder);
    tv_time_selected=findViewById(R.id.tv_time_share);
    mMinuteSeekbar=findViewById(R.id.minute_seekbar);

    deactivate.setOnClickListener(this);
    generate_link.setOnClickListener(this);
    iv_reset.setOnClickListener(this);
    share.setOnClickListener(this);
    url.setOnClickListener(this);
    get_existing_url();
    mMinuteSeekbar.setOnSeekBarChangeListener(new CircleSeekbar.OnSeekBarChangeListener() {
      @Override
      public void onChanged(CircleSeekbar seekbar, int curValue) {
        Log.e(TAG, "onChanged: "+curValue );
        minute=curValue;
        if (curValue>55 && curValue<60){
          if (isPathFollow) {
            hour++;

            seekbar.setCurProcess(0);
            curValue=0;
            isPathFollow=false;
          }
        }

        if (curValue>10 && curValue<30 ){
          isPathFollow=true;
        }
        changeText(hour, curValue);
      }
    });


  }

  private void changeText(int hour, int minute) {

    String hourStr = hour > 9 ? hour + "" : "0" + hour;
    String minuteStr = minute > 9 ? minute + "" : "0" + minute;
    tv_time_selected.setText(hourStr + ":" + minuteStr+ ":00");
  }
  @Override
  public void onClick(View v) {
    switch(v.getId()){
      case R.id.tv_live_share:
        onShare(url.getText().toString());
        break;
      case R.id.tv_live_deactivate:
        deactivate_url();
        break;
      case R.id.tv_generate_url:
        int seconds=(hour*60*60)+(minute*60);
        if (seconds>0) {
          boolean shouldShowDisclaimer = CommonPreference.getInstance().getRemeber();
          if (!shouldShowDisclaimer) {
            Dialog dialog = new DialogDisclaimer(LiveShareActivity.this);
            dialog.show();
          } else {
            generate_url();

          }
        }else{
          ToastUtils.showShort("Please select duration.");
        }

        break;
      case R.id.tv_url:
        int sdk = android.os.Build.VERSION.SDK_INT;

        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
          android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
          clipboard.setText(url.getText().toString());
          ToastUtils.showShort("Text copied on clipboard");
          } else {
          android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
          android.content.ClipData clip = android.content.ClipData.newPlainText("label",url.getText().toString());
          clipboard.setPrimaryClip(clip);
          ToastUtils.showShort("Text copied on clipboard");
        }
        break;
      case R.id.iv_reset:
        hour=0;
        mMinuteSeekbar.setCurProcess(0);
        break;

    }

  }



  private void get_existing_url(){
    ApiRequests.getInstance().get_existing_url(GlobalReferences.getInstance().baseActivity,LiveShareActivity.this,car_id);
  }
  public void generate_url(){
    int seconds=(hour*60*60)+(minute*60);
    ApiRequests.getInstance().generate_url(GlobalReferences.getInstance().baseActivity,LiveShareActivity.this,car_id,String.valueOf(seconds));
  }
  private void deactivate_url(){
    ApiRequests.getInstance().deactivate_url(GlobalReferences.getInstance().baseActivity,LiveShareActivity.this,car_id);
  }


  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {
    progressbar.setVisibility(View.VISIBLE);

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    try{
      progressbar.setVisibility(View.GONE);
      JSONObject object=listener.getJsonResponse();
      LogUtils.error("GET",object.toString());

      if (listener.getTag().equals("existing")){
        String status=object.getString("status");
        Log.e(TAG, "onExistingUrl: "+formatter.format(new Date()) );
        if (status.equals("ERROR")){
          layout_select_time.setVisibility(View.VISIBLE);
          layout_timer.setVisibility(View.GONE);
          progressbar.setVisibility(View.GONE);
        }else if (status.equals("SUCCESS")){
          layout_select_time.setVisibility(View.GONE);
          progressbar.setVisibility(View.GONE);
          layout_timer.setVisibility(View.VISIBLE);
          String url_sharing=object.getJSONObject("msg").getString("sharing_url");
          String date=object.getJSONObject("msg").getString("expiry_time");

          try{
            url.setText(url_sharing);
            url.setMaxLines(1);
            //formatter.setTimeZone(TimeZone.getDefault());

            Date date_expired=formatter.parse(UtilityMethod.getUtcDate(date));
            Log.e(TAG, "onExistingUrl:date_expired"+date_expired );

            Date date_current=formatter.parse(formatter.format(new Date()));
            Log.e(TAG, "onExistingUrl: date_1"+date_current  );
            duration=date_expired.getTime()-date_current.getTime();
            Log.e(TAG, "onExistingUrl: dusration"+duration );

            startTimer(duration);
          }catch (Exception ex){
            Log.e(TAG, "onSuccess: "+ex.getLocalizedMessage());
          }
        }


      }else if (listener.getTag().equals("delete_url")){
        String status = object.getString("status");
        if (status.equals("SUCCESS")) {
          if (countDownTimer !=null){
            countDownTimer.onFinish();
            countDownTimer.cancel();
          }
          progressbar.setVisibility(View.GONE);
          layout_select_time.setVisibility(View.VISIBLE);
          layout_timer.setVisibility(View.GONE);



        }else if (status.equals("ERROR")){
          layout_timer.setVisibility(View.VISIBLE);
          layout_select_time.setVisibility(View.GONE);
          progressbar.setVisibility(View.GONE);

        }

      }else if (listener.getTag().equals("generate")) {
        String status = object.getString("status");
        if (status.equals("ERROR")) {

          layout_select_time.setVisibility(View.VISIBLE);
          layout_timer.setVisibility(View.GONE);


        } else if (status.equals("SUCCESS")) {

          layout_select_time.setVisibility(View.GONE);
          layout_timer.setVisibility(View.VISIBLE);
          progressbar.setVisibility(View.GONE);

          String url_sharing = object.getJSONObject("msg").getString("sharing_url");
          String date = object.getJSONObject("msg").getString("expiry_time");

          try {
            url.setText(url_sharing);
            url.setMaxLines(1);
            //formatter.setTimeZone(TimeZone.getDefault());

            Date date_expired = formatter.parse(UtilityMethod.getUtcDate(date));
            Log.e(TAG, "onExistingUrl:date_expired" + date_expired);

            Date date_current = formatter.parse(formatter.format(new Date()));
            Log.e(TAG, "onExistingUrl: date_1" + date_current);
            duration = date_expired.getTime() - date_current.getTime();
            Log.e(TAG, "onExistingUrl: dusration" + duration);

            startTimer(duration);
          } catch (Exception ex) {
            Log.e(TAG, "onSuccess: " + ex.getLocalizedMessage());
          }

        }
      }

    }catch (Exception ex){

    }
  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {
    doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
     progressbar.setVisibility(View.VISIBLE);
  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {


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
    Analytics.index(LiveShareActivity.this,"LiveShareActivity");
  }


  private void startTimer(long duration){
    layout_timer.setVisibility(View.VISIBLE);
    layout_select_time.setVisibility(View.GONE);



    countDownTimer=	new CountDownTimer(duration, 1000) {

      public void onTick(long millisUntilFinished) {


        timer.setText(UtilityMethod.timeConversion((int)millisUntilFinished / 1000,true));
        //here you can have your logic to set text to edittext
      }

      public void onFinish() {
        //timer.setText("Url expired!");
        layout_timer.setVisibility(View.GONE);
        layout_select_time.setVisibility(View.VISIBLE);
      }

    }.start();
  }

  public void onShare(String url) {

    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Live tracking link for my car using Zyme Pro");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "You can track my car using the following link :"+url);
    startActivity(Intent.createChooser(sharingIntent, "Share via"));


  }

}
