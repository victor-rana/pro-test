package blackflame.com.zymepro.ui.livetrip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.mqtt.MqttHandler;
import blackflame.com.zymepro.ui.home.MqttDataListener;
import blackflame.com.zymepro.ui.livetrip.presenter.LiveTripPresenter;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import blackflame.com.zymepro.util.UtilityMethod;

import static blackflame.com.zymepro.util.MarkerUtil.getEndCapIcon;

public class LiveTripActivity extends BaseActivity implements LiveTripPresenter.View,GoogleMap.OnMapClickListener, MqttDataListener, GoogleMap.OnMarkerClickListener, AppRequest {

    /**
     * Whether or not the system UI should be auto-hidden after {@link #AUTO_HIDE_DELAY_MILLIS}
     * milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after user interaction before
     * hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates and a change of the status and
     * navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private View mControlsView;

    SupportMapFragment fm;
    GoogleMap Gmap;
    MapView mMapView;
    boolean isMapReady,ispathAdded=false;

    String subscriptionTopic,imei;
    PolylineOptions polylineOptions;
    Polyline line;
    double latitude,longitude;;
    BitmapDescriptor endCapIcon;
    ImageView iv_back;

    LiveTripPresenter presenter;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_live_trip);

        presenter=new LiveTripPresenter(this,new LiveTripInteractor());

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.route_map);


        endCapIcon = getEndCapIcon(LiveTripActivity.this, getResources().getColor(R.color.colorAccent));
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        subscriptionTopic= CommonPreference.getInstance().getSubscriptionTopic();

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        imei=getIntent().getStringExtra("imei");

        polylineOptions = new PolylineOptions().width(10).color(getResources().getColor(R.color.colorAccent)).geodesic(true);
        mMapView = findViewById(R.id.route_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            fetchRouteData();
            MapsInitializer.initialize(Prosingleton.getAppContext());
        } catch (Exception e) {
            e.printStackTrace();
            // Log.e(TAG, "onCreateView:mapInitLizer "+e.getMessage() );
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {


                registerMqtt(getIntent().getStringExtra("imei"));

                Gmap = mMap;
                isMapReady = true;
                Gmap.getUiSettings().setScrollGesturesEnabled(true);
                Gmap.getUiSettings().setTiltGesturesEnabled(true);
                Gmap.setOnMapClickListener(LiveTripActivity.this);

                Gmap.getUiSettings().setCompassEnabled(true);

                Gmap.getUiSettings().setZoomGesturesEnabled(true);
                Gmap.getUiSettings().setRotateGesturesEnabled(true);
                Gmap.setOnMarkerClickListener(LiveTripActivity.this);

                if (CommonPreference.getInstance().getLiveTraffic()) {
                    Gmap.setTrafficEnabled(true);
                }else{
                    Gmap.setTrafficEnabled(false);
                }

                try{

                    String map_type = CommonPreference.getInstance().getMapType();
                    if (Gmap != null) {
                        int id= UtilityMethod.getStyle(CommonPreference.getInstance().getMapType());

                        if (map_type == null) {
                            Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            boolean success = Gmap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(LiveTripActivity.this, id));
                            if (!success) {
                                // Handle map style load failure
                            }

                        } else if (map_type.equals("NORMAL")) {
                            Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            boolean success = Gmap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(LiveTripActivity.this, id));
                            if (!success) {
                                // Handle map style load failure
                            }
                        } else if (map_type.equals("NIGHT")) {
                            Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            boolean success = Gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(LiveTripActivity.this,id));
                            if (!success) {
                                // Handle map style load failure
                            }

                        } else if (map_type.equals("SATELLITE")) {
                            Gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                        } else {
                            Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            boolean success = Gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(LiveTripActivity.this,id));
                            if (!success) {
                                // Handle map style load failure
                            }
                        }
                    }
                } catch(Resources.NotFoundException e){

                }

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(24.8937, 78.9629))
                        .zoom(12)
                        .bearing(0)
                        .build();
                if (Gmap != null) {
                    Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }




                Gmap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        final Context context = LiveTripActivity.this; //or getActivity(), YourActivity.this, etc.

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

                        info.addView(title);
                        info.addView(snippet);

                        return info;
                    }
                });
                /// For zooming automatically to the location of the marker
            }
        });




    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }



    public void fetchRouteData(){
        if (NetworkUtils.isConnected()) {
            ApiRequests.getInstance().get_live_trip_path(GlobalReferences.getInstance().baseActivity, this,CommonPreference.getInstance().getCarId());
        } else {

            ToastUtils.showShort(R.string.no_internet);
        }
    }








    @Override
    public void onMapClick(LatLng latLng) {



    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> listener, Constants.RequestParam requestParam) {

    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> listener, Constants.RequestParam requestParam) {

        JSONObject data=listener.getJsonResponse();

        if(listener.getTag().equals("live_trip")){
            try {
                presenter.parseTripData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> listener, Constants.RequestParam requestParam) {
        doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
    }

    @Override
    public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {

    }

    @Override
    public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {

    }

    @Override
    public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, Constants.RequestParam requestParam) {
        doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void indexScreen() {
        Analytics.index(LiveTripActivity.this,"LiveTripActivity");

    }

    @Override
    public void registerMqtt(String imei) {

        MqttHandler.registerMqtt(imei,this);

    }

    @Override
    public void setPolyLines(JSONArray trip_path) {



        try{

            for (int i=0;i<trip_path.length();i++){

                JSONArray location=trip_path.getJSONArray(i);
                polylineOptions.add(new LatLng(location.getDouble(0),location.getDouble(1)));
            }


            ispathAdded=true;



            if (Gmap !=null && polylineOptions !=null){
                int lineColor = getResources().getColor(R.color.colorAccent);


                line=Gmap.addPolyline(polylineOptions);
                line.setJointType(JointType.ROUND);
                line.setStartCap(
                        new CustomCap(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_start), 10));
                BitmapDescriptor endCapIcon = getEndCapIcon(LiveTripActivity.this,lineColor);

                line.setEndCap(new CustomCap(endCapIcon,8));

            }

        }catch (JSONException ex){
            Log.e("TAG", "onPathData: "+ex.getMessage() );


        }

    }

    @Override
    public void setData(JSONObject jsonObject, String topic) {




        try {
            // int deviceId=jsonObject.getInt("device_id");
            final int coolant = jsonObject.getInt("coolant");
            final String status= jsonObject.getString("status");


            JSONArray array = jsonObject.getJSONArray("coordinates");

            int length = array.length();
            latitude = array.getDouble(0);
            longitude = array.getDouble(1);
            if (ispathAdded) {

                polylineOptions.add(new LatLng(latitude, longitude));
            }


            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (Gmap!=null && line != null) {
                        line.remove();
                        line=Gmap.addPolyline(polylineOptions);
                        line.setStartCap(
                                new CustomCap(
                                        BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_start), 10));

                        if (status != null && (status.equals("PARKED") || status.equals("HALT"))){
                            line.setEndCap( new CustomCap(
                                    BitmapDescriptorFactory.fromResource(R.drawable.ic_trip_end), 10));
                        }else{
                            line.setEndCap(new CustomCap(endCapIcon,8));
                        }

                        line.setZIndex(10.0f);

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(latitude, longitude))
                                .zoom(15)
                                .bearing(0)
                                .build();
                        Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }
            });





        } catch (JSONException e) {
//            Log.e("MainActivity", "broadcastMessage: "+e.getMessage() );
        }



    }
}
