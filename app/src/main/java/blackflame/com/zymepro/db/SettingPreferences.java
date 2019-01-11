package blackflame.com.zymepro.db;

import android.content.Context;
import android.content.SharedPreferences;
import blackflame.com.zymepro.common.Constants;

/**
 * Created by Prashant on 01-07-2017.
 */

public class SettingPreferences {
    private static final String PREF_NAME = "pro.setting.PREF_NAME";
    private static SettingPreferences sInstance;
    private SharedPreferences mPref;

    private SettingPreferences(Context context){
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SettingPreferences(context);
        }
    }
    public static synchronized SettingPreferences getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(SettingPreferences.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setStartTime(String time){
        mPref.edit()
                .putString(Constants.STARTTIME, time)
                .apply();
    }
    public String getStartTime(){

        return mPref.getString(Constants.STARTTIME, "23:00");}
    public void setMobile1(String mobile){
        mPref.edit()
                .putString(Constants.MOBILE1, mobile)
                .apply();
    }
    public String getMobile1(){

        return mPref.getString(Constants.MOBILE1, "--");}
    public void setMobile2(String mobile){
        mPref.edit()
                .putString(Constants.MOBILE2, mobile)
                .apply();
    }
    public String getMobile2(){

        return mPref.getString(Constants.MOBILE2, "--");}
    public void setMobile3(String mobile){
        mPref.edit()
                .putString(Constants.MOBILE3, mobile)
                .apply();
    }
    public String getMobile3(){

        return mPref.getString(Constants.MOBILE3, "--");}
    public void setName1(String mobile){
        mPref.edit()
                .putString(Constants.NAME1, mobile)
                .apply();
    }
    public String getName1(){

        return mPref.getString(Constants.NAME1, "Add sos number");}
    public void setName2(String mobile){
        mPref.edit()
                .putString(Constants.NAME2, mobile)
                .apply();
    }
    public String getName2(){

        return mPref.getString(Constants.NAME2, "Add sos number");}
    public void setName3(String mobile){
        mPref.edit()
                .putString(Constants.NAME3, mobile)
                .apply();
    }
    public String getName3(){

        return mPref.getString(Constants.NAME3, "Add sos number");}
    public void setName4(String mobile){
        mPref.edit()
                .putString(Constants.NAME4, mobile)
                .apply();
    }
    public String getName4(){

        return mPref.getString(Constants.NAME4, "Add sos number");}
    public void setMobile4(String mobile){
        mPref.edit()
                .putString(Constants.MOBILE4, mobile)
                .apply();
    }
    public String getMobile4(){

        return mPref.getString(Constants.MOBILE4, "--");}
    public void setEndtTime(String time){
        mPref.edit()
                .putString(Constants.ENDTIME, time)
                .apply();
    }
    public String getEndTime(){

        return mPref.getString(Constants.ENDTIME, "06:00");}
    public void setSOSArray(String time){
        mPref.edit()
                .putString(Constants.SOSARRAY, time)
                .apply();
    }
    public String getSOSArray(){

        return mPref.getString(Constants.SOSARRAY, "[]");}
    public void setSpeedLimit(String time){
        mPref.edit()
                .putString(Constants.SPEEDLIMIT, time)
                .apply();
    }
    public String getSpeedLimit(){

        return mPref.getString(Constants.SPEEDLIMIT, "0");}

    public void setTheft(boolean value){
        mPref.edit()
                .putBoolean(Constants.THEFT, value)
                .apply();

    }
    public boolean getTheft(){
        return mPref.getBoolean(Constants.THEFT, false);
    }


    public void setLongIdling(boolean value){
        mPref.edit()
                .putBoolean(Constants.LONGIDLING, value)
                .apply();

    }
    public boolean getLongIdling(){
        return mPref.getBoolean(Constants.LONGIDLING, false);
    }

    public void setTripStart(boolean value){
        mPref.edit()
                .putBoolean(Constants.TRIPSTART, value)
                .apply();

    }
    public boolean getTripStart(){
        return mPref.getBoolean(Constants.TRIPSTART, false);
    }
    public void setTripEnd(boolean value){
        mPref.edit()
                .putBoolean(Constants.TRIPEND, value)
                .apply();

    }
    public boolean getTripEnd(){
        return mPref.getBoolean(Constants.TRIPEND, false);
    }
    public void setFatigue(boolean value){
        mPref.edit()
                .putBoolean(Constants.FATTIGUE, value)
                .apply();

    }
    public boolean getFatigue(){
        return mPref.getBoolean(Constants.FATTIGUE, false);
    }
    public void setSos(boolean value){
        mPref.edit()
                .putBoolean(Constants.ISSOSSET, value)
                .apply();

    }
    public boolean getSOS(){
        return mPref.getBoolean(Constants.ISSOSSET, false);
    }

    public void setCollision(boolean value){
        mPref.edit()
                .putBoolean(Constants.COLLISION, value)
                .apply();

    }
    public boolean getCollision(){
        return mPref.getBoolean(Constants.COLLISION, false);
    }
    public void setTowing(boolean value){
        mPref.edit()
                .putBoolean(Constants.TOWING, value)
                .apply();

    }
    public boolean getTowing(){
        return mPref.getBoolean(Constants.TOWING, false);
    }


    public void setGeofence(boolean value){
        mPref.edit()
                .putBoolean(Constants.GEOFENCE, value)
                .apply();

    }
    public boolean getGeofence(){
        return mPref.getBoolean(Constants.GEOFENCE, false);
    }
    public void setOverspeeding(boolean value){
        mPref.edit()
                .putBoolean(Constants.OVERSPEEDING, value)
                .apply();

    }
    public boolean getOverspeeding(){
        return mPref.getBoolean(Constants.OVERSPEEDING, false);
    }
    public void setRashDriving(boolean value){
        mPref.edit()
                .putBoolean(Constants.RASHDRIVING, value)
                .apply();

    }
    public boolean getRashDriving(){
        return mPref.getBoolean(Constants.RASHDRIVING, false);
    }
    public void setUnplug(boolean value){
        mPref.edit()
                .putBoolean(Constants.UNPLUG, value)
                .apply();

    }
    public boolean getUnplug(){
        return mPref.getBoolean(Constants.UNPLUG, false);
    }

    public void setLowBattery(boolean value){
        mPref.edit()
                .putBoolean(Constants.LOWBATTERYVOLTAGE, value)
                .apply();

    }
    public boolean getLowBattery(){
        return mPref.getBoolean(Constants.LOWBATTERYVOLTAGE, false);
    }
    public void setHighCoolant(boolean value){
        mPref.edit()
                .putBoolean(Constants.HIGHCOOLANTTEMPERATURE, value)
                .apply();

    }
    public boolean getHighCoolant(){
        return mPref.getBoolean(Constants.HIGHCOOLANTTEMPERATURE, false);
    }


    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }



}
