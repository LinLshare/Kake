package com.home77.kake;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

/**
 * @author CJ
 */
public class GlobalData {

  public static final String KEY_USER_NAME = "user_name";
  public static final String KEY_USER_AVATER = "user_avatar";
  public static final String KEY_USER_MOBILE = "user_avatar";

  public static final String KEY_ACCESS_TOKEN = "access_token";
  public static final String KEY_REFRESH_TOKEN = "refresh_token";
  public static final String KEY_EXPIRE_IN = "expires_in";
  public static final String KEY_TOKEN_TYPE = "token_type";

  private final SharedPreferences sharedPreferences;

  public GlobalData(Context context) {
    sharedPreferences = context.getSharedPreferences("kake_global", Context.MODE_PRIVATE);
  }

  Map<String, Object> map = new HashMap<>();

  public void restore() {
    map.clear();
    map.putAll(sharedPreferences.getAll());
  }

  public GlobalData putString(String key, @Nullable String value) {
    map.put(key, value);
    sharedPreferences.edit().putString(key, value).apply();
    return this;
  }

  public GlobalData putInt(String key, int value) {
    map.put(key, value);
    sharedPreferences.edit().putInt(key, value).apply();
    return this;
  }

  public GlobalData putLong(String key, long value) {
    map.put(key, value);
    sharedPreferences.edit().putLong(key, value).apply();
    return this;
  }

  public GlobalData putBoolean(String key, boolean value) {
    map.put(key, value);
    sharedPreferences.edit().putBoolean(key, value).apply();
    return this;
  }

  public GlobalData putFloat(String key, float value) {
    map.put(key, value);
    sharedPreferences.edit().putFloat(key, value).apply();
    return this;
  }

  public Map<String, Object> getAll() {
    return map;
  }

  @Nullable
  public String getString(String key, @Nullable String defValue) {
    Object o = map.get(key);
    if (o == null || !(o instanceof String)) {
      return defValue;
    }
    return (String) o;
  }

  public int getInt(String key, int defValue) {
    Object o = map.get(key);
    if (o == null || !(o instanceof Integer)) {
      return defValue;
    }
    return (int) o;
  }

  public long getLong(String key, long defValue) {
    Object o = map.get(key);
    if (o == null || !(o instanceof Long)) {
      return defValue;
    }
    return (long) o;
  }

  public float getFloat(String key, float defValue) {
    Object o = map.get(key);
    if (o == null || !(o instanceof Float)) {
      return defValue;
    }
    return (float) o;
  }

  public boolean getBoolean(String key, boolean defValue) {
    Object o = map.get(key);
    if (o == null || !(o instanceof Boolean)) {
      return defValue;
    }
    return (boolean) o;
  }
}
