package com.home77.common.ui.util;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


@SuppressLint("RtlHardcoded")
public final class LayoutHelper {
  // Gravity

  public static final int START = 0x00800000 | Gravity.LEFT;
  public static final int END = 0x00800000 | Gravity.RIGHT;

  public static final int CENTER = Gravity.CENTER;
  public static final int LEFT_CENTER = START | Gravity.CENTER_VERTICAL;
  public static final int RIGHT_CENTER = END | Gravity.CENTER_VERTICAL;
  public static final int FILL_CENTER = Gravity.FILL_HORIZONTAL | Gravity.CENTER_VERTICAL;
  public static final int CENTER_TOP = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
  public static final int CENTER_BOTTOM = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
  public static final int CENTER_FILL = Gravity.CENTER_HORIZONTAL | Gravity.FILL_VERTICAL;

  public static final int FILL = Gravity.FILL;
  public static final int LEFT_FILL = START | Gravity.FILL_VERTICAL;
  public static final int RIGHT_FILL = END | Gravity.FILL_VERTICAL;
  public static final int FILL_TOP = Gravity.FILL_HORIZONTAL | Gravity.TOP;
  public static final int FILL_BOTTOM = Gravity.FILL_HORIZONTAL | Gravity.BOTTOM;

  public static final int LEFT_TOP = START | Gravity.TOP;
  public static final int RIGHT_TOP = END | Gravity.TOP;
  public static final int LEFT_BOTTOM = START | Gravity.BOTTOM;
  public static final int RIGHT_BOTTOM = END | Gravity.BOTTOM;

  // LayoutParams

  public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
  public static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;

  // VG
  public static final ViewGroup.LayoutParams VG_WRAP = new ViewGroup.LayoutParams(WRAP, WRAP);
  public static final ViewGroup.LayoutParams VG_MATCH = new ViewGroup.LayoutParams(MATCH, MATCH);

  // FL
  public static final FrameLayout.LayoutParams FL_WRAP =
      new FrameLayout.LayoutParams(WRAP, WRAP, LEFT_TOP);
  public static final FrameLayout.LayoutParams FL_MATCH =
      new FrameLayout.LayoutParams(MATCH, MATCH, FILL);

  public static final FrameLayout.LayoutParams FL_MATCH_WRAP =
      new FrameLayout.LayoutParams(MATCH, WRAP, FILL);

  public static final FrameLayout.LayoutParams FL_WRAP_WRAP_LEFT =
      new FrameLayout.LayoutParams(MATCH, WRAP, LEFT_CENTER);

  public static final FrameLayout.LayoutParams FL_WRAP_WRAP_RIGHT =
      new FrameLayout.LayoutParams(MATCH, WRAP, RIGHT_CENTER);


  public static FrameLayout.LayoutParams flp(View v) {
    return (FrameLayout.LayoutParams) v.getLayoutParams();
  }

  public static LinearLayout.LayoutParams llp(View v) {
    return (LinearLayout.LayoutParams) v.getLayoutParams();
  }

  public static void setLayout(View v, int width, int height, int... margins) {
    ViewGroup.LayoutParams lp = v.getLayoutParams();
    lp.width = width;
    lp.height = height;
    setMargin(v, margins);
  }

  public static void setFrameLayoutParam(View v, int width, int height) {
    setFrameLayoutParam(v, width, height, LEFT_TOP);
  }

  public static void setFrameLayoutParam(View v, int width, int height, int gravity) {
    FrameLayout.LayoutParams lp = LayoutHelper.createFL(width, height, gravity);
    v.setLayoutParams(lp);
  }

  public static void setMargin(View v, int... margins) {
    if (margins.length > 0) {
      ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
      switch (margins.length) {
        case 4:
          lp.bottomMargin = margins[3];
        case 3:
          lp.rightMargin = margins[2];
        case 2:
          lp.topMargin = margins[1];
        case 1:
          lp.leftMargin = margins[0];
          break;
        default:
          break;
      }
    }
  }

  public static void setPosition(View v, int gravity, int... margins) {
    flp(v).gravity = gravity;
    setMargin(v, margins);
  }

  public static FrameLayout.LayoutParams createFLWrap() {
    return createFL(WRAP, WRAP, LEFT_TOP);
  }

  public static FrameLayout.LayoutParams createFLWrap(int gravity) {
    return createFL(WRAP, WRAP, gravity);
  }

  public static FrameLayout.LayoutParams createFLMatch() {
    return createFL(MATCH, MATCH, FILL);
  }

  public static FrameLayout.LayoutParams createFL(int width, int height) {
    return createFL(width, height, LEFT_TOP);
  }

  public static FrameLayout.LayoutParams createFL(int width,
                                                  int height,
                                                  int gravity,
                                                  int... margins) {
    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height, gravity);
    switch (margins.length) {
      case 4:
        lp.bottomMargin = margins[3];
      case 3:
        lp.rightMargin = margins[2];
      case 2:
        lp.topMargin = margins[1];
      case 1:
        lp.leftMargin = margins[0];
        break;

      default:
        break;
    }
    return lp;
  }

  // LL
  public static final LinearLayout.LayoutParams LL_WRAP = new LinearLayout.LayoutParams(WRAP, WRAP);

  public static final LinearLayout.LayoutParams LL_MATCH =
      new LinearLayout.LayoutParams(MATCH, MATCH);
  public static final LinearLayout.LayoutParams LL_MATCH_WRAP =
      new LinearLayout.LayoutParams(MATCH, WRAP);
  public static final LinearLayout.LayoutParams LL_WRAP_MATCH =
      new LinearLayout.LayoutParams(WRAP, MATCH);

  public static LinearLayout.LayoutParams createLLWeight(int width, int height, float weight) {
    return new LinearLayout.LayoutParams(width, height, weight);
  }

  public static LinearLayout.LayoutParams createLL(int width, int height) {
    return createLL(width, height, LEFT_TOP);
  }

  public static LinearLayout.LayoutParams createLL(int width,
                                                   int height,
                                                   int gravity,
                                                   int... margins) {
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
    lp.gravity = gravity;
    switch (margins.length) {
      case 4:
        lp.bottomMargin = margins[3];
      case 3:
        lp.rightMargin = margins[2];
      case 2:
        lp.topMargin = margins[1];
      case 1:
        lp.leftMargin = margins[0];
        break;

      default:
        break;
    }
    return lp;
  }

  // TextView

  public static void setTextSize(TextView tv, float px) {
    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, px);
  }

  public static void setTextLayout(TextView tv,
                                   float textSizePx,
                                   int width,
                                   int height,
                                   int... margins) {
    setTextSize(tv, textSizePx);
    setLayout(tv, width, height, margins);
  }

  public static void setTextPosition(TextView tv, float textSizePx, int gravity, int... margins) {
    setTextSize(tv, textSizePx);
    setPosition(tv, gravity, margins);
  }

  public static Rect measureText(TextView tv) {
    Paint paint = tv.getPaint();
    Rect rect = new Rect();
    String text = tv.getText().toString();

    paint.getTextBounds(text, 0, text.length(), rect);

    return rect;
  }

  // Coordinate

  /**
   * Calculate offset between A and B, where A and B are already attached to
   * window. For example, if you want to translate (Ax,Ay) to (Bx,By), there's
   * Ax+OffsetX= Bx.
   *
   * @param a
   * @param b
   *
   * @return ret
   */
  public static Point calcOffset(View a, View b) {
    int[] loc = new int[2];
    a.getLocationInWindow(loc);
    Point p = new Point(loc[0], loc[1]);
    loc[0] = 0; // must clear
    loc[1] = 0; // must clear
    b.getLocationInWindow(loc);
    p.x -= loc[0];
    p.y -= loc[1];
    return p;
  }

  /**
   * Translate point from ViewA coordinate to ViewB coordinate.
   *
   * @return the same as the input point which is only for convenience
   */
  public static Point translatePoint(Point p, View a, View b) {
    int[] loc = new int[2];
    a.getLocationInWindow(loc);
    p.x += loc[0];
    p.y += loc[1];
    b.getLocationInWindow(loc);
    p.x -= loc[0];
    p.y -= loc[1];
    return p;
  }

  // Visual Copy/Transfer

  public static FrameLayout.LayoutParams visualCopy(View target, FrameLayout toParent) {
    Point p = LayoutHelper.calcOffset(target, toParent);
    return LayoutHelper.createFL(target.getWidth(),
                                 target.getHeight(),
                                 LayoutHelper.LEFT_TOP,
                                 p.x,
                                 p.y);
  }

  // Common

  public static boolean isAttachedToWindow(View v) {
    return v.getWindowToken() != null;
  }
}

