package blackflame.com.zymepro.ui.enginescan;

import static blackflame.com.zymepro.util.UtilityMethod.getDate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings.Global;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.Prosingleton;
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
import blackflame.com.zymepro.ui.enginescan.adapter.ErrorListAdapter;
import blackflame.com.zymepro.ui.enginescan.model.ErrorCode;
import blackflame.com.zymepro.ui.enginescan.model.ErrorCodeData;
import blackflame.com.zymepro.ui.pitstop.PitstopActivity;
import blackflame.com.zymepro.util.LogUtils;
import blackflame.com.zymepro.util.ToastUtils;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EngineScanActivity extends BaseActivity implements OnClickListener,AppRequest,ScanPresenter.View {
  ImageView imageview;
  int counter=0;
  TextView textView_Information;
  ImageView imageView_Error;
  String body,status_message,msg;
  ArrayList<ErrorCodeData> list_error;
  String last_update_time;
  Context context;
  CountDownTimer timer;
  TextView textView_mechanic_near_by;
  Button btn_scan;
  int width;
  View view_animation;
  String[] list_error_code;
  ArrayList<String> errorcode_list;
  RelativeLayout linearLayout_after_scan,relativeLayout_btn_scan;
  int transmission_count, air_fuel_count, speed_control_count, auxillary_count, ignition_count, computer_output_count;
  ImageView imageView_transmission, imageView_air_fuel, imageView_speed_control, imageView_auxillary, imageView_ignition, imageView_computer_out;
  LinearLayout linearLayout_dot_first, linearLayout_dot_second, linearLayout_dot_third, linearLayout_dot_forth, linearLayout_dot_fifth, linearLayout_dot_sixth;
  TextView textView_transmission, textView_air_fuel, textView_speed_control, textView_auxillary, textView_ignition, textView_computer_out;
  ImageView iv_rescan;
  TextView tv_last_scan_date;
  boolean isDatashown=false;
  TextView tv_battery_voltage,tv_coolant_temp;
  boolean isbtConnected=false;
  String[] error_list;
  ArrayList<ErrorCode> list,list_server;
  View imageview_animation;

  String title=null,description=null;
  LinearLayout linearLayout_transmission,linearLayout_air_fuel,linearLayout_speed_control,linearLayout_auxillary,linearLayout_ignition,linearLayout_computer_output;

  ErrorListAdapter listAdapter;
  JSONArray array_error_code;
  String car_status;
  TranslateAnimation animation;
  ScanPresenter presenter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_engine_scan);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new ScanPresenter(this);
    initViews();
  }

  private void initViews(){
  Toolbar  toolbar = findViewById(R.id.toolbar_common);
 TextView  tv_title = findViewById(R.id.toolbar_title);
 GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,tv_title,"Engine Diagnostics");
    view_animation=findViewById(R.id.view);
    textView_mechanic_near_by = findViewById(R.id.tv_diagnostics_near_by);
    linearLayout_after_scan = findViewById(R.id.rl_diagnostic_status);
    linearLayout_dot_first = findViewById(R.id.lineradot1);
    linearLayout_dot_second = findViewById(R.id.lineradot2);
    linearLayout_dot_third = findViewById(R.id.lineradot3);
    linearLayout_dot_forth = findViewById(R.id.lineradot4);
    linearLayout_dot_fifth = findViewById(R.id.lineradot5);
    linearLayout_dot_sixth = findViewById(R.id.lineradot6);
    relativeLayout_btn_scan= findViewById(R.id.rl_backgroung_btn);
    tv_battery_voltage= findViewById(R.id.tv_battry_voltage);
    tv_coolant_temp= findViewById(R.id.tv_coolant_temp);
    btn_scan = findViewById(R.id.btn_scan);
    view_animation = findViewById(R.id.view);
    imageView_air_fuel = findViewById(R.id.iv_diagnostics_air_fuel);
    imageView_auxillary = findViewById(R.id.iv_diagnostics_auxillary_emission);
    imageView_computer_out = findViewById(R.id.iv_diagnostics_computer_output);
    imageView_ignition = findViewById(R.id.iv_diagnostics_ignition_system);
    imageView_speed_control = findViewById(R.id.iv_diagnostics_speed_control);
    imageView_transmission = findViewById(R.id.iv_diagnostics_transmission);
    textView_air_fuel = findViewById(R.id.tv_diagnostic_error_air_fuel);
    textView_auxillary = findViewById(R.id.tv_diagnostic_error_auxillary_emmission);
    textView_computer_out = findViewById(R.id.tv_diagnostic_error_computer_output);
    textView_ignition = findViewById(R.id.tv_diagnostic_error_ignition_system);
    textView_speed_control = findViewById(R.id.tv_diagnostic_error_speed_control);
    textView_transmission = findViewById(R.id.tv_diagnostic_error_transmission);
    iv_rescan = findViewById(R.id.iv_rescan);
    tv_last_scan_date= findViewById(R.id.tv_last_scan_date);
    linearLayout_auxillary= findViewById(R.id.ll_diagnostic_auxillary_emission);
    linearLayout_computer_output= findViewById(R.id.ll_diagnostic_computer_control);
    linearLayout_speed_control= findViewById(R.id.ll_diagnostic_speed_control);
    linearLayout_transmission= findViewById(R.id.ll_diagnostic_transmission);
    linearLayout_ignition= findViewById(R.id.ll_diagnostic_ignition_system);
    linearLayout_air_fuel= findViewById(R.id.ll_diagnostic_air_fuel);
    linearLayout_auxillary.setOnClickListener(this);
    linearLayout_computer_output.setOnClickListener(this);
    linearLayout_speed_control.setOnClickListener(this);
    linearLayout_transmission.setOnClickListener(this);
    linearLayout_ignition.setOnClickListener(this);
    linearLayout_air_fuel.setOnClickListener(this);
    btn_scan.setOnClickListener(this);
    iv_rescan.setOnClickListener(this);

    list=new ArrayList<>();
    list_server=new ArrayList<>();
    array_error_code=new JSONArray();
    errorcode_list=new ArrayList<>();
    listAdapter=new ErrorListAdapter(list,EngineScanActivity.this);
    loadPreviousData();


  }
  private void settextBeforeScan() {
    textView_air_fuel.setText("--");
    textView_auxillary.setText("--");
    textView_computer_out.setText("--");
    textView_ignition.setText("--");
    textView_speed_control.setText("--");
    textView_transmission.setText("--");
  }

  private void resetVariable() {
    computer_output_count = 0;
    air_fuel_count = 0;
    auxillary_count = 0;
    ignition_count = 0;
    speed_control_count = 0;
    transmission_count = 0;
  }

  public void scanView() {
    linearLayout_after_scan.setVisibility(View.GONE);
    imageview_animation = view_animation;
    resetVariable();
    settextBeforeScan();



    imageview_animation.setVisibility(View.VISIBLE);
    view_animation.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        imageview_animation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        width = imageview_animation.getWidth(); //height is ready
      }
    });


    animation = new TranslateAnimation(0.0f, (float) width,
        0.0f, 0.0f);
    animation.setDuration(2000);
    animation.setRepeatCount(Animation.INFINITE);
    animation.setRepeatMode(2);
    animation.setFillAfter(true);

    imageview_animation.startAnimation(animation);

    animation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        // btn_scan.setVisibility(View.GONE);
        relativeLayout_btn_scan.setVisibility(View.GONE);
        setDotVisiblity(true);
        setImageVisiblity(false);
        setData();
        isDatashown=true;
        // Toast.makeText(ThirtySecond.this, "start", Toast.LENGTH_SHORT).show();
        // textView_Information.setText("Establishing connection..");

      }

      @Override
      public void onAnimationEnd(Animation animation) {
        // Toast.makeText(ThirtySecond.this, "end", Toast.LENGTH_SHORT).show();
        // btn_scan.setVisibility(View.VISIBLE);
//                imageview_animation.clearAnimation();
//                imageview_animation.setVisibility(View.INVISIBLE);
//                linearLayout_after_scan.setVisibility(View.VISIBLE);
//                setDotVisiblity(false);
//                setImageVisiblity(true);
//                isDatashown=false;
        // tv_last_scan_date.setText("");

      }

      @Override
      public void onAnimationRepeat(Animation animation) {
        // Toast.makeText(ThirtySecond.this, "repeat", Toast.LENGTH_SHORT).show();
        //textView_Information.setText("Collecting Data..");
      }
    });

//                else{
//                    Toast.makeText(ThirtySecond.this, "Disconnected", Toast.LENGTH_SHORT).show();
//                }
    //}
    // });

  }
  public void setDotVisiblity(boolean isVisible) {
    if (isVisible) {
      linearLayout_dot_first.setVisibility(View.VISIBLE);
      linearLayout_dot_second.setVisibility(View.VISIBLE);
      linearLayout_dot_third.setVisibility(View.VISIBLE);
      linearLayout_dot_forth.setVisibility(View.VISIBLE);
      linearLayout_dot_fifth.setVisibility(View.VISIBLE);
      linearLayout_dot_sixth.setVisibility(View.VISIBLE);
    } else {
      linearLayout_dot_first.setVisibility(View.GONE);
      linearLayout_dot_second.setVisibility(View.GONE);
      linearLayout_dot_third.setVisibility(View.GONE);
      linearLayout_dot_forth.setVisibility(View.GONE);
      linearLayout_dot_fifth.setVisibility(View.GONE);
      linearLayout_dot_sixth.setVisibility(View.GONE);
    }
  }
  public void setImageVisiblity(boolean isVisible) {
    if (isVisible) {
      imageView_air_fuel.setVisibility(View.VISIBLE);
      imageView_transmission.setVisibility(View.VISIBLE);
      imageView_auxillary.setVisibility(View.VISIBLE);
      imageView_computer_out.setVisibility(View.VISIBLE);
      imageView_ignition.setVisibility(View.VISIBLE);
      imageView_speed_control.setVisibility(View.VISIBLE);


    } else {
      imageView_air_fuel.setVisibility(View.GONE);
      imageView_auxillary.setVisibility(View.GONE);
      imageView_computer_out.setVisibility(View.GONE);
      imageView_ignition.setVisibility(View.GONE);
      imageView_speed_control.setVisibility(View.GONE);
      imageView_transmission.setVisibility(View.GONE);
    }
  }
  private void showList(){

    ErrorListDialog cdd=new ErrorListDialog(EngineScanActivity.this,list);
    cdd.show();
  }
  private void setError(char cat,String code){


    if(cat=='1') {
      if (code.charAt(2) == '1' || code.charAt(2) == '2') {
        //Log.e("Diagnostic", "setError: " + code);

        for (int i=0;i<list_server.size();i++){
          ErrorCode errorCode = list_server.get(i);
          if(errorCode.getCode().equals(code)) {
            list.add(errorCode);

            Log.e("tag", "setError: "+errorCode.getCode());

            break;
          }

        }
        array_error_code.put(code);

      }
    } else{



      if(cat==code.charAt(2)){
        for (int i=0;i<list_server.size();i++){
          ErrorCode errorCode = list_server.get(i);

          if(errorCode.getCode().equals(code)) {
            list.add(errorCode);
            Log.e("tag", "setError: "+errorCode.getCode());

            break;
          }

        }
        array_error_code.put(code);
      }
    }


  }
  public void calculateCategory(char category) {
    //Log.e("Diagnostic", "calculateCategory: " + category);
    switch (category) {

      case '1':
        air_fuel_count = air_fuel_count + 1;
        break;
      case '2':
        air_fuel_count = air_fuel_count + 1;
        break;
      case '3':
        ignition_count = ignition_count + 1;
        break;
      case '4':
        auxillary_count = auxillary_count + 1;
        break;
      case '5':
        speed_control_count = speed_control_count + 1;
        break;
      case '6':
        computer_output_count = computer_output_count + 1;
        break;
      case '7':
        transmission_count = transmission_count + 1;
        break;
    }
  }
  private void setPreviousValue(ArrayList<String> codelist){
    int length=  codelist.size();
    if(length>0) {


      for (int i = 0; i < length; i++) {
        String code = codelist.get(i);

        if (code != null && code.length() > 4) {
          calculateCategory(code.charAt(2));
        }

      }
      imageView_computer_out.setVisibility(View.VISIBLE);

      if (computer_output_count > 0) {
        String error_text = " errors";
        if (computer_output_count == 1) {
          error_text = " error";
        }
        textView_computer_out.setText(String.valueOf(computer_output_count) + error_text);
        imageView_computer_out.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

      } else {
        textView_computer_out.setText(String.valueOf(computer_output_count) + " errors");

        imageView_computer_out.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

      }
      imageView_ignition.setVisibility(View.VISIBLE);
      if (ignition_count > 0) {
        String error_text = " errors";
        if (ignition_count == 1) {
          error_text = " error";
        }
        textView_ignition.setText(String.valueOf(ignition_count) + error_text);

        imageView_ignition.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

      } else {
        textView_ignition.setText(String.valueOf(ignition_count) + " errors");

        imageView_ignition.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

      }


      imageView_auxillary.setVisibility(View.VISIBLE);

      if (auxillary_count > 0) {
        String error_text = " errors";
        if (auxillary_count == 1) {
          error_text = " error";
        }
        textView_auxillary.setText(String.valueOf(auxillary_count) + error_text);

        imageView_auxillary.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

      } else {
        textView_auxillary.setText(String.valueOf(auxillary_count) + " errors");

        imageView_auxillary.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

      }

      imageView_speed_control.setVisibility(View.VISIBLE);
      if (speed_control_count > 0) {
        String error_text = " errors";
        if (speed_control_count == 1) {
          error_text = " error";
        }
        imageView_speed_control.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

        textView_speed_control.setText(String.valueOf(speed_control_count) + error_text);

      } else {
        textView_speed_control.setText(String.valueOf(speed_control_count) + " errors");
        imageView_speed_control.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

      }


      imageView_air_fuel.setVisibility(View.VISIBLE);
      if (air_fuel_count > 0) {
        String error_text = " errors";
        if (air_fuel_count == 1) {
          error_text = " error";
        }
        imageView_air_fuel.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));
        textView_air_fuel.setText(String.valueOf(air_fuel_count) + error_text);
      } else {
        imageView_air_fuel.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

        textView_air_fuel.setText(String.valueOf(air_fuel_count) + " errors");
      }


      imageView_transmission.setVisibility(View.VISIBLE);

      if (transmission_count > 0) {
        String error_text = " errors";
        if (transmission_count == 1) {
          error_text = " error";
        }
        imageView_transmission.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

        textView_transmission.setText(String.valueOf(transmission_count) + error_text);

      } else {
        textView_transmission.setText(String.valueOf(transmission_count) + " errors");

        imageView_transmission.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));
      }
    }else{


      imageView_computer_out.setVisibility(View.VISIBLE);
      textView_computer_out.setText(String.valueOf(computer_output_count) + " errors");

      imageView_computer_out.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

      imageView_ignition.setVisibility(View.VISIBLE);

      textView_ignition.setText(String.valueOf(ignition_count) + " errors");

      imageView_ignition.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));



      imageView_auxillary.setVisibility(View.VISIBLE);


      textView_auxillary.setText(String.valueOf(auxillary_count) + " errors");

      imageView_auxillary.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));


      imageView_speed_control.setVisibility(View.VISIBLE);

      textView_speed_control.setText(String.valueOf(speed_control_count) + " errors");
      imageView_speed_control.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));




      imageView_air_fuel.setVisibility(View.VISIBLE);

      imageView_air_fuel.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

      textView_air_fuel.setText(String.valueOf(air_fuel_count) + " errors");



      imageView_transmission.setVisibility(View.VISIBLE);

      textView_transmission.setText(String.valueOf(transmission_count) + " errors");

      imageView_transmission.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));


    }

  }
  public void setData (){
    timer= new CountDownTimer(50000, 1000) {

      public void onTick(long millisUntilFinished) {
        int remain=(int)millisUntilFinished/1000;


        if(remain>0&&remain<2){
          linearLayout_dot_sixth.setVisibility(View.GONE);
          imageView_computer_out.setVisibility(View.VISIBLE);
          if(computer_output_count>0){
            String error_text=" errors";
            if(computer_output_count==1){
              error_text=" error";
            }
            textView_computer_out.setText(String.valueOf(computer_output_count)+error_text);

            imageView_computer_out.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

          }else{
            textView_computer_out.setText(String.valueOf(computer_output_count)+" errors");

            imageView_computer_out.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

          }
        }
        if(remain>2&&remain<4){
          linearLayout_dot_fifth.setVisibility(View.GONE);
          imageView_ignition.setVisibility(View.VISIBLE);
          if(ignition_count>0){
            String error_text=" errors";
            if(ignition_count==1){
              error_text=" error";
            }
            textView_ignition.setText(String.valueOf(ignition_count)+error_text);

            imageView_ignition.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

          }else{
            textView_ignition.setText(String.valueOf(ignition_count)+" errors");

            imageView_ignition.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

          }
        }
        if(remain>4&&remain<6){
          linearLayout_dot_forth.setVisibility(View.GONE);
          imageView_auxillary.setVisibility(View.VISIBLE);

          if(auxillary_count>0){
            String error_text=" errors";
            if(auxillary_count==1){
              error_text=" error";
            }
            textView_auxillary.setText(String.valueOf(auxillary_count)+error_text);

            imageView_auxillary.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

          }else{
            textView_auxillary.setText(String.valueOf(auxillary_count)+" errors");

            imageView_auxillary.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

          }
        }
        if(remain>6&&remain<8){
          linearLayout_dot_third.setVisibility(View.GONE);
          imageView_speed_control.setVisibility(View.VISIBLE);
          if(speed_control_count>0){
            String error_text=" errors";
            if(speed_control_count==1){
              error_text=" error";
            }
            imageView_speed_control.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

            textView_speed_control.setText(String.valueOf(speed_control_count)+error_text);

          }else{
            textView_speed_control.setText(String.valueOf(speed_control_count)+" errors");
            imageView_speed_control.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

          }
        }
        if(remain>8&&remain<10){
          linearLayout_dot_second.setVisibility(View.GONE);
          imageView_air_fuel.setVisibility(View.VISIBLE);
          if(air_fuel_count>0) {
            String error_text=" errors";
            if(air_fuel_count==1){
              error_text=" error";
            }
            imageView_air_fuel.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));
            textView_air_fuel.setText(String.valueOf(air_fuel_count)+error_text);
          }else{
            imageView_air_fuel.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));

            textView_air_fuel.setText(String.valueOf(air_fuel_count)+" errors");
          }
        }
        if(remain>10&&remain<12){
          linearLayout_dot_first.setVisibility(View.GONE);
          imageView_transmission.setVisibility(View.VISIBLE);

          if(transmission_count>0){
            String error_text=" errors";
            if(transmission_count==1){
              error_text=" error";
            }
            imageView_transmission.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));

            textView_transmission.setText(String.valueOf(transmission_count)+error_text);

          }else{
            textView_transmission.setText(String.valueOf(transmission_count)+" errors");

            imageView_transmission.setImageDrawable(getResources().getDrawable(R.drawable.ic_tick));


          }                }

      }

      public void onFinish() {

      }

    };
  }
  @Override
  public void onClick(View v) {


    switch (v.getId()){
      case R.id.ll_diagnostic_auxillary_emission:
        if(auxillary_count>0) {
          list.clear();
          for (int i = 0; i < list_server.size(); i++) {
            ErrorCode errorCode=list_server.get(i);
            String code = errorCode.getCode();

            if (code != null && code.length() > 4) {
              setError('4', code);
            }
          }
          showList();
        }else{
          ToastUtils.showShort("There is no error of this type");
          }
        break;
      case R.id.ll_diagnostic_air_fuel:
        break;
      case R.id.ll_diagnostic_computer_control:

        if(computer_output_count>0) {
          list.clear();
          for (int i = 0; i < list_server.size(); i++) {
            ErrorCode errorCode=list_server.get(i);
            String code = errorCode.getCode();

            if (code != null && code.length() > 4) {
              setError('6', code);
            }

          }
          showList();
          // getDetailCode(array_error_code);
        }else{
          ToastUtils.showShort("There is no error of this type");
        }
        break;
      case R.id.ll_diagnostic_ignition_system:
        if(ignition_count>0) {
          list.clear();
          for (int i = 0; i < list_server.size(); i++) {
            ErrorCode errorCode=list_server.get(i);
            String code = errorCode.getCode();

            if (code != null && code.length() > 4) {
              setError('3', code);
            }
          }
          showList();
          // getDetailCode(array_error_code);
        }else{
          ToastUtils.showShort("There is no error of this type");
        }
        break;
      case R.id.ll_diagnostic_speed_control:


        if(speed_control_count>0) {
          list.clear();
          for (int i = 0; i < list_server.size(); i++) {
            ErrorCode errorCode=list_server.get(i);
            String code = errorCode.getCode();

            if (code != null && code.length() > 4) {
              setError('5', code);
            }

          }
          showList();
          // getDetailCode(array_error_code);
        }else{
          ToastUtils.showShort("There is no error of this type");
        }
        break;
      case R.id.ll_diagnostic_transmission:
        if(transmission_count>0) {
          list.clear();
          for (int i = 0; i < list_server.size(); i++) {
            ErrorCode errorCode=list_server.get(i);
            String code = errorCode.getCode();

            if (code != null && code.length() > 4) {
              setError('7', code);
            }

          }
          showList();
        }else{
          ToastUtils.showShort("There is no error of this type");
        }
        break;
      case R.id.btn_scan:
        scanEngine();

        break;
      case R.id.iv_rescan:
        scanEngine();

        break;
      case R.id.tv_diagnostics_near_by:
        String carId=CommonPreference.getInstance().getCarId();
        ApiRequests.getInstance().get_location(GlobalReferences.getInstance().baseActivity,EngineScanActivity.this,carId);
        break;
    }

  }



  private void scanEngine(){
    new AwesomeInfoDialog(GlobalReferences.getInstance().baseActivity)
        .setTitle("Alert")
        .setMessage("Please make sure your car is stationary and ignition is turned on. If you are facing any issue with engine scan, please try restarting your car.")
        .setColoredCircle(R.color.colorAccent)
        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
        .setCancelable(true)
        .setPositiveButtonText("Scan")
        .setPositiveButtonbackgroundColor(R.color.colorAccent)
        .setPositiveButtonTextColor(R.color.white)
        .setNegativeButtonText("Cancel")
        .setNegativeButtonbackgroundColor(R.color.colorAccent)
        .setNegativeButtonTextColor(R.color.white)
        .setPositiveButtonClick(new Closure() {
          @Override
          public void exec() {
          getCarStatus();
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

  private void loadPreviousData(){
    String imei=CommonPreference.getInstance().getImeiForAPI();
    ApiRequests.getInstance().get_old_error(GlobalReferences.getInstance().baseActivity,EngineScanActivity.this,imei);

  }


  private void getCarStatus(){
    ApiRequests.getInstance().get_status(GlobalReferences.getInstance().baseActivity,EngineScanActivity.this);
  }

  private void loadCurrentError(){
    String imei=CommonPreference.getInstance().getImeiForAPI();
    ApiRequests.getInstance().get_current_error(GlobalReferences.getInstance().baseActivity,EngineScanActivity.this,imei);

  }





  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {
  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    try {
      if (listener.getTag().equals("current_error")) {
        JSONObject jsonObject = listener.getJsonResponse();
        presenter.parseCurrentData(listener.getJsonResponse());
        list_server.clear();
        errorcode_list.clear();
        JSONObject object_errorData = jsonObject.getJSONObject("msg");
        JSONArray errorArray = object_errorData.getJSONArray("error_code");
        last_update_time = object_errorData.getString("last_update_date");
        tv_coolant_temp.setText(object_errorData.getString("coolant") + " °C");
        tv_battery_voltage.setText(object_errorData.getString("voltage") + " V");
        int length = errorArray.length();
        for (int i = 0; i < length; i++) {
          JSONObject dataObject = errorArray.getJSONObject(i);
          ErrorCode data = new ErrorCode();
          errorcode_list.add(dataObject.getString("error_code"));
          data.setTitle(dataObject.getString("title"));
          data.setCode(dataObject.getString("error_code"));
          data.setDescription(dataObject.getString("description"));
          list_server.add(data);
        }
        timer.start();

        if (last_update_time.equals("null")) {
          if (!((Activity) context).isFinishing()) {
            new AwesomeErrorDialog(EngineScanActivity.this)
                .setTitle("Alert!")
                .setMessage("Unable to run diagnostics currently. Please try again later.")
                .setColoredCircle(R.color.dialogErrorBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                .setCancelable(true).setButtonText("Ok")
                .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                .setButtonText(getString(R.string.dialog_ok_button))
                .setErrorButtonClick(new Closure() {
                  @Override
                  public void exec() {
                    // click
                    imageview_animation.clearAnimation();
                    timer.cancel();
                    finish();
                  }
                })
                .show();
          }
        } else {
          tv_last_scan_date.setText(getDate(last_update_time));
          setPreviousValue(errorcode_list);
          imageview_animation.clearAnimation();
          imageview_animation.setVisibility(View.INVISIBLE);
          linearLayout_after_scan.setVisibility(View.VISIBLE);
          setDotVisiblity(false);
          setImageVisiblity(true);
          isDatashown = false;
        }
      } else if (listener.getTag().equals("previous_error")) {
        JSONObject jObject = listener.getJsonResponse();
        JSONObject carData = jObject.getJSONObject("msg");
        JSONArray jsonArray_error_code = carData.getJSONArray("error_code");
        for (int i = 0; i < jsonArray_error_code.length(); i++) {
          JSONObject jsonObject = jsonArray_error_code.getJSONObject(i);
          errorcode_list.add(jsonObject.getString("error_code"));
          ErrorCode errorCode = new ErrorCode();
          errorCode.setCode(jsonObject.getString("error_code"));
          errorCode.setDescription(jsonObject.getString("description"));
          errorCode.setTitle(jsonObject.getString("title"));
          list_server.add(errorCode);
        }
        setPreviousValue(errorcode_list);
        tv_last_scan_date.setText(getDate(carData.getString("last_update_date")));

      }else if(listener.getTag().equals("status")){
        JSONObject jObject = listener.getJsonResponse();
        JSONObject carData=jObject.getJSONObject("msg");
        JSONArray jsonArray=carData.getJSONArray("car_list");
        String imei=CommonPreference.getInstance().getImeiForAPI();

        for (int i=0;i<jsonArray.length();i++){
          JSONObject jsonObject=jsonArray.getJSONObject(i);
          String imei_current=jsonObject.getString("IMEI");
          if(imei_current.equals(imei)){
            car_status=jsonObject.getString("status");
          }

        }
        if(car_status!=null&&car_status.equals("ACTIVE")){
          scanView();

          loadCurrentError();


        }else{
          ToastUtils.showShort("Please turn on ignition of your car");
        }
      }else if(listener.getTag().equals("pitstop")){
        JSONObject jObject = listener.getJsonResponse();
        JSONObject carData = jObject.getJSONObject("msg");
        JSONArray array = carData.getJSONArray("last_location");
        double lastLatitude = array.getDouble(0);
        double lastLongitude = array.getDouble(1);
        Intent intent = new Intent(GlobalReferences.getInstance().baseActivity, PitstopActivity.class);
        intent.putExtra("latitude", lastLatitude);
        intent.putExtra("longitude", lastLongitude);
        startActivity(intent);

      }

    }catch (Exception ex){
      LogUtils.error("Engine scan",ex.getCause()+"");

    }
  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {
    LogUtils.error("Engine scan",listener.getTag());

    VolleyError error=listener.getVolleyError();

    new Handler(getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        imageview_animation.clearAnimation();
        imageview_animation.setVisibility(View.INVISIBLE);
      }
    });

    NetworkResponse response = error.networkResponse;
    if(response != null && response.data != null){
      switch(response.statusCode){
        case 400:
          //  Log.e("Error", "onErrorResponse: "+new String(response.data));
          try{
            if(!((Activity) context).isFinishing()) {
              setPreviousValue(errorcode_list);
              imageview.clearAnimation();

              JSONObject obj = new JSONObject(new String(response.data));
              String msg = obj.getString("msg");
//

              new AwesomeErrorDialog(GlobalReferences.getInstance().baseActivity)
                  .setTitle("Alert")
                  .setMessage(msg)
                  .setColoredCircle(R.color.dialogErrorBackgroundColor)
                  .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                  .setCancelable(true).setButtonText("Ok")
                  .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                  .setButtonText(getString(R.string.dialog_ok_button))
                  .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                      // click
                      finish();
                    }
                  })
                  .show();
            }
          } catch(JSONException e){
            e.printStackTrace();
          }



          break;
        case 500:
          // Log.e("Error", "onErrorResponse: "+new String(response.data));
          try{
            if(!((Activity) context).isFinishing()) {

              setPreviousValue(errorcode_list);
              imageview.clearAnimation();
              JSONObject obj = new JSONObject(new String(response.data));
              String msg = obj.getString("msg");
//
              new AwesomeErrorDialog(GlobalReferences.getInstance().baseActivity)
                  .setTitle("Alert")
                  .setMessage(msg)
                  .setColoredCircle(R.color.dialogErrorBackgroundColor)
                  .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                  .setCancelable(true).setButtonText("Ok")
                  .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                  .setButtonText(getString(R.string.dialog_ok_button))
                  .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                      // click
                    }
                  })
                  .show();
            }
          } catch(JSONException e){
            e.printStackTrace();
          }



          break;
        case 501:
          break;

      }
      //Additional cases
    }


    try {
      if (error.networkResponse.data != null) {


        if (error instanceof NetworkError) {
          LogUtils.error("Engine Scan","Network Error");



        } else if (error instanceof TimeoutError) {
          LogUtils.error("Engine Scan","Timeout Error");




        }


      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
      LogUtils.error("Engine",listener.getTag());
  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void setUpdateTime(String time) {

  }

  @Override
  public void setCoolant(String coolant) {

  }

  @Override
  public void setVoltage(String voltage) {

  }

  @Override
  public void setErrorCode(ArrayList<String> error) {

  }

  @Override
  public void setListData(ArrayList<ErrorCode> data) {

  }
  @Override
  protected void onResume() {
    super.onResume();
    view_animation.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        view_animation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        width=  view_animation.getWidth(); //height is ready
      }
    });

  }
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();

  }



}
