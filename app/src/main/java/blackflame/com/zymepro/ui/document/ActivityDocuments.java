package blackflame.com.zymepro.ui.document;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.ui.document.documentsave.DocumentStoreActivity;
import blackflame.com.zymepro.ui.document.image.DocumentImage;
import blackflame.com.zymepro.ui.document.model.ImageTableHelper;
import java.util.ArrayList;

public class ActivityDocuments extends BaseActivity implements View.OnClickListener {
  private static final int SELECT_IMAGE =12 ;

  RelativeLayout relativeLayout_container;
  ArrayList<ImageTableHelper> fileList;
  String type;
  Button btAddDocument;
  TextView textView_Title;
  RelativeLayout relativeLayout_registration,relativeLayout_puc,relativeLayout_licence,relativeLayout_insurance;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_documents);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
  }
  private void initViews(){
   Toolbar toolbar= findViewById(R.id.toolbar_common);
    textView_Title= findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,textView_Title,"Documents");
    relativeLayout_insurance= findViewById(R.id.insurance);
    relativeLayout_licence= findViewById(R.id.licence);
    relativeLayout_puc= findViewById(R.id.puc);
    relativeLayout_registration= findViewById(R.id.registration);
    btAddDocument= findViewById(R.id.add_document);
    relativeLayout_insurance.setOnClickListener(this);
    relativeLayout_licence.setOnClickListener(this);
    relativeLayout_puc.setOnClickListener(this);
    relativeLayout_registration.setOnClickListener(this);
    btAddDocument.setOnClickListener(this);

  }


  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.insurance:
        Intent insurence=new Intent(ActivityDocuments.this,DocumentImage.class);
        // intent.putExtra("dataList",tablehelper);
        insurence.putExtra("title","Insurance");
        startActivity(insurence);
        break;
      case R.id.licence:
        Intent licence=new Intent(ActivityDocuments.this,DocumentImage.class);
        licence.putExtra("title","Licence");
        startActivity(licence);
        break;
      case R.id.puc:
        Intent puc=new Intent(ActivityDocuments.this,DocumentImage.class);
        puc.putExtra("title","PUC");
        startActivity(puc);

        break;
      case R.id.registration:
        Intent intent=new Intent(ActivityDocuments.this,DocumentImage.class);
        intent.putExtra("title","Registration Certificate");
        startActivity(intent);
        break;
      case R.id.add_document:
        Intent document= new Intent(ActivityDocuments.this,DocumentStoreActivity.class);
        startActivity(document);
        break;
    }

  }
}
