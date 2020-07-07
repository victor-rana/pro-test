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

public class AlexaActivity extends BaseActivity implements OnClickListener {
  Button btn_start_alexa;
  TextView buy;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alexa);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();

  }


  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Alexa");
    btn_start_alexa=findViewById(R.id.btn_alexa_start);
    buy=findViewById(R.id.tv_alexa_by_now);

    btn_start_alexa.setOnClickListener(this);
    buy.setOnClickListener(this);

  }


  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.btn_alexa_start:
        Intent i;
        PackageManager manager = getPackageManager();
        try {
          i = manager.getLaunchIntentForPackage("com.amazon.dee.app");
          if (i == null)
            throw new PackageManager.NameNotFoundException();
          i.addCategory(Intent.CATEGORY_LAUNCHER);
          startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {

          openLink("https://play.google.com/store/apps/details?id=com.amazon.dee.app");
        }
        break;
      case R.id.tv_alexa_by_now:
        openLink("https://www.amazon.in/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=alexa&tag=oneshopapp-21&linkCode=ur2&linkId=eb1297399a4f7754ddcbf179014d8f8c&camp=3638&creative=24630");

        break;
    }
  }

  @Override
  public void indexScreen() {
    Analytics.index(AlexaActivity.this,"AlexaActivity");
  }
}
