package com.home77.kake.business.home.presenter;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.home.CloudPhotoActivity;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.business.home.view.CloudPhotoListView;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.api.response.PhotoListResponse;
import com.home77.kake.common.api.service.CloudAlbumService;

import java.util.ArrayList;

/**
 * @author CJ
 */
public class CloudPhotoListPresenter extends BasePresenter<CloudPhotoListView> {
  private CloudAlbumService cloudAlbumService;
  private Album album;

  public CloudPhotoListPresenter(CloudPhotoListView attachedView) {
    super(attachedView);
    cloudAlbumService = Instance.of(CloudAlbumService.class);
  }

  public void setParam(Album album) {
    this.album = album;
  }

  @Override
  public void start() {
  }

  @Override
  public void onCreateView() {
    attachedView.onAlbumNameChanged(album.getName());
    getPhotoList();
  }

  private void getPhotoList() {
    cloudAlbumService.getPhotoList(album.getId(), new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        final PhotoListResponse photoListResponse = source.responseClass(PhotoListResponse.class);
        BaseHandler.post(new Runnable() {
          @Override
          public void run() {
            if (photoListResponse != null) {
              switch (photoListResponse.getCode()) {
                case 200:
                  attachedView.onPhotoListUpdated(photoListResponse.getData());
                  break;
                case 400:
                  attachedView.onPhotoListUpdated(new ArrayList<Photo>());
                  break;
              }
            } else {
              attachedView.onPhotoListUpdateError(photoListResponse.getMessage());
            }
          }
        });
      }

      @Override
      public void onError(final String msg) {
        BaseHandler.post(new Runnable() {
          @Override
          public void run() {
            attachedView.onPhotoListUpdateError(msg);
          }
        });
      }
    });
  }

  public void onMenuNavClick() {
    attachedView.onShowSubMenu();
  }

  public void onRefresh() {
    getPhotoList();
  }

  public void onBackNavClick() {
    CloudPhotoActivity.NavEvent navEvent =
        new CloudPhotoActivity.NavEvent(this, CloudPhotoActivity.NavEvent.CLICK_BACK, null);
    App.eventBus().post(navEvent);
  }
}
