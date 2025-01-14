package blackflame.com.zymepro.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;
import androidx.core.content.FileProvider;
import android.util.Log;
import java.io.File;

public final class UriUtils {
  private UriUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * File to uri.
   *
   * @param file The file.
   * @return uri
   */
  public static Uri file2Uri(@NonNull final File file) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      String authority = Utils.getApp().getPackageName() + ".utilcode.provider";
      return FileProvider.getUriForFile(Utils.getApp(), authority, file);
    } else {
      return Uri.fromFile(file);
    }
  }

  /**
   * Uri to file.
   *
   * @param uri The uri.
   * @return file
   */
  public static File uri2File(@NonNull final Uri uri) {
    Log.d("UriUtils", uri.toString());
    String authority = uri.getAuthority();
    String scheme = uri.getScheme();
    if (ContentResolver.SCHEME_FILE.equals(scheme)) {
      String path = uri.getPath();
      if (path != null) return new File(path);
      Log.d("UriUtils", uri.toString() + " parse failed. -> 0");
      return null;
    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
      return getFileFromUri(uri, null, null);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        && DocumentsContract.isDocumentUri(Utils.getApp(), uri)) {
      if ("com.android.externalstorage.documents".equals(authority)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];
        if ("primary".equalsIgnoreCase(type)) {
          return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
        }
        Log.d("UriUtils", uri.toString() + " parse failed. -> 2");
        return null;
      } else if ("com.android.providers.downloads.documents".equals(authority)) {
        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"),
            Long.valueOf(id)
        );
        return getFileFromUri(contentUri, null, null);
      } else if ("com.android.providers.media.documents".equals(authority)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];
        Uri contentUri;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        } else {
          Log.d("UriUtils", uri.toString() + " parse failed. -> 3");
          return null;
        }
        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{split[1]};
        return getFileFromUri(contentUri, selection, selectionArgs);
      } else {
        Log.d("UriUtils", uri.toString() + " parse failed. -> 4");
        return null;
      }
    } else {
      Log.d("UriUtils", uri.toString() + " parse failed. -> 5");
      return null;
    }
  }

  private static File getFileFromUri(@NonNull final Uri uri) {
    return getFileFromUri(uri, null, null);
  }

  private static File getFileFromUri(@NonNull final Uri uri,
      final String selection,
      final String[] selectionArgs) {
    CursorLoader cl = new CursorLoader(Utils.getApp());
    cl.setUri(uri);
    cl.setProjection(new String[]{"_data"});
    Cursor cursor = null;
    try {
      cursor = cl.loadInBackground();
      int columnIndex = cursor.getColumnIndexOrThrow("_data");
      cursor.moveToFirst();
      return new File(cursor.getString(columnIndex));
    } catch (Exception e) {
      Log.d("UriUtils", uri.toString() + " parse failed. -> 1");
      return null;
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }
}