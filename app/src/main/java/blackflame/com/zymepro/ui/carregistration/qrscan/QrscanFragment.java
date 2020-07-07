package blackflame.com.zymepro.ui.carregistration.qrscan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.PermissionConstants;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.carregistration.CarRegistration;
import blackflame.com.zymepro.ui.qrscanner.QrscanActivity;
import blackflame.com.zymepro.util.PermissionUtils;
import com.android.volley.NetworkResponse;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import org.json.JSONException;
import org.json.JSONObject;

public class QrscanFragment extends CommonFragment implements AppRequest ,PermissionUtils.SimpleCallback{

  private static final String TAG = QrscanFragment.class.getCanonicalName();
  Button btnScan;
  String scanResult,body,status_message,msg;
  TextInputEditText et_qrcode;
  TextWatcher watcher;
  String imei;
  boolean isDataSubmit=false;
  TextView textView_barcode_locator;
  final  int REQ_PERMISSION=12;
  int count_text;
  Context context;
  Activity activity;
  AppCompatImageView imageView_sendCode;
  String code_qr;



  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.layout_qr_fragment, container, false);
    ImageView imageView = v.findViewById(R.id.image_gif);
    GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
    Glide.with(getContext()).load(R.mipmap.barcode_black).into(imageViewTarget);
    btnScan= v.findViewById(R.id.btn_scan);
    // mScannerView = new ZBarScannerView(getActivity());
    textView_barcode_locator= v.findViewById(R.id.bar_code_locater);
    imageView_sendCode= v.findViewById(R.id.image_send_code);
    CommonPreference.initializeInstance(getActivity());
    textView_barcode_locator.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AwesomeInfoDialog(getActivity())
            .setTitle("Can't find code?")
            .setMessage("You can find the bar code on top of device. In case of any issues, please drop us a mail at support@getzyme.com and our technician will get in touch with you in 24 hours")
            .setColoredCircle(R.color.colorAccent)
            .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
            .setCancelable(true)
            .setPositiveButtonText("Ok")
            .setPositiveButtonbackgroundColor(R.color.colorAccent)
            .setPositiveButtonTextColor(R.color.white)
            .setPositiveButtonClick(new Closure() {
              @Override
              public void exec() {
                //click
              }
            })

            .show();

      }
    });
    et_qrcode= v.findViewById(R.id.et_qrcode);
    et_qrcode.setTag(false);
    et_qrcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        et_qrcode.setTag(true);
      }
    });
    sendCode();
    et_qrcode.addTextChangedListener(watcher);

    imageView_sendCode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        code_qr =et_qrcode.getText().toString();
        if(code_qr!=null){
          upLoadQrCode(code_qr);
        }
      }
    });
    btnScan();
    return v;
  }

  public void btnScan(){
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);
    btnScan.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        if (!PermissionUtils.isGranted(PermissionConstants.CAMERA)){
          PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.CAMERA));
          PermissionUtils.sInstance.callback(QrscanFragment.this);
          PermissionUtils.sInstance.request();
        }else{
          sendScanActivity();
        }
      }
    });
  }
  public void sendScanActivity(){
    Intent intent=new Intent(getActivity(), QrscanActivity.class);
    intent.putExtra("coming",1);
    startActivity(intent);
    getActivity().finish();
  }


  private void upLoadQrCode(String code){
    try {
      JSONObject data=new JSONObject();
      data.put("IMEI",code);
      data.put("email_id",CommonPreference.getInstance().getEmail());
      ApiRequests.getInstance().uploadImei(GlobalReferences.getInstance().baseActivity,QrscanFragment.this,data);

    }catch (JSONException ex){

    }
  }




  public void sendCode(){
    watcher=new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Log.e("tag", "beforeTextChanged: "+"after"+after+"count"+count );


      }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Log.e("Text", "onTextChanged: "+count );
      }

      @Override
      public void afterTextChanged(Editable s) {
        //Log.e("Text", "afterTextChanged: "+s.length() );
        if(s.length()==12) {
          code_qr=s.toString();
          upLoadQrCode(code_qr);
        }

      }
    };
  }


  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {

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
          CommonPreference.getInstance().setImei(code_qr);
          GlobalReferences.getInstance().baseActivity.hideSoftKeyboard();
          ((CarRegistration) GlobalReferences.getInstance().baseActivity).mViewPager.setCurrentItem(2);

        }catch (JSONException ex){

          Log.e(TAG, "onRequestCompleted: "+ex.getCause() );

        }

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    try{
      NetworkResponse response = listener.getVolleyError().networkResponse;
      if(response != null && response.data != null) {
        switch (response.statusCode) {
          case 400:
            // Log.e("Error", "onErrorResponse: "+new String(response.data));
            try {
              JSONObject obj = new JSONObject(new String(response.data));
              String msg = obj.getString("msg");
              new AwesomeErrorDialog(getActivity())
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

      Log.e(TAG, "onRequestFailed: "+e.getCause() );

    }



  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void onGranted() {

    sendScanActivity();

  }

  @Override
  public void onDenied() {

  }
}
