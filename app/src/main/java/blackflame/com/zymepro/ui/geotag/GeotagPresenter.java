package blackflame.com.zymepro.ui.geotag;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import blackflame.com.zymepro.ui.geotag.model.GeotagResponse;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeotagPresenter {


  View view;
  public GeotagPresenter(View v){
    this.view=v;
  }


  public void parseTagResponse(JSONObject data){
    try{
      ArrayList<GeotagResponse> list=new ArrayList<>();

      String status=data.getString("status");
      if (status !=null && status.equals("SUCCESS")){
        JSONArray list_data=data.getJSONArray("tags");
        int length=list_data.length();
        if(length>0){
          for (int i=0;i<length;i++){
            JSONObject data_tag=list_data.getJSONObject(i);
            GeotagResponse geotagResponse=new GeotagResponse();
            geotagResponse.setAddress(data_tag.getString("address"));
            geotagResponse.setId(data_tag.getString("_id"));
            geotagResponse.setLat(data_tag.getJSONArray("location").getDouble(0));
            geotagResponse.setLng(data_tag.getJSONArray("location").getDouble(1));
            geotagResponse.setTitle(data_tag.getString("tag_name"));
            geotagResponse.setType(data_tag.getString("geotag_type"));

            list.add(geotagResponse);
          }
          view.setList(list);

        }else{

          view.setNotag();
        }

      }


    }catch (JSONException ex){}

  }


  public void parseDeleteResponse(JSONObject data,int position){
    try{
      String status=data.getString("status");
      final String msg=data.getString("msg");
      if (status !=null && status.equals("SUCCESS")){
        view.deleteItem(position,msg);

      }


    }catch (JSONException ex){


    }

  }



  interface View{
    void setList(ArrayList<GeotagResponse> list);
    void deleteItem(int position,String msg);

    void setNotag();

  }
}
