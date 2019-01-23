package blackflame.com.zymepro.ui.shopping;

import static blackflame.com.zymepro.util.UtilityMethod.openLink;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;


public class ZymeShop extends BaseActivity implements View.OnClickListener {
  CardView air,hud,cam,volt,pro,lite;
  String [] urls=new String[]{
      "https://getzyme.com/pro/?utm_source=pro&utm_medium=application",
      "https://getzyme.com/air/?utm_source=pro&utm_medium=application",
      "https://getzyme.com/hud/?utm_source=pro&utm_medium=application",
      "https://getzyme.com/cam/?utm_source=pro&utm_medium=application",
      "https://getzyme.com/volt/?utm_source=pro&utm_medium=application",
      "https://getzyme.com/lite/?utm_source=pro&utm_medium=application"
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_zyme_shop);
    initViews();

  }
  public void forceCrash(View view) {
    throw new RuntimeException("This is a crash");
  }

  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Zyme Shop");

    air=findViewById(R.id.btn_buy_air);
    hud=findViewById(R.id.btn_buy_hud);
    cam=findViewById(R.id.btn_buy_cam);
    volt=findViewById(R.id.btn_buy_volt);
    pro=findViewById(R.id.btn_buy_pro);
    lite=findViewById(R.id.btn_buy_lite);

    air.setOnClickListener(this);
    hud.setOnClickListener(this);
    cam.setOnClickListener(this);
    volt.setOnClickListener(this);
    pro.setOnClickListener(this);
    lite.setOnClickListener(this);
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.btn_buy_air:
        //Prosingleton.getInstance().trackEvent("Shopping","Air click","Shopping Activity");
        openLink(urls[1]);
        break;
      case R.id.btn_buy_cam:
       // Prosingleton.getInstance().trackEvent("Shopping","Cam click","Shopping Activity");
        openLink(urls[3]);

        break;
      case R.id.btn_buy_volt:
        //Prosingleton.getInstance().trackEvent("Shopping","Volt click","Shopping Activity");

        openLink(urls[4]);
        break;
      case R.id.btn_buy_hud:
       // Prosingleton.getInstance().trackEvent("Shopping","Hud click","Shopping Activity");

        openLink(urls[2]);
        break;
      case R.id.btn_buy_pro:
       // Prosingleton.getInstance().trackEvent("Shopping","Pro click","Shopping Activity");

        openLink(urls[0]);
        break;
      case R.id.btn_buy_lite:
       // Prosingleton.getInstance().trackEvent("Shopping","Lite click","Shopping Activity");
        openLink(urls[5]);
        break;
    }
  }
}
