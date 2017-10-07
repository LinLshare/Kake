package com.home77.kake.business.home.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.debug.DLog;
import com.home77.common.base.pattern.Instance;
import com.home77.common.base.util.HashHelper;
import com.home77.common.net.http.URLFetcher;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.CloudPhotoActivity;
import com.home77.kake.business.home.PhotoSelectActivity;
import com.home77.kake.business.home.PhotoViewActivity;
import com.home77.kake.business.home.model.CloudPhoto;
import com.home77.kake.business.home.model.LocalPhoto;
import com.home77.kake.business.home.view.GLPhotoActivity;
import com.home77.kake.common.api.request.AddPhotoRequest;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.api.response.PhotoListResponse;
import com.home77.kake.common.api.response.Response;
import com.home77.kake.common.api.response.UploadPhotoResponse;
import com.home77.kake.common.api.service.CloudAlbumService;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
    App.eventBus().register(this);
  }

  public void setParam(Album album) {
    this.album = album;
  }

  @Override
  public void handleMessage(MsgType msgType, final Params params) {
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
      case CLICK_MENU_IMPORT_PHOTO:
        // show local image selector
        baseView.onCommand(CmdType.SHOW_PHOTO_SELECTOR, null, null);
        // upload to cloud
        // add to album
        break;
      case CLICK_MENU_GENERATE_QRCODE:
        if (TextUtils.isEmpty(album.getPanourl())) {
          Toast.showShort("全景图片尚未合成");
        } else {
          loadQRCode(album.getPanourl());
        }
        break;
      case CLICK_BOTTOM_BUTTON:
        handleClickBottomButton();
        break;
      case CLICK_MENU_KAKE:
        if (TextUtils.isEmpty(album.getPanourl())) {
          Toast.showShort("全景图片尚未合成");
        } else {
          baseView.onCommand(CmdType.SHOW_MAKE_PUBLIC_DIALOG, null, null);
        }
        break;
      case CLICK_MENU_SHARE:
        baseView.onCommand(CmdType.SHOW_SHARE_DIALOG, null, null);
        break;
      case CLICK_OK_MAKE_PUBLIC_DIALOG:
        makePublic();
        break;
      case CLICK_DELETE_PHOTO_EDIT_DIALOG: {
        handleDeletePhoto(params);
      }
      break;
      case CLICK_RENAME_PHOTO_EDIT_DIALOG: {
        handleRenamePhoto(params);
      }
      break;
      case CLICK_CANCEL_PHOTO_EDIT_DIALOG:
        // do nothing
        break;
      case CLICK_OK_DELETE_PHOTO_CONFIRM_DIALOG: {
        //delete photo
        baseView.onCommand(CmdType.LOADING, Params.create(ParamsKey.MSG, "正在删除"), null);
        CloudPhoto cloudPhoto = params.get(ParamsKey.CLOUD_PHOTO);
        cloudAlbumService.deletePhoto(cloudPhoto.getId(), new URLFetcher.Delegate() {
          @Override
          public void onSuccess(URLFetcher source) {
            Response response = source.responseClass(Response.class);
            if (response != null && response.getCode() == 200) {
              baseView.onCommand(CmdType.PHOTO_DELETE_SUCCESS, null, null);
              getPhotoList();
            } else {
              baseView.onCommand(CmdType.PHOTO_DELETE_ERROR, null, null);
            }
          }

          @Override
          public void onError(String msg) {
            baseView.onCommand(CmdType.PHOTO_DELETE_ERROR, null, null);
          }
        });
      }
      break;
      case CLICK_OK_RENAME_PHOTO_DIALOG: {
        baseView.onCommand(CmdType.LOADING, Params.create(ParamsKey.MSG, "正在重命名"), null);
        CloudPhoto cloudPhoto = params.get(ParamsKey.CLOUD_PHOTO);
        String rename = params.get(ParamsKey.STR);
        cloudAlbumService.renamePhoto(cloudPhoto.getId(), rename, new URLFetcher.Delegate() {
          @Override
          public void onSuccess(URLFetcher source) {
            Response response = source.responseClass(Response.class);
            if (response != null && response.getCode() == 200) {
              baseView.onCommand(CmdType.PHOTO_RENAME_SUCCESS, null, null);
              getPhotoList();
            } else {
              baseView.onCommand(CmdType.PHOTO_RENAME_ERROR, null, null);
            }
          }

          @Override
          public void onError(String msg) {
            baseView.onCommand(CmdType.PHOTO_RENAME_ERROR, null, null);
          }
        });
      }
      break;
      case CLICK_OK_RENAME_ALBUM_DIALOG: {
        Album album = params.get(ParamsKey.ALBUM);
        String name = params.get(ParamsKey.STR);
        cloudAlbumService.renameAlbum(album.getId(), name, new URLFetcher.Delegate() {
          @Override
          public void onSuccess(URLFetcher source) {
            Response response = source.responseClass(Response.class);
            if (response != null && response.getCode() == 200) {
              baseView.onCommand(CmdType.ALBUM_RENAME_SUCCESS, params, null);
            } else {
              baseView.onCommand(CmdType.ALBUM_RENAME_ERROR, params, null);
            }
          }

          @Override
          public void onError(String msg) {
            baseView.onCommand(CmdType.ALBUM_RENAME_ERROR, params, null);
          }
        });
      }
      break;
    }
  }

  private void handleRenamePhoto(Params params) {
    baseView.onCommand(CmdType.SHOW_RENAME_PHOTO_DIALOG, params, null);
  }

  private void handleDeletePhoto(Params params) {
    baseView.onCommand(CmdType.SHOW_DELETE_PHOTO_CONFIRM_DIALOG, params, null);
  }

  private void handleClickBottomButton() {
    if (album == null) {
      return;
    }
    if (TextUtils.isEmpty(album.getPanourl())) {
      // 合成户型全景
      makePano();
    } else {
      // 观看户型全景
      baseView.onCommand(CmdType.SHOW_PANO_PHOTO, null, null);
    }
  }

  private void makePano() {
    baseView.onCommand(CmdType.MAKE_PANO_POSTING, null, null);
    cloudAlbumService.makePano(album.getId(), new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        Response response = source.responseClass(Response.class);
        if (response != null && response.getCode() == 200) {
          baseView.onCommand(CmdType.MAKE_PANO_POST_SUCCESS, null, null);
        } else {
          baseView.onCommand(CmdType.MAKE_PANO_POST_ERROR, null, null);
        }
      }

      @Override
      public void onError(String msg) {
        baseView.onCommand(CmdType.MAKE_PANO_POST_ERROR, null, null);
      }
    });
  }

  private void makePublic() {
    baseView.onCommand(CmdType.MAKE_PUBLIC_POSTING, null, null);
    cloudAlbumService.makePublic(album.getId(), new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        Response response = source.responseClass(Response.class);
        if (response != null && response.getCode() == 200) {
          baseView.onCommand(CmdType.MAKE_PUBLIC_SUCCESS, null, null);
        } else {
          baseView.onCommand(CmdType.MAKE_PUBLIC_ERROR, null, null);
        }
      }

      @Override
      public void onError(String msg) {
        baseView.onCommand(CmdType.MAKE_PUBLIC_ERROR, null, null);
      }
    });
  }

  private void loadQRCode(String content) {
    baseView.onCommand(CmdType.SHOW_QRCODE_DIALOG, Params.create(ParamsKey.STR, content), null);
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
    if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
      ArrayList<LocalPhoto> images =
          data.getParcelableArrayListExtra(PhotoSelectActivity.EXTRA_LOCAL_PHOTO);
      if (images == null || images.isEmpty()) {
        return;
      }
      File file = new File(images.get(0).getPath());
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
                                           getPhotoList();
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
                case 200: {
                  Collections.sort(photoListResponse.getData(), new Comparator<CloudPhoto>() {
                    @Override
                    public int compare(CloudPhoto o1, CloudPhoto o2) {
                      return (int) (o2.getUpdated_at() - o1.getUpdated_at());
                    }
                  });
                  List<CloudPhoto> temp2 = new ArrayList<>(photoListResponse.getData());
                  int lastDay = 0;
                  int titleCount = 0;
                  for (int i = 0; i < photoListResponse.getData().size(); i++) {
                    CloudPhoto photo = photoListResponse.getData().get(i);
                    Calendar instance = Calendar.getInstance();
                    instance.setTimeInMillis(photo.getUpdated_at());
                    int currentDay = instance.get(Calendar.DAY_OF_MONTH);
                    if (lastDay != currentDay) {
                      temp2.add(i + titleCount, CloudPhoto.makeTitle(photo.getUpdated_at()));
                      titleCount++;
                      lastDay = currentDay;
                    }
                  }
                  baseView.onCommand(CmdType.CLOUD_PHOTO_LIST_LOAD_SUCCESS,
                                     Params.create(ParamsKey.CLOUD_PHOTO_LIST, temp2),
                                     null);
                }
                break;
                case 400:
                  baseView.onCommand(CmdType.CLOUD_PHOTO_LIST_LOAD_SUCCESS,
                                     Params.create(ParamsKey.CLOUD_PHOTO_LIST, new ArrayList<>()),
                                     null);
                  break;
              }
            } else {
              baseView.onCommand(CmdType.CLOUD_PHOTO_LIST_LOAD_ERROR,
                                 Params.create(ParamsKey.MSG, "加载图片失败"),
                                 null);
            }
          }
        });
      }

      @Override
      public void onError(final String msg) {
        baseView.onCommand(CmdType.CLOUD_PHOTO_LIST_LOAD_ERROR,
                           Params.create(ParamsKey.MSG, "网络异常"),
                           null);
      }
    });
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.LONG_CLICK_CLOUD_PHOTO:
        baseView.onCommand(CmdType.SHOW_EDIT_PHOTO_DIALOG, event.getParams(), null);
        break;
      case BroadCastEventConstant.CLICK_CLOUD_PHOTO: {
        final CloudPhoto cloudPhoto = event.getParams().get(ParamsKey.CLOUD_PHOTO);
        Picasso.with(baseView.context())
               .load(cloudPhoto.getImgurl())
               .resize(100, 100)
               .centerCrop()
               .into(new Target() {
                 @Override
                 public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                   ByteArrayOutputStream stream = new ByteArrayOutputStream();
                   bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                   byte[] byteArray = stream.toByteArray();
                   GLPhotoActivity.startActivityForResult(baseView.activity(),
                                                          byteArray,
                                                          cloudPhoto.getImgurl(),
                                                          cloudPhoto.getName(),
                                                          false);
                 }

                 @Override
                 public void onBitmapFailed(Drawable errorDrawable) {

                 }

                 @Override
                 public void onPrepareLoad(Drawable placeHolderDrawable) {

                 }
               });
      }
      break;
    }
  }

}
