package com.home77.kake.common.widget.bottombar;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

public class ImageBottomItem extends ImageView implements IBottomItem {

  public ImageBottomItem(Context context, @DrawableRes int iconRes) {
    super(context);
    setScaleType(ImageView.ScaleType.FIT_CENTER);
    setImageResource(iconRes);
  }

  @Override
  public void shrink(Animation shrinkAnimation) {
    this.startAnimation(shrinkAnimation);
  }

  @Override
  public View getBottomView() {
    return this;
  }
}
