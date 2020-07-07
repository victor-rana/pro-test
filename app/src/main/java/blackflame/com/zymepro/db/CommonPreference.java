package blackflame.com.zymepro.db;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonPreference {

  private static final String PREF_NAME = "pref_login_pro";
  private static final String WHATSNEWSHOWN="whatsnew_geotag";
  private static final String REMEMBERMYCHOICE="remembermychoice";
  private static final String IMEIFORAPI="imeiforapi";
  private static final String CLIENTTOKEN="clienttoken";
  private static final String IMEIFORSERVER="imeiforserver";
  private static final String ISAUTOSTARTLAUNCH="isautostartlaunch";
  private static final String ARRAYOFIMEI="imeilist";
  private static final String IMEI="imei";
  private static final String NAME="name";
  private static final String MOBILE="pref";
  private static final String EMAIL="email_pro";
  private static final  String CARID="carId";
  private static final String USERNAME="username";
  private static final String TOPIC="topic_pro";
  private static final String REFERCODE="refer_code";
  private static final String ACCESSTOKEN="access_token";
  private static final String FCMTOKEN="fcm_token_server";
  private static final String REGISTRATIONFORTOOLBAR="registrationfortoolbar";
  private static final String MODELFORTOOLBAR="modelfortoolbar";
  private static final String  INSTALLTIME="installtime";
  private static final String DATE_FIRST_LAUNCH="date_first_launch";
  private static final String LAUNCH_COUNT="launch_count";
  private static final String ISDEVICEACTIVATED="isdeviceactivated";
  private static final String DONTSHOWAGAIN="dont_show_again";
  private static final String ISTHISDEVICEACTIVATED="imeiforserver";
  private static final  String HASLOGGEDIN="isLoggedin";
  private static final String DEVICECOUNT="devicecount";
  private static final String MONTHCOUNT="monthcount";

  private static final  String ISDEMOUSER="isdemouser";
  private static final String MAP_TYPE="map_type";
  private static final String SHOULD_LIVE_TRAFFIC="live_traffic";
  private static final String IMEI_SUBSCRIPTION="imei_subscription";
  private static final String SELECTED_STYLE="selected_style";
  private static final String SELECTED_POSITION="selected_position";
  private static final String SELECTED_REPLACEMENT="selected_replacement";
  private static final String REPLACEMENT_CARID="replacement_carid";
  private static final String PLAYER_ID="player_id";
  public static final String DEVICELINKED="devicelinked";
  public static final String GEOFENCE_RADIUS="radius";
  public static final String GEOFENCE_LATITUDE="latitude";
  public static final String GEOFENCE_LONGITUDE = "longitude";
  private static final String NOTIFICATION_ROUTE = "notification_route";
  private static final String IS_DEMO_LOGIN = "is_demo_login";
  private static final String LOGIN_TIME = "login_time";
  private static final String LOGIN_EXPIRY = "login_expiry";

  private static final String TOKEN_EXP_TIME="token_exp_time";


  static final String GEOFENCESEND="Geofencesend";
  static final String WHATS3WORDS="whats3words";



  private static CommonPreference sInstance;
  private SharedPreferences mPref;

  private CommonPreference(Context context){
    mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
  }
  public static synchronized void initializeInstance(Context context) {
    if (sInstance == null) {
      sInstance = new CommonPreference(context);
    }
  }
  public static synchronized CommonPreference getInstance() {
    if (sInstance == null) {
      throw new IllegalStateException(CommonPreference.class.getSimpleName() +
          " is not initialized, call initializeInstance(..) method first.");
    }
    return sInstance;
  }
  public void setWhatsShown(){
    mPref.edit().putBoolean(WHATSNEWSHOWN, true)
        .apply();
  }
  public boolean getWhatsShown(){

    return mPref.getBoolean(WHATSNEWSHOWN, false);}


  public void setShouldLiveTraffic(boolean value){
    mPref.edit().putBoolean(SHOULD_LIVE_TRAFFIC, value)
        .apply();
  }
  public boolean getLiveTraffic(){

    return mPref.getBoolean(SHOULD_LIVE_TRAFFIC, false);}








  public void setImeiForAPI(String imei){
    mPref.edit().putString(IMEIFORAPI, imei)
        .apply();
  }
  public String getImeiForAPI(){

    return mPref.getString(IMEIFORAPI, "A");}

  public void setCLientToken(String token){
    mPref.edit().putString(CLIENTTOKEN, token)
        .apply();
  }
  public String getClientToken(){

    return mPref.getString(CLIENTTOKEN, null);}


  public void setImeiForServer(String time){
    mPref.edit().putString(IMEIFORSERVER, time)
        .apply();
  }


  public boolean isAutoStartLaunched(){
    return mPref.getBoolean(ISAUTOSTARTLAUNCH, false);}

  public void setAutoStartLaunched() {
    mPref.edit().putBoolean(ISAUTOSTARTLAUNCH, true)
        .apply();
  }

  public String getImeiForServer(){

    return mPref.getString(IMEIFORSERVER, null);}

  public void setImei(String imei){
    mPref.edit().putString(IMEI, imei)
        .apply();
  }
  public String getImei(){

    return mPref.getString(IMEI, null);}
  public void setName(String name){
    mPref.edit().putString(NAME, name)
        .apply();
  }
  public String getName(){

    return mPref.getString(NAME, null);}
  public void setMobile(String name){
    mPref.edit().putString(MOBILE, name)
        .apply();
  }
  public String getMobile(){

    return mPref.getString(MOBILE, null);}


  public void setCarId(String time){
    mPref.edit().putString(CARID, time)
        .apply();
  }
  public String getCarId(){

    return mPref.getString(CARID, "a");}
  public void setEmail(String email){
    mPref.edit().putString(EMAIL, email)
        .apply();
  }
  public String getEmail(){

    return mPref.getString(EMAIL, null);}
  public void setUserName(String time){
    mPref.edit().putString(USERNAME, time)
        .apply();
  }
  public String getUserName(){
    return mPref.getString(USERNAME, null);}
  public void setSubscriptionTopic(String topicName){
    mPref.edit().putString(TOPIC, topicName)
        .apply();
  }
  public String getSubscriptionTopic(){

    return mPref.getString(TOPIC, "a");}


  public void setReferCode(String time){
    mPref.edit().putString(REFERCODE, time)
        .apply();
  }
  public String getReferCode(){

    return mPref.getString(REFERCODE, "a");}
  public void setToken(String time){
    mPref.edit().putString(ACCESSTOKEN, time)
        .apply();
  }
  public String getToken(){

    return mPref.getString(ACCESSTOKEN, null);}

  public void setFcmToken(String time){
    mPref.edit().putString(FCMTOKEN, time)
        .apply();
  }
  public String getFcmToken(){

    return mPref.getString(FCMTOKEN, "TOKEN_NOT_AVAILABLE");}
  public void setRegNumber(String time){
    mPref.edit().putString(REGISTRATIONFORTOOLBAR, time)
        .apply();
  }
  public String getRegNumber(){

    return mPref.getString(REGISTRATIONFORTOOLBAR, "--");}
  public void setModel(String time){
    mPref.edit().putString(MODELFORTOOLBAR, time)
        .apply();
  }
  public String getModel(){

    return mPref.getString(MODELFORTOOLBAR, "--");}
  public void setInstallTime(long time){
    mPref.edit().putLong(INSTALLTIME, time)
        .apply();
  }
  public long getInstallTime(){
    return mPref.getLong(INSTALLTIME, 123);}

  public void setFirstLaunch(long time){
    mPref.edit().putLong(DATE_FIRST_LAUNCH, time)
        .apply();
  }
  public long getFirstLaunch(){
    return mPref.getLong(DATE_FIRST_LAUNCH, 0);}




  public void setLaunchCount(long time){
    mPref.edit().putLong(LAUNCH_COUNT, time)
        .apply();
  }
  public long getLaunchCount(){
    return mPref.getLong(LAUNCH_COUNT, 0);}


  public void setDeviceActivated(boolean value){
    mPref.edit()
        .putBoolean(ISDEVICEACTIVATED, value)
        .apply();

  }
  public boolean getDeviceActivated(){
    return mPref.getBoolean(ISDEVICEACTIVATED, false);
  }
  public void setDeviceLinked(boolean value){
    mPref.edit()
        .putBoolean(DEVICELINKED, value)
        .apply();

  }

  public boolean getDeviceLinked(){
    return mPref.getBoolean(DEVICELINKED, false);
  }
  public void setDontShow(boolean value){
    mPref.edit()
        .putBoolean(DONTSHOWAGAIN, value)
        .apply();

  }
  public boolean getDontShow(){
    return mPref.getBoolean(DONTSHOWAGAIN, false);
  }



  public void setThisDeviceLinked(boolean value){
    mPref.edit()
        .putBoolean(ISTHISDEVICEACTIVATED, value)
        .apply();

  }
  public boolean getThisDeviceLinked(){
    return mPref.getBoolean(ISTHISDEVICEACTIVATED, false);
  }
  public void setIsLoggedIn(boolean value){
    mPref.edit()
        .putBoolean(HASLOGGEDIN, value)
        .apply();

  }
  public boolean getIsLoggedIn(){
    return mPref.getBoolean(HASLOGGEDIN, false);
  }





  public void setIsDemoUser(boolean value){
    mPref.edit()
            .putBoolean(ISDEMOUSER, value)
            .apply();

  }
  public boolean getIsDemoUser(){
    return mPref.getBoolean(ISDEMOUSER, false);
  }




  public void setTokenExpTime(String time){
    mPref.edit().putString(TOKEN_EXP_TIME, time)
            .apply();
  }
  public String getTokenExpTime(){

    return mPref.getString(TOKEN_EXP_TIME, null
    );
  }


  public void setDeviceCount(int count){
    mPref.edit()
        .putInt(DEVICECOUNT, count)
        .apply();
  }
  public int getDeviceCount(){
    return mPref.getInt(DEVICECOUNT, 0);
  }
  public boolean clear() {
    return mPref.edit()
        .clear()
        .commit();
  }

  public void setMapType(String time){
    mPref.edit().putString(MAP_TYPE, time)
        .apply();
  }
  public String getMapType(){

    return mPref.getString(MAP_TYPE, "DEFAULT");}

  public void setImeiSubscription(String name){
    mPref.edit().putString(IMEI_SUBSCRIPTION, name)
        .apply();
  }
  public String getSubscriptionImei(){

    return mPref.getString(IMEI_SUBSCRIPTION, null);}


  public void setSubscriptionMonth(int count){
    mPref.edit()
        .putInt(MONTHCOUNT, count)
        .apply();
  }
  public int getSubscriptionMonth(){
    return mPref.getInt(MONTHCOUNT, 0);
  }


  public void setStyle(String time){
    mPref.edit().putString(SELECTED_STYLE, time)
        .apply();
  }
  public String getStyle(){
    return mPref.getString(SELECTED_STYLE, "DEFAULT");}


  public void setSelectedPosition(int position){
    mPref.edit().putInt(SELECTED_POSITION, position)
        .apply();


  }
  public int getSelectedPosition(){
    return mPref.getInt(SELECTED_POSITION, -1);}


  public void setRemember(){
    mPref.edit().putBoolean(REMEMBERMYCHOICE, true)
        .apply();
  }
  public boolean getRemeber(){

    return mPref.getBoolean(REMEMBERMYCHOICE, false);}


  public void setSelectedReplacement(String imei){
    mPref.edit().putString(SELECTED_REPLACEMENT, imei)
        .apply();
  }
  public String getSelectedReplacement(){
    return mPref.getString(SELECTED_REPLACEMENT, null);}
  public void setReplacementCarid(String carId){
    mPref.edit().putString(REPLACEMENT_CARID, carId)
        .apply();
  }
  public String getReplacementCarid(){
    return mPref.getString(REPLACEMENT_CARID, null);}

  public void setPlayerId(String carId){
    mPref.edit().putString(PLAYER_ID, carId)
        .apply();
  }
  public String getPlayerId(){
    return mPref.getString(PLAYER_ID, null);}





  public void setExpiryTime(String carId){
    mPref.edit().putString(LOGIN_EXPIRY, carId)
            .apply();
  }
  public String getExpiryTime(){
    return mPref.getString(LOGIN_EXPIRY, null);}


  public void setGeofence(boolean value){
    mPref.edit()
        .putBoolean(GEOFENCESEND, value)
        .apply();
  }
  public boolean isGeofenceSet(){
    return mPref.getBoolean(GEOFENCESEND, false);
  }


  public void setWhats3Words(boolean value){
    mPref.edit()
            .putBoolean(WHATS3WORDS, value)
            .apply();
  }
  public boolean getsetWhats3Words(){
    return mPref.getBoolean(WHATS3WORDS, false);
  }


public void setGeofenceRadius(int radius){
  mPref.edit()
      .putInt(DEVICECOUNT, radius)
      .apply();

}
public int getGeonceRadius(){
  return mPref.getInt(GEOFENCE_RADIUS, 0);
}
  public void setGeofenceLatitude(String latitude){
    mPref.edit()
        .putString(GEOFENCE_LATITUDE, latitude)
        .apply();

  }
  public String getGeofenceLatitude(){
    return mPref.getString(GEOFENCE_LATITUDE, null);
  }

  public void setGeofenceLongitude(String latitude){
    mPref.edit()
        .putString(GEOFENCE_LONGITUDE, latitude)
        .apply();

  }
  public String getGeofenceLongitude(){
    return mPref.getString(GEOFENCE_LONGITUDE, null);
  }


  public void setOneSignalNotification(boolean status){
    mPref.edit().putBoolean(NOTIFICATION_ROUTE, status)
            .apply();
  }
  public boolean getOneSignalNotification(){

    return mPref.getBoolean(NOTIFICATION_ROUTE, true);}
}
