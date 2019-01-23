package blackflame.com.zymepro.ui.alertdetails;

import static blackflame.com.zymepro.util.UtilityMethod.getDate;
import static blackflame.com.zymepro.util.UtilityMethod.getTime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.alerts.model.Alert;
import blackflame.com.zymepro.util.GMapUtil;
import blackflame.com.zymepro.util.UtilityMethod;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import org.json.JSONObject;

public class AlertDetails extends BaseActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback,AppRequest,DetailsPresenter.View {
  private GoogleMap Gmap;
  private TextView tv_address;
  private String title;
  ArrayList<Alert> list_fullData;
  private double latitude,longitude;
  private String alertName_title;
  private String result;
  boolean isFromNotification=false;
  String date;
  DetailsPresenter presenter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_alert_details);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new DetailsPresenter(this);

    initViews();
  }


  private void initViews(){
    Toolbar toolbar= findViewById(R.id.toolbar_common);
    TextView   tv_title = findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,tv_title,"");

    TextView  textView_alertDeatils= findViewById(R.id.alertDetails);
    tv_address=findViewById(R.id.tv_address);

    try {
      Bundle bundle=getIntent().getBundleExtra("bundle");
      if (bundle != null){
        int count =1;
        isFromNotification=true;
        String time=bundle.getString("date");
        title=bundle.getString("alert_name");
        alertName_title=getTitle(title);
        latitude=bundle.getDouble("lat");
        longitude=bundle.getDouble("lng");
        Log.e("Lat", "onCreate: "+latitude+","+longitude);
        date=getTime(time);
        setData(latitude,longitude,date);
        if(count==1){
          textView_alertDeatils.setText(String.valueOf(count)+" "+alertName_title+" "+"instance on "+UtilityMethod.getDatewithMonth(time));
        }else if(count>1){
          textView_alertDeatils.setText(String.valueOf(count)+" "+alertName_title+" "+"instances on "+UtilityMethod.getDatewithMonth(time));
        }

      }else{
        title=getIntent().getStringExtra("title");
        isFromNotification=false;

        alertName_title=getTitle(title);
        list_fullData = (ArrayList<Alert>) getIntent().getSerializableExtra("datalist");


        int count=getIntent().getIntExtra("count",0);
        String time=getDate(list_fullData.get(0).getTime());
        if(count==1){

          textView_alertDeatils.setText(String.valueOf(count)+" "+alertName_title+" "+"instance on "+time);
        }else if(count>1){
          textView_alertDeatils.setText(String.valueOf(count)+" "+alertName_title+" "+"instances on "+time);

        }

      }

      tv_title.setText(alertName_title);


    }catch (ClassCastException e){

    }




    setUpMapIfNeeded();
  }

  private String getTitle(String title){
    switch (title){
      case "GEOFENCE":
        alertName_title="Geofence Alert";
        break;
      case "THEFT":
        alertName_title="Theft Alert";
        break;
      case "PULL_OUT_REMINDER":
        alertName_title="Unplug Alert";
        break;
      case "OVERSPEED":
        alertName_title="Over Speeding";
        break;
      case "TOWING_ALARM":
        alertName_title="Towing Alert";
        break;
      case "LOW_BATTERY":
        alertName_title="Low Battery";
        break;
      case "LONG_IDLING":
        alertName_title="Long Idling";
        break;
      case "HIGH_COOLANT":
        alertName_title="High Coolant";
        break;
      case "COLLISION_ALARM":
        alertName_title="Collision";
        break;
      case "SUDDEN_ACCELERATION":
        alertName_title="Hard Acceleration";
        break;

      case "SUDDEN_LANE_CHANGE":
        alertName_title="Sudden Lane Change";
        break;

      case "SUDDEN_BRAKING":
        alertName_title="Sudden Braking";
        break;
      case "SHARP_TURN":
        alertName_title="Sharp Turn";
        break;


    }

    return alertName_title;

  }



  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (Gmap == null) {
      // Try to obtain the map from the SupportMapFragment.
      SupportMapFragment fm = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview));
      // Check if we were successful in obtaining the map.
      fm.getMapAsync(AlertDetails.this);

    }
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    if(marker.isInfoWindowShown()){
      marker.hideInfoWindow();
    }
    int position = Integer.valueOf((String)marker.getTag());


    marker.showInfoWindow();
    loadAddress(marker.getPosition().latitude,marker.getPosition().longitude);

    setCurrentLocation(marker.getPosition());
    return true;
  }

  private void loadAddress(double latitude,double longitude){
    ApiRequests.getInstance().get_address(GlobalReferences.getInstance().baseActivity,AlertDetails.this,latitude,longitude);
  }

  private void setCurrentLocation(LatLng latlng){


    Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));

    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(latlng)      // Sets the center of the map to location user
        .zoom(17)                   // Sets the zoom
        .bearing(90)                // Sets the orientation of the camera to east
        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
    Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }




  @Override
  public void onMapReady(GoogleMap googleMap) {
    Gmap = googleMap;

    if (isFromNotification){
      setData(latitude,longitude,date);
      isFromNotification=false;

    }else{
      setListData();
    }
    Gmap.setOnMarkerClickListener(this);
    Gmap.getUiSettings().setZoomControlsEnabled(true);

    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(new LatLng(26.5937, 78.9629))
        .zoom(4)
        .bearing(0)
        .build();
    Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    GMapUtil.setMapStyle(Gmap);


  }

  private void setData(double latitude,double longitude,String time){
    if (Gmap !=null) {
      Gmap.addMarker(new MarkerOptions()
          .position(new LatLng(latitude, longitude))
          .title(alertName_title + "\n" + "@ " + "\n" + time)
          .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(100, 100))))
          .setTag(String.valueOf(1));

      Gmap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

        @Override
        public View getInfoWindow(Marker arg0) {
          return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

          final Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

          LinearLayout info = new LinearLayout(context);
          info.setOrientation(LinearLayout.VERTICAL);

          TextView title = new TextView(context);
          title.setTextColor(Color.BLACK);
          title.setGravity(Gravity.CENTER);
          title.setTypeface(null, Typeface.BOLD);
          title.setText(marker.getTitle());

          TextView snippet = new TextView(context);
          snippet.setTextColor(Color.GRAY);
          snippet.setText(marker.getSnippet());
          snippet.setTextIsSelectable(true);
//
          info.addView(title);
          info.addView(snippet);
          // info.addView(imageView);

          return info;
        }
      });
    }
  }

  private Bitmap resizeMapIcons(int width, int height){
    Bitmap imageBitmap = BitmapFactory
        .decodeResource(getResources(),getResources().getIdentifier("marker_alert", "drawable", getPackageName()));
    return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
  }


  private void setListData(){
    int length=list_fullData.size();
    if(Gmap!=null) {
      Gmap.clear();
    }
    for (int i=0;i<length;i++){

      Alert declare=list_fullData.get(i);
      String time=getTime(declare.getTime());
      String indivdual_alert_name = declare.getAlertName();
      if(title!=null&&title.equals(indivdual_alert_name)) {
        latitude=declare.getLatitude();
        longitude=declare.getLongitude();
        Gmap.addMarker(new MarkerOptions()
            .position(new LatLng(latitude, longitude))
            .title(alertName_title + "\n" + "@ " + "\n" + time)
            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(100, 100))))
            .setTag(String.valueOf(i));
        Gmap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

          @Override
          public View getInfoWindow(Marker arg0) {
            return null;
          }

          @Override
          public View getInfoContents(Marker marker) {

            final Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

            LinearLayout info = new LinearLayout(context);
            info.setOrientation(LinearLayout.VERTICAL);

            TextView title = new TextView(context);
            title.setTextColor(Color.BLACK);
            title.setGravity(Gravity.CENTER);
            title.setTypeface(null, Typeface.BOLD);
            title.setText(marker.getTitle());

            TextView snippet = new TextView(context);
            snippet.setTextColor(Color.GRAY);
            snippet.setText(marker.getSnippet());
            snippet.setTextIsSelectable(true);
//
            info.addView(title);
            info.addView(snippet);
            // info.addView(imageView);


            return info;
          }
        });

      }else if(title.equals("PULL_OUT_REMINDER")&&indivdual_alert_name.equals("UNPLUG")){
        latitude=declare.getLatitude();
        longitude=declare.getLongitude();
        Gmap.addMarker(new MarkerOptions()
            .position(new LatLng(latitude, longitude))
            .title(alertName_title + "\n" + "@ " + "\n" + time)
            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons( 100, 100))))
            .setTag(String.valueOf(i));

        Gmap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

          @Override
          public View getInfoWindow(Marker arg0) {
            return null;
          }

          @Override
          public View getInfoContents(Marker marker) {

            final Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

            LinearLayout info = new LinearLayout(context);
            info.setOrientation(LinearLayout.VERTICAL);

            TextView title = new TextView(context);
            title.setTextColor(Color.BLACK);
            title.setGravity(Gravity.CENTER);
            title.setTypeface(null, Typeface.BOLD);
            title.setText(marker.getTitle());

            TextView snippet = new TextView(context);
            snippet.setTextColor(Color.GRAY);
            snippet.setText(marker.getSnippet());
            snippet.setTextIsSelectable(true);
//

            info.addView(title);
            info.addView(snippet);
            // info.addView(imageView);


            return info;
          }
        });

      }


    }
    Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10.0f));
  }


  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
      presenter.parseAddress(listener.getJsonResponse());
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
  public void updateAddress(String address) {
    tv_address.setText(address);

  }
}
