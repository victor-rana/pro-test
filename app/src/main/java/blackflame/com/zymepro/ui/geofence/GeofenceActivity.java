package blackflame.com.zymepro.ui.geofence;

import static blackflame.com.zymepro.util.GMapUtil.getZoomLevel;
import static blackflame.com.zymepro.util.UtilityMethod.resizeMapIcons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.PermissionConstants;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.PermissionUtils;
import blackflame.com.zymepro.util.ToastUtils;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeofenceActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
    com.google.android.gms.location.LocationListener,SeekBar.OnSeekBarChangeListener,
    AdapterView.OnItemSelectedListener, ResultCallback<Status>, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener,PermissionUtils.SimpleCallback ,AppRequest {

  int radius;
  LocationRequest mLocationRequest;
  GoogleApiClient mGoogleApiClient;
  GoogleMap Gmap;
  SupportMapFragment fm;
  double latitude, longitude;
  LatLng latLng,latLng_geofenceset;
  boolean isMapReady = false;
  Circle circle;
  Marker marker;
  TextView textView_radius;
  String latitude_set;
  String lonitude_set;
  String email,imei,carId;
  Context context;
  boolean isMarkerCreated = false,isCircleCreated=false;
  String result;
  TextView textView_address, textView_title_data;
  boolean isGeofenceSet=false;
  TextView textView_saveGeofence;
  ImageView imageView_edit;//,imageView_plus,imageView_minus;
  boolean isEditClick=false;
  SeekBar seekbar_radius;
  int radius_common=50;
  boolean isActivated,isGeofenceSend;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_geofence);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
  }


  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_geofence);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Geofence");
    textView_address = findViewById(R.id.activity_geofence_address_data);
    textView_title_data = findViewById(R.id.activity_geofence_title_data);
    textView_saveGeofence= findViewById(R.id.textview_geofencesave);
    imageView_edit= findViewById(R.id.imageview_geofenceedit);
    textView_title_data= findViewById(R.id.activity_geofence_title_data);
    seekbar_radius= findViewById(R.id.seekbar_radius);
    seekbar_radius.setOnSeekBarChangeListener(this);
    textView_radius = findViewById(R.id.geofence_radius);
    if (PermissionUtils.isGranted(PermissionConstants.LOCATION)) {
      if (Gmap != null) {
        Gmap.setMyLocationEnabled(true);
      }
    } else {
      PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.LOCATION));
      PermissionUtils.sInstance.callback(this);
      PermissionUtils.sInstance.request();
    }
    setUpMapIfNeeded();
    buildGoogleApiClient();
    isActivated=CommonPreference.getInstance().getDeviceActivated();

    if(NetworkUtils.isConnected()) {
      if (CommonPreference.getInstance().isGeofenceSet()) {
        textView_title_data.setText("Geofence set at");

        getGeofence();
      } else {
        textView_title_data.setText("Geofence not set");
      }
    }else{
      Toast.makeText(this, "No internet.", Toast.LENGTH_SHORT).show();
    }
      radius = CommonPreference.getInstance().getGeonceRadius();
      latitude_set = CommonPreference.getInstance().getGeofenceLatitude();
      lonitude_set =CommonPreference.getInstance().getGeofenceLongitude();

    latitude=28.621574;
    longitude= 76.929329;


    textView_saveGeofence.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isConnected = NetworkUtils.isConnected();
        if (isConnected) {
          if (radius_common==0){
            ToastUtils.showShort("Please select radius greater than 0");
          }else if (latLng_geofenceset ==null){
            ToastUtils.showShort("Please select a location on map");
          }else{
            saveGeofence();
          }
        }else{
          ToastUtils.showShort("No internet.");

        }
      }

    });

    imageView_edit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(isActivated) {
          textView_address.setText("Tap anywhere on the map to drop pin");
          imageView_edit.setVisibility(View.GONE);
          textView_saveGeofence.setVisibility(View.VISIBLE);
          //LatLng latLng = new LatLng(latitude, longitude);
          if (Gmap!=null) {
            Gmap.clear();
          }
          textView_radius.setText("0");
          isMarkerCreated=false;
          isCircleCreated=false;
          if(latLng!=null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(14)
                .bearing(0)
                .build();
            Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
          }

          isEditClick=true;
        }else{
          ToastUtils.showShort("Please activate the device to access this feature");
        }

      }
    });
    carId=CommonPreference.getInstance().getCarId();
  }

  private void saveGeofence() {
    try{
      JSONObject params=new JSONObject();
      params.put("car_id", carId);
      params.put("lat", String.valueOf(latLng_geofenceset.latitude));
      params.put("lng", String.valueOf(latLng_geofenceset.longitude));
      params.put("radius", String.valueOf(Integer.valueOf(textView_radius.getText().toString())*1000));
      ApiRequests.getInstance().save_geofence(GlobalReferences.getInstance().baseActivity,GeofenceActivity.this,params);

    }catch (JSONException ex){

    }

  }

  private void getGeofence() {
    carId=CommonPreference.getInstance().getCarId();
    ApiRequests.getInstance().get_geofence(GlobalReferences.getInstance().baseActivity,GeofenceActivity.this,carId);
  }

  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (Gmap == null) {
      // Try to obtain the map from the SupportMapFragment.
      fm = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.log_geofence));
      fm.getMapAsync(this);

    }
  }
  protected synchronized void buildGoogleApiClient() {

    //   Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */,
            this /* OnConnectionFailedListener */)
        .addConnectionCallbacks(this)
        .addApi(LocationServices.API)
        .addApi(Places.GEO_DATA_API)
        .build();
    mGoogleApiClient.connect();
  }

  @Override
  public void onStart() {
    super.onStart();
    if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
      mGoogleApiClient.connect();
    }

  }

  @Override
  public void onStop() {
    super.onStop();
    if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
    }
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    textView_radius.setText(String.valueOf(progress));
    radius_common = progress;
    if (circle != null) {
      circle.remove();
    }
    if (latLng_geofenceset != null) {
      LatLng latLng = new LatLng(latLng_geofenceset.latitude, latLng_geofenceset.longitude);
      marker.setPosition(latLng);
      CircleOptions circleOptions = new CircleOptions().center(new LatLng(latLng_geofenceset.latitude, latLng_geofenceset.longitude));
      circle = Gmap.addCircle(circleOptions
          .radius(progress*1000)
          .strokeColor(getResources().getColor(R.color.map_circle_fill_color))
          .fillColor(getResources().getColor(R.color.map_circle_fill_color)));
      Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(
          circleOptions.getCenter(), getZoomLevel(circle)));
    } else if (latLng != null) {
      marker.setPosition(latLng);
      CircleOptions circleOptions = new CircleOptions().center(latLng);
      circle = Gmap.addCircle(circleOptions
          .radius(progress*1000)
          .strokeColor(getResources().getColor(R.color.map_circle_fill_color))
          .fillColor(getResources().getColor(R.color.map_circle_fill_color)));
      Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(
          circleOptions.getCenter(), getZoomLevel(circle)));
    }
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {

  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {

  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    try {

      if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&

          ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //   Gmap.setMyLocationEnabled(true);
      }
      mLocationRequest = LocationRequest.create();
      mLocationRequest.setInterval(10000); //1 minute
      mLocationRequest.setSmallestDisplacement(100f);
      mLocationRequest.setFastestInterval(10000); //10seconds
      mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
      PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }catch (Exception ex){


    }
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override
  public void onResult(@NonNull Status status) {

  }

  @Override
  public void onLocationChanged(Location location) {

    latLng = new LatLng(location.getLatitude(), location.getLongitude());
    latitude = location.getLatitude();
    longitude = location.getLongitude();
  }

  @Override
  public void onMapClick(LatLng latLng) {
    if(isEditClick) {
      radius_common=0;
      seekbar_radius.setVisibility(View.VISIBLE);
      this.latLng_geofenceset = latLng;
      seekbar_radius.setProgress(0);
      setMarkerForCircle(latLng.latitude,latLng.longitude);


      getAddress(latLng.latitude, latLng.longitude);
    }
  }

  private void getAddress(double latitude, double longitude) {
    ApiRequests.getInstance().get_address(GlobalReferences.getInstance().baseActivity,GeofenceActivity.this,latitude,longitude);
  }

  @Override
  public void onMapLongClick(LatLng latLng) {

  }

  @Override
  public void onMarkerDragStart(Marker marker) {

  }

  @Override
  public void onMarkerDrag(Marker marker) {

  }

  @Override
  public void onMarkerDragEnd(Marker marker) {

  }

  @SuppressLint("MissingPermission")
  @Override
  public void onMapReady(GoogleMap googleMap) {
    isMapReady = true;
    Gmap = googleMap;
    Gmap.setMyLocationEnabled(false);
    Gmap.setOnMarkerDragListener(this);
    //Gmap.setOnMapLongClickListener(this);
    Gmap.getUiSettings().setZoomControlsEnabled(true);
    Gmap.setOnMapClickListener(this);
    CameraPosition cameraPosition_default = new CameraPosition.Builder()
        .target(new LatLng(24.8937, 78.9629))
        .zoom(4)
        .bearing(0)
        .build();
    Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition_default));


    if(!isActivated&&!isGeofenceSend){
      if(latLng!=null){


        if (Gmap != null) {
          Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8.0f));

          Gmap.clear();
          //setMarker(latLng);

          LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
          Criteria criteria = new Criteria();

          Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
          if (location != null) {
            Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                //.bearing(90)                // Sets the orientation of the camera to east
                //.tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
            Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
          }
        }

      }
    }

    try {
      boolean success = Gmap.setMapStyle(
          MapStyleOptions.loadRawResourceStyle(this, R.raw.style_map));
      if (!success) {
        // Handle map style load failure
      }
    } catch (Resources.NotFoundException e) {
      // Oops, looks like the map style resource couldn't be found!
    }
  }
  public void setMarkerForCircle(double latitude,double longitude){
    if(!isMarkerCreated) {
      marker = Gmap.addMarker(new MarkerOptions()
          .position(new LatLng(latitude, longitude))
          //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_result)
          .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_geofence", 100, 100))
          ));
      isMarkerCreated = true;
      marker.setDraggable(true);
      CircleOptions circleOptions = new CircleOptions().center(new LatLng(latitude, longitude));
      circle = Gmap.addCircle(circleOptions
          .radius(radius_common * 1000)
          .strokeColor(getResources().getColor(R.color.map_circle_fill_color))
          .fillColor(getResources().getColor(R.color.map_circle_fill_color)));
      isCircleCreated = true;
      Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(
          circleOptions.getCenter(), getZoomLevel(circle)));
    }else{
      marker.setPosition(new LatLng(latitude,longitude));
      CircleOptions circleOptions = new CircleOptions().center(new LatLng(latitude, longitude));
      circle.setCenter(new LatLng(latitude,longitude));
      Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(
          circleOptions.getCenter(), getZoomLevel(circle)));
    }
  }


  @Override
  public void onGranted() {
    if (Gmap != null) {
      Gmap.setMyLocationEnabled(true);
    }
  }

  @Override
  public void onDenied() {

  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    try {
      Log.e("Response", "onRequestCompleted: "+listener.getTag() );

      if (listener.getTag().equals("get_geofence")){
        JSONObject jsonObject = listener.getJsonResponse();
        String code = jsonObject.getString("status");
        JSONObject object_data = jsonObject.getJSONObject("msg");
        if (code.equals("SUCCEESS") || code.equals("SUCCESS")) {
           latitude=object_data.getDouble("lat");
           longitude=object_data.getDouble("lng");
          getAddress(latitude,longitude);
          int radius= object_data.getInt("radius");
          radius_common=radius/1000;
          textView_radius.setText(String.valueOf(radius_common)+" km");
          marker = Gmap.addMarker(new MarkerOptions()
              .position(new LatLng(latitude, longitude))

              .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_geofence", 100, 100))
              ));
          isMarkerCreated = true;
          marker.setDraggable(true);
          CircleOptions circleOptions = new CircleOptions().center(new LatLng(latitude, longitude));
          circle = Gmap.addCircle(circleOptions
              .radius(radius_common * 1000)
              .strokeColor(getResources().getColor(R.color.map_circle_fill_color))
              .fillColor(getResources().getColor(R.color.map_circle_fill_color)));
          isCircleCreated = true;
          Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(
              circleOptions.getCenter(), getZoomLevel(circle)));

          }
        }else if (listener.getTag().equals("address")){
         JSONObject response=listener.getJsonResponse();
          if (response.getJSONArray("results").length() > 0) {
            String addre = null;
            String result=null;

            addre = ((JSONArray) response.get("results")).getJSONObject(0).getString("formatted_address");

            String[] address_array = addre.split(",");
            StringBuffer buffer = new StringBuffer();

            for (int i = 0; i < address_array.length - 2; i++) {
              result = result + address_array[i];
              if (i == address_array.length - 3) {
                buffer.append(address_array[i]);
              } else {
                buffer.append(address_array[i] + ",");
              }
            }
            textView_address.setText(buffer.toString());

          }



      }

    } catch (JSONException ex) {

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
    try {
      JSONObject jsonObject = listener.getJsonResponse();
      String code = jsonObject.getString("status");
      String message = jsonObject.getString("msg");
      if (code.equals("SUCCEESS") || code.equals("SUCCESS")) {
        textView_radius.setText(textView_radius.getText().toString()+" km");
        textView_title_data.setText("Geofence set at");
        CommonPreference.getInstance().setGeofence(true);
        getAddress(latLng_geofenceset.latitude,latLng_geofenceset.longitude);
        seekbar_radius.setVisibility(View.GONE);
        isGeofenceSet=true;
        textView_saveGeofence.setVisibility(View.GONE);
        isEditClick=false;
        imageView_edit.setVisibility(View.VISIBLE);
        setMarkerForCircle(latLng_geofenceset.latitude,latLng_geofenceset.longitude);
        new AwesomeSuccessDialog(GeofenceActivity.this)
            .setTitle(code)
            .setMessage("SUCCESS")
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
