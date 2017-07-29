package com.home77.kake.common.widget.bottombar;

import android.view.View;
import android.view.animation.Animation;

public interface IBottomItem {
  void shrink(Animation shrinkAnimation);

  View getBottomView();
}
