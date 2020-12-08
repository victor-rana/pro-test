package blackflame.com.zymepro.view.custom.photoview;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.io.File;

public class Util {
  static void checkZoomLevels(float minZoom, float midZoom,
      float maxZoom) {
    if (minZoom >= midZoom) {
      throw new IllegalArgumentException(
          "Minimum zoom has to be less than Medium zoom. Call setMinimumZoom() with a more appropriate value");
    } else if (midZoom >= maxZoom) {
      throw new IllegalArgumentException(
          "Medium zoom has to be less than Maximum zoom. Call setMaximumZoom() with a more appropriate value");
    }
  }

  static boolean hasDrawable(ImageView imageView) {
    return imageView.getDrawable() != null;
  }

  static boolean isSupportedScaleType(final ImageView.ScaleType scaleType) {
    if (scaleType == null) {
      return false;
    }
    switch (scaleType) {
      case MATRIX:
        throw new IllegalStateException("Matrix scale type is not supported");
    }
    return true;
  }

  static int getPointerIndex(int action) {
    return (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
  }



  public static void deleteCache(Context context) {
    try {
      File dir = context.getCacheDir();
      deleteDir(dir);
    } catch (Exception e) { e.printStackTrace();}
  }

  public static boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
      return dir.delete();
    } else if(dir!= null && dir.isFile()) {
      return dir.delete();
    } else {
      return false;
    }
  }




  public static void clearApplicationData(Context context) {
    File cacheDirectory = context.getCacheDir();
    File applicationDirectory = new File(cacheDirectory.getParent());
    if (applicationDirectory.exists()) {
      String[] fileNames = applicationDirectory.list();
      for (String fileName : fileNames) {
        if (!fileName.equals("lib")) {
          deleteFile(new File(applicationDirectory, fileName));
        }
      }
    }
  }

  public static boolean deleteFile(File file) {
    boolean deletedAll = true;
    if (file != null) {
      if (file.isDirectory()) {
        String[] children = file.list();
        for (int i = 0; i < children.length; i++) {
          deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
        }
      } else {
        deletedAll = file.delete();
      }
    }

    return deletedAll;
  }
}
