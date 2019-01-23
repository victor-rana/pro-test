package blackflame.com.zymepro.ui.liveshare.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.ui.liveshare.LiveShareActivity;

public class DialogDisclaimer extends Dialog implements
    android.view.View.OnClickListener{

  public Activity c;
  boolean isChecked=false;
  public DialogDisclaimer(Activity activity) {
    super(activity);
    this.c=activity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_live_share_dialog);
    ViewGroup.LayoutParams params = getWindow().getAttributes();
    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
    getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    CheckBox checkBox=findViewById(R.id.remember);
    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
        isChecked=checked;
      }
    });

    Button btn_remind= findViewById(R.id.btn_ok);
    btn_remind.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        if (isChecked){
          CommonPreference.getInstance().setRemember();

        }

        if (c instanceof LiveShareActivity){
          ((LiveShareActivity) c).generate_url();
        }

        dismiss();


      }
    });


  }

  @Override
  public void onClick(View v) {
    dismiss();
  }
}
