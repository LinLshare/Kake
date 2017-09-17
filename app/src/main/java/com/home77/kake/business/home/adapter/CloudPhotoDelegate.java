package com.home77.kake.business.home.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.home77.common.base.pattern.Instance;
import com.home77.common.base.util.UnitHelper;
import com.home77.common.ui.model.UiData;
import com.home77.kake.R;
import com.home77.kake.business.home.model.CloudPhoto;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author CJ
 */
public class CloudPhotoDelegate implements ItemViewDelegate<CloudPhoto> {
  @Override
  public int getItemViewLayoutId() {
    return R.layout.layout_item_photo;
  }

  @Override
  public boolean isForViewType(CloudPhoto item, int position) {
    return !item.isTitle();
  }

  @Override
  public void convert(final ViewHolder holder, final CloudPhoto photo, int position) {
    holder.setText(R.id.name_text_view, photo.getName());

    int winWidth = Instance.of(UiData.class).winWidth();
    Picasso.with(holder.getConvertView().getContext())
           .load(photo.getImgurl())
           .resize(winWidth / 2, winWidth / 2)
           //           .centerCrop()
           .error(R.drawable.ic_img_def)
           .placeholder(R.drawable.ic_img_def)
           .into(new Target() {
             @Override
             public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
               holder.setText(R.id.size_image_view,
                              UnitHelper.formatBytesInByte(
                                  bitmap.getRowBytes() * bitmap.getHeight(), true) + "");
               holder.setImageBitmap(R.id.photo_image_view, bitmap);
             }

             @Override
             public void onBitmapFailed(Drawable errorDrawable) {
               holder.setImageResource(R.id.photo_image_view, R.drawable.ic_img_def);
             }

             @Override
             public void onPrepareLoad(Drawable placeHolderDrawable) {
               holder.setImageResource(R.id.photo_image_view, R.drawable.ic_img_def);
             }
           });
  }
}
