package com.home77.kake.common.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * check what user inputs is legal or not.
 *
 * @author CJ
 */
public class InputChecker {

  public static boolean isPhoneNumberLegal(String phoneNumber) {
    if (TextUtils.isEmpty(phoneNumber)) {
      return false;
    }
    Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6,16}$");
    Matcher matcher = pattern.matcher(phoneNumber);
    return matcher.matches();
  }

  public static boolean isPasswordLegal(String password) {
    if (TextUtils.isEmpty(password)) {
      return false;
    }
    return true;
  }

  public static boolean isUserNameLegal(String userName) {
    if (TextUtils.isEmpty(userName)) {
      return false;
    }
    return true;
  }
}
