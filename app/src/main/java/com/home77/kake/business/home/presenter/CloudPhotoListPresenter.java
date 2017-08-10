package com.home77.kake.business.home.presenter;

import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.CloudPhotoActivity;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.api.response.PhotoListResponse;
import com.home77.kake.common.api.service.CloudAlbumService;

import java.util.ArrayList;

/**
 * @author CJ
 */
public class CloudPhotoListPresenter extends BaseFragmentPresenter {
  private CloudAlbumService cloudAlbumService;
  private Album album;

  public CloudPhotoListPresenter(BaseView baseView, NavigateCallback navigateCallback) {
    super(baseView, navigateCallback);
    cloudAlbumService = Instance.of(CloudAlbumService.class);
  }

  public void setParam(Album album) {
    this.album = album;
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_BACK:
        navigateCallback.onNavigate(CloudPhotoActivity.EVENT_EXIST);
        break;
      case CLICK_MENU:
        baseView.onCommand(CmdType.SHOW_MENU, null, null);
        break;
      case VIEW_REFRESH:
        baseView.onCommand(CmdType.SHOW_ALBUM_INFO, Params.create(ParamsKey.ALBUM, album), null);
        getPhotoList();
        break;
    }
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
                  baseView.onCommand(CmdType.CLOUD_PHOTO_LIST_LOAD_SUCCESS,
                                     Params.create(ParamsKey.PHOTO_LIST,
                                                   photoListResponse.getData()),
                                     null);
                  break;
                case 400:
                  baseView.onCommand(CmdType.CLOUD_PHOTO_LIST_LOAD_SUCCESS,
                                     Params.create(ParamsKey.PHOTO_LIST, new ArrayList<>()),
                                     null);
                  break;
              }
            } else {
              baseView.onCommand(CmdType.CLOUD_PHOTO_LIST_LOAD_ERROR,
                                 Params.create(ParamsKey.MSG, "加载图片失败[-1]"),
                                 null);
            }
          }
        });
      }

      @Override
      public void onError(final String msg) {
        baseView.onCommand(CmdType.CLOUD_PHOTO_LIST_LOAD_ERROR,
                           Params.create(ParamsKey.MSG, "加载图片失败[0]"),
                           null);
      }
    });
  }
}
