package com.home77.kake.common.widget.recyclerview;

import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class DefaultGridItemDecoration extends RecyclerView.ItemDecoration {

  private int space;

  public DefaultGridItemDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(Rect outRect,
                             View view,
                             RecyclerView parent,
                             RecyclerView.State state) {
    int spacePx =/* parent.getResources().getDimensionPixelOffset(space)*/ space;
    outRect.left = spacePx;
    outRect.bottom = spacePx;
    GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
    if (parent.getChildLayoutPosition(view) % gridLayoutManager.getSpanCount() == 0) { //最左边
      outRect.left = 0;
    }
  }
}
