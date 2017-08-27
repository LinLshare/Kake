package com.home77.kake.business.home.presenter;

import android.content.Intent;

import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.debug.DLog;
import com.home77.common.base.pattern.Instance;
import com.home77.common.base.util.HashHelper;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.CloudPhotoActivity;
import com.home77.kake.business.home.model.LocalPhoto;
import com.home77.kake.common.api.request.AddPhotoRequest;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.api.response.PhotoListResponse;
import com.home77.kake.common.api.response.Response;
import com.home77.kake.common.api.response.ServerPhoto;
import com.home77.kake.common.api.response.UploadPhotoResponse;
import com.home77.kake.common.api.service.CloudAlbumService;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author CJ
 */
public class CloudPhotoListPresenter extends BaseFragmentPresenter {
  private static final String TAG = CloudPhotoListPresenter.class.getSimpleName();
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
        navigateCallback.onNavigate(CloudPhotoActivity.EVENT_EXIST, null);
        break;
      case CLICK_MENU:
        baseView.onCommand(CmdType.SHOW_MENU, null, null);
        break;
      case VIEW_REFRESH:
        baseView.onCommand(CmdType.SHOW_ALBUM_INFO, Params.create(ParamsKey.ALBUM, album), null);
        getPhotoList();
        break;
      case CLICK_IMPORT_PHOTO:
        // show local image selector
        baseView.onCommand(CmdType.SHOW_PHOTO_SELECTOR, null, null);
        // upload to cloud
        // add to album
        break;
    }
  }

  @Override
  public void start(Params params) {
    super.start(params);
    baseView.onCommand(CmdType.SHOW_ALBUM_INFO, Params.create(ParamsKey.ALBUM, album), null);
    getPhotoList();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
      ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
      File file = new File(images.get(0).path);
      upload(file);
    }
  }

  private void upload(final File file) {
    String hash = null;
    try {
      RandomAccessFile f = new RandomAccessFile(file, "r");
      byte[] b = new byte[(int) f.length()];
      f.readFully(b);
      hash = HashHelper.digestMd5(b);
    } catch (java.io.IOException e) {
      DLog.e(TAG, e.getMessage());
    }
    baseView.onCommand(CmdType.PHOTO_UPLOADING, null, null);
    final String finalHash = hash;
    cloudAlbumService.uploadPhoto(hash, file, new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        final UploadPhotoResponse response = source.responseClass(UploadPhotoResponse.class);
        if (response != null && response.getCode() == 200) {
          cloudAlbumService.addPhoto(new AddPhotoRequest(album.getId(),
                                                         file.getName(),
                                                         finalHash,
                                                         response.getData()),
                                     new URLFetcher.Delegate() {
                                       @Override
                                       public void onSuccess(URLFetcher source) {
                                         Response responseClass =
                                             source.responseClass(Response.class);
                                         if (responseClass != null &&
                                             responseClass.getCode() == 200) {
                                           baseView.onCommand(CmdType.PHOTO_UPLOAD_SUCCESS,
                                                              null,
                                                              null);
                                         } else {
                                           baseView.onCommand(CmdType.PHOTO_UPLOAD_ERROR,
                                                              null,
                                                              null);
                                         }
                                       }

                                       @Override
                                       public void onError(String msg) {
                                         baseView.onCommand(CmdType.PHOTO_UPLOAD_ERROR, null, null);
                                       }
                                     });
          getPhotoList();
        } else {
          baseView.onCommand(CmdType.PHOTO_UPLOAD_ERROR, null, null);
        }
      }

      @Override
      public void onError(String msg) {
        baseView.onCommand(CmdType.PHOTO_UPLOAD_ERROR, null, null);
      }
    });
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
                  List<LocalPhoto> localPhotoList = new ArrayList<>();
                  for (ServerPhoto serverPhoto : photoListResponse.getData()) {
                    localPhotoList.add(serverPhoto.map());
                  }

                  baseView.onCommand(CmdType.CLOUD_PHOTO_LIST_LOAD_SUCCESS,
                                     Params.create(ParamsKey.PHOTO_LIST, localPhotoList),
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
