package blackflame.com.zymepro.ui.history.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.history.DateSelectedListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportDialog extends Dialog implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
  public ReportDialog(@NonNull Context context) {
    super(context);
  }
  private int mYear, mMonth, mDay, mHour, mMinute;
  public Activity c;
  public Dialog d;
  public TextView pdf,csv;
  View view_clicked;
  long starttime = 0;
  DateSelectedListener listener;
  Calendar cal;
  TextView start_date;
  TextView end_date;
  SimpleDateFormat format,date_format;
  RadioButton rb_today,rb_yesterday,rb_last_week,rb_last_month,rb_custom_date;
  RadioGroup radioGroup;
  String date_selected_start,date_selected_end;
  LinearLayout container_custom_range;
  public ReportDialog(Activity a, DateSelectedListener listener,Calendar calendar) {

    super(a);
    this.c = a;
    this.listener=listener;
    this.cal=calendar;
    mYear=cal.get(Calendar.YEAR);
    mDay=cal.get(Calendar.DAY_OF_MONTH);
    mMonth=cal.get(Calendar.MONTH);
    format=new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.SSSZZZ");
    date_format=new SimpleDateFormat("yyyy-MMM-dd");

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_report_dialog);
    radioGroup=findViewById(R.id.rg_time_period);
    radioGroup.setOnCheckedChangeListener(this);
    container_custom_range=findViewById(R.id.container_custom_range);


    rb_today=findViewById(R.id.rb_today);
    rb_yesterday=findViewById(R.id.rb_yesterday);
    rb_last_week=findViewById(R.id.rb_last_week);
    rb_last_month=findViewById(R.id.rb_last_month);
    rb_custom_date=findViewById(R.id.rb_custom_range);




    //calendar=Calendar.getInstance();
    start_date=findViewById(R.id.btn_report_start);
    end_date=findViewById(R.id.btn_report_end);

    start_date.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //startDialog.show();
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            startdateListener,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(Color.parseColor("#2da9e1"));
        dpd.setThemeDark(true);
        dpd.setTitle("Select Date");
        dpd.setMaxDate(now);
        dpd.setYearRange(2017,2018);
        dpd.show((c).getFragmentManager(), "DatePickerDialog");



      }
    });
    end_date.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            enddateListener,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(Color.parseColor("#2da9e1"));
        dpd.setThemeDark(true);
        dpd.setTitle("Select Date");
        dpd.setMaxDate(now);
        dpd.setYearRange(2017,2018);
        dpd.show((c).getFragmentManager(), "DatePickerDialog");


      }
    });





    ViewGroup.LayoutParams params = getWindow().getAttributes();
    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    pdf= findViewById(R.id.btn_pdf);
    csv=findViewById(R.id.btn_csv);
    csv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (date_selected_start ==null || date_selected_end==null){
          if (start_date.getText().toString().equals("Start Date") || end_date.getText().toString().equals("End date")){
            Toast.makeText(c, "Please select a date.", Toast.LENGTH_SHORT).show();
          }
        }else{
          listener.onDateSelected(date_selected_start,date_selected_end,"csv");
          dismiss();
        }



        //listener.onDateSelected(start_date.getText().toString(),end_date.getText().toString());


      }
    });
    pdf.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        if (date_selected_start ==null || date_selected_end==null){
          if (start_date.getText().toString().equals("Start Date") || end_date.getText().toString().equals("End date")){
            Toast.makeText(c, "Please select a date.", Toast.LENGTH_SHORT).show();
          }
        }else{
          listener.onDateSelected(date_selected_start,date_selected_end,"pdf");
          dismiss();

        }



      }
    });


  }



  com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener startdateListener =new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
      Calendar newDate = Calendar.getInstance();
      newDate.set(year, monthOfYear, dayOfMonth);
      date_selected_start=format.format(newDate.getTime());

      start_date.setText(date_format.format(newDate.getTime()));
    }
  };

  com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener enddateListener =new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
      Calendar newDate = Calendar.getInstance();
      newDate.set(year, monthOfYear, dayOfMonth);
      date_selected_end=format.format(newDate.getTime());
      end_date.setText(date_format.format(newDate.getTime()));

    }
  };










  @Override
  public void onClick(View v) {

    dismiss();
  }



  @Override
  public void onCheckedChanged(RadioGroup group, int checkedId) {

    switch (checkedId){
      case R.id.rb_today:
        container_custom_range.setVisibility(View.GONE);
        //Log.e(TAG, "onCheckedChanged: "+"Today");

        Calendar cal_today = Calendar.getInstance();
        date_selected_start=format.format(cal_today.getTime());
        date_selected_end=format.format(cal_today.getTime());
        break;
      case R.id.rb_yesterday:
        container_custom_range.setVisibility(View.GONE);

        Calendar cal_yesterday = Calendar.getInstance();
        cal_yesterday.add(Calendar.DATE, -1);
        date_selected_start=format.format(cal_yesterday.getTime());
        date_selected_end=format.format(cal_yesterday.getTime());

        break;
      case R.id.rb_last_week:
        container_custom_range.setVisibility(View.GONE);
        Calendar cal_last_week = Calendar.getInstance();
        date_selected_start=format.format(cal_last_week.getTime());
        cal_last_week.add(Calendar.DATE, -7);
        date_selected_end=format.format(cal_last_week.getTime());


        break;
      case R.id.rb_last_month:
        container_custom_range.setVisibility(View.GONE);
        Calendar cal_last_month = Calendar.getInstance();
        date_selected_start=format.format(cal_last_month.getTime());
        cal_last_month.add(Calendar.DATE, -30);
        date_selected_end=format.format(cal_last_month.getTime());
        break;
      case R.id.rb_custom_range:
        container_custom_range.setVisibility(View.VISIBLE);



    }



  }
}