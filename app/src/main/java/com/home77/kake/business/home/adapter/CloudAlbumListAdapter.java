package com.home77.kake.business.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.home77.common.base.pattern.Instance;
import com.home77.common.ui.model.UiData;
import com.home77.kake.common.api.response.Album;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author CJ
 */
public class CloudAlbumListAdapter extends MultiItemTypeAdapter<Album> {
  private final int widthAndHeight;

  public CloudAlbumListAdapter(Context context, List<Album> datas, int widthAndHeight) {
    super(context, datas);
    this.widthAndHeight = widthAndHeight;
    addItemViewDelegate(new CloudAlbumHeaderDelegate());
    addItemViewDelegate(new CloudAlbumNormalDelegate());
  }

  @Override
  public void onViewHolderCreated(ViewHolder holder, View itemView) {
    super.onViewHolderCreated(holder, itemView);
    itemView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
      @Override
      public void onViewAttachedToWindow(View v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = widthAndHeight;
        layoutParams.width = widthAndHeight;
        v.requestLayout();
      }

      @Override
      public void onViewDetachedFromWindow(View v) {
      }
    });
  }
}
