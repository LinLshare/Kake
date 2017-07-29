package com.home77.kake.business.home.adapter;

import android.content.Context;

import com.home77.kake.business.home.model.Photo;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * @author CJ
 */
public class LocalPhotoListAdapter extends MultiItemTypeAdapter<Photo> {
  public LocalPhotoListAdapter(Context context, List<Photo> datas) {
    super(context, datas);
    addItemViewDelegate(new LocalPhotoGroupDelegate());
    addItemViewDelegate(new LocalPhotoDelegate());
  }
}
