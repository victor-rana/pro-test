package blackflame.com.zymepro.ui.document.image.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.document.model.ImageTableHelper;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class GridAdapterDocument extends BaseAdapter {
  ArrayList<ImageTableHelper> mlist;
  Activity context;
  DocumetListener listener;
  public GridAdapterDocument(Activity context, ArrayList<ImageTableHelper> list,DocumetListener listener){
    this.context=context;
    mlist=list;
    this.listener=listener;
//        Log.e(TAG, "GridAdapterDocument: "+mlist.size() );
  }
  public GridAdapterDocument(Activity c){
    context=c;
  }
  @Override
  public int getCount() {
    return mlist.size();

  }

  @Override
  public Object getItem(int position) {
//        Log.e(TAG, "getItem: "+position );
    return null;
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(final int position, final View convertView, ViewGroup parent) {
    LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view=inflater.inflate(R.layout.layout_document_image_card,parent,false);
    ImageTableHelper helper=mlist.get(position);
    final ImageView imageView= view.findViewById(R.id.image_document);
    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onFullImage(v,position);
      }
    });

    TextView tvName= view.findViewById(R.id.tvname);
    //TextView tvType= (TextView) view.findViewById(R.id.tvtype);
    ImageView delete= view.findViewById(R.id.iv_document_delete);
    delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onDeleteDocument(v,position);

      }
    });
    tvName.setText(helper.getName());
    Glide.with(context)
        .load(helper.getPath())
        .asBitmap()
        .into(imageView);
    return view;
  }
}
