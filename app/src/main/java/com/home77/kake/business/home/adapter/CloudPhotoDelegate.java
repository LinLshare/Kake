package com.home77.kake.business.home.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
import com.home77.common.base.util.UnitHelper;
import com.home77.common.ui.model.UiData;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.model.CloudPhoto;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
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
           .resize(winWidth / 2, 0)
           .centerCrop()
           .error(R.drawable.image_placeholder)
           .placeholder(R.drawable.image_placeholder)
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
             }

             @Override
             public void onPrepareLoad(Drawable placeHolderDrawable) {
             }
           });

    //    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
    //      @Override
    //      public void onClick(View v) {
    //        App.eventBus()
    //           .post(new BroadCastEvent(BroadCastEventConstant.CLICK_LOCAL_PHOTO,
    //                                    Params.create(ParamsKey.LOCAL_PHOTO, photo)));
    //      }
    //    });
  }
}
