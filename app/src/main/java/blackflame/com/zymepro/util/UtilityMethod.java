package blackflame.com.zymepro.util;



import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.TypedValue;


import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.R;

import blackflame.com.zymepro.ui.login.LoginActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UtilityMethod {
  static   String TAG=UtilityMethod.class.getCanonicalName();
    
    public static String calculateTime(int totalSeconds){
        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        String suffix_hour,suffix_min,suffix_second;
    
        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;
     if (hours>1){
         suffix_hour=" hrs ";
         
     }else{
         suffix_hour=" hr ";
     
     }
     if (minutes>1){
         suffix_min=" mins ";
     
     }else{
         suffix_min=" min ";
     
     }
     if (seconds>1){
         suffix_second=" sec ";
         
     
     }else{
         suffix_second=" sec ";
     
     }
     
        return hours+suffix_hour+minutes+suffix_min+seconds+suffix_second;
    }

  public static Bitmap resizeMapIcons(String iconName, int width, int height) {
      Context context=Prosingleton.getAppContext();
    Bitmap imageBitmap = BitmapFactory
        .decodeResource(context.getResources(), context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
    return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
  }
    
    
    
    
    public static String timeConversion(int totalSeconds ,boolean isHourRequired ) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        String time=null;

        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;
        String.format("%02d:%02d", minutes ,seconds);
        //return minutes + ":" + seconds;
        if (isHourRequired){
          time=String.format("%02d:%02d:%02d", hours,minutes ,seconds);
        }else{
          time= String.format("%02d:%02d", totalMinutes ,seconds);
        }


        return time;
    }
    
    
    public static String getDateChart(String OurDate) {
        try
        {
            Log.e(TAG, "getDateOnly:Chart "+OurDate );
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat("d"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);
            
            //Log.d("OurDate", OurDate);
        }
        catch (Exception e)
        {
            OurDate = "--";
        }
        return OurDate;
    }
    
    
    public static String getDateOnly(String OurDate) {
        try
        {
            Log.e(TAG, "getDateOnly: "+OurDate );
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.SSSZZZ");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat("d"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);
            
            //Log.d("OurDate", OurDate);
        }
        catch (Exception e)
        {
            OurDate = "--";
        }
        return OurDate;
    }
    
    
    
    
    public static String getUtcDate(String OurDate) {
        try
        {
            Log.e(TAG, "getDateOnly: "+OurDate );
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);
            
        }
        catch (Exception e)
        {
            OurDate = "--";
        }
        return OurDate;
    }
    
    public static String getDateMonth(String OurDate) {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.SSSZZZ");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM yyyy"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);
            
            //Log.d("OurDate", OurDate);
        }
        catch (Exception e)
        {
            OurDate = "--";
        }
        return OurDate;
    }
    
    
    public static String getDay(String OurDate) {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.SSSZZZ");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(OurDate);
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);
            
            //Log.d("OurDate", OurDate);
        }
        catch (Exception e)
        {
            OurDate = "--";
        }
        return OurDate;
    }
    
    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }
    
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
    
    
    
    public static int getStyle(String style){
        int id=0;
        switch (style){
            case "Military Dark":
                id= R.raw.military;
                
                break;
            case "Exotic Original":
                id=R.raw.exotic_original;
                break;
            case "Nature Green":
                id=R.raw.nature_green;
                break;
            case "Colorful Bliss":
                id=R.raw.colorful_bliss;
                break;
            case "Black n White":
                id=R.raw.black_white;
                break;
            case "Golden Hue":
                id=R.raw.golden;
                
                break;
            case "Watery Blue":
                id=R.raw.watery;
                break;
            case "Pink Hues":
                id=R.raw.pink;
                break;
            case "Midnight Commander":
                id=R.raw.midnight;
                break;
            case "Sober Vintage":
                id=R.raw.sober;
                break;
            case "NIGHT":
                id=R.raw.style_night_mode;
                break;
            case "DEFAULT":
                id=R.raw.style_map;
                break;
                
            
                
        }
        return id;
    }
    public static void clearAndLogout(Context mcontext){

//      SettingPreferences.initializeInstance(mcontext);
//      SettingPreferences.getInstance().clear();
//      CommonPreference.getInstance().clear();
     // OneSignal.setSubscription(false);
      Intent intent = new Intent(mcontext, LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intent.putExtra("logout",1);
      mcontext.startActivity(intent);


    }

  public static String getDate(String OurDate) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      Date value = formatter.parse(OurDate);


      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa"); //this format changeable
      dateFormatter.setTimeZone(TimeZone.getDefault());
      OurDate = dateFormatter.format(value);

      //Log.d("OurDate", OurDate);
    } catch (Exception e) {
      OurDate = "00-00-0000 00:00";

    }
    return OurDate;
  }


  public static String get12HourTime(int selectedHour,int selectedMinutes){
    String status = " AM";

    if(selectedHour > 11)
    {
      // If the hour is greater than or equal to 12
      // Then the current AM PM status is PM
      status = " PM";
    }

    // Initialize a new variable to hold 12 hour format hour value
    int hour_of_12_hour_format;

    if(selectedHour > 11){

      // If the hour is greater than or equal to 12
      // Then we subtract 12 from the hour to make it 12 hour format time
      hour_of_12_hour_format = selectedHour - 12;
    }
    else {
      hour_of_12_hour_format = selectedHour;
    }
    String time;
    if(selectedMinutes==0){
      time=  hour_of_12_hour_format + ":" + "00"+status;

    }else{
      time=hour_of_12_hour_format + ":" + selectedMinutes+status;
    }

    return time;
  }


  public static String getTime(int seconds){
    int  hours = seconds / 3600;

    int   minutes = (seconds % 3600) / 60;
    String    timeString = get12HourTime(hours,minutes);

    return timeString;}


}
