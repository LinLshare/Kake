package com.home77.kake.common.widget.recyclerview;

import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DefaultLinearItemDecoration extends RecyclerView.ItemDecoration {

  private int space;
  private int[] ignorePosition;

  public DefaultLinearItemDecoration(@DimenRes int space, int... ignorePosition) {
    this.space = space;
    this.ignorePosition = ignorePosition;
  }

  @Override
  public void getItemOffsets(Rect outRect,
                             View view,
                             RecyclerView parent,
                             RecyclerView.State state) {
    for (int position : ignorePosition) {
      if (parent.getChildAdapterPosition(view) == position)
        /*if (parent.indexOfChild(view) == position)*/ {
        return;
      }
    }
    outRect.top += parent.getResources().getDimensionPixelOffset(space);
  }
}
