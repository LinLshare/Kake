package com.home77.common.ui.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;

import com.home77.common.base.util.CompatHelper;

public class DrawableHelper {
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @SuppressWarnings("deprecation")
  public static void setBackgroundDrawable(View v, Drawable backgroundDrawable) {
    if (v == null) {
      return;
    }
    if (CompatHelper.sdk(Build.VERSION_CODES.JELLY_BEAN)) {
      v.setBackground(backgroundDrawable);
    } else {
      v.setBackgroundDrawable(backgroundDrawable);
    }
  }

  /**
   * 创建一个图片选择器
   *
   * @param context
   * @param idNormal
   * @param idPressed
   * @param idFocused
   *
   * @return
   */
  public static StateListDrawable addStateDrawable(Context context,
                                                   int idNormal,
                                                   int idPressed,
                                                   int idFocused) {
    StateListDrawable sd = new StateListDrawable();
    Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
    Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
    Drawable focus = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);

    sd.addState(new int[] {android.R.attr.state_checked}, pressed);
    sd.addState(new int[] {android.R.attr.state_selected}, pressed);
    sd.addState(new int[] {android.R.attr.state_pressed}, pressed);
    sd.addState(new int[] {android.R.attr.state_checked}, normal);
    sd.addState(new int[] {android.R.attr.state_selected}, normal);
    sd.addState(new int[] {android.R.attr.state_pressed}, normal);

    sd.addState(new int[] {}, normal);
    return sd;
  }


  /**
   * 创建一个图片
   *
   * @param contentColor
   *     内部填充颜色
   * @param strokeColor
   *     描边颜色
   * @param radius
   *     圆角
   */
  public static GradientDrawable createDrawable(int contentColor, int strokeColor, int radius) {
    GradientDrawable drawable = new GradientDrawable(); // 生成Shape
    drawable.setGradientType(GradientDrawable.RECTANGLE); // 设置矩形
    drawable.setColor(contentColor);// 内容区域的颜色
    drawable.setStroke(SizeHelper.dp(1),
                       strokeColor); // 四周描边,描边后四角真正为圆角，不会出现黑色阴影。如果父窗体是可以滑动的，需要把父View设置setScrollCache(false)
    drawable.setCornerRadius(radius); // 设置四角都为圆角
    return drawable;
  }

  public static StateListDrawable getSelector(Drawable normalDraw, Drawable pressedDraw) {
    StateListDrawable stateListDrawable = new StateListDrawable();
    stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, pressedDraw);
    stateListDrawable.addState(new int[] {}, normalDraw);
    return stateListDrawable;
  }
}
