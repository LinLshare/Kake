package com.home77.kake.business.user.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.home77.common.base.pattern.Instance;
import com.home77.common.base.util.UnitHelper;
import com.home77.common.ui.model.UiData;
import com.home77.kake.R;
import com.home77.kake.business.user.model.Kake;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author CJ
 */
public class KakeListAdapter extends CommonAdapter<Kake> {
  public KakeListAdapter(Context context, List<Kake> datas) {
    super(context, R.layout.item_kake, datas);
  }

  @Override
  protected void convert(final ViewHolder holder, Kake kake, int position) {
    holder.setText(R.id.name_text_view, kake.getName());
    int winWidth = Instance.of(UiData.class).winWidth();
    Picasso.with(holder.getConvertView().getContext())
           .load(kake.getUrl())
           .resize(winWidth / 2, winWidth / 2)
           .placeholder(R.drawable.user_avatar_def)
           .error(R.drawable.user_avatar_def)
           .into(new Target() {
             @Override
             public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
               holder.setImageBitmap(R.id.photo_image_view, bitmap);
             }

             @Override
             public void onBitmapFailed(Drawable errorDrawable) {
             }

             @Override
             public void onPrepareLoad(Drawable placeHolderDrawable) {
             }
           });
  }
}
