package blackflame.com.zymepro.ui.profile.model;

public class ProfileModel {
  public String getImei() {
    return Imei;
  }
  String subscriptiondays;
  String car_id;

  public String getCar_id() {
    return car_id;
  }

  public void setCar_id(String car_id) {
    this.car_id = car_id;
  }

  public String getSubscriptiondays() {
    return subscriptiondays;
  }

  public void setSubscriptiondays(String subscriptiondays) {
    this.subscriptiondays = subscriptiondays;
  }

  public void setImei(String imei) {
    Imei = imei;
  }

  String Imei;
  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getRegistration_number() {
    return registration_number;
  }

  public void setRegistration_number(String registration_number) {
    this.registration_number = registration_number;
  }

  public String getEngine_type() {
    return engine_type;
  }

  public void setEngine_type(String engine_type) {
    this.engine_type = engine_type;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  String model;
  String brand;
  String registration_number;
  String engine_type;
  String status;

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  String nickName;

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  String state;

  public String getFuelType() {
    return fuelType;
  }

  public void setFuelType(String fuelType) {
    this.fuelType = fuelType;
  }

  public String getEngineCc() {
    return engineCc;
  }

  public void setEngineCc(String engineCc) {
    this.engineCc = engineCc;
  }

  String fuelType,engineCc;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  String name;
}
