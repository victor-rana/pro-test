package blackflame.com.zymepro.ui.pitstop;

import android.util.Log;
import blackflame.com.zymepro.ui.pitstop.model.NearByData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PitstopPresenter {
  View view;

  public PitstopPresenter(View view){
    this.view=view;

  }
  public void parseData(JSONObject result){
    try{
      ArrayList<NearByData> list=new ArrayList<>();

      String status=result.getString("status");
      if (status.equals("OK")) {
        JSONArray nearbydata = result.getJSONArray("results");
        int length = nearbydata.length();
        for (int i = 0; i < length; i++) {
          NearByData data = new NearByData();
          JSONObject object = nearbydata.getJSONObject(i);
          if (object.has("opening_hours")) {
            data.setIsopen(object.getJSONObject("opening_hours").getBoolean("open_now"));
          }
          if (object.has("name")) {
            Log.e("", "nearbyData: " + object.getString("name"));
            data.setName(object.getString("name"));
          }
          if (object.has("photos")) {
            JSONArray details_image = object.getJSONArray("photos");
            if (details_image.length() > 0) {
              JSONObject photo = details_image.getJSONObject(0);
              if (photo.has("photo_reference")) {
                data.setImage_reference(photo.getString("photo_reference"));
              }
            }
          }

          if (object.has("rating")) {
            data.setRating(Float.valueOf(object.getString("rating")));
          }
          if (object.has("vicinity")) {
            data.setAddress(object.getString("vicinity"));
          }

          if (object.has("geometry")) {
            JSONObject location = object.getJSONObject("geometry");
            if (location.has("location")) {
              data.setLocation(new LatLng(location.getJSONObject("location").getDouble("lat"), location.getJSONObject("location").getDouble("lng")));
            }
          }

          view.setMarker(data,i);

          list.add(data);

        }
        view.setList(list);
      }else if(status.equals("ZERO_RESULTS")){
        view.setNoResult("No results found around you");

      }else if(status.equals("OVER_QUERY_LIMIT")){
        view.otherError("Could not reach Google servers at this time. Please try again later.");

      }else if(status.equals("REQUEST_DENIED")){
        view.otherError("Could not reach Google servers at this time. Please try again later.");


      }else if (status.equals("INVALID_REQUEST")){
        view.otherError("Could not reach Google servers at this time. Please try again later.");

      }else if (status.equals("UNKNOWN_ERROR")){
        view.otherError("Could not reach Google servers at this time. Please try again later.");

      }



    }catch (JSONException ex){
      Log.e("Nearby", "nearbyData: "+ex.getMessage() );
    }


  }

  interface View {
    void setMarker(NearByData data,int index);
    void setList(ArrayList<NearByData> list);
    void setNoResult(String msg);
    void otherError(String msg);
  }



}
