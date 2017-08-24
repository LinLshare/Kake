package com.home77.kake.business.home.presenter;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.home77.common.base.collection.Params;
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

  private final String[] imgProjection = new String[] {
      MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME,
      MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA,
      MediaStore.Images.Media.DATE_MODIFIED
  };

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
      File file;
      File pictureDir =
          new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                   "kake");
      if (!pictureDir.exists()) {
        boolean b = pictureDir.mkdir();
      }
      ArrayList<LocalPhoto> temp = new ArrayList<>();
      File[] files = pictureDir.listFiles();
      for (File f : files) {
        if (f.getName().endsWith("jpg")) {
          temp.add(new LocalPhoto(f.hashCode(),
                                  f.getName(),
                                  f.length(),
                                  f.lastModified(),
                                  f.getAbsolutePath()));
        }
      }


      //      Cursor cursor = baseView.context()
      //                              .getContentResolver()
      //                              .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
      //                                     imgProjection,
      //                                     MediaStore.Images.Media.MIME_TYPE + "=" + "'image/jpeg'" +
      //                                     " AND " + MediaStore.Images.Media.DATA + " like " + "'%kake%'",
      //                                     null,
      //                                     MediaStore.Images.Media.DATE_ADDED);
      //      if (cursor == null) {
      //        // error
      //        return null;
      //      }
      //
      //      if (cursor.moveToLast()) {
      //        do {
      //          if (Thread.interrupted()) {
      //            return null;
      //          }
      //          long id = cursor.getLong(cursor.getColumnIndex(imgProjection[0]));
      //          String name = cursor.getString(cursor.getColumnIndex(imgProjection[1]));
      //          long size = cursor.getLong(cursor.getColumnIndex(imgProjection[2]));
      //          String path = cursor.getString(cursor.getColumnIndex(imgProjection[3]));
      //          long date = cursor.getLong(cursor.getColumnIndex(imgProjection[4]));
      //          file = new File(path);
      //          if (file.exists()) {
      ////            if (path.contains("kake")) {
      //              temp.add(new LocalPhoto(id, name, size, date, path));
      ////            }
      //          }
      //        } while (cursor.moveToPrevious());
      //      }
      //      cursor.close();
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
