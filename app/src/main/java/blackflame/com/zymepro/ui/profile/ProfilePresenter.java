package blackflame.com.zymepro.ui.profile;

import static blackflame.com.zymepro.util.UtilityMethod.getDate;

import android.util.Log;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.profile.model.ProfileModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfilePresenter {

  View view;

  public ProfilePresenter(View view){

    this.view=view;
  }





    public void parseData(JSONObject data){
      String name=null,email=null,mobileNumber=null;


      try {
        ArrayList<ProfileModel> list_cardata=new ArrayList<>();
        JSONObject object = data.getJSONObject("msg");

        if (object.has("name")) {
          name = object.getString("name");
          CommonPreference.getInstance().setUserName(name);
        }
        email = CommonPreference.getInstance().getEmail();
        mobileNumber = object.getString("mobile_number");
        view.setEmail(email);
        view.setMobile(mobileNumber);
        view.setName(name);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


        JSONArray array_car = object.getJSONArray("car");
        for (int i = 0; i < array_car.length(); i++) {
          //email=object.getString("email");
          ProfileModel profileModel = new ProfileModel();
          JSONObject object_cardata = array_car.getJSONObject(i);
          profileModel.setBrand(object_cardata.getString("brand"));
          profileModel.setModel(object_cardata.getString("model"));
          profileModel.setEngine_type(object_cardata.getString("engine_type"));
          profileModel.setName(name + "'s car");
          profileModel.setStatus(object_cardata.getString("status"));
          profileModel.setImei(object_cardata.getString("IMEI"));
          profileModel.setEngineCc(object_cardata.getString("engine_capacity"));
          profileModel.setCar_id(object_cardata.getString("car_id"));
          if (object_cardata.getString("nickname").equals("null")) {
            profileModel.setNickName("--");
          } else {
            profileModel.setNickName(object_cardata.getString("nickname"));
          }

          try {
            Calendar now = Calendar.getInstance();
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String ist_time = simpleDateFormat.format(now.getTime());
            Date date_start = simpleDateFormat.parse(ist_time);
            Date date_end = simpleDateFormat
                .parse(getDate(object_cardata.getString("subscription_end")));
            long diff = date_end.getTime() - date_start.getTime();
            //  Log.e("date", "onResponse: "+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) ); ;

            profileModel.setSubscriptiondays(String.valueOf(TimeUnit.DAYS
                .convert((date_end.getTime() - date_start.getTime()), TimeUnit.MILLISECONDS)));
            Log.e("diff", "onResponse:days " + TimeUnit.DAYS
                .convert((date_end.getTime() - date_start.getTime()), TimeUnit.MILLISECONDS));
          }catch (ParseException ex) {
            Log.e("ex", "onResponse: " + ex.getMessage());
          }

          profileModel.setState(object_cardata.getString("state"));
          profileModel.setRegistration_number(object_cardata.getString("registration_number"));
          list_cardata.add(profileModel);

        }
        view.setData(list_cardata);
      }catch (JSONException ex){

      }


    }



  interface View{
    void setEmail(String email);
    void setName(String name);
    void setMobile(String mobile);
    void setData(ArrayList<ProfileModel> data);


  }


  private String getDate(String OurDate) {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      Date value = formatter.parse(OurDate);

      SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); //this format changeable
      dateFormatter.setTimeZone(TimeZone.getDefault());
      OurDate = dateFormatter.format(value);

      //Log.d("OurDate", OurDate);
    }
    catch (Exception e)
    {
      OurDate = "00-00-0000 00:00";
    }
    return OurDate;
  }
}
