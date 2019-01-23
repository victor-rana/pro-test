package blackflame.com.zymepro.ui.dashcam.dashcamvideo;


import static blackflame.com.zymepro.util.UtilityMethod.getTime;
import static blackflame.com.zymepro.util.UtilityMethod.humanReadableByteCount;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.common.GlobalReferences;
import blackflame.com.zymepro.constant.PermissionConstants;
import blackflame.com.zymepro.constant.PermissionConstants.Permission;
import blackflame.com.zymepro.ui.dashcam.dashcamvideo.adapter.DashcamVideoAdapter;
import blackflame.com.zymepro.ui.dashcam.dashcamvideo.model.DashcamVideoModel;
import blackflame.com.zymepro.util.PermissionUtils;
import blackflame.com.zymepro.util.UtilityMethod;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DashcamVideo extends BaseActivity implements DashcamVideoAdapter.ItemClickListener,PermissionUtils.SimpleCallback {
  RecyclerView recyclerView_video;
  DashcamVideoAdapter adapter;
  int length;
  FrameLayout progressBar;
  Toolbar toolbar_dashcam_video;
  TextView textView_title;
  int priority;
  TextView textView_sort;
  File[] files;
  Point point;


  ArrayList<DashcamVideoModel> dashcamVideoModels=new ArrayList<>();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashcam_video);
    GlobalReferences.getInstance().baseActivity=this;
    initViews();
  }


  private void initViews(){
    Toolbar toolbar=findViewById(R.id.toolbar_common);
    TextView title=findViewById(R.id.toolbar_title);
    GlobalReferences.getInstance().baseActivity.setToolbar(toolbar,title,"Dashcam Videos");
    textView_sort= findViewById(R.id.tv_sort_video);
    progressBar= findViewById(R.id.progressBarHolder);
    recyclerView_video.setLayoutManager(new GridLayoutManager(this, 2));
    adapter = new DashcamVideoAdapter(DashcamVideo.this, dashcamVideoModels);
    recyclerView_video.setAdapter(adapter);

    if (PermissionUtils.isGranted(PermissionConstants.STORAGE)){
      new LoadVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }else{
        PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.STORAGE));
        PermissionUtils.sInstance.callback(this);
        PermissionUtils.sInstance.request();
      }


    textView_sort.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (length>0) {

          Dialog dialog = onCreateDialogSingleChoice();
          dialog.show();
        }
      }
    });
    }


  @Override
  public void onItemClick(View view, int position) {
    DashcamVideoModel utils=dashcamVideoModels.get(position);
    // Uri uri =Uri.parse(utils.getPath().getAbsolutePath());
    final File file=utils.getPath();

    final Uri fileUri = FileProvider.getUriForFile(DashcamVideo.this,
        "blackflame.com.zymepro" ,
        file);

    if(view.getId()==R.id.iv_video_play){
      view.setBackground(getResources().getDrawable(R.drawable.ripple_effect));

      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.addFlags(
          Intent.FLAG_GRANT_READ_URI_PERMISSION);

      intent.setDataAndType(fileUri, "video/*");
      //intent.setDataAndTypeAndNormalize(fileUri, "video/*");
      startActivity(intent);
    }

    if(view.getId()==R.id.iv_video_share){
      view.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
      shareImage(file);
    }
  }

  @Override
  public void onMoreClick(int position, View v) {
    int[] location = new int[2];
    v.getLocationOnScreen(location);
    point=new Point();
    point.x=location[0];
    point.y=location[1];
  }

  private void shareImage(File path) {

    try {
      Intent share = new Intent(Intent.ACTION_SEND);

      // If you want to share a png image only, you can do:
      // setType("image/png"); OR for jpeg: setType("image/jpeg");
      share.setType("video/*");

      // Make sure you put example png image named myImage.png in your
      // directory


      //File imageFileToShare = new File(path);

      Uri uri = Uri.fromFile(path);
      share.putExtra(Intent.EXTRA_STREAM, uri);

      startActivity(Intent.createChooser(share, "Share Video!"));
    }catch (Exception ex){
      Log.e("DVideo", "shareImage: "+ex.getMessage() );
    }
  }

  @Override
  public void onGranted() {
    new LoadVideo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

  }

  @Override
  public void onDenied() {

  }



  public class LoadVideo extends AsyncTask<Void,Void,Void> {
    @Override
    protected void onPreExecute() {
      progressBar.setVisibility(View.VISIBLE);
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
      String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"ZymePro";
      Log.d("Files", "Path: " + path);
      File directory = new File(path);
      files = directory.listFiles();
      if(files!=null){

        length=files.length;

      }
      // Log.e("Dashcam", "doInBackground: length"+length );
      for (int i = 0; i < length; i++)
      {
        try {
          DashcamVideoModel utils = new DashcamVideoModel();
          // String name=files[i].getName().substring(0,files[i].getName().length()-4);
          long file_length=files[i].length();
          utils.setFile_length(file_length);

          if(file_length>0) {
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            long fileSizeInKB = file_length / 1024;
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            long fileSizeInMB = fileSizeInKB / 1024;
            utils.setSize(humanReadableByteCount(file_length,true));
          }
          utils.setPath(files[i]);
          Date file_date=new Date(files[i].lastModified());
          utils.setDate_file(file_date);

          utils.setDate(UtilityMethod.getDate(file_date));
          utils.setTime(getTime(file_date));
          Bitmap bMap = ThumbnailUtils
              .createVideoThumbnail(files[i].getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
          MediaMetadataRetriever retriever = new MediaMetadataRetriever();
          retriever.setDataSource(files[i].getAbsolutePath());

          String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
          long timeInmillisec = Long.parseLong(time);
          utils.setFile_duration_time(timeInmillisec);

          long duration = timeInmillisec / 1000;
          long hours = duration / 3600;
          long minutes = (duration - hours * 3600) / 60;
          long seconds = duration - (hours * 3600 + minutes * 60);
          String formattime = String.format("%02d:%02d", minutes, seconds);
          utils.setLength(formattime);
          utils.setLength(String.valueOf(duration));
          utils.setBitmap(bMap);
          dashcamVideoModels.add(utils);
          new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              adapter.notifyDataSetChanged();
            }
          });

          Log.d("Files", "FileName:" + files[i].getName());
        }catch (Exception e){
          e.printStackTrace();
        }
      }



      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      adapter.notifyDataSetChanged();
      progressBar.setVisibility(View.GONE);
    }

  }

  public Dialog onCreateDialogSingleChoice() {

    //Initialize the Alert Dialog
    AlertDialog.Builder builder = new AlertDialog.Builder(DashcamVideo.this);
    //Source of the data in the DIalog
    CharSequence[] array = {"Date : Ascending","Date : Descending", "File Size : Ascending","File Size : Descending","Duration : Ascending","Duration : Descending"};

    // Set the dialog title
    builder.setTitle("Sort By")
        // Specify the list array, the items to be selected by default (null for none),
        // and the listener through which to receive callbacks when items are selected
        .setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {

            priority=which;
          }
        })

        // Set the action buttons
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            // User clicked OK, so save the result somewhere
            // or return them to the component that opened the dialog
            // Log.e(TAG, "onClick: priorityclick"+id );
            if(priority==0){
              Collections.sort(dashcamVideoModels, new Comparator<DashcamVideoModel>(){
                public int compare(DashcamVideoModel s1, DashcamVideoModel s2) {

                  return  (s1.getDate_file().getTime() > s2.getDate_file().getTime() ? 1 : -1);
                }

              });
              adapter.notifyDataSetChanged();
            }
            if(priority==1){
              Collections.sort(dashcamVideoModels, new Comparator<DashcamVideoModel>(){
                public int compare(DashcamVideoModel s1, DashcamVideoModel s2) {
                  return (s1.getDate_file().getTime() > s2.getDate_file().getTime() ? -1 : 1);
                }

              });
              adapter.notifyDataSetChanged();
            }
            if(priority==2){
              Collections.sort(dashcamVideoModels, new Comparator<DashcamVideoModel>(){
                public int compare(DashcamVideoModel s1, DashcamVideoModel s2) {
                  return (int)(s1.getFile_length()-s2.getFile_length());
                }

              });
              adapter.notifyDataSetChanged();
            }
            if(priority==3){
              Collections.sort(dashcamVideoModels, new Comparator<DashcamVideoModel>(){
                public int compare(DashcamVideoModel s1, DashcamVideoModel s2) {
                  return (int)(s2.getFile_length()-s1.getFile_length());
                }

              });
              adapter.notifyDataSetChanged();
            }
            if(priority==4){
              Collections.sort(dashcamVideoModels, new Comparator<DashcamVideoModel>(){
                public int compare(DashcamVideoModel s1, DashcamVideoModel s2) {
                  return  (s1.getFile_duration_time() > s2.getFile_duration_time() ? 1 : -1);

                }

              });
              adapter.notifyDataSetChanged();
            }
            if(priority==5){
              Collections.sort(dashcamVideoModels, new Comparator<DashcamVideoModel>(){
                public int compare(DashcamVideoModel s1, DashcamVideoModel s2) {
                  return  (s1.getFile_duration_time() > s2.getFile_duration_time() ? -1 : 1);
                }

              });
              adapter.notifyDataSetChanged();
            }

          }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {

          }
        });

    return builder.create();
  }
}
