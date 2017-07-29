package com.home77.kake.common.widget.bottombar;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home77.common.ui.util.SizeHelper;
import com.home77.kake.R;

public class NormalBottomItem extends LinearLayout implements IBottomItem {

  private final ImageView iconImg;

  public NormalBottomItem(Context context, @DrawableRes int iconRes, @StringRes int descRes) {
    super(context);
    setGravity(Gravity.CENTER);
    setOrientation(LinearLayout.VERTICAL);

    iconImg = new ImageView(context);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(SizeHelper.dp(32), SizeHelper.dp(32));
    iconImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
    iconImg.setImageResource(iconRes);
    TextView descText = new TextView(context);
    descText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                         context.getResources().getDimensionPixelSize(R.dimen.fontSizeF4));
    descText.setTextColor(context.getResources().getColorStateList(R.color.colorC5));
    descText.setText(descRes);

    addView(iconImg, layoutParams);
    addView(descText,
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                          ViewGroup.LayoutParams.WRAP_CONTENT));
  }

  @Override
  public void shrink(Animation shrinkAnimation) {
    iconImg.startAnimation(shrinkAnimation);
  }

  @Override
  public View getBottomView() {
    return this;
  }
}
