package com.home77.kake.business.home.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.home77.common.base.collection.Params;
import com.home77.common.base.debug.DLog;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.model.LocalPhoto;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author CJ
 */
public class LocalPhotoPresenter extends BaseFragmentPresenter {

  private static final String TAG = LocalPhotoPresenter.class.getSimpleName();
  private LoadLocalPhotoListTask loadObjectListTask;
  private LoadLocalPhotoTask loadLocalPhotoTask;
  private List<LocalPhoto> localPhotoList;

  public LocalPhotoPresenter(BaseView baseView) {
    super(baseView, null);
    App.eventBus().register(this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (navigateCallback != null) {
      loadObjectListTask.cancel(true);
    }
    if (loadLocalPhotoTask != null) {
      loadLocalPhotoTask.cancel(true);
    }
    App.eventBus().unregister(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    requestPermissions();
  }

  @Override
  public void start(Params params) {
    super.start(params);
    loadImageAndVideoList();
  }

  public String getRecentPhotoPath() {
    return localPhotoList == null || localPhotoList.size() < 2
        ? null
        : localPhotoList.get(1).getPath();
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  private static final String[] REQUEST_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
  private static final int REQUEST_PERMISSION_CODE = 10001;

  private void requestPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(baseView.context(), REQUEST_PERMISSIONS[0]) ==
          PackageManager.PERMISSION_GRANTED) {
        start(null);
        return;
      }
      ActivityCompat.requestPermissions(baseView.activity(),
                                        REQUEST_PERMISSIONS,
                                        REQUEST_PERMISSION_CODE);
    }

  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_PERMISSION_CODE &&
        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      start(null);
    } else {
      Toast.showShort("咔客全景相机需要读写外存权限");
    }
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case VIEW_REFRESH:
        loadImageAndVideoList();
        break;
      case CLICK_DELETE_PHOTO_EDIT_DIALOG: {
        LocalPhoto localPhoto = params.get(ParamsKey.LOCAL_PHOTO);
        File file = new File(localPhoto.getPath());
        if (file.exists()) {
          boolean delete = file.delete();
          if (delete) {
            baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, "删除成功"), null);
            loadImageAndVideoList();
          }
        } else {
          baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, "删除失败"), null);
        }
      }
      break;
      case CLICK_CANCEL_PHOTO_EDIT_DIALOG:
        //do nothing
        break;
    }
  }

  private void loadImageAndVideoList() {
    if (loadObjectListTask != null && !loadObjectListTask.isCancelled()) {
      loadObjectListTask.cancel(true);
    }
    loadObjectListTask = new LoadLocalPhotoListTask();
    loadObjectListTask.execute();
  }

  private class LoadLocalPhotoListTask extends AsyncTask<Void, String, List<LocalPhoto>> {
    @Override
    protected List<LocalPhoto> doInBackground(Void... params) {
      baseView.onCommand(CmdType.LOCAL_PHOTO_LIST_LOADING, null, null);
      File pictureDir =
          new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                   "kake");
      if (!pictureDir.exists()) {
        boolean b = pictureDir.mkdir();
      }
      ArrayList<LocalPhoto> temp = new ArrayList<>();
      File[] files = pictureDir.listFiles();
      if (files == null || files.length == 0) {
        return null;
      }
      for (File f : files) {
        if (f.getName().endsWith("jpg")) {
          temp.add(new LocalPhoto(f.hashCode(),
                                  f.getName().split("\\.jpg")[0],
                                  f.length(),
                                  f.lastModified(),
                                  f.getAbsolutePath()));
          DLog.d(TAG, "jpg: " + f.getName() + " # " + f.lastModified());
        }
      }
      Collections.sort(temp, new Comparator<LocalPhoto>() {
        @Override
        public int compare(LocalPhoto o1, LocalPhoto o2) {
          return (int) (o2.getDate() - o1.getDate());
        }
      });
      List<LocalPhoto> temp2 = new ArrayList<>(temp);
      int lastDay = 0;
      int titleCount = 0;
      for (int i = 0; i < temp.size(); i++) {
        LocalPhoto photo = temp.get(i);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(photo.getDate());
        int currentDay = instance.get(Calendar.DAY_OF_MONTH);
        if (lastDay != currentDay) {
          temp2.add(i + titleCount, LocalPhoto.makeTitle(photo.getDate()));
          titleCount++;
          lastDay = currentDay;
        }
      }
      return temp2;
    }

    @Override
    protected void onPostExecute(List<LocalPhoto> localPhotos) {
      super.onPostExecute(localPhotos);
      localPhotoList = localPhotos;
      if (localPhotos == null) {
        baseView.onCommand(CmdType.LOCAL_PHOTO_LIST_LOAD_ERROR,
                           Params.create(ParamsKey.MSG, "加载本地图片失败"),
                           null);

      } else {
        baseView.onCommand(CmdType.LOCAL_PHOTO_LIST_LOAD_SUCCESS,
                           Params.create(ParamsKey.PHOTO_LIST, localPhotos),
                           null);
      }
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.CLICK_LOCAL_PHOTO: {
        LocalPhoto localPhoto = event.getParams().get(ParamsKey.LOCAL_PHOTO);
        if (loadLocalPhotoTask != null && !loadLocalPhotoTask.isCancelled()) {
          loadLocalPhotoTask.cancel(true);
        }
        loadLocalPhotoTask = new LoadLocalPhotoTask();
        loadLocalPhotoTask.execute(localPhoto);
      }
      break;
      case BroadCastEventConstant.LONG_CLICK_LOCAL_PHOTO: {
        baseView.onCommand(CmdType.SHOW_EDIT_PHOTO_DIALOG, event.getParams(), null);
      }
      break;
    }
  }


  private class LoadLocalPhotoTask extends AsyncTask<LocalPhoto, String, byte[]> {

    private LocalPhoto localPhoto;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_SHOW, null));
    }

    @Override
    protected byte[] doInBackground(LocalPhoto... params) {
      localPhoto = params[0];
      byte[] imageData = null;
      try {
        final int THUMBNAIL_SIZE = 64;
        FileInputStream fis = new FileInputStream(localPhoto.getPath());
        Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imageData = baos.toByteArray();
      } catch (Exception ex) {
        DLog.e(TAG, ex.getMessage());
      }
      return imageData;
    }

    @Override
    protected void onPostExecute(byte[] b) {
      App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
      baseView.onCommand(CmdType.TO_PHOTO_VIEW_ACTIVITY,
                         Params.create(ParamsKey._THUMBNAIL, b)
                               .put(ParamsKey.PHOTO_NAME, localPhoto.getName())
                               .put(ParamsKey.FILE_PATH, localPhoto.getPath()),
                         null);
    }
  }
}
