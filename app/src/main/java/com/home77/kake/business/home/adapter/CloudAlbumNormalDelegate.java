package com.home77.kake.business.home.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
import com.home77.common.ui.model.UiData;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author CJ
 */
public class CloudAlbumNormalDelegate implements ItemViewDelegate<Album> {
  @Override
  public int getItemViewLayoutId() {
    return R.layout.list_item_album;
  }

  @Override
  public boolean isForViewType(Album item, int position) {
    return position != 0;
  }

  @Override
  public void convert(ViewHolder holder, final Album album, int position) {
    holder.setText(R.id.album_name_text_view, album.getName());
    //    holder.setText(R.id.photo_count_text_view,album.get);
    String imgUrl = album.getCover();
    if (!TextUtils.isEmpty(imgUrl)) {
      ImageView coverImageView = holder.getView(R.id.album_cover_image_view);
      Picasso.with(holder.getConvertView().getContext())
             .load(imgUrl)
             .placeholder(R.drawable.ic_img_def)
             .resize(Instance.of(UiData.class).winWidth() / 3,
                     Instance.of(UiData.class).winWidth() / 3)
             .error(R.drawable.ic_img_def)
             .into(coverImageView);
    } else {
      holder.setImageResource(R.id.album_cover_image_view, R.drawable.ic_img_def);
    }

    holder.setVisible(R.id.corner_image_view, !TextUtils.isEmpty(album.getPanourl()));

    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.ACTIVITY_CLOUD_PHOTO_LIST,
                                    Params.create(ParamsKey.ALBUM, album)));
      }
    });
    holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.LONG_CLICK_CLOUD_ALBUM,
                                    Params.create(ParamsKey.ALBUM, album)));
        return true;
      }
    });
  }
}
