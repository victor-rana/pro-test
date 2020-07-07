package blackflame.com.zymepro.ui.breakdown;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.PermissionConstants;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.PermissionUtils;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;

public class BreakdownActivity extends BaseActivity implements OnClickListener,PermissionUtils.SimpleCallback {
  final int REQ_PERMISSION = 12;
  TextView textView_Title;
  String[] Brand = {
      "1800 266 0130",
      "1800 102 1800",
      "1800 102 4645",
      "1800 300 44444",
      "1800 209 3428",
      "1800 209 7400",
      "1800 209 3456",
      "1800 209 7979",
      "1800 209 6006",
      "1800 209 4646",
      "1800 102 1155",
      "1800 102 5001",
      "1800 103 8090",
      "1800 103 6800",
      "1800 103 2211",
      "1800 102 9222",
      "1800 102 2955",
      "1800 102 9100"

  };
  String number,callPhone;

  ImageView honda,maruti,hyundai,renault,fiat,ford,nissan,tata,mahindra,skoda,volkswagen,toyota,chevrolet,audi,bmw,mercedes,mitsubishi,volvo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_breakdown);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
  }



  private void initViews(){
  Toolbar toolbar_Breakdown= findViewById(R.id.toolbar_common);
  TextView  textView_Title= findViewById(R.id.toolbar_title);

    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar_Breakdown,textView_Title,"Breakdown");
    textView_Title.setText("Breakdown");
    honda= findViewById(R.id.honda);
    nissan= findViewById(R.id.nissan);
    renault= findViewById(R.id.renault);
    maruti= findViewById(R.id.suzuki);
    mitsubishi= findViewById(R.id.mitsubishi);
    mercedes= findViewById(R.id.mercedes);
    bmw= findViewById(R.id.bmw);
    volvo= findViewById(R.id.volvo);
    volkswagen= findViewById(R.id.volkswagen);
    hyundai= findViewById(R.id.hundai);
    fiat= findViewById(R.id.fiat);
    ford= findViewById(R.id.ford);
    tata= findViewById(R.id.tata);
    mahindra= findViewById(R.id.mahindra);
    skoda= findViewById(R.id.skoda);
    toyota= findViewById(R.id.toyota);
    chevrolet= findViewById(R.id.chevrolet);
    audi= findViewById(R.id.audi);
    honda.setOnClickListener(this);
    nissan.setOnClickListener(this);
    renault.setOnClickListener(this);
    maruti.setOnClickListener(this);
    mitsubishi.setOnClickListener(this);
    mercedes.setOnClickListener(this);
    bmw.setOnClickListener(this);
    volvo.setOnClickListener(this);
    volkswagen.setOnClickListener(this);
    hyundai.setOnClickListener(this);
    fiat.setOnClickListener(this);
    ford.setOnClickListener(this);
    tata.setOnClickListener(this);
    mahindra.setOnClickListener(this);
    skoda.setOnClickListener(this);
    toyota.setOnClickListener(this);
    chevrolet.setOnClickListener(this);
    audi.setOnClickListener(this);
  }



  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      default:
        return;
      case R.id.honda:
        number= Brand[0];
        makeCall(number);
        return;
      case R.id.suzuki:
        number=Brand[1];
        makeCall(number);
        return;
      case R.id.hundai:
        number=Brand[2];
        makeCall(number);
        return;
      case R.id.renault:
        number=Brand[3];
        makeCall(number);
        return;
      case R.id.fiat:
        number=Brand[4];
        makeCall(number);
        return;
      case R.id.ford:
        number=Brand[5];
        makeCall(number);
        return;
      case R.id.nissan:
        number=Brand[6];
        makeCall(number);
        return;
      case R.id.tata:
        number=Brand[7];
        makeCall(number);
        return;
      case R.id.mahindra:
        number=Brand[8];
        makeCall(number);
        return;
      case R.id.skoda:
        number=Brand[9];
        makeCall(number);
        return;
      case R.id.volkswagen:
        number=Brand[10];
        makeCall(number);
        return;
      case R.id.toyota:
        number=Brand[11];
        makeCall(number);
        return;
      case R.id.chevrolet:
        number=Brand[12];
        makeCall(number);
        return;
      case R.id.audi:
        number=Brand[13];
        makeCall(number);
        return;
      case R.id.bmw:
        number=Brand[14];
        makeCall(number);
        return;
      case R.id.mercedes:
        number=Brand[15];
        makeCall(number);
        return;
      case R.id.mitsubishi:
        number=Brand[16];
        makeCall(number);
        return;
      case R.id.volvo:
        number=Brand[17];
        makeCall(number);

    }

  }

  public void makeCall(String number){
    callPhone=number;
    new AwesomeInfoDialog(this)
        .setTitle("Call manufacturer helpline")
        .setColoredCircle(R.color.colorAccent)
        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
        .setCancelable(true)
        .setPositiveButtonText("Yes")
        .setPositiveButtonbackgroundColor(R.color.colorAccent)
        .setPositiveButtonTextColor(R.color.white)
        .setNegativeButtonText("No")
        .setNegativeButtonbackgroundColor(R.color.colorAccent)
        .setNegativeButtonTextColor(R.color.white)
        .setPositiveButtonClick(new Closure() {
          @Override
          public void exec() {
//            if(PermissionUtils.isGranted(PermissionConstants.PHONE)){

              Intent callIntent = new Intent(Intent.ACTION_DIAL);
              callIntent.setData(Uri.parse("tel:" + callPhone));
              startActivity(callIntent);

//            }else{
//
//                PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.PHONE));
//                PermissionUtils.sInstance.callback(BreakdownActivity.this);
//                PermissionUtils.sInstance.request();
//
//            }
          }
        })

        .setNegativeButtonClick(new Closure() {
          @Override
          public void exec() {
            //click
          }
        })
        .show();
  }

  @Override
  public void onGranted() {

  }

  @Override
  public void onDenied() {

  }

  @Override
  public void indexScreen() {
    Analytics.index(BreakdownActivity.this,"BreakDownActivity");

  }
}
