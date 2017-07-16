package com.home77.common.base.debug;

import android.util.Log;
import android.widget.Toast;

import com.home77.common.BuildConfig;
import com.home77.common.base.component.ContextManager;


public final class DLog {
  public static void i(String tag, String msg, Object... args) {
    if (BuildConfig.DEBUG_BUILD) {
      Log.i(tag, format(msg, args));
    }
  }

  public static void d(String tag, String msg, Object... args) {
    if (BuildConfig.DEBUG_BUILD) {
      Log.d(tag, format(msg, args));
    }
  }

  public static void w(String tag, String msg, Throwable tr) {
    if (BuildConfig.DEBUG_BUILD) {
      Log.w(tag, msg, tr);
    }
  }

  public static void w(String tag, String msg, Object... args) {
    if (BuildConfig.DEBUG_BUILD) {
      Log.w(tag, format(msg, args));
    }
  }

  public static void e(String tag, String msg, Throwable tr) {
    if (BuildConfig.DEBUG_BUILD) {
      Log.e(tag, msg, tr);
    }
  }

  public static void e(String tag, String msg, Object... args) {
    if (BuildConfig.DEBUG_BUILD) {
      Log.e(tag, format(msg, args));
    }
  }

  public static void v(String tag, String msg, Object... args) {
    if (BuildConfig.DEBUG_BUILD) {
      Log.v(tag, format(msg, args));
    }
  }

  //  /** Tag for release log */
  //  private final static String RELEASE_LOG_TAG = "";
  //
  //  /**
  //   * Output log in logcat (available in release version)
  //   * Use:
  //   * <code>
  //   * adb shell setprop log.tag.UNIONADS DEBUG
  //   * </code>
  //   * to turn on log output.
  //   */
  //  public static void log(String tag, String msg, Object... args) {
  //    if (Log.isLoggable(RELEASE_LOG_TAG, Log.DEBUG) || BuildConfig.DEBUG_BUILD) {
  //      Log.d(RELEASE_LOG_TAG, format("[" + tag + "]" + msg, args));
  //    }
  //  }

  private static String format(String msg, Object[] args) {
    if (args.length > 0) {
      msg = String.format(msg, args);
    }
    return msg;
  }

  public static void toast(String msg) {
    if (BuildConfig.DEBUG_BUILD) {
      Toast.makeText(ContextManager.appContext(), msg, Toast.LENGTH_SHORT).show();
    }
  }
}
