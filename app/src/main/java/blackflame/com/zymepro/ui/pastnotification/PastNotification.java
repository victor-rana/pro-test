package blackflame.com.zymepro.ui.pastnotification;

import static blackflame.com.zymepro.util.UtilityMethod.getDate;

import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.pastnotification.adapter.NotificationAdapter;
import blackflame.com.zymepro.ui.pastnotification.listener.NotificationClickListener;
import blackflame.com.zymepro.ui.pastnotification.model.HistoricResponse;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.view.custom.swiperefresh.SwipyRefreshLayout;
import blackflame.com.zymepro.view.custom.swiperefresh.SwipyRefreshLayout.OnRefreshListener;
import blackflame.com.zymepro.view.custom.swiperefresh.SwipyRefreshLayoutDirection;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PastNotification extends BaseActivity implements NotificationClickListener,OnRefreshListener,AppRequest {
  private SwipyRefreshLayout mSwipeRefreshLayout = null;
  Context context;
  int page_count=0;
  ArrayList<HistoricResponse> list_notification;
  NotificationAdapter adapter;
  RecyclerView recyclerView;
  int last_position;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_past_notification);
    GlobalReferences.getInstance().baseActivity=this;

    initViews();
  }
  private  void initViews(){
    Toolbar  toolbar_Notification= findViewById(R.id.toolbar_common);
    TextView  textView_Title= findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar_Notification,textView_Title,"Past Notifications");
    recyclerView = findViewById(R.id.notification_list);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(linearLayoutManager);
    list_notification=new ArrayList<>();
    adapter=new NotificationAdapter(list_notification,this);
    adapter.setHasStableIds(true);
    recyclerView.setAdapter(adapter);
    CommonPreference.initializeInstance(this);
    loadData(page_count);
    // enable pull down to refresh
    mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    mSwipeRefreshLayout.setDistanceToTriggerSync(1);
    mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#2da9e1"), Color.GREEN, Color.BLUE, Color.YELLOW);
    mSwipeRefreshLayout.setOnRefreshListener(this);
  }
  @Override
  public void itemClick(int position, View view) {
    HistoricResponse response=list_notification.get(position);
    String title=response.getTitle();
    String string= Html.fromHtml(response.getBody()).toString();
    String date=getDate(response.getTime());
    new AwesomeInfoDialog(this)
        .setTitle(title)
        .setMessage(date+"\n\n"+string.substring(1, string.length()-1))
        .setColoredCircle(R.color.colorAccent)
        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
        .setCancelable(true)
        .setPositiveButtonClick(new Closure() {
          @Override
          public void exec() {
            //click
          }
        })
        .show();

  }
  private void loadData(int page){
    ApiRequests.getInstance().get_past_notification(GlobalReferences.getInstance().baseActivity,PastNotification.this,page);
  }
  @Override
  public void onRefresh(SwipyRefreshLayoutDirection direction) {
    loadData(page_count);
  }
  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }
  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    adapter.notifyDataSetChanged();
    // after refresh is done, remember to call the following code
    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
      mSwipeRefreshLayout.setRefreshing(false);  // This hides the spinner
    }
    try {

      JSONObject jObject = listener.getJsonResponse();
      JSONObject jsonObject_Data = jObject.getJSONObject("msg");
      mSwipeRefreshLayout.setRefreshing(false);
      JSONArray notification_list = jsonObject_Data.getJSONArray("notification_history");
      if (notification_list.length() > 0) {

        ArrayList<HistoricResponse> responses = new ArrayList<>();
        for (int i = 0; i < notification_list.length(); i++) {
          JSONObject jsonObject = notification_list.getJSONObject(i);
          // Log.e("Data", "onResponse: "+jsonObject );
          HistoricResponse notificationResponse = new HistoricResponse();
          notificationResponse.setBody(jsonObject.getString("body"));
          notificationResponse.setTime(jsonObject.getString("generation_time"));
          String title_text = jsonObject.getString("title");
          if (title_text != null) {
            String title = title_text.substring(1, title_text.length() - 1);

            notificationResponse.setTitle(title);
          }

          responses.add(notificationResponse);

        }
        list_notification.addAll(responses);
        adapter.notifyDataSetChanged();
        //adapter.notifyItemInserted(last_position+1);
        recyclerView.scrollToPosition(last_position + 1);
        page_count++;
        last_position += notification_list.length();
      }
    }catch (JSONException ex){

    }
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

    Analytics.index(PastNotification.this,"PastNotification");

  }
}
