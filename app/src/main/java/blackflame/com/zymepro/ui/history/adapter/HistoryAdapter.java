package blackflame.com.zymepro.ui.history.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.history.model.TripHistory;
import blackflame.com.zymepro.ui.tripdetails.TripDetailsActivity;
import blackflame.com.zymepro.view.custom.ExpandableLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
  private static final int UNSELECTED = -1;

  private RecyclerView recyclerView;
  private int selectedItem = UNSELECTED;
  private Activity activity;
  private LayoutInflater inflater;
  TextView counter, startDate, startAddress, endAddress,tripTime,max_speed,idle_time,end_time;
  ImageView Show;
  ArrayList<TripHistory> mhistory_arraylist;
  ProgressDialog pDialog;
  Bundle bundle;
  String date_change;
  public HistoryAdapter(RecyclerView recyclerView,ArrayList<TripHistory> history_arraylist,Activity activity) {
    this.recyclerView = recyclerView;
    this.activity = activity;
    this.mhistory_arraylist = history_arraylist;

  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.layout_trip_card, parent, false);



    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.setIsRecyclable(false);
    holder.bind();
  }

  @Override
  public int getItemCount() {
    return mhistory_arraylist.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
    private ExpandableLayout expandableLayout;
    private TextView expandButton,distance;
    private ImageView arrow;

    public ViewHolder(View itemView) {
      super(itemView);

      expandableLayout = itemView.findViewById(R.id.expandable_layout);
      expandableLayout.setInterpolator(new OvershootInterpolator());
      expandableLayout.setOnExpansionUpdateListener(this);
      expandButton = itemView.findViewById(R.id.expand_button);
      startAddress = itemView.findViewById(R.id.StartAddress);
      startDate = itemView.findViewById(R.id.StartDate);
      counter= itemView.findViewById(R.id.trip_counter);
      distance=itemView.findViewById(R.id.distance);
      tripTime=itemView.findViewById(R.id.tripTime);
      idle_time=itemView.findViewById(R.id.idling_time);
      max_speed=itemView.findViewById(R.id.max_speed);
      end_time=itemView.findViewById(R.id.end_time);

      // StartTime = (TextView) convertView.findViewById(R.id.StartTime);
      endAddress = itemView.findViewById(R.id.endAddress);
      arrow=itemView.findViewById(R.id.arrow_expand);

      Show = itemView.findViewById(R.id.show);

      expandButton.setOnClickListener(this);
      arrow.setOnClickListener(this);
    }

    public void bind() {
      int position = getAdapterPosition();
      boolean isSelected = position == selectedItem;

      //expandButton.setText(position + ". Quick View");
      expandButton.setSelected(isSelected);
      expandableLayout.setExpanded(isSelected, false);

      TripHistory helper = mhistory_arraylist.get(position);
      String time=helper.getStartTime();

      String formattedTime=getDate(time);

      distance.setText(String.valueOf(Math.round(((float)helper.getTotal_distance()/1000)*100.00f)/100.00f)+" km");
      float left_time=(float)(helper.getTotal_time()%60)/60*100;
      tripTime.setText(String.valueOf(helper.getTotal_time()/60)+"."+String.valueOf((int)left_time)+" min");
      max_speed.setText(String.valueOf(helper.getMax_speed())+" Km/h");
      idle_time.setText(String.valueOf(helper.getTotal_idle_time())+" sec");
      end_time.setText(getDate(helper.getEndTime()));
      startDate.setText(formattedTime+" - ");
      startAddress.setText(helper.getStartAddress());
      endAddress.setText(helper.getEndAddress());
      counter.setText("Trip "+(mhistory_arraylist.size()-position));

      Show.setTag(position);
      Show.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          int position = (Integer) v.getTag();
          TripHistory helper = mhistory_arraylist.get(position);
          int trip_id = helper.getTripId();
          Intent intent = new Intent(activity, TripDetailsActivity.class);
          intent.putExtra("trip_id",trip_id);
          intent.putExtra("date",helper.getStartTime());
          intent.putExtra("time_consumed",helper.getTime_consumed());
          intent.putExtra("startAddress",helper.getStartAddress());
          intent.putExtra("endAddress",helper.getEndAddress());
          activity.startActivity(intent);
        }
      });

    }

    @Override
    public void onClick(View view) {
      ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
      if (holder != null) {
        holder.expandButton.setSelected(false);
        holder.expandableLayout.collapse();
        holder.arrow.setImageDrawable(Prosingleton.getAppContext().getResources().getDrawable(R.drawable.ic_up_arrow));
      }

      int position = getAdapterPosition();
      if (position == selectedItem) {
        selectedItem = UNSELECTED;
        if (holder !=null)
          arrow.setImageDrawable(Prosingleton.getAppContext().getResources().getDrawable(R.drawable.ic_arrow_up));
      } else {
        expandButton.setSelected(true);
        expandableLayout.expand();
        if (arrow!=null)
          arrow.setImageDrawable(Prosingleton.getAppContext().getResources().getDrawable(R.drawable.ic_down_arrow));
        selectedItem = position;
      }
    }

    @Override
    public void onExpansionUpdate(float expansionFraction, int state) {
      Log.d("ExpandableLayout", "State: " + state);
      if (state == ExpandableLayout.State.EXPANDING) {
        recyclerView.smoothScrollToPosition(getAdapterPosition());

      }
    }
  }



  private String getDate(String OurDate)
  {
    try
    {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      Date value = formatter.parse(OurDate);

      SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm aaa"); //this format changeable
      dateFormatter.setTimeZone(TimeZone.getDefault());
      OurDate = dateFormatter.format(value);

      //Log.d("OurDate", OurDate);
    }
    catch (Exception e)
    {
      OurDate = "00-00-0000 00:00";
    }
    return OurDate;
  }
}