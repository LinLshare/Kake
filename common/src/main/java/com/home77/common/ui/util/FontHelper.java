package com.home77.common.ui.util;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TextView;

public final class FontHelper {
  public static final Typeface REGULAR = Typeface.DEFAULT;
  public static final Typeface BOLD = Typeface.DEFAULT_BOLD;
  public static final Typeface LIGHT = Typeface.create("sans-serif-light", Typeface.NORMAL);

  static public void setTextPixelSize(TextView tv, float pixelSize) {
    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixelSize);
  }
}
