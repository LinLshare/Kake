package com.home77.kake.business.home.adapter;

import android.graphics.BitmapFactory;

import com.home77.common.base.collection.Params;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author CJ
 */
public class LocalPhotoDelegate implements ItemViewDelegate<Photo> {
  @Override
  public int getItemViewLayoutId() {
    return R.layout.layout_item_photo;
  }

  @Override
  public boolean isForViewType(Photo item, int position) {
    return item.getType() == Photo.TYPE_NORMAL;
  }

  @Override
  public void convert(final ViewHolder holder, Photo photo, int position) {
    holder.setText(R.id.size_image_view, photo.getSize());
    holder.setText(R.id.name_text_view, photo.getName());
    holder.setImageBitmap(R.id.photo_image_view,
                          BitmapFactory.decodeByteArray(photo.getThumbnail(),
                                                        0,
                                                        photo.getThumbnail().length));
    App.eventBus()
       .post(new BroadCastEvent(BroadCastEventConstant.CLICK_LOCAL_PHOTO,
                                Params.create(ParamsKey.PHOTO, photo)));
  }
}
