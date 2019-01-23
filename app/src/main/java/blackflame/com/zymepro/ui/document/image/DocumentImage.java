package blackflame.com.zymepro.ui.document.image;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.PermissionConstants;
import blackflame.com.zymepro.constant.PermissionConstants.Permission;
import blackflame.com.zymepro.db.DataBaseHelper;
import blackflame.com.zymepro.ui.document.documentsave.DocumentStoreActivity;
import blackflame.com.zymepro.ui.document.fullimage.FullImageActivity;
import blackflame.com.zymepro.ui.document.image.adapter.DocumetListener;
import blackflame.com.zymepro.ui.document.image.adapter.GridAdapterDocument;
import blackflame.com.zymepro.ui.document.model.ImageTableHelper;
import blackflame.com.zymepro.util.PermissionUtils;
import blackflame.com.zymepro.util.ToastUtils;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import java.io.File;
import java.util.ArrayList;

public class DocumentImage extends BaseActivity implements DocumetListener,PermissionUtils.SimpleCallback {
  GridView gridView;
  GridAdapterDocument documentAdapter;
  ArrayList<ImageTableHelper> fileList;
  String title;
  LinearLayout image_no_document;
  TextView tv_add_document;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_document_image);

    GlobalReferences.getInstance().baseActivity=this;
    initViews();
  }


  private void initViews() {
    try {
      title = getIntent().getStringExtra("title");
    } catch (ClassCastException e) {
      ToastUtils.showShort("Some thing wrong");

    }

    Toolbar toolbar = findViewById(R.id.toolbar_common);
    TextView textView_Title = findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar, textView_Title, title);

    fileList = new ArrayList<>();
    gridView = findViewById(R.id.gridview);
    image_no_document = findViewById(R.id.ll_no_documet);
    tv_add_document = findViewById(R.id.tv_add_documet);
    tv_add_document.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent= new Intent(DocumentImage.this,DocumentStoreActivity.class);
        startActivity(intent);

      }

    });

    documentAdapter = new GridAdapterDocument(DocumentImage.this, fileList, DocumentImage.this);
    gridView.setAdapter(documentAdapter);
    if (!PermissionUtils.isGranted(PermissionConstants.getPermissions(PermissionConstants.CAMERA))) {
      PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.CAMERA));
    PermissionUtils.sInstance.callback(this);
    PermissionUtils.sInstance.request();
  }

  }

  @Override
  public void onDeleteDocument(View view, final int position) {
    new AwesomeInfoDialog(this)
        .setTitle("Alert")
        .setMessage("Are you sure you want to delete?")
        .setColoredCircle(R.color.dialogInfoBackgroundColor)
        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
        .setCancelable(true)
        .setPositiveButtonText("Yes")
        .setPositiveButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
        .setPositiveButtonTextColor(R.color.white)
        .setNegativeButtonText("NO")
        .setNegativeButtonbackgroundColor(R.color.dialogInfoBackgroundColor)
        .setNegativeButtonTextColor(R.color.white)
        .setPositiveButtonClick(new Closure() {
                                  @Override
                                  public void exec() {
                                    //click
                                    ImageTableHelper  helper=fileList.get(position);
                                    Log.e("Image", "onClick: "+helper.getId() );
                                    new DataBaseHelper(DocumentImage.this).deleteDocument(helper.getId());

                                    if (helper.getFile().delete()){
                                      fileList.remove(position);
                                      ToastUtils.showShort("Document removed successfully");
                                    }

                                    // documentAdapter.notifyDataSetChanged();
                                    if (fileList.size()>0){
                                      documentAdapter.notifyDataSetChanged();
                                      gridView.setVisibility(View.VISIBLE);
                                      image_no_document.setVisibility(View.GONE);
                                    }else {
                                      gridView.setVisibility(View.GONE);
                                      image_no_document.setVisibility(View.VISIBLE);
                                    }
                                  }
                                }
        )
        .setNegativeButtonClick(new Closure() {
          @Override
          public void exec() {
            //click
          }
        })
        .show();
  }

  @Override
  public void onFullImage(View view, int position) {
    ImageTableHelper  helper=fileList.get(position);
    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
    i.putExtra("id", position);
    i.putExtra("path",helper.getPath());
    startActivity(i);
  }

  @Override
  public void onGranted() {
      // Permission granted
      new LoadImage().execute();

  }

  @Override
  public void onDenied() {
  ToastUtils.showShort("Zyme needs Camera Permission to scan code");

  }



  public class LoadImage extends AsyncTask<Void,Void,Void> {
    @Override
    protected Void doInBackground(Void... params) {

      final ArrayList<ImageTableHelper> helper=new DataBaseHelper(DocumentImage.this).getImageData();

      for (int i = 0; i < helper.size(); i++) {

        ImageTableHelper helper1 = helper.get(i);
        String type = helper1.getType();
        if (type.equals(title)) {

          String path=helper1.getPath();


          // Uri uri=Uri.parse(path);
          File file=new File(path);
          if (file.exists()){

            ImageTableHelper imageTableHelper = new ImageTableHelper();
            imageTableHelper.setType(helper1.getType());
            imageTableHelper.setFile(file);
            imageTableHelper.setName(helper1.getName());
            imageTableHelper.setId(helper1.getId());
            imageTableHelper.setPath(helper1.getPath());
            imageTableHelper.setLast_modified(helper1.getLast_modified());
            imageTableHelper.setCreation_date(helper1.getCreation_date());

            fileList.add(imageTableHelper);
            Log.e("Size", "doInBackground: "+fileList.size() );
          }

        }

      }
      new Handler(getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
          //gridView.setAdapter(documentAdapter);
          //imagetest.setImageBitmap(bitmap);
          if (fileList.size()>0){
            documentAdapter.notifyDataSetChanged();
            gridView.setVisibility(View.VISIBLE);
            image_no_document.setVisibility(View.GONE);
          }else {
            gridView.setVisibility(View.GONE);
            image_no_document.setVisibility(View.VISIBLE);
          }




        }
      });

      return null;
    }
  }
}
