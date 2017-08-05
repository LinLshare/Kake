package com.home77.kake.common.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.home77.common.base.debug.DLog;


public class CloudAlbumGridItemDecoration extends RecyclerView.ItemDecoration {

  private static final String TAG = CloudAlbumGridItemDecoration.class.getSimpleName();
  private int outerSpace;
  private final int innerSpace;

  public CloudAlbumGridItemDecoration(int outerSpace, int innerSpace) {
    this.outerSpace = outerSpace;
    this.innerSpace = innerSpace;
  }

  @Override
  public void getItemOffsets(Rect outRect,
                             View view,
                             RecyclerView parent,
                             RecyclerView.State state) {
    GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
    int itemCount = parent.getAdapter().getItemCount();
    int spanCount = gridLayoutManager.getSpanCount();
    if (itemCount == 0 || spanCount == 0) {
      return;
    }
    int position = parent.getChildLayoutPosition(view);
    int row = position / spanCount;
    int column = position % spanCount;
    DLog.v(TAG, "%d: (%d, %d)", position, row, column);
    if (row == 0) { // first row
      if (column == 0) {
        outRect.left = outerSpace;
        outRect.top = outerSpace;
      } else if (column == spanCount - 1) {
        outRect.left = innerSpace;
        outRect.top = outerSpace;
        outRect.right = outerSpace;
      } else {
        outRect.left = innerSpace;
        outRect.top = outerSpace;
      }
    } else if (row == (itemCount - 1) / spanCount) { //lastRow
      if (column == 0) {
        outRect.left = outerSpace;
        outRect.top = innerSpace;
      } else if (column == spanCount - 1) {
        outRect.left = innerSpace;
        outRect.top = innerSpace;
        outRect.right = outerSpace;
      } else {
        outRect.left = innerSpace;
        outRect.top = innerSpace;
      }
      outRect.bottom = outerSpace;
    } else { // middle row
      if (column == 0) {
        outRect.top = innerSpace;
        outRect.left = outerSpace;
      } else if (column == spanCount - 1) {
        outRect.top = innerSpace;
        outRect.left = innerSpace;
        outRect.right = outerSpace;
      } else {
        outRect.top = innerSpace;
        outRect.left = innerSpace;
      }
    }
  }
}
