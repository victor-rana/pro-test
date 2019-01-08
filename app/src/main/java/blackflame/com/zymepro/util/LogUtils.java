package blackflame.com.zymepro.util;

import android.util.Log;
import blackflame.com.zymepro.BuildConfig;

public class LogUtils {

  public static void debug(final String tag, String message) {
    if (BuildConfig.DEBUG) {
      Log.d(tag, message);
    }
  }
  public static void error(final String tag, String message) {
    if (BuildConfig.DEBUG) {
      Log.e(tag, message);
    }
  }

  public static void warning(final String tag, String message) {
    if (BuildConfig.DEBUG) {
      Log.w(tag, message);
    }
  }
  public static void info(final String tag, String message) {
    if (BuildConfig.DEBUG) {
      Log.i(tag, message);
    }
  }



}
