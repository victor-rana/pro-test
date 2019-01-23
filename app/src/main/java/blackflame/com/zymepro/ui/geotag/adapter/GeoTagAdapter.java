package blackflame.com.zymepro.ui.geotag.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.geotag.GeoTagActivity;
import blackflame.com.zymepro.ui.geotag.model.GeotagResponse;
import java.util.ArrayList;

public class GeoTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

  Context mContext;
  ArrayList<GeotagResponse> geotagList;
  private static final int TYPE_ITEM = 2;
  GeoTagActivity activity;
  CardClickListener listener;

  public GeoTagAdapter(GeoTagActivity geotTagActivity, ArrayList<GeotagResponse> tagList, Context context,CardClickListener listener) {
    this.geotagList = tagList;
    this.mContext = context;
    this.activity=geotTagActivity;
    this.listener=listener;

  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    //Inflating recycle view item layout
    View itemView = LayoutInflater
        .from(parent.getContext()).inflate(R.layout.layout_geotag_card, parent, false);
    return new ItemViewHolder(itemView);


  }
  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder,  int pos) {


    if (holder instanceof ItemViewHolder) {
      GeotagResponse response=geotagList.get(pos);
      ((ItemViewHolder) holder).address.setText(response.getAddress());
      ((ItemViewHolder) holder).title.setText(response.getTitle());
      String type=response.getType();
      switch (type){
        case "HOME":
          ((ItemViewHolder) holder).img_type.setImageResource(R.drawable.ic_home);
          break;
        case "OFFICE":
          ((ItemViewHolder) holder).img_type.setImageResource(R.drawable.ic_office);
          break;
        case "OTHER":
          ((ItemViewHolder) holder).img_type.setImageResource(R.drawable.ic_placeholder_pin);
          break;

      }

      ((ItemViewHolder) holder).delete.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onCardClick(holder.getAdapterPosition(),v);
        }
      });



    }

  }

  @Override
  public int getItemViewType(int position) {
    return TYPE_ITEM;
  }


  @Override
  public int getItemCount() {
    return geotagList.size();
  }



  private class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView title,address,delete;
    ImageView img_type;

    public ItemViewHolder(View convertView) {
      super(convertView);
      title=convertView.findViewById(R.id.tv_tag_title);
      address=convertView.findViewById(R.id.tv_tag_address);
      delete=convertView.findViewById(R.id.tv_tag_delete);
      img_type=convertView.findViewById(R.id.iv_tag_identifier);

    }
  }

  public interface CardClickListener{
    void onCardClick(int position,View v);
  }
}

