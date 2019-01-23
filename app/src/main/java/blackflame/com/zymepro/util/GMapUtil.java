package blackflame.com.zymepro.util;

import android.content.res.Resources;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

public class GMapUtil {
  public static void setMapStyle(GoogleMap Gmap){

    try{
      String map_type = CommonPreference.getInstance().getMapType();
      if (Gmap != null) {

        int id=UtilityMethod.getStyle(CommonPreference.getInstance().getMapType());

        if (map_type == null) {
          Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          boolean success = Gmap.setMapStyle(
              MapStyleOptions.loadRawResourceStyle(GlobalReferences.getInstance().baseActivity, id));
          if (!success) {
            // Handle map style load failure
          }

        } else if (map_type.equals("NORMAL")) {
          Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          boolean success = Gmap.setMapStyle(
              MapStyleOptions.loadRawResourceStyle(GlobalReferences.getInstance().baseActivity, id));
          if (!success) {
            // Handle map style load failure
          }
        } else if (map_type.equals("NIGHT")) {
          Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          boolean success = Gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(GlobalReferences.getInstance().baseActivity,id));
          if (!success) {
            // Handle map style load failure
          }

        } else if (map_type.equals("SATELLITE")) {
          Gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        } else {
          Gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          boolean success = Gmap.setMapStyle(MapStyleOptions.loadRawResourceStyle(GlobalReferences.getInstance().baseActivity,id));
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


  public static int getZoomLevel(Circle circle) {
    int zoomLevel = 11;
    if (circle != null) {
      double radius = circle.getRadius() + circle.getRadius() / 2;
      double scale = radius / 500;
      zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
    }
    return zoomLevel;
  }

  public static float getBearing(LatLng begin, LatLng end) {
    double lat = Math.abs(begin.latitude - end.latitude);
    double lng = Math.abs(begin.longitude - end.longitude);

    if (begin.latitude < end.latitude && begin.longitude < end.longitude)
      return (float) (Math.toDegrees(Math.atan(lng / lat)));
    else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
      return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
    else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
      return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
    else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
      return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
    return -1;
  }

}
