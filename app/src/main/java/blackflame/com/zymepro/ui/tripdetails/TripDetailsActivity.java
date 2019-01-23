package blackflame.com.zymepro.ui.tripdetails;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.tripdetails.graphview.GraphFragment;
import blackflame.com.zymepro.ui.tripdetails.mapview.TripPathFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

public class TripDetailsActivity extends BaseActivity implements DetailsPresenter.View ,AppRequest {
  TextView trip_total_time, trip_distance, trip_average_speed, trip_fuel_cost,
      textView_startAddress,textView_endAddress,tv_max_speed,tv_max_rpm,tv_idle_time,tv_alerts;
  String run_Time,  start_time,tripDate;
  long finalTime;
  int tripId;
  int time_consumed;
  Date date;
  long time;
  String ist_time;
  public   String startAddress,endAddress;
  TripPathFragment tripPathFragment;
  GraphFragment graphFragment;
  ImageView iv_play;
  DetailsPresenter presenter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trip_details);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new DetailsPresenter(this);
    initViews();
  }
  private void initViews(){
    trip_total_time = findViewById(R.id.trip_time);
    trip_distance = findViewById(R.id.distance);
    trip_average_speed = findViewById(R.id.avg_speed);
    trip_fuel_cost = findViewById(R.id.fuel_cost);
    textView_endAddress= findViewById(R.id.endaddress);
    textView_startAddress= findViewById(R.id.startaddress);
    tv_max_rpm= findViewById(R.id.maxrpm);
    tv_max_speed= findViewById(R.id.maxspeed);
    tv_idle_time= findViewById(R.id.idletime);
    tv_alerts= findViewById(R.id.alerts);
    iv_play=findViewById(R.id.iv_replay);
    Toolbar toolbar= findViewById(R.id.toolbar_common);
    TextView title= findViewById(R.id.toolbar_title_common);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Trip Details");
    Bundle bundle=getIntent().getBundleExtra("bundle");
    if(bundle !=null){
      tripId = Integer.valueOf(bundle.getString("trip_id"));
      tripDate=bundle.getString("date");
      startAddress=bundle.getString("start_address");
      endAddress=bundle.getString("end_address");
      time_consumed=Integer.valueOf(bundle.getString("trip_time"));
    }else{

      tripId = getIntent().getIntExtra("trip_id",-1);
      tripDate=getIntent().getStringExtra("date");
      startAddress=getIntent().getStringExtra("startAddress");
      endAddress=getIntent().getStringExtra("endAddress");
      time_consumed=getIntent().getIntExtra("time_consumed",0);
    }

    textView_startAddress.setText("From: "+startAddress);
    textView_endAddress.setText("To: "+endAddress);
    String date_trip=tripDate.replace("T"," ");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      date = sdf.parse(date_trip);
    }catch (ParseException e){
      e.printStackTrace();
    }
    time = date.getTime();
    ist_time=new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.SSSZZZ").format(date.getTime());
    ViewPager viewPager = findViewById(R.id.viewpager);
    setupViewPager(viewPager);
    TabLayout tabLayout = findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
    if(run_Time!=null) {
      finalTime = Long.parseLong(run_Time) - Long.parseLong(start_time);
    }
    loadTrip(ist_time);

  }
  private void setupViewPager(ViewPager viewPager) {
    TripDetailsPagerAdapter adapter = new TripDetailsPagerAdapter(getSupportFragmentManager());
    tripPathFragment=new TripPathFragment();
    Bundle bundle=new Bundle();
    bundle.putString("startAddress",startAddress);
    bundle.putString("endAddress",endAddress);
    bundle.putString("time",ist_time);
    bundle.putInt("trip_id",tripId);
    tripPathFragment.setArguments(bundle);
    graphFragment=new GraphFragment();
    tripPathFragment.setArguments(bundle);
    graphFragment.setArguments(bundle);
    adapter.addFragment(tripPathFragment, "Map View");
    adapter.addFragment(graphFragment, "Graph View");
    viewPager.setAdapter(adapter);
  }
  private void loadTrip(String time){
    String carId=CommonPreference.getInstance().getCarId();
    ApiRequests.getInstance().get_trip_details(GlobalReferences.getInstance().baseActivity,TripDetailsActivity.this,carId,time,tripId);
  }
  @Override
  public void setData() {

  }

  @Override
  public void setMaxRpm(String rpm) {
    tv_max_rpm.setText(rpm);

  }

  @Override
  public void setIdleTime(String idleTime) {
    tv_idle_time.setText(idleTime);

  }

  @Override
  public void setDistance(String distance) {
    trip_distance.setText(distance);

  }

  @Override
  public void setAlerts(String count) {
    tv_alerts.setText(count);

  }

  @Override
  public void setFuelcost(String fuelcost) {
    trip_fuel_cost.setText(fuelcost);

  }

  @Override
  public void setTripTime(String time) {
    trip_total_time.setText(time);

  }

  @Override
  public void setMaxSpeed(String speed) {
    tv_max_speed.setText(speed);

  }
  @Override
  public void setAverageSpeed(String avg_speed) {
    trip_average_speed.setText(avg_speed);
  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {


  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    JSONObject data=listener.getJsonResponse();
    presenter.parseData(data);
    if(tripPathFragment!=null){
      tripPathFragment.dataListener(data);
      graphFragment.dataListener(data);
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

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public void onResponse(JSONObject response) {

  }
}
