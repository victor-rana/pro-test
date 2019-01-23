package blackflame.com.zymepro.ui.analytic;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.Settings.Global;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.Constants.RequestParam;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.io.http.ApiRequests;
import blackflame.com.zymepro.io.http.BaseTask;
import blackflame.com.zymepro.io.http.BaseTaskJson;
import blackflame.com.zymepro.io.listener.AppRequest;
import blackflame.com.zymepro.ui.analytic.chart.DateAxisFormatter;
import blackflame.com.zymepro.ui.analytic.chart.LineValueFromatter;
import blackflame.com.zymepro.ui.analytic.chart.MAxisValueFormatter;
import blackflame.com.zymepro.ui.analytic.chart.MyMarkerView;
import blackflame.com.zymepro.ui.analytic.chart.RadarMarkerView;
import blackflame.com.zymepro.ui.analytic.listener.AnalyticResponseListener;
import blackflame.com.zymepro.util.UtilityMethod;
import blackflame.com.zymepro.view.custom.SwitchMultiButton;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONObject;

public class AnalyticActivity extends BaseActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener,AppRequest,AnalyticResponseListener {
  protected BarChart mchart_speed,mChart_alert_day,mchart_time;
  int [] speed=new int[7];
  int[] rpm=new int[7];
  int[] max_speed=new int[7];
  int [] max_rpm=new int[7];
  float[] active_time=new float[7];
  float[] active_time_total=new float[7];
  float[] idle_time=new float[7];
  float[] alert_day=new float[7];
  float[] pie_time=new float[4];
  float [] pie_distance=new float[4];
  protected Typeface mTfLight;
  SwitchMultiButton switchMultiButton,multiButton_alerts,multiButton_max_speed,multiButton_speed_rpm,multiButton_speed_time;
  private RadarChart chart_alert;
  private LineChart linechart_speed_rpm;
  PieChart pieChart;
  String[] time_chart_default = new String[]{" ", " ", " ", " "};

  SimpleDateFormat dateFormat_ui,dateFormat_server;

  TextView start_date,end_date,start_month,end_month,start_day,end_day,
      tv_total_trip_count,tv_total_alert_count,tv_total_active_time,tv_total_idle_time,tv_average_speed,tv_total_distance;
  String carId;

  String[] date=new String[7];
  float[] distance=new float[7];
  int[] time=new int[7];
  float[] trip_count=new float[7];


  int[] driver_behaviour=new int[5];
  int[] car_safety=new int[5];
  int[] engine_alert=new int[3];
  String[] label_driver=new String[]{"Over Speed","Sudden Acceleration","Sudden Braking","Sudden Lane Change","Sharp Turn"};
  String []  label_car_safety=new String[]{"Collision","Theft","Geofence","Unplug","Towing"};
  String [] label_engine=new String[]{"Low Battery","High Coolant","Long Idling"};
  MyMarkerView mv;
  IAxisValueFormatter valueFormatter;

  int driver_max,engine_max,safety_max;
  FrameLayout btn_pick_Date;
  FrameLayout progressBarHolder;
  AlphaAnimation inAnimation;
  AlphaAnimation outAnimation;
  TextView tv_unit_max_rpm,tv_unit_distance,tv_unit_alert_count,tv_unit_pie;


  String totalDistance,totalTime;

  AnalyticPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_analytic);
    GlobalReferences.getInstance().baseActivity=this;
    presenter=new AnalyticPresenter(this);
    initViews();
  }
  private void initViews(){


    Toolbar toolbar= findViewById(R.id.toolbar_common);
    TextView textView_title= findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,textView_title,"Analytics");
    carId=getIntent().getStringExtra("carId");
    mchart_speed=findViewById(R.id.chart_speed);
    mChart_alert_day=findViewById(R.id.chart_alert_day);
    mchart_time=findViewById(R.id.chart_idle_time);
    linechart_speed_rpm=findViewById(R.id.line_chart);
    start_date=findViewById(R.id.tv_start_date);
    end_date=findViewById(R.id.tv_end_date);
    start_month=findViewById(R.id.tv_month_year);
    end_month=findViewById(R.id.tv_end_month_year);
    start_day=findViewById(R.id.tv_start_day);
    end_day=findViewById(R.id.tv_end_day);
    multiButton_alerts=findViewById(R.id.multibutton_alerts);
    tv_total_active_time=findViewById(R.id.tv_total_active_time);
    tv_total_alert_count=findViewById(R.id.tv_total_alerts_count);
    tv_total_distance=findViewById(R.id.tv_total_distance);
    tv_total_idle_time=findViewById(R.id.tv_total_idle_time);
    tv_average_speed=findViewById(R.id.tv_average_speed);
    tv_total_trip_count=findViewById(R.id.tv_trip_count);
    btn_pick_Date=findViewById(R.id.frame_pick_date);
    tv_unit_max_rpm=findViewById(R.id.chart_unit_maxrpm);
    tv_unit_distance=findViewById(R.id.chart_unit_distance_time);
    tv_unit_alert_count=findViewById(R.id.chart_unit_alert_count);
    progressBarHolder = findViewById(R.id.progressBarHolder);
    tv_unit_pie=findViewById(R.id.chart_unit_pie_time);
    switchMultiButton = findViewById(R.id.switchmultibutton1);
    multiButton_alerts = findViewById(R.id.multibutton_alerts);
    multiButton_max_speed=findViewById(R.id.multi_maxspeed_maxrpm);
    multiButton_speed_time=findViewById(R.id.multi_speed_time);
    multiButton_speed_rpm=findViewById(R.id.multi_speed_rpm);
    pieChart=findViewById(R.id.pie_chart);


    multiButton_max_speed.setText("Max. Speed ", "Max. RPM").setOnSwitchListener(onMaxSpeedButton);
    //multiButton_idle_time.setText("Active Time","Idle Time").setOnSwitchListener(onSwitchListener);
    multiButton_speed_time.setText("Time","Distance").setOnSwitchListener(onPieListener);
    multiButton_speed_rpm.setText("Trip Count","Alert Count").setOnSwitchListener(onAlertDay);

    switchMultiButton.setText("Distance", "Time").setOnSwitchListener(onSwitchListener);

    multiButton_alerts.setText("Driver Alerts", "Engine Alerts","Safety Alerts").setOnSwitchListener(onAlertSwitchListener);
    mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    dateFormat_server=new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.SSSZZZ");
    dateFormat_ui=new SimpleDateFormat("yyyy-MM-dd");
    inAnimation = new AlphaAnimation(0f, 1f);
    outAnimation = new AlphaAnimation(1f, 0f);
    Calendar calendar=Calendar.getInstance();
    Date date_start=calendar.getTime();
    calendar.add(Calendar.DATE,-6);
    Date date_end=calendar.getTime();
    setFormattedDate(date_end,date_start);
    mv = new MyMarkerView(this, R.layout.layout_analytic_custom_marker);
    mv.setChartView(mchart_speed); // For bounds control
    mchart_speed.setMarker(mv);

    XAxis xAxis = mchart_speed.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setTypeface(mTfLight);
    xAxis.setDrawGridLines(false);
    xAxis.setGranularity(1f); // only intervals of 1 day
    xAxis.setLabelCount(7);


    //code to handle radar chart
    chart_alert = findViewById(R.id.chart_alerts);
    chart_alert.setBackgroundColor(Color.rgb(22, 24, 35));
    chart_alert.getDescription().setEnabled(false);
    chart_alert.setWebLineWidth(1f);
    chart_alert.setWebColor(Color.LTGRAY);
    chart_alert.setWebLineWidthInner(1f);
    chart_alert.setWebColorInner(Color.LTGRAY);
    chart_alert.setWebAlpha(100);

    MarkerView rv = new RadarMarkerView(this, R.layout.layout_analytic_radar_view);
    rv.setChartView(chart_alert); // For bounds control
    chart_alert.setMarker(rv);
    setBarChartSetting(mchart_time);

    //=======================pie chart=============
    pieChart.setUsePercentValues(true);
    pieChart.getDescription().setEnabled(false);
    pieChart.setExtraOffsets(5, 10, 5, 5);

    pieChart.setDragDecelerationFrictionCoef(0.95f);


    pieChart.setCenterText("Distance");
    pieChart.setCenterTextColor(Color.WHITE);
    pieChart.setExtraOffsets(5, 10, 5, 5);

    pieChart.setDrawHoleEnabled(true);
    pieChart.setHoleColor(getResources().getColor(R.color.chart_background));
    pieChart.setHoleRadius(40f);
    pieChart.setTransparentCircleRadius(0f);
    pieChart.setSaveEnabled(true);

    pieChart.setDrawCenterText(true);

    pieChart.setRotationAngle(0);
    // enable rotation of the chart by touch
    pieChart.setRotationEnabled(true);
    pieChart.setHighlightPerTapEnabled(true);
    pieChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
    pieChart.getLegend().setEnabled(false);
    // entry label styling
    pieChart.setEntryLabelColor(Color.BLACK);
    // mChart.setEntryLabelTypeface(mTfRegular);
    pieChart.setEntryLabelTextSize(12f);

    setDateListener();

  }

  private SwitchMultiButton.OnSwitchListener onSwitchListener = new SwitchMultiButton.OnSwitchListener() {
    @Override
    public void onSwitch(int position, String tabText,boolean isUser) {
      //	Log.e("tag", "onSwitch: position"+position );

      switch (position){
        case 0:

          setData(mchart_speed,distance);
          tv_unit_distance.setText("(km)");

          break;
        case 1:
          setData(mchart_speed,active_time_total);

          tv_unit_distance.setText("(min)");

          break;


      }

    }
  };
  private SwitchMultiButton.OnSwitchListener onPieListener = new SwitchMultiButton.OnSwitchListener() {
    @Override
    public void onSwitch(int position, String tabText,boolean isUser) {


      switch (position){
        case 0:
          tv_unit_pie.setText("Total drive time : "+totalTime);
          setPieData(pieChart,pie_time,time_chart_default,"Time");
          //setData(mchart_speed,distance);
          break;
        case 1:
          tv_unit_pie.setText("Total distance : "+totalDistance+" km");
          setPieData(pieChart,pie_distance,time_chart_default,"Distance");
          //setData(mchart_speed,active_time);

          break;


      }

    }
  };


  private SwitchMultiButton.OnSwitchListener onAlertDay = new SwitchMultiButton.OnSwitchListener() {
    @Override
    public void onSwitch(int position, String tabText,boolean isUser) {

      switch (position){
        case 0:
          tv_unit_alert_count.setText("(count)");
          setData(mChart_alert_day,trip_count);
          break;
        case 1:
          tv_unit_alert_count.setText("(count)");
          setData(mChart_alert_day,alert_day);
          break;

      }

    }
  };



  private SwitchMultiButton.OnSwitchListener onMaxSpeedButton = new SwitchMultiButton.OnSwitchListener() {
    @Override
    public void onSwitch(int position, String tabText,boolean isUser) {

      switch (position){
        case 0:
          linechart_speed_rpm.clear();
          lineChartSetting(200,valueFormatter);
          setLineData(linechart_speed_rpm,7,speed);

          tv_unit_max_rpm.setText("(km/h)");
          break;
        case 1:
          //line_maximum=8000f;
          linechart_speed_rpm.clear();
          lineChartSetting(8000,valueFormatter);
          setLineData(linechart_speed_rpm,7,max_rpm);
          tv_unit_max_rpm.setText("(RPM)");


          break;

      }

    }
  };

  private SwitchMultiButton.OnSwitchListener onAlertSwitchListener = new SwitchMultiButton.OnSwitchListener() {
    @Override
    public void onSwitch(int position, String tabText,boolean isUser) {

      switch (position){
        case 0:
          chart_alert.clear();
          if (driver_max==0){
            chart_alert.clear();
            chart_alert.invalidate();
            return;
          }
          setData(driver_behaviour,label_driver,driver_max);
          break;
        case 1:
          chart_alert.clear();
          if (engine_max==0){
            chart_alert.clear();
            chart_alert.invalidate();
            return;
          }
          setData(engine_alert,label_engine,engine_max);

          break;
        case 2:
          chart_alert.clear();

          if (safety_max==0){
            chart_alert.clear();
            chart_alert.invalidate();
            return;
          }
          chart_alert.clear();

          setData(car_safety,label_car_safety,safety_max);
          break;


      }

    }
  };

  private void lineChartSetting(int max,IAxisValueFormatter xAxisFormatter){

    linechart_speed_rpm.setDrawGridBackground(false);

    // no description text
    linechart_speed_rpm.getDescription().setEnabled(false);

    // enable touch gestures
    linechart_speed_rpm.setTouchEnabled(true);

    // enable scaling and dragging
    linechart_speed_rpm.setDragEnabled(false);
    linechart_speed_rpm.setScaleEnabled(false);
    // mChart.setScaleXEnabled(true);
    // mChart.setScaleYEnabled(true);

    // if disabled, scaling can be done on x- and y-axis separately
    linechart_speed_rpm.setPinchZoom(false);

    // set an alternative background color
    // mChart.setBackgroundColor(Color.GRAY);

    // create a custom MarkerView (extend MarkerView) and specify the layout
    // to use for it
    mv.setChartView(linechart_speed_rpm); // For bounds control
    linechart_speed_rpm.setMarker(mv); // Set the marker to the chart

    // x-axis limit line
    LimitLine llXAxis = new LimitLine(10f, "Index 10");
    llXAxis.setLineWidth(4f);
    llXAxis.enableDashedLine(10f, 10f, 0f);
    llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
    llXAxis.setTextSize(10f);

    XAxis lineXaxis = linechart_speed_rpm.getXAxis();
    lineXaxis.enableGridDashedLine(10f, 10f, 0f);
    lineXaxis.setValueFormatter(xAxisFormatter);
    lineXaxis.addLimitLine(llXAxis); // add x-axis limit line

    lineXaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    lineXaxis.setTypeface(mTfLight);
    lineXaxis.setDrawGridLines(false);
    lineXaxis.setAxisLineColor(Color.WHITE);
    lineXaxis.setTextColor(Color.WHITE);

    Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
    YAxis leftAxis = linechart_speed_rpm.getAxisLeft();
    leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
    //leftAxis.setAxisMaximum(max);
    leftAxis.setAxisMinimum(0f);
    leftAxis.enableGridDashedLine(10f, 20f, 0f);
    leftAxis.setDrawZeroLine(false);
    leftAxis.setTextColor(Color.WHITE);
    leftAxis.setAxisLineColor(Color.WHITE);

    // limit lines are drawn behind data (and not on top)
    leftAxis.setDrawLimitLinesBehindData(true);

    linechart_speed_rpm.getAxisRight().setEnabled(false);

    /////=================Line chart setting=================
    linechart_speed_rpm.getDescription().setEnabled(false);
    linechart_speed_rpm.setPinchZoom(false);
    linechart_speed_rpm.setDrawGridBackground(false);
    linechart_speed_rpm.getAxisLeft().setDrawGridLines(true);
    linechart_speed_rpm.getXAxis().setDrawGridLines(true);
    linechart_speed_rpm.getAxisRight().setDrawGridLines(true);
    linechart_speed_rpm.getXAxis().setEnabled(true);
    linechart_speed_rpm.getAxisLeft().setEnabled(true);
    linechart_speed_rpm.getAxisRight().setEnabled(true);
    linechart_speed_rpm.getLegend().setEnabled(false);
    linechart_speed_rpm.getAxisRight().setEnabled(false);

  }


  private void setLineData(LineChart mChart,int count, int[] data) {

    ArrayList<Entry> values = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      //Log.e(TAG, "setLineData: "+data[i] );
      values.add(new Entry(i, data[i]));

    }

    LineDataSet set1;

    // create a dataset and give it a type
    set1 = new LineDataSet(values, "DataSet 1");

    set1.setDrawIcons(false);
    // set the line to be drawn like this "- - - - - -"
    set1.enableDashedLine(10f, 5f, 0f);
    set1.enableDashedHighlightLine(10f, 5f, 0f);
    set1.setColor(Color.WHITE);
    set1.setCircleColor(Color.WHITE);
    set1.setLineWidth(1f);
    set1.setCircleRadius(3f);
    set1.setDrawCircleHole(false);
    set1.setDrawValues(false);
    set1.setValueTextSize(9f);
    set1.setDrawFilled(true);
    set1.setFormLineWidth(1f);
    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
    set1.setFormSize(15.f);
    set1.setValueTextColor(Color.WHITE);
    set1.setValueFormatter(new LineValueFromatter());
    if (Utils.getSDKInt() >= 18) {
      // fill drawable only supported on api level 18 and above
      Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
      set1.setFillDrawable(drawable);
    }
    else {

      set1.setFillColor(getResources().getColor(R.color.colorAccent));
    }

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    dataSets.add(set1); // add the datasets


    // create a data object with the datasets
    LineData linedata = new LineData(dataSets);
    //linedata.setValueFormatter(new LargeValueFormatter());



    // set data
    mChart.setData(linedata);
    mChart.setDrawMarkers(false);

    mChart.invalidate();
    mChart.notifyDataSetChanged();
    mChart.refreshDrawableState();


    //}
  }

  public void setData(int[] data, final String[] cat, float max) {

    ArrayList<RadarEntry> entries1 = new ArrayList<>();

    for (int i=0;i<data.length;i++){
      entries1.add(new RadarEntry(data[i]));
    }
    XAxis xAxis_radar = chart_alert.getXAxis();
    xAxis_radar.setTypeface(mTfLight);
    xAxis_radar.setTextSize(5f);
    xAxis_radar.setYOffset(0f);
    xAxis_radar.setXOffset(0f);
    xAxis_radar.setValueFormatter(new IAxisValueFormatter() {

      @Override
      public String getFormattedValue(float value, AxisBase axis) {

        //Log.e(TAG, "getFormattedValue: "+cat[(int) value % cat.length] );
        return cat[(int) value % cat.length];
      }
    });



    //	xAxis_radar.setValueFormatter(new AlertAxisFormatter(cat));

    xAxis_radar.setTextColor(Color.WHITE);

    YAxis yAxis = chart_alert.getYAxis();
    yAxis.setTypeface(mTfLight);
    yAxis.setLabelCount(cat.length, true);
    yAxis.setTextSize(5f);
    yAxis.setAxisMinimum(0f);
    yAxis.setAxisMaximum(max);
    yAxis.setDrawLabels(false);


    RadarDataSet set1 = new RadarDataSet(entries1, "Last Week");
    set1.setColor(Color.rgb(103, 110, 129));
    set1.setFillColor(Color.rgb(159, 255, 203));
    set1.setDrawFilled(true);
    set1.setFillAlpha(120);
    set1.setLineWidth(2f);
    set1.setDrawHighlightCircleEnabled(true);
    set1.setDrawHighlightIndicators(false);

    ArrayList<IRadarDataSet> sets = new ArrayList<>();
    sets.add(set1);

    RadarData rdata = new RadarData(sets);
    rdata.setValueTypeface(mTfLight);
    rdata.setValueTextSize(5f);
    rdata.setDrawValues(false);
    rdata.setValueTextColor(Color.WHITE);
    rdata.notifyDataChanged();

    chart_alert.setData(rdata);
    chart_alert.getLegend().setEnabled(false);
    chart_alert.animateX(1000);
    chart_alert.notifyDataSetChanged();
    chart_alert.invalidate();
  }

  private void setPieData(PieChart chart,float[] distance, String[] time,String centerText) {

    try {
      ArrayList<PieEntry> entries = new ArrayList<>();



      // NOTE: The order of the entries when being added to the entries array determines their position around the center of
      // the chart.

      for (int i = 0; i < distance.length; i++) {

        //	Log.e(TAG, "setData: " + distance[i]);

        // entries.add(new PieEntry(distance[i], time[i]));

        if (distance[i] > 0) {
          entries.add(new PieEntry(distance[i], time_chart_default[i]));
        }
      }
      chart.setCenterText(centerText);


      PieDataSet dataSet = new PieDataSet(entries, "Trip Data");

      dataSet.setDrawIcons(false);

      dataSet.setSliceSpace(3f);
      dataSet.setIconsOffset(new MPPointF(0, 40));
      dataSet.setSelectionShift(5f);

      // add a lot of colors

      ArrayList<Integer> colors = new ArrayList<>();
      Resources resources = Prosingleton.getAppContext().getResources();
      colors.add(resources.getColor(R.color.bar_color));
      colors.add(resources.getColor(R.color.chart_2));
      colors.add(resources.getColor(R.color.chart_3));
      colors.add(resources.getColor(R.color.chart_4));
      dataSet.setColors(colors);
      PieData data = new PieData(dataSet);
      data.setValueFormatter(new PercentFormatter());
      data.setValueTextSize(11f);
      data.setValueTextColor(Color.BLACK);



      if (chart != null) {

        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();

        chart.animateY(1000);
      }
    }catch (Exception ex){

    }


    //dataSet.setSelectionShift(0f);


  }

  private  void setBarChartSetting(BarChart chart){
    chart.setDrawBarShadow(false);
    chart.setDrawValueAboveBar(true);
    chart.getDescription().setEnabled(false);
    chart.setPinchZoom(false);
    chart.setDrawGridBackground(false);
    chart.getAxisLeft().setDrawGridLines(true);
    chart.getXAxis().setDrawGridLines(true);
    chart.getAxisRight().setDrawGridLines(true);
    chart.getXAxis().setEnabled(true);
    chart.getAxisLeft().setEnabled(true);
    chart.getAxisRight().setEnabled(true);
    chart.getLegend().setEnabled(false);
    chart.getAxisLeft().setGranularity(1f);
    chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
    chart.getAxisLeft().setSpaceTop(15f);
    chart.getAxisLeft().setAxisMinimum(0f);
    chart.getAxisRight().setEnabled(false);
    XAxis xAxis = chart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setTypeface(mTfLight);
    xAxis.setDrawGridLines(false);
    xAxis.setGranularity(0f); // only intervals of 1 day
    xAxis.setLabelCount(7);
    xAxis.setValueFormatter(valueFormatter);
    chart.setMarker(mv);


  }

  private void setData(BarChart chart, float[] data) {
    float start = 1f;

    ArrayList<BarEntry> bargroup1 = new ArrayList<>();
    chart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
    chart.getXAxis().setTextColor(Color.WHITE);


    for (int i=0;i<7;i++){
      bargroup1.add(new BarEntry(i, data[i]));
    }



    BarDataSet set1;
    set1 = new BarDataSet(bargroup1, "The year 2017");

    set1.setDrawIcons(false);
    set1.setDrawValues(false);
    int startColor1 = ContextCompat.getColor(this, R.color.bar_color);
    int startColor2 = ContextCompat.getColor(this, R.color.bar_color);

    set1.setColor(startColor1);
    //set1.setBarShadowColor(startColor2);
    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
    dataSets.add(set1);

    BarData barData = new BarData(dataSets);
    barData.setValueTextSize(10f);
    barData.setValueTypeface(mTfLight);
    chart.setPinchZoom(false);
    chart.setDoubleTapToZoomEnabled(false);

    //barData.setBarWidth(0.9f);
    barData.setBarWidth(0.5f);

    chart.setData(barData);
    chart.invalidate();
    chart.notifyDataSetChanged();
    chart.animateXY(1000,1000);

  }
  private void setTimeData(BarChart chart, float[] active_time,float[] idle_time) {
    float start = 1f;

    ArrayList<BarEntry> bargroup1 = new ArrayList<>();
    chart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
    chart.getXAxis().setTextColor(Color.WHITE);
    chart.setDrawMarkers(false);
    chart.setClickable(false);
    chart.setTouchEnabled(false);

    for(int i=0;i<active_time.length;i++){
      bargroup1.add(new BarEntry(
          i,
          new float[]{active_time[i], idle_time[i]}));
    }

    BarDataSet set1;
    set1 = new BarDataSet(bargroup1, "");
    set1.setDrawIcons(false);
    //set1.setDrawValues(false);

    int startColor1 = ContextCompat.getColor(this, R.color.start_color_first);
    int startColor2 = ContextCompat.getColor(this, R.color.bar_color);
    set1.setColors(startColor1,startColor2);
    set1.setStackLabels(new String[]{"Active Time", "Idle Time"});
    set1.setDrawValues(true);


    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
    dataSets.add(set1);

    BarData barData = new BarData(dataSets);
    barData.setValueTextSize(10f);
    barData.setValueTypeface(mTfLight);
    chart.setPinchZoom(false);
    chart.setDoubleTapToZoomEnabled(false);
    //barData.setBarWidth(0.9f);
    barData.setBarWidth(0.5f);
    barData.setValueTextColor(Color.BLACK);

    chart.setFitBars(true);
    chart.setDrawValueAboveBar(false);

    chart.setData(barData);
    chart.getXAxis().setDrawLabels(true);

    Legend l = chart.getLegend();
    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
    l.setDrawInside(false);
    l.setFormSize(8f);
    l.setFormToTextSpace(4f);
    l.setXEntrySpace(6f);
    l.setTextColor(Color.WHITE);


    l.setEnabled(true);



    IAxisValueFormatter custom = new MAxisValueFormatter();

    YAxis leftAxis = chart.getAxisLeft();
    leftAxis.setTypeface(mTfLight);
    leftAxis.setLabelCount(5, false);
    leftAxis.setValueFormatter(custom);
    leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
    leftAxis.setSpaceTop(0f);
    leftAxis.setAxisMinimum(0f);


    chart.invalidate();
    chart.notifyDataSetChanged();
    chart.animateXY(1000,1000);

  }

  private void setDateListener(){

    btn_pick_Date.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            AnalyticActivity.this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        );
        now.add(Calendar.DATE,-6);
        dpd.setAccentColor(Color.parseColor("#2da9e1"));
        dpd.setThemeDark(true);
        dpd.setTitle("Select start date");
        dpd.setMaxDate(now);
        dpd.setYearRange(2017,2019);
        dpd.show((AnalyticActivity.this).getFragmentManager(), "DatePickerDialog");
      }
    });
  }
  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, monthOfYear, dayOfMonth);

    Date date_start=calendar.getTime();
    calendar.add(Calendar.DATE,+6);
    Date date_end=calendar.getTime();
    setFormattedDate(date_start,date_end);
  }
  private void setFormattedDate(Date start,Date end){
    String ist_time=dateFormat_server.format(start.getTime());
    start_date.setText(UtilityMethod.getDateOnly(dateFormat_server.format(start)));
    start_month.setText(UtilityMethod.getDateMonth(dateFormat_server.format(start)));
    start_day.setText(UtilityMethod.getDay(dateFormat_server.format(start)));
    end_date.setText(UtilityMethod.getDateOnly(dateFormat_server.format(end)));
    end_month.setText(UtilityMethod.getDateMonth(dateFormat_server.format(end)));
    end_day.setText(UtilityMethod.getDay(dateFormat_server.format(end)));
    loadData(ist_time,dateFormat_server.format(end));
  }
  private void loadData(String start_date,String end_date ){
    ApiRequests.getInstance().load_analytics(GlobalReferences.getInstance().baseActivity,AnalyticActivity.this,start_date,end_date,carId);
  }


  @Override
  public <T> void onRequestStarted(BaseTask<T> listener, RequestParam requestParam) {
    inAnimation.setDuration(2000);

    progressBarHolder.setAnimation(inAnimation);
    progressBarHolder.setVisibility(View.VISIBLE);
  }

  @Override
  public <T> void onRequestCompleted(BaseTask<T> listener, RequestParam requestParam) {
    outAnimation.setDuration(200);
    progressBarHolder.setAnimation(outAnimation);
    progressBarHolder.setVisibility(View.GONE);

    presenter.parseData(listener.getJsonResponse());

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
  public void onDateData(String[] data) {
    outAnimation.setDuration(200);
    progressBarHolder.setAnimation(outAnimation);
    progressBarHolder.setVisibility(View.GONE);
    date=data;
    valueFormatter=new DateAxisFormatter(mchart_speed,date);
    setBarChartSetting(mChart_alert_day);
  }

  @Override
  public void onTimeData(int[] data) {
    time=data;
  }

  @Override
  public void onDistanceData(float[] data) {
    distance=data;

    switchMultiButton.setSelectedTab(0);
    tv_unit_distance.setText("(km)");

    setBarChartSetting(mchart_speed);
    setData(mchart_speed,distance);
  }

  @Override
  public void onIdleTimeData(int[] data) {

  }

  @Override
  public void onMaxSpeedData(int[] data) {
    speed=data;

    linechart_speed_rpm.clear();

    multiButton_max_speed.setSelectedTab(0);
    tv_unit_max_rpm.setText("(km/h)");

    lineChartSetting(200,valueFormatter);

    setLineData(linechart_speed_rpm,7,speed);


  }

  @Override
  public void onMaxRpmData(int[] data) {
    max_rpm=data;
  }

  @Override
  public void onAlertData(int[] count) {
    driver_max=0;
    engine_max=0;
    safety_max=0;
    if (count[0]>driver_max){
      driver_max=count[0];
    }
    if (count[1]>driver_max){
      driver_max=count[1];
    }
    if (count[2]>driver_max){
      driver_max=count[2];
    }
    if (count[3]>driver_max){
      driver_max=count[3];
    }
    if (count[4]>driver_max){
      driver_max=count[4];
    }

    driver_behaviour[0]=count[0];
    driver_behaviour[1]=count[1];
    driver_behaviour[2]=count[2];
    driver_behaviour[3]=count[3];
    driver_behaviour[4]=count[4];
    multiButton_alerts.setSelectedTab(0);

    if (driver_max==0){
      chart_alert.clear();
      chart_alert.invalidate();

    }else {
      setData(driver_behaviour, label_driver, driver_max);
    }


    if (count[6]>safety_max){
      safety_max=count[6];
    }
    if (count[7]>safety_max){
      safety_max=count[7];
    }
    if (count[8]>safety_max){
      safety_max=count[8];
    }
    if (count[9]>safety_max){
      safety_max=count[9];
    }
    if (count[10]>safety_max){
      safety_max=count[10];
    }

    //Log.e(TAG, "onAlertData: " +count[6]+count[7]+count[8]+count[9]+count[10]);
    car_safety[0]=count[6];
    car_safety[1]=count[7];
    car_safety[2]=count[8];
    car_safety[3]=count[9];
    car_safety[4]=count[10];

    if (count[11]>engine_max){
      engine_max=count[11];
    }
    if (count[12]>engine_max){
      engine_max=count[12];
    }
    if (count[13]>engine_max){
      engine_max=count[13];
    }
    engine_alert[0]=count[11];
    engine_alert[1]=count[13];
    engine_alert[2]=count[13];
  }

  @Override
  public void onActiveTime(float[] active, float[] idle) {
    active_time_total=active;
    idle_time=idle;
    for(int i=0;i<active.length;i++){
      float totaltime=active[i];
      if (totaltime>0) {
        active_time[i] = ((totaltime - idle[i] )/ totaltime) * 100;
        //	Log.e(TAG, "onActiveTime:active " + active_time[i]);

        if (idle[i] > 0) {
          idle_time[i] = (idle[i] / totaltime) * 100;
          //Log.e(TAG, "onActiveTime: IDle "+idle_time[i] );

        } else {
          idle_time[i] = 0;
        }
      }else{
        active_time[i]=0;
        idle_time[i]=0;
      }



    }

    setBarChartSetting(mchart_time);



    setTimeData(mchart_time,active_time,idle_time);
  }

  @Override
  public void onAlertCount(float[] data) {
    alert_day=data;
  }

  @Override
  public void onPieData(float[] time, float[] distance) {
    pie_time=time;
    pie_distance=distance;
    multiButton_speed_time.setSelectedTab(0);
    tv_unit_pie.setText("Total drive time: "+totalTime);



    setPieData(pieChart,time,time_chart_default,"Time");


  }

  @Override
  public void onTripCount(float[] trip) {
    trip_count=trip;


    multiButton_speed_rpm.setSelectedTab(0);

    setData(mChart_alert_day,trip_count);
  }

  @Override
  public void onSummaryData(int totalTrip, String totalDistance, int totalAlerts, int averageSpeed,
      String driveTime, String idleTime) {
    this.totalDistance=totalDistance;
    this.totalTime=driveTime;

    tv_total_trip_count.setText(String.valueOf(totalTrip));
    tv_total_distance.setText(totalDistance+" km");

    tv_total_alert_count.setText(String.valueOf(totalAlerts));
    tv_average_speed.setText(averageSpeed+" km/h");
    tv_total_idle_time.setText(idleTime);
    tv_total_active_time.setText(driveTime);

    tv_unit_pie.setText("Total drive time : "+totalTime);

  }

  @Override
  public void onError(VolleyError error) {

  }
}
