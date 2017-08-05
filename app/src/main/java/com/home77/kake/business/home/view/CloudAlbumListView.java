package com.home77.kake.business.home.view;

import com.home77.kake.base.BaseView;
import com.home77.kake.business.home.presenter.CloudAlbumListPresenter;
import com.home77.kake.common.api.response.Album;

import java.util.List;

/**
 * @author CJ
 */
public interface CloudAlbumListView extends BaseView<CloudAlbumListPresenter> {
  void onAlbumUpdated(List<Album> albumList);

  void onAlbumError(String msg);

  void onAlbumCreating(String albumName);

  void onAlbumCreated(String albumName, String msg);

  void onAlbumCreateFail(String albumName, String error);
}
