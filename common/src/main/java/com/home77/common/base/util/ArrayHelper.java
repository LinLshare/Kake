package com.home77.common.base.util;

public final class ArrayHelper {
  public static boolean contains(int[] array, int target) {
    if (array != null) {
      for (int item : array) {
        if (item == target) {
          return true;
        }
      }
    }
    return false;
  }

  public static void checkOffsetAndCount(long size, long offset, long byteCount) {
    if ((offset | byteCount) < 0 || offset > size || size - offset < byteCount) {
      throw new ArrayIndexOutOfBoundsException(String.format("size=%s offset=%s byteCount=%s",
                                                             size,
                                                             offset,
                                                             byteCount));
    }
  }

  public static boolean arrayRangeEquals(byte[] a,
                                         int aOffset,
                                         byte[] b,
                                         int bOffset,
                                         int byteCount) {
    for (int i = 0; i < byteCount; i++) {
      if (a[i + aOffset] != b[i + bOffset]) {
        return false;
      }
    }
    return true;
  }
}
