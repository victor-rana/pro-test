package blackflame.com.zymepro.ui.home.singlecar;

import static blackflame.com.zymepro.R.id.car_selection_bg_opacity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import blackflame.com.zymepro.ui.activation.ActivityActivation;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.R;
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
import blackflame.com.zymepro.ui.home.MqttDataListener;
import blackflame.com.zymepro.ui.home.multicar.MulticarFragment;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.PermissionUtils;
import blackflame.com.zymepro.util.PermissionUtils.SimpleCallback;
import blackflame.com.zymepro.util.ToastUtils;
import blackflame.com.zymepro.util.UtilityMethod;
import blackflame.com.zymepro.view.custom.RippleBackground;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import org.json.JSONObject;

public class SingleCarFragment extends CommonFragment implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback,SimpleCallback,SinglePresenter.View ,AppRequest ,MqttDataListener {


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


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_singlecar, container, false);
    mMapView = view.findViewById(R.id.map);
    mMapView.onCreate(savedInstanceState);
    initViews(view);
    presenter=new SinglePresenter(this,new SingleInteractor());
    presenter.handleBundle(getArguments());
    return view;
  }
  public void initViews(View view){
    linearLayout_bottom = view.findViewById(R.id.contaier_bottom);
    pitstop = view.findViewById(R.id.ll_pitstop);
    enginescan = view.findViewById(R.id.ll_enginescan);
    history = view.findViewById(R.id.ll_triphistory);
    alert = view.findViewById(R.id.ll_alert);
    analytics=view.findViewById(R.id.ll_analytics);
    setRipple(enginescan);
    setRipple(history);
    setRipple(alert);
    setRipple(analytics);
    setRipple(pitstop);
    view_bg_opacity = view.findViewById(car_selection_bg_opacity);
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
    linearLayout_group = view.findViewById(R.id.layout_container_group);
    imageView_zoomin = view.findViewById(R.id.zoom_in);
    imageView_zoomout = view.findViewById(R.id.zoom_out);
    linearLayout_sos = view.findViewById(R.id.iv_sos);
    linearLayout_carllocation = view.findViewById(R.id.iv_parkcarlocator);
    linearLayout_sharelocation = view.findViewById(R.id.iv_takemeonlocation);
    cardView_parked = view.findViewById(R.id.containertop_textview);
    iv_more = view.findViewById(R.id.iv_more);
    cardView_parked.setClickable(false);
    cardView_active = view.findViewById(R.id.containertop);
    iv_dashcam= view.findViewById(R.id.iv_dashcam);
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
    mMapView.onResume();
    fetchStatusData();
    try {
      MapsInitializer.initialize(Prosingleton.getAppContext());
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!PermissionUtils.isGranted(PermissionConstants.LOCATION)&& !PermissionUtils.isGranted(PermissionConstants.CONTACTS)){
      PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.LOCATION));
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

        try{

          String map_type = CommonPreference.getInstance().getMapType();
          if (gmap != null) {
            int id=UtilityMethod.getStyle(CommonPreference.getInstance().getMapType());

            if (map_type == null) {
              gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
              boolean success = gmap.setMapStyle(
                  MapStyleOptions.loadRawResourceStyle(getActivity(), id));
              if (!success) {
                // Handle map style load failure
              }

            } else if (map_type.equals("NORMAL")) {
              gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
              boolean success = gmap.setMapStyle(
                  MapStyleOptions.loadRawResourceStyle(getActivity(), id));
              if (!success) {
                // Handle map style load failure
              }
            } else if (map_type.equals("NIGHT")) {
              gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
              boolean success = gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),id));
              if (!success) {
                // Handle map style load failure
              }

            } else if (map_type.equals("SATELLITE")) {
              gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            } else {
              gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
              boolean success = gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),id));
              if (!success) {
                // Handle map style load failure
              }
            }
          }
        } catch(Resources.NotFoundException e){

        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(24.8937, 78.9629))
            .zoom(4)
            .bearing(0)
            .build();
        if (gmap != null) {
          gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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
    onAlert();
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
          GlobalReferences.getInstance().baseActivity.addFragmentWithBackStack(multicar,false,null);
        } else {
          onDetach();
          onDestroyView();
          listview_carCount.setVisibility(View.GONE);
          SingleCarFragment singleCar = new SingleCarFragment();
          Bundle bundle = new Bundle();
          bundle.putString("IMEI", list.get(position).getImei());
          bundle.putString("registration_number", list.get(position).getRegistration());
          bundle.putInt("coming", 2);
          GlobalReferences.getInstance().baseActivity.addFragmentWithBackStack(singleCar,false,bundle);
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

      String map_type = CommonPreference.getInstance().getMapType();
      if (gmap != null) {
        int id=UtilityMethod.getStyle(CommonPreference.getInstance().getMapType());

        if (map_type == null) {
          gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          boolean success = gmap.setMapStyle(
              MapStyleOptions.loadRawResourceStyle(getActivity(), id));
          if (!success) {
            // Handle map style load failure
          }

        } else if (map_type.equals("NORMAL")) {
          gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          boolean success = gmap.setMapStyle(
              MapStyleOptions.loadRawResourceStyle(getActivity(), id));
          if (!success) {
            // Handle map style load failure
          }
        } else if (map_type.equals("NIGHT")) {
          gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          boolean success = gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),id));
          if (!success) {
            // Handle map style load failure
          }

        } else if (map_type.equals("SATELLITE")) {
          gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        } else {
          gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          boolean success = gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),id));
          if (!success) {
            // Handle map style load failure
          }
        }
      }
    } catch(Resources.NotFoundException e){
      // Log.e(TAG, "onResume: "+e.getMessage() );
      // Oops, looks like the map style resource couldn't be found!
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
    Log.e(TAG, "onGranted: "+"Granted" );
    //fetchStatusData();
  }

  @Override
  public void onDenied() {

  }


  @Override
  public void onPause() {
    super.onPause();
    mMapView.onPause();
  }

  private void onAlert() {
    alert.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        Intent intent = new Intent(getActivity(), AlertActivity.class);
//        intent.putExtra("coming",0);
//        startActivity(intent);
//        screenName="Alert Activity";
//        //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
//        getActivity().overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
      }
    });
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
    Log.e(TAG, "setActivationCard: "+status );
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
    MqttHandler.registerMqtt(imei,this);
    Log.e(TAG, "registerMqtt: call" );

  }

  @Override
  public void navigateToRegistration() {
//    Intent intent = new Intent(getActivity(), CarInfoActivity.class);
//    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
//    startActivity(intent);
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
    ApiRequests.getInstance().get_address(GlobalReferences.getInstance().baseActivity,SingleCarFragment.this,latitude,longitude);

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


    @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
      progressBar_dataloading.setVisibility(View.GONE);
      JSONObject data=listener.getJsonResponse();

     if(listener.getTag().equals("status")){
       presenter.parseData(data);
     }else if(listener.getTag().equals("address")){
       presenter.parseAddress(data);
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


  @Override
  public void setData(final JSONObject data,final String topic) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        presenter.parseRealtimeData(topic,data);
      }
    });

    Log.e(TAG, "setData: "+topic+"  "+data );
  }

  @Override
  public void setTripData(String status, double avgSpeed, String time, double distance) {
    tv_time.setText(time);
    tv_avgspeed.setText("" + avgSpeed + " km/h");
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
      CameraPosition cameraPosition = new CameraPosition.Builder()
          .target(new LatLng(latitude, longitude))
          .zoom(gmap.getCameraPosition().zoom)
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


}



