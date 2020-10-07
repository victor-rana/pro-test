package blackflame.com.zymepro.ui.profile.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.google.android.material.textfield.TextInputEditText;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.ActivityConstants;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.profile.ActivityProfile;
import blackflame.com.zymepro.ui.profile.ProfilePresenter;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.LogUtils;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import blackflame.com.zymepro.view.custom.WheelView;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateProfile extends BaseActivity implements UpdatePresenter.View ,AppRequest {
  TextView textView_brand_update,textView_model_update,textView_cc_update,textView_state_update;
  TextInputEditText textInputEditText_registration,textInputEditText_name_update;
  RadioButton radioButton_petrol_update,radioButton_diesel_update,radioButton;
  RadioGroup radioGroup_fuelType_update;
  Button btn_continue_update;
  WheelView wv_cc_update;
  String[] carBrand,carModel;
  boolean isRadiobtnchecked;
  String fuelType;
  String modified_state,selected_state;
  Context context;
  Toolbar toolbar_Profile_update;
  String brand_previous,model_previous,state_previous,car_id,cc_previous,
      fueltype_previous,name_previous,
      registration_previous,imei_return;
  UpdatePresenter presenter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_profile);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new UpdatePresenter(this);
    context=this;


    initViews();

  }
  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView tv_title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,tv_title,"Update");
    textView_brand_update= findViewById(R.id.tv_brand_update);
    textView_model_update= findViewById(R.id.tv_model_update);
    toolbar_Profile_update= findViewById(R.id.toolbar_common);
    textView_cc_update= findViewById(R.id.tv_cc_update);
    textInputEditText_registration= findViewById(R.id.tv_regnumber_update);
    textInputEditText_name_update= findViewById(R.id.tv_name_update);
    textView_state_update= findViewById(R.id.tv_state_update);
    radioGroup_fuelType_update= findViewById(R.id.rg_fueltype_update);
    btn_continue_update= findViewById(R.id.btn_carinfo_continue_update);
    radioButton_petrol_update= findViewById(R.id.btn_radio_petrol_update);
    radioButton_diesel_update= findViewById(R.id.btn_radio_diesel_update);

    Intent intent=getIntent();
    name_previous=intent.getStringExtra("nickname");
    registration_previous=intent.getStringExtra("registration_number");
    model_previous=intent.getStringExtra("model");
    brand_previous=intent.getStringExtra("brand");
    fueltype_previous=intent.getStringExtra("fueltype");
    cc_previous=intent.getStringExtra("cc");
    imei_return=intent.getStringExtra("imei");
    String state=intent.getStringExtra("state");
    LogUtils.error("UpdateProfile",state);
    modified_state=ActivityConstants.getDisplayText(state);
    if (modified_state==null){
      modified_state=state;
    }

    car_id=intent.getStringExtra("car_id");
    textInputEditText_registration.setText(registration_previous);
    textView_cc_update.setText(cc_previous);
    textInputEditText_name_update.setText(name_previous);
    textView_brand_update.setText(brand_previous);
    textView_model_update.setText(model_previous);
    textView_state_update.setText(modified_state);
    radioGroup_fuelType_update.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                            @Override
                                                            public void onCheckedChanged(RadioGroup group, int checkedId)
                                                            {
                                                              isRadiobtnchecked=true;
                                                              radioButton = findViewById(checkedId);
                                                              // Toast.makeText(getActivity(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                                                            }
                                                          }
    );

    if(fueltype_previous.equals("PETROL")){
      radioButton_petrol_update.setChecked(true);
    }else{
      radioButton_diesel_update.setChecked(true);
    }

    loadModel(brand_previous);
  onBrand();
  onCC();
  selectState();
  uploadData();
  onModel();

  loadBand();

  }
  private void loadBand(){
    ApiRequests.getInstance().getBrands(GlobalReferences.getInstance().baseActivity,UpdateProfile.this);

  }
  private void loadModel(String brand){
    ApiRequests.getInstance().getModels(GlobalReferences.getInstance().baseActivity,UpdateProfile.this,brand);

  }

  public void onBrand(){
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    textView_brand_update.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        View outerView = LayoutInflater
            .from(GlobalReferences.getInstance().baseActivity).inflate(R.layout.layout_wheel_selection, null);
        outerView.setBackgroundColor(context.getResources().getColor(R.color.grey));
        Button btn_confirm= outerView.findViewById(R.id.btn_confirm);
        wv_cc_update = outerView.findViewById(R.id.wheel_view_wv);
        wv_cc_update.setOffset(2);
        if(carBrand!=null) {
          wv_cc_update.setItems(Arrays.asList(carBrand));
        }
        wv_cc_update.setSeletion(0);
        wv_cc_update.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
          @Override
          public void onSelected(int selectedIndex, String item) {
            // Log.d("CarInfor", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
            textView_brand_update.setText(item);
            boolean isConnected=NetworkUtils.isConnected();
            if(isConnected) {
              loadModel(item);
            }else{
              ToastUtils.showShort("No internet");

            }
          }
        });
        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_title_dialog, null);
        TextView textView= titleView.findViewById(R.id.dialog_title);
        textView.setText("Select a brand");
        final AlertDialog dialog=  new AlertDialog.Builder(GlobalReferences.getInstance().baseActivity)
            .setTitle("Select a brand")
            .setView(outerView)
            .setCustomTitle(titleView)
//                        .setPositiveButton("OK", null)
            .show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
          }
        });
      }
    });
  }
  public void selectState(){
    textView_state_update.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        View outerView = LayoutInflater.from(GlobalReferences.getInstance().baseActivity).inflate(R.layout.layout_wheel_selection, null);
        outerView.setBackgroundColor(context.getResources().getColor(R.color.grey));
        Button btn_confirm= outerView.findViewById(R.id.btn_confirm);
        wv_cc_update = outerView.findViewById(R.id.wheel_view_wv);
        wv_cc_update.setOffset(2);
        wv_cc_update.setItems(Arrays.asList(ActivityConstants.STATELIST));
        wv_cc_update.setSeletion(0);
        wv_cc_update.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
          @Override
          public void onSelected(int selectedIndex, String item) {

            textView_state_update.setText(item);
            selected_state=item;
          }
        });
        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_title_dialog, null);
        TextView textView= titleView.findViewById(R.id.dialog_title);
        textView.setText("Select a state");
        final AlertDialog dialog= new AlertDialog.Builder(GlobalReferences.getInstance().baseActivity)
            .setTitle("Select a state")
            .setView(outerView)
            .setCustomTitle(titleView)
            .show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
//
            try {
              modified_state = selected_state.replace(" ", "-");
              //   Log.e(TAG, "onClick: "+modified_state );
            }catch (Exception ex){
              ToastUtils.showShort("Please select a state");
              }
            dialog.dismiss();
          }
        });
      }
    });
  }
  public void  onCC(){
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    textView_cc_update.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        View outerView = LayoutInflater.from(GlobalReferences.getInstance().baseActivity).inflate(R.layout.layout_wheel_selection, null);
        outerView.setBackgroundColor(context.getResources().getColor(R.color.grey));
        Button btn_confirm= outerView.findViewById(R.id.btn_confirm);
        wv_cc_update = outerView.findViewById(R.id.wheel_view_wv);
        wv_cc_update.setOffset(2);
        wv_cc_update.setItems(Arrays.asList(ActivityConstants.CC));
        wv_cc_update.setSeletion(0);
        wv_cc_update.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
          @Override
          public void onSelected(int selectedIndex, String item) {
            textView_cc_update.setText(item);

          }
        });
        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_title_dialog, null);
        TextView textView= titleView.findViewById(R.id.dialog_title);
        textView.setText("Select engine capacity");
        final AlertDialog dialog= new AlertDialog.Builder(GlobalReferences.getInstance().baseActivity)
            .setTitle("Select engine capacity")
            .setView(outerView)
            .setCustomTitle(titleView)
            .show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
          }
        });

      }
    });
  }
  public void uploadData() {
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F);
    btn_continue_update.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);

        btn_continue_update.setClickable(false);
        boolean isConnected = NetworkUtils.isConnected();
        if (isConnected) {
          final String registration_number = textInputEditText_registration.getText().toString();
          final String brand = textView_brand_update.getText().toString();
          final String model = textView_model_update.getText().toString();
          final String cc = textView_cc_update.getText().toString();
          final String name_owner = textInputEditText_name_update.getText().toString();
          if (isRadiobtnchecked) {
            fuelType = radioButton.getText().toString();
          }
          presenter.validateData(name_owner,registration_number,brand,model,cc,fuelType,modified_state);
        } else {
          ToastUtils.showShort("No internet");

        }

      }

    });
  }

  public void onModel(){
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    textView_model_update.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        View outerView = LayoutInflater
                .from(GlobalReferences.getInstance().baseActivity).inflate(R.layout.layout_wheel_selection, null);
        outerView.setBackgroundColor(context.getResources().getColor(R.color.grey));
        Button btn_confirm= outerView.findViewById(R.id.btn_confirm);
        wv_cc_update = outerView.findViewById(R.id.wheel_view_wv);
        wv_cc_update.setOffset(2);
        if(carModel!=null) {
          wv_cc_update.setItems(Arrays.asList(carModel));
        }
        wv_cc_update.setSeletion(0);
        wv_cc_update.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
          @Override
          public void onSelected(int selectedIndex, String item) {
            // Log.d("CarInfor", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
            textView_model_update.setText(item);
            boolean isConnected=NetworkUtils.isConnected();

          }
        });
        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_title_dialog, null);
        TextView textView= titleView.findViewById(R.id.dialog_title);
        textView.setText("Select a model");
        final AlertDialog dialog=  new AlertDialog.Builder(GlobalReferences.getInstance().baseActivity)
                .setTitle("Select a model")
                .setView(outerView)
                .setCustomTitle(titleView)
//                        .setPositiveButton("OK", null)
                .show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
          }
        });
      }
    });
  }


  @Override
  public void nameError() {
    textInputEditText_name_update.setError("Please enter car owner name.");
    btn_continue_update.setClickable(true);
  }

  @Override
  public void registrationError() {
    textInputEditText_registration.setError("Please enter car registration number.");
    btn_continue_update.setClickable(true);
  }

  @Override
  public void brandError() {
    ToastUtils.showShort("Please select car brand.");
    btn_continue_update.setClickable(true);
  }

  @Override
  public void modelError() {
    ToastUtils.showShort("Please select car model.");
    btn_continue_update.setClickable(true);
  }

  @Override
  public void ccError() {
    ToastUtils.showShort("Please select cc.");
    btn_continue_update.setClickable(true);
  }

  @Override
  public void fuelError() {
    ToastUtils.showShort("Please check fuel type.");
    btn_continue_update.setClickable(true);
  }

  @Override
  public void setBrand(String[] data) {

    this.carBrand=data;
    Log.e("TAG", "setBrand: "+carBrand.length );
  }

  @Override
  public void setModel(String[] model) {
    this.carModel=model;
    Log.e("TAG", "setModel: "+model.length );
  }

  @Override
  public void setStateError() {
    ToastUtils.showShort("Please select a state.");
    btn_continue_update.setClickable(true);
  }

  @Override
  public void uploadRegistration(String name, String reg, String brand, String model, String fuel,
      String state, String cc) {
    try{
      JSONObject data=new JSONObject();
      data.put("nickname",name);
      data.put("registration_number",reg);
      data.put("brand",brand);
      data.put("model",model);
      data.put("engine_capacity",cc);
      data.put("car_id",car_id);
      data.put("engine_type",fuel);
      data.put("IMEI",CommonPreference.getInstance().getImei());
      String code=ActivityConstants.getSelectedStateCode(state);
      if (code!=null){
        data.put("state",code);
      }else{
        data.put("state", modified_state);
      }
      ApiRequests.getInstance().updateCar(GlobalReferences.getInstance().baseActivity,UpdateProfile.this,data);
    }catch (JSONException ex){

    }


  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    Log.e("TAG", "onRequestCompleted: 1"+ listener.getTag());

    if (listener.getTag().equals("brand")){
      presenter.parseBrand(listener.getJsonResponse());
    }else if(listener.getTag().equals("model")){
      presenter.parseModel(listener.getJsonResponse());
    }

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
    try {

      Log.e("TAG", "onRequestCompleted: 2"+ listener.getTag());

      if (listener.getTag().equals("brand")){
        presenter.parseBrand(listener.getJsonResponse());
      }else if(listener.getTag().equals("model")){
        presenter.parseModel(listener.getJsonResponse());
      }else {

        JSONObject object = listener.getJsonResponse();
        String status = object.getString("status");
        String msg = object.getString("msg");
        new AwesomeSuccessDialog(GlobalReferences.getInstance().baseActivity)
                .setTitle("Success")
                .setMessage(msg)
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
                    Intent intent = new Intent(UpdateProfile.this, ActivityProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                  }
                })
                .show();
      }

    } catch (JSONException ex) {

    }
  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void indexScreen() {
    Analytics.index(UpdateProfile.this,"UpdateProfile");
  }
}
