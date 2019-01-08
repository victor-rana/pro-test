package blackflame.com.zymepro.constant;

public final class RegexConstants {

  /**
   * Regex of simple mobile.
   */
  public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";


  public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";
  /**
   * Regex of telephone number.
   */
  public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
   /**
   * Regex of email.
   */
  public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
  /**
   * Regex of url.
   */
  public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
  /**
   * Regex of Chinese character.
   */
  public static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
   /**
   * Regex of date which pattern is "yyyy-MM-dd".
   */
  public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
  public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

  /**
   * Regex of double-byte characters.
   */
  public static final String REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]";
  /**
   * Regex of blank line.
   */
  public static final String REGEX_BLANK_LINE = "\\n\\s*\\r";
  /**
   * Regex of positive integer.
   */
  public static final String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";
  /**
   * Regex of negative integer.
   */
  public static final String REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$";
  /**
   * Regex of integer.
   */
  public static final String REGEX_INTEGER = "^-?[1-9]\\d*$";
  /**
   * Regex of non-negative integer.
   */
  public static final String REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$";
  /**
   * Regex of non-positive integer.
   */
  public static final String REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$";
  /**
   * Regex of positive float.
   */
  public static final String REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
  /**
   * Regex of negative float.
   */
  public static final String REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

}