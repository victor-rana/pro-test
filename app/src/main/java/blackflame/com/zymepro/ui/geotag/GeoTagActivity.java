package blackflame.com.zymepro.ui.geotag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.geotag.adapter.GeoTagAdapter;
import blackflame.com.zymepro.ui.geotag.adapter.GeoTagAdapter.CardClickListener;
import blackflame.com.zymepro.ui.geotag.model.GeotagResponse;
import blackflame.com.zymepro.ui.geotag.savegeotag.SaveGeotagActivity;
import blackflame.com.zymepro.util.ToastUtils;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import java.util.ArrayList;
import org.json.JSONObject;

public class GeoTagActivity extends BaseActivity implements CardClickListener,AppRequest,GeotagPresenter.View {
  RecyclerView recyclerView_geotag;
  GeoTagAdapter adapter;
  ArrayList<GeotagResponse> list;
  Toolbar toolbar;
  TextView textView_title;
  ProgressBar progressBarHolder;
  LinearLayout no_tag;
  int delete_position=-1;
  GeotagPresenter presenter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_geo_tag);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new GeotagPresenter(this);
    initViews();
  }

  private void initViews(){

    toolbar= findViewById(R.id.toolbar_common);
    textView_title= findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,textView_title,"Geotag");
    recyclerView_geotag= findViewById(R.id.recycler_geotag);
    recyclerView_geotag.setItemAnimator(new DefaultItemAnimator());
    recyclerView_geotag.setLayoutManager(new LinearLayoutManager(GeoTagActivity.this, LinearLayoutManager.VERTICAL, false));
    list=new ArrayList<>();
    adapter=new GeoTagAdapter(GeoTagActivity.this,list,GeoTagActivity.this,this);
    recyclerView_geotag.setAdapter(adapter);
    progressBarHolder=findViewById(R.id.pBar);
    no_tag=findViewById(R.id.ll_notag);
    TextView tv_add_geotag=findViewById(R.id.tv_add_geo_Tag);
    tv_add_geotag.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent=new Intent(GeoTagActivity.this,SaveGeotagActivity.class);
        startActivity(intent);
      }
    });

    loadGeotag();

  }

  @Override
  public void onCardClick(int pos, View v) {
    final int position=pos;
    new AwesomeInfoDialog(this)
        .setTitle("Alert")
        .setMessage("Are you sure you want to delete this geotag?")
        .setColoredCircle(R.color.colorAccent)
        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
        .setCancelable(true)
        .setPositiveButtonText("Yes")
        .setPositiveButtonbackgroundColor(R.color.colorAccent)
        .setPositiveButtonTextColor(R.color.white)
        .setNegativeButtonText("No")
        .setNegativeButtonbackgroundColor(R.color.colorAccent)
        .setNegativeButtonTextColor(R.color.white)
        .setPositiveButtonClick(new Closure() {
          @Override
          public void exec() {
            delete_position=position;

            deleteGeoTag(list.get(position).getId());
            //new DeleteGeoTag().execute(list.get(position).getId(),String.valueOf(position));
          }
        })
        .setNegativeButtonClick(new Closure() {
          @Override
          public void exec() {
            //click
          }
        })
        .show();

  }

  private void loadGeotag(){
    ApiRequests.getInstance().load_geotag(GlobalReferences.getInstance().baseActivity,GeoTagActivity.this);
  }
  private void deleteGeoTag(String id){
    ApiRequests.getInstance().delete_geotag(GlobalReferences.getInstance().baseActivity,GeoTagActivity.this,id);
  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {
    progressBarHolder.setVisibility(View.VISIBLE);
  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    if (listener.getTag().equals("load_geotag")){
      presenter.parseTagResponse(listener.getJsonResponse());

    }else if (listener.getTag().equals("delete_geotag")){
      presenter.parseDeleteResponse(listener.getJsonResponse(),delete_position);
    }


  }

  @Override
  public <T> void onRequestFailed(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestStarted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestFailed(BaseTaskJson<JSONObject> listener, RequestParam requestParam) {

  }

  @Override
  public void onResponse(JSONObject response) {

  }

  @Override
  public void setList(ArrayList<GeotagResponse> list) {
    progressBarHolder.setVisibility(View.GONE);
    progressBarHolder.invalidate();

    if (this.list!=null){
      this.list.addAll(list);
    }
    if (no_tag.getVisibility()==View.VISIBLE){
      no_tag.setVisibility(View.GONE);
    }
    if (recyclerView_geotag.getVisibility()==View.GONE){
      recyclerView_geotag.setVisibility(View.VISIBLE);
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void deleteItem(int position,String msg) {
    progressBarHolder.setVisibility(View.GONE);
    list.remove(position);
    adapter.notifyItemRemoved(position);
    adapter.notifyDataSetChanged();
    ToastUtils.showShort(msg);
    int length=list.size();
    if (length==0){
      no_tag.setVisibility(View.VISIBLE);
      recyclerView_geotag.setVisibility(View.GONE);
    }
  }

  @Override
  public void setNotag() {
    recyclerView_geotag.setVisibility(View.GONE);
    progressBarHolder.setVisibility(View.GONE);
    no_tag.setVisibility(View.VISIBLE);
  }
}
