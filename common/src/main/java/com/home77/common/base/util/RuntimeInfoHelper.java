package com.home77.common.base.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Looper;

import com.home77.common.base.component.ContextManager;

import java.util.List;

public final class RuntimeInfoHelper {

  // Runtime Info

  /**
   * @return current process id of this application
   */
  public static int pid() {
    return android.os.Process.myPid();
  }

  private static String sProcessName;

  /**
   * @return current process name of this application
   */
  public static String processName() {
    if (sProcessName == null) {
      List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = null;
      final int pid = pid();
      final ActivityManager am =
          (ActivityManager) ContextManager.systemService(Context.ACTIVITY_SERVICE);
      if (am != null) {
        runningAppProcessInfoList = am.getRunningAppProcesses();
      }
      if (runningAppProcessInfoList != null) {
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcessInfoList) {
          if (info.pid == pid) {
            sProcessName = info.processName;
            break;
          }
        }
      }
    }
    return sProcessName;
  }

  public static String mainProcessName() {
    final ApplicationInfo info = AppInfoHelper.currentApplicationInfo();
    return (info == null) ? TextHelper.EMPTY : info.processName;
  }

  public static boolean currentlyOnMainProcess() {
    final String main = mainProcessName();
    final String current = processName();
    return !TextHelper.isEmptyOrSpaces(current) && current.equals(main);
  }

  public static boolean currentlyOnMainThread() {
    return Looper.getMainLooper() == Looper.myLooper();
  }
}
