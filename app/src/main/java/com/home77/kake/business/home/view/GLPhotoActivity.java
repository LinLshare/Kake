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
import com.home77.kake.business.home.PhotoViewActivity;
import com.home77.kake.business.home.view.glview.GLPhotoView;
import com.theta360.v2.model.Photo;
import com.theta360.v2.model.RotateInertia;
import com.theta360.v2.network.HttpConnector;
import com.theta360.v2.network.HttpDownloadListener;
import com.theta360.v2.network.ImageData;
import com.theta360.v2.network.XMP;
import com.theta360.v2.view.ConfigurationDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;


/**
 * Activity that displays photo object as a sphere
 */
public class GLPhotoActivity extends Activity implements ConfigurationDialog.DialogBtnListener {

  public static final int SOURCE_LOCAL = 0;
  public static final int SOURCE_SERVER = 1;
  public static final int SOURCE_CAMERA = 2;
  private static final String SOURCE = "source";
  private static final String CAMERA_IP_ADDRESS = "CAMERA_IP_ADDRESS";
  private static final String OBJECT_ID = "OBJECT_ID";
  private static final String THUMBNAIL = "THUMBNAIL";
  private static final String NAME = "NAME";
  private static final String PATH = "PATH";
  private static final String TAG = GLPhotoActivity.class.getSimpleName();

  private GLPhotoView mGLPhotoView;

  private Photo mTexture = null;
  private AsyncTask mLoadPhotoTask = null;
  private String name;
  private int source;

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
    findViewById(R.id.back_image_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        GLPhotoActivity.this.finish();
      }
    });
    findViewById(R.id.share_image_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });
    Intent intent = getIntent();
    source = intent.getIntExtra(SOURCE, SOURCE_LOCAL);
    name = intent.getStringExtra(NAME);

    byte[] byteThumbnail = intent.getByteArrayExtra(THUMBNAIL);
    if (byteThumbnail != null && byteThumbnail.length > 0) {
      ByteArrayInputStream inputStreamThumbnail = new ByteArrayInputStream(byteThumbnail);
      Drawable thumbnail = BitmapDrawable.createFromStream(inputStreamThumbnail, null);
      Photo _thumbnail = new Photo(((BitmapDrawable) thumbnail).getBitmap());
      mGLPhotoView.setTexture(_thumbnail);
      mGLPhotoView.setmRotateInertia(mRotateInertia);
    }

    if (source == SOURCE_CAMERA) { // load camera
      String cameraIpAddress = intent.getStringExtra(CAMERA_IP_ADDRESS);
      String fileId = intent.getStringExtra(OBJECT_ID);
      new LoadPhotoTask(cameraIpAddress, fileId).execute();
    } else if (source == SOURCE_LOCAL) { // load local
      String path = intent.getStringExtra(PATH);
      new LoadLocalPhotoTask().execute(path);
    } else if (source == SOURCE_SERVER) { // load server
      String path = intent.getStringExtra(PATH);
      new LoadServerPhotoTask().execute(path);
    }
  }

  private class LoadServerPhotoTask extends AsyncTask<String, String, byte[]> {
    String path = null;

    @Override
    protected byte[] doInBackground(String... paths) {
      byte[] bytes = null;
      try {
        path = paths[0];
        URL url = new URL(path);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream inputStream = url.openStream();
        int n = 0;
        byte[] buffer = new byte[1024];
        while (-1 != (n = inputStream.read(buffer))) {
          output.write(buffer, 0, n);
        }
        bytes = output.toByteArray();
      } catch (Exception e) {
        DLog.e(TAG, "read server photo fail: " + e.getMessage());
      }
      return bytes;
    }

    @Override
    protected void onPostExecute(byte[] b) {
      if (b == null) {
        GLPhotoActivity.this.finish();
        return;
      }
      try {
        XMP xmp = new XMP(b);
        mTexture = new Photo(BitmapFactory.decodeByteArray(b, 0, b.length),
                             Double.valueOf(0),
                             xmp.getPosePitchDegrees(),
                             xmp.getPoseRollDegrees());
        if (null != mGLPhotoView) {
          mGLPhotoView.setTexture(mTexture);
        }
      } catch (Exception e) {
        DLog.e(TAG, e.getMessage());
        GLPhotoActivity.this.finish();
        PhotoViewActivity.start(getApplicationContext(), path);
      }
    }
  }

  private class LoadLocalPhotoTask extends AsyncTask<String, String, byte[]> {
    String path = null;

    @Override
    protected byte[] doInBackground(String... paths) {
      RandomAccessFile f = null;
      byte[] b = null;
      path = paths[0];
      try {
        f = new RandomAccessFile(path, "r");
        b = new byte[(int) f.length()];
        f.readFully(b);
      } catch (java.io.IOException e) {
        e.printStackTrace();
      }
      return b;
    }

    @Override
    protected void onPostExecute(byte[] b) {
      try {
        XMP xmp = new XMP(b);
        mTexture = new Photo(BitmapFactory.decodeByteArray(b, 0, b.length),
                             Double.valueOf(0),
                             xmp.getPosePitchDegrees(),
                             xmp.getPoseRollDegrees());
        if (null != mGLPhotoView) {
          mGLPhotoView.setTexture(mTexture);
        }
      } catch (Exception e) {
        DLog.e(TAG, e.getMessage());
        GLPhotoActivity.this.finish();
        PhotoViewActivity.start(getApplicationContext(), path);
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
        File f = new File(pictureDir, name);
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
        // insert image
        //        MediaStore.Images.Media.insertImage(getContentResolver(),
        //                                            f.getPath(),
        //                                            f.getName(),
        //                                            f.getName());
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
        Instance.of(ImageDataStorage.class).bind(name).putYaw(yaw).putPitch(pitch).putRoll(roll);
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
                                            String name,
                                            String path,
                                            byte[] thumbnail,
                                            boolean refreshAfterClose) {
    int requestCode;
    if (refreshAfterClose) {
      requestCode = REQUEST_REFRESH_LIST;
    } else {
      requestCode = REQUEST_NOT_REFRESH_LIST;
    }

    Intent intent = new Intent(activity, GLPhotoActivity.class);
    intent.putExtra(THUMBNAIL, thumbnail);
    intent.putExtra(SOURCE, SOURCE_LOCAL);
    intent.putExtra(NAME, name);
    intent.putExtra(PATH, path);
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
    intent.putExtra(SOURCE, SOURCE_CAMERA);
    intent.putExtra(THUMBNAIL, thumbnail);
    activity.startActivityForResult(intent, requestCode);
  }

  public static void startActivityForResult(Activity activity,
                                            byte[] thumbnail,
                                            String panourl,
                                            String name,
                                            boolean refreshAfterClose) {
    int requestCode;
    if (refreshAfterClose) {
      requestCode = REQUEST_REFRESH_LIST;
    } else {
      requestCode = REQUEST_NOT_REFRESH_LIST;
    }

    Intent intent = new Intent(activity, GLPhotoActivity.class);
    intent.putExtra(NAME, name);
    intent.putExtra(THUMBNAIL, thumbnail);
    intent.putExtra(PATH, panourl);
    intent.putExtra(SOURCE, SOURCE_SERVER);
    activity.startActivityForResult(intent, requestCode);
  }

}