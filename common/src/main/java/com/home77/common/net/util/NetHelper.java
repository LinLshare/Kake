package com.home77.common.net.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.home77.common.base.component.ContextManager;

import java.util.Locale;

public final class NetHelper {
  private static NetworkInfo activeConnectivityNetworkInfo() {
    final ConnectivityManager connectivity =
        (ConnectivityManager) ContextManager.systemService(Context.CONNECTIVITY_SERVICE);
    if (connectivity == null) {
      return null;
    }
    return connectivity.getActiveNetworkInfo();
  }

  private static int currentTelephonyNetworkType() {
    final TelephonyManager telephonyManager =
        (TelephonyManager) ContextManager.systemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager == null) {
      return TelephonyManager.NETWORK_TYPE_UNKNOWN;
    }
    return telephonyManager.getNetworkType();
  }

  public static boolean isNetworkAvailable() {
    final NetworkInfo info = activeConnectivityNetworkInfo();
    return info != null && info.isConnected();
  }

  public static boolean isWifiAvailable() {
    final NetworkInfo info = activeConnectivityNetworkInfo();
    if (info != null && !info.getTypeName().toLowerCase(Locale.US).equals("mobile")) {
      return true;
    }
    return false;
  }

  public static final String NETWORK_UNAVAILABLE = "unavailable";

  public static String getNetworkType() {
    final NetworkInfo info = activeConnectivityNetworkInfo();
    if (info == null) {
      return NETWORK_UNAVAILABLE;
    }
    String networkType = info.getTypeName().toLowerCase(Locale.US);
    if (networkType.equals("mobile")) {
      final int currentNetworkType = currentTelephonyNetworkType();
      networkType = convertTelephonyNetworkType(currentNetworkType);
    }
    return networkType;
  }

  private static String convertTelephonyNetworkType(int type) {
    final String networkType;
    switch (type) {
      case TelephonyManager.NETWORK_TYPE_GPRS:
      case TelephonyManager.NETWORK_TYPE_EDGE:
      case TelephonyManager.NETWORK_TYPE_CDMA:
      case TelephonyManager.NETWORK_TYPE_1xRTT:
      case TelephonyManager.NETWORK_TYPE_IDEN:
        networkType = "mobile_2G";
        break;
      case TelephonyManager.NETWORK_TYPE_UMTS:
      case TelephonyManager.NETWORK_TYPE_EVDO_0:
      case TelephonyManager.NETWORK_TYPE_EVDO_A:
      case TelephonyManager.NETWORK_TYPE_EVDO_B:
      case TelephonyManager.NETWORK_TYPE_HSDPA:
      case TelephonyManager.NETWORK_TYPE_HSPA:
      case TelephonyManager.NETWORK_TYPE_HSPAP:
      case TelephonyManager.NETWORK_TYPE_HSUPA:
        networkType = "mobile_3G";
        break;
      case TelephonyManager.NETWORK_TYPE_LTE:
        networkType = "mobile_4G";
        break;
      default:
        networkType = "mobile_unknown";
        break;
    }
    return networkType;
  }
}
