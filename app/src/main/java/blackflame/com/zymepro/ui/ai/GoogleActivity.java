package blackflame.com.zymepro.ui.ai;

import static blackflame.com.zymepro.util.UtilityMethod.openLink;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.util.Analytics;

public class GoogleActivity extends BaseActivity implements OnClickListener {
  Button btn_start_home;
  TextView buy;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_google);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();

  }
  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Google Home");
    btn_start_home=findViewById(R.id.btn_home_start);
    buy=findViewById(R.id.tv_home_by_now);
    btn_start_home.setOnClickListener(this);
    buy.setOnClickListener(this);


  }


  @Override
  public void onClick(View v) {

    switch (v.getId()){
      case R.id.btn_home_start:
        Intent i;
        PackageManager manager = getPackageManager();
        try {
          i = manager.getLaunchIntentForPackage("com.google.android.apps.chromecast.app");
          if (i == null)
            throw new PackageManager.NameNotFoundException();
          i.addCategory(Intent.CATEGORY_LAUNCHER);
          startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {

          openLink("https://play.google.com/store/apps/details?id=com.google.android.apps.chromecast.app");
        }
        break;
      case R.id.tv_home_by_now:
        openLink("https://www.flipkart.com/google-home/p/itmf3xz9aa6n6urf?affid=partnersg1");
        break;

    }

  }

  @Override
  public void indexScreen() {
    Analytics.index(GoogleActivity.this,"GoogleActivity");
  }
}
