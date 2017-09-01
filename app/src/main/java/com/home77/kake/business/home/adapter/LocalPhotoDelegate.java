package com.home77.kake.business.home.adapter;

import android.graphics.BitmapFactory;
import android.view.View;

import com.home77.common.base.collection.Params;
import com.home77.common.base.util.UnitHelper;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.model.LocalPhoto;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author CJ
 */
public class LocalPhotoDelegate implements ItemViewDelegate<LocalPhoto> {
  @Override
  public int getItemViewLayoutId() {
    return R.layout.layout_item_photo;
  }

  @Override
  public boolean isForViewType(LocalPhoto item, int position) {
    return true;
  }

  @Override
  public void convert(final ViewHolder holder, final LocalPhoto photo, int position) {
    holder.setText(R.id.size_image_view, UnitHelper.formatBytesInByte(photo.getSize(), true) + "");
    holder.setText(R.id.name_text_view, photo.getName());
    holder.setImageBitmap(R.id.photo_image_view, BitmapFactory.decodeFile(photo.getPath()));
    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.CLICK_LOCAL_PHOTO,
                                    Params.create(ParamsKey.LOCAL_PHOTO, photo)));
      }
    });
  }
}
