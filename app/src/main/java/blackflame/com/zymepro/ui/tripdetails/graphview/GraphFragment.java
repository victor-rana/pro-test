package blackflame.com.zymepro.ui.tripdetails.graphview;

import static blackflame.com.zymepro.Prosingleton.TAG;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.common.CommonFragment;
import blackflame.com.zymepro.ui.tripdetails.TripDataListener;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphFragment extends CommonFragment implements OnChartValueSelectedListener, TripDataListener {

  private PieChart mChart;

  float[] distance_default = {0f, 0f, 0f, 0f};
  String[] time_chart_default = new String[]{" ", " ", " ", " "};
  float[] distance;
  String[] time_chart;
  String time;
  int tripId;
  TextView tv_normal, tv_medium, tv_high, tv_overspeed;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.layout_graph_fragment, container, false);
    Bundle bundle = getArguments();
    time = bundle.getString("time");
    mChart = view.findViewById(R.id.chart1);
    mChart.setUsePercentValues(true);
    mChart.getDescription().setEnabled(false);
    mChart.setExtraOffsets(5, 10, 5, 5);

    mChart.setDragDecelerationFrictionCoef(0.95f);


    mChart.setCenterText("Distance");
    mChart.setCenterTextColor(Color.WHITE);
    mChart.setExtraOffsets(5, 10, 5, 5);

    mChart.setDrawHoleEnabled(true);
    mChart.setHoleColor(Color.BLACK);
    mChart.setHoleRadius(40f);
    mChart.setTransparentCircleRadius(0f);
    mChart.setSaveEnabled(true);


    mChart.setDrawCenterText(true);

    mChart.setRotationAngle(0);
    // enable rotation of the chart by touch
    mChart.setRotationEnabled(true);
    mChart.setHighlightPerTapEnabled(true);
    tripId = bundle.getInt("trip_id");


    // add a selection listener
    mChart.setOnChartValueSelectedListener(this);

    setData(distance_default, time_chart_default);

    mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    mChart.getLegend().setEnabled(false);

    // entry label styling
    mChart.setEntryLabelColor(Color.BLACK);
    mChart.setEntryLabelTextSize(12f);
    if (distance != null && distance.length > 0) {
      //setData(4,4);
    }

    tv_normal = view.findViewById(R.id.tv_graph_low);
    tv_medium = view.findViewById(R.id.tv_graph_normal);
    tv_high = view.findViewById(R.id.tv_graph_high);
    tv_overspeed = view.findViewById(R.id.tv_graph_overspeed);
    return view;
  }

  @Override
  public void dataListener(JSONObject jsonObject) {
    try {
      JSONObject jsonObject_TripData = jsonObject.getJSONObject("msg");
      JSONObject tripObject = jsonObject_TripData.getJSONObject("trip_data");
      distance = new float[4];
      float high_speed_distance = (float) tripObject.getInt("high_speed_distance");
      float normal_speed_distance = (float) tripObject.getInt("normal_speed_distance");
      float low_speed_distance = (float) tripObject.getInt("low_speed_distance");
      float over_speed_time = (float) tripObject.getInt("overspeed_distance");

      int distance_total = tripObject.getInt("distance");

      distance[3] = getPercentage((float) (distance_total), over_speed_time);
      distance[2] = getPercentage((float) (distance_total), high_speed_distance);
      distance[1] = getPercentage((float) (distance_total), normal_speed_distance);
      distance[0] = getPercentage((float) (distance_total), low_speed_distance);
      time_chart = new String[4];
      time_chart[0] = tripObject.getInt("low_speed_time") + " sec";
      time_chart[1] = tripObject.getInt("normal_speed_time") + " sec";
      time_chart[2] = tripObject.getInt("high_speed_time") + " sec";
      time_chart[3] = tripObject.getInt("overspeed_time") + " sec";

      if (tv_normal != null) {
        tv_normal.setText(time_chart[0]);
        tv_medium.setText(time_chart[1]);
        tv_high.setText(time_chart[2]);
        tv_overspeed.setText(time_chart[3]);
      }


      setData(distance, time_chart);


      // setData(4,100);
    } catch (JSONException ex) {
      ex.printStackTrace();
    }
  }


  private float getPercentage(float total_distance, float currentdistance) {

    return ((float) ((currentdistance / total_distance) * 100.0));

  }

  @Override
  public void onValueSelected(Entry e, Highlight h) {

  }

  @Override
  public void onNothingSelected() {

  }


  private void setData(float[] distance, String[] time) {

    try {
      ArrayList<PieEntry> entries = new ArrayList<>();

      // NOTE: The order of the entries when being added to the entries array determines their position around the center of
      // the chart.

      for (int i = 0; i < distance.length; i++) {

        Log.e(TAG, "setData: " + distance[i]);

        // entries.add(new PieEntry(distance[i], time[i]));

        if (distance[i] > 0) {
          entries.add(new PieEntry(distance[i], time_chart_default[i]));
        }
      }

      PieDataSet dataSet = new PieDataSet(entries, "Trip Data");

      dataSet.setDrawIcons(false);

      dataSet.setSliceSpace(3f);
      dataSet.setIconsOffset(new MPPointF(0, 40));
      dataSet.setSelectionShift(5f);

      // add a lot of colors

      ArrayList<Integer> colors = new ArrayList<>();
      Resources resources = Prosingleton.getAppContext().getResources();
      colors.add(resources.getColor(R.color.chart_1));
      colors.add(resources.getColor(R.color.chart_2));
      colors.add(resources.getColor(R.color.chart_3));
      colors.add(resources.getColor(R.color.chart_4));
      dataSet.setColors(colors);
      PieData data = new PieData(dataSet);
      data.setValueFormatter(new PercentFormatter());
      data.setValueTextSize(11f);
      data.setValueTextColor(Color.BLACK);

      if (mChart != null) {
        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
      }
    }catch (Exception ex){

    }
  }
}
