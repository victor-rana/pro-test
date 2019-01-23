package blackflame.com.zymepro.ui.pastnotification.adapter;

import static blackflame.com.zymepro.util.UtilityMethod.getDate;
import static blackflame.com.zymepro.util.UtilityMethod.getDatewithMonth;
import static blackflame.com.zymepro.util.UtilityMethod.getTime;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.pastnotification.listener.NotificationClickListener;
import blackflame.com.zymepro.ui.pastnotification.model.HistoricResponse;
import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    ArrayList<HistoricResponse> list;

  NotificationClickListener listener;
public class MyViewHolder extends RecyclerView.ViewHolder {
  TextView textView_title, textView_data, textView_date, textView_month;
  ImageView iv_info;
  CardView cardView;


  public MyViewHolder(View view) {
    super(view);
    //textView_data= (TextView) view.findViewById(R.id.notif_description);
    textView_date= view.findViewById(R.id.notif_date);
    textView_month= view.findViewById(R.id.notif_time);
    textView_title= view.findViewById(R.id.notif_type);
    cardView= view.findViewById(R.id.card_notification);
    iv_info= view.findViewById(R.id.iv_info);


  }
}

  public NotificationAdapter(ArrayList<HistoricResponse> historyResponses,NotificationClickListener listener) {
    this.list = historyResponses;
    this.listener=listener;

  }

  @Override
  public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.layout_past_notification_card, parent, false);

    return new NotificationAdapter.MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final NotificationAdapter.MyViewHolder holder, int position) {
    HistoricResponse notificationResponse=list.get(position);
    holder.textView_title.setText(notificationResponse.getTitle());
    holder.textView_month.setText(getTime(notificationResponse.getTime()));
    holder.textView_date.setText(getDatewithMonth(notificationResponse.getTime()));
    holder.iv_info.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.itemClick(holder.getAdapterPosition(),v);
      }
    });

    //holder.textView_data.setText(Html.fromHtml(notificationResponse.getBody()));
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemCount() {
    return  list.size();
  }





}
