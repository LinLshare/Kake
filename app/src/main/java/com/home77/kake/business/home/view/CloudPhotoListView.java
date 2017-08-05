package com.home77.kake.business.home.view;

import com.home77.kake.base.BaseView;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.business.home.presenter.CloudAlbumListPresenter;
import com.home77.kake.business.home.presenter.CloudPhotoListPresenter;

import java.util.List;

/**
 * @author CJ
 */
public interface CloudPhotoListView extends BaseView<CloudPhotoListPresenter> {
  void onShowSubMenu();

  void onPhotoListUpdated(List<Photo> photoList);

  void onPhotoListUpdateError(String msg);

  void onAlbumNameChanged(String name);
}
