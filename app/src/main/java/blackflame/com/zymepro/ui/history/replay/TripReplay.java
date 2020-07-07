package blackflame.com.zymepro.ui.history.replay;

import static blackflame.com.zymepro.util.GMapUtil.getBearing;
import static blackflame.com.zymepro.util.UtilityMethod.getDate;
import static blackflame.com.zymepro.util.UtilityMethod.getDisplayTime;
import static blackflame.com.zymepro.util.UtilityMethod.getTime;
import static blackflame.com.zymepro.util.UtilityMethod.resizeMapIcons;
import static com.google.android.gms.maps.model.JointType.ROUND;

import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import blackflame.com.zymepro.ui.history.replay.model.ReplayData;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.TimeUtils;
import blackflame.com.zymepro.util.UtilityMethod;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TripReplay extends BaseActivity implements OnClickListener,AppRequest {
  MapView mMapView;
  GoogleMap Gmap;
  boolean isMapReady;
  final int REQ_PERMISSION = 12;

  String date;
  int trip_id;
  JSONArray array_tripPath;
  List<LatLng> polyLineList;
  private PolylineOptions polylineOptions;
  private Polyline blackPolyline, greyPolyLine;
  private double lat, lng;
  private Handler handler;
  private LatLng startPosition, endPosition;
  private int index, next;
  private Marker marker;
  private float v;
  JSONArray array_startLocation;
  JSONArray array_endLocation;
  int play_time=2000;
  boolean isPause=false;
  ImageView pause,play;
  SeekBar seekbar;
  String start_address,end_address;
  Toolbar toolbar_showHistory;
  TextView textView_title,elapsed_time;
  int previous_index=0;
  TextView tv_trip_duration;
  TextView tv_replay_start_time,tv_replay_end_time;
  ArrayList<ReplayData> list_path;
  SimpleDateFormat sdf;
  Date date_start;
  Date date_current;

  View viewInfoWindow;
  CustomInfoWindowGoogleMap customInfoWindowGoogleMap;

  TextView tvRpm,tvSpeed,tvVoltage,tvCoolant,tvTime;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_trip_replay);
    GlobalReferences.getInstance().baseActivity=this;
    mMapView = findViewById(R.id.map_trip_details);
    mMapView.onCreate(savedInstanceState);
    mMapView.onResume();
    initViews();

  }


  private void getData(String date,String tripId){
    String carId=CommonPreference.getInstance().getCarId();
    ApiRequests.getInstance().get_replay(GlobalReferences.getInstance().baseActivity,TripReplay.this,carId,date,tripId);

  }

  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Route Replay");
    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    tv_trip_duration=findViewById(R.id.tv_trip_duration);
    tv_replay_end_time=findViewById(R.id.tv_detail_trip_end_time);
    tv_replay_start_time=findViewById(R.id.tv_detail_start_time);


    tvRpm=findViewById(R.id.tvRpm);
    tvCoolant=findViewById(R.id.tvCoolant);
    tvSpeed=findViewById(R.id.tvSpeed);
    tvVoltage=findViewById(R.id.tvVoltage);
    tvTime=findViewById(R.id.tvTime);

    TextView time_increase=findViewById(R.id.iv_increase_time);
    TextView time_decrease=findViewById(R.id.iv_reduce_time);
    TextView tv_start_address=findViewById(R.id.tv_detail_start_address);
    TextView tv_end_address=findViewById(R.id.tv_detail_end_address);
    elapsed_time=findViewById(R.id.tv_zero_time);
    tv_start_address.setText(start_address);
    tv_end_address.setText(end_address);
    pause=findViewById(R.id.iv_pause);
    pause.setOnClickListener(this);

    play=findViewById(R.id.iv_play);

    play.setOnClickListener(this);
    time_decrease.setOnClickListener(this);
    time_increase.setOnClickListener(this);
    list_path=new ArrayList<>();
    date=getIntent().getStringExtra("date");
    trip_id=getIntent().getIntExtra("trip_id",-1);
    start_address=getIntent().getStringExtra("start_address");
    end_address=getIntent().getStringExtra("end_address");

    tv_start_address.setText(start_address);
    tv_end_address.setText(end_address);



    polyLineList= new ArrayList<>();
    seekbar = findViewById(R.id.seekBar);
    seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try {

          int i = progress;

          index = progress;



          if (list_path!=null && progress==list_path.size()-1){


            if (date_start==null && list_path !=null && list_path.size()>0){
              date_start=getCurrentDate(list_path.get(0).getTime());

            }

            date_current=getCurrentDate(list_path.get(index).getTime());

            getDifference(date_start,date_current);


            isPause=false;
            handler.removeCallbacks(runnable);
            seekbar.setProgress(polyLineList.size()-1);
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
            index=0;
          }else

          if (progress<polyLineList.size()) {
            LatLng newPos = polyLineList.get(index);


            if (marker == null) {
              marker = Gmap.addMarker(new MarkerOptions().position(new LatLng(array_startLocation.getDouble(0), array_startLocation.getDouble(1)))
                  .flat(true)
                  .icon(BitmapDescriptorFactory
                      .fromBitmap(resizeMapIcons("car_replay",112,112))));

              marker.setAnchor(0.5f, 0.5f);
            } else {
              marker.setPosition(newPos);
              marker.setAnchor(0.5f, 0.5f);

            }


            // Log.e(TAG, "onAnimationUpdate:new Position " + newPos);


            Gmap.moveCamera(CameraUpdateFactory
                .newCameraPosition
                    (new CameraPosition.Builder()
                        .target(newPos)
                        .zoom(15f)
                        .build()));

            if (fromUser) {


              play.setVisibility(View.VISIBLE);
              pause.setVisibility(View.GONE);
            }


          }
        }catch (JSONException ex){}
//
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

        isPause=true;

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        if (list_path!=null && seekBar.getProgress()<list_path.size()-1) {
          if (date_start == null  && list_path.size() > 0) {
            date_start = getCurrentDate(list_path.get(0).getTime());
          }

          date_current = getCurrentDate(list_path.get(index).getTime());
          getDifference(date_start, date_current);


        }
      }
    });
    mMapView.getMapAsync(new OnMapReadyCallback() {
      public void onMapReady(GoogleMap mMap) {
        Gmap = mMap;
        isMapReady = true;
        Gmap.getUiSettings().setScrollGesturesEnabled(true);
        Gmap.getUiSettings().setTiltGesturesEnabled(true);

        Gmap.getUiSettings().setScrollGesturesEnabled(true);
        Gmap.getUiSettings().setCompassEnabled(true);
        Gmap.getUiSettings().setScrollGesturesEnabled(true);
        Gmap.getUiSettings().setZoomGesturesEnabled(true);
        Gmap.getUiSettings().setRotateGesturesEnabled(true);
        Gmap.getUiSettings().setZoomControlsEnabled(true);

//       customInfoWindowGoogleMap=new CustomInfoWindowGoogleMap(TripReplay.this);
//
//
//        Gmap.setInfoWindowAdapter(customInfoWindowGoogleMap);


        try {
          boolean success = Gmap.setMapStyle(
              MapStyleOptions.loadRawResourceStyle(TripReplay.this, R.raw.style_map));
          if (!success) {
            // Handle map style load failure
          }
        } catch (Resources.NotFoundException e) {
          // Oops, looks like the map style resource couldn't be found!
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(24.8937, 78.9629))
            .zoom(4)
            .bearing(0)
            .build();
        Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


      }
    });
    handler = new Handler();
    getData(date,String.valueOf(trip_id));
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.iv_play:
        //  myRoutePlayer.togglePlayPause();

        pause.setVisibility(View.VISIBLE);
        play.setVisibility(View.GONE);
        if (isPause){
          isPause=false;
          handler.removeCallbacks(runnable);

          handler.postDelayed(runnable,play_time);




        }else{

          if (runnable!=null&& handler!=null) {
            handler.removeCallbacks(runnable);
          }


          playPath();
          play_time=3000;


        }


        break;

      case R.id.iv_pause:{
        pause.setVisibility(View.GONE);
        play.setVisibility(View.VISIBLE);

        isPause=true;
      }
      break;
      case R.id.iv_increase_time:
        if (play_time>=1000){
          play_time-=500;

        }
        //  Log.e(TAG, "onClick: +playtime"+play_time );


        break;
      case R.id.iv_reduce_time:



        if (play_time<=9500){
          play_time+=500;
        }

        // Log.e(TAG, "onClick: +playtime"+play_time );
        break;

    }
  }


  Runnable runnable=new Runnable() {
    @Override
    public void run() {



      if (!isPause) {

        if (index == list_path.size()-1) {

          handler.removeCallbacks(this);
          return;
        }else if (index < list_path.size() - 2 ) {
          index++;
          next = index + 1;
          startPosition = list_path.get(index).getPosition();
          endPosition = list_path.get(next).getPosition();
        }


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(play_time);
        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator valueAnimator) {
            // Log.e(TAG, "run:onAnimationUpdate "+"call" );
            try {
              previous_index = index;

              if (index==0){
                date_start=sdf.parse(list_path.get(index).getTime());
              }
              date_current=sdf.parse(list_path.get(index).getTime());
              if (date_start!=null){
                getDifference(date_start,date_current);
              }
              if (index == list_path.size() - 1 || index == list_path.size()) {
                isPause = false;
                handler.removeCallbacks(runnable);
                seekbar.setProgress(list_path.size() - 1);
                date_current=getCurrentDate(list_path.get(list_path.size()-1).getTime());
                getDifference(date_start,date_current);
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);

                index = 0;
                return;
              } else {
                seekbar.setProgress(index);
                v = valueAnimator.getAnimatedFraction();
                lng = v * endPosition.longitude + (1 - v)
                    * startPosition.longitude;
                lat = v * endPosition.latitude + (1 - v)
                    * startPosition.latitude;
                LatLng newPos = new LatLng(lat, lng);

                if (marker != null && marker.isVisible()) {


                  marker.setAnchor(0.5f, 0.5f);
                  marker.setRotation(getBearing(startPosition, endPosition));
                  marker.setPosition(newPos);
//                  marker.setTag(list_path.get(index));
//                  marker.showInfoWindow();

//                  viewInfoWindow=customInfoWindowGoogleMap.getInfoWindow(marker);
                  updateInfoWindow(list_path.get(index));




                  // Log.e(TAG, "onAnimationUpdate: marker position set previous" );
                } else {
                  if (index < list_path.size() - 1)
                    marker = Gmap.addMarker(new MarkerOptions().position(list_path.get(index).getPosition())
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("car_replay", 112, 112)))
                    );

//                  marker.setTag(list_path.get(index));
//                  marker.showInfoWindow();
//                  viewInfoWindow=customInfoWindowGoogleMap.getInfoWindow(marker);
                  updateInfoWindow(list_path.get(index));

                  date_start = getCurrentDate(list_path.get(index).getTime());



                  // Log.e(TAG, "onAnimationUpdate: marker position set new" );
                }
                Gmap.moveCamera(CameraUpdateFactory
                    .newCameraPosition
                        (new CameraPosition.Builder()
                            .target(newPos)
                            .zoom(16f)
                            .build()));
              }


            }catch (Exception ex){


            }
          }
          //}
        });


        valueAnimator.start();
        handler.postDelayed(this, play_time);
      }else{
        handler.postDelayed(this, play_time);
      }
    }
  };
  @Override
  protected void onPause() {

    if (handler!=null && runnable !=null){
      handler.removeCallbacks(runnable);
    }
    super.onPause();
  }
  private  void playPath(){
    try {

      if (index==0) {
        index = -1;
        next = 1;
        if (marker==null &&array_startLocation!=null &&array_startLocation.length()>0 ){
          marker = Gmap.addMarker(new MarkerOptions().position(new LatLng(array_startLocation.getDouble(0), array_startLocation.getDouble(1)))
              .flat(true)
              .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("car_replay",112,112))));

        }
      }else{
        if (marker ==null && index < list_path.size() - 1){
          marker = Gmap.addMarker(new MarkerOptions().position(list_path.get(index).getPosition())
              .flat(true)
              .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("car_replay",112,112))));

        }else{
          if (index <list_path.size()-1) {
            marker.setPosition(list_path.get(index).getPosition());
          }

        }

      }

      handler.postDelayed(runnable,play_time);

    }catch (JSONException ex){

    }
  }


  public void getDifference(Date startDate, Date endDate) {
    //milliseconds
    long different = (endDate.getTime() - startDate.getTime())/1000;
    elapsed_time.setText(String.format("%02d:%02d", different / 60, different % 60));

  }

  private Date getCurrentDate(String date) {
    Date current_date=null;
    try{
      current_date= sdf.parse(date);
    }catch (ParseException ex){

    }
    return  current_date;
  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    try {
      Log.e("TripHistoryDetails", "onResponse: "+listener.getJsonResponse() );
      JSONObject jObject =listener.getJsonResponse(); // json
      JSONObject jsonObject_TripData=jObject.getJSONObject("msg");
      JSONObject tripObject=jsonObject_TripData.getJSONObject("trip_data");
      final String startTime=tripObject.getString("start_time");
      final String endTime=tripObject.getString("end_time");
      final int trip_Time=tripObject.getInt("trip_time");
      array_startLocation=tripObject.getJSONArray("start_location");
      array_endLocation=tripObject.getJSONArray("end_location");
      array_tripPath = jsonObject_TripData .getJSONArray("trip_path");
      int length=array_tripPath.length();
      for (int i=0;i<length;i++){
        JSONObject object_trip_path=array_tripPath.getJSONObject(i);
        ReplayData replayData=new ReplayData();
        replayData.setSpeed(object_trip_path.getString("speed"));
        replayData.setCoolant(object_trip_path.getString("coolant"));
        replayData.setDistance(object_trip_path.getString("distance"));
        replayData.setRpm(object_trip_path.getString("rpm"));
        replayData.setVoltage(object_trip_path.getString("voltage"));



        double lat=object_trip_path.getJSONArray("coordinates").getDouble(0);
        replayData.setLatitude(lat);
        double lng=object_trip_path.getJSONArray("coordinates").getDouble(1);
        replayData.setLongitude(lng);
        replayData.setTime(object_trip_path.getString("time"));
        replayData.setPosition(new LatLng(lat,lng));
        polyLineList.add(new LatLng(lat,lng));
        list_path.add(replayData);


      }

      seekbar.setMax(polyLineList.size()-1);


      LatLngBounds.Builder builder = new LatLngBounds.Builder();
      for (LatLng latLng : polyLineList) {
        builder.include(latLng);
      }


      Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().
          target(new LatLng(array_startLocation.getDouble(0),array_startLocation.getDouble(1)))
          .zoom(15.0f).build()));
      polylineOptions = new PolylineOptions();
      polylineOptions.color(getResources().getColor(R.color.colorAccent));
      polylineOptions.width(10);
      polylineOptions.startCap(new SquareCap());
      polylineOptions.endCap(new SquareCap());
      polylineOptions.jointType(ROUND);
      polylineOptions.addAll(polyLineList);
      greyPolyLine = Gmap.addPolyline(polylineOptions);
      new Handler(getMainLooper()).post(new Runnable() {
        @Override
        public void run() {

          tv_trip_duration.setText(UtilityMethod
              .timeConversion(trip_Time,true));
          tv_replay_end_time.setText(getDisplayTime(endTime));
          tv_replay_start_time.setText(getDisplayTime(startTime));

        }
      });




    } catch (JSONException e) {
      e.printStackTrace();
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
    Analytics.index(TripReplay.this,"TripReplay");
  }


  private void updateInfoWindow(ReplayData replayData){
    if (replayData != null) {

      tvSpeed.setText(replayData.getSpeed());
      tvRpm.setText(replayData.getRpm());
      tvVoltage.setText(replayData.getVoltage());
      tvCoolant.setText(replayData.getCoolant());
      tvTime.setText(TimeUtils.getFormattedDate(replayData.getTime()));
    }
  }
}
