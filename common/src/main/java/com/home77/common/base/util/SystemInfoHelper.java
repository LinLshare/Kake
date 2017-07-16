package com.home77.common.base.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.home77.common.base.component.ContextManager;
import com.home77.common.base.debug.Check;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class SystemInfoHelper {
  /**
   * A 64-bit number (as a hex string) that is randomly generated on the
   * device's first boot and should remain constant for the lifetime of the
   * device. (The value may change if a factory reset is performed on the
   * device.)
   */
  @SuppressLint("HardwareIds")
  public static String androidId() {
    return Secure.getString(ContextManager.contentResolver(), Secure.ANDROID_ID);
  }

  @SuppressLint("HardwareIds")
  public static String imei() {
    String imei = null;
    if (ContextManager.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
      try {
        TelephonyManager mgr =
            (TelephonyManager) ContextManager.systemService(Context.TELEPHONY_SERVICE);
        if (mgr != null) {
          imei = mgr.getDeviceId();
        }
      } catch (Exception ignored) {
      }
    }
    return TextHelper.ensureNotNull(imei);
  }

  @SuppressLint("HardwareIds")
  public static String imsi() {
    String imsi = null;
    if (ContextManager.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
      try {
        TelephonyManager mgr =
            (TelephonyManager) ContextManager.systemService(Context.TELEPHONY_SERVICE);
        if (mgr != null) {
          imsi = mgr.getSubscriberId();
        }
      } catch (Exception ignored) {
      }
    }
    return TextHelper.ensureNotNull(imsi);
  }

  @SuppressLint("HardwareIds")
  public static String simId() {
    String simId = null;
    if (ContextManager.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)) {
      try {
        TelephonyManager mgr =
            (TelephonyManager) ContextManager.systemService(Context.TELEPHONY_SERVICE);
        if (mgr != null) {
          simId = mgr.getSimSerialNumber();
        }
      } catch (Exception ignored) {
      }
    }
    return TextHelper.ensureNotNull(simId);
  }

  @SuppressLint("HardwareIds")
  public static String wifiMac() {
    String mac = null;
    try {
      WifiManager mgr = (WifiManager) ContextManager.systemService(Context.WIFI_SERVICE);
      if (mgr != null) {
        mac = mgr.getConnectionInfo().getMacAddress();
      }
    } catch (Exception ignored) {
    }
    return TextHelper.ensureNotNull(mac);
  }

  private static String mCPUArch;

  @SuppressWarnings("deprecation")
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static String cpuArch() {
    if (mCPUArch == null) {
      /**
       * Email from yuming.li@intel.com in 2014/06/27 said their new x86
       * ROM modifies the android.os.abi to make the Build.CPU_ABI to
       * always return "armeabi-v7a" and recommended following method to
       * get real CPU arch.
       */
      BufferedReader ibr = null;
      try {
        Process process = Runtime.getRuntime().exec("getprop ro.product.cpu.abi");
        ibr = new BufferedReader(new InputStreamReader(process.getInputStream()));
        mCPUArch = ibr.readLine();
      } catch (IOException ignored) {
      } finally {
        if (ibr != null) {
          try {
            ibr.close();
          } catch (IOException ignored) {
          }
        }
      }

      if (TextUtils.isEmpty(mCPUArch)) {
        // if meet something wrong, get cpu arch from android sdk.
        mCPUArch = CompatHelper.sdk(21) ? Build.SUPPORTED_ABIS[0] : Build.CPU_ABI;
      }
    }

    return mCPUArch;
  }

  private static long mRamSize;

  /**
   * Get total ram size of this device.
   *
   * @return -- ram size in byte.
   */
  public static long ramSize() {
    if (mRamSize == 0) {
      BufferedReader br = null;
      try {
        br = new BufferedReader(new FileReader("/proc/meminfo"), 1024);
        String line = br.readLine();

                /*
                 * # cat /proc/meminfo MemTotal: 94096 kB MemFree: 1684 kB
                 */
        if (!TextUtils.isEmpty(line)) {
          String[] splits = line.split("\\s+");
          if (splits.length > 1) {
            mRamSize = Long.valueOf(splits[1]) * UnitHelper.BYTES_PER_KB;
          }
        }
      } catch (IOException ignored) {
      } finally {
        if (br != null) {
          try {
            br.close();
          } catch (IOException ignored) {
          }
        }
      }
    }

    return mRamSize;
  }

  /**
   * The android version which is displayed in system Settings/About
   * phone/Android version
   */
  public static String osVersion() {
    return Build.VERSION.RELEASE;
  }

  /**
   * The serial number which is displayed in system Settings/About
   * phone/Status/Serial number
   */
  public static String serial() {
    return Build.SERIAL;
  }

  /**
   * The OS build number which is displayed in system Settings/About
   * phone/Build number
   */
  public static String displayBuildNumber() {
    return Build.DISPLAY;
  }

  /**
   * The OS build number
   */
  public static String buildNumber() {
    return Build.ID;
  }

  /**
   * The baseband version which is displayed in system Settings/About
   * phone/Baseband version
   */
  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public static String baseband() {
    if (CompatHelper.sdk(14)) {
      try {
        return Build.getRadioVersion();
      } catch (Throwable e) {
        Check.d(e);
      }
    }
    return TextHelper.UNKNOWN;
  }

  /**
   * The end-user-visible name for the end product.
   */
  public static String device() {
    return Build.MODEL;
  }

  /**
   * The manufacturer (e.g.,Samsung/Huawei...) of the product/hardware
   */
  public static String manufacturer() {
    return Build.MANUFACTURER;
  }

  /**
   * The brand (e.g., Google) the software is customized for, if any.
   */
  public static String brand() {
    return Build.BRAND;
  }
}
