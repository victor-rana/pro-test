package blackflame.com.zymepro.ui.home;

import org.json.JSONObject;

public interface MqttDataListener {


  void setData(JSONObject data,String topic);


}
