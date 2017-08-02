package com.home77.kake.business.home.view;

import com.home77.kake.base.BaseView;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.business.home.presenter.LocalPhotoPresenter;
import com.home77.kake.business.theta.LoadFileListResult;
import com.theta360.v2.network.ImageInfo;

import java.util.List;

/**
 * @author CJ
 */
public interface LocalPhotoView extends BaseView<LocalPhotoPresenter> {
  void onRefresh();

  void onIdle(List<Photo> imageInfoList);

  void onEmpty();

  void onError();

}
