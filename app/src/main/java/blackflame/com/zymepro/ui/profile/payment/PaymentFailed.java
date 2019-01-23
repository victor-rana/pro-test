package blackflame.com.zymepro.ui.profile.payment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import blackflame.com.zymepro.R;

public class PaymentFailed extends AppCompatActivity {
  Button button_home;
  TextView mail;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment_failed);
    mail=findViewById(R.id.drop_mail);
    mail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try{
          Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "support@getzyme.com"));
          intent.putExtra(Intent.EXTRA_SUBJECT, "");
          intent.putExtra(Intent.EXTRA_TEXT, "");
          startActivity(intent);
        }catch(ActivityNotFoundException e){
        }
      }
    });

    button_home=findViewById(R.id.btn_home);
    button_home.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }
}
