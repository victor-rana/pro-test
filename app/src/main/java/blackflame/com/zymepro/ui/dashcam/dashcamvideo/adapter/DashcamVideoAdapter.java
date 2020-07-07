package blackflame.com.zymepro.ui.dashcam.dashcamvideo.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.dashcam.dashcamvideo.model.DashcamVideoModel;
import java.util.ArrayList;

public class DashcamVideoAdapter extends RecyclerView.Adapter<DashcamVideoAdapter.ViewHolder> {

  private ArrayList<DashcamVideoModel> model=new ArrayList<>();

  private LayoutInflater mInflater;
  private ItemClickListener mClickListener;
  Activity mContext;

  // data is passed into the constructor
  public DashcamVideoAdapter(ItemClickListener listener, ArrayList<DashcamVideoModel> data) {

    this.model = data;
    this.mClickListener=listener;
  }

  // inflates the cell layout from xml when needed
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    this.mInflater = LayoutInflater.from(parent.getContext());
    View view = mInflater.inflate(R.layout.layout_dashcam_video_card, parent, false);
    final ViewHolder mViewHolder = new ViewHolder(view);

    return mViewHolder;
  }

  // binds the data to the textview in each cell
  @Override
  public void onBindViewHolder(ViewHolder holder, final int position) {
    DashcamVideoModel dashcamVideoModel=model.get(position);
    holder.textView_date.setText(dashcamVideoModel.getDate());
    holder.textView_time.setText(dashcamVideoModel.getTime());
    holder.textView_length.setText(dashcamVideoModel.getSize());
    holder.textView_duration.setText(dashcamVideoModel.getLength()+" sec");
    holder.imageView_thumb_nail.setImageBitmap(dashcamVideoModel.getBitmap());


  }

  // total number of cells
  @Override
  public int getItemCount() {
    return model.size();
  }


  // stores and recycles views as they are scrolled off screen
  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView textView_length,textView_time,textView_date,textView_duration;
    ImageView imageView_thumb_nail,imageView_share,imageView_play;
    ImageView imageView_more;

    ViewHolder(View itemView) {
      super(itemView);
      textView_date= itemView.findViewById(R.id.video_date);
      textView_length= itemView.findViewById(R.id.video_length);
      textView_duration= itemView.findViewById(R.id.video_duration);
      textView_time= itemView.findViewById(R.id.video_time);
      imageView_thumb_nail= itemView.findViewById(R.id.dashcam_video_thumbnail);
      imageView_play= itemView.findViewById(R.id.iv_video_play);
      imageView_share= itemView.findViewById(R.id.iv_video_share);
      imageView_more= itemView.findViewById(R.id.iv_dashcam_more);
      imageView_more.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mClickListener.onMoreClick(getAdapterPosition(),v);
        }
      });
      imageView_share.setOnClickListener(this);
      imageView_play.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
      mClickListener.onItemClick(view,getAdapterPosition());
    }
  }

  // convenience method for getting data at click position
  DashcamVideoModel getItem(int id) {
    return model.get(id);
  }

  // allows clicks events to be caught
  public  void setClickListener(ItemClickListener itemClickListener) {
    this.mClickListener = itemClickListener;
  }

  // parent activity will implement this method to respond to click events
  public  interface ItemClickListener {
    void onItemClick(View view, int position);

    void onMoreClick(int position,View v);
  }



}
