package blackflame.com.zymepro.ui.setting.mapstyle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.setting.mapstyle.model.MapStyle;
import blackflame.com.zymepro.ui.setting.mapstyle.model.SelectableItem;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class StyleAdapter extends RecyclerView.Adapter<StyleAdapter.ViewHolder> {
  ArrayList<SelectableItem> mlist;
  //	ViewHolder.OnItemSelectedListener listener;
  private ArrayList<Integer> selectCheck;
  public int lastCheckedPosition = -1;


  private boolean isMultiSelectionEnabled = false;

  public StyleAdapter(ArrayList<MapStyle> list, boolean isMultiSelectionEnabled) {
    //this.listener = listener;
    mlist = new ArrayList<>();
    this.isMultiSelectionEnabled = isMultiSelectionEnabled;


    for (MapStyle item : list) {
      this.mlist.add(new SelectableItem(item, false));
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.layout_map_style_card, parent, false);

    return new ViewHolder(itemView, itemView.getContext());
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //holder.bind(mlist);

    SelectableItem style = mlist.get(position);
    holder.title.setText(style.getName());
    //image_map.setImageResource(style.getImage());

    TypedValue value = new TypedValue();
    holder.checkedTextView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
    int checkMarkDrawableResId = value.resourceId;
    holder.checkedTextView.setCheckMarkDrawable(checkMarkDrawableResId);
    Glide.with(holder.context)
        .load(style.getImage())
        .into(holder.image_map);


    holder.mItem = style;
    holder.setChecked(position == lastCheckedPosition,checkMarkDrawableResId);
  }

  @Override
  public int getItemCount() {
    return mlist.size();
  }


  public List<MapStyle> getSelectedItems() {

    List<MapStyle> selectedItems = new ArrayList<>();
    for (SelectableItem item : mlist) {
      if (item.isSelected()) {
        selectedItems.add(item);
      }
    }
    return selectedItems;
  }


  @Override
  public int getItemViewType(int position) {
    if (isMultiSelectionEnabled) {
      return ViewHolder.MULTI_SELECTION;
    } else {
      return ViewHolder.SINGLE_SELECTION;
    }
  }


  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView image_map;
    public Context context;
    public CheckedTextView checkedTextView;
    SelectableItem item;
    public static final int SINGLE_SELECTION = 1;
    public static final int MULTI_SELECTION = 2;


    public SelectableItem mItem;

    public ViewHolder(View view, Context context) {
      super(view);


      title = view.findViewById(R.id.tv_map_title);
      image_map = view.findViewById(R.id.iv_map_style);
      checkedTextView = view.findViewById(R.id.checked_text_item);
      this.context = context;

      image_map.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          lastCheckedPosition = getAdapterPosition();

          notifyDataSetChanged();

        }
      });

    }

    public void bind(ArrayList<SelectableItem> list) {
      int position = getAdapterPosition();
      SelectableItem style = list.get(position);
      title.setText(style.getName());
      //image_map.setImageResource(style.getImage());

      TypedValue value = new TypedValue();
      checkedTextView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
      int checkMarkDrawableResId = value.resourceId;
      checkedTextView.setCheckMarkDrawable(checkMarkDrawableResId);
      Glide.with(context)
          .load(style.getImage())
          .into(image_map);
      mItem = style;
      setChecked(mItem.isSelected(),checkMarkDrawableResId);


    }


    public void setChecked(boolean value,int drawable) {

      if (value) {
        checkedTextView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
      } else {
        checkedTextView.setBackground(null);
      }
      mItem.setSelected(value);
      checkedTextView.setChecked(value);
      if (value){
        checkedTextView.setCheckMarkDrawable(R.drawable.checked_tick);
      }else{
        checkedTextView.setCheckMarkDrawable(drawable);
      }
    }


  }
}

