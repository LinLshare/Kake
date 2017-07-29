package com.home77.kake.business.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.home77.kake.R;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.common.utils.BitmapHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
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
    Context context = holder.getConvertView().getContext();
    Picasso.with(context).load(photo.getImgurl()).into(new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        holder.setImageBitmap(R.id.photo_image_view, bitmap);
        holder.setText(R.id.size_image_view, BitmapHelper.sizeOf(bitmap) / (1024 * 1024) + " M");
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
      }
    });
    holder.setText(R.id.name_text_view, photo.getRename() + "");

  }
}
