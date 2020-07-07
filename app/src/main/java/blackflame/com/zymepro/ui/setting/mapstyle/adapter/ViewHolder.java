package blackflame.com.zymepro.ui.setting.mapstyle.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.setting.mapstyle.model.SelectableItem;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class ViewHolder extends RecyclerView.ViewHolder {
  public TextView title;
  public ImageView image_map;
  public Context context;
  public CheckedTextView checkedTextView;
  SelectableItem item;
  public static final int SINGLE_SELECTION = 1;
  public static final int MULTI_SELECTION = 2;
  public int lastCheckedPosition = -1;


  public SelectableItem mItem;

  public ViewHolder(View view, Context context, final ViewHolder.OnItemSelectedListener listener) {
    super(view);


    title = view.findViewById(R.id.tv_map_title);
    image_map=view.findViewById(R.id.iv_map_style);
    checkedTextView=view.findViewById(R.id.checked_text_item);
    this.context=context;




    image_map.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        lastCheckedPosition=getAdapterPosition();



      }
    });

  }

  public void bind(ArrayList<SelectableItem> list,ViewHolder holder) {
    int position = getAdapterPosition();
    SelectableItem style=list.get(position);
    title.setText(style.getName());
    //image_map.setImageResource(style.getImage());

    TypedValue value = new TypedValue();
    checkedTextView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
    int checkMarkDrawableResId = value.resourceId;
    checkedTextView.setCheckMarkDrawable(checkMarkDrawableResId);
    Glide.with(context)
        .load(style.getImage())
        .into(image_map);


    holder.mItem = style;
    holder.setChecked(holder.mItem.isSelected());


  }


  public interface OnItemSelectedListener {

    void onItemSelected(SelectableItem item);
  }


  public void setChecked(boolean value) {
    if (value) {
      checkedTextView.setBackgroundColor(Color.BLUE);
    } else {
      checkedTextView.setBackground(null);
    }
    mItem.setSelected(value);
    checkedTextView.setChecked(value);
  }
}

