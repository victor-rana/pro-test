package blackflame.com.zymepro.ui.enginescan;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.ui.enginescan.adapter.ErrorListAdapter;
import blackflame.com.zymepro.ui.enginescan.model.ErrorCode;
import java.util.ArrayList;

public class ErrorListDialog extends Dialog implements android.view.View.OnClickListener {

  public Activity activity;
  public Dialog d;
  public Button yes, no;
  ArrayList<ErrorCode> list;
  ErrorListAdapter listAdapter;
  ListView listView;
  public ErrorListDialog(Activity activity, ArrayList<ErrorCode> list) {
    super(activity);
    this.activity = activity;
    this.list=list;
    listAdapter=new ErrorListAdapter(list,activity);
  }



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.layout_error_list);
    yes = findViewById(R.id.btn_ok);
    yes.setOnClickListener(this);

    listView= findViewById(R.id.lv);
    listView.setAdapter(listAdapter);


  }
  public void notifyData(){
    listAdapter.notifyDataSetChanged();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_ok:
        dismiss();
        break;
      default:
        break;
    }
    dismiss();
  }
}
