package blackflame.com.zymepro.ui.message;

import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.util.Analytics;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageFromTeam extends BaseActivity implements AppRequest {
  TextView message;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_message_from_team);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
  }

  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Message");
    message= findViewById(R.id.message);
    message.setTextColor(Color.parseColor("#FFFFFF"));
    Bundle bundle=getIntent().getBundleExtra("bundle");
    if (bundle !=null){
      String message_from_team=bundle.getString("team_message");
      if(message_from_team!=null) {
        message.setText(message_from_team);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          message.setText(Html.fromHtml(message_from_team, Html.FROM_HTML_MODE_COMPACT));
        } else {
          message.setText(Html.fromHtml(message_from_team));
        }
      }
    }else {

      loadMessage();
    }
  }


  private void loadMessage(){
    ApiRequests.getInstance().load_message(GlobalReferences.getInstance().baseActivity,MessageFromTeam.this);

  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    try{
       String message_from_team=listener.getJsonResponse().getString("message");
      message.setText(message_from_team);

    }catch (JSONException ex){}

  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {
    doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {
    doGlobalLogout(listener.getVolleyError(),listener.getJsonResponse());
  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void indexScreen() {
    Analytics.index(MessageFromTeam.this,"MessageFromTeam");
  }
}
