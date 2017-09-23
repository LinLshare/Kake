package com.home77.kake.business.user.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.home77.common.base.pattern.Instance;
import com.home77.common.ui.model.UiData;
import com.home77.kake.R;
import com.home77.kake.business.user.model.Kake;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    holder.setText(R.id.user_text_view, kake.getShip_user().getName());
    holder.setText(R.id.date_text_view, kake.getCreated_at() + "");
    holder.setText(R.id.read_count_text_view, kake.getView() + "");
    int winWidth = Instance.of(UiData.class).winWidth();
    Picasso.with(holder.getConvertView().getContext())
           .load(kake.getUrl())
           .resize(winWidth / 2, winWidth / 2)
           .placeholder(R.drawable.ic_img_def)
           .error(R.drawable.ic_img_def)
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
    Picasso.with(holder.getConvertView().getContext())
           .load(kake.getShip_user().getAvatar())
           .into((ImageView) holder.getView(R.id.user_image_view));
  }
}
