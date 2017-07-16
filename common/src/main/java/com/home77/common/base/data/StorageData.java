package com.home77.common.base.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.home77.common.BuildConfig;
import com.home77.common.base.component.ContextManager;
import com.home77.common.base.debug.Check;

import java.util.HashSet;

/**
 * Data with ability to save synchronously simple key-values into Android
 * specified SharedPreferences. Only use this when you need stability from
 * system storage.
 */
public abstract class StorageData extends Data {
  private final String mStorageName;

  protected StorageData(String storageName) {
    mStorageName = storageName;

    if (BuildConfig.DEBUG_BUILD) {
      Check.d(sStorageDataSet.add(mStorageName), "Duplicate storage name : " + mStorageName);
    }
  }

  private static final HashSet<String> sStorageDataSet = new HashSet<>();

  public final SharedPreferences storage() {
    int mode = Context.MODE_PRIVATE;
    // Do not support |MODE_MULTI_PROCESS| anymore, as it will lost data in multi apply() and it
    // no longer support in Android M.
    return ContextManager.sharedPreferences(mStorageName, mode);
  }

  // Getter/Setter

  public void put(String key, int value) {
    storage().edit().putInt(key, value).apply();
  }

  public void put(String key, long value) {
    storage().edit().putLong(key, value).apply();
  }

  public void put(String key, boolean value) {
    storage().edit().putBoolean(key, value).apply();
  }

  public void put(String key, String value) {
    storage().edit().putString(key, value).apply();
  }

  public int getInt(String key, int defaultValue) {
    return storage().getInt(key, defaultValue);
  }

  public long getLong(String key, long defaultValue) {
    return storage().getLong(key, defaultValue);
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return storage().getBoolean(key, defaultValue);
  }

  public String getString(String key, String defaultValue) {
    return storage().getString(key, defaultValue);
  }

  public boolean contains(String key) {
    return storage().contains(key);
  }

  public void clear() {
    storage().edit().clear().apply();
  }

  public void remove(String key) {
    storage().edit().remove(key).apply();
  }
}
