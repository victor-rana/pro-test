package blackflame.com.zymepro.ui.login.fragment.loginfragment;

public class User {
  String name;
  String topic;
  String mobile_number;
  String coupon_code;
  String token;
  String imei;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  String email;
  int car_count;
  boolean activation_status;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getMobile_number() {
    return mobile_number;
  }

  public void setMobile_number(String mobile_number) {
    this.mobile_number = mobile_number;
  }

  public String getCoupon_code() {
    return coupon_code;
  }

  public void setCoupon_code(String coupon_code) {
    this.coupon_code = coupon_code;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getImei() {
    return imei;
  }

  public void setImei(String imei) {
    this.imei = imei;
  }

  public int getCar_count() {
    return car_count;
  }

  public void setCar_count(int car_count) {
    this.car_count = car_count;
  }

  public boolean isActivation_status() {
    return activation_status;
  }

  public void setActivation_status(boolean activation_status) {
    this.activation_status = activation_status;
  }
}
