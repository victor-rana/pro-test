package blackflame.com.zymepro.ui.tripdetails.mapview;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.tripdetails.TripDataListener;
import blackflame.com.zymepro.util.UtilityMethod;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.text.DecimalFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class TripPathFragment extends CommonFragment implements GoogleMap.OnMarkerClickListener,GoogleApiClient.ConnectionCallbacks,TripDataListener {
  MapView mMapView;
  GoogleMap Gmap;
  boolean isMapReady;
  GoogleApiClient mGoogleApiClient;
  PolylineOptions polylineOptions;
  Polyline line;
  JSONArray array_tripPath;
  String startAddress,endAddress;
  String ist_time;
  private int distance;
  int tripId;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.layout_history_path, container, false);

    // Inflate the layout for this fragment
    mMapView = view.findViewById(R.id.history_path);

    Bundle bundle=getArguments();
    startAddress=bundle.getString("startAddress");
    endAddress=bundle.getString("endAddress");
    ist_time=bundle.getString("time");
    tripId=bundle.getInt("trip_id");


   // buildGoogleApiClient();
    mMapView.onCreate(savedInstanceState);
    mMapView.onResume();



    try {
      MapsInitializer.initialize(getActivity().getApplicationContext());
    } catch (Exception e) {
      e.printStackTrace();
    }
    mMapView.getMapAsync(new OnMapReadyCallback() {
      @Override
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
        Gmap.setOnMarkerClickListener(TripPathFragment.this);
        Gmap.getUiSettings().setZoomControlsEnabled(true);
        try{
          String map_type = CommonPreference.getInstance().getMapType();
          if (Gmap != null) {
            int id=UtilityMethod.getStyle(CommonPreference.getInstance().getMapType());

            if (map_type == null) {
              Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
              boolean success = Gmap.setMapStyle(
                  MapStyleOptions.loadRawResourceStyle(getActivity(), id));
              if (!success) {
                // Handle map style load failure
              }

            } else if (map_type.equals("NORMAL")) {
              Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
              boolean success = Gmap.setMapStyle(
                  MapStyleOptions.loadRawResourceStyle(getActivity(), id));
              if (!success) {
                // Handle map style load failure
              }
            } else if (map_type.equals("NIGHT")) {
              Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
              boolean success = Gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),id));
              if (!success) {
                // Handle map style load failure
              }

            } else if (map_type.equals("SATELLITE")) {
              Gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            } else {
              Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
              boolean success = Gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),id));
              if (!success) {
                // Handle map style load failure
              }
            }
          }
        } catch(Resources.NotFoundException e){
          // Log.e(TAG, "onResume: "+e.getMessage() );
          // Oops, looks like the map style resource couldn't be found!
        }


        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(24.8937, 78.9629))
            .zoom(4)
            .bearing(0)
            .build();
        Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

          @Override
          public View getInfoWindow(Marker arg0) {
            return null;
          }

          @Override
          public View getInfoContents(Marker marker) {

            LinearLayout info = new LinearLayout(getActivity());
            info.setOrientation(LinearLayout.VERTICAL);

            TextView title = new TextView(getActivity());
            title.setTextColor(Color.BLACK);
            title.setGravity(Gravity.CENTER);
            title.setTypeface(null, Typeface.BOLD);
            title.setText(marker.getTitle());

            TextView snippet = new TextView(getActivity());
            snippet.setTextColor(Color.GRAY);
            snippet.setText(marker.getSnippet());
            snippet.setGravity(Gravity.CENTER);

            info.addView(title);
            info.addView(snippet);

            return info;
          }
        });

      }
    });
    polylineOptions = new PolylineOptions();
    polylineOptions=new PolylineOptions().width(10).color(getResources().getColor(R.color.colorAccent)).geodesic(true);
    return view;
  }


//  protected synchronized void buildGoogleApiClient() {
//    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//        .addConnectionCallbacks(MapViewFragment.this)
//        .addOnConnectionFailedListener(this)
//        .addApi(Loca)
//        .build();
//    mGoogleApiClient.connect();
//  }



  @Override
  public void onResume() {
    super.onResume();
    mMapView.onResume();


  }

  @Override
  public void onStart() {
    super.onStart();



  }

  @Override
  public void onPause() {
    super.onPause();

    mMapView.onPause();
  }
  @Override
  public void onDestroy() {
    super.onDestroy();

    mMapView.onDestroy();
  }
  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mMapView.onLowMemory();
  }



  @Override
  public boolean onMarkerClick(Marker marker) {
    marker.showInfoWindow();
    setCurrentLocation(marker.getPosition());
    return true;
  }
  public void setCurrentLocation(LatLng latlng){


    Gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlng.latitude, latlng.longitude), 13));

    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(latlng)      // Sets the center of the map to location user
        .zoom(17)                   // Sets the zoom
        .bearing(90)                // Sets the orientation of the camera to east
        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
    Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {

  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void dataListener(JSONObject jsonObject) {


    try {
      JSONObject jsonObject_TripData = jsonObject.getJSONObject("msg");
      JSONObject tripObject = jsonObject_TripData.getJSONObject("trip_data");

      final JSONArray array_startLocation = tripObject.getJSONArray("start_location");
      final JSONArray array_endLocation = tripObject.getJSONArray("end_location");
      array_tripPath = jsonObject_TripData.getJSONArray("trip_path");
      if (!tripObject.isNull("distance")) {
        distance = tripObject.getInt("distance");
      }

      Activity activity = getActivity();
      if (activity!=null){
        Looper looper = getActivity().getMainLooper();
        if (looper != null) {

          new Handler(getActivity().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              PolylineOptions options = new PolylineOptions().width(10).color(getResources().getColor(R.color.colorAccent)).geodesic(true);
              if (array_tripPath != null) {

                int length = array_tripPath.length();
                for (int i = 0; i < length; i++) {
                  try {
                    JSONArray array = array_tripPath.getJSONArray(i);
//                                                    Log.e("Array", "run: "+array.getDouble(0)+ array.getDouble(1) );

                    LatLng point = new LatLng(array.getDouble(0), array.getDouble(1));
                    options.add(point);
                  } catch (JSONException e) {
                    e.printStackTrace();
                  }
                }

                if (Gmap!=null)

                  line = Gmap.addPolyline(options);
              }
              DecimalFormat twoDForm = new DecimalFormat("#.#");

              double distance_round = Double.valueOf(twoDForm.format((double) distance / 1000));


              try {
                if (Gmap!=null) {
                  setMarker(array_endLocation.getDouble(0), array_endLocation.getDouble(1), "End");
                  setMarker(array_startLocation.getDouble(0), array_startLocation.getDouble(1), "Start");
                  if (distance_round > 0.0) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(new LatLng(array_endLocation.getDouble(0), array_endLocation.getDouble(1)));
                    builder.include(new LatLng(array_startLocation.getDouble(0), array_startLocation.getDouble(1)));
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                    Gmap.animateCamera(cu);
                  } else {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(array_endLocation.getDouble(0), array_endLocation.getDouble(1)))
                        .zoom(16)
                        .bearing(0)
                        .build();
                    Gmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                  }
                }


              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
          });
        }
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }






  public void setMarker(double latitude,double longitude,String pin){
    if(Gmap !=null) {
      if (pin.equals("End")) {
        Gmap.addMarker(new MarkerOptions()
            .position(new LatLng(latitude, longitude))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trip_end_marker))
            .title("Trip ended at:")
            .snippet(endAddress)
        );


      } else if (pin.equals("Start")) {
        Gmap.addMarker(new MarkerOptions()
            .position(new LatLng(latitude, longitude))
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.trip_start_marker))
            .title("Trip started at:")
            .snippet(startAddress)
        );
      }
    }


  }
}

