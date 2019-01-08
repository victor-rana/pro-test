package blackflame.com.zymepro.ui.home.navigation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;

/**
 * Created by Prashant on 29-03-2017.
 */

public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    Context mContext;
    int layoutResourceId;
    DataModel data[] = null;
    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DataModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = listItem.findViewById(R.id.textViewName);

        DataModel folder = data[position];
        imageViewIcon.setImageResource(folder.icon);

        textViewName.setText(folder.name);
//        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_top_to_bottom);
//        animation.setStartOffset(position*50);
//        listItem.startAnimation(animation);
        return listItem;
    }

}
