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
      if (response.has("msg") ) {
        String addre = null;

        addre=response.getString("msg");



        view.updateAddress(addre);

        LogUtils.error("Alerts",addre);


      }


    } catch (JSONException e) {
      e.printStackTrace();

    }
  }


  interface View{
    void updateAddress(String address);
  }

}
