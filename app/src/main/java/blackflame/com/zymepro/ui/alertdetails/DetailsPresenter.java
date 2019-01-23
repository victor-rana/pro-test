package blackflame.com.zymepro.ui.alertdetails;
import blackflame.com.zymepro.util.LogUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsPresenter {
  View view;

  public DetailsPresenter(View v){
    this.view=v;
  }

  public void parseAddress(JSONObject response){
    try {
      if (response.getJSONArray("results").length() > 0) {
        String addre = null;
        String result=null;

        addre = ((JSONArray) response.get("results")).getJSONObject(0).getString("formatted_address");

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


        view.updateAddress(buffer.toString());

        LogUtils.error("Alerts",buffer.toString());


      }


    } catch (JSONException e) {
      e.printStackTrace();

    }
  }


  interface View{
    void updateAddress(String address);
  }

}
