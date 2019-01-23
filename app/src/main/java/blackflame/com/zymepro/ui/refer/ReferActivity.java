package blackflame.com.zymepro.ui.refer;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.util.ToastUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ReferActivity extends BaseActivity implements AppRequest {
  Button referFriend;
  TextView messageTwo;
  TextView referal;
  TextView toolbarText;
  LinearLayout llBack;
  Toolbar toolbar;
  String coupon_code;
  TextView textView_refertext,refer_count;
  Context context;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_refer);
    initViews();
  }

  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Invite Your Friends");

    referFriend = findViewById(R.id.refer_friend);
    referal = findViewById(R.id.referal_code);
    textView_refertext= findViewById(R.id.refer_text);
    refer_count= findViewById(R.id.tv_refer_count);
    coupon_code=CommonPreference.getInstance().getReferCode();

    referal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
          android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
          clipboard.setText("text to clip");
          ToastUtils.showShort("Text copied on clipboard");
             } else {
          android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
          android.content.ClipData clip = android.content.ClipData.newPlainText("label",referal.getText().toString());
          clipboard.setPrimaryClip(clip);
          ToastUtils.showShort("Text copied on clipboard");
          }


      }
    });

    messageTwo = findViewById(R.id.message2);
    referFriend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Check out Zyme Pro - the perfect companion for your car. It is a simple plug and play device which pairs your car to your smart phone and allows you to remotely track your car and it's performance. Avail INR 250/- off by using the below referral code on www.getzyme.com";
        // ;
        if (!referal.getText().toString().isEmpty()) {
          shareBody += " Referral code - " + referal.getText().toString();
          shareBody+="\nhttp://www.getzyme.com";
        }
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out - Zyme Pro!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
      }
    });

    loadReferMessage();
  }


  private void loadReferMessage(){
    ApiRequests.getInstance().load_refer(GlobalReferences.getInstance().baseActivity,ReferActivity.this);

  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    try{
    JSONObject jObject = listener.getJsonResponse();
    JSONObject carData = jObject.getJSONObject("msg");
    final int count=carData.getInt("referral_count");

    final String refer_code=carData.getString("coupon_code");
    final String refer_text=carData.getString("display_text");
      referal.setText(refer_code);
      textView_refertext.setText(refer_text);
      if (count==1 || count==0){
        refer_count.setText("You have referred Zyme Pro to "+count+" friend");

      }else
        refer_count.setText("You have referred Zyme Pro to "+count+" friends");

  } catch (JSONException e) {
    e.printStackTrace();
  }
  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public void onResponse(JSONObject response) {

  }
}
