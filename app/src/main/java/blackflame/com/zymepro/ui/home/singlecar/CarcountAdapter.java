package blackflame.com.zymepro.ui.home.singlecar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import java.util.ArrayList;


public class CarcountAdapter extends BaseAdapter {
    ArrayList<CarcountModel> list_carCount;


    TextView textView_brand,textView_model,textView_registration;
    public CarcountAdapter(ArrayList<CarcountModel> list_carcount){
        list_carCount=list_carcount;

    }

    @Override
    public int getCount() {
        //Log.e("Carcount", "getCount"+list_carCount.size());
        return list_carCount.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.car_count_card, parent,false);
        textView_brand= convertView.findViewById(R.id.carbrand);
        textView_registration= convertView.findViewById(R.id.registration);
        CarcountModel carcountModel=list_carCount.get(position);
        textView_brand.setText(carcountModel.getBrand()+" "+carcountModel.getModel());
        textView_registration.setText(carcountModel.getRegistration());

        return convertView;
    }
    public interface FragmentListListener {
        void onListItemSelected(View view, int position);
    }
}
