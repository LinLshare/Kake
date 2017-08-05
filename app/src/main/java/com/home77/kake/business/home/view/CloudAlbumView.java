package com.home77.kake.business.home.view;

import com.home77.kake.base.BaseView;
import com.home77.kake.business.home.presenter.CloudAlbumPresenter;
import com.home77.kake.common.api.response.Album;

import java.util.List;

/**
 * @author CJ
 */
public interface CloudAlbumView extends BaseView<CloudAlbumPresenter> {
  void onAlbumUpdated(List<Album> albumList);

  void onAlbumError(String msg);

  void onAlbumCreating(String albumName);

  void onAlbumCreated(String albumName, String msg);

  void onAlbumCreateFail(String albumName, String error);
}
