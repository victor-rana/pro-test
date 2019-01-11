package blackflame.com.zymepro.ui.profile;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.carregistration.CarRegistration;
import blackflame.com.zymepro.ui.profile.adapter.ProfileRecyclerAdapter;
import blackflame.com.zymepro.ui.profile.listener.ProfileCardClickListener;
import blackflame.com.zymepro.ui.profile.model.ProfileModel;
import blackflame.com.zymepro.ui.profile.update.UpdateProfile;
import blackflame.com.zymepro.util.LogUtils;
import blackflame.com.zymepro.util.NetworkUtils;

import blackflame.com.zymepro.util.ToastUtils;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.JSONObject;

public class ActivityProfile extends BaseActivity implements ProfileCardClickListener,View.OnClickListener,AppRequest ,ProfilePresenter.View{
  Toolbar toolbar_Profile;
  TextView textView_Title;
  RecyclerView listView_car;
  ArrayList<ProfileModel> list_cardata;
  private static final int RESULT_PICK_CONTACT = 2;
  AlertDialog.Builder alertDialog;
  TextView tv_name,tv_email,tv_mobile, tv_carRegistration;
  String name,mobileNumber,email,carBrand,carModel,registration;
  Context context;
  ProfileRecyclerAdapter profileRecyclerAdapter;
  SimpleDateFormat simpleDateFormat;
  ProfilePresenter presenter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
    presenter=new ProfilePresenter(this);
  }

  private void initViews() {
    toolbar_Profile = findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar_Profile,title,"Profile");
    tv_name = findViewById(R.id.profilecomname);
    tv_email = findViewById(R.id.profileemail);
    tv_mobile = findViewById(R.id.profilemobile);
    tv_carRegistration = findViewById(R.id.registration);
    listView_car = findViewById(R.id.list_profile_car);
    listView_car.setItemAnimator(new DefaultItemAnimator());
    listView_car.setLayoutManager(new LinearLayoutManager(ActivityProfile.this, LinearLayoutManager.HORIZONTAL, false));
    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    list_cardata = new ArrayList<>();
    profileRecyclerAdapter = new ProfileRecyclerAdapter(ActivityProfile.this, list_cardata,
        ActivityProfile.this);
    listView_car.setAdapter(profileRecyclerAdapter);

    ImageView imageView_edit_profile = findViewById(R.id.iv_edit_profile);
    imageView_edit_profile.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isNetworkAvailable = NetworkUtils.isConnected();

        if (tv_name.getText().toString().equals("--") || !isNetworkAvailable) {
          ToastUtils.showShort("Please check your internet connection");
        } else {
//          Intent intent=new Intent(Profile.this,UpdateUserProfile.class);
//          intent.putExtra("name",tv_name.getText().toString());
//          intent.putExtra("mobile",tv_mobile.getText().toString());
//          startActivity(intent);

        }

      }
    });
    loadProfile();
  }
  private void loadProfile(){
    ApiRequests.getInstance().get_profile(GlobalReferences.getInstance().baseActivity,ActivityProfile.this);
  }
  @Override
  public void onEditClickCallback(int position) {

    final ProfileModel model1=list_cardata.get(position);
    new AwesomeInfoDialog(this)
        .setTitle(model1.getRegistration_number())
        .setMessage("Device ID - "+model1.getImei()+"\nis linked with this car")
        .setColoredCircle(R.color.colorAccent)
        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
        .setCancelable(true)
        .setPositiveButtonText("Edit")
        .setPositiveButtonbackgroundColor(R.color.colorAccent)
        .setPositiveButtonTextColor(R.color.white)
        .setNegativeButtonText(getString(R.string.dialog_no_button))
        .setNegativeButtonbackgroundColor(R.color.colorAccent)
        .setNegativeButtonTextColor(R.color.white)
        .setPositiveButtonClick(new Closure() {
          @Override
          public void exec() {
            //click
            Intent intent=  new Intent(ActivityProfile.this,UpdateProfile.class);
            intent.putExtra("name",model1.getName());
            intent.putExtra("registration_number",model1.getRegistration_number());
            intent.putExtra("model",model1.getModel());
            intent.putExtra("brand",model1.getBrand());
            intent.putExtra("cc",model1.getEngineCc());
            intent.putExtra("state",model1.getState());
            intent.putExtra("fueltype",model1.getEngine_type());
            intent.putExtra("nickname",model1.getNickName());
            intent.putExtra("car_id",model1.getCar_id());
            intent.putExtra("imei",model1.getImei());
            startActivity(intent);
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
  public void onSubscriptionCallback(int position, int days) {
    if (days> 120){
      int left_days=days-120;
      Toast.makeText(context, "You can renew subscription within 3 month of expiration ", Toast.LENGTH_SHORT).show();
    } else if(days<=-30){
      new AwesomeInfoDialog(this)
          .setTitle("Alert")
          .setMessage("Your subscription has expired before 1 month, if you want to continue to use Zymepro write to us.")
          .setColoredCircle(R.color.colorAccent)
          .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
          .setCancelable(true)
          .setPositiveButtonText("Mail us")
          .setPositiveButtonbackgroundColor(R.color.colorAccent)
          .setPositiveButtonTextColor(R.color.white)
          .setNegativeButtonText("I want to renew later")
          .setNegativeButtonbackgroundColor(R.color.colorAccent)
          .setNegativeButtonTextColor(R.color.white)
          .setPositiveButtonClick(new Closure() {
            @Override
            public void exec() {
              Intent i = new Intent(Intent.ACTION_SEND);
              i.setType("message/rfc822");
              i.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@getzyme.com"});
              try {
                startActivity(Intent.createChooser(i, "Send mail..."));

              } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ActivityProfile.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
              }
            }
          })

          .setNegativeButtonClick(new Closure() {
            @Override
            public void exec() {
              //click
            }
          })
          .show();

    }else {
      ProfileModel model = list_cardata.get(position);
//      Intent intent = new Intent(ActivityProfile.this, PaymentActivity.class);
//      intent.putExtra("name", tv_name.getText().toString().trim());
//      intent.putExtra("email", email);
//      intent.putExtra("mobile", mobileNumber);
//      intent.putExtra("imei", model.getImei());
//      startActivity(intent);
      finish();

    }
  }

  @Override
  public void onAddcarCallback(int position) {
    startActivity(new Intent(ActivityProfile.this,CarRegistration.class));

  }

  @Override
  public void onClick(View v) {

  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    LogUtils.error("Profile",listener.getJsonResponse().toString());
      JSONObject data=listener.getJsonResponse();
      presenter.parseData(data);
  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {
  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public void onResponse(JSONObject response) {

  }
  @Override
  public void setEmail(String email) {
    this.email=email;
    tv_email.setText(email);
  }

  @Override
  public void setName(String name) {
    this.name=name;
    tv_name.setText(name);

  }
  @Override
  public void setMobile(String mobile) {
    this.mobileNumber=mobile;
    tv_mobile.setText(mobile);

  }

  @Override
  public void setData(ArrayList<ProfileModel> data) {

    list_cardata.clear();
    list_cardata.addAll(data);
    profileRecyclerAdapter.notifyDataSetChanged();

  }
}
