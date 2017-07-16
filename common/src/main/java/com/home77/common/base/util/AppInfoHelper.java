package com.home77.common.base.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.home77.common.base.component.ContextManager;
import com.home77.common.base.debug.Check;
import com.home77.common.base.debug.DLog;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class AppInfoHelper {
  private final static String TAG = AppInfoHelper.class.getSimpleName();

  // Self RuntimeInfoHelper Info

  static String sPackageName;

  /**
   * @return current package name of this application
   */
  public static String packageName() {
    if (sPackageName == null) {
      sPackageName = ContextManager.appContext().getPackageName();
    }
    return sPackageName;
  }

  static String sVersionName;

  /**
   * @return current version name of this application
   */
  public static String versionName() {
    if (sVersionName == null) {
      try {
        sVersionName = ContextManager.packageManager().getPackageInfo(packageName(), 0).versionName;
      } catch (PackageManager.NameNotFoundException ignored) {
      }
      sVersionName = TextHelper.ensureNotNull(sVersionName);
    }
    return sVersionName;
  }

  static int sVersionCode = Integer.MIN_VALUE;

  /**
   * @return current version code of this application
   */
  public static int versionCode() {
    if (sVersionCode == Integer.MIN_VALUE) {
      try {
        sVersionCode = ContextManager.packageManager().getPackageInfo(packageName(), 0).versionCode;
      } catch (PackageManager.NameNotFoundException ignored) {
      }
    }
    return sVersionCode;
  }

  /**
   * @return default application info of this application
   */
  public static ApplicationInfo currentApplicationInfo() {
    return ContextManager.appContext().getApplicationInfo();
  }

  /**
   * @return rich application info of this application
   */
  public static ApplicationInfo currentApplicationInfo(int flags) {
    return applicationInfo(packageName(), null, flags);
  }

  /**
   * @return meta string associate to |metaKey| of this application
   */
  public static String currentApplicationInfoMetaString(String metaKey) {
    final ApplicationInfo info = currentApplicationInfo(PackageManager.GET_META_DATA);
    final Bundle metaBundle = (info != null) ? info.metaData : null;
    return (metaBundle != null) ? metaBundle.getString(metaKey) : null;
  }

  /**
   * @return meta string associate to |metaKey| of this application
   */
  public static PackageInfo currentPackageInfo(int flags) {
    return packageInfo(packageName(), flags);
  }

  public static String selfSignature() {
    return signature(packageName());
  }

  // Application Info

  public static ApplicationInfo applicationInfo(String pkg) {
    return applicationInfo(pkg, null, PackageManager.GET_UNINSTALLED_PACKAGES);
  }

  public static ApplicationInfo applicationInfo(String pkg, int flags) {
    return applicationInfo(pkg, null, flags);
  }

  public static ApplicationInfo applicationInfo(String pkg, Collection<String> alias, int flags) {
    final PackageManager pm = ContextManager.packageManager();

    // ). prepare pkg alias list.
    ArrayList<String> pkgAlias = new ArrayList<>();
    pkgAlias.add(pkg);
    if (alias != null) {
      pkgAlias.addAll(alias);
    }

    // ). fetch application info
    ApplicationInfo info = null;
    for (String p : pkgAlias) {
      Check.d(!TextHelper.isEmptyOrSpaces(p));

      try {
        info = pm.getApplicationInfo(p, flags);
      } catch (PackageManager.NameNotFoundException e) {
        DLog.e(TAG, e.getMessage(), e);
      }
      if (info != null) {
        break;
      }
    }

    return info;
  }

  public static String applicationLabel(ApplicationInfo info) {
    return info.loadLabel(ContextManager.packageManager()).toString().trim();
  }

  public static Drawable applicationIcon(ApplicationInfo info) {
    Drawable icon = null;
    try {
      icon = info.loadIcon(ContextManager.packageManager());
    } catch (OutOfMemoryError e) {
      DLog.e(TAG, e.getMessage(), e);
    }
    return icon;
  }

  public static boolean appInstalled(String packageName) {
    ApplicationInfo info = null;
    try {
      info = ContextManager.packageManager()
                           .getApplicationInfo(packageName,
                                               PackageManager.GET_UNINSTALLED_PACKAGES);
    } catch (Exception e) {
      DLog.w(TAG, e.getMessage(), e);
    }
    return info != null && FileHelper.exists(info.sourceDir);
  }

  // Package Info

  public static PackageInfo packageInfo(String pkg, int flags) {
    final PackageManager pm = ContextManager.packageManager();
    PackageInfo pi = null;
    try {
      pi = pm.getPackageInfo(pkg, flags);
    } catch (Exception e) {
      DLog.e(TAG, e.getMessage(), e);
    }
    return pi;
  }

  /**
   * Get currently install application list, including both system and user's
   *
   * @return currently installed system and user's application list
   */
  public static List<PackageInfo> installedApps() {
    final PackageManager pm = ContextManager.packageManager();
    List<ApplicationInfo> packages = null;
    try {
      packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
    } catch (Exception e) {
      DLog.e(TAG, e.getMessage(), e);
    }

    List<PackageInfo> packageInfoList = new ArrayList<>();
    if (packages != null) {
      for (ApplicationInfo appInfo : packages) {
        PackageInfo pi = packageInfo(appInfo.packageName, 0);

        if (pi != null) {
          packageInfoList.add(pi);
        }
      }
    }
    return packageInfoList;
  }

  public static String signature(String pkg) {
    String ret = "";
    try {
      Signature[] signs = packageInfo(pkg, PackageManager.GET_SIGNATURES).signatures;

      CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
      X509Certificate X509Cert =
          (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(signs[0]
                                                                                                .toByteArray()));

      ret = HashHelper.digestMd5(X509Cert.getSignature());
    } catch (Exception e) {
      Check.d(e);
    }

    return ret;
  }
}
