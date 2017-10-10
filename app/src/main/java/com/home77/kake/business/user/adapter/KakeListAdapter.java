package com.home77.kake.business.user.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.home77.common.base.pattern.Instance;
import com.home77.common.ui.model.UiData;
import com.home77.kake.R;
import com.home77.kake.business.home.PinoWebPageActivity;
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
  protected void convert(final ViewHolder holder, final Kake kake, int position) {
    holder.setText(R.id.name_text_view, kake.getName());
    holder.setText(R.id.user_text_view, kake.getShip_user().getName());
    holder.setText(R.id.date_text_view, kake.getCreated_at().substring(0, 10));
    holder.setText(R.id.read_count_text_view, kake.getView() + "");
    int winWidth = Instance.of(UiData.class).winWidth();
    if (!TextUtils.isEmpty(kake.getCover())) {
      Picasso.with(holder.getConvertView().getContext())
             .load(kake.getCover() + "?x-oss-process=style/750_376")
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
    }
    Picasso.with(holder.getConvertView().getContext())
           .load(kake.getShip_user().getAvatar() + "?x-oss-process=style/224_224")
           .into((ImageView) holder.getView(R.id.user_image_view));
    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        PinoWebPageActivity.start(holder.getConvertView().getContext(), kake.getName(), kake.getUrl());
      }
    });
  }
}
