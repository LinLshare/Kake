package com.home77.common.base.util;

import android.os.Build;
import android.util.SparseBooleanArray;

import java.util.regex.Pattern;

public final class CompatHelper {
  // SDK

  /**
   * sdk >= |min|
   *
   * @param min
   *     min sdk level
   *
   * @return true is matched
   */
  public static boolean sdk(int min) {
    return Build.VERSION.SDK_INT >= min;
  }

  /**
   * |min| <= sdk <= |max|
   *
   * @param min
   *     min sdk level
   * @param max
   *     max sdk level
   *
   * @return true is matched
   */
  public static boolean sdk(int min, int max) {
    return MathHelper.inRange(Build.VERSION.SDK_INT, min, max);
  }

  public static boolean sdks(int... targets) {
    final int sdk = Build.VERSION.SDK_INT;
    for (int target : targets) {
      if (sdk == target) {
        return true;
      }
    }
    return false;
  }

  // Device Name

  private static SparseBooleanArray sDeviceResultCache;

  public static boolean device(String regex) {
    if (sDeviceResultCache == null) {
      sDeviceResultCache = new SparseBooleanArray();
    }

    int hashCode = regex.hashCode();
    int index = sDeviceResultCache.indexOfKey(hashCode);
    boolean result;
    if (index < 0) {
      result = Pattern.matches(regex, SystemInfoHelper.device());
      sDeviceResultCache.put(hashCode, result);
    } else {
      result = sDeviceResultCache.valueAt(index);
    }
    return result;
  }

  // CPU Arch

  private static SparseBooleanArray sCPUResultCache;

  public static boolean cpu(String regex) {
    if (sCPUResultCache == null) {
      sCPUResultCache = new SparseBooleanArray();
    }

    int hashCode = regex.hashCode();
    int index = sCPUResultCache.indexOfKey(hashCode);
    boolean result;
    if (index < 0) {
      result = Pattern.matches(regex, SystemInfoHelper.cpuArch());
      sCPUResultCache.put(hashCode, result);
    } else {
      result = sCPUResultCache.valueAt(index);
    }
    return result;
  }

  // Ram Size

  public static boolean ram(int min, int max) {
    return MathHelper.inRange(SystemInfoHelper.ramSize(), min, max);
  }
}
