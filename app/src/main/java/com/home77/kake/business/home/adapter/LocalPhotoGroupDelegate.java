package com.home77.kake.business.home.adapter;

import com.home77.kake.R;
import com.home77.kake.business.home.model.Photo;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author CJ
 */
public class LocalPhotoGroupDelegate implements ItemViewDelegate<Photo> {
  @Override
  public int getItemViewLayoutId() {
    return R.layout.layout_item_photo_group;
  }

  @Override
  public boolean isForViewType(Photo item, int position) {
    return item.getType() == Photo.TYPE_GROUP;
  }

  @Override
  public void convert(ViewHolder holder, Photo photo, int position) {
    holder.setText(R.id.group_name_text_view, photo.getDateTimeZone());
  }
}
