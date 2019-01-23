package blackflame.com.zymepro.ui.geotag.savegeotag;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;


import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.PermissionConstants;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.geotag.GeoTagActivity;
import blackflame.com.zymepro.util.AnimationUtils;
import blackflame.com.zymepro.util.GMapUtil;

import blackflame.com.zymepro.util.PermissionUtils;
import blackflame.com.zymepro.util.ToastUtils;
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
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


import java.io.IOException;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class SaveGeotagActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
    com.google.android.gms.location.LocationListener,
    ResultCallback<Status>, OnClickListener, OnCameraIdleListener, PermissionUtils.SimpleCallback,
    AppRequest {

  private static final String TAG = SaveGeotagActivity.class.getCanonicalName();
  LocationRequest mLocationRequest;
  GoogleApiClient mGoogleApiClient;
  GoogleMap Gmap;
  SupportMapFragment fm;
  LatLng latLng;
  double lat, lng;
  EditText et_tag_address;
  RelativeLayout home, office, other;
  String type;
  View line_home, line_office, line_other;
  RelativeLayout layout_other;
  EditText et_other_name;
  TextView cancel;
  LinearLayout container_home;
  TextView tv_save;
  ProgressBar progressBarHolder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_save_geotag);
    GlobalReferences.getInstance().baseActivity = this;
    initViews();
  }

  private void initViews() {
    Toolbar toolbar = findViewById(R.id.toolbar_common);
    TextView title = findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar, title, "Add Geotag");
    home = findViewById(R.id.add_geotag_home);
    office = findViewById(R.id.add_geotag_office);
    other = findViewById(R.id.add_geotag_other);
    line_home = findViewById(R.id.view_home);
    line_office = findViewById(R.id.view_office);
    line_other = findViewById(R.id.view_other);
    layout_other = findViewById(R.id.layout_other_text);
    container_home = findViewById(R.id.container_home_tag);
    cancel = findViewById(R.id.tv_add_tag_cancel);
    et_other_name = findViewById(R.id.et_add_other_text);
    tv_save = findViewById(R.id.tv_save_geo_tag);
    progressBarHolder = findViewById(R.id.pBar);
    et_tag_address=findViewById(R.id.et_add_tag_address);
    tv_save.setOnClickListener(this);
    cancel.setOnClickListener(this);
    home.setOnClickListener(this);
    other.setOnClickListener(this);
    office.setOnClickListener(this);
    buildGoogleApiClient();
    setUpMapIfNeeded();

    if (PermissionUtils.isGranted(PermissionConstants.LOCATION)) {
      if (Gmap != null) {
        Gmap.setMyLocationEnabled(true);
      }
    } else {
      PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.LOCATION));
      PermissionUtils.sInstance.callback(this);
      PermissionUtils.sInstance.request();
    }

  }

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.tv_save_geo_tag:

        Log.e(TAG, "onClick:   " + type);
        if (lat == 0) {
          ToastUtils.showShort("Move pin to adjust location");

        } else if (lng == 0) {
          ToastUtils.showShort("Move pin to adjust location");
        } else if (et_tag_address.getText().toString() == null || TextUtils
            .isEmpty(et_tag_address.getText().toString()) || et_tag_address.getText().toString()
            .matches("^\\s*$")) {
          et_tag_address.setError("Move pin to adjust location");
        } else if (type == null) {
          ToastUtils.showShort("Please select geotag name");
        } else if (type.equals("OTHER") && (TextUtils.isEmpty(et_other_name.getText().toString())
            || et_other_name.getText().toString().matches("^\\s*$"))) {
          et_other_name.setError("Please enter tag name");
        } else {

          saveGeoTag();

        }
        break;
      case R.id.tv_add_tag_cancel:
        if (layout_other.getVisibility() == View.VISIBLE) {
          if (layout_other.getAnimation() != null) {
            layout_other.getAnimation().cancel();
            layout_other.startAnimation(AnimationUtils.outToRightAnimation());
          }
          layout_other.setVisibility(View.GONE);
        }
        Log.e(TAG, "onClick: " + layout_other.getVisibility());

        container_home.setVisibility(View.VISIBLE);
        container_home.startAnimation(AnimationUtils.inFromLeftAnimation());

        break;
      case R.id.add_geotag_home:
        type = "HOME";
        setBackGround("HOME");
        break;
      case R.id.add_geotag_other:

        type = "OTHER";
        setBackGround("OTHER");

        container_home.setVisibility(View.GONE);

        if (container_home.getAnimation() != null) {
          container_home.startAnimation(AnimationUtils.outToLeftAnimation());
          container_home.getAnimation().cancel();
        }
        et_other_name.setFocusable(true);
        layout_other.setVisibility(View.VISIBLE);
        layout_other.startAnimation(AnimationUtils.inFromRightAnimation());
        break;
      case R.id.add_geotag_office:
        type = "OFFICE";
        setBackGround("OFFICE");
        break;
    }

  }

  private void setBackGround(String type) {
    switch (type) {
      case "HOME":
        line_office.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        line_other.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        line_home.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        break;
      case "OFFICE":
        line_office.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        line_other.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        line_home.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        break;
      case "OTHER":
        line_office.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        line_other.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        line_home.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        break;
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

  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (Gmap == null) {
      // Try to obtain the map from the SupportMapFragment.
      fm = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_add_geotag));
      fm.getMapAsync(this);
    }
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    try {

      if (Build.VERSION.SDK_INT >= 23 &&
          ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
              != PackageManager.PERMISSION_GRANTED &&

          ContextCompat
              .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
              != PackageManager.PERMISSION_GRANTED) {
        //   Gmap.setMyLocationEnabled(true);
      }
      mLocationRequest = LocationRequest.create();
      mLocationRequest.setInterval(10000); //1 minute
      mLocationRequest.setSmallestDisplacement(100f);
      mLocationRequest.setFastestInterval(10000); //10seconds
      mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
      PendingResult<Status> pendingResult = LocationServices.FusedLocationApi
          .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    } catch (Exception ex) {

    }
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
    lat = location.getLatitude();
    lng = location.getLongitude();

    latLng = new LatLng(location.getLatitude(), location.getLongitude());

    CameraPosition cameraPosition_default = new CameraPosition.Builder()
        .target(new LatLng(location.getLatitude(), location.getLongitude()))
        .zoom(16)
        .bearing(0)
        .build();
    Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition_default));

  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    Gmap = googleMap;
    Gmap.getUiSettings().setZoomControlsEnabled(true);
    CameraPosition cameraPosition_default = new CameraPosition.Builder()
        .target(new LatLng(24.8937, 78.9629))
        .zoom(4)
        .bearing(0)
        .build();
    Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition_default));

    Gmap.setOnCameraIdleListener(this);

    if (latLng != null) {
      if (Gmap != null) {
        Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8.0f));

        Gmap.clear();
        //setMarker(latLng);

        LocationManager locationManager = (LocationManager) getSystemService(
            Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager
            .getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
          Gmap.animateCamera(CameraUpdateFactory
              .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

          CameraPosition cameraPosition = new CameraPosition.Builder()
              .target(new LatLng(location.getLatitude(),
                  location.getLongitude()))      // Sets the center of the map to location user
              .zoom(17)                   // Sets the zoom
              // Sets the tilt of the camera to 30 degrees
              .build();                   // Creates a CameraPosition from the builder
          Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
      }

    }
    if (Gmap != null) {
      GMapUtil.setMapStyle(Gmap);
    }
  }


  @Override
  public void onCameraIdle() {
    LatLng latLng = Gmap.getCameraPosition().target;
    lat = latLng.latitude;
    lng = latLng.longitude;

    Geocoder geocoder = new Geocoder(SaveGeotagActivity.this);
    try {
      List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
      if (addressList != null && addressList.size() > 0) {
        String locality = addressList.get(0).getAddressLine(0);
        String country = addressList.get(0).getCountryName();
        if (!locality.isEmpty() && !country.isEmpty()) {
          et_tag_address.setText(locality + "  " + country);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void saveGeoTag() {
    try {
      JSONObject params = new JSONObject();
      params.put("latitude", String.valueOf(lat));
      params.put("longitude", String.valueOf(lng));
      params.put("address", et_tag_address.getText().toString());
      params.put("geotag_type", type);
      if (type.equals("OTHER")) {
        params.put("tag_name", et_other_name.getText().toString());

      } else {
        params.put("tag_name", type);
      }

      ApiRequests.getInstance()
          .save_geotag(GlobalReferences.getInstance().baseActivity, SaveGeotagActivity.this,
              params);
    } catch (JSONException ex) {
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
    progressBarHolder.setVisibility(View.VISIBLE);

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
    try {
      progressBarHolder.setVisibility(View.GONE);
      JSONObject jsonObject = listener.getJsonResponse();
      String status = jsonObject.getString("status");
      final String msg = jsonObject.getString("msg");
      if (status.equals("SUCCESS")) {
        ToastUtils.showShort(msg);
        Intent intent = new Intent(SaveGeotagActivity.this, GeoTagActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
      } else if (status.equals("ERROR")) {
        new Handler(getMainLooper()).post(new Runnable() {
          @Override
          public void run() {
            progressBarHolder.setVisibility(View.GONE);
            ToastUtils.showShort(msg);

          }
        });


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
