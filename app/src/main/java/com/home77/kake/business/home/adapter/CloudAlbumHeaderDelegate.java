package com.home77.kake.business.home.adapter;

import android.view.View;

import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author CJ
 */
public class CloudAlbumHeaderDelegate implements ItemViewDelegate<Album> {
  @Override
  public int getItemViewLayoutId() {
    return R.layout.list_item_add_album;
  }

  @Override
  public boolean isForViewType(Album item, int position) {
    return position == 0;
  }

  @Override
  public void convert(ViewHolder holder, final Album album, int position) {
    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_UPLOAD_ALBUM, null));
      }
    });
  }
}
