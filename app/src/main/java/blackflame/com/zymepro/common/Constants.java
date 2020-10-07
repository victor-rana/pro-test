
package blackflame.com.zymepro.common;


import android.os.Debug;
import com.android.volley.Request.Method;

public interface Constants {

  public static final boolean DEBUG = false; // true for test env, false for prod env.
  String BASE_URL_PRODUCTION = "https://api.getzyme.xyz/pro";
  String SEED_URL_PRODUCTION = "https://api.getzyme.xyz/seed";
  //TODO CHANGE MQTT URL BEFORE RELEASE

  String MQTT_URL_PRODUCTION = "tcp://broker.getzyme.xyz:3000";
  String NOTIFICATION_URL_PRODUCTION = "https://api.getzyme.xyz/notification";
  String INSTA_URL_PRODUCTION_ = "https://api.instamojo.com/";
  String INSTA_TYPE_PROD = "prod";
  int VERSION_CODE = 39;
  //test url
  String BASE_URL_TEST = "http://api-test.getzyme.xyz/pro";
  String SEED_URL_TEST = "http://api-test.getzyme.xyz/seed";
  //TODO CHANGE MQTT URL BEFORE RELEASE
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
    GET_SETTING(16,Method.GET,"/API/v1/alert/","get_setting"),
    LOAD_TRIP(17,Method.GET,"/API/v1/trip/","get_trip"),
    GET_PDF_URL(18,Method.GET,"/API/v1/report/get-pdf/","get_pdf_url"),
    GET_CSV_URL(19,Method.GET,"/API/v1/report/get-csv/","get_csv_url"),
    GET_TRIP_DETAILS(20,Method.GET,"/API/v1/trip/","trip_details"),
    GET_ALERT_LIST(21,Method.GET,"/API/v1/alert/","alert"),
    GET_PREVIOUS_ERROR(22,Method.GET,"/API/v1/device/old-engine-diagnostics/","previous_error"),
    GET_CURRENT_ERROR(23,Method.GET,"/API/v1/device/engine-diagnostics/","current_error"),
    GET_NEAR_BY(24,Method.GET,"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=","near_by"),
    PITSTOP(25, Method.GET, "/API/v1/device/status/car/", "pitstop"),
    GET_FUEL_PRICE(26,Method.GET,"/API/v1/user/getprices/","price"),
    LOAD_ANALYTICS(27,Method.GET,"/API/v1/analytics/","analytics"),
    LOAD_PAST_NOTIFICATION(28,Method.GET,"/API/v1/notification/","past_notification"),
    LOAD_GEOTAG(29,Method.GET,"/API/v1/user/geotag/","load_geotag"),
    DELETE_GEOTAG(30,Method.DELETE,"/API/v1/user/geotag/","delete_geotag"),
    LOAD_MESSAGE_FROM_TEAM(31,Method.GET,"/API/v1/user/message","message_from_team"),
    LOAD_REFER_MESSAGE(32,Method.GET,"/API/v1/user/refer-data/","refer"),
    GET_EXISTING_URL(33,Method.GET,"/API/v1/sharing/existing-url/","existing"),
    GENERATE_URL(34,Method.POST,"/API/v1/sharing/generate-url/","generate"),
    DEACTIVATE_URL(35,Method.DELETE,"/API/v1/sharing/deactivate/","delete_url"),
    SAVE_GEO_TAG(36,Method.GET,"/API/v1/user/geotag","save_geotag"),
    UPDATE_PAYMENT(37,Method.POST,"/API/v1/user/recharge","payment_success"),
    UPDATE_FAILED(38,Method.POST,"/API/v1/user/recharge","payment_failed"),
    UPDATE_MOBILE_SETTING(39,Method.POST,"/API/v1/user/mobile/","mobile_Setting"),
    GET_GEO_FENCE(40,Method.GET,"/API/v1/user/geofence/","get_geofence"),
    SAVE_GEOFENCE(41,Method.POST,"/API/v1/user/geofence/","save_geofence"),
    GET_LIVE_TRIP_PATH(42, Method.GET, "/API/v1/trip/latest/", "live_trip"),
    GET_ADDRESS(43,Method.POST,"/API/v1/location/address","address"),
    GET_SUBSCRIPTION_AMOUNT(44, Method.GET,"/API/v1/user/subscription-amount","amount"),
    CREATE_ORDER(45, Method.POST,"/API/v1/user/generate-recharge-order","create_order"),
    UPDATE_ORDER(45, Method.POST,"/API/v1/user/recharge-order-complete","update_order");


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

    public String getBaseCompleteUrl() {
      if (DEBUG) {
        return BASE_URL_TEST.concat(postFix);

      } else {
        return BASE_URL_PRODUCTION.concat(postFix);
      }
    }



    public String getSeedComleteUrl() {
      if (DEBUG) {
        return SEED_URL_TEST.concat(postFix);

      } else {
        return SEED_URL_PRODUCTION.concat(postFix);
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
