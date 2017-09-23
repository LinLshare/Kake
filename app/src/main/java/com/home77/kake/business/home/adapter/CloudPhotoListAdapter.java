package com.home77.kake.business.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.home77.kake.business.home.model.CloudPhoto;
import com.home77.kake.common.widget.recyclerview.PinnedHeaderItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author CJ
 */
public class CloudPhotoListAdapter extends MultiItemTypeAdapter<CloudPhoto>
    implements PinnedHeaderItemDecoration.PinnedHeaderAdapter {
  public CloudPhotoListAdapter(Context context, List<CloudPhoto> datas) {
    super(context, datas);
    addItemViewDelegate(new CloudPhotoGroupDelegate());
    addItemViewDelegate(new CloudPhotoDelegate());
  }

  @Override
  public boolean isPinnedViewType(int viewType) {
    return getDatas().get(viewType).isTitle();
  }
}
