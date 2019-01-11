package blackflame.com.zymepro.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.ui.history.adapter.HistoryAdapter;
import blackflame.com.zymepro.ui.history.model.TripHistory;
import java.util.ArrayList;

public class HistoryActivity extends BaseActivity {
  RecyclerView listView;
  Double latitude,longitude;
  String result;
  ArrayList<TripHistory> list_helper;
  HistoryAdapter adopter;
  LinearLayout linearLayout_notrip;
  private TextView timeTextView;
  LinearLayout pickdate;
  TextView dateTextView,yearTextview;
  int totaltime,fuelcost;
  float total_distance;
  TextView tv_totaldistance,tv_totaltime,tv_totalcost,tv_fuelconsumed;
  String time_format;
  int tempFuelConsumed;
  Context context;
  String imei,carId;
  String[] months={"Jan","Feb","Mar","APR","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);
  }
}
