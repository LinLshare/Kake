package com.home77.kake.business.home.presenter;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.home77.common.base.collection.Params;
import com.home77.common.base.debug.DLog;
import com.home77.kake.App;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.common.api.ServerConfig;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.theta360.v2.network.DeviceInfo;
import com.theta360.v2.network.HttpConnector;
import com.theta360.v2.network.ImageInfo;
import com.theta360.v2.network.StorageInfo;
import com.theta360.v2.view.ImageRow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
public class LocalPhotoPresenter extends BaseFragmentPresenter {

  private static final String TAG = LocalPhotoPresenter.class.getSimpleName();
  private LoadObjectListTask loadObjectListTask;

  public LocalPhotoPresenter(BaseView baseView) {
    super(baseView, null);
    App.eventBus().register(this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    App.eventBus().unregister(this);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case VIEW_REFRESH:
        loadImageAndVideoList();
        break;
      case CLICK_LINCK_CAMERA:
        baseView.onCommand(CmdType.OPEN_WIFI_SETTING, null, null);
        break;
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    loadImageAndVideoList();
  }

  private void loadImageAndVideoList() {
    if (App.hasConnectedToWifi()) {
      if (loadObjectListTask != null && !loadObjectListTask.isCancelled()) {
        loadObjectListTask.cancel(true);
      }
      loadObjectListTask = new LoadObjectListTask();
      loadObjectListTask.execute();
    } else {
      baseView.onCommand(CmdType.DEVICE_NOT_LINK, null, null);
    }
  }

  private class LoadObjectListTask extends AsyncTask<Void, String, List<ImageRow>> {
    @Override
    protected List<ImageRow> doInBackground(Void... params) {
      baseView.onCommand(CmdType.LOCAL_PHOTO_LIST_LOADING, null, null);
      try {
        publishProgress("------");
        HttpConnector camera = new HttpConnector(ServerConfig.CAMERA_HOST);
        DeviceInfo deviceInfo = camera.getDeviceInfo();
        if (deviceInfo.getSerialNumber().isEmpty()) {
          baseView.onCommand(CmdType.DEVICE_NOT_LINK, null, null);
          return null;
        }
        publishProgress("connected.");
        publishProgress(
            deviceInfo.getClass().getSimpleName() + ":<" + deviceInfo.getModel() + ", " +
            deviceInfo.getDeviceVersion() + ", " + deviceInfo.getSerialNumber() + ">");

        List<ImageRow> imageRows = new ArrayList<>();

        StorageInfo storage = camera.getStorageInfo();
        ImageRow storageCapacity = new ImageRow();
        int freeSpaceInImages = storage.getFreeSpaceInImages();
        int megaByte = 1024 * 1024;
        long freeSpace = storage.getFreeSpaceInBytes() / megaByte;
        long maxSpace = storage.getMaxCapacity() / megaByte;
        storageCapacity.setFileName(
            "Free space: " + freeSpaceInImages + "[shots] (" + freeSpace + "/" + maxSpace +
            "[MB])");
        imageRows.add(storageCapacity);

        ArrayList<ImageInfo> objects = camera.getList();
        int objectSize = objects.size();

        for (int i = 0; i < objectSize; i++) {
          ImageRow imageRow = new ImageRow();
          ImageInfo object = objects.get(i);
          imageRow.setFileId(object.getFileId());
          imageRow.setFileSize(object.getFileSize());
          imageRow.setFileName(object.getFileName());
          imageRow.setCaptureDate(object.getCaptureDate());
          publishProgress(
              "<ImageInfo: File ID=" + object.getFileId() + ", filename=" + object.getFileName() +
              ", capture_date=" + object.getCaptureDate() + ", image_pix_width=" +
              object.getWidth() + ", image_pix_height=" + object.getHeight() + ", object_format=" +
              object.getFileFormat() + ">");

          if (object.getFileFormat().equals(ImageInfo.FILE_FORMAT_CODE_EXIF_JPEG)) {
            imageRow.setIsPhoto(true);
            Bitmap thumbnail = camera.getThumb(object.getFileId());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] thumbnailImage = baos.toByteArray();
            imageRow.setThumbnail(thumbnailImage);
          } else {
            imageRow.setIsPhoto(false);
          }
          imageRows.add(imageRow);

          publishProgress("getList: " + (i + 1) + "/" + objectSize);
        }
        return imageRows;

      } catch (Throwable throwable) {
        baseView.onCommand(CmdType.LOCAL_PHOTO_LIST_LOAD_ERROR,
                           Params.create(ParamsKey.MSG, "加载本地图片失败"),
                           null);
        DLog.d(TAG, throwable.getMessage());
        return null;
      }
    }

    @Override
    protected void onProgressUpdate(String... values) {
      DLog.v(TAG, values + "");
    }

    @Override
    protected void onPostExecute(List<ImageRow> imageRows) {
      if (imageRows != null) {
        String info = imageRows.get(0).getFileName();
        DLog.d(TAG, "info: " + info);
        imageRows.remove(0);
        List<Photo> photoList = new ArrayList<>();
        for (ImageRow imgRow : imageRows) {
          if (imgRow.isPhoto()) {
            photoList.add(new Photo(Photo.TYPE_NORMAL,
                                    imgRow.getFileSize() + "",
                                    imgRow.getFileName(),
                                    imgRow.getCaptureDate(),
                                    imgRow.getThumbnail(),
                                    imgRow.getFileId()));
          }
        }

        baseView.onCommand(CmdType.LOCAL_PHOTO_LIST_LOAD_SUCCESS,
                           Params.create(ParamsKey.PHOTO_LIST, photoList),
                           null);
      }
    }

    @Override
    protected void onCancelled() {
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.CLICK_LOCAL_PHOTO:
        baseView.onCommand(CmdType.TO_PHOTO_VIEW_ACTIVITY, event.getParams(), null);
        break;
    }
  }

  private class GetThumbnailTask extends AsyncTask<Void, String, Void> {

    private String fileId;

    public GetThumbnailTask(String fileId) {
      this.fileId = fileId;
    }

    @Override
    protected Void doInBackground(Void... params) {
      HttpConnector camera = new HttpConnector("192.168.1.1");
      Bitmap thumbnail = camera.getThumb(fileId);
      if (thumbnail != null) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] thumbnailImage = baos.toByteArray();

        //        GLPhotoActivity.startActivityForResult(ImageListActivity.this,
        //                                               cameraIpAddress,
        //                                               fileId,
        //                                               thumbnailImage,
        //                                               true);
      } else {
        publishProgress("failed to get file data.");
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
      for (String log : values) {
        //        logViewer.append(log);
      }
    }
  }
}
