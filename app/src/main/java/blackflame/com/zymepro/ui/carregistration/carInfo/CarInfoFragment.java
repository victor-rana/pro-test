package blackflame.com.zymepro.ui.carregistration.carInfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.ActivityConstants;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.activation.ActivityActivation;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import blackflame.com.zymepro.view.custom.WheelView;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

public class CarInfoFragment extends CommonFragment implements AppRequest,CarRegistrationPresenter.View {
  TextView textView_brand,textView_model,textView_cc,textView_state;
  TextInputEditText textInputEditText_registration,textInputEditText_name;
  RadioButton radioButton_petrol,radioButton_diesel,radioButton;
  RadioGroup radioGroup_fuelType;
  Button btn_continue;
  WheelView wv_cc;
  String[] carBrand,carModel;
  boolean isRadiobtnchecked;
  View v;
  String fuelType;
  String modified_state,selected_state,imei;
  Context context;
  CarRegistrationPresenter presenter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
  v = inflater.inflate(R.layout.layout_car_info, container, false);
  presenter=new CarRegistrationPresenter(this);
  initViews(v);
  context=getActivity();
  radioGroup_fuelType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                       isRadiobtnchecked=true;
                                                       radioButton = v.findViewById(checkedId);
                                                     }
                                                   }
    );
    if(NetworkUtils.isConnected()) {
     loadBrand();
    }else{
      Toast.makeText(getActivity(), "No internet.", Toast.LENGTH_SHORT).show();
    }
    return v;
  }

  private void initViews(View v){
    textView_brand= v.findViewById(R.id.tv_brand);
    textView_model= v.findViewById(R.id.tv_model);
    textView_cc= v.findViewById(R.id.tv_cc);
    textInputEditText_registration= v.findViewById(R.id.tv_regnumber);
    textInputEditText_name= v.findViewById(R.id.tv_name);
    textView_state= v.findViewById(R.id.tv_state);
    radioGroup_fuelType= v.findViewById(R.id.rg_fueltype);
    btn_continue= v.findViewById(R.id.btn_carinfo_continue);
    radioButton_petrol= v.findViewById(R.id.btn_radio_petrol);
    radioButton_diesel= v.findViewById(R.id.btn_radio_diesel);
    selectState();
    onCC();
    onBrand();
    onModel();
    uploadData();

  }


  public void onModel(){
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    textView_model.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        if(carModel!=null) {
          View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_wheel_selection, null);
          outerView.setBackgroundColor(context.getResources().getColor(R.color.grey));
          Button btn_confirm= outerView.findViewById(R.id.btn_confirm);
          wv_cc = outerView.findViewById(R.id.wheel_view_wv);
          wv_cc.setOffset(2);
          if(carModel!=null) {
            wv_cc.setItems(Arrays.asList(carModel));
          }
          wv_cc.setSeletion(0);
          wv_cc.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
              textView_model.setText(item);
              }
          });

          LayoutInflater inflater = getActivity().getLayoutInflater();
          View titleView = inflater.inflate(R.layout.custom_title_dialog, null);
          TextView textView= titleView.findViewById(R.id.dialog_title);
          textView.setText("Select a model");
          final AlertDialog dialog=   new AlertDialog.Builder(getActivity())
              .setTitle("Select a model")
              .setView(outerView)
              .setCustomTitle(titleView)
//                            .setPositiveButton("OK", null)
              .show();
          dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
          });
        }else{
          Toast.makeText(getActivity(),"Something error please try again.",Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
  public void onBrand(){
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    textView_brand.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_wheel_selection, null);
        outerView.setBackgroundColor(context.getResources().getColor(R.color.grey));
        Button btn_confirm= outerView.findViewById(R.id.btn_confirm);
        wv_cc = outerView.findViewById(R.id.wheel_view_wv);
        wv_cc.setOffset(2);
        if(carBrand!=null) {
          wv_cc.setItems(Arrays.asList(carBrand));
        }
        wv_cc.setSeletion(0);
        wv_cc.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
          @Override
          public void onSelected(int selectedIndex, String item) {
            // Log.d("CarInfor", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
            textView_brand.setText(item);
            boolean isConnected=NetworkUtils.isConnected();
            if(isConnected) {
              loadModel(item);
            }else{
              Toast.makeText(getActivity(), "No internet.", Toast.LENGTH_SHORT).show();
            }
//                        wv_cc.setSeletion(selectedIndex);
          }
        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_title_dialog, null);
        TextView textView= titleView.findViewById(R.id.dialog_title);
        textView.setText("Select a brand");
        final AlertDialog dialog=  new AlertDialog.Builder(getActivity())
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
    textView_state.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_wheel_selection, null);
        outerView.setBackgroundColor(context.getResources().getColor(R.color.grey));
        Button btn_confirm= outerView.findViewById(R.id.btn_confirm);
        wv_cc = outerView.findViewById(R.id.wheel_view_wv);
        wv_cc.setOffset(2);
        wv_cc.setItems(Arrays.asList(ActivityConstants.STATELIST));
        wv_cc.setSeletion(0);
        wv_cc.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
          @Override
          public void onSelected(int selectedIndex, String item) {
            textView_state.setText(item);
            selected_state=item;
          }
        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_title_dialog, null);
        TextView textView= titleView.findViewById(R.id.dialog_title);
        textView.setText("Select a state");
        final AlertDialog dialog= new AlertDialog.Builder(getActivity())
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
              Toast.makeText(getActivity(), "Please select a state", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
          }
        });
      }
    });
  }
  public void  onCC(){
    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    textView_cc.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        View outerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_wheel_selection, null);
        outerView.setBackgroundColor(context.getResources().getColor(R.color.grey));
        Button btn_confirm= outerView.findViewById(R.id.btn_confirm);
        wv_cc = outerView.findViewById(R.id.wheel_view_wv);
        wv_cc.setOffset(2);
        wv_cc.setItems(Arrays.asList(ActivityConstants.CC));
        wv_cc.setSeletion(0);
        wv_cc.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
          @Override
          public void onSelected(int selectedIndex, String item) {
            textView_cc.setText(item);

          }
        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View titleView = inflater.inflate(R.layout.custom_title_dialog, null);
        TextView textView= titleView.findViewById(R.id.dialog_title);
        textView.setText("Select engine capacity");
        final AlertDialog dialog= new AlertDialog.Builder(getActivity())
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
    btn_continue.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);

        btn_continue.setClickable(false);
        boolean isConnected = NetworkUtils.isConnected();
        if (isConnected) {
          final String registration_number = textInputEditText_registration.getText().toString();
          final String brand = textView_brand.getText().toString();
          final String model = textView_model.getText().toString();
          final String cc = textView_cc.getText().toString();
          final String name_owner = textInputEditText_name.getText().toString();
          if (isRadiobtnchecked) {
            fuelType = radioButton.getText().toString();
          }
          presenter.validateData(name_owner,registration_number,brand,model,cc,fuelType,modified_state);
        } else {
          Toast.makeText(getActivity(), "No internet.", Toast.LENGTH_SHORT).show();
        }

      }

    });
  }
  private void loadBrand(){
    ApiRequests.getInstance().getBrands(GlobalReferences.getInstance().baseActivity,CarInfoFragment.this);
  }
  private void loadModel(String brand){
    ApiRequests.getInstance().getModels(GlobalReferences.getInstance().baseActivity,CarInfoFragment.this,brand);

  }

  @Override
  public void nameError() {
    textInputEditText_name.setError("Please enter car owner name.");
    btn_continue.setClickable(true);
  }

  @Override
  public void registrationError() {
    textInputEditText_registration.setError("Please enter car registration number.");
    btn_continue.setClickable(true);
  }

  @Override
  public void brandError() {
  ToastUtils.showShort("Please select car brand.");
    btn_continue.setClickable(true);
  }

  @Override
  public void modelError() {
    ToastUtils.showShort("Please select car model.");
    btn_continue.setClickable(true);
  }

  @Override
  public void ccError() {
    ToastUtils.showShort("Please select cc.");
    btn_continue.setClickable(true);

  }

  @Override
  public void fuelError() {
    ToastUtils.showShort("Please check fuel type.");
    btn_continue.setClickable(true);

  }

  @Override
  public void setBrand(String[] data) {
   this.carBrand=data;
  }

  @Override
  public void setModel(String[] model) {
   this.carModel=model;
  }

  @Override
  public void setStateError() {

    ToastUtils.showShort("Please select a state.");
    btn_continue.setClickable(true);

  }

  @Override
  public void uploadRegistration(String name, String reg, String brand, String model, String fuel, String state,String cc) {
    try{
      JSONObject data=new JSONObject();
      data.put("nickname",name);
      data.put("registration_number",reg);
      data.put("brand",brand);
      data.put("model",model);
      data.put("engine_capacity",cc);
      data.put("email_id",CommonPreference.getInstance().getEmail());
      data.put("engine_type",fuel);
      data.put("IMEI",CommonPreference.getInstance().getImei());
      String code=ActivityConstants.getSelectedStateCode(state);
      if (code!=null){
        data.put("state",code);
      }else{
        data.put("state", modified_state);
      }
      ApiRequests.getInstance().register_car(GlobalReferences.getInstance().baseActivity,CarInfoFragment.this,data);
    }catch (JSONException ex){

    }

  }


  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {
  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
      if (listener.getTag().equals("brand")){
          presenter.parseBrand(listener.getJsonResponse());
      }else if(listener.getTag().equals("model")){
        presenter.parseModel(listener.getJsonResponse());
      }
  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {
  }
  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
  }
  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    btn_continue.setClickable(true);
    try {
      JSONObject jsonProfile = listener.getJsonResponse();

      String status=jsonProfile.getString("status");
      if (status!=null && status.equals("ERROR")){
        String message=jsonProfile.getString("msg");

        ActivityConstants.showAlert(getActivity(),message);
      }else {
        JSONObject object = jsonProfile.getJSONObject("msg");
        CommonPreference.getInstance().setCarId(object.getString("car_id"));
        int devicecount = CommonPreference.getInstance().getDeviceCount();
        CommonPreference.getInstance().setName(textInputEditText_name.getText().toString());
        CommonPreference.getInstance().setDeviceLinked(true);
        CommonPreference.getInstance().setDeviceCount(devicecount + 1);
        startActivity(new Intent(getActivity(), ActivityActivation.class));
        getActivity().finish();
      }
    } catch (JSONException ex) {

    }

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
  }
  @Override
  public void onResponse(JSONObject response) {

  }

}
