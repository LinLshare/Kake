package com.home77.kake.business.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;

import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
import com.home77.kake.R;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.business.theta.LoadFileListResult;
import com.home77.kake.business.theta.ThetaService;
import com.home77.kake.common.utils.BitmapHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import static com.home77.kake.business.theta.ThetaService.PARAMS_KEY_FILE_LIST;
import static com.home77.kake.business.theta.ThetaService.PARAMS_KEY_IMAGE;

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
    holder.setText(R.id.size_image_view, photo.getSize() / (1024 * 1024) + " M");

    Instance.of(ThetaService.class)
            .loadImage(photo.getFileUrl(), new ThetaService.OnThetaServiceCallback() {
              @Override
              public void onThetaServiceSuccess(Params params) {
                String imgStr = params.get(PARAMS_KEY_IMAGE);
                byte[] bytes = imgStr.getBytes();
                holder.setImageBitmap(R.id.photo_image_view,
                                      BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                //                LoadFileListResult loadFileListResult =
                //                    params.get(ThetaService.PARAMS_KEY_FILE_LIST, new LoadFileListResult());
                //                byte[] imageBytes =
                //                    Base64.decode(loadFileListResult.getEntries().get(0).getThumbnail(),
                //                                  Base64.DEFAULT);
                //                Bitmap decodedImage =
                //                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                //                holder.setImageBitmap(R.id.photo_image_view, decodedImage);
              }

              @Override
              public void onThetaServiceError(String msg) {
              }
            });
    holder.setText(R.id.name_text_view, photo.getName() + "");

  }
}
