package com.home77.common.base.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public class HashHelper {
  public static String digestMd5(String param) {
    String md5String;

    // space is legal here
    if (param == null || param.length() == 0) {
      return param;
    }

    md5String = digestMd5(param.getBytes());

    if (TextHelper.isEmptyOrSpaces(md5String)) {
      int end = param.length() > 32 ? 32 : param.length();
      md5String = param.substring(0, end);
    }

    return md5String;
  }

  public static String digestMd5(byte[] param) {
    String md5String = null;

    if (param != null) {
      try {
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        md5String = TextHelper.byteToHexString(md5Digest.digest(param));
      } catch (NoSuchAlgorithmException ignored) {
      }
    }

    return md5String;
  }

  public static String digestMd5(Collection<String> values) {
    if (values == null || values.isEmpty()) {
      return null;
    }

    String md5String = null;

    try {
      MessageDigest md5Digest = MessageDigest.getInstance("MD5");
      for (String s : values) {
        md5Digest.update(s.getBytes());
      }
      md5String = TextHelper.byteToHexString(md5Digest.digest());
    } catch (NoSuchAlgorithmException ignored) {
    }

    return md5String;
  }
}
