package blackflame.com.zymepro.ui.geotag.model;

public class GeotagResponse {
  String title;
  String address;
  String type;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  String id;
  double lat,lng;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }
}



