package com.home77.common.ui.model;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.home77.common.base.component.ContextManager;
import com.home77.common.base.data.Data;
import com.home77.common.base.debug.Check;
import com.home77.common.base.util.CompatHelper;

public class UiData extends Data {
  private final DisplayMetrics mDisplayMetrics = ContextManager.resources().getDisplayMetrics();

  private UiData() {
    // 1) density
    mDensity = mDisplayMetrics.density;
    mDensityDpi = mDisplayMetrics.densityDpi;

    // 2) orientation
    mOrientation = ContextManager.config().orientation;
  }

  // Event

  public static final int EVENT_ORIENTATION_CHANGED = 1;

  // DisplayMetrics

  public DisplayMetrics displayMetrics() {
    return mDisplayMetrics;
  }

  // Density

  private final float mDensity;
  private final float mDensityDpi;

  public float density() {
    return mDensity;
  }

  public float densityDpi() {
    return mDensityDpi;
  }

  // Orientation

  // TODO(dengzb)

  public static final int ORIENTATION_PORTRAIT = Configuration.ORIENTATION_PORTRAIT;
  public static final int ORIENTATION_LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;

  int mOrientation;

  public boolean isPortrait() {
    return mOrientation == ORIENTATION_PORTRAIT;
  }

  public int orientation() {
    mOrientation = ContextManager.config().orientation;
    return mOrientation;
  }

  // Lock Orientation

  private boolean mIsOrientationLocked = false;

  public void lockOrientation(int orientation) {
    mIsOrientationLocked = true;
    int lock = (orientation == ORIENTATION_PORTRAIT)
        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    ContextManager.activity().setRequestedOrientation(lock);
  }

  private int mOrientationHolderCount = 0;

  public void holdOrientation() {
    if (mIsOrientationLocked) {
      return;
    }

    ++mOrientationHolderCount;
    if (mOrientationHolderCount == 1) {
      int hold = (mOrientation == ORIENTATION_PORTRAIT)
          ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
          : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
      ContextManager.activity().setRequestedOrientation(hold);
    }
  }

  public void unholdOrientation() {
    if (mIsOrientationLocked) {
      return;
    }

    --mOrientationHolderCount;
    if (mOrientationHolderCount == 0) {
      ContextManager.activity()
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
  }

  // Hold Screen On

  private int mScreenOnHoldCount = 0;

  public void holdScreenOn() {
    ++mScreenOnHoldCount;
    if (mScreenOnHoldCount == 1) {
      ContextManager.window().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
  }

  public void unholdScreenOn() {
    --mScreenOnHoldCount;
    Check.d(mScreenOnHoldCount >= 0);

    if (mScreenOnHoldCount == 0) {
      ContextManager.window().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
  }

  // Window Size

  /**
   * Window horizontal size in pixel
   */
  public int winWidth() {
    return mDisplayMetrics.widthPixels;
  }

  /**
   * Window vertical size in pixel
   */
  public int winHeight() {
    return mDisplayMetrics.heightPixels;
  }

  public int winShort() {
    return Math.min(winWidth(), winHeight());
  }

  public int winLong() {
    return Math.max(winWidth(), winHeight());
  }

  // Screen Size

  private Point mScreenPixel;
  private double mScreenInch;

  /**
   * The length of the shorter side of screen in pixels
   */
  public int screenShortSide() {
    ensureScreenSize();
    return Math.min(mScreenPixel.x, mScreenPixel.y);
  }

  /**
   * The length of the longer side of screen in pixels
   */
  public int screenLongSide() {
    ensureScreenSize();
    return Math.max(mScreenPixel.x, mScreenPixel.y);
  }

  public double screenInch() {
    ensureScreenSize();
    return mScreenInch;
  }

  private void ensureScreenSize() {
    if (mScreenPixel == null) {
      mScreenPixel = computeScreenSize();
      mScreenInch = computeScreenInch();
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  private static Point computeScreenSize() {
    final Point outSize = new Point();

    final Activity activity = ContextManager.activity();
    if (activity != null) {
      Display dp = activity.getWindowManager().getDefaultDisplay();

      try {
        if (CompatHelper.sdk(Build.VERSION_CODES.JELLY_BEAN)) {
          dp.getRealSize(outSize);
        } else {
          outSize.x = (Integer) Display.class.getMethod("getRawWidth").invoke(dp);
          outSize.y = (Integer) Display.class.getMethod("getRawHeight").invoke(dp);
        }
      } catch (Exception ignored) {
      }
    }

    if (outSize.x <= 0 || outSize.y <= 0) {
      DisplayMetrics dm = ContextManager.resources().getDisplayMetrics();
      outSize.x = dm.widthPixels;
      outSize.y = dm.heightPixels;
    }

    return outSize;
  }

  private double computeScreenInch() {
    final float dpi = selectDpi();
    final double widthInch = mScreenPixel.x / dpi;
    final double heightInch = mScreenPixel.y / dpi;

    return Math.sqrt(widthInch * widthInch + heightInch * heightInch);
  }

  /**
   * Select correct dpi value from original DisplayMetrics, The value is used
   * for calculating screen inches.
   * <p>
   * we pick up densityDpi, xdpi, ydpi from original DisplayMetrics, and the
   * selection strategy is, densityDpi will be preferred as
   * <p>
   * <pre>
   *          1) all they are regular value;
   *          2) all they are NOT regular value;
   *          3) densityDpi is regular, but the other two are both malformed(like 50 or 80 whatever)
   * </pre>
   * <p>
   * </p>
   * <p>
   * xdpi or ydpi will be preferred as
   * <p>
   * <pre>
   *          1) densityDpi is not regular and one of them is;
   *          2) densityDpi is regular and one of them is not regular but correct.
   * </pre>
   * <p>
   * </p>
   */
  private static float selectDpi() {
    final DisplayMetrics dm = ContextManager.resources().getDisplayMetrics();
    final float dDpi = dm.densityDpi;
    final float xDpi = dm.xdpi;
    final float yDpi = dm.ydpi;
    final boolean isXdpiRegular = isRegularDpi(xDpi);
    final boolean isYdpiRegular = isRegularDpi(yDpi);

    if (isRegularDpi(dDpi)) {
      boolean xyNearlySame = Math.abs(xDpi - yDpi) < X_Y_DPI_DIFF_THRESHOLDS;
      if (xyNearlySame) {
        if (!isXdpiRegular && !isDpiMalformed(xDpi)) {
          return xDpi;
        } else if (!isYdpiRegular && !isDpiMalformed(yDpi)) {
          return yDpi;
        }
      }
    } else {
      if (isXdpiRegular) {
        return xDpi;
      } else if (isYdpiRegular) {
        return yDpi;
      }
    }
    return dDpi;
  }

  /**
   * Define all supported dpi values. 120 ldpi, 160 mdpi, 240 hdpi, 320 xhdpi,
   * 480 xxhdpi. And define all it's tolerance also.
   */
  // TODO
  private static final int[] REGULAR_DPI = new int[] {120, 160, 240, 320, 480};
  private static final int[] REGULAR_DPI_TOL = new int[] {10, 20, 40, 40, 60};
  private static final int X_Y_DPI_DIFF_THRESHOLDS = 20;

  /**
   * Test the specified dpi value is regular. Regular dpi is define in
   * REGULAR_DPI array.
   *
   * @return True if the dpi value is near to Regular value
   */
  private static boolean isRegularDpi(float dpi) {
    for (int i = 0; i < REGULAR_DPI.length; ++i) {
      if (Math.abs(dpi - REGULAR_DPI[i]) < REGULAR_DPI_TOL[i]) {
        return true;
      }
    }
    return false;
  }

  private static boolean isDpiMalformed(float dpi) {
    return dpi < REGULAR_DPI[0] || dpi > REGULAR_DPI[REGULAR_DPI.length - 1];
  }
}
