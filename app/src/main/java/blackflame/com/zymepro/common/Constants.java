
package blackflame.com.zymepro.common;


import android.os.Debug;
import com.android.volley.Request.Method;

public interface Constants {

  public static final boolean DEBUG = true; // true for test env, false for prod env.

  String BASE_URL_PRODUCTION = "http://api.getzyme.xyz/pro";
  String SEED_URL_PRODUCTION = "http://api.getzyme.xyz/seed";
  String MQTT_URL_PRODUCTION = "tcp://broker.getzyme.xyz:3000";
  String NOTIFICATION_URL_PRODUCTION = "http://api.getzyme.xyz/notification";
  String INSTA_URL_PRODUCTION_ = "https://api.instamojo.com/";
  String INSTA_TYPE_PROD = "prod";
  int VERSION_CODE = 39;
  //test url
  String BASE_URL_TEST = "http://api-test.getzyme.xyz/pro";
  String SEED_URL_TEST = "http://api-test.getzyme.xyz/seed";
  String MQTT_URL_TEST = "tcp://broker-test.getzyme.xyz:3000";
  String NOTIFICATION_URL_TEST = "http://api-test.getzyme.xyz/notification";
  String INSTA_URL_TEST = "https://test.instamojo.com/";
  String INSTA_TYPE_TEST = "test";


  public enum RequestParam {
    SIGN_IN(1, Method.POST, "/API/v1/user/login", "sign_in"),
    SIGN_UP(2, Method.POST, "/API/v1/user", "sign_up"),
    SIGN_OUT(3, Method.POST, "/API/v1/user/logout", "logout"),
    STATUS(4, Method.GET, "/API/v1/device/status/", "status"),
    LAST_CONNECTION(5, Method.GET, "/API/v1/device/last-connection/", "last_connection"),
    GPS_STATUS(6, Method.GET, "/API/v1/device/status/car/", "gps_status"),
    LINK_DEVICE(7, Method.POST, "/API/v1/device/link/", "link_device"),
    LOAD_BRANDS(8, Method.GET, "/API/v1/car/brands/", "brand"),
    LOAD_MODELS(9, Method.GET, "/API/v1/car/models/", "model"),
    REGISTER_CAR(10, Method.POST, "/API/v1/user/car", "register_car"),
    LOAD_PROFILE(11, Method.GET, "/API/v1/user/profile/", "profile"),
    UPDATE_CAR(12, Method.PUT, "/API/v1/user/car", "update_car"),
    UPDATE_PROFILE(13, Method.POST, "/API/v1/user/profile/", "update_profile"),
    UPDATE_PASSWORD(14, Method.POST, "/API/v1/user/change-password", "update_password"),
    UPLOAD_SETTING(15,Method.POST,"/API/v1/alert/setting","upload_setting"),
    GET_SETTING(16,Method.GET,"/API/v1/alert/","get_setting");
    private int id;
    private int method;
    private String postFix;
    private String requestTag;

    RequestParam(int id, int method, String postFix, String requestTag) {
      this.id = id;
      this.method = method;
      this.postFix = postFix;
      this.requestTag = requestTag;
    }

    public int getId() {
      return id;
    }

    public String getBaseComleteUrl() {
      if (DEBUG) {
        return BASE_URL_TEST.concat(postFix);

      } else {
        return BASE_URL_PRODUCTION.concat(postFix);
      }
    }

    public String getMqttUrl() {
      if (DEBUG) {
        return MQTT_URL_TEST;
      } else {
        return MQTT_URL_PRODUCTION;
      }
    }

    public String getNotificationUrl() {
      if (DEBUG) {
        return NOTIFICATION_URL_TEST.concat(postFix);
      } else {
        return NOTIFICATION_URL_PRODUCTION.concat(postFix);
      }
    }


    public String getInstaUrl() {
      if (DEBUG) {
        return INSTA_URL_TEST;
      } else {
        return INSTA_URL_PRODUCTION_;
      }
    }


    public String getOnlyPostFixUrl() {
      return postFix;
    }

    public void setPostFix(String postFix) {
      this.postFix = postFix;
    }

    public String getRequestTag() {
      return requestTag;
    }

    public int getMethod() {
      return method;
    }
  }


  String EMAIL = "email_id";
  String PASSWORD = "password";
  String MOBILE = "mobile_number";
  String NAME = "name";


  String TRIPSTART = "trip_start";
  String TRIPEND = "trip_end";
  String FATTIGUE = "fattigue";
  String ISSOSSET = "issosset";
  String COLLISION = "collision";
  String THEFT = "theft";
  String LONGIDLING = "longidling";
  String TOWING = "towing";
  String GEOFENCE = "geofence";
  String OVERSPEEDING = "over_speeding";
  String RASHDRIVING = "rash_driving";
  String UNPLUG = "unplug";
  String STARTTIME = "startTime";
  String ENDTIME = "endTime";

  String LOWBATTERYVOLTAGE = "low_battery_voltage";
  String HIGHCOOLANTTEMPERATURE = "high_coolant_temperature";
  String SPEEDLIMIT = "speedLimit";

  String MOBILE1 = "mob1";
  String MOBILE2 = "mob2";
  String MOBILE3 = "mob3";
  String MOBILE4 = "mob4";
  String NAME1 = "name1";
  String NAME2 = "name2";
  String NAME3 = "name3";
  String NAME4 = "name4";

  String SOSARRAY = "sosArray";


}
