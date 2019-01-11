package blackflame.com.zymepro.constant;

import android.app.Activity;
import blackflame.com.zymepro.R;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import java.util.HashMap;

public final class ActivityConstants {







  public static final String [] STATELIST={
      "Andaman and Nicobar Islands",
      "Andhra Pradesh",
      "Arunachal Pradesh",
      "Assam",
      "Bihar",
      "Chandigarh",
      "Chhattisgarh",
      "Dadra and Nagar Haveli",
      "Daman and Diu",
      "Delhi",
      "Goa",
      "Gujarat",
      "Haryana",
      "Himachal Pradesh",
      "Jammu and Kashmir",
      "Jharkhand",
      "Karnataka",
      "Kerala",
      "Lakshadweep",
      "Madhya Pradesh",
      "Maharashtra",
      "Manipur",
      "Meghalaya",
      "Mizoram",
      "Nagaland",
      "Odisha",
      "Punjab",
      "Rajasthan",
      "Sikkim",
      "Tamil Nadu",
      "Telangana",
      "Tripura",
      "Uttarakhand",
      "Uttar Pradesh",
      "West Bengal",

  };

  private static HashMap<String,String> getDisplayMap(){
    HashMap<String,String> map=new HashMap<>();
    map.put("IN-AP","Andhra Pradesh");
    map.put("IN-AR","Arunachal Pradesh");
    map.put("IN-AS","Assam");
    map.put("IN-BR","Bihar");
    map.put("IN-CT","Chhattisgarh");
    map.put("IN-GA","Goa");
    map.put("IN-GJ","Gujarat");
    map.put("IN-HR","Haryana");
    map.put("IN-HP","Himachal Pradesh");
    map.put("IN-JK","Jammu and Kashmir");
    map.put("IN-JH","Jharkhand");
    map.put("IN-KA","Karnataka");
    map.put("IN-KL","Kerala");
    map.put("IN-MP","Madhya Pradesh");
    map.put("IN-MH","Maharashtra");
    map.put("IN-MN","Manipur");
    map.put("IN-ML","Meghalaya");
    map.put("IN-MZ","Mizoram");
    map.put("IN-NL","Nagaland");
    map.put("IN-OR","Odisha");
    map.put("IN-PB","Punjab");
    map.put("IN-RJ","Rajasthan");
    map.put("IN-SK","Sikkim");
    map.put("IN-TN","Tamil Nadu");
    map.put("IN-TG","Telangana");
    map.put("IN-TR","Tripura");
    map.put("IN-UT","Uttarakhand");
    map.put("IN-UP","Uttar Pradesh");
    map.put("IN-WB","West Bengal");
    map.put("IN-AN","Andaman and Nicobar Islands");
    map.put("IN-CH","Chandigarh");
    map.put("IN-DN","Dadra and Nagar Haveli");
    map.put("IN-DD","Daman and Diu");
    map.put("IN-DL","Delhi");
    map.put("IN-LD","Lakshadweep");

    return map;
  }

 public static  String[]  CC={"CC","600","700","800","900","1000","1100","1200","1300","1400","1500","1600","1700","1800","1900","2000","2100","2200","2300","2400","2500","2600","2700","2800","2900","3000","3100","3200"};
  public static String getSelectedStateCode(String state){

    HashMap<String,String> map=getStateMap();
    return map.get(state);}
  private static  HashMap<String,String> getStateMap(){
    HashMap<String,String> map=new HashMap<>();
    map.put("Andhra Pradesh","IN-AP");
    map.put("Arunachal Pradesh","IN-AR");
    map.put("Assam","IN-AS");
    map.put("Bihar","IN-BR");
    map.put("Chhattisgarh","IN-CT");
    map.put("Goa","IN-GA");
    map.put("Gujarat","IN-GJ");
    map.put("Haryana","IN-HR");
    map.put("Himachal Pradesh","IN-HP");
    map.put("Jammu and Kashmir","IN-JK");
    map.put("Jharkhand","IN-JH");
    map.put("Karnataka","IN-KA");
    map.put("Kerala","IN-KL");
    map.put("Madhya Pradesh","IN-MP");
    map.put("Maharashtra","IN-MH");
    map.put("Manipur","IN-MN");
    map.put("Meghalaya","IN-ML");
    map.put("Mizoram","IN-MZ");
    map.put("Nagaland","IN-NL");
    map.put("Odisha","IN-OR");
    map.put("Punjab","IN-PB");
    map.put("Rajasthan","IN-RJ");
    map.put("Sikkim","IN-SK");
    map.put("Tamil Nadu","IN-TN");
    map.put("Telangana","IN-TG");
    map.put("Tripura","IN-TR");
    map.put("Uttarakhand","IN-UT");
    map.put("Uttar Pradesh","IN-UP");
    map.put("West Bengal","IN-WB");
    map.put("Andaman and Nicobar Islands","IN-AN");
    map.put("Chandigarh","IN-CH");
    map.put("Dadra and Nagar Haveli","IN-DN");
    map.put("Daman and Diu","IN-DD");
    map.put("Delhi","IN-DL");
    map.put("Lakshadweep","IN-LD");

    return map;}

  public static void showAlert(Activity activity,String msg){
//        new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
//                .setTitleText("Error" )
//                .setContentText(msg)
//                .setConfirmText("Ok!")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();

    new AwesomeErrorDialog(activity)
        .setTitle("Error")
        .setMessage(msg)
        .setColoredCircle(R.color.dialogErrorBackgroundColor)
        .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
        .setCancelable(true)
        .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
        .setButtonText("Ok")
        .setErrorButtonClick(new Closure() {
          @Override
          public void exec() {
            // click
          }
        })
        .show();
  }



  public static String getDisplayText(String code){
    HashMap<String,String> map=getDisplayMap();
    return   map.get(code);
  }
}
