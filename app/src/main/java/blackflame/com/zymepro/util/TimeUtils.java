package blackflame.com.zymepro.util;

import androidx.annotation.NonNull;
import blackflame.com.zymepro.constant.TimeConstants;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class TimeUtils {
  private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

  private static SimpleDateFormat getDefaultFormat() {
    SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
    if (simpleDateFormat == null) {
      simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
      SDF_THREAD_LOCAL.set(simpleDateFormat);
    }
    return simpleDateFormat;
  }

  private TimeUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * Milliseconds to the formatted time string.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param millis The milliseconds.
   * @return the formatted time string
   */
  public static String millis2String(final long millis) {
    return millis2String(millis, getDefaultFormat());
  }

  /**
   * Milliseconds to the formatted time string.
   *
   * @param millis The milliseconds.
   * @param format The format.
   * @return the formatted time string
   */
  public static String millis2String(final long millis, @NonNull final DateFormat format) {
    return format.format(new Date(millis));
  }

  /**
   * Formatted time string to the milliseconds.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time The formatted time string.
   * @return the milliseconds
   */
  public static long string2Millis(final String time) {
    return string2Millis(time, getDefaultFormat());
  }

  /**
   * Formatted time string to the milliseconds.
   *
   * @param time   The formatted time string.
   * @param format The format.
   * @return the milliseconds
   */
  public static long string2Millis(final String time, @NonNull final DateFormat format) {
    try {
      return format.parse(time).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return -1;
  }

  /**
   * Formatted time string to the date.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time The formatted time string.
   * @return the date
   */
  public static Date string2Date(final String time) {
    return string2Date(time, getDefaultFormat());
  }

  /**
   * Formatted time string to the date.
   *
   * @param time   The formatted time string.
   * @param format The format.
   * @return the date
   */
  public static Date string2Date(final String time, @NonNull final DateFormat format) {
    try {
      return format.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Date to the formatted time string.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param date The date.
   * @return the formatted time string
   */
  public static String date2String(final Date date) {
    return date2String(date, getDefaultFormat());
  }

  /**
   * Date to the formatted time string.
   *
   * @param date   The date.
   * @param format The format.
   * @return the formatted time string
   */
  public static String date2String(final Date date, @NonNull final DateFormat format) {
    return format.format(date);
  }

  /**
   * Date to the milliseconds.
   *
   * @param date The date.
   * @return the milliseconds
   */
  public static long date2Millis(final Date date) {
    return date.getTime();
  }

  /**
   * Milliseconds to the date.
   *
   * @param millis The milliseconds.
   * @return the date
   */
  public static Date millis2Date(final long millis) {
    return new Date(millis);
  }

  /**
   * Return the time span, in unit.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time1 The first formatted time string.
   * @param time2 The second formatted time string.
   * @param unit  The unit of time span.
   *              <ul>
   *              <li>{@link TimeConstants#MSEC}</li>
   *              <li>{@link TimeConstants#SEC }</li>
   *              <li>{@link TimeConstants#MIN }</li>
   *              <li>{@link TimeConstants#HOUR}</li>
   *              <li>{@link TimeConstants#DAY }</li>
   *              </ul>
   * @return the time span, in unit
   */
  public static long getTimeSpan(final String time1,
      final String time2,
      @TimeConstants.Unit final int unit) {
    return getTimeSpan(time1, time2, getDefaultFormat(), unit);
  }

  /**
   * Return the time span, in unit.
   *
   * @param time1  The first formatted time string.
   * @param time2  The second formatted time string.
   * @param format The format.
   * @param unit   The unit of time span.
   *               <ul>
   *               <li>{@link TimeConstants#MSEC}</li>
   *               <li>{@link TimeConstants#SEC }</li>
   *               <li>{@link TimeConstants#MIN }</li>
   *               <li>{@link TimeConstants#HOUR}</li>
   *               <li>{@link TimeConstants#DAY }</li>
   *               </ul>
   * @return the time span, in unit
   */
  public static long getTimeSpan(final String time1,
      final String time2,
      @NonNull final DateFormat format,
      @TimeConstants.Unit final int unit) {
    return millis2TimeSpan(string2Millis(time1, format) - string2Millis(time2, format), unit);
  }

  /**
   * Return the time span, in unit.
   *
   * @param date1 The first date.
   * @param date2 The second date.
   * @param unit  The unit of time span.
   *              <ul>
   *              <li>{@link TimeConstants#MSEC}</li>
   *              <li>{@link TimeConstants#SEC }</li>
   *              <li>{@link TimeConstants#MIN }</li>
   *              <li>{@link TimeConstants#HOUR}</li>
   *              <li>{@link TimeConstants#DAY }</li>
   *              </ul>
   * @return the time span, in unit
   */
  public static long getTimeSpan(final Date date1,
      final Date date2,
      @TimeConstants.Unit final int unit) {
    return millis2TimeSpan(date2Millis(date1) - date2Millis(date2), unit);
  }

  /**
   * Return the time span, in unit.
   *
   * @param millis1 The first milliseconds.
   * @param millis2 The second milliseconds.
   * @param unit    The unit of time span.
   *                <ul>
   *                <li>{@link TimeConstants#MSEC}</li>
   *                <li>{@link TimeConstants#SEC }</li>
   *                <li>{@link TimeConstants#MIN }</li>
   *                <li>{@link TimeConstants#HOUR}</li>
   *                <li>{@link TimeConstants#DAY }</li>
   *                </ul>
   * @return the time span, in unit
   */
  public static long getTimeSpan(final long millis1,
      final long millis2,
      @TimeConstants.Unit final int unit) {
    return millis2TimeSpan(millis1 - millis2, unit);
  }

  /**
   * Return the fit time span.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time1     The first formatted time string.
   * @param time2     The second formatted time string.
   * @param precision The precision of time span.
   *
   * @return the fit time span
   */
  public static String getFitTimeSpan(final String time1,
      final String time2,
      final int precision) {
    long delta = string2Millis(time1, getDefaultFormat()) - string2Millis(time2, getDefaultFormat());
    return millis2FitTimeSpan(delta, precision);
  }

  /**
   * Return the fit time span.
   *
   * @param time1     The first formatted time string.
   * @param time2     The second formatted time string.
   * @param format    The format.
   * @param precision The precision of time span.
   *
   * @return the fit time span
   */
  public static String getFitTimeSpan(final String time1,
      final String time2,
      @NonNull final DateFormat format,
      final int precision) {
    long delta = string2Millis(time1, format) - string2Millis(time2, format);
    return millis2FitTimeSpan(delta, precision);
  }

  /**
   * Return the fit time span.
   *
   * @param date1     The first date.
   * @param date2     The second date.
   * @param precision The precision of time span.
   *
   * @return the fit time span
   */
  public static String getFitTimeSpan(final Date date1, final Date date2, final int precision) {
    return millis2FitTimeSpan(date2Millis(date1) - date2Millis(date2), precision);
  }

  /**
   * Return the fit time span.
   *
   * @param millis1   The first milliseconds.
   * @param millis2   The second milliseconds.
   * @param precision The precision of time span.
   *
   * @return the fit time span
   */
  public static String getFitTimeSpan(final long millis1,
      final long millis2,
      final int precision) {
    return millis2FitTimeSpan(millis1 - millis2, precision);
  }

  /**
   * Return the current time in milliseconds.
   *
   * @return the current time in milliseconds
   */
  public static long getNowMills() {
    return System.currentTimeMillis();
  }

  /**
   * Return the current formatted time string.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @return the current formatted time string
   */
  public static String getNowString() {
    return millis2String(System.currentTimeMillis(), getDefaultFormat());
  }

  /**
   * Return the current formatted time string.
   *
   * @param format The format.
   * @return the current formatted time string
   */
  public static String getNowString(@NonNull final DateFormat format) {
    return millis2String(System.currentTimeMillis(), format);
  }

  /**
   * Return the current date.
   *
   * @return the current date
   */
  public static Date getNowDate() {
    return new Date();
  }

  /**
   * Return the time span by now, in unit.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time The formatted time string.
   * @param unit The unit of time span.
   *             <ul>
   *             <li>{@link TimeConstants#MSEC}</li>
   *             <li>{@link TimeConstants#SEC }</li>
   *             <li>{@link TimeConstants#MIN }</li>
   *             <li>{@link TimeConstants#HOUR}</li>
   *             <li>{@link TimeConstants#DAY }</li>
   *             </ul>
   * @return the time span by now, in unit
   */
  public static long getTimeSpanByNow(final String time, @TimeConstants.Unit final int unit) {
    return getTimeSpan(time, getNowString(), getDefaultFormat(), unit);
  }

  /**
   * Return the time span by now, in unit.
   *
   * @param time   The formatted time string.
   * @param format The format.
   * @param unit   The unit of time span.
   *               <ul>
   *               <li>{@link TimeConstants#MSEC}</li>
   *               <li>{@link TimeConstants#SEC }</li>
   *               <li>{@link TimeConstants#MIN }</li>
   *               <li>{@link TimeConstants#HOUR}</li>
   *               <li>{@link TimeConstants#DAY }</li>
   *               </ul>
   * @return the time span by now, in unit
   */
  public static long getTimeSpanByNow(final String time,
      @NonNull final DateFormat format,
      @TimeConstants.Unit final int unit) {
    return getTimeSpan(time, getNowString(format), format, unit);
  }

  /**
   * Return the time span by now, in unit.
   *
   * @param date The date.
   * @param unit The unit of time span.
   *             <ul>
   *             <li>{@link TimeConstants#MSEC}</li>
   *             <li>{@link TimeConstants#SEC }</li>
   *             <li>{@link TimeConstants#MIN }</li>
   *             <li>{@link TimeConstants#HOUR}</li>
   *             <li>{@link TimeConstants#DAY }</li>
   *             </ul>
   * @return the time span by now, in unit
   */
  public static long getTimeSpanByNow(final Date date, @TimeConstants.Unit final int unit) {
    return getTimeSpan(date, new Date(), unit);
  }

  /**
   * Return the time span by now, in unit.
   *
   * @param millis The milliseconds.
   * @param unit   The unit of time span.
   *               <ul>
   *               <li>{@link TimeConstants#MSEC}</li>
   *               <li>{@link TimeConstants#SEC }</li>
   *               <li>{@link TimeConstants#MIN }</li>
   *               <li>{@link TimeConstants#HOUR}</li>
   *               <li>{@link TimeConstants#DAY }</li>
   *               </ul>
   * @return the time span by now, in unit
   */
  public static long getTimeSpanByNow(final long millis, @TimeConstants.Unit final int unit) {
    return getTimeSpan(millis, System.currentTimeMillis(), unit);
  }

  /**
   * Return the fit time span by now.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time      The formatted time string.
   * @param precision The precision of time span.
   *
   * @return the fit time span by now
   */
  public static String getFitTimeSpanByNow(final String time, final int precision) {
    return getFitTimeSpan(time, getNowString(), getDefaultFormat(), precision);
  }

  /**
   * Return the fit time span by now.
   *
   * @param time      The formatted time string.
   * @param format    The format.
   * @param precision The precision of time span.
   *
   * @return the fit time span by now
   */
  public static String getFitTimeSpanByNow(final String time,
      @NonNull final DateFormat format,
      final int precision) {
    return getFitTimeSpan(time, getNowString(format), format, precision);
  }

  /**
   * Return the fit time span by now.
   *
   * @param date      The date.
   * @param precision The precision of time span.
   *
   * @return the fit time span by now
   */
  public static String getFitTimeSpanByNow(final Date date, final int precision) {
    return getFitTimeSpan(date, getNowDate(), precision);
  }

  /**
   * Return the fit time span by now.
   *
   * @param millis    The milliseconds.
   * @param precision The precision of time span.
   *
   * @return the fit time span by now
   */
  public static String getFitTimeSpanByNow(final long millis, final int precision) {
    return getFitTimeSpan(millis, System.currentTimeMillis(), precision);
  }

  /**
   * Return the friendly time span by now.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time The formatted time string.
   * @return the friendly time span by now

   */
  public static String getFriendlyTimeSpanByNow(final String time) {
    return getFriendlyTimeSpanByNow(time, getDefaultFormat());
  }

  /**
   * Return the friendly time span by now.
   *
   * @param time   The formatted time string.
   * @param format The format.
   * @return the friendly time span by now

   */
  public static String getFriendlyTimeSpanByNow(final String time,
      @NonNull final DateFormat format) {
    return getFriendlyTimeSpanByNow(string2Millis(time, format));
  }

  /**
   * Return the friendly time span by now.
   *
   * @param date The date.
   * @return the friendly time span by now

   */
  public static String getFriendlyTimeSpanByNow(final Date date) {
    return getFriendlyTimeSpanByNow(date.getTime());
  }

  /**
   * Return the friendly time span by now.
   *
   * @param millis The milliseconds.
   * @return the friendly time span by now

   */
  public static String getFriendlyTimeSpanByNow(final long millis) {
    long now = System.currentTimeMillis();
    long span = now - millis;
    if (span < 0)
      // U can read http://www.apihome.cn/api/java/Formatter.html to understand it.
      return String.format("%tc", millis);
    if (span < 1000) {
      return "just";
    } else if (span < TimeConstants.MIN) {
      return String.format(Locale.getDefault(), "%d seconds ago", span / TimeConstants.SEC);
    } else if (span < TimeConstants.HOUR) {
      return String.format(Locale.getDefault(), "%d minutes ago", span / TimeConstants.MIN);
    }
    // 获取当天 00:00
    long wee = getWeeOfToday();
    if (millis >= wee) {
      return String.format("今天%tR", millis);
    } else if (millis >= wee - TimeConstants.DAY) {
      return String.format("昨天%tR", millis);
    } else {
      return String.format("%tF", millis);
    }
  }

  private static long getWeeOfToday() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTimeInMillis();
  }

  /**
   * Return the milliseconds differ time span.
   *
   * @param millis   The milliseconds.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the milliseconds differ time span
   */
  public static long getMillis(final long millis,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return millis + timeSpan2Millis(timeSpan, unit);
  }

  /**
   * Return the milliseconds differ time span.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time     The formatted time string.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the milliseconds differ time span
   */
  public static long getMillis(final String time,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return getMillis(time, getDefaultFormat(), timeSpan, unit);
  }

  /**
   * Return the milliseconds differ time span.
   *
   * @param time     The formatted time string.
   * @param format   The format.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the milliseconds differ time span.
   */
  public static long getMillis(final String time,
      @NonNull final DateFormat format,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return string2Millis(time, format) + timeSpan2Millis(timeSpan, unit);
  }

  /**
   * Return the milliseconds differ time span.
   *
   * @param date     The date.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the milliseconds differ time span.
   */
  public static long getMillis(final Date date,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return date2Millis(date) + timeSpan2Millis(timeSpan, unit);
  }

  /**
   * Return the formatted time string differ time span.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param millis   The milliseconds.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the formatted time string differ time span
   */
  public static String getString(final long millis,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return getString(millis, getDefaultFormat(), timeSpan, unit);
  }

  /**
   * Return the formatted time string differ time span.
   *
   * @param millis   The milliseconds.
   * @param format   The format.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the formatted time string differ time span
   */
  public static String getString(final long millis,
      @NonNull final DateFormat format,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return millis2String(millis + timeSpan2Millis(timeSpan, unit), format);
  }

  /**
   * Return the formatted time string differ time span.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time     The formatted time string.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the formatted time string differ time span
   */
  public static String getString(final String time,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return getString(time, getDefaultFormat(), timeSpan, unit);
  }

  /**
   * Return the formatted time string differ time span.
   *
   * @param time     The formatted time string.
   * @param format   The format.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the formatted time string differ time span
   */
  public static String getString(final String time,
      @NonNull final DateFormat format,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return millis2String(string2Millis(time, format) + timeSpan2Millis(timeSpan, unit), format);
  }

  /**
   * Return the formatted time string differ time span.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param date     The date.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the formatted time string differ time span
   */
  public static String getString(final Date date,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return getString(date, getDefaultFormat(), timeSpan, unit);
  }

  /**
   * Return the formatted time string differ time span.
   *
   * @param date     The date.
   * @param format   The format.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the formatted time string differ time span
   */
  public static String getString(final Date date,
      @NonNull final DateFormat format,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return millis2String(date2Millis(date) + timeSpan2Millis(timeSpan, unit), format);
  }

  /**
   * Return the date differ time span.
   *
   * @param millis   The milliseconds.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the date differ time span
   */
  public static Date getDate(final long millis,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return millis2Date(millis + timeSpan2Millis(timeSpan, unit));
  }

  /**
   * Return the date differ time span.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time     The formatted time string.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the date differ time span
   */
  public static Date getDate(final String time,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return getDate(time, getDefaultFormat(), timeSpan, unit);
  }

  /**
   * Return the date differ time span.
   *
   * @param time     The formatted time string.
   * @param format   The format.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the date differ time span
   */
  public static Date getDate(final String time,
      @NonNull final DateFormat format,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return millis2Date(string2Millis(time, format) + timeSpan2Millis(timeSpan, unit));
  }

  /**
   * Return the date differ time span.
   *
   * @param date     The date.
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the date differ time span
   */
  public static Date getDate(final Date date,
      final long timeSpan,
      @TimeConstants.Unit final int unit) {
    return millis2Date(date2Millis(date) + timeSpan2Millis(timeSpan, unit));
  }

  /**
   * Return the milliseconds differ time span by now.
   *
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the milliseconds differ time span by now
   */
  public static long getMillisByNow(final long timeSpan, @TimeConstants.Unit final int unit) {
    return getMillis(getNowMills(), timeSpan, unit);
  }

  /**
   * Return the formatted time string differ time span by now.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the formatted time string differ time span by now
   */
  public static String getStringByNow(final long timeSpan, @TimeConstants.Unit final int unit) {
    return getStringByNow(timeSpan, getDefaultFormat(), unit);
  }

  /**
   * Return the formatted time string differ time span by now.
   *
   * @param timeSpan The time span.
   * @param format   The format.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the formatted time string differ time span by now
   */
  public static String getStringByNow(final long timeSpan,
      @NonNull final DateFormat format,
      @TimeConstants.Unit final int unit) {
    return getString(getNowMills(), format, timeSpan, unit);
  }

  /**
   * Return the date differ time span by now.
   *
   * @param timeSpan The time span.
   * @param unit     The unit of time span.
   *                 <ul>
   *                 <li>{@link TimeConstants#MSEC}</li>
   *                 <li>{@link TimeConstants#SEC }</li>
   *                 <li>{@link TimeConstants#MIN }</li>
   *                 <li>{@link TimeConstants#HOUR}</li>
   *                 <li>{@link TimeConstants#DAY }</li>
   *                 </ul>
   * @return the date differ time span by now
   */
  public static Date getDateByNow(final long timeSpan, @TimeConstants.Unit final int unit) {
    return getDate(getNowMills(), timeSpan, unit);
  }

  /**
   * Return whether it is today.
   * <p>The pattern is {@code yyyy-MM-dd HH:mm:ss}.</p>
   *
   * @param time The formatted time string.
   * @return {@code true}: yes<br>{@code false}: no
   */
  public static boolean isToday(final String time) {
    return isToday(string2Millis(time, getDefaultFormat()));
  }

  /**
   * Return whether it is today.
   *
   * @param time   The formatted time string.
   * @param format The format.
   * @return {@code true}: yes<br>{@code false}: no
   */
  public static boolean isToday(final String time, @NonNull final DateFormat format) {
    return isToday(string2Millis(time, format));
  }

  /**
   * Return whether it is today.
   *
   * @param date The date.
   * @return {@code true}: yes<br>{@code false}: no
   */
  public static boolean isToday(final Date date) {
    return isToday(date.getTime());
  }

  /**
   * Return whether it is today.
   *
   * @param millis The milliseconds.
   * @return {@code true}: yes<br>{@code false}: no
   */
  public static boolean isToday(final long millis) {
    long wee = getWeeOfToday();
    return millis >= wee && millis < wee + TimeConstants.DAY;
  }






  private static long timeSpan2Millis(final long timeSpan, @TimeConstants.Unit final int unit) {
    return timeSpan * unit;
  }

  private static long millis2TimeSpan(final long millis, @TimeConstants.Unit final int unit) {
    return millis / unit;
  }

  private static String millis2FitTimeSpan(long millis, int precision) {
    if (precision <= 0) return null;
    precision = Math.min(precision, 5);
    String[] units = {"day", "hour", "minute", "second", "millisecond"};
    if (millis == 0) return 0 + units[precision - 1];
    StringBuilder sb = new StringBuilder();
    if (millis < 0) {
      sb.append("-");
      millis = -millis;
    }
    int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
    for (int i = 0; i < precision; i++) {
      if (millis >= unitLen[i]) {
        long mode = millis / unitLen[i];
        millis -= mode * unitLen[i];
        sb.append(mode).append(units[i]);
      }
    }
    return sb.toString();
  }




  public static String getFormattedDate(String ourDate)
  {
    try
    {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      Date value = formatter.parse(ourDate);

      SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm"); //this format changeable
      dateFormatter.setTimeZone(TimeZone.getDefault());
      ourDate = dateFormatter.format(value);

      //Log.d("ourDate", ourDate);
    }
    catch (Exception e)
    {
      ourDate = "00-00-0000 00:00";
    }
    return ourDate;
  }

}