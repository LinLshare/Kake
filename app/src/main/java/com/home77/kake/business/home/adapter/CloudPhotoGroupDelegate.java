package com.home77.kake.business.home.adapter;

import com.home77.kake.R;
import com.home77.kake.business.home.model.CloudPhoto;
import com.home77.kake.common.utils.DateHelper;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;

/**
 * @author CJ
 */
public class CloudPhotoGroupDelegate implements ItemViewDelegate<CloudPhoto> {
  @Override
  public int getItemViewLayoutId() {
    return R.layout.layout_item_photo_group;
  }

  @Override
  public boolean isForViewType(CloudPhoto item, int position) {
    return item.isTitle();
  }

  @Override
  public void convert(ViewHolder holder, CloudPhoto photo, int position) {
    long millis = DateHelper.toMillis(photo.getUpdated_at());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    holder.setText(R.id.group_name_text_view, simpleDateFormat.format(millis));
  }
}
