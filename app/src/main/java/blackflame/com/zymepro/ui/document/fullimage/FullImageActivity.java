package blackflame.com.zymepro.ui.document.fullimage;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import blackflame.com.zymepro.R;
import blackflame.com.zymepro.view.custom.photoview.PhotoView;

public class FullImageActivity extends AppCompatActivity {
  RelativeLayout activityFullimage;
  PhotoView photoView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_full_image);
    photoView= findViewById(R.id.photo_view);
    // get intent data
    Intent i = getIntent();
    String path = i.getStringExtra("path");
    photoView.setImageURI(Uri.parse(path));
  }
}
