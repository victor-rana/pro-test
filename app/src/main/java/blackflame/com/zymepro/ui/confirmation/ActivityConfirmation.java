package blackflame.com.zymepro.ui.confirmation;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.activation.ActivityActivation;
import blackflame.com.zymepro.ui.home.MainActivity;
import blackflame.com.zymepro.ui.wbview.WebActivity;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.NetworkUtils;

public class ActivityConfirmation extends BaseActivity {
  private Button btn_troubleshoot,btn_retry,btn_Ready;
  private TextView btn_contact,tv_allset;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_confirmation);

    initViews();




    btnContact();
    btnRetry();
    btnTroubleshoot();
    btnAllSet();
  }

  private void initViews(){
    LinearLayout rl_activate= findViewById(R.id.rl_allset);
    RelativeLayout rl_failed= findViewById(R.id.rl_activatio_failed);
    btn_retry= findViewById(R.id.btn_tryagain);
    btn_troubleshoot= findViewById(R.id.btn_troubleshoot);
    btn_contact= findViewById(R.id.btn_contact);
    btn_Ready= findViewById(R.id.btn_ready);
    Intent intent=getIntent();
    int status=intent.getIntExtra("status",-1);
    if(status==0){
      rl_failed.setVisibility(View.VISIBLE);
      rl_activate.setVisibility(View.GONE);
    }
    if(status==1){
      rl_failed.setVisibility(View.GONE);
      rl_activate.setVisibility(View.VISIBLE);
    }
  }


  private void btnAllSet(){
    btn_Ready.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(ActivityConfirmation.this,MainActivity.class);
        finish();
        startActivity(intent);

      }
    });
  }

  private void btnContact(){
    btn_contact.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isConnected = NetworkUtils.isConnected();
        if (isConnected) {
          String imei= CommonPreference.getInstance().getImei();
          Intent i = new Intent(Intent.ACTION_SEND);
          i.setType("message/rfc822");
          i.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@getzyme.com"});
          i.putExtra(Intent.EXTRA_SUBJECT, "Activation of device "+imei+" failed");
          i.putExtra(Intent.EXTRA_TEXT, "Hi Team Zyme,\n I tried to activate the device but it was not going through.\n" +
              "Please contact me on this number : <mention your number here>");
          try {
            startActivity(Intent.createChooser(i, "Send mail..."));
          } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ActivityConfirmation.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
          }
        }else{
          Toast.makeText(ActivityConfirmation.this, "No internet", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void btnRetry(){
    btn_retry.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(ActivityConfirmation.this,ActivityActivation.class));

      }
    });
  }
  private void btnTroubleshoot(){
    btn_troubleshoot.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isConnected=NetworkUtils.isConnected();
        if(isConnected) {
          Intent intent = new Intent(ActivityConfirmation.this, WebActivity.class);
          intent.putExtra("url", "http://getzyme.com/app/troubleshoot.html");
          startActivity(intent);
        }else{
          Toast.makeText(ActivityConfirmation.this, "No internet", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  @Override
  public void indexScreen() {
    Analytics.index(ActivityConfirmation.this,"ActivityCOnfirmation");
  }
}
