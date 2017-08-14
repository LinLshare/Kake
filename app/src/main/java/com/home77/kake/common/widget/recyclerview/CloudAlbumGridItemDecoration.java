package com.home77.kake.common.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class CloudAlbumGridItemDecoration extends RecyclerView.ItemDecoration {

  private static final String TAG = CloudAlbumGridItemDecoration.class.getSimpleName();
  private final int space;

  public CloudAlbumGridItemDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(Rect outRect,
                             View view,
                             RecyclerView parent,
                             RecyclerView.State state) {
    GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
    //判断总的数量是否可以整除
    int totalCount = layoutManager.getItemCount();
    int surplusCount = totalCount % layoutManager.getSpanCount();
    int childPosition = parent.getChildAdapterPosition(view);
    if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {//竖直方向的
      if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
        //后面几项需要bottom
        outRect.bottom = space;
      } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
        outRect.bottom = space;
      }
      if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要右边
        outRect.right = space;
      }
      outRect.top = space;
      outRect.left = space;
    } else {
      if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
        //后面几项需要右边
        outRect.right = space;
      } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
        outRect.right = space;
      }
      if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {//被整除的需要下边
        outRect.bottom = space;
      }
      outRect.top = space;
      outRect.left = space;
    }
  }
}
