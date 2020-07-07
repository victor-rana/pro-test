package blackflame.com.zymepro.ui.dashcam.recordvideo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.base.BaseActivity;
import blackflame.com.zymepro.db.CommonPreference;
import blackflame.com.zymepro.mqtt.MqttHandler;
import blackflame.com.zymepro.ui.home.MqttDataListener;
import blackflame.com.zymepro.util.Analytics;
import blackflame.com.zymepro.util.ToastUtils;

import com.github.anastr.speedviewlib.ProgressiveGauge;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.FotoapparatSwitcher;
import io.fotoapparat.error.CameraErrorCallback;
import io.fotoapparat.hardware.CameraException;
import io.fotoapparat.parameter.LensPosition;
import io.fotoapparat.view.CameraView;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static io.fotoapparat.log.Loggers.fileLogger;
import static io.fotoapparat.log.Loggers.logcat;
import static io.fotoapparat.log.Loggers.loggers;
import static io.fotoapparat.parameter.selector.AspectRatioSelectors.standardRatio;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.lensPosition;
import static io.fotoapparat.parameter.selector.Selectors.firstAvailable;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;

public class RecordVideoActivity extends BaseActivity implements MqttDataListener {
  private final static String DEBUG_TAG = "Dashcam";
  private static final String TAG = "MainActivity";
  private static final int REQUEST_CODE = 1000;
  private int mScreenDensity;
  @RequiresApi(api = LOLLIPOP)
  private MediaProjectionManager mProjectionManager;
  private static  int DISPLAY_WIDTH =1280;
  private static  int DISPLAY_HEIGHT = 720;
  @RequiresApi(api = LOLLIPOP)
  private MediaProjection mMediaProjection;
  @RequiresApi(api = LOLLIPOP)
  private VirtualDisplay mVirtualDisplay;
  @RequiresApi(api = LOLLIPOP)
  private MediaProjectionCallback mMediaProjectionCallback;
  //private ToggleButton mToggleButton;
  private MediaRecorder mMediaRecorder;
  private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
  private static final int REQUEST_PERMISSIONS = 10;
  boolean isRecordingStart=false;
  boolean isSubscribed = false, isSubsCribedCall = false;
  File folder;
  String videoName;
  File videoFileName;
  int maxSpeed,maxRpm;
  TextView tv_maxSpeed,tv_maxRpm,tv_currentSpeed,tv_currentRpm,tv_record;
  boolean isTouch=true;
  private FotoapparatSwitcher fotoapparatSwitcher;
  private Fotoapparat backFotoapparat;
  ImageView imageButton;
  ProgressiveGauge progressiveGauge_Speed,progressiveGauge_rpm;
  MqttAndroidClient mqttAndroidClient;
  String imei;
  // com.flurgle.camerakit.CameraView camera;

  static {
    ORIENTATIONS.append(Surface.ROTATION_0, 90);
    ORIENTATIONS.append(Surface.ROTATION_90, 0);
    ORIENTATIONS.append(Surface.ROTATION_180, 270);
    ORIENTATIONS.append(Surface.ROTATION_270, 180);
  }
  boolean success;
  CameraView cameraView;
  FrameLayout camera_view;
  RelativeLayout parentLayout;
  String subscriptionTopic;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_record_video);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    progressiveGauge_rpm= findViewById(R.id.progress_rpm);
    progressiveGauge_Speed= findViewById(R.id.progress_speed);
    tv_currentSpeed= findViewById(R.id.currentSpeed);
    tv_currentRpm= findViewById(R.id.currentRpm);
    tv_maxRpm= findViewById(R.id.maxRpm);
    tv_maxSpeed= findViewById(R.id.maxSpeed);
    imageButton = findViewById(R.id.toggle);
    CommonPreference.initializeInstance(RecordVideoActivity.this);
    subscriptionTopic= CommonPreference.getInstance().getSubscriptionTopic();
    imei=CommonPreference.getInstance().getImeiForAPI();
    imageButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

        long milliSeconds = System.currentTimeMillis();

        System.out.println(milliSeconds);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        videoName = formatter.format(calendar.getTime());
        videoFileName = new File(folder.getAbsolutePath());
        if (!videoFileName.exists()) {
          try {
            videoFileName.mkdirs();
            videoFileName.createNewFile();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        System.out.println(formatter.format(calendar.getTime()));

        startRecording();
      }
    });


    Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    if(isSDPresent) {
      // yes SD-card is present
      folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
          File.separator + "ZymePro");
      if (!folder.exists()) {
        success = folder.mkdirs();
      }
    }
    if (Build.VERSION.SDK_INT >= 21) {
      aboveApilevel21();
      tv_record= findViewById(R.id.tvRecord);
      camera_view = findViewById(R.id.camera_view);
      parentLayout= findViewById(R.id.parentlayout);


    } else {
      Toast.makeText(this, "Your current api level is" + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show();
    }

    cameraView= findViewById(R.id.camera_view1);
    setupFotoapparat();
    imei=CommonPreference.getInstance().getImeiForAPI();
    MqttHandler.registerMqtt(imei,this);


  }


  private void setupFotoapparat() {
    Fotoapparat frontFotoapparat = createFotoapparat(LensPosition.FRONT);
    backFotoapparat = createFotoapparat(LensPosition.BACK);
    fotoapparatSwitcher = FotoapparatSwitcher.withDefault(backFotoapparat);
  }

  private Fotoapparat createFotoapparat(LensPosition position) {
    return Fotoapparat
        .with(this)
        .into(cameraView)
        .photoSize(standardRatio(biggestSize()))
        .lensPosition(lensPosition(position))
        .focusMode(firstAvailable(
            continuousFocus(),
            autoFocus(),
            fixed()
        ))

        .logger(loggers(
            logcat(),
            fileLogger(this)
        ))
        .cameraErrorCallback(new CameraErrorCallback() {
          @Override
          public void onError(CameraException e) {
            ToastUtils.showShort(e.toString());
          }
        })
        .build();
  }


  @Override
  protected void onResume() {
    super.onResume();
    if (Build.VERSION.SDK_INT >= 21) {
      // camera.start();
      //camera.setFocus(CameraKit.Constants.FOCUS_CONTINUOUS);


    }
  }

  @Override
  protected void onPause() {
    if (Build.VERSION.SDK_INT >= 21) {
      //  camera.stop();
    }
    super.onPause();
  }
  @TargetApi(LOLLIPOP)
  public void aboveApilevel21() {
    try {
      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);
      mScreenDensity = metrics.densityDpi;
      mMediaRecorder = new MediaRecorder();

      mProjectionManager = (MediaProjectionManager) getSystemService
          (Context.MEDIA_PROJECTION_SERVICE);
    }catch (Exception ex){}

  }
  public void startRecording(){
    if (ContextCompat.checkSelfPermission(RecordVideoActivity.this,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
        .checkSelfPermission(RecordVideoActivity.this,
            android.Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale
          (RecordVideoActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
          ActivityCompat.shouldShowRequestPermissionRationale
              (RecordVideoActivity.this, android.Manifest.permission.RECORD_AUDIO)) {
        // mToggleButton.setChecked(false);
        Snackbar.make(findViewById(android.R.id.content), R.string.label_permissions,
            Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                ActivityCompat.requestPermissions(RecordVideoActivity.this,
                    new String[]{android.Manifest.permission
                        .WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSIONS);
              }
            }).show();



      } else {
        ActivityCompat.requestPermissions(RecordVideoActivity.this,
            new String[]{android.Manifest.permission
                .WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO},
            REQUEST_PERMISSIONS);
      }
    } else {
      // onToggleScreenShare();
      startRecor();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode != REQUEST_CODE) {
      // Log.e(TAG, "Unknown request code: " + requestCode);
      return;
    }
    if (resultCode != RESULT_OK) {
      Toast.makeText(this,
          "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
      isTouch=true;
      finish();
//            Intent mainActivity=new Intent(Dashcam.this,MainActivity.class);
//            startActivity(mainActivity);

      // mToggleButton.setChecked(false);
      return;
    }
    if(resultCode==RESULT_OK && requestCode==REQUEST_CODE) {
      if (Build.VERSION.SDK_INT >= 21) {
        mMediaProjectionCallback = new MediaProjectionCallback();
        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
        //tv_record.setVisibility(View.VISIBLE);
      }
    }

  }

  public void startRecor(){
    imageButton.setVisibility(View.GONE);
    tv_record.setVisibility(View.VISIBLE);
    isRecordingStart=true;
    initRecorder();
    shareScreen();
  }

  @Override
  public void onBackPressed() {


    if(isRecordingStart) {
      isRecordingStart=false;
      stopRecordScreen();

    }

    int count = CommonPreference.getInstance().getDeviceCount();
    try {
      if (count == 1) {
        if (isSubscribed) {
          isSubscribed = false;
          mqttAndroidClient.unsubscribe(subscriptionTopic + "/" + imei);
          // mqttAndroidClient.disconnect();
        }
      }
    }catch (MqttException ex){

    }
    super.onBackPressed();
  }



  public void stopRecordScreen(){
    try {
      imageButton.setVisibility(View.VISIBLE);
      tv_record.setVisibility(View.GONE);
      mMediaRecorder.stop();
      mMediaRecorder.reset();
      Log.v(TAG, "Stopping Recording");
      stopScreenSharing();
      Toast.makeText(this, "Video stored"+folder.getAbsolutePath() + "/" + videoName + ".mp4", Toast.LENGTH_SHORT).show();
    }catch (Exception ex){

    }
  }
  @TargetApi(LOLLIPOP)
  private void shareScreen() {
    if (mMediaProjection == null) {
      if (Build.VERSION.SDK_INT >= 21) {
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        return;
      }
    }
    mVirtualDisplay = createVirtualDisplay();
    mMediaRecorder.start();
  }

  private VirtualDisplay createVirtualDisplay() {
    try{
      if (Build.VERSION.SDK_INT >= 21) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        return mMediaProjection.createVirtualDisplay("Dashcam",
            width, height, mScreenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mMediaRecorder.getSurface(), null /*Callbacks*/, null
            /*Handler*/);
      }

    }catch (Exception ex){}
    {
      return null;
    }
  }

  private void initRecorder() {
    if (Build.VERSION.SDK_INT >= 21) {
      try {
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(folder.getAbsolutePath() + "/" + videoName + ".mp4");
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mMediaRecorder.setVideoSize(2048, 1024);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncodingBitRate(6000000);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setVideoFrameRate(10);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int orientation = ORIENTATIONS.get(rotation + 90);
        mMediaRecorder.setOrientationHint(orientation);

        mMediaRecorder.prepare();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void setData(JSONObject jsonObject, String topic) {

    Log.e(TAG, "setData:Record "+jsonObject );
    try {
      final   int  speed_realtime = jsonObject.getInt("speed");

      final  int  rpm_realtime = jsonObject.getInt("rpm");
      new Handler(getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
          progressiveGauge_rpm.speedTo((float)rpm_realtime);
          progressiveGauge_Speed.speedTo((float)speed_realtime);
          if(rpm_realtime>maxRpm){
            maxRpm=rpm_realtime;
          }
          if(speed_realtime>maxSpeed){
            maxSpeed=speed_realtime;
          }
          tv_currentRpm.setText(String.valueOf(rpm_realtime));
          tv_currentSpeed.setText(String.valueOf(speed_realtime));
          tv_maxRpm.setText(String.valueOf(maxRpm));
          tv_maxSpeed.setText(String.valueOf(maxSpeed));
        }
      });





    } catch (JSONException e) {
//            Log.e("MainActivity", "broadcastMessage: "+e.getMessage() );
    }

  }


  @RequiresApi(api = LOLLIPOP)
  private class MediaProjectionCallback extends MediaProjection.Callback {
    @Override
    public void onStop() {
      if (imageButton.getVisibility()==View.GONE) {
        imageButton.setVisibility(View.VISIBLE);
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        Log.v(TAG, "Recording Stopped");
      }
      mMediaProjection = null;
      stopScreenSharing();
    }
  }
  @TargetApi(LOLLIPOP)
  private void stopScreenSharing() {
    if (mVirtualDisplay == null) {
      return;
    }
    if (Build.VERSION.SDK_INT >= 21) {
      mVirtualDisplay.release();
    }
    //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
    // be reused again
    destroyMediaProjection();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    destroyMediaProjection();
  }

  @Override
  public void indexScreen() {
    Analytics.index(RecordVideoActivity.this,"RecordVideoActivity");
  }

  private void destroyMediaProjection() {
    if (Build.VERSION.SDK_INT >= 21) {
      if (mMediaProjection != null) {
        mMediaProjection.unregisterCallback(mMediaProjectionCallback);
        mMediaProjection.stop();
        mMediaProjection = null;
      }
    }
    Log.i(TAG, "MediaProjection Stopped");
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      @NonNull String permissions[],
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_PERMISSIONS: {
        if ((grantResults.length > 0) && (grantResults[0] +
            grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
          //onToggleScreenShare(mToggleButton);
          startRecor();
        } else {
          // mToggleButton.setChecked(false);
          Snackbar.make(findViewById(android.R.id.content), "Enable ",
              Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent intent = new Intent();
                  intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                  intent.addCategory(Intent.CATEGORY_DEFAULT);
                  intent.setData(Uri.parse("package:" + getPackageName()));
                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                  intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                  startActivity(intent);
                }
              }).show();
        }

      }
    }
  }





  @Override
  public boolean onTouchEvent(MotionEvent event) {
    //Log.e(TAG, "onTouch: called"+event );
    if(isRecordingStart) {
      isRecordingStart=false;
      stopRecordScreen();
    }

    return true;}





  @Override
  protected void onStart() {
    super.onStart();
    //if (hasCameraPermission) {
    fotoapparatSwitcher.start();


  }

  @Override
  protected void onStop() {
    super.onStop();
    // if (hasCameraPermission) {
    fotoapparatSwitcher.stop();


    //}
  }




  public void broadcastMessage(String topic, MqttMessage message) {

    final byte[] payload = message.getPayload();
    final String data = new String(payload);

    try {
      final JSONObject jsonObject = new JSONObject(data);

//            Log.e(TAG, "broadcastMessage:Dashcam "+jsonObject );

      final   int  speed_realtime = jsonObject.getInt("speed");

      final  int  rpm_realtime = jsonObject.getInt("rpm");





      new Handler(getMainLooper()).post(new Runnable() {
        @Override
        public void run() {
          progressiveGauge_rpm.speedTo((float)rpm_realtime);
          progressiveGauge_Speed.speedTo((float)speed_realtime);
          if(rpm_realtime>maxRpm){
            maxRpm=rpm_realtime;
          }
          if(speed_realtime>maxSpeed){
            maxSpeed=speed_realtime;
          }

          tv_currentRpm.setText(String.valueOf(rpm_realtime));
          tv_currentSpeed.setText(String.valueOf(speed_realtime));
          tv_maxRpm.setText(String.valueOf(maxRpm));
          tv_maxSpeed.setText(String.valueOf(maxSpeed));


        }
      });





    } catch (JSONException e) {
//            Log.e("MainActivity", "broadcastMessage: "+e.getMessage() );
    }

  }



}
