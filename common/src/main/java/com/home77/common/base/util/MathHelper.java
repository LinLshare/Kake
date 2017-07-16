package com.home77.common.base.util;

import android.annotation.TargetApi;
import android.os.Build;

public final class MathHelper {
  // Float Constants

  public static final float PI = 3.1415927f;

  // Float Arithmetic

  public static float sqr(float a) {
    return a * a;
  }

  public static float sumSqr(float a, float b) {
    return a * a + b * b;
  }

  public static float diag(float a, float b) {
    return (float) Math.sqrt(a * a + b * b);
  }

  public static float exp(float value) {
    return (float) Math.exp(value);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public static float pow(float x, float y) {
    return (float) Math.pow(x, y);
  }

  public static float log(float a, float newBase) {
    if (Float.isNaN(a)) {
      return a; // IEEE 754-2008: NaN payload must be preserved
    }
    if (Float.isNaN(newBase)) {
      return newBase; // IEEE 754-2008: NaN payload must be preserved
    }

    if (newBase == 1f) {
      return Float.NaN;
    }
    if (a != 1 && (newBase == 0 || newBase == Float.POSITIVE_INFINITY)) {
      return Float.NaN;
    }

    return (float) (Math.log(a) / Math.log(newBase));
  }

  public static int roundFloatToInt(float a) {
    return (Math.round(a * 10)) / 10;
  }

  // Range

  /**
   * Shrink the value to [min,max]
   */
  public static int range(int value, int min, int max) {
    return Math.min(Math.max(value, min), max);
  }

  /**
   * Shrink the value to [min,max]
   */
  public static float range(float value, float min, float max) {
    return Math.min(Math.max(value, min), max);
  }

  /**
   * The value will be cycled in [min, max)
   */
  public static float cycle(float value, float min, float max) {
    if (value < min) {
      value = max - (min - value) % (max - min);
    }
    if (value >= max) {
      return min + (value - max) % (max - min);
    }
    return value;
  }

  /**
   * check value whether in [min,max]
   */
  public static boolean inRange(int value, int min, int max) {
    return value >= min && value <= max;
  }

  public static boolean inRange(float value, float min, float max) {
    return value >= min && value <= max;
  }

  public static boolean isIntersected(int start1, int end1, int start2, int end2) {
    return start1 <= end2 && end1 >= start2;
  }

  public static boolean isIntersected(float start1, float end1, float start2, float end2) {
    return start1 <= end2 && end1 >= start2;
  }

  // Angle

  public static float radius(float degree) {
    return (degree / 180f) * PI;
  }

  public static float degree(float radius) {
    return (radius / PI) * 180f;
  }

  public static long ceil(long input, double step) {
    return (long) (step * Math.ceil(input / step));
  }

  public static int ceil(int input, double step) {
    return (int) (step * Math.ceil(input / step));
  }

  // Reverse number

  public static short reverseBytesShort(short s) {
    int i = s & 0xffff;
    int reversed = (i & 0xff00) >>> 8 | (i & 0x00ff) << 8;
    return (short) reversed;
  }

  public static int reverseBytesInt(int i) {
    return (i & 0xff000000) >>> 24 | (i & 0x00ff0000) >>> 8 | (i & 0x0000ff00) << 8 |
           (i & 0x000000ff) << 24;
  }

  public static long reverseBytesLong(long v) {
    return (v & 0xff00000000000000L) >>> 56 | (v & 0x00ff000000000000L) >>> 40 |
           (v & 0x0000ff0000000000L) >>> 24 | (v & 0x000000ff00000000L) >>> 8 |
           (v & 0x00000000ff000000L) << 8 | (v & 0x0000000000ff0000L) << 24 |
           (v & 0x000000000000ff00L) << 40 | (v & 0x00000000000000ffL) << 56;
  }
}
