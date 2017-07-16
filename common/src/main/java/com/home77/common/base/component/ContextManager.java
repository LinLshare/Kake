package com.home77.common.base.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.home77.common.base.debug.Check;
import com.home77.common.base.util.CompatHelper;

import java.util.Locale;

@SuppressLint("StaticFieldLeak")
public final class ContextManager {
  public static void init(Context context) {
    sAppContext = context.getApplicationContext();
  }

  // app scope

  private static Context sAppContext;

  /**
   * Use this as possible instead of {@link #activity()}
   */
  public static Context appContext() {
    Check.d(sAppContext != null, "init first!");
    return sAppContext;
  }

  public static Resources resources() {
    return sAppContext.getResources();
  }

  public static AssetManager assetManager() {
    return sAppContext.getAssets();
  }

  public static ContentResolver contentResolver() {
    return sAppContext.getContentResolver();
  }

  public static Object systemService(String name) {
    if (!TextUtils.isEmpty(name)) {
      return sAppContext.getSystemService(name);
    }
    return null;
  }

  public static SharedPreferences sharedPreferences(String name, int mode) {
    return sAppContext.getSharedPreferences(name, mode);
  }

  public static PackageManager packageManager() {
    return sAppContext.getPackageManager();
  }

  public static ActivityManager activityManager() {
    return (ActivityManager) sAppContext.getSystemService(Context.ACTIVITY_SERVICE);
  }

  public static WindowManager windowManager() {
    return (WindowManager) sAppContext.getSystemService(Context.WINDOW_SERVICE);
  }

  public static Configuration config() {
    return resources().getConfiguration();
  }

  @TargetApi(Build.VERSION_CODES.N)
  public static Locale locale() {
    final Locale locale;
    if (CompatHelper.sdk(Build.VERSION_CODES.N)) {
      locale = config().getLocales().get(0);
    } else {
      locale = config().locale;
    }
    return locale;
  }

  @TargetApi(Build.VERSION_CODES.M)
  public static boolean checkSelfPermission(String name) {
    return !CompatHelper.sdk(Build.VERSION_CODES.M) ||
           (sAppContext.checkSelfPermission(name) == PackageManager.PERMISSION_GRANTED);
  }

  // activity scope

  private static Activity sActivity;

  public static void update(Activity activity) {
    sActivity = activity;
  }

  public static Activity activity() {
    return sActivity;
  }

  public static Window window() {
    return activity().getWindow();
  }

  public static ViewConfiguration viewConfig() {
    return ViewConfiguration.get(sActivity);
  }
}
