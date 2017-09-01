package com.home77.kake.business.home.adapter;

import android.content.Context;

import com.home77.kake.business.home.model.LocalPhoto;
import com.home77.kake.common.widget.recyclerview.PinnedHeaderItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * @author CJ
 */
public class LocalPhotoListAdapter extends MultiItemTypeAdapter<LocalPhoto>
    implements PinnedHeaderItemDecoration.PinnedHeaderAdapter {
  public LocalPhotoListAdapter(Context context, List<LocalPhoto> datas) {
    super(context, datas);
    addItemViewDelegate(new LocalPhotoGroupDelegate());
    addItemViewDelegate(new LocalPhotoDelegate());
  }

  @Override
  public boolean isPinnedViewType(int viewType) {
    return getDatas().get(viewType).isTitle();
  }
}
