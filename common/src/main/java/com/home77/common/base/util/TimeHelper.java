package com.home77.common.base.util;

import android.text.format.Time;

import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public final class TimeHelper {
  // Now

  public static Time now() {
    Time now = new Time();
    now.setToNow();
    return now;
  }

  public static Time hoursFromNow(int hours) {
    Time now = now();
    offsetHours(now, hours);
    return now;
  }

  public static Time minsFromNow(int mins) {
    Time now = now();
    offsetMins(now, mins);
    return now;
  }

  public static Time secondsFromNow(int seconds) {
    Time now = now();
    offsetSeconds(now, seconds);
    return now;
  }

  /**
   * Get time offset in seconds of (now-t)
   */
  public static int since(Time t) {
    return minus(now(), t);
  }

  // Creators

  public static Time date(int year, int month, int monthDay) {
    Time t = new Time();
    t.year = year;
    t.month = month;
    t.monthDay = monthDay;
    return t;
  }

  public static Time time(int hour, int minute, int second) {
    Time t = new Time();
    t.hour = hour;
    t.minute = minute;
    t.second = second;
    return t;
  }

  // Setters

  public static Time setDateOnly(Time t, int year, int month, int monthDay) {
    t.year = year;
    t.month = month;
    t.monthDay = monthDay;
    return t;
  }

  public static Time setTimeOnly(Time t, int hour, int minute, int second) {
    t.hour = hour;
    t.minute = minute;
    t.second = second;
    return t;
  }

  // Operations

  /**
   * Get time offset in seconds of (a-b)
   */
  public static int minus(Time a, Time b) {
    return (int) ((a.toMillis(true) - b.toMillis(true)) / TimeUnit.SECONDS.toMillis(1));
  }

  public static void offsetDays(Time t, int days) {
    t.monthDay += days;
    t.normalize(true);
  }

  public static void offsetHours(Time t, int hours) {
    t.hour += hours;
    t.normalize(true);
  }

  public static void offsetMins(Time t, int mins) {
    t.minute += mins;
    t.normalize(true);
  }

  public static void offsetSeconds(Time t, int seconds) {
    t.second += seconds;
    t.normalize(true);
  }

  public static int compareWithoutDate(Time a, Time b) {
    int hourOffset = a.hour - b.hour;
    if (hourOffset == 0) {
      int minOffset = a.minute - b.minute;
      if (minOffset == 0) {
        return a.second - b.second;
      } else {
        return minOffset;
      }

    } else {
      return hourOffset;
    }
  }

  private final static TimeZone GMT = TimeZone.getTimeZone("GMT");

  public static boolean isSameDayUTC(long previousTimestamp, long timestamp) {
    GregorianCalendar previous = new GregorianCalendar(GMT);
    previous.setTimeInMillis(previousTimestamp);

    GregorianCalendar current = new GregorianCalendar(GMT);
    current.setTimeInMillis(timestamp);

    return previous.get(GregorianCalendar.YEAR) == current.get(GregorianCalendar.YEAR) && //
           previous.get(GregorianCalendar.MONTH) == current.get(GregorianCalendar.MONTH) && //
           previous.get(GregorianCalendar.DAY_OF_MONTH) ==
           current.get(GregorianCalendar.DAY_OF_MONTH);
  }
}
