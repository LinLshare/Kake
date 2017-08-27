package com.home77.kake.business.home.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.home77.common.base.collection.Params;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
public class LocalPhotoPresenter extends BaseFragmentPresenter {

  private static final String TAG = LocalPhotoPresenter.class.getSimpleName();
  private LoadLocalImageTask loadObjectListTask;

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
  public void onResume() {
    super.onResume();
    requestPermissions();
  }

  @Override
  public void start(Params params) {
    super.start(params);
    loadImageAndVideoList();
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
    }
  }

  private void loadImageAndVideoList() {
    if (loadObjectListTask != null && !loadObjectListTask.isCancelled()) {
      loadObjectListTask.cancel(true);
    }
    loadObjectListTask = new LoadLocalImageTask();
    loadObjectListTask.execute();
  }

  private class LoadLocalImageTask extends AsyncTask<Void, String, List<LocalPhoto>> {
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
      if (files == null) {
        return null;
      }
      for (File f : files) {
        if (f.getName().endsWith("jpg")) {
          temp.add(new LocalPhoto(f.hashCode(),
                                  f.getName(),
                                  f.length(),
                                  f.lastModified(),
                                  f.getAbsolutePath()));
        }
      }

      return temp;
    }

    @Override
    protected void onPostExecute(List<LocalPhoto> localPhotos) {
      super.onPostExecute(localPhotos);
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
      case BroadCastEventConstant.CLICK_LOCAL_PHOTO:
        baseView.onCommand(CmdType.TO_PHOTO_VIEW_ACTIVITY, event.getParams(), null);
        break;
    }
  }
}
