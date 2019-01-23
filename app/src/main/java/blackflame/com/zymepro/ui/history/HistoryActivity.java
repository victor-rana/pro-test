package blackflame.com.zymepro.ui.history;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import blackflame.com.zymepro.ui.history.adapter.HistoryAdapter;
import blackflame.com.zymepro.ui.history.dialog.ReportDialog;
import blackflame.com.zymepro.ui.history.model.TripHistory;
import blackflame.com.zymepro.util.NetworkUtils;
import blackflame.com.zymepro.util.ToastUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.json.JSONObject;

public class HistoryActivity extends BaseActivity implements HistoryPresenter.View,AppRequest,com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener,DateSelectedListener {
  RecyclerView listView;
  ArrayList<TripHistory> list_helper;
  HistoryAdapter adopter;
  LinearLayout linearLayout_notrip;
  private TextView timeTextView;
  LinearLayout pickdate;
  TextView dateTextView,yearTextview;
  TextView tv_totaldistance,tv_totaltime,tv_totalcost,tv_fuelconsumed;
  String[] months={"Jan","Feb","Mar","APR","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
  HistoryPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new HistoryPresenter(this);
    initViews();

  }

  public void initViews(){
    Toolbar toolbar= findViewById(R.id.toolbar_history);
    TextView  textView_title= findViewById(R.id.toolbar_title_history);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,textView_title,"History");
    listView = findViewById(R.id.history_recycler);
    timeTextView = findViewById(R.id.time_textview);
    tv_fuelconsumed= findViewById(R.id.fuelconsumed);
    tv_totalcost= findViewById(R.id.totalcost);
    tv_totaldistance= findViewById(R.id.totaldistance);
    tv_totaltime= findViewById(R.id.totaltime);
    linearLayout_notrip= findViewById(R.id.ll_notrip);
    list_helper=new ArrayList<>();
    ImageView imageView=findViewById(R.id.pdf_loader);
    pickdate= findViewById(R.id.pickdate);
    dateTextView= findViewById(R.id.date);
    yearTextview= findViewById(R.id.year);
    adopter=new HistoryAdapter(listView,list_helper,HistoryActivity.this);
    listView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
    listView.setAdapter(adopter);
    Calendar now = Calendar.getInstance();
    String ist_time=new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.SSSZZZ").format(now.getTime());
    int year=now.get(Calendar.YEAR);
    int month= now.get(Calendar.MONTH);
    int day= now.get(Calendar.DAY_OF_MONTH);
    //Log.e("+date", "onCreate: "+year+month+day );
    timeTextView.setText(""+day);
    dateTextView.setText(months[month]);
    yearTextview.setText(""+year);
    if(NetworkUtils.isConnected()) {
      loadTrip(ist_time);
    }else{
      ToastUtils.showShort("No internet connection");
    }
    pickDate();
    loadPdf();


  }
  private void loadPdf(){
    ImageView imageView=findViewById(R.id.pdf_loader);
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new ReportDialog(HistoryActivity.this,HistoryActivity.this,Calendar.getInstance()).show();


      }
    });

  }
  private void pickDate(){
    pickdate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            HistoryActivity.this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(Color.parseColor("#2da9e1"));
        dpd.setThemeDark(true);
        dpd.setTitle("Select Date");
        dpd.setMaxDate(now);
        dpd.setYearRange(2018,2019);
        dpd.show((HistoryActivity.this).getFragmentManager(), "DatePickerDialog");
      }
    });
  }
  private  void loadTrip(String time){
    String carId=CommonPreference.getInstance().getCarId();

    ApiRequests.getInstance().get_trip(GlobalReferences.getInstance().baseActivity,HistoryActivity.this,carId,time);

  }

  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {

  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
try{
  JSONObject data=listener.getJsonResponse();

  if (listener.getTag().equals("get_trip")){
    presenter.parseData(data);
  }else  {
      if (listener.getJsonResponse().getString("status").equals("SUCCESS")) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getString("msg"))));
      }else if (data.getString("status").equals("FAILED")){
        ToastUtils.showShort(data.getString("msg"));
      }

  }
}catch (Exception ex){

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
  public void setData(ArrayList<TripHistory> list) {
    if (list_helper !=null){
      list_helper.clear();
      list_helper.addAll(list);
      adopter.notifyDataSetChanged();
    }
  }

  @Override
  public void setTotalDistance(String distance) {
    tv_totaldistance.setText(distance);

  }

  @Override
  public void setFuelConsumed(String fuel) {
    tv_fuelconsumed.setText(fuel);

  }

  @Override
  public void setCost(String cost) {
    tv_totalcost.setText(cost);

  }

  @Override
  public void setTotalTime(String time) {
    tv_totaltime.setText(time);

  }
  @Override
  public void setNoTrip() {
    linearLayout_notrip.setVisibility(View.VISIBLE);
    listView.setVisibility(View.GONE);


  }

  @Override
  public void hideNoTrip() {
    linearLayout_notrip.setVisibility(View.GONE);
    listView.setVisibility(View.VISIBLE);

  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    String date = " " + dayOfMonth ;
    String monthAndYear=months[monthOfYear];
    timeTextView.setText(date);
    dateTextView.setText(monthAndYear);
    yearTextview.setText(""+year);
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, monthOfYear, dayOfMonth);
    String ist_time=new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.SSSZZZ").format(calendar.getTime());
    if(NetworkUtils.isConnected()) {
      loadTrip(ist_time);
    }else{
      ToastUtils.showShort("No internet connection");
    }
  }

  @Override
  public void onDateSelected(String startDate, String endDate, String type) {

    ApiRequests.getInstance().download_trip(GlobalReferences.getInstance().baseActivity,HistoryActivity.this,type,startDate,endDate);
  }
}


