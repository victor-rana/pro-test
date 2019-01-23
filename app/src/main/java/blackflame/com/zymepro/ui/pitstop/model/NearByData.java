package blackflame.com.zymepro.ui.pitstop.model;

import com.google.android.gms.maps.model.LatLng;

public class NearByData {
  LatLng location;
  String image_path;
  String image_reference;
  String address;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getImage_reference() {
    return image_reference;
  }

  public void setImage_reference(String image_reference) {
    this.image_reference = image_reference;
  }

  String name;
  float rating;
  boolean isopen;

  public LatLng getLocation() {
    return location;
  }

  public void setLocation(LatLng location) {
    this.location = location;
  }

  public String getImage_path() {
    return image_path;
  }

  public void setImage_path(String image_path) {
    this.image_path = image_path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }

  public boolean isIsopen() {
    return isopen;
  }

  public void setIsopen(boolean isopen) {
    this.isopen = isopen;
  }
}
