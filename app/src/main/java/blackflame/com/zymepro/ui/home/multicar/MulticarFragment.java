package blackflame.com.zymepro.ui.home.multicar;

import static blackflame.com.zymepro.util.UtilityMethod.resizeMapIcons;
import static com.android.volley.VolleyLog.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.home.MainActivity;
import blackflame.com.zymepro.ui.home.singlecar.CarcountAdapter;
import blackflame.com.zymepro.ui.home.singlecar.CarcountModel;
import blackflame.com.zymepro.ui.home.singlecar.SingleCarFragment;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

public class MulticarFragment extends CommonFragment implements OnMarkerClickListener,OnInfoWindowClickListener,GoogleApiClient.ConnectionCallbacks,AppRequest,OnMapClickListener,MulticarPresenter.View {

  MapView mapView;
  GoogleMap gmap;
  boolean isMapReady;
  final int REQ_PERMISSION = 12;
  RelativeLayout relativeLayout_car_selection;
  TextView textView_car_active,textView_car_inactive,textView_car_address,textView_selected_car;
  ListView listView_multicar;
  ArrayList<CarcountModel> list;
  View view_bg_opacity;
  ImageView imageView_zoomout, imageView_zoomin;
  CarcountAdapter adapter;
  Marker marker;
  int count_active;
  LatLngBounds.Builder builder;
  private static final String BACK_STACK_ROOT_TAG = "root_fragment";
  LinearLayout linearLayout_bottom,linearLayout_topcard;
  MulticarPresenter presenter;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_multicar, container, false);


    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mapView = view.findViewById(R.id.map);
    mapView.onCreate(savedInstanceState);
    presenter=new MulticarPresenter(this);
    builder = new LatLngBounds.Builder();
    initViews(view);

  }


  private void initViews(View view){

    textView_car_active= view.findViewById(R.id.car_active_count);
    textView_car_inactive= view.findViewById(R.id.car_inactive_count);
    textView_car_address= view.findViewById(R.id.car_address);
    textView_selected_car= view.findViewById(R.id.car_multicar_selected);
    relativeLayout_car_selection= view.findViewById(R.id.current_car_multicar);
    listView_multicar= view.findViewById(R.id.listview_car_count_multicar);
    view_bg_opacity=view.findViewById(R.id.car_selection_multicar_bg_opacity);
    imageView_zoomin = view.findViewById(R.id.zoom_in_multicar);
    imageView_zoomout = view.findViewById(R.id.zoom_out_multicar);
    linearLayout_bottom= view.findViewById(R.id.contaier_bottom);
    linearLayout_topcard= view.findViewById(R.id.top_card);
    Log.e(TAG, "MANUFACTURER: "+ Build.MANUFACTURER );
    mapView.onResume();
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(GoogleMap mMap) {
        gmap = mMap;
        isMapReady = true;
        gmap.getUiSettings().setScrollGesturesEnabled(true);
        gmap.getUiSettings().setTiltGesturesEnabled(true);
        gmap.getUiSettings().setScrollGesturesEnabled(true);
        gmap.getUiSettings().setCompassEnabled(true);
        gmap.getUiSettings().setZoomGesturesEnabled(true);
        gmap.getUiSettings().setRotateGesturesEnabled(true);
        gmap.setOnMarkerClickListener(MulticarFragment.this);
        gmap.setOnMapClickListener(MulticarFragment.this);
        gmap.setOnInfoWindowClickListener(MulticarFragment.this);
        try{
          String map_type= CommonPreference.getInstance().getMapType();
          if(map_type==null){
            boolean success = gmap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_map));
            if (!success) {
              // Handle map style load failure
            }

          }else if(map_type.equals("NORMAL")){
            boolean success = gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_map));
            if (!success) {
              // Handle map style load failure
            }
          }else if(map_type.equals("NIGHT")){
            boolean success = gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_night_mode));
            if (!success) {
              // Handle map style load failure
            }

          }else if(map_type.equals("SATELLITE")){
            gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

          }else {
            boolean success = gmap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_map));
            if (!success) {
              // Handle map style load failure
            }
          }
        } catch (Resources.NotFoundException e) {
          // Oops, looks like the map style resource couldn't be found!
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(24.8937, 78.9629))
            .zoom(4)
            .bearing(0)
            .build();
        gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));




        gmap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
          @Override
          public View getInfoWindow(Marker marker) {
            return null;
          }

          @Override
          public View getInfoContents(Marker marker) {
            final Context context = getActivity(); //or getActivity(), YourActivity.this, etc.
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
            snippet.setGravity(Gravity.CENTER);

            info.addView(title);
            info.addView(snippet);
            return info;
          }

        });
      }
    });
    list=new ArrayList<>();
    adapter=new CarcountAdapter(list);
    listView_multicar.setAdapter(adapter);

    zoomIn();
    zoomout();
    fetchStatusData();
    listView_multicar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listView_multicar.setVisibility(View.GONE);
        onDestroyView();
        CarcountModel model=list.get(position);
        if(model.getBrand().equals("All")){

        }else{
          SingleCarFragment singleCar=new SingleCarFragment();
          Bundle bundle=new Bundle();
          bundle.putString("IMEI",list.get(position).getImei());
          bundle.putString("registration_number",list.get(position).getRegistration());
          bundle.putInt("coming",2);
          GlobalReferences.getInstance().baseActivity.addFragmentWithBackStack(singleCar,false,bundle);
        }
        textView_selected_car.setText(model.getRegistration()+" "+model.getBrand()+" "+model.getBrand());

      }
    });

    relativeLayout_car_selection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listView_multicar.setVisibility(listView_multicar.isShown()
            ? View.GONE
            : View.VISIBLE);
        view_bg_opacity.setVisibility(listView_multicar.isShown()
            ? View.VISIBLE
            : View.GONE);

      }
    });

    view_bg_opacity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(listView_multicar.getVisibility()==View.VISIBLE){
          view_bg_opacity.setVisibility(View.GONE);
        }
        listView_multicar.setVisibility(View.GONE);
      }
    });




  }






  public void zoomIn(){
    imageView_zoomin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gmap.animateCamera(CameraUpdateFactory.zoomIn());

      }
    });


  }
  public void zoomout(){
    imageView_zoomout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gmap.animateCamera(CameraUpdateFactory.zoomOut());
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();

  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();

  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }


  public void refreshData(){
    textView_car_address.setText("Tap on marker to see address");
    Toast.makeText(getActivity(), "Refreshing", Toast.LENGTH_SHORT).show();

   fetchStatusData();
  }

  public void fetchStatusData(){
    if (NetworkUtils.isConnected()) {
      ApiRequests.getInstance().get_status(GlobalReferences.getInstance().baseActivity,MulticarFragment.this);
    } else {
      ToastUtils.showShort(R.string.no_internet);
    }
  }
  @Override
  public void onMapClick(LatLng latLng) {
    textView_car_address.setText("Tap on marker to see address");
  }

  @Override
  public void onInfoWindowClick(Marker marker) {

  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    ApiRequests.getInstance().get_address(GlobalReferences.getInstance().baseActivity,this,marker.getPosition().latitude,marker.getPosition().longitude);
    return false;
  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {

    if (listener.getTag().equals("status")) {
      presenter.parseListData(listener.getJsonResponse());
    }
    if (listener.getTag().equals("address")){
      presenter.parseAddressData(listener.getJsonResponse());
    }

  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public void setList(ArrayList<CarcountModel> list) {
       this.list.clear();
       this.list.addAll(list);
       adapter.notifyDataSetChanged();


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
  public void setActiveCar(CarcountModel model) {


    if (gmap !=null) {
      if (model.getLatitude() != 0) {

        if (builder != null) {
          builder.include(new LatLng(model.getLatitude(), model.getLongitude()));
        }
        gmap.addMarker(new MarkerOptions()
            .position(new LatLng(model.getLatitude(), model.getLongitude()))
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_result)
            .title(model.getNickName())
            .zIndex(1.0f)
            .snippet(model.getBrand() + " " + model.getModel() + "\n" + model.getRegistration())
            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_car_blue_small", 80, 80))));
      }
    }

  }

  @Override
  public void setInactiveCar(CarcountModel model) {
    if (gmap !=null) {
      if (model.getLatitude() != 0) {
        if (builder != null) {
          builder.include(new LatLng(model.getLatitude(), model.getLongitude()));
        }
        gmap.addMarker(new MarkerOptions()
            .position(new LatLng(model.getLatitude(), model.getLongitude()))
            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_result)
            .title(model.getNickName())
            .snippet(model.getBrand() + " " + model.getModel() + "\n" + model.getRegistration())
            // .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("car_grey", 80, 80))
            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_car_grey_small", 80, 80))));

      }
    }
    }

  @Override
  public void setCount(int active, int inactive) {
    textView_car_active.setText(""+active);
    textView_car_inactive.setText(""+(inactive));

    try {
      LatLngBounds bounds = builder.build();

      if ( gmap != null) {
        gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
      }

    }catch (Exception ex){
    }
  }

  @Override
  public void setAddress(String address) {
    textView_car_address.setText(address);

  }

  @Override
  public void clearMap() {
    if (gmap !=null) {
      gmap.clear();
    }

  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {

  }

  @Override
  public void onConnectionSuspended(int i) {

  }
}
