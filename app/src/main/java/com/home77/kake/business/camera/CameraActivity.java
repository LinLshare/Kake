package com.home77.kake.business.camera;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.home77.common.base.debug.DLog;
import com.home77.kake.R;
import com.home77.kake.business.home.view.GLPhotoActivity;
import com.home77.kake.common.api.ServerConfig;
import com.theta360.v2.network.HttpConnector;
import com.theta360.v2.network.HttpEventListener;
import com.theta360.v2.view.MJpegInputStream;
import com.theta360.v2.view.MJpegView;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity {

  private static final String TAG = CameraActivity.class.getSimpleName();
  @BindView(R.id.pre_image_view)
  ImageView preImageView;
  @BindView(R.id.live_view)
  MJpegView liveView;
  @BindView(R.id.shoot_image_view)
  ImageView shootImageView;
  private ShowLiveViewTask livePreviewTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    ButterKnife.bind(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(getResources().getColor(R.color.colorTransparent70));
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    liveView.stopPlay();
  }

  @Override
  protected void onResume() {
    super.onResume();
    liveView.play();

    if (livePreviewTask != null) {
      livePreviewTask.cancel(true);
      livePreviewTask = new ShowLiveViewTask();
      livePreviewTask.execute(ServerConfig.CAMERA_HOST);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (livePreviewTask != null) {
      livePreviewTask.cancel(true);
    }
  }

  @OnClick({R.id.back_image_view, R.id.shoot_image_view, R.id.bright_image_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        this.finish();
        break;
      case R.id.shoot_image_view:
        new ShootTask().execute();
        break;
      case R.id.bright_image_view:
        break;
    }
  }


  private class ShowLiveViewTask extends AsyncTask<String, String, MJpegInputStream> {
    @Override
    protected MJpegInputStream doInBackground(String... ipAddress) {
      MJpegInputStream mjis = null;
      final int MAX_RETRY_COUNT = 20;

      for (int retryCount = 0; retryCount < MAX_RETRY_COUNT; retryCount++) {
        try {
          publishProgress("start Live view");
          HttpConnector camera = new HttpConnector(ipAddress[0]);
          InputStream is = camera.getLivePreview();
          mjis = new MJpegInputStream(is);
          retryCount = MAX_RETRY_COUNT;
        } catch (IOException e) {
          try {
            Thread.sleep(500);
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
        } catch (JSONException e) {
          try {
            Thread.sleep(500);
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
        }
      }

      return mjis;
    }

    @Override
    protected void onProgressUpdate(String... values) {
      for (String log : values) {
        DLog.i(TAG, "log: " + log);
      }
    }

    @Override
    protected void onPostExecute(MJpegInputStream mJpegInputStream) {
      if (mJpegInputStream != null) {
        liveView.setSource(mJpegInputStream);
      } else {
        DLog.d(TAG, "failed to start live view");
      }
    }
  }

  private class ShootTask extends AsyncTask<Void, Void, HttpConnector.ShootResult> {

    @Override
    protected void onPreExecute() {
      DLog.d(TAG, "takePicture");
    }

    @Override
    protected HttpConnector.ShootResult doInBackground(Void... params) {
      CaptureListener postviewListener = new CaptureListener();
      HttpConnector camera = new HttpConnector(getResources().getString(R.string.theta_ip_address));
      HttpConnector.ShootResult result = camera.takePicture(postviewListener);

      return result;
    }

    @Override
    protected void onPostExecute(HttpConnector.ShootResult result) {
      if (result == HttpConnector.ShootResult.FAIL_CAMERA_DISCONNECTED) {
        DLog.d(TAG, "takePicture:FAIL_CAMERA_DISCONNECTED");
      } else if (result == HttpConnector.ShootResult.FAIL_STORE_FULL) {
        DLog.d(TAG, "takePicture:FAIL_STORE_FULL");
      } else if (result == HttpConnector.ShootResult.FAIL_DEVICE_BUSY) {
        DLog.d(TAG, "takePicture:FAIL_DEVICE_BUSY");
      } else if (result == HttpConnector.ShootResult.SUCCESS) {
        DLog.d(TAG, "takePicture:SUCCESS");
      }
    }

    private class CaptureListener implements HttpEventListener {
      private String latestCapturedFileId;
      private boolean ImageAdd = false;

      @Override
      public void onCheckStatus(boolean newStatus) {
        if (newStatus) {
          DLog.d(TAG, "takePicture:FINISHED");
        } else {
          DLog.d(TAG, "takePicture:IN PROGRESS");
        }

      }

      @Override
      public void onObjectChanged(String latestCapturedFileId) {
        this.ImageAdd = true;
        this.latestCapturedFileId = latestCapturedFileId;
        DLog.d(TAG, "ImageAdd:FileId " + this.latestCapturedFileId);
      }

      @Override
      public void onCompleted() {
        DLog.d(TAG, "CaptureComplete");
        if (ImageAdd) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              shootImageView.setEnabled(true);
              new GetThumbnailTask(latestCapturedFileId).execute();
            }
          });
        }
      }

      @Override
      public void onError(String errorMessage) {
        DLog.e(TAG, "CaptureError " + errorMessage);
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            shootImageView.setEnabled(true);
          }
        });
      }
    }
  }

  private class GetThumbnailTask extends AsyncTask<Void, String, Void> {

    private String fileId;

    public GetThumbnailTask(String fileId) {
      this.fileId = fileId;
    }

    @Override
    protected Void doInBackground(Void... params) {
      HttpConnector camera = new HttpConnector(getResources().getString(R.string.theta_ip_address));
      Bitmap thumbnail = camera.getThumb(fileId);
      if (thumbnail != null) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] thumbnailImage = baos.toByteArray();
        GLPhotoActivity.startActivityForResult(CameraActivity.this,
                                               ServerConfig.CAMERA_HOST,
                                               fileId,
                                               thumbnailImage,
                                               true);
      } else {
        publishProgress("failed to get file data.");
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
      for (String log : values) {
        DLog.d(TAG, log);
      }
    }
  }
}
