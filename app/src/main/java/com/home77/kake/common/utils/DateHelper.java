package com.home77.kake.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author CJ
 */
public class DateHelper {

  private static final SimpleDateFormat defaultDateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

  // like 2017-07-08 15:29:10
  public static long toMillis(String dateStr) {
    try {
      return defaultDateFormat.parse(dateStr).getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return -1;
  }
}
