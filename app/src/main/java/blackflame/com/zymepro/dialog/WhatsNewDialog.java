package blackflame.com.zymepro.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Scanner;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.db.CommonPreference;

public class WhatsNewDialog extends Dialog implements
        View.OnClickListener {

    public WhatsNewDialog(@NonNull Context context) {
        super(context);
    }

    public Activity c;
    public Dialog d;
    public TextView dismiss;
    View view_clicked;
    long starttime = 0;
    public WhatsNewDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_whats_new);

        //  textView.setText(c.getResources().getString(R.string.whatsnew));



        //textView.setText(Html.fromHtml(c.getResources().getString(R.string.whatsnew)));
        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dismiss= findViewById(R.id.tv_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonPreference.initializeInstance(c);
                CommonPreference.getInstance().setWhatsShown();
                dismiss();
            }
        });


    }


    @Override
    public void onClick(View v) {

        dismiss();
    }

    private int returnInt(String value){
        Scanner in = new Scanner(value).useDelimiter("[^0-9]+");
        return  in.nextInt();}
}

