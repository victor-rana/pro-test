package blackflame.com.zymepro.ui.home.singlecar;


import static blackflame.com.zymepro.constant.PermissionConstants.getPermissions;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.cardview.widget.CardView;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import blackflame.com.zymepro.BuildConfig;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.db.SettingPreferences;
import blackflame.com.zymepro.ui.activation.ActivityActivation;
import blackflame.com.zymepro.Prosingleton;

import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.PermissionConstants;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.dialog.NerdModeDialog;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.mqtt.MqttHandler;
import blackflame.com.zymepro.ui.alertdetails.AlertDetails;
import blackflame.com.zymepro.ui.alerts.AlertActivity;
import blackflame.com.zymepro.ui.analytic.AnalyticActivity;
import blackflame.com.zymepro.ui.carregistration.CarRegistration;
import blackflame.com.zymepro.ui.dashcam.recordvideo.RecordVideoActivity;
import blackflame.com.zymepro.ui.enginescan.EngineScanActivity;
import blackflame.com.zymepro.ui.history.HistoryActivity;
import blackflame.com.zymepro.ui.home.MainActivity;
import blackflame.com.zymepro.ui.home.MqttDataListener;
import blackflame.com.zymepro.ui.home.multicar.MulticarFragment;
import blackflame.com.zymepro.ui.livetrip.LiveTripActivity;
import blackflame.com.zymepro.ui.pitstop.PitstopActivity;
import blackflame.com.zymepro.util.GMapUtil;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.PermissionUtils;
import blackflame.com.zymepro.util.PermissionUtils.SimpleCallback;
import blackflame.com.zymepro.util.ToastUtils;
import blackflame.com.zymepro.util.UtilityMethod;
import blackflame.com.zymepro.view.custom.RippleBackground;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.what3words.androidwrapper.What3WordsV3;
import com.what3words.javawrapper.request.Coordinates;
import com.what3words.javawrapper.response.ConvertTo3WA;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SingleCarFragment extends CommonFragment implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback,SimpleCallback,SinglePresenter.View ,AppRequest ,MqttDataListener,OnClickListener {

  private static final int REQ_PERMISSION = 100;
  MapView mMapView;
  LinearLayout history, alert, pitstop, enginescan,analytics;
  LinearLayout linearLayout_bottom;
  View view_bg_opacity,view_clicked;
  TextView textView_selectedCar,textView_parked;
  ListView listview_carCount;
  RelativeLayout relativeLayout_selectedcar,relativeLayout_realtime_data,relativeLayout_pokemon_hide;
  TextView tv_time, tv_distance, tv_avgspeed,tv_address,
      tv_speed,tv_status, tv_last_update_time, tv_battery_realtime, tv_coolant_realtime;
  ProgressBar progressBar_dataloading;
  LinearLayout linearLayout_nerd_mode,linearLayout_group,iv_nerd_mode,iv_dashcam,linearLayout_home_container_top;
  ImageView imageView_zoomin,imageView_zoomout,imageView_car,iv_more;
  LinearLayout linearLayout_sos, linearLayout_sharelocation,
      linearLayout_pokemon_child,linearLayout_carllocation;
  CardView cardView_parked, cardView_active;
  RippleBackground rippleBackground;
  GoogleMap gmap;
  boolean isMapReady;
  SinglePresenter presenter;
  private boolean isCardclickable =false;
  String text_parked = "<font color=#46000000>Car is</font> <font color=#2da9e1>Parked</font>";
  AlphaAnimation buttonClick;
  double lastLatitude= 24.8937, lastLongitude=78.9629;
  private boolean isMarkerCreated=false;
  CarcountAdapter adapter;

  ArrayList<CarcountModel> list;

  String registration,status;
  String TAG=SingleCarFragment.class.getCanonicalName();
  NerdModeDialog dialog_nerd_mode;
  boolean isSosClicked=false;
  ImageView trip_route;

  String selectedImei;


  PendingIntent sentPending, deliveredPending;

  TextView tvTimer,tvThreeWords,tvWhats3words;
    What3WordsV3 what3WordsV3;
    ImageView ivTakeOnMap;
    String mapAddress;
    SwitchCompat switchWhats3Words;
    LinearLayout llWhatswords;

    String fragmentTag="SingleCar";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    Log.d("Single car fragment ","");
    View view = inflater.inflate(R.layout.fragment_singlecar, container, false);
    mMapView = view.findViewById(R.id.map);
    switchWhats3Words=view.findViewById(R.id.switch_w3w);
    llWhatswords=view.findViewById(R.id.ll_whats3words);




    tvWhats3words=view.findViewById(R.id.tvWhatsW3);




    switchWhats3Words.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        CommonPreference.getInstance().setWhats3Words(b);
        if (b){


          if (lastLatitude != 0){
            llWhatswords.setVisibility(View.VISIBLE);
            new FetchWhatsThreeWords().execute(lastLatitude,lastLongitude);
          }

        }else{
          llWhatswords.setVisibility(View.GONE);
        }

      }
    });


    switchWhats3Words.setChecked(CommonPreference.getInstance().getsetWhats3Words());



    tvWhats3words.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://what3words.com/about-us/"));

        startActivity(browserIntent);

      }
    });

    mMapView.onCreate(savedInstanceState);
    initViews(view);
    presenter=new SinglePresenter(this,new SingleInteractor());
    presenter.handleBundle(getArguments());
    sentPending = PendingIntent.getBroadcast(getActivity(), 0, new Intent("SENT"), 0);
    deliveredPending = PendingIntent.getBroadcast(getActivity(), 0, new Intent("DELIVERED"), 0);
    return view;
  }
  public void initViews(View view){
    linearLayout_bottom = view.findViewById(R.id.contaier_bottom);
    pitstop = view.findViewById(R.id.ll_pitstop);
    enginescan = view.findViewById(R.id.ll_enginescan);
    history = view.findViewById(R.id.ll_triphistory);
    alert = view.findViewById(R.id.ll_alert);
    analytics=view.findViewById(R.id.ll_analytics);
    ivTakeOnMap=view.findViewById(R.id.ivTakeonMap);
    ivTakeOnMap.setOnClickListener(view1 -> {

      if (mapAddress != null) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapAddress));

        startActivity(browserIntent);

      }
    });



    tvTimer=view.findViewById(R.id.tv_timer);
    if (CommonPreference.getInstance().getIsDemoUser()){
      tvTimer.setVisibility(View.VISIBLE);
    }

    tvThreeWords=view.findViewById(R.id.address_realtime_three_words);


    pitstop.setOnClickListener(this);
    enginescan.setOnClickListener(this);
    alert.setOnClickListener(this);
    history.setOnClickListener(this);
    analytics.setOnClickListener(this);

     trip_route=view.findViewById(R.id.iv_trip_details);
     trip_route.setOnClickListener(new OnClickListener() {
       @Override
       public void onClick(View view) {
         Intent intent=new Intent(getActivity(), LiveTripActivity.class);
         intent.putExtra("imei",selectedImei);
         startActivity(intent);
       }
     });

    setRipple(enginescan);
    setRipple(history);
    setRipple(alert);
    setRipple(analytics);
    setRipple(pitstop);
    view_bg_opacity = view.findViewById(R.id.car_selection_bg_opacity);
    textView_selectedCar = view.findViewById(R.id.tv_selected_car);
    textView_parked = view.findViewById(R.id.textview_parked);
    listview_carCount = view.findViewById(R.id.listview_car_count);
    relativeLayout_selectedcar = view.findViewById(R.id.current_car);
    relativeLayout_pokemon_hide = view.findViewById(R.id.pokemon_and_ripple);
    tv_battery_realtime = view.findViewById(R.id.battery_realtime);
    tv_coolant_realtime = view.findViewById(R.id.coolant_realtime);
    progressBar_dataloading = view.findViewById(R.id.progressBar_data_loading);
    linearLayout_home_container_top= view.findViewById(R.id.home_container_tv_top);
    iv_nerd_mode= view.findViewById(R.id.iv_nerd_mode);
    iv_dashcam= view.findViewById(R.id.iv_dashcam);
    iv_dashcam.setOnClickListener(this);
    linearLayout_group = view.findViewById(R.id.layout_container_group);
    imageView_zoomin = view.findViewById(R.id.zoom_in);
    imageView_zoomout = view.findViewById(R.id.zoom_out);
    linearLayout_sos = view.findViewById(R.id.iv_sos);
    linearLayout_carllocation = view.findViewById(R.id.iv_parkcarlocator);
    linearLayout_sharelocation = view.findViewById(R.id.iv_takemeonlocation);

    linearLayout_sos.setOnClickListener(this);
    linearLayout_carllocation.setOnClickListener(this);
    linearLayout_sharelocation.setOnClickListener(this);
    cardView_parked = view.findViewById(R.id.containertop_textview);
    iv_more = view.findViewById(R.id.iv_more);
    cardView_parked.setClickable(false);
    cardView_active = view.findViewById(R.id.containertop);

    linearLayout_nerd_mode= view.findViewById(R.id.layout_container_group_top);
    linearLayout_pokemon_child = view.findViewById(R.id.pokemon_child);
    view_clicked = view.findViewById(R.id.view_click);
    tv_avgspeed = view.findViewById(R.id.avgspeed);
    tv_distance = view.findViewById(R.id.distance);
    tv_time = view.findViewById(R.id.triptime);
    tv_status = view.findViewById(R.id.status);
    tv_speed = view.findViewById(R.id.speed_realtime);
    //tv_rpm = (TextView) convertview.findViewById(R.id.rpm_realtime);
    tv_address = view.findViewById(R.id.address_realtime);
    tv_last_update_time = view.findViewById(R.id.address_lastupdate_time);
    rippleBackground = view.findViewById(R.id.ripple);
    relativeLayout_realtime_data = view.findViewById(R.id.pokemon);
    imageView_car = view.findViewById(R.id.centerImage);
    what3WordsV3 = new What3WordsV3(BuildConfig.THREE_WORDS,getActivity().getApplicationContext());
    mMapView.onResume();
    fetchStatusData();
    try {
      MapsInitializer.initialize(Prosingleton.getAppContext());
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!PermissionUtils.isGranted(PermissionConstants.LOCATION)&& !PermissionUtils.isGranted(PermissionConstants.CONTACTS)){
      PermissionUtils.permission(getPermissions(PermissionConstants.LOCATION));
      PermissionUtils.sInstance.callback(this);
      PermissionUtils.sInstance.request();
    }


    mMapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap mMap) {
        gmap = mMap;
        isMapReady = true;
        gmap.getUiSettings().setScrollGesturesEnabled(false);
        gmap.getUiSettings().setTiltGesturesEnabled(false);
        gmap.getUiSettings().setScrollGesturesEnabled(false);
        gmap.getUiSettings().setCompassEnabled(false);
        gmap.getUiSettings().setScrollGesturesEnabled(false);
        gmap.getUiSettings().setZoomGesturesEnabled(false);
        gmap.getUiSettings().setRotateGesturesEnabled(false);
        gmap.setOnMarkerClickListener(SingleCarFragment.this);

        if (CommonPreference.getInstance().getLiveTraffic()) {
          gmap.setTrafficEnabled(true);
        }else{
          gmap.setTrafficEnabled(false);
        }

        GMapUtil.setMapStyle(gmap);

        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(24.8937, 78.9629))
            .zoom(4)
            .bearing(0)
            .build();
        if (gmap != null) {
          gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }




        if ( getActivity() instanceof MainActivity){
          MainActivity   activity = (MainActivity) getActivity();
          activity.setting.setVisibility(View.VISIBLE);
          activity.refresh.setVisibility(View.GONE);
          activity.share_live_tracking.setVisibility(View.VISIBLE);
        }


      }
    });



    View googleLogo = view.findViewWithTag("GoogleWatermark");
    try {
      RelativeLayout.LayoutParams glLayoutParams = (RelativeLayout.LayoutParams) googleLogo.getLayoutParams();
      glLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
      glLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
      glLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
      glLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
      glLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
      googleLogo.setLayoutParams(glLayoutParams);

    }catch (Exception ex){


    }

    onSelectCar();
    onOpacity();
    cardParkedClick();
    viewBacgroundBlack();
    zoomIn();
    zoomout();
    pokemonClick();
    listClick();
    nerdMode();


  }


  public void listClick() {
    listview_carCount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listview_carCount.setVisibility(View.GONE);
        CarcountModel model = list.get(position);
        if (model.getBrand().equals("All")) {
          MulticarFragment multicar = new MulticarFragment();
          GlobalReferences.getInstance().baseActivity.addFragmentWithBackStack(multicar,false,null,"MultiCar");
        } else {
          onDetach();
          onDestroyView();
          listview_carCount.setVisibility(View.GONE);
          SingleCarFragment singleCar = new SingleCarFragment();
          Bundle bundle = new Bundle();
          bundle.putString("IMEI", list.get(position).getImei());
          bundle.putString("registration_number", list.get(position).getRegistration());
          bundle.putInt("coming", 2);
          GlobalReferences.getInstance().baseActivity.addFragmentWithBackStack(singleCar,false,bundle,"SingleCar");
        }
      }
    });
  }


  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    buttonClick = new AlphaAnimation(1F, 0.7F);
  }

  public void cardParkedClick() {
    textView_parked.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isCardclickable) {
         // v.startAnimation(buttonClick);
          presenter.handleActivation();
        }
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    mMapView.onResume();
    try {
      if (CommonPreference.getInstance().getLiveTraffic()) {
     if (gmap!=null)
          gmap.setTrafficEnabled(true);
      }else{
        if (gmap!=null)
          gmap.setTrafficEnabled(false);
      }
      GMapUtil.setMapStyle(gmap);


    } catch(Resources.NotFoundException e){
      // Log.e(TAG, "onResume: "+e.getMessage() );
      // Oops, looks like the map style resource couldn't be found!
    }


    Log.e(TAG, "onResume: "+ CommonPreference.getInstance().getExpiryTime());

    if (CommonPreference.getInstance().getExpiryTime() != null && CommonPreference.getInstance().getIsDemoUser()) {
      long time = UtilityMethod.getTimerDate(CommonPreference.getInstance().getExpiryTime()).getTime() - System.currentTimeMillis();


      new CountDownTimer(time, 1000) {

        public void onTick(long millisUntilFinished) {
          tvTimer.setText("Time remaining: " + UtilityMethod.convertSecondsToHMmSs(millisUntilFinished / 1000));

          //here you can have your logic to set text to edittext
        }

        public void onFinish() {
          tvTimer.setText("done");
        }

      }.start();
    }





  }

  private void onSelectCar(){
    relativeLayout_selectedcar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listview_carCount.setVisibility(listview_carCount.isShown()
            ? View.GONE
            : View.VISIBLE);
        relativeLayout_pokemon_hide.setVisibility(listview_carCount.isShown()
            ? View.GONE
            : View.VISIBLE);
        view_bg_opacity.setVisibility(listview_carCount.isShown()
            ? View.VISIBLE
            : View.GONE);











      }
    });
  }
  private  void onOpacity(){
    view_bg_opacity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (listview_carCount.getVisibility() == View.VISIBLE) {
          view_bg_opacity.setVisibility(View.GONE);
        }
        listview_carCount.setVisibility(View.GONE);
        relativeLayout_pokemon_hide.setVisibility(View.VISIBLE);
      }
    });
  }


  private void nerdMode(){

    iv_nerd_mode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog_nerd_mode=new NerdModeDialog(getActivity(),view_clicked);
        dialog_nerd_mode.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        dialog_nerd_mode.show();
        dialog_nerd_mode.setCancelable(false);
        if (relativeLayout_realtime_data.getVisibility() == View.VISIBLE) {
          //view_clicked.setVisibility(View.GONE);
          relativeLayout_realtime_data.setVisibility(View.GONE);
          imageView_car.setImageDrawable(getResources().getDrawable(R.drawable.ic_car_big));
        }
      }
    });
  }


  private void setRipple(View view){
    LinearLayout btn=(LinearLayout) view;
    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
      btn.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
    }
  }
  public void pokemonClick() {
    imageView_car.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        relativeLayout_realtime_data.setVisibility(relativeLayout_realtime_data.isShown()
            ? View.GONE
            : View.VISIBLE);
        if (relativeLayout_realtime_data.isShown()) {
          view_clicked.setVisibility(View.VISIBLE);
          view_clicked.setBackgroundColor(getResources().getColor(R.color.black_low));

        } else {

          view_clicked.setVisibility(View.GONE);
          view_clicked.setBackgroundColor(getResources().getColor(R.color.transparent));

        }
        if (status != null && status.equals("ACTIVE")) {

          loadAddress(lastLatitude,lastLongitude);

        }

        imageView_car.setImageDrawable(relativeLayout_realtime_data.isShown()
            ? getResources().getDrawable(R.drawable.ic_car_icon)
            : getResources().getDrawable(R.drawable.ic_car_big));
      }
    });

  }



  @Override
  public boolean onMarkerClick(Marker marker) {
    if (gmap != null) {
      gmap.clear();
      CameraPosition cameraPosition = new CameraPosition.Builder()
          .target(new LatLng(lastLatitude, lastLongitude))
          .zoom(16)
          .bearing(0)
//                                                    .tilt(45)
          .build();
      gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

      imageView_car.setVisibility(relativeLayout_realtime_data.isShown()
          ? View.GONE
          : View.VISIBLE);

      relativeLayout_realtime_data.setVisibility(relativeLayout_realtime_data.isShown()
          ? View.GONE
          : View.VISIBLE);
      if (relativeLayout_realtime_data.isShown()) {
        view_clicked.setVisibility(View.VISIBLE);
        view_clicked.setBackgroundColor(getResources().getColor(R.color.black_low));

      } else {
        view_clicked.setVisibility(View.GONE);
        view_clicked.setBackgroundColor(getResources().getColor(R.color.transparent));

      }
      if (status != null && status.equals("ACTIVE")) {
        loadAddress(lastLatitude,lastLongitude);

      }


      imageView_car.setImageDrawable(relativeLayout_realtime_data.isShown()
          ? getResources().getDrawable(R.drawable.ic_car_icon)
          : getResources().getDrawable(R.drawable.ic_car_big));

    }
    return true;
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {

  }


  @Override
  public void onGranted() {
  //  Log.e(TAG, "onGranted: "+"Granted" );
    //fetchStatusData();

    if (isSosClicked){
      getSos();
      isSosClicked=false;
    }
  }

  @Override
  public void onDenied() {

  }


  @Override
  public void onPause() {
    super.onPause();
    mMapView.onPause();
  }



  public void zoomIn() {
    imageView_zoomin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        boolean isDeviceActivate = CommonPreference.getInstance().getDeviceActivated();
        if (isDeviceActivate) {
          if (relativeLayout_realtime_data.getVisibility() == View.VISIBLE) {
            relativeLayout_realtime_data.setVisibility(View.GONE);
            view_clicked.setVisibility(View.GONE);
          }
          imageView_car.setVisibility(View.GONE);
          if (gmap != null) {
            gmap.clear();
            gmap.addMarker(new MarkerOptions().position(new LatLng(lastLatitude, lastLongitude))
                //.icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.car_icon_big)))))

                .icon(BitmapDescriptorFactory
                    .fromBitmap(UtilityMethod.resizeMapIcons("ic_car_big", 90, 90))))
                .setAnchor(0.5f, 0.5f)
            ;

            // imageView.setVisibility(View.GONE);

            float max_zoom=gmap.getMaxZoomLevel();
            float zoom = gmap.getCameraPosition().zoom ;
            if (zoom<=max_zoom-2) {
              CameraPosition cameraPosition = new CameraPosition.Builder()
                  .target(new LatLng(lastLatitude, lastLongitude))
                  .zoom(zoom + 1)
                  .bearing(0)
                  .build();

              gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null);
            }
          }
        }else {

          gmap.animateCamera(CameraUpdateFactory.zoomIn(), 500, null);
        }

      }
    });

  }


  public void zoomout() {
    imageView_zoomout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.startAnimation(buttonClick);
        //CameraUpdate update= CameraUpdateFactory.zoomOut();
        //if(status!=null&&!status.equals("ACTIVE")) {

        boolean isDeviceActivate = CommonPreference.getInstance().getDeviceActivated();
        if (isDeviceActivate) {

          if (relativeLayout_realtime_data.getVisibility() == View.VISIBLE) {
            relativeLayout_realtime_data.setVisibility(View.GONE);
            view_clicked.setVisibility(View.GONE);
          }
          imageView_car.setVisibility(View.GONE);
          if (gmap!=null) {
            gmap.clear();
            gmap.addMarker(new MarkerOptions().position(new LatLng(lastLatitude, lastLongitude))
                .icon(BitmapDescriptorFactory.fromBitmap(UtilityMethod.resizeMapIcons("ic_car_big", 90, 90))))
                .setAnchor(0.5f, 0.5f);

            float zoom = gmap.getCameraPosition().zoom;
            CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lastLatitude, lastLongitude))
                .zoom(zoom - 1)
                .bearing(0)
//                            .tilt(0)
                .build();
            gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),500,null);
          }
          //}
        } else {
          if (gmap!=null)
            gmap.animateCamera(CameraUpdateFactory.zoomOut(),500,null);
        }

      }
    });
  }



  public void viewBacgroundBlack() {
    view_clicked.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (relativeLayout_realtime_data.getVisibility() == View.VISIBLE) {
          relativeLayout_realtime_data.setVisibility(View.GONE);
          view_clicked.setVisibility(View.GONE);
          imageView_car.setImageDrawable(getResources().getDrawable(R.drawable.ic_car_big));

        }
      }
    });
  }

  @Override
  public void setParkedCard() {
    status="PARKED";
    isCardclickable = false;
    cardView_parked.setClickable(false);
    rippleBackground.stopRippleAnimation();
    tv_speed.setVisibility(View.GONE);
    tv_battery_realtime.setVisibility(View.GONE);
    tv_coolant_realtime.setVisibility(View.GONE);
    linearLayout_nerd_mode.setVisibility(View.GONE);
    linearLayout_home_container_top.setVisibility(View.GONE);
    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
      textView_parked.setText(Html.fromHtml(text_parked));
    } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      textView_parked.setText(Html.fromHtml(text_parked, Html.FROM_HTML_MODE_COMPACT));
    } else {
      textView_parked.setText("Car is Parked");
    }
    cardView_parked.setVisibility(View.VISIBLE);
    relativeLayout_selectedcar.setVisibility(View.VISIBLE);
    linearLayout_home_container_top.setVisibility(View.GONE);



  }

  @Override
  public void setActivationCard() {
    status="NOT_ACTIVATED";
   // Log.e(TAG, "setActivationCard: "+status );
    isCardclickable = true;
    cardView_parked.setClickable(true);
    textView_parked.setFocusable(true);
    cardView_parked.setVisibility(View.VISIBLE);
    textView_parked.setText("Click to Activate Device");
    relativeLayout_selectedcar.setVisibility(View.GONE);
    linearLayout_nerd_mode.setVisibility(View.GONE);
    linearLayout_home_container_top.setVisibility(View.GONE);

  }

  @Override
  public void setMulticar() {

  }

  @Override
  public void setSingleCar() {
    iv_more.setVisibility(View.GONE);
  }

  @Override
  public void setSelectedImei(String imei) {
  }

  @Override
  public void setRegistration(String registration) {
    this.registration=registration;

  }

  @Override
  public void registerMqtt(String imei) {
    selectedImei=imei;
    MqttHandler.registerMqtt(imei,this);

  }

  @Override
  public void navigateToRegistration() {
    Intent intent = new Intent(getActivity(), CarRegistration.class);
    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(intent);
  }

  @Override
  public void navigationToActivation(String imei) {
    Intent intent = new Intent(getActivity(), ActivityActivation.class);
    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
    intent.putExtra("imei", imei);
    startActivity(intent);
  }

  @Override
  public void setCarLocation(double latitude, double longitude) {
    lastLatitude=latitude;
    lastLongitude=longitude;
    imageView_car.setVisibility(View.VISIBLE);

    if (CommonPreference.getInstance().getsetWhats3Words()){
      llWhatswords.setVisibility(View.VISIBLE);
    }


    if (lastLatitude != 0.0) {
      isMarkerCreated = true;
      CameraPosition cameraPosition = new CameraPosition.Builder()
          .target(new LatLng(lastLatitude, lastLongitude))
          .zoom(16)
          .bearing(0)
          .build();
      if (gmap != null) {
        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      }
      }

    if (CommonPreference.getInstance().getsetWhats3Words()) {
      new FetchWhatsThreeWords().execute(latitude, longitude);
    }

  }

  @Override
  public void setSelectedModel(String model, String registration) {
    textView_selectedCar.setText(model + " - " + registration);

  }

  @Override
  public void setCardClickable(boolean value) {
    isCardclickable=value;

  }

  @Override
  public void setUnlinked() {
    String text_unlinked = "<font color=#46000000>Device is</font> <font color=#2da9e1>" + "Unlinked" + "</font>" + "<br>" + "<font size=1><small>Relink your device from the profile page</small></font>";
    imageView_car.setVisibility(View.VISIBLE);
    rippleBackground.stopRippleAnimation();
    tv_speed.setVisibility(View.GONE);
    tv_battery_realtime.setVisibility(View.GONE);
    tv_coolant_realtime.setVisibility(View.GONE);
    textView_parked.setClickable(false);
    isCardclickable = false;

    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
      textView_parked.setText(Html.fromHtml(text_unlinked));
    } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      textView_parked.setText(Html.fromHtml(text_unlinked, Html.FROM_HTML_MODE_COMPACT));
    } else {
     // textView_parked.setText(car_last_status);
    }
    cardView_parked.setVisibility(View.VISIBLE);
    linearLayout_nerd_mode.setVisibility(View.GONE);
    linearLayout_home_container_top.setVisibility(View.GONE);

  }

  @Override
  public void setUnplugged() {
    String text_unplugged = "<font color=#46000000>Zyme is</font> <font color=#2da9e1>Un-plugged</font>";
    imageView_car.setVisibility(View.VISIBLE);
    // tv_status.setText("PARKED");
    rippleBackground.stopRippleAnimation();
    tv_speed.setVisibility(View.GONE);
    tv_battery_realtime.setVisibility(View.GONE);
    tv_coolant_realtime.setVisibility(View.GONE);
    textView_parked.setClickable(false);
    isCardclickable = false;
    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
      textView_parked.setText(Html.fromHtml(text_unplugged));
    } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      textView_parked.setText(Html.fromHtml(text_unplugged, Html.FROM_HTML_MODE_COMPACT));
    } else {
      textView_parked.setText("Zyme is Un-plugged");
    }
    cardView_parked.setVisibility(View.VISIBLE);
    linearLayout_nerd_mode.setVisibility(View.GONE);
    linearLayout_home_container_top.setVisibility(View.GONE);
  }

  @Override
  public void setListData(ArrayList<CarcountModel> data) {
    list = data;
    adapter = new CarcountAdapter(list);
    listview_carCount.setAdapter(adapter);
  }
  @Override
  public void setActiveCard() {
    status="ACTIVE";
    rippleBackground.startRippleAnimation();
    tv_status.setText("ACTIVE");
    imageView_car.setVisibility(View.VISIBLE);
    cardView_parked.setVisibility(View.GONE);
    textView_parked.setClickable(false);
    isCardclickable = false;
    relativeLayout_selectedcar.setVisibility(View.VISIBLE);
    linearLayout_nerd_mode.setVisibility(View.VISIBLE);
    linearLayout_home_container_top.setVisibility(View.VISIBLE);
    tv_speed.setVisibility(View.VISIBLE);
    tv_coolant_realtime.setVisibility(View.VISIBLE);
    tv_battery_realtime.setVisibility(View.VISIBLE);
  }


  @Override
  public void setLastUpdateTime(String time) {
    tv_last_update_time.setText("Last updated" + " " + time);
  }

  @Override
  public void updateAddress(String address) {
    tv_address.setText(address);
    if (status != null && status.equals("ACTIVE")) {
      Date intDate = new Date(System.currentTimeMillis());
      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa"); //this format changeable
      dateFormatter.setTimeZone(TimeZone.getDefault());
      String lastUpdate_Date = dateFormatter.format(intDate);
      tv_last_update_time.setText("Last update :" + lastUpdate_Date);
    }
  }

  @Override
  public void loadAddress(double latitude, double longitude) {
    try {
      JSONObject address=new JSONObject();
      address.put("lat",latitude);
      address.put("lon",longitude);

      ApiRequests.getInstance().get_address(GlobalReferences.getInstance().baseActivity, SingleCarFragment.this, address);
    }catch (JSONException ex){

    }
   if (CommonPreference.getInstance().getsetWhats3Words()){
     new FetchWhatsThreeWords().execute(latitude,longitude);
   }




  }

  public void fetchStatusData(){
    if (NetworkUtils.isConnected()) {
      ApiRequests.getInstance().get_status(GlobalReferences.getInstance().baseActivity,SingleCarFragment.this);
    } else {

      ToastUtils.showShort(R.string.no_internet);
    }
  }
  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mMapView.onLowMemory();
  }
  private void sendSos(JSONObject data){
    try {

      JSONObject object_setting = data.getJSONObject("msg");

      SmsManager sms = SmsManager.getDefault();

      String message = "HELP! My car is stuck. Its location is " + "http://maps.google.com/?q=" + lastLatitude + "," + lastLongitude;

      if (object_setting.has("sos_1")) {
        JSONArray array_sos = object_setting.getJSONArray("sos_1");
        SettingPreferences.getInstance().setSOSArray(array_sos.toString());
        int length = array_sos.length();
        MsgReceiver.registerReceiver(GlobalReferences.getInstance().baseActivity);
        if (length > 0) {
          for (int i = 0; i < length; i++) {
            JSONObject object = array_sos.getJSONObject(i);
            if (object.has("name")) {
              String number = object.getString("number");
              if (!number.equals("null")) {


                if (number != null) {
                  try {
                    sms.sendTextMessage(number, null, message, sentPending, deliveredPending);
                    //Toast.makeText(getActivity(), "SMS sent on "+number,Toast.LENGTH_LONG).show();
                  } catch (Exception ex) {
                    // Toast.makeText(getActivity(),"SMS failed on "+number+" please try again.",Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                  }
                }
              }
            }
          }
        } else {
          Toast.makeText(getActivity(), "There is no SOS number stored", Toast.LENGTH_SHORT).show();
        }
      }


    } catch (JSONException e) {
      e.printStackTrace();
    }
  }


    @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
      progressBar_dataloading.setVisibility(View.GONE);
      JSONObject data=listener.getJsonResponse();


    Log.e(TAG, "onRequestCompleted: "+data );
     if(listener.getTag().equals("status")){
       presenter.parseData(data,registration);
     }else if(listener.getTag().equals("address")){

       Log.e(TAG, "onRequestCompleted: "+data );
       presenter.parseAddress(data);
     }else if (listener.getTag().equals("pitstop")){
       presenter.parseLocation(listener.getJsonResponse());

     }else if (listener.getTag().equals("get_setting")){
       sendSos(listener.getJsonResponse());

     }

  }




  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {

//    Toast.makeText(getActivity(),"Error get"+listener.getVolleyError(),Toast.LENGTH_SHORT).show();


//    if (getActivity() instanceof BaseActivity){
//      ((BaseActivity) getActivity()).doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
//    }

  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    if(listener.getTag().equals("address")){

      Log.e(TAG, "onRequestCompleted: "+listener.getJsonResponse() );
      presenter.parseAddress(listener.getJsonResponse());
    }
  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

//    if (getActivity() instanceof BaseActivity){
//      Toast.makeText(getActivity(),"Error post"+listener.getVolleyError()  ,Toast.LENGTH_SHORT).show();
//      ((BaseActivity) getActivity()).doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
//
//    }
  }

  @Override
  public void onResponse(JSONObject response) {

  }


  @Override
  public void setData(final JSONObject data,final String topic) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        presenter.parseRealtimeData(topic,data);

        Log.d("Mqtt data ",data.toString());



      }
    });

   // Log.e(TAG, "setData: "+topic+"  "+data );
  }

  @Override
  public void setTripData(String status, double avgSpeed, String time, double distance) {
    tv_time.setText(time);
   String avg_speed= String.format("%.0f", avgSpeed);
    tv_avgspeed.setText("" + avg_speed + " km/h");
    tv_status.setText("" + status);
    tv_distance.setText("" + distance + " km");

  }

  @Override
  public void setRealtimeData(int coolant, int rpm, int speed, String voltage,String status) {

    tv_coolant_realtime.setText(coolant + " °C");
    tv_battery_realtime.setText(voltage + " V");
    tv_speed.setText("" + speed + " km/h");

  }



  @Override
  public void updateRealtimeLocation(double latitude, double longitude) {
    imageView_car.setVisibility(View.VISIBLE);
    if (gmap!=null) {
      gmap.clear();

      float zoom=gmap.getCameraPosition().zoom;

      if (zoom <12){
        zoom=16.0f;
      }

      CameraPosition cameraPosition = new CameraPosition.Builder()
          .target(new LatLng(latitude, longitude))
          .zoom(zoom)
          .bearing(0)
          .build();
      gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
  }
  @Override
  public void updateNerdMode(JSONObject jsonObject) {
    if(dialog_nerd_mode!=null){
      dialog_nerd_mode.upDateView(jsonObject);
    }
  }


  @Override
  public void onClick(View v) {
    switch(v.getId()){
      case R.id.ll_triphistory:
        Intent history=new Intent(GlobalReferences.getInstance().baseActivity,HistoryActivity.class);
        startActivity(history);
        break;
      case R.id.ll_alert:
        Intent alert=new Intent(GlobalReferences.getInstance().baseActivity,AlertActivity.class);
        startActivity(alert);
        break;
      case R.id.ll_enginescan:
        Intent scan=new Intent(GlobalReferences.getInstance().baseActivity,EngineScanActivity.class);
        startActivity(scan);
        break;
      case R.id.ll_pitstop:
        String carId=CommonPreference.getInstance().getCarId();
        ApiRequests.getInstance().get_location(GlobalReferences.getInstance().baseActivity,SingleCarFragment.this,carId);
        break;

      case R.id.ll_analytics:

        final String id = CommonPreference.getInstance().getCarId();
        if (NetworkUtils.isConnected()) {

          int count = CommonPreference.getInstance().getDeviceCount();
          if (count == 1) {
            boolean isActivated = CommonPreference.getInstance().getDeviceActivated();
            if (isActivated) {

              Intent intent=new Intent(getActivity(),AnalyticActivity.class);
              intent.putExtra("carId",id);
              startActivity(intent);


            } else {
              Toast.makeText(getActivity(), "Please activate the device to access this feature", Toast.LENGTH_SHORT).show();
            }
          } else {
            boolean isActivated = CommonPreference.getInstance().getThisDeviceLinked();
            if (isActivated) {
              Intent intent=new Intent(getActivity(),AnalyticActivity.class);
              intent.putExtra("carId",id);
              startActivity(intent);
            } else {
              ToastUtils.showShort("Please activate the device to access this feature");
            }
          }
        }else{

          ToastUtils.showShort("Please check your internet connection");

        }
        break;

      case R.id.iv_takemeonlocation:
        v.startAnimation(buttonClick);
        UtilityMethod.onShare(GlobalReferences.getInstance().baseActivity,lastLatitude,lastLongitude);
        break;
      case R.id.iv_parkcarlocator:
        v.startAnimation(buttonClick);

        if (lastLatitude != 0.0) {
          try {
            String url = "http://maps.google.com/maps?f=d&daddr=" + lastLatitude + "," + lastLongitude + "&dirflg=d";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
          }catch (ActivityNotFoundException ex){

          }

        }
        break;

      case R.id.iv_sos:
        isSosClicked=true;
        if (PermissionUtils.isGranted(getPermissions(PermissionConstants.SMS))){
          getSos();
        }else{
          PermissionUtils.permission(PermissionConstants.SMS);
          PermissionUtils.sInstance.callback(this);
          PermissionUtils.sInstance.request();
        }


        break;
      case R.id.iv_dashcam:
        showDialogForDashcam();
        break;

    }

  }


  private void showDialogForDashcam() {
    if(Build.VERSION.SDK_INT <21){

      new AwesomeWarningDialog(getActivity())
          .setTitle("Dashcam")
          .setMessage("This feature can be accessed only with Android Lollipop")
          .setColoredCircle(R.color.dialogNoticeBackgroundColor)
          .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
          .setCancelable(true)
          .setButtonText("Ok")
          .setButtonBackgroundColor(R.color.dialogNoticeBackgroundColor)
          .setButtonText(getString(R.string.dialog_ok_button))
          .setWarningButtonClick(new Closure() {
            @Override
            public void exec() {
              // click
            }
          })
          .show();

    } else if(Build.VERSION.SDK_INT == 21||Build.VERSION.SDK_INT ==22){
      new AwesomeWarningDialog(getActivity())
          .setTitle("Dashcam")
          .setMessage("Please DO NOT check 'Dont't show again' or 'Cancel' check box on the permission dialog which you will see when recording starts to avoid System UI crash. \n If you already have, remove and re-install the application.\n" +
              "This is a bug in Android 5.1 and can not be fixed by us.")
          .setColoredCircle(R.color.colorAccent)
          .setDialogIconAndColor(R.drawable.ic_notice, R.color.white)
          .setCancelable(true)
          .setButtonText("Try it")
          .setButtonBackgroundColor(R.color.colorAccent)
          .setButtonText(getString(R.string.dialog_ok_button))
          .setWarningButtonClick(new Closure() {
            @Override
            public void exec() {
              // click
              if (Build.VERSION.SDK_INT > 21) {
//                                Intent intent = new Intent(MainActivity.this, Dashcam.class);
//                                startActivity(intent);
                checkDashcamPermission();


              } else {
                Toast.makeText(getActivity(), "This feature can be accessed only with Android Lollipop ", Toast.LENGTH_SHORT).show();

              }
            }
          })

          .show();

    }else {
      checkDashcamPermission();
    }

  }
  private void checkDashcamPermission() {

    if (ActivityCompat.checkSelfPermission(getActivity(),android. Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED ) {



      openDashcam();

    } else
      ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_PERMISSION);

  }
  private void openDashcam(){
    startActivity(new Intent(getActivity(),RecordVideoActivity.class));
  }


  private void getSos(){
    String carId=CommonPreference.getInstance().getCarId();
    ApiRequests.getInstance().get_setting(GlobalReferences.getInstance().baseActivity,SingleCarFragment.this,carId);
  }


  @Override
  public void navigateToPitstop(double latitude, double longitude) {
    Intent intent = new Intent(getActivity(), PitstopActivity.class);
    intent.putExtra("latitude", lastLatitude);
    intent.putExtra("longitude", lastLongitude);
    startActivity(intent);
  }



  private final class FetchWhatsThreeWords extends AsyncTask<Double, Void, String> {


    @Override
    protected String doInBackground(Double... doubles) {
      ConvertTo3WA words=null;
      String wordsAddress="";
      if (what3WordsV3 != null) {

         words = what3WordsV3.convertTo3wa(new Coordinates(doubles[0], doubles[1]))
                .language("en")
                .execute();


        Log.e(TAG, "loadAddress:words " + words);

        mapAddress = words.getMap();
        wordsAddress=words.getWords();
      }

      return wordsAddress;
    }

    @Override
    protected void onPostExecute(String result) {
      tvThreeWords.setText(result);

      // You might want to change "executed" for the returned string
      // passed into onPostExecute(), but that is up to you
    }
  }



}



