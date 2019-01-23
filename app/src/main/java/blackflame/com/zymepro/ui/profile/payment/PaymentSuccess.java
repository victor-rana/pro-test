package blackflame.com.zymepro.ui.profile.payment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.db.CommonPreference;

public class PaymentSuccess extends BaseActivity {
  final float growTo = 1.2f;
  final long duration = 1000;
  Button btn_home;
  TextView month,tv_payment_id;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment_success);
    initViews();

  }



  private void initViews(){
    final ImageView success=findViewById(R.id.iv_success);
    btn_home=findViewById(R.id.button_home);
    month=findViewById(R.id.tv_increased_month);
    tv_payment_id=findViewById(R.id.tv_payment_id);
    String payment_id=getIntent().getStringExtra("payment_id");
    tv_payment_id.setText("Your payment ID : "+payment_id);
    month.setText("Subscription time increased by "+CommonPreference.getInstance().getSubscriptionMonth()+" months");
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
    btn_home.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });


  }
}
