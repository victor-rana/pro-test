package blackflame.com.zymepro.ui.document.documentsave;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import blackflame.com.zymepro.Prosingleton;
import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.PermissionConstants;
import blackflame.com.zymepro.db.DataBaseHelper;
import blackflame.com.zymepro.ui.document.ImageUtil;
import blackflame.com.zymepro.ui.document.model.ImageTableHelper;
import blackflame.com.zymepro.util.PermissionUtils;
import blackflame.com.zymepro.util.ToastUtils;
import blackflame.com.zymepro.view.custom.WheelView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DocumentStoreActivity extends BaseActivity implements OnClickListener,PermissionUtils.SimpleCallback {
  TextView textView_select_document,textView_save;
  ImageView imageView_click,imgView;
  int itemPosition;
  EditText personName;
  String type;
  Bitmap bitmap;
  boolean isImageSelected=false;
  byte []  imageArray;
  final int PICK_IMAGE=1;
  File myDir;
  Uri fileUri;
  String imageName,imagePath,root;
  String mCurrentPhotoPath;
  LinearLayout linearLayout_container;
  WheelView wv_document;
  String[] document_array={"Select document type","Licence", "Insurance", "Registration Certificate", "PUC"};
  private String TAG=DocumentStoreActivity.class.getCanonicalName();
  File dir = null;
  String path;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_document_store);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
  }

  private void initViews(){
  Toolbar  toolbar= findViewById(R.id.toolbar_datasubmit);
  TextView  title= findViewById(R.id.toolbar_title_datasubmit);
  GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Add Documents");
    textView_select_document= findViewById(R.id.spinnerdocument);
    imageView_click= findViewById(R.id.imageViewclick);
    imageView_click.setOnClickListener(this);
    imgView = findViewById(R.id.imageView);
    personName= findViewById(R.id.documentname);
    textView_save= findViewById(R.id.textview_datasave);
    textView_save.setOnClickListener(this);
    textView_select_document.setOnClickListener(this);
    linearLayout_container= findViewById(R.id.container_image);
    dir = ImageUtil.getStorageDirectory(this, "ZymePro");
    root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    myDir = new File(root + "/Zyme");

  }


  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.textview_datasave:
        String perSonName=personName.getText().toString();
        String documentName=textView_select_document.getText().toString();
        if(documentName.equals("Select document")){
          ToastUtils.showShort("Please select document type");
        }else if(perSonName.length()==0){
          ToastUtils.showShort("Please enter name");
        }else if(!isImageSelected){
          ToastUtils.showShort("Please select a image");
        }
        else {
          if(path!=null) {
            String id= UUID.randomUUID().toString();
            ImageTableHelper tablehelper = new ImageTableHelper(perSonName, documentName,path,id,String.valueOf(System.currentTimeMillis()),String.valueOf(System.currentTimeMillis()));
            DataBaseHelper dataBaseHelper = new DataBaseHelper(DocumentStoreActivity.this);
            dataBaseHelper.insertImage(tablehelper);
            ToastUtils.showShort("Saved");
            finish();

          }
        }
        break;
      case R.id.spinnerdocument:
        openDialog();

        break;
      case R.id.imageViewclick:
        if (!PermissionUtils.isGranted(PermissionConstants.getPermissions(PermissionConstants.CAMERA))) {
          PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.CAMERA));
          PermissionUtils.sInstance.callback(this);
          PermissionUtils.sInstance.request();
        }else{
          try{
            dispatchTakePictureIntent();

          }catch (IOException ex){};

        }


        break;

    }
  }
  private void openDialog(){
    View outerView = LayoutInflater.from(DocumentStoreActivity.this).inflate(R.layout.layout_wheel_selection, null);
    outerView.setBackgroundColor(getResources().getColor(R.color.grey));
    Button btn_confirm= outerView.findViewById(R.id.btn_confirm);
    wv_document = outerView.findViewById(R.id.wheel_view_wv);
    wv_document.setOffset(2);
    wv_document.setItems(Arrays.asList(document_array));
    wv_document.setSeletion(0);
    wv_document.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
      @Override
      public void onSelected(int selectedIndex, String item) {
        textView_select_document.setText(item);
      }
    });
    LayoutInflater inflater = getLayoutInflater();
    View titleView = inflater.inflate(R.layout.custom_title_dialog, null);
    TextView textView= titleView.findViewById(R.id.dialog_title);
    textView.setText("");
    final AlertDialog dialog= new AlertDialog.Builder(DocumentStoreActivity.this)
        .setTitle("WheelView in Dialog")
        .setView(outerView)
        .setCustomTitle(titleView)
        .show();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    btn_confirm.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        dialog.dismiss();
      }
    });
  }

  @Override
  public void onGranted() {
      try {
        dispatchTakePictureIntent();
      }catch (IOException e){
        e.printStackTrace();
      }
  }

  private void dispatchTakePictureIntent() throws IOException {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Ensure that there's a camera activity to handle the intent
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      // Create the File where the photo should go
      File photoFile = null;
      try {
        photoFile = createImageFile();
      } catch (IOException ex) {
        // Error occurred while creating the File
        return;
      }
      if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
        fileUri = FileProvider.getUriForFile(DocumentStoreActivity.this,
            "blackflame.com.zymepro" ,
            createImageFile());
        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
          String packageName = resolveInfo.activityInfo.packageName;
          this.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(takePictureIntent, PICK_IMAGE);
      }
    }
  }

  private File createImageFile() throws IOException {
    // Create an image file name
    Random generator = new Random();
    int n = 10000;
    n = generator.nextInt(n);
    imageName = "Zyme_pro" + n + ".jpg";
    File storageDir = new File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DCIM), "Camera");
    File image = File.createTempFile(
        imageName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
    return image;
  }

  @Override
  public void onDenied() {

    // Permission denied
    ToastUtils.showShort("Zyme needs Camera Permission to scan code");
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    try{

      if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
        //previewCapturedImage();
        isImageSelected = true;
        Uri imageUri = Uri.parse(mCurrentPhotoPath);
        File file = new File(imageUri.getPath());


        try {
          InputStream ims = new FileInputStream(file);
          linearLayout_container.setVisibility(View.GONE);
          imgView.setVisibility(View.VISIBLE);
          bitmap=BitmapFactory.decodeStream(ims);
          BitmapFactory.Options options = new BitmapFactory.Options();

          options.inSampleSize = 8;
          bitmap = BitmapFactory.decodeFile(imageUri.getPath(),
              options);
          ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
          bitmap.compress(CompressFormat.PNG, 40, buffer);
          imageArray=buffer.toByteArray();

          path=   ImageUtil.writeImage(DocumentStoreActivity.this,imageArray);
          imgView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
          //return;
          Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

      }

    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
