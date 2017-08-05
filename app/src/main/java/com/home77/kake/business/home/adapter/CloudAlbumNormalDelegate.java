package com.home77.kake.business.home.adapter;

import android.view.View;

import com.home77.common.base.collection.Params;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.home77.kake.common.event.ParamKey;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author CJ
 */
public class CloudAlbumNormalDelegate implements ItemViewDelegate<Album> {
  @Override
  public int getItemViewLayoutId() {
    return R.layout.list_item_album;
  }

  @Override
  public boolean isForViewType(Album item, int position) {
    return position != 0;
  }

  @Override
  public void convert(ViewHolder holder, final Album album, int position) {
    holder.setText(R.id.album_name_text_view, album.getName());
    holder.setImageResource(R.id.album_cover_image_view, R.drawable.ic_img_def);
    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.ACTIVITY_CLOUD_PHOTO_LIST,
                                    Params.create(ParamKey.ALBUM, album)));
      }
    });
  }
}
