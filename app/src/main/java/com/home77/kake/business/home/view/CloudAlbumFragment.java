package com.home77.kake.business.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home77.kake.base.BaseFragment;
import com.home77.kake.business.home.presenter.CloudAlbumPresenter;

/**
 * @author CJ
 */
public class CloudAlbumFragment extends BaseFragment<CloudAlbumPresenter>
    implements CloudAlbumView {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }
}
