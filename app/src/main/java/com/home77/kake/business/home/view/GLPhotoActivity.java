package com.home77.kake.business.home.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.home77.common.base.debug.DLog;
import com.home77.common.base.pattern.Instance;
import com.home77.kake.R;
import com.home77.kake.business.camera.ImageDataStorage;
import com.home77.kake.business.home.model.LocalPhoto;
import com.home77.kake.business.home.view.glview.GLPhotoView;
import com.theta360.v2.model.Photo;
import com.theta360.v2.model.RotateInertia;
import com.theta360.v2.network.HttpConnector;
import com.theta360.v2.network.HttpDownloadListener;
import com.theta360.v2.network.ImageData;
import com.theta360.v2.view.ConfigurationDialog;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;


/**
 * Activity that displays photo object as a sphere
 */
public class GLPhotoActivity extends Activity implements ConfigurationDialog.DialogBtnListener {

  private static final String CAMERA_IP_ADDRESS = "CAMERA_IP_ADDRESS";
  private static final String OBJECT_ID = "OBJECT_ID";
  private static final String THUMBNAIL = "THUMBNAIL";
  private static final String LOCAL_PHOTO = "LOCAL_PHOTO";
  private static final String NAME = "NAME";
  private static final String TAG = GLPhotoActivity.class.getSimpleName();

  private GLPhotoView mGLPhotoView;

  private Photo mTexture = null;
  private AsyncTask mLoadPhotoTask = null;
  private String name;

  private RotateInertia mRotateInertia = RotateInertia.INERTIA_50;

  public static final int REQUEST_REFRESH_LIST = 100;
  public static final int REQUEST_NOT_REFRESH_LIST = 101;

  /**
   * onCreate Method
   *
   * @param savedInstanceState
   *     onCreate Status value
   */
  @Override
  protected void onCreate(final Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                         WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏

    setContentView(R.layout.activity_glphoto);
    mGLPhotoView = (GLPhotoView) findViewById(R.id.photo_image);

    Intent intent = getIntent();
    LocalPhoto localPhoto = (LocalPhoto) intent.getSerializableExtra(LOCAL_PHOTO);

    if (localPhoto == null) { // load camera
      String cameraIpAddress = intent.getStringExtra(CAMERA_IP_ADDRESS);
      String fileId = intent.getStringExtra(OBJECT_ID);
      name = intent.getStringExtra(NAME);
      byte[] byteThumbnail = intent.getByteArrayExtra(THUMBNAIL);
      ByteArrayInputStream inputStreamThumbnail = new ByteArrayInputStream(byteThumbnail);
      Drawable thumbnail = BitmapDrawable.createFromStream(inputStreamThumbnail, null);
      Photo _thumbnail = new Photo(((BitmapDrawable) thumbnail).getBitmap());
      mGLPhotoView.setTexture(_thumbnail);
      mGLPhotoView.setmRotateInertia(mRotateInertia);
      new LoadPhotoTask(cameraIpAddress, fileId).execute();
    } else { // load local
      new LoadLocalPhotoTask().execute(localPhoto);
    }
  }

  private class LoadLocalPhotoTask extends AsyncTask<LocalPhoto, String, Bitmap> {

    @Override
    protected Bitmap doInBackground(LocalPhoto... params) {
      LocalPhoto localPhoto = params[0];
      return BitmapFactory.decodeFile(localPhoto.getPath());
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      // TODO: 2017/8/24 get share
      ImageDataStorage imageDataStorage = Instance.of(ImageDataStorage.class);
      mTexture = new Photo(bitmap,
                           imageDataStorage.getYaw(),
                           imageDataStorage.getPitch(),
                           imageDataStorage.getRoll());
      if (null != mGLPhotoView) {
        mGLPhotoView.setTexture(mTexture);
      }
    }
  }

  @Override
  protected void onDestroy() {
    if (mTexture != null) {
      mTexture.getPhoto().recycle();
    }
    if (mLoadPhotoTask != null) {
      mLoadPhotoTask.cancel(true);
    }
    super.onDestroy();
  }

  /**
   * onCreateOptionsMenu method
   *
   * @param menu
   *     Menu initialization object
   *
   * @return Menu display feasibility status value
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(com.theta360.v2.R.menu.configuration, menu);

    return true;
  }


  /**
   * onOptionsItemSelected Method
   *
   * @param item
   *     Process menu
   *
   * @return Menu process continuation feasibility value
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
    return true;
  }


  /**
   * onResume Method
   */
  @Override
  protected void onResume() {
    super.onResume();
    mGLPhotoView.onResume();

    if (null != mTexture) {
      if (null != mGLPhotoView) {
        mGLPhotoView.setTexture(mTexture);
      }
    }
  }

  /**
   * onPause Method
   */
  @Override
  protected void onPause() {
    this.mGLPhotoView.onPause();
    super.onPause();
  }


  /**
   * onDialogCommitClick Method
   *
   * @param inertia
   *     selected inertia
   */
  @Override
  public void onDialogCommitClick(RotateInertia inertia) {
    mRotateInertia = inertia;
    if (null != mGLPhotoView) {
      mGLPhotoView.setmRotateInertia(mRotateInertia);
    }
  }


  private class LoadPhotoTask extends AsyncTask<Void, Object, ImageData> {

    private ProgressBar progressBar;
    private String cameraIpAddress;
    private String fileId;
    private long fileSize;
    private long receivedDataSize = 0;

    public LoadPhotoTask(String cameraIpAddress, String fileId) {
      this.progressBar = (ProgressBar) findViewById(R.id.loading_photo_progress_bar);
      this.cameraIpAddress = cameraIpAddress;
      this.fileId = fileId;
    }

    @Override
    protected void onPreExecute() {
      progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected ImageData doInBackground(Void... params) {
      try {
        publishProgress("start to download image" + fileId);
        HttpConnector camera = new HttpConnector(cameraIpAddress);
        ImageData resizedImageData = camera.getImage(fileId, new HttpDownloadListener() {
          @Override
          public void onTotalSize(long totalSize) {
            fileSize = totalSize;
          }

          @Override
          public void onDataReceived(int size) {
            receivedDataSize += size;

            if (fileSize != 0) {
              int progressPercentage = (int) (receivedDataSize * 100 / fileSize);
              publishProgress(progressPercentage);
            }
          }
        });
        publishProgress("finish to download");
        //create a file to write bitmap data
        File pictureDir =
            new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                     "kake");
        if (!pictureDir.exists()) {
          boolean mkdir = pictureDir.mkdir();
        }
        File f = new File(pictureDir, name.replace("JPG", "jpg"));
        if (!f.getParentFile().exists()) {
          f.getParentFile().mkdir();
        }
        boolean newFile = f.createNewFile();

        //Convert bitmap to byte array
        byte[] bitmapdata = resizedImageData.getRawData();
        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        // save meta data

        return resizedImageData;

      } catch (Throwable throwable) {
        String errorLog = Log.getStackTraceString(throwable);
        publishProgress(errorLog);
        return null;
      }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
      for (Object param : values) {
        if (param instanceof Integer) {
          progressBar.setProgress((Integer) param);
        } else if (param instanceof String) {
          DLog.d(TAG, param.toString());
        }
      }
    }

    @Override
    protected void onPostExecute(ImageData imageData) {
      if (imageData != null) {

        byte[] dataObject = imageData.getRawData();

        if (dataObject == null) {
          DLog.d(TAG, "failed to download image");
          return;
        }

        Bitmap __bitmap = BitmapFactory.decodeByteArray(dataObject, 0, dataObject.length);


        progressBar.setVisibility(View.GONE);

        Double yaw = imageData.getYaw();
        Double pitch = imageData.getPitch();
        Double roll = imageData.getRoll();
        Instance.of(ImageDataStorage.class).putYaw(yaw).putPitch(pitch).putRoll(roll);
        DLog.d(TAG, "<Angle: yaw=" + yaw + ", pitch=" + pitch + ", roll=" + roll + ">");
        mTexture = new Photo(__bitmap, yaw, pitch, roll);
        if (null != mGLPhotoView) {
          mGLPhotoView.setTexture(mTexture);
        }
      } else {
        DLog.d(TAG, "failed to download image");
      }
    }
  }

  public static void startActivityForResult(Activity activity,
                                            LocalPhoto localPhoto,
                                            boolean refreshAfterClose) {
    int requestCode;
    if (refreshAfterClose) {
      requestCode = REQUEST_REFRESH_LIST;
    } else {
      requestCode = REQUEST_NOT_REFRESH_LIST;
    }

    Intent intent = new Intent(activity, GLPhotoActivity.class);
    intent.putExtra(LOCAL_PHOTO, localPhoto);
    activity.startActivityForResult(intent, requestCode);
  }

  /**
   * Activity call method
   *
   * @param activity
   *     Call source activity
   * @param cameraIpAddress
   *     IP address for camera device
   * @param fileId
   *     Photo object identifier
   * @param thumbnail
   *     Thumbnail
   * @param refreshAfterClose
   *     true is to refresh list after closing this activity, otherwise is not to refresh
   */
  public static void startActivityForResult(Activity activity,
                                            String cameraIpAddress,
                                            String fileId,
                                            byte[] thumbnail,
                                            String name,
                                            boolean refreshAfterClose) {
    int requestCode;
    if (refreshAfterClose) {
      requestCode = REQUEST_REFRESH_LIST;
    } else {
      requestCode = REQUEST_NOT_REFRESH_LIST;
    }

    Intent intent = new Intent(activity, GLPhotoActivity.class);
    intent.putExtra(CAMERA_IP_ADDRESS, cameraIpAddress);
    intent.putExtra(OBJECT_ID, fileId);
    intent.putExtra(NAME, name);
    intent.putExtra(THUMBNAIL, thumbnail);
    activity.startActivityForResult(intent, requestCode);
  }
}