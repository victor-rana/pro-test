package blackflame.com.zymepro.util;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;
import org.apache.http.util.TextUtils;

public class AndroidUtils  {

  private static final boolean YES = true;
  private static final boolean NO = false;

  private static final String EMAIL_PATTERN_1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
  private static final String EMAIL_PATTERN_2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";

  public static AndroidUtils androidUtils;



  public static boolean nonEmpty(String msg) {
    if (!TextUtils.isEmpty(msg)) {
      return YES;
    } else {
      return NO;
    }
  }

  public static boolean validateEmail(String email) {
    if (nonEmpty(email)) {
      String emailAsString = removeBlankSpace(email);
      return emailAsString.matches(EMAIL_PATTERN_1)
          || emailAsString.matches(EMAIL_PATTERN_2);

    } else {
      LogUtils.debug("Error", "edit text object is null");
      return NO;
    }
  }

  public static boolean matchMinLength(String text, int length) {
    if (nonEmpty(text)) {
      String content = removeBlankSpace(text);
      return content.length() >= length ? YES : NO;
    } else {
      LogUtils.debug("Error", "edit text object is null");
      return NO;
    }
  }



  public static boolean noSpecialCharacters(String text) {
    if (nonEmpty(text)) {
      String content = removeBlankSpace(text);
      return content.matches("[a-zA-Z0-9.? ]*");
    } else {
      LogUtils.debug("Error", "edit text object is null");
      return NO;
    }
  }


  public static boolean matchLength(String text, int length) {
    if (nonEmpty(text)) {
      String content = removeBlankSpace(text);
      return content.length() == length;
    } else {
      Log.d("SERI_PAR->Error", "edit text object is null");
      return NO;
    }
  }


  public static boolean mobileNumberValidation(String text) {
    if (nonEmpty(text)) {
      String mobileNumber = removeBlankSpace(text.trim());
      return Patterns.PHONE.matcher(mobileNumber).matches();
    } else {
     LogUtils.debug("Error", "edit text object is null");
      return NO;
    }
  }



  public static boolean mobileNumberValidation(String number, int minLength, int maxLength) {
    if (minLength > 0 && maxLength > 0 && nonEmpty(number)) {
      String mobileNumber = removeBlankSpace(number.trim());
      return mobileNumber.length() >= minLength && mobileNumber.length() <= maxLength;
    } else {
      return NO;
    }
  }



  public static void showToast(Context context, String msg) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
  }

  public static String removeBlankSpace(String value) {
    value = value.replace(" ", "");
    return value;
  }
}