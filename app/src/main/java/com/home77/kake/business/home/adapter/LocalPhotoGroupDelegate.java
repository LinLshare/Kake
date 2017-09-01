package com.home77.kake.business.home.adapter;

import com.home77.kake.R;
import com.home77.kake.business.home.model.LocalPhoto;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;

/**
 * @author CJ
 */
public class LocalPhotoGroupDelegate implements ItemViewDelegate<LocalPhoto> {
  @Override
  public int getItemViewLayoutId() {
    return R.layout.layout_item_photo_group;
  }

  @Override
  public boolean isForViewType(LocalPhoto item, int position) {
    return item.isTitle();
  }

  @Override
  public void convert(ViewHolder holder, LocalPhoto photo, int position) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    holder.setText(R.id.group_name_text_view, simpleDateFormat.format(photo.getDate()));
  }
}
