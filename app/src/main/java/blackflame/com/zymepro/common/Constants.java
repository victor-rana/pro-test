
package blackflame.com.zymepro.common;


import android.os.Debug;
import com.android.volley.Request.Method;

public interface Constants {
    public static final boolean DEBUG = true; // true for test env, false for prod env.

   String BASE_URL_PRODUCTION="http://api.getzyme.xyz/pro";
   String SEED_URL_PRODUCTION="http://api.getzyme.xyz/seed";
   String MQTT_URL_PRODUCTION="tcp://broker.getzyme.xyz:3000";
   String NOTIFICATION_URL_PRODUCTION="http://api.getzyme.xyz/notification";
   String INSTA_URL_PRODUCTION_="https://api.instamojo.com/";
   String INSTA_TYPE_PROD="prod";
  int VERSION_CODE=39;


  //test url
    String BASE_URL_TEST="http://api-test.getzyme.xyz/pro";
    String SEED_URL_TEST="http://api-test.getzyme.xyz/seed";
     String MQTT_URL_TEST="tcp://broker-test.getzyme.xyz:3000";
     String NOTIFICATION_URL_TEST="http://api-test.getzyme.xyz/notification";
    String INSTA_URL_TEST="https://test.instamojo.com/";
     String INSTA_TYPE_TEST="test";


    public enum RequestParam {
      SIGN_IN(1, Method.POST, "/API/v1/user/login", "sign_in"),
      SIGN_UP(2, Method.POST, "/API/v1/user", "sign_up"),
      SIGN_OUT(2, Method.POST, "/API/v1/user/logout", "logout"),
      STATUS(3, Method.GET, "/API/v1/device/status/", "status");;
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
          if (DEBUG){
            return BASE_URL_TEST.concat(postFix);

          }else{
            return BASE_URL_PRODUCTION.concat(postFix);
          }
        }

        public String getMqttUrl(){
          if (DEBUG){
            return MQTT_URL_TEST;
          }else{
            return  MQTT_URL_PRODUCTION;
          }
      }

      public String getNotificationUrl(){
          if (DEBUG){
            return NOTIFICATION_URL_TEST.concat(postFix);
          }else{
            return NOTIFICATION_URL_PRODUCTION.concat(postFix);
          }
      }


      public String getInstaUrl(){
          if (DEBUG){
            return INSTA_URL_TEST;
          }else{
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


  public final String EMAIL = "email_id";
  public final String PASSWORD = "password";
  public final String MOBILE="mobile_number";
  public final String NAME="name";


}
