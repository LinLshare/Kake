package com.home77.kake.common.utils;

import android.graphics.Bitmap;
import android.os.Build;

/**
 * @author CJ
 */
public class BitmapHelper {
  public static int sizeOf(Bitmap data) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
      return data.getRowBytes() * data.getHeight();
    } else {
      return data.getByteCount();
    }
  }
}
