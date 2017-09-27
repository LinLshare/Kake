package com.home77.kake.business.home.presenter;

import android.text.TextUtils;

import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.HomeActivity;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.api.response.AlbumListResponse;
import com.home77.kake.common.api.response.Response;
import com.home77.kake.common.api.service.CloudAlbumService;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author CJ
 */
public class CloudAlbumListPresenter extends BaseFragmentPresenter {

  private static final String TAG = CloudAlbumListPresenter.class.getSimpleName();
  private CloudAlbumService cloudAlbumService;

  public CloudAlbumListPresenter(BaseView baseView, NavigateCallback navigateCallback) {
    super(baseView, navigateCallback);
    cloudAlbumService = Instance.of(CloudAlbumService.class);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_CREATE_ALBUM_DIALOG_OK:
        String albumName = params.get(ParamsKey.ALBUM_NAME, "");
        createAlbum(albumName);
        break;
      case VIEW_REFRESH:
        getAlbumList();
        break;
      case CLICK_OK_DELETE_ALBUM_DIALOG: {
        Album album = params.get(ParamsKey.ALBUM);
        cloudAlbumService.deleteAlbum(album.getId(), new URLFetcher.Delegate() {
          @Override
          public void onSuccess(URLFetcher source) {
            Response response = source.responseClass(Response.class);
            if (response != null && response.getCode() == 200) {
              baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, "删除成功"), null);
              getAlbumList();
            } else {
              baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, "删除失败"), null);
            }
          }

          @Override
          public void onError(String msg) {
            baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, "删除失败"), null);
          }
        });
      }
      break;
    }
  }

  @Override
  public void start(Params params) {
    super.start(params);
    if (App.isLogin()) {
      getAlbumList();
    } else {
      Toast.showShort("客官，登录后才可以查看云相册~");
      navigateCallback.onNavigate(HomeActivity.NAVIGATE_TO_USER, null);
    }
  }

  private void createAlbum(final String albumName) {
    if (TextUtils.isEmpty(albumName)) {
      baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, "相册名不应为空"), null);
      return;
    }
    // handle creating ui
    baseView.onCommand(CmdType.CLOUD_ALBUM_CREATING, null, null);
    // create album
    cloudAlbumService.createAlbum(App.globalData().getInt(GlobalData.KEY_USER_ID, 0),
                                  albumName,
                                  new URLFetcher.Delegate() {
                                    @Override
                                    public void onSuccess(URLFetcher source) {
                                      final Response response =
                                          source.responseClass(Response.class);
                                      if (response != null) {
                                        switch (response.getCode()) {
                                          case 200:
                                            baseView.onCommand(CmdType.CLOUD_ALBUM_CREATE_SUCCESS,
                                                               Params.create(ParamsKey.MSG,
                                                                             response.getMessage()),
                                                               null);
                                            getAlbumList();
                                            break;
                                          case 400:
                                            baseView.onCommand(CmdType.CLOUD_ALBUM_CREATE_ERROR,
                                                               Params.create(ParamsKey.MSG, "相册为空"),
                                                               null);
                                            break;
                                        }
                                      }
                                    }

                                    @Override
                                    public void onError(final String msg) {
                                      baseView.onCommand(CmdType.CLOUD_ALBUM_LOAD_ERROR,
                                                         Params.create(ParamsKey.MSG, "网络异常"),
                                                         null);

                                    }
                                  });
  }

  private void getAlbumList() {
    cloudAlbumService.getAlbumList(App.globalData().getInt(GlobalData.KEY_USER_ID, 0),
                                   new URLFetcher.Delegate() {
                                     @Override
                                     public void onSuccess(URLFetcher source) {
                                       final AlbumListResponse albumListResponse =
                                           source.responseClass(AlbumListResponse.class);
                                       if (albumListResponse != null) {
                                         switch (albumListResponse.getCode()) {
                                           case 200:
                                             Collections.reverse(albumListResponse.getData());
                                             baseView.onCommand(CmdType.CLOUD_ALBUM_LOAD_SUCCESS,
                                                                Params.create(ParamsKey.ALBUM_LIST,
                                                                              albumListResponse.getData()),
                                                                null);
                                             break;
                                           case 400:
                                             baseView.onCommand(CmdType.CLOUD_ALBUM_LOAD_SUCCESS,
                                                                Params.create(ParamsKey.ALBUM_LIST,
                                                                              new ArrayList<>()),
                                                                null);
                                             break;
                                         }
                                       }
                                     }

                                     @Override
                                     public void onError(final String msg) {
                                       baseView.onCommand(CmdType.CLOUD_ALBUM_LOAD_ERROR,
                                                          Params.create(ParamsKey.MSG, "网络异常"),
                                                          null);

                                     }
                                   });
  }
}
