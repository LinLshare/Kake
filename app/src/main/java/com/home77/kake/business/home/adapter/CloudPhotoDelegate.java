package com.home77.kake.business.home.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
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
    return R.layout.layout_item_cloud_photo;
  }

  @Override
  public boolean isForViewType(CloudPhoto item, int position) {
    return !item.isTitle();
  }

  @Override
  public void convert(final ViewHolder holder, final CloudPhoto photo, int position) {
    holder.setText(R.id.name_text_view, photo.getName());
    int winWidth = Instance.of(UiData.class).winWidth();
    holder.getConvertView().getLayoutParams().height = winWidth / 3;
    holder.getConvertView().requestLayout();
    Picasso.with(holder.getConvertView().getContext())
           .load(photo.getImgurl() + "?x-oss-process=style/750_376")
           .resize(winWidth / 3, winWidth / 3)
           .error(R.drawable.ic_img_def)
           .placeholder(R.drawable.ic_img_def)
           .into(new Target() {
             @Override
             public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
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
    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.CLICK_CLOUD_PHOTO,
                                    Params.create(ParamsKey.CLOUD_PHOTO, photo)));
      }
    });
  }
}
