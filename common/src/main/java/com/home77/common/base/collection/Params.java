package com.home77.common.base.collection;

import android.util.SparseArray;

public class Params {
  // Static API

  /**
   * Get with default value when params is null or key is not contained.
   *
   * @param params
   * @param key
   * @param defaultValue
   *
   * @return value associate to |key| or |defaultValue| if not contain |key|.
   */
  @SuppressWarnings("unchecked")
  public static <T> T get(Params params, int key, T defaultValue) {
    if (params == null || !params.containsKey(key)) {
      return defaultValue;
    }
    return (T) params.get(key);
  }

  /**
   * Safe way to check if a params which is possibly {@code null} contains a
   * key.
   *
   * @param params
   * @param key
   *
   * @return true if |params| contain specific |key|
   */
  public static boolean contains(Params params, int key) {
    return params != null && params.containsKey(key);
  }

  // Instance API

  /**
   * Create an instance out of pool management.
   */
  public static Params create() {
    return new Params();
  }

  /**
   * Convenient method to create {@link #Params} with one param only.
   */
  public static Params create(int key, Object value) {
    return create().put(key, value);
  }

  /**
   * Convenient method to create {@link #Params} with merging another |Params|.
   */
  public static Params create(Params other) {
    return create().merge(other);
  }

  public <T> T get(int key) {
    return get(key, null);
  }

  @SuppressWarnings("unchecked")
  public <T> T get(int key, T valueIfKeyNotFound) {
    Object value = mMap.get(key, valueIfKeyNotFound);
    return (T) value;
  }

  public boolean containsKey(int key) {
    return mMap.indexOfKey(key) >= 0;
  }

  public boolean isEmpty() {
    return mMap.size() == 0;
  }

  public Params merge(Params src) {
    /** For {@link SparseArray}, ascending append is more efficient */
    for (int i = 0, size = src.size(); i < size; ++i) {
      mMap.append(src.keyAt(i), src.valueAt(i));
    }
    return this;
  }

  public Params clone() {
    return create(this);
  }

  // SparseArray Methods

  public int size() {
    return mMap.size();
  }

  public int indexOfKey(int key) {
    return mMap.indexOfKey(key);
  }

  public int indexOfValue(Object value) {
    return mMap.indexOfValue(value);
  }

  public int keyAt(int index) {
    return mMap.keyAt(index);
  }

  public Object valueAt(int index) {
    return mMap.valueAt(index);
  }

  public Params put(int key, Object value) {
    mMap.append(key, value);
    return this;
  }

  public Params remove(int key) {
    mMap.remove(key);
    return this;
  }

  public Params clear() {
    mMap.clear();
    return this;
  }

  // Internal

  private final SparseArray<Object> mMap = new SparseArray<>();

  private Params() {
  }

  @Override
  public String toString() {
    if (size() <= 0) {
      return "{}";
    }

    StringBuilder buffer = new StringBuilder(size() * 28);
    buffer.append('{');
    for (int i = 0; i < size(); i++) {
      if (i > 0) {
        buffer.append(", ");
      }
      int key = keyAt(i);
      buffer.append(key);
      buffer.append('=');
      Object value = valueAt(i);
      if (value != this) {
        buffer.append(value);
      } else {
        buffer.append("(this Map)");
      }
    }
    buffer.append('}');
    return buffer.toString();
  }
}
