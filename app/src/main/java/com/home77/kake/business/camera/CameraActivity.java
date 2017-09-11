package com.home77.kake.business.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.home77.common.base.debug.DLog;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.business.home.view.GLPhotoActivity;
import com.home77.kake.common.api.ServerConfig;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.home77.kake.common.utils.BrightnessTools;
import com.home77.kake.common.widget.VerticalSlider;
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

public class CameraActivity extends AppCompatActivity
    implements VerticalSlider.OnSliderListener {

  private static final String TAG = CameraActivity.class.getSimpleName();
  @BindView(R.id.pre_image_view)
  ImageView preImageView;
  @BindView(R.id.live_view)
  MJpegView liveView;
  @BindView(R.id.shoot_image_view)
  ImageView shootImageView;
  @BindView(R.id.brightness_layout)
  VerticalSlider brightnessLayout;
  private ShowLiveViewTask livePreviewTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                         WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
    setContentView(R.layout.activity_camera);
    ButterKnife.bind(this);
    brightnessLayout.setOnSliderListener(this);
    brightnessLayout.setPostion(BrightnessTools.getScreenBrightness(this));
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
    runPreviewTask();
  }

  private void runPreviewTask() {
    if (livePreviewTask != null) {
      livePreviewTask.cancel(true);
      livePreviewTask = new ShowLiveViewTask();
    } else {
      livePreviewTask = new ShowLiveViewTask();
    }
    livePreviewTask.execute(ServerConfig.CAMERA_HOST);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (livePreviewTask != null) {
      livePreviewTask.cancel(true);
    }
  }

  @OnClick({R.id.shoot_image_view, R.id.bright_image_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.shoot_image_view:
        new ShootTask().execute();
        break;
      case R.id.bright_image_view:
        toggleBrightnessLayout();
        break;
    }
  }

  private void toggleBrightnessLayout() {
    brightnessLayout.setVisibility(
        brightnessLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
  }

  @Override
  public void onPositionChanged(float position) {
    BrightnessTools.setBrightness(this, position);
  }

  @Override
  public void onConfirmed(float position) {
    brightnessLayout.setVisibility(View.GONE);
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
        Toast.showShort("拍照失败，相机未连接");
        DLog.d(TAG, "takePicture:FAIL_CAMERA_DISCONNECTED");
      } else if (result == HttpConnector.ShootResult.FAIL_STORE_FULL) {
        Toast.showShort("拍照失败，相机存储已满");
        DLog.d(TAG, "takePicture:FAIL_STORE_FULL");
      } else if (result == HttpConnector.ShootResult.FAIL_DEVICE_BUSY) {
        Toast.showShort("拍照失败，相机很忙");
        DLog.d(TAG, "takePicture:FAIL_DEVICE_BUSY");
      } else if (result == HttpConnector.ShootResult.SUCCESS) {
        Toast.showShort("拍照成功");
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
          App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_SHOW, null));
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
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
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
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
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
        final byte[] thumbnailImage = baos.toByteArray();
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            preImageView.setImageBitmap(BitmapFactory.decodeByteArray(thumbnailImage,
                                                                      0,
                                                                      thumbnailImage.length));
            runPreviewTask();
            preImageView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                GLPhotoActivity.startActivityForResult(CameraActivity.this,
                                                       ServerConfig.CAMERA_HOST,
                                                       fileId,
                                                       thumbnailImage,
                                                       fileId.replace("/", ".")
                                                             .replace("JPG", "jpg"),
                                                       true);
              }
            });
          }
        });
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
