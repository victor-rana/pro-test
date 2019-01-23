package blackflame.com.zymepro.onesignal.helper;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import blackflame.com.zymepro.ui.ai.AlexaActivity;
import blackflame.com.zymepro.ui.ai.GoogleActivity;
import blackflame.com.zymepro.ui.alertdetails.AlertDetails;
import blackflame.com.zymepro.ui.document.ActivityDocuments;
import blackflame.com.zymepro.ui.enginescan.EngineScanActivity;
import blackflame.com.zymepro.ui.geotag.GeoTagActivity;
import blackflame.com.zymepro.ui.home.MainActivity;
import blackflame.com.zymepro.ui.message.MessageFromTeam;
import blackflame.com.zymepro.ui.profile.ActivityProfile;
import blackflame.com.zymepro.ui.refer.ReferActivity;
import blackflame.com.zymepro.ui.setting.SettingActivity;
import blackflame.com.zymepro.ui.shopping.ZymeShop;
import blackflame.com.zymepro.ui.tripdetails.TripDetailsActivity;
import blackflame.com.zymepro.ui.wbview.WebActivity;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import org.json.JSONObject;

public class NotificationHelperMethod {



  public static Intent getActivityToLaunch(String name,Context context) {
    Intent activityTOlaunch=null;
    switch (name){
      case "HOME":
        activityTOlaunch=new Intent(context,MainActivity.class);
        break;
      case "TRIP_DETAILS":
        activityTOlaunch=new Intent(context,TripDetailsActivity.class);
        break;
      case "PROFILE":
        activityTOlaunch=new Intent(context,ActivityProfile.class);
        break;
      case "MESSAGE":
        activityTOlaunch=new Intent(context,MessageFromTeam.class);
        break;
      case "REFER":
        activityTOlaunch=new Intent(context,ReferActivity.class);
        break;
      case "SETTING":
        activityTOlaunch=new Intent(context,SettingActivity.class);
        break;
      case "ALERTS":
        activityTOlaunch=new Intent(context,AlertDetails.class);
         break;
      case "SHOP":
        activityTOlaunch=new Intent(context,ZymeShop.class);
        break;
      case "GOOGLE":
       activityTOlaunch=new Intent(context,GoogleActivity.class);
        break;
      case "ALEXA":
        activityTOlaunch=new Intent(context,AlexaActivity.class);
        break;
      case "GEOTAG":
        activityTOlaunch=new Intent(context,GeoTagActivity.class);
        break;
      case "ENGINESCAN":
        activityTOlaunch=new Intent(context,EngineScanActivity.class);
        break;
      case "DOCUMENT":
        activityTOlaunch=new Intent(context,ActivityDocuments.class);
        break;
      case "WEBVIEW":
        activityTOlaunch=new Intent(context,WebActivity.class);
        break;
     default:
          activityTOlaunch=new Intent(context,MainActivity.class);
    }
        return activityTOlaunch;

  }

  private static Bundle setCommonData(Bundle bundle,JSONObject data) throws Exception {
    if(data.has("time")){
      bundle.putString("time",data.getString("time"));
    }
    if (data.has("title")){
      bundle.putString("title",data.getString("title"));
    }
    if (data.has("sound")){
      bundle.putString("sound",data.getString("sound"));
    }
    if (data.has("screen")){
      bundle.putString("screen",data.getString("screen"));

    }
    if (data.has("identifier")){
      bundle.putString("identifier",data.getString("identifier"));
    }
    if (data.has("IMEI")) {
      bundle.putString("IMEI", data.getString("IMEI"));
    }
    if (data.has("registration_number")) {
      bundle.putString("registration_number", data.getString("registration_number"));
    }
    if (data.has("car_count")) {

      bundle.putString("car_count", data.getString("car_count"));
    }
    if (data.has("summary")) {
      bundle.putString("summary", data.getString("summary"));
    }
    if (data.has("message")){
      bundle.putString("message", data.getString("message"));
    }
    return bundle;

  }

  public static Bundle getBundle(JSONObject data) throws Exception {
    Bundle bundle=new Bundle();
    String screen=data.getString("screen");
    switch (screen){
      case "HOME":
        bundle.clear();
        setCommonData(bundle,data);
        if (data.has("rate_message")){
          bundle.putString("rate_message",data.getString("rate_message"));
        }

        break;
      case "TRIP_DETAILS":
        bundle.clear();
        setCommonData(bundle,data);

        if (data.has("trip_id")){
          bundle.putString("trip_id",data.getString("trip_id"));

        }
        if (data.has("summary")) {
          bundle.putString("summary", data.getString("summary"));
        }
        if (data.has("car_id")){
          bundle.putString("car_id",data.getString("car_id"));

        }
        if (data.has("date")){
          bundle.putString("date",data.getString("date"));

        }
        if (data.has("start_address")){
          bundle.putString("start_address",data.getString("start_address"));

        }

        if (data.has("end_address")){
          bundle.putString("end_address",data.getString("end_address"));
        }

        if (data.has("trip_time")){
          bundle.putString("trip_time",data.getString("trip_time"));
        }

        break;
      case "PROFILE":
        bundle.clear();
        setCommonData(bundle,data);
        break;
      case "MESSAGE":
        bundle.clear();
        setCommonData(bundle,data);
        if (data.has("team_message")){
          bundle.putString("team_message",data.getString("team_message"));
        }
        break;
      case "REFER":
        bundle.clear();
        setCommonData(bundle,data);
        break;
      case "SETTING":
        bundle.clear();
        setCommonData(bundle,data);
        break;
      case "ALERTS":
        bundle.clear();
        setCommonData(bundle,data);
        if (data.has("car_count")){
          bundle.putInt("car_count",data.getInt("car_count"));

        }
        if (data.has("date")){
          bundle.putString("date",data.getString("date"));
        }
        if (data.has("alert_name")){
          bundle.putString("alert_name",data.getString("alert_name"));

        }
        if (data.has("lat")){
          bundle.putDouble("lat",data.getDouble("lat"));

      }
      if (data.has("lng")){
          bundle.putDouble("lng",data.getDouble("lng"));
      }
        break;
      case "SHOP":
        bundle.clear();
        setCommonData(bundle,data);
        break;
      case "GOOGLE":
        bundle.clear();
        setCommonData(bundle,data);
        break;
      case "ALEXA":
        bundle.clear();
        setCommonData(bundle,data);
        break;
      case "GEOTAG":
        bundle.clear();
        setCommonData(bundle,data);
        break;
      case "ENGINESCAN":
        bundle.clear();
        setCommonData(bundle,data);
        break;
      case "DOCUMENT":
        bundle.clear();
        setCommonData(bundle,data);
        break;

      case "WEBVIEW":
        bundle.clear();
        setCommonData(bundle,data);
        if (data.has("url")){
          bundle.putString("link",data.getString("url"));
        }
           break;
     default:
          bundle.clear();
          setCommonData(bundle,data);
    }
    return bundle;

  }


  public static int getRequestCode() {
    Random rnd = new Random();
    return 100 + rnd.nextInt(9000);
  }
  /**
   * Downloading push notification image before displaying it in
   * the notification tray
   */
  public static Bitmap getBitmapFromURL(String strURL) {
    try {
      URL url = new URL(strURL);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoInput(true);
      connection.connect();
      InputStream input = connection.getInputStream();
      Bitmap myBitmap = BitmapFactory.decodeStream(input);
      return myBitmap;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }



}
