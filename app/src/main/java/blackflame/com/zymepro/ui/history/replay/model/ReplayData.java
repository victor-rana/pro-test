package blackflame.com.zymepro.ui.history.replay.model;

import com.google.android.gms.maps.model.LatLng;

public class ReplayData {
  String time;
  LatLng position;
    String rpm;
    String speed;
    String coolant;
    String distance;
    String voltage;

  public String getVoltage() {
    return voltage;
  }

  public void setVoltage(String voltage) {
    this.voltage = voltage;
  }

  public String getRpm() {
    return rpm;
  }

  public void setRpm(String rpm) {
    this.rpm = rpm;
  }

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String spped) {
    this.speed = spped;
  }

  public String getCoolant() {
    return coolant;
  }

  public void setCoolant(String coolant) {
    this.coolant = coolant;
  }

  public String getDistance() {
    return distance;
  }

  public void setDistance(String distance) {
    this.distance = distance;
  }

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
