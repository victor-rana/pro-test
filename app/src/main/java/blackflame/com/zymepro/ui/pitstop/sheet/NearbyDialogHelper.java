package blackflame.com.zymepro.ui.pitstop.sheet;

import static blackflame.com.zymepro.Prosingleton.TAG;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import blackflame.com.zymepro.BuildConfig;
import blackflame.com.zymepro.R;
import com.bumptech.glide.Glide;

public class NearbyDialogHelper {
  public static void  openDialog(final Activity activity, final Bundle bundle){
    try {
      String key=BuildConfig.PLACES_KEY;
      Log.e("Log", "openDialog: call");
      View view = activity.getLayoutInflater().inflate(R.layout.layout_bottom_nearby, null);

      final BottomSheetDialog dialog = new BottomSheetDialog(activity);
      dialog.setContentView(view);
      TextView name = view.findViewById(R.id.content_name);
      TextView address = view.findViewById(R.id.content_address);
      TextView rating = view.findViewById(R.id.content_rating);
      TextView status = view.findViewById(R.id.content_status);
      ImageView image = view.findViewById(R.id.img_nearby_type);
      ImageView close = view.findViewById(R.id.btn_hide_sheet);
      close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          dialog.cancel();
        }
      });
      ImageView direction = view.findViewById(R.id.btn_route_map);
      direction.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          try {
            String url = "http://maps.google.com/maps?f=d&daddr=" + bundle.getDouble("lat") + "," + bundle.getDouble("lng") + "&dirflg=d";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            activity.startActivity(intent);
          } catch (ActivityNotFoundException ex) {

          }
        }
      });
      Log.e(TAG, "openDialog: " + bundle.getString("name"));

      name.setText(bundle.getString("name"));
      address.setText(bundle.getString("address"));
      rating.setText(bundle.getString("rating") + "/5");
      if (bundle.getBoolean("isopen")) {
        status.setText("Open now");
      } else {
        status.setText("Closed now");

      }
      String image_path = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + bundle.getString("image") + "&sensor=false&key="+key;
      Glide.with(activity)
          .load(image_path)
          .placeholder(R.drawable.placeholder)
          .error(R.drawable.placeholder)
          .centerCrop()
          .into(image);

      dialog.show();
    }catch (Exception ex){

    }


  }
}
