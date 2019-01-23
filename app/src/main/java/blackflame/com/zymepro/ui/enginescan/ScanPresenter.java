package blackflame.com.zymepro.ui.enginescan;

import static blackflame.com.zymepro.util.UtilityMethod.getDate;

import android.util.Log;
import android.view.View;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.enginescan.model.ErrorCode;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScanPresenter {

  View view;
  public ScanPresenter(View v){
    this.view=v;
  }

  public void parsePreviousData(JSONObject object){

    try {
      ArrayList<String> list_error=new ArrayList<>();
      ArrayList<ErrorCode> list_data=new ArrayList<>();

      JSONObject carData=object.getJSONObject("msg");
      JSONArray jsonArray_error_code=carData.getJSONArray("error_code");
      for (int i=0;i<jsonArray_error_code.length();i++){
        JSONObject jsonObject=jsonArray_error_code.getJSONObject(i);
        list_error.add(jsonObject.getString("error_code"));
        ErrorCode errorCode=new ErrorCode();
        errorCode.setCode(jsonObject.getString("error_code"));
        errorCode.setDescription(jsonObject.getString("description"));
        errorCode.setTitle(jsonObject.getString("title"));
        list_data.add(errorCode);
      }
      view.setErrorCode(list_error);
      view.setListData(list_data);
      view.setUpdateTime(getDate(carData.getString("last_update_date")));
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }








  public void parseCurrentData(JSONObject data){
    try {
      ArrayList<String> list_error=new ArrayList<>();
      ArrayList<ErrorCode> list_data=new ArrayList<>();



      JSONObject object_errorData = data.getJSONObject("msg");
      JSONArray errorArray = object_errorData.getJSONArray("error_code");
      if (object_errorData.has("last_update_date")){
      String  last_update_time = object_errorData.getString("last_update_date");
        view.setUpdateTime(last_update_time);

      }
      if (object_errorData.has("coolant")){
        view.setCoolant(object_errorData.getString("coolant").concat(" °C")  );

      }

      if (object_errorData.has("voltage")){
        view.setVoltage(object_errorData.getString("voltage").concat(" V"));

      }

      int length = errorArray.length();
      for (int i = 0; i < length; i++) {
        JSONObject dataObject = errorArray.getJSONObject(i);
        ErrorCode error = new ErrorCode();
        list_error.add(dataObject.getString("error_code"));
        error.setTitle(dataObject.getString("title"));
        error.setCode(dataObject.getString("error_code"));
        error.setDescription(dataObject.getString("description"));
        list_data.add(error);
      }



    }catch (JSONException ex){}


  }


  interface View{

    void setUpdateTime(String time);
    void setCoolant(String coolant);
    void setVoltage(String voltage);
    void setErrorCode(ArrayList<String> error);
    void setListData(ArrayList<ErrorCode> data);

  }

}
