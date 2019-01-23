package blackflame.com.zymepro.ui.enginescan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.enginescan.model.ErrorCode;
import java.util.ArrayList;



public class ErrorListAdapter extends BaseAdapter {
ArrayList<ErrorCode> errorCodeArrayList=new ArrayList<>();
    Context context;
    private LayoutInflater inflater;
    TextView textView_error,textView_title,textView_description,textView_more;
    public ErrorListAdapter(ArrayList<ErrorCode> errorCodes, Context context){
        this.errorCodeArrayList=errorCodes;
        this.context=context;
    }
    @Override
    public int getCount() {
        return errorCodeArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ErrorCode code=errorCodeArrayList.get(i);
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.card_error_code, null);
        textView_error= view.findViewById(R.id.tv_error);
        textView_description= view.findViewById(R.id.tv_descriptiom);
        textView_title= view.findViewById(R.id.tv_title);

       // Log.e("Error", "getView: "+code.getCode() );
       // Log.e("Error", "getView: "+code.getDescription() );
       // Log.e("Error", "getView: "+code.getTitle() );

        textView_error.setText(code.getCode());
        textView_description.setText(code.getDescription());
        textView_title.setText(code.getTitle());


        return view;
    }
}
