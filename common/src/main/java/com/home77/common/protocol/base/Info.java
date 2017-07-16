package com.home77.common.protocol.base;

import com.google.gson.annotations.SerializedName;

import static com.home77.common.protocol.base.Constants.SEPARATOR;

public final class Info {
  public final static String PLATFORM_WEB = "web";
  public final static String PLATFORM_ANDROID = "android";

  // common

  @SerializedName("platform")
  private String mPlatform;

  @SerializedName("version")
  private String mVersion;

  // android

  @SerializedName("identity")
  private String mIdentity;

  @SerializedName("pkg")
  private String mPkg;

  @SerializedName("pkg_ve")
  private String mPkgVersion;

  // getter

  public String platform() {
    return mPlatform;
  }

  public String version() {
    return mVersion;
  }

  public String identity() {
    return mIdentity;
  }

  public String pkg() {
    return mPkg;
  }

  public String pkgVersion() {
    return mPkgVersion;
  }

  public boolean isWeb() {
    return PLATFORM_WEB.equals(mPlatform);
  }

  public boolean isAndroid() {
    return PLATFORM_ANDROID.equals(mPlatform);
  }

  // setter

  public static Info make(String platform, String version) {
    final Info info = new Info();
    info.mPlatform = platform;
    info.mVersion = version;
    return info;
  }

  public static Info makeWeb(String version) {
    return make(PLATFORM_WEB, version);
  }

  public static Info makeAndroid(String version, String identity, String pkg) {
    final Info info = make(PLATFORM_ANDROID, version);
    info.mIdentity = identity;
    info.mPkg = pkg;
    info.mPkgVersion = version;
    return info;
  }

  // to string

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("platform=").append(mPlatform);
    sb.append(SEPARATOR).append("version=").append(mVersion);
    switch (mPlatform) {
      case PLATFORM_WEB:
        break;
      case PLATFORM_ANDROID:
        sb.append(SEPARATOR).append("identity=").append(mIdentity);
        sb.append(SEPARATOR).append("pkg=").append(mPkg);
        sb.append(SEPARATOR).append("pkg_ve=").append(mPkgVersion);
        break;
      default:
        break;
    }
    return sb.toString();
  }
}
