package blackflame.com.zymepro.ui.home.multicar;

import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.home.singlecar.CarcountModel;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MulticarPresenter {

  View view;
  public MulticarPresenter(View view){
    this.view=view;

  }

public void parseListData(JSONObject object){
  try {
    ArrayList<CarcountModel> list=new ArrayList<>();
   int count_active=0;

    String hit_status = object.getString("status");
    if (hit_status.equals("SUCCESS")) {
      JSONObject carData = object.getJSONObject("msg");
      if (carData.has("onesignal_status")) {
        CommonPreference.getInstance().setOneSignalNotification(carData.getBoolean("onesignal_status"));

      }
      final JSONArray array_car_list=carData.getJSONArray("car_list");
      final int length=array_car_list.length();
      list.clear();
      view.clearMap();
      for(int i=0;i<length;i++){
        JSONObject object_cardata=array_car_list.getJSONObject(i);
        CarcountModel model=new CarcountModel();
        model.setModel(object_cardata.getString("model"));
        model.setBrand(object_cardata.getString("brand"));
        model.setRegistration(object_cardata.getString("registration_number"));
        model.setStatus(object_cardata.getString("status"));
        String status=object_cardata.getString("status");
        model.setNickName(object_cardata.getString("nickname"));
        JSONArray array = object_cardata.getJSONArray("last_location");
        if (array.length()>0) {
          model.setLatitude(array.getDouble(0));
          model.setLongitude(array.getDouble(1));
        }
        model.setImei(object_cardata.getString("IMEI"));
        if(status.equals("ACTIVE")){
          count_active++;
          view.setActiveCar(model);
        }else{
          view.setInactiveCar(model);
        }
        list.add(model);
      }
      view.setList(list);
      view.setCount(count_active,(length-count_active));
    }




  } catch (JSONException e) {
    e.printStackTrace();
  }catch (Exception ex){

  }



}
public void parseAddressData(JSONObject object){
  try {
    if (object.getJSONArray("results").length() > 0) {
      String addre = null;
      String result=null;

      addre = ((JSONArray) object.get("results")).getJSONObject(0).getString("formatted_address");

      String[] address_array = addre.split(",");
      StringBuffer buffer = new StringBuffer();

      for (int i = 0; i < address_array.length - 2; i++) {
        result = result + address_array[i];
        if (i == address_array.length - 3) {
          buffer.append(address_array[i]);
        } else {
          buffer.append(address_array[i] + ",");
        }
      }

      view.setAddress(buffer.toString());


    }


  } catch (JSONException e) {
    e.printStackTrace();

  }
}



  public interface View{
    void setList(ArrayList<CarcountModel> list);
   void setActiveCar(CarcountModel model);
   void setInactiveCar(CarcountModel model);
   void setCount(int active,int incative);
   void setAddress(String address);
   void clearMap();

  }

}
