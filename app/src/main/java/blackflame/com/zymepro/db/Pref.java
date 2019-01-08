package blackflame.com.zymepro.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Pref {
    private Context context;
    public SharedPreferences preferences;
    private final String IS_LOGGED_IN = "is_logged_in";
    private final String USER_NAME = "userName";
    private final String USER_EMAIL = "user_email";
    private final String USER_ID = "access_token";
    private final String APP_SETTINGS ="app_settings";
    private final String USER_PROFILE ="user_profile";
    private final String INTEREST_ADDED ="interest_added";
    private final String IS_FIRST_TIME="is_first_time";
    private final String ACCESS_TOKEN ="token";
    private final String FCM_TOKEN ="fcm_token";
    private final String BARBER_ID= "barber_id";
    private static final String CLIENTTOKEN="clienttoken";


    public Pref(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUserLoggedIn(boolean is_user_logged_in) {
        if (preferences != null)
            preferences.edit().putBoolean(IS_LOGGED_IN, is_user_logged_in).apply();
    }

    public boolean getIsUserLOggedIn() {
        if (preferences != null)
            return preferences.getBoolean(IS_LOGGED_IN, false);
        else return false;
    }


    public  void setClientToken(String token){
        if (preferences != null)
            preferences.edit().putString(CLIENTTOKEN, token).apply();
    }

    public String getClientToken(){
        if (preferences != null)
            return preferences.getString(CLIENTTOKEN, null);
        else return null;
    }

    public void setUserName(String userName) {
        if (preferences != null)
            preferences.edit().putString(USER_NAME, userName).apply();
    }

    public String getUserName() {
        if (preferences != null)
            return preferences.getString(USER_NAME, null);
        else return null;
    }

    public void setIsFirstTime(boolean isFirstTime) {
        if (preferences != null)
            preferences.edit().putBoolean(IS_FIRST_TIME, isFirstTime).apply();
    }

    public boolean getIsFirstTime() {
        if (preferences != null)
            return preferences.getBoolean(IS_FIRST_TIME,false);
        else return false;
    }

    public void setUserEmail(String userEmail) {
        if (preferences != null)
            preferences.edit().putString(USER_EMAIL, userEmail).apply();
    }

    public String getUserEmail() {
        if (preferences != null)
            return preferences.getString(USER_EMAIL, null);
        else return null;
    }


    public void setAccessToken(String accessToken) {
        if (preferences != null)
            preferences.edit().putString(ACCESS_TOKEN, accessToken).apply();
    }

    public String getAccessToken() {
        if (preferences != null)
            return preferences.getString(ACCESS_TOKEN, null);
        else return null;
    }




    public void clearPref(){
       preferences.edit().clear().apply();

    }
}
