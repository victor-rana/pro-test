package blackflame.com.zymepro.onesignal.helper;
import blackflame.com.zymepro.db.CommonPreference;
import com.onesignal.OneSignal;


public class OneSignalUtility {
  public static void setSubscribe(){
    OneSignal.setSubscription(true);
  }

  public static void removeSubscription(){
    OneSignal.setSubscription(false);
  }
  public static void removeTags(){
    OneSignal.sendTag("name","");
    OneSignal.sendTag("email","");

  }

  public static void updateTags(){
    OneSignal.sendTag("name", CommonPreference.getInstance().getUserName());
    OneSignal.sendTag("email", CommonPreference.getInstance().getEmail());
  }




}
