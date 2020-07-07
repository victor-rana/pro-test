package blackflame.com.zymepro.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import blackflame.com.zymepro.R;

public class MarkerUtil {
    public static BitmapDescriptor getEndCapIcon(Context context, int color) {

        // mipmap icon - white arrow, pointing up, with point at center of image
        // you will want to create:  mdpi=24x24, hdpi=36x36, xhdpi=48x48, xxhdpi=72x72, xxxhdpi=96x96
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.arrow_head);

        // set the bounds to the whole image (may not be necessary ...)
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        // overlay (multiply) your color over the white icon
//        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        // create a bitmap from the drawable
        android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // render the bitmap on a blank canvas
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);

        // create a BitmapDescriptor from the new bitmap
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
