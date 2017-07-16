package com.home77.common.ui.util;

import com.home77.common.base.pattern.Instance;
import com.home77.common.ui.model.UiData;
import com.home77.common.base.util.MathHelper;

public final class SizeHelper {

  // Converter

  static final float DP_UNIT = Instance.of(UiData.class).density();

  /**
   * Convert dp to int pixels
   */
  public static int dp(float dp) {
    return Math.round(dpf(dp));
  }

  /**
   * Convert dp to float pixels
   */
  public static float dpf(float dp) {
    return dp * DP_UNIT;
  }

  /**
   * Convert pixels to float dp
   */
  public static float pixelToDip(final float pixel) {
    return pixel / DP_UNIT;
  }

  // Calculator

  public static int of(float base, float ratio) {
    return of(base, ratio, true, Float.NEGATIVE_INFINITY, true, Float.POSITIVE_INFINITY);
  }

  public static int of(float base, float ratio, float minDp) {
    return of(base, ratio, true, minDp, true, Float.POSITIVE_INFINITY);
  }

  public static int of(float base, float ratio, float minDp, float maxDp) {
    return of(base, ratio, true, minDp, true, maxDp);
  }

  public static int of(float base, float ratio, boolean isMinDp, float minValue, float maxDp) {
    return of(base, ratio, isMinDp, minValue, true, maxDp);
  }

  public static int of(float base, float ratio, float minDp, boolean isMaxDp, float maxValue) {
    return of(base, ratio, true, minDp, isMaxDp, maxValue);
  }

  public static int of(float base,
                       float ratio,
                       boolean isMinDp,
                       float minValue,
                       boolean isMaxDp,
                       float maxValue) {
    if (isMinDp && minValue != Float.NEGATIVE_INFINITY) {
      minValue = dpf(minValue);
    }
    if (isMaxDp && maxValue != Float.POSITIVE_INFINITY) {
      maxValue = dpf(maxValue);
    }
    return Math.round(MathHelper.range(base * ratio, minValue, maxValue));
  }

  public static float off(float baseSize, float ratio) {
    return baseSize * ratio;
  }
}
