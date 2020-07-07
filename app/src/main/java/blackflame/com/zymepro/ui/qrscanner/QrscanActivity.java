package blackflame.com.zymepro.ui.qrscanner;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.carregistration.CarRegistration;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.ToastUtils;
import com.android.volley.NetworkResponse;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import org.json.JSONException;
import org.json.JSONObject;

public class QrscanActivity extends BaseActivity implements ZBarScannerView.ResultHandler,AppRequest {

  String scanResult, registration, imei_return, car_id;
  private ZBarScannerView mScannerView;
  int sendCode;
  String email;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_qrscan);
    GlobalReferences.getInstance().baseActivity=this;

    mScannerView = new ZBarScannerView(this);   // Programmatically initialize the scanner view
    setContentView(mScannerView);
    CommonPreference.initializeInstance(QrscanActivity.this);

    Intent intent = getIntent();
    if (intent != null) {
      sendCode = intent.getIntExtra("coming", 0);
      if (sendCode == 2) {
        //registration=intent.getStringExtra("registration");
        car_id = CommonPreference.getInstance().getReplacementCarid();
        imei_return = CommonPreference.getInstance().getSelectedReplacement();
      }
      email = CommonPreference.getInstance().getEmail();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
//        qrScan.initiateScan();
    mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
    mScannerView.setFocusable(true);
    mScannerView.startCamera();
  }

  @Override
  protected void onPause() {
    super.onPause();

    mScannerView.stopCamera();
  }

  @Override
  public void handleResult(Result result) {
    if (result != null) {
      scanResult = result.getContents();
      // Toast.makeText(this, scanResult, Toast.LENGTH_SHORT).show();
      // Log.e("IMEI", "handleResult: "+scanResult );
      if (sendCode == 1) {
        linkDevice(scanResult);

      } else if (sendCode == 2) {
        if (scanResult != null && !scanResult
            .equals(CommonPreference.getInstance().getSelectedReplacement())) {
         // sendReplaceCode();
        } else {
          ToastUtils.showShort("You can not replace this device with existing device");
        }

      }
    }
  }

  private void linkDevice(String imei){
    try{
      JSONObject data=new JSONObject();
      data.put("IMEI",imei);
      data.put("email_id",CommonPreference.getInstance().getEmail());
      ApiRequests.getInstance().uploadImei(GlobalReferences.getInstance().baseActivity,QrscanActivity.this,data);


    }catch (JSONException ex){

    }
  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {

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
    try{
      JSONObject object=listener.getJsonResponse();
      String topicName = object.getString("topic_name");
      CommonPreference.getInstance().setSubscriptionTopic(topicName);
      CommonPreference.getInstance().setImei(scanResult);
      Intent intent = new Intent(QrscanActivity.this, CarRegistration.class);
      intent.putExtra("currentItem", 2);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
      finish();
    }catch (JSONException ex){

    }



  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    try{
      doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
      NetworkResponse response = listener.getVolleyError().networkResponse;
      if(response != null && response.data != null) {
        switch (response.statusCode) {
          case 400:
            // Log.e("Error", "onErrorResponse: "+new String(response.data));
            try {
              JSONObject obj = new JSONObject(new String(response.data));
              String msg = obj.getString("msg");
              new AwesomeErrorDialog(GlobalReferences.getInstance().baseActivity)
                  .setTitle("Alert")
                  .setMessage(msg)
                  .setColoredCircle(R.color.colorAccent)
                  .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                  .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                  .setButtonBackgroundColor(R.color.colorAccent)
                  .setButtonText(getString(R.string.dialog_ok_button))
                  .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                      // click
                    }
                  })
                  .show();


            } catch (JSONException e) {
              e.printStackTrace();
            }

            break;


        }
      }
    }catch (Exception e){

    }
  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void indexScreen() {
    Analytics.index(QrscanActivity.this,"QrscanActivity");
  }
}
