package com.home77.kake.business.home.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
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

  public CloudAlbumListPresenter(BaseView baseView) {
    super(baseView);
    cloudAlbumService = Instance.of(CloudAlbumService.class);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    getAlbumList();
    return view;
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
                                                               Params.create(ParamsKey.MSG,
                                                                             response.getMessage()),
                                                               null);
                                            break;
                                        }
                                      }
                                    }

                                    @Override
                                    public void onError(final String msg) {
                                      baseView.onCommand(CmdType.CLOUD_ALBUM_LOAD_ERROR,
                                                         Params.create(ParamsKey.MSG, msg),
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
                                                          Params.create(ParamsKey.MSG, msg),
                                                          null);

                                     }
                                   });
  }
}
