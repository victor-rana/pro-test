package blackflame.com.zymepro.ui.history.replay.model;

import com.google.android.gms.maps.model.LatLng;

public class ReplayData {
  String time;
  LatLng position;

  public LatLng getPosition() {
    return position;
  }

  public void setPosition(LatLng position) {
    this.position = position;
  }

  double latitude;

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  double longitude;
}
