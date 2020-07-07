package blackflame.com.zymepro.ui.pitstop;

import static blackflame.com.zymepro.util.UtilityMethod.resizeMapIcons;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import blackflame.com.zymepro.ui.pitstop.model.NearByData;
import blackflame.com.zymepro.ui.pitstop.sheet.NearbyDialogHelper;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.GMapUtil;
import blackflame.com.zymepro.util.LogUtils;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import org.json.JSONObject;

public class PitstopActivity extends BaseActivity implements OnClickListener,OnMapReadyCallback,OnMarkerClickListener,AppRequest,PitstopPresenter.View {
  Toolbar toolbar_pitstop;
  TextView Toolbar_Title,textview_model;
  GoogleMap Gmap;
  GoogleApiClient mGoogleApiClient;
  LocationManager manager;
  String Name;
  String MobileNo1, MobileNo2;
  final int REQ_PERMISSION = 2;
  final int REQ_CALL = 3;
  Location mlastlocation;
  double latitude_mechanic, longitude_mechanic;
  LatLng latlng_locateOnmap;
  String tag_json_arry = "json_array_req";
  double latitude;
  double longitude;
  LatLngBounds.Builder builder;
  String number, callPhone;
  Context context;
  boolean isPriceAvailable=false;
  String fuel_type,fuel_price,selected_state;
  //private String GOOGLE_BROWSER_API_KEY=BuildConfig.PLACES_KEY;
  private String type;
  LinearLayout mechanic,parking,car_service,hotel,atm,store,food,gas_station,hospital,police_station,laundry;
  ArrayList<NearByData> data_list;
  Animation animation_in,animation_out;
  FrameLayout progressBarHolder;
  AlphaAnimation inAnimation;
  AlphaAnimation outAnimation;

PitstopPresenter presenter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pitstop);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new PitstopPresenter(this);
    initViews();
  }
  private void initViews(){
    toolbar_pitstop = findViewById(R.id.toolbar_common);
   TextView title = findViewById(R.id.toolbar_title_common);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar_pitstop,title,"Pitstop");
    CommonPreference.initializeInstance(GlobalReferences.getInstance().baseActivity);
    ImageView price= findViewById(R.id.toolbar_price);
    food=findViewById(R.id.btn_food);
    parking=findViewById(R.id.btn_parking);
    car_service=findViewById(R.id.btn_car_service);
    hotel=findViewById(R.id.btn_hotel);
    store=findViewById(R.id.btn_store);
    atm=findViewById(R.id.btn_bank);
    mechanic=findViewById(R.id.btn_mechanic);
    gas_station=findViewById(R.id.btn_fuel_station);
    hospital=findViewById(R.id.btn_hospital);
    police_station=findViewById(R.id.btn_police_station);
    laundry=findViewById(R.id.btn_laundry);
    progressBarHolder = findViewById(R.id.progressBarHolder);
    food.setOnClickListener(this);
    parking.setOnClickListener(this);
    car_service.setOnClickListener(this);
    hotel.setOnClickListener(this);
    store.setOnClickListener(this);
    atm.setOnClickListener(this);
    mechanic.setOnClickListener(this);
    gas_station.setOnClickListener(this);
    hospital.setOnClickListener(this);
    police_station.setOnClickListener(this);
    laundry.setOnClickListener(this);
    builder = new LatLngBounds.Builder();
    data_list=new ArrayList<>();
    price.setOnClickListener(this);

    animation_out = AnimationUtils.loadAnimation(getApplicationContext(),
        R.anim.slide_down);

    animation_in = AnimationUtils.loadAnimation(getApplicationContext(),
        R.anim.slide_up);
    setUpMapIfNeeded();
    type="car_repair";
    Intent intent = getIntent();
    latitude = intent.getDoubleExtra("latitude", 0.0);
    longitude = intent.getDoubleExtra("longitude", 0.0);

    getFuelPrice();
  }


  private void getFuelPrice(){
    String carId=CommonPreference.getInstance().getCarId();
    ApiRequests.getInstance().get_fuel_price(GlobalReferences.getInstance().baseActivity,PitstopActivity.this,carId);
  }



  private void loadNearBy(String type){
    ApiRequests.getInstance().get_nearby_data(GlobalReferences.getInstance().baseActivity,PitstopActivity.this,type,latitude,longitude);
  }

  private void setSelected(int id){
    switch (id){
      case R.id.btn_food:
        food.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        break;
      case R.id.btn_parking:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        break;
      case R.id.btn_car_service:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        break;
      case R.id.btn_hotel:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        break;
      case R.id.btn_store:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        break;
      case R.id.btn_bank:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        break;
      case R.id.btn_mechanic:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);

        break;
      case R.id.btn_fuel_station:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        break;
      case R.id.btn_police_station:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        break;
      case R.id.btn_hospital:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        break;
      case R.id.btn_laundry:
        food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        mechanic.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
        laundry.setBackgroundResource(R.drawable.drawable_pitstop_selected);
        break;



    }
  }
  private void showProgress(){
    inAnimation = new AlphaAnimation(0f, 1f);
    inAnimation.setDuration(200);
    progressBarHolder.setAnimation(inAnimation);
    progressBarHolder.setAnimation(inAnimation);
    progressBarHolder.setVisibility(View.VISIBLE);
  }
  @Override
  public void onClick(View v) {
    int id=v.getId();

    switch (id){
      case R.id.btn_food:
        setSelected(R.id.btn_food);
        loadNearBy("restaurant");

        break;
      case R.id.btn_parking:
        setSelected(R.id.btn_parking);
        loadNearBy("parking");

        break;
      case R.id.btn_car_service:
        setSelected(R.id.btn_car_service);
        loadNearBy("car_wash");

        break;
      case R.id.btn_hotel:
        setSelected(R.id.btn_hotel);
        loadNearBy("sharaton");

        break;
      case R.id.btn_store:
        setSelected(R.id.btn_store);
        loadNearBy("shopping_mall");
        break;
      case R.id.btn_bank:
        setSelected(R.id.btn_bank);
        loadNearBy("atm");
        break;
      case R.id.btn_mechanic:
        setSelected(R.id.btn_mechanic);
        loadNearBy("car_repair");

        break;
      case R.id.btn_fuel_station:
        setSelected(R.id.btn_fuel_station);
        loadNearBy("gas_station");
        break;
      case R.id.btn_police_station:
        setSelected(R.id.btn_police_station);
        loadNearBy("police");
        break;
      case R.id.btn_hospital:
        setSelected(R.id.btn_hospital);
        loadNearBy("hospital");

        break;
      case R.id.btn_laundry:
        setSelected(R.id.btn_laundry);
        loadNearBy("laundry");
        break;
      case R.id.toolbar_price:

        if (isPriceAvailable){
          if (fuel_type!=null && fuel_type.toLowerCase().equals("petrol")){
            fuel_type="Petrol";

          }else if (fuel_type !=null && fuel_type.toLowerCase().equals("diesel")){
            fuel_type="Diesel";
          }

          new AwesomeInfoDialog(GlobalReferences.getInstance().baseActivity)
              .setTitle("Fuel Price")
              .setMessage("State selected - "+selected_state+"\n"+"Fuel type - "+fuel_type+"\n Fuel price - ₹ "+fuel_price + "/ lt *")
              .setColoredCircle(R.color.colorAccent)
              .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
              .setCancelable(true)
              .setPositiveButtonText("Ok")

              .setPositiveButtonbackgroundColor(R.color.colorAccent)
              .setPositiveButtonTextColor(R.color.white)
              .setPositiveButtonClick(new Closure() {
                @Override
                public void exec() {
                }
              })
              .show();



        }
        break;


    }
  }


  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (Gmap == null) {
      // Try to obtain the map from the SupportMapFragment.
      SupportMapFragment fm = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview));
      // Check if we were successful in obtaining the map.
      fm.getMapAsync(PitstopActivity.this);

    }
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    Gmap = googleMap;
    Gmap.setOnMarkerClickListener(this);
    Gmap.getUiSettings().setZoomControlsEnabled(true);
    Gmap.getUiSettings().setCompassEnabled(false);
      if (Gmap != null) {
        GMapUtil.setMapStyle(Gmap);
      }
    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(new LatLng(26.5937, 78.9629))
        .zoom(6)
        .bearing(0)
        .build();
    Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    int position = Integer.valueOf((String)marker.getTag());
    NearByData data=data_list.get(position);
    Bundle bundle=new Bundle();
    bundle.putString("address",data.getAddress());
    bundle.putString("image",data.getImage_reference());
    bundle.putString("name",data.getName());
    bundle.putBoolean("isopen",data.isIsopen());
    bundle.putString("rating",String.valueOf(data.getRating()));
    bundle.putDouble("lat",marker.getPosition().latitude);
    bundle.putDouble("lng",marker.getPosition().longitude);
    NearbyDialogHelper.openDialog(PitstopActivity.this,bundle);
    return true;
  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {
    try{
      showProgress();

    }catch (Exception ex){

    }
  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
      try{
        if (progressBarHolder!=null) {
          progressBarHolder.setAnimation(outAnimation);
          progressBarHolder.setVisibility(View.GONE);
        }



        if (listener.getTag().equals("near_by")){
          if (Gmap !=null){
            Gmap.clear();
          }

          presenter.parseData(listener.getJsonResponse());
        }else if (listener.getTag().equals("price")){
          JSONObject response=listener.getJsonResponse();
          String status = response.getString("status");
          if (status.equals("SUCCESS")){
            isPriceAvailable=true;
            fuel_type=response.getJSONObject("msg").getString("fuel_type");
            fuel_price=response.getJSONObject("msg").getString("price");
            selected_state=ActivityConstants.getDisplayText(response.getJSONObject("msg").getString("state"));
            LogUtils.error("selected state",selected_state);

          }


  }










      }catch (Exception ex){

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
    Analytics.index(PitstopActivity.this,"PitstopActivity");
  }

  @Override
  public void setMarker(NearByData data,int i) {
    if (Gmap != null) {
        builder.include(data.getLocation());
        Gmap.addMarker(new MarkerOptions()
            .position(data.getLocation())
            .title(data.getName())
            .icon(BitmapDescriptorFactory
                .fromBitmap(resizeMapIcons("pitstop_marker_fuel_station", 64, 64))))
            .setTag(String.valueOf(i));
      }
    }


  @Override
  public void setList(ArrayList<NearByData> list) {
    if (data_list !=null){
      data_list.clear();
      data_list.addAll(list);
      LogUtils.error("length",""+list.size());
      if (data_list.size() > 0) {
        LatLngBounds bounds = builder.build();
        Gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
      }
    }

  }

  @Override
  public void setNoResult(String msg) {

  }

  @Override
  public void otherError(String msg) {


  }


  @Override
  protected void onResume() {
    super.onResume();

    showProgress();

    loadNearBy("car_repair");

    food.setBackgroundResource(R.drawable.drawable_circle_pitstop);
    parking.setBackgroundResource(R.drawable.drawable_circle_pitstop);
    car_service.setBackgroundResource(R.drawable.drawable_circle_pitstop);
    hotel.setBackgroundResource(R.drawable.drawable_circle_pitstop);
    store.setBackgroundResource(R.drawable.drawable_circle_pitstop);
    atm.setBackgroundResource(R.drawable.drawable_circle_pitstop);
    mechanic.setBackgroundResource(R.drawable.drawable_pitstop_selected);
    gas_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
    hospital.setBackgroundResource(R.drawable.drawable_circle_pitstop);
    police_station.setBackgroundResource(R.drawable.drawable_circle_pitstop);
    laundry.setBackgroundResource(R.drawable.drawable_circle_pitstop);
  }
}
