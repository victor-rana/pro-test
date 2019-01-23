package blackflame.com.zymepro.ui.document;

import static blackflame.com.zymepro.Prosingleton.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtil {
    private ImageUtil() {
}


    static File compressImage(File imageFile, int reqWidth, int reqHeight, Bitmap.CompressFormat compressFormat, int quality, String destinationPath) throws IOException {
      FileOutputStream fileOutputStream = null;
      File file = new File(destinationPath).getParentFile();
      if (!file.exists()) {
        file.mkdirs();
      }
      try {
        fileOutputStream = new FileOutputStream(destinationPath);

        decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight).compress(compressFormat, quality, fileOutputStream);
      } finally {
        if (fileOutputStream != null) {
          fileOutputStream.flush();
          fileOutputStream.close();
        }
      }

      return new File(destinationPath);
    }

    static Bitmap decodeSampledBitmapFromFile(File imageFile, int reqWidth, int reqHeight) throws IOException {
      // First decode with inJustDecodeBounds=true to check dimensions


      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);



      // Calculate inSampleSize


      options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);



      // Decode bitmap with inSampleSize set
      options.inJustDecodeBounds = false;

      Bitmap scaledBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

      //check the rotation of the image and display it properly


      ExifInterface exif;
      exif = new ExifInterface(imageFile.getAbsolutePath());
      int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
      Matrix matrix = new Matrix();
      if (orientation == 6) {
        matrix.postRotate(90);
      } else if (orientation == 3) {
        matrix.postRotate(180);
      } else if (orientation == 8) {
        matrix.postRotate(270);
      }
      scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
      return scaledBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
      // Raw height and width of image
      final int height = options.outHeight;
      final int width = options.outWidth;
      int inSampleSize = 1;

      if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both

        // height and width larger than the requested height and width.

        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
          inSampleSize *= 2;
        }
      }

      return inSampleSize;
    }

    public static String moveFile(File source,File destination) {
      String path=null;

      Log.e(TAG, "moveFile: "+source);
      if(source.renameTo(destination))
      {
        // if file copied successfully then delete the original file
        source.delete();
        path=destination.getAbsolutePath();
        System.out.println("File moved successfully");
        return path;
      } else
      {

        System.out.println("Failed to move the file");
      }
      return path;

    }


    public static String writeImage(Context ctx, byte[] imageData) {

      final String FILE_NAME = System.currentTimeMillis()+".jpeg";
      File dir = null;
      String filePath = null;
      OutputStream imageFileOS;

      dir = getStorageDirectory(ctx, "ZymePro");
      File f = new File(dir, FILE_NAME);

      try {
        imageFileOS = new FileOutputStream(f);
        imageFileOS.write(imageData);
        imageFileOS.flush();
        imageFileOS.close();

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }

      filePath = f.getAbsolutePath();

      return filePath;
    }

    public static File getStorageDirectory(Context ctx, String dirName) {

      if (TextUtils.isEmpty(dirName)) {
        dirName = "atemp";
      }

      File f = null;

      String state = Environment.getExternalStorageState();

      if (Environment.MEDIA_MOUNTED.equals(state)) {
        f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + dirName);
      } else {
        // media is removed, unmounted etc
        // Store image in
        // /data/data/<package-name>/cache/atemp/photograph.jpeg
        f = new File(ctx.getCacheDir() + "/" + dirName);
      }

      if (!f.exists()) {
        f.mkdirs();
      }

      return f;
    }



}