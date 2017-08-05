package com.home77.kake.business.home.presenter;

import android.text.TextUtils;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.home.view.CloudAlbumListView;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.api.response.AlbumListResponse;
import com.home77.kake.common.api.response.Response;
import com.home77.kake.common.api.service.CloudAlbumService;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author CJ
 */
public class CloudAlbumListPresenter extends BasePresenter<CloudAlbumListView> {

  private static final String TAG = CloudAlbumListPresenter.class.getSimpleName();
  private CloudAlbumService cloudAlbumService;

  public CloudAlbumListPresenter(CloudAlbumListView attachedView) {
    super(attachedView);
    cloudAlbumService = Instance.of(CloudAlbumService.class);
  }

  @Override
  public void start() {
  }

  public void onRefresh() {
    getAlbumList();
  }

  public void onUploadAlbumDialogClickConfirm(final String albumName) {
    if (TextUtils.isEmpty(albumName)) {
      attachedView.toast("相册名不应为空");
      return;
    }
    // handle creating ui
    attachedView.onAlbumCreating(albumName);
    // create album
    cloudAlbumService.createAlbum(App.globalData().getInt(GlobalData.KEY_USER_ID, 0),
                                  albumName,
                                  new URLFetcher.Delegate() {
                                    @Override
                                    public void onSuccess(URLFetcher source) {
                                      final Response response =
                                          source.responseClass(Response.class);
                                      if (response != null) {
                                        BaseHandler.post(new Runnable() {
                                          @Override
                                          public void run() {
                                            switch (response.getCode()) {
                                              case 200:
                                                attachedView.onAlbumCreated(albumName,
                                                                            response.getMessage() +
                                                                            "");
                                                getAlbumList();
                                                break;
                                              case 400:
                                                attachedView.onAlbumCreateFail(albumName,
                                                                               response.getMessage() +
                                                                               "");
                                                break;
                                            }
                                          }
                                        });

                                      }
                                    }

                                    @Override
                                    public void onError(final String msg) {
                                      BaseHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                          attachedView.onAlbumCreateFail(albumName, msg + "");
                                        }
                                      });

                                    }
                                  });
  }

  @Override
  public void onViewCreated() {
    getAlbumList();
  }

  @Override
  public void onViewDestroy() {
  }

  private void getAlbumList() {
    cloudAlbumService.getAlbumList(App.globalData().getInt(GlobalData.KEY_USER_ID, 0),
                                   new URLFetcher.Delegate() {
                                     @Override
                                     public void onSuccess(URLFetcher source) {
                                       final AlbumListResponse albumListResponse =
                                           source.responseClass(AlbumListResponse.class);
                                       BaseHandler.post(new Runnable() {
                                         @Override
                                         public void run() {
                                           if (albumListResponse != null) {
                                             switch (albumListResponse.getCode()) {
                                               case 200:
                                                 Collections.reverse(albumListResponse.getData());
                                                 attachedView.onAlbumUpdated(albumListResponse.getData());
                                                 break;
                                               case 400:
                                                 attachedView.toast(
                                                     albumListResponse.getMessage() + "");
                                                 attachedView.onAlbumUpdated(new ArrayList<Album>());
                                                 break;
                                             }
                                           }
                                         }
                                       });
                                     }

                                     @Override
                                     public void onError(final String msg) {
                                       BaseHandler.post(new Runnable() {
                                         @Override
                                         public void run() {
                                           attachedView.onAlbumError(msg);
                                         }
                                       });

                                     }
                                   });
  }
}
