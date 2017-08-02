package com.home77.kake.business.theta;

import android.os.CountDownTimer;

import com.google.gson.reflect.TypeToken;
import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.event.GenericEvent;
import com.home77.common.net.http.HttpContextBuilder;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

/**
 * @author CJ
 */
public class ThetaService {

  private static final String HOST = "http://192.168.1.1/osc/commands/execute";
  private static final String CAMERA_START_SESSION = "camera.startSession";
  private static final String CAMERA_SET_OPTIONS = "camera.setOptions";
  private static final String CAMERA_LIST_FILES = "camera._listAll";
  private static final String CAMERA_GET_IMAGE = "camera.getImage";

  public static final int PARAMS_KEY_FILE_LIST = 1001;
  public static final int PARAMS_KEY_IMAGE = 1002;

  // state machine
  private static final int STATE_IDLE = 0;
  private static final int STATE_SESSION_ESTABLISHING = 1;
  private static final int STATE_SESSION_ESTABLISHED = 2;
  private int currentState = STATE_IDLE;

  private CountDownTimer countDownTimer;
  private String currentSessionId;

  private ThetaService() {
    App.eventBus().register(this);
  }

  public void disconnect() {
    App.eventBus().unregister(this);
  }

  public void loadImage(String fileUri, final OnThetaServiceCallback callback) {
    ThetaRequest thetaRequest = null;
    try {
      thetaRequest = ThetaRequest.create(CAMERA_GET_IMAGE)
                                 .putParameter("fileUri", fileUri)
                                 .putParameter("_type", "thumb");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    createUrlFetcher(new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        String s = source.responseUtf8String();
        callback.onThetaServiceSuccess(Params.create(PARAMS_KEY_IMAGE, s));
      }

      @Override
      public void onError(String msg) {
        callback.onThetaServiceError(msg);
      }
    }).addHeader("Content-Type", "image/jpeg").postJson(thetaRequest).start();
    //    loadFileListTruly(ThetaRequest.FILE_TYPE_IMAGE, position, 1, maxSize, callback);
  }

  public void loadFileList(final String fileType,
                           final int startPosition,
                           final int entryCount,
                           final int maxThumbSize,
                           final OnThetaServiceCallback callback) {
    switch (currentState) {
      case STATE_IDLE:
        startSession(new OnThetaServiceCallback() {
          @Override
          public void onThetaServiceSuccess(Params params) {
            loadFileListTruly(fileType, startPosition, entryCount, maxThumbSize, callback);
          }

          @Override
          public void onThetaServiceError(String msg) {
            callback.onThetaServiceError(msg);
          }
        });
        break;
      case STATE_SESSION_ESTABLISHED:
        loadFileListTruly(fileType, startPosition, entryCount, maxThumbSize, callback);
        break;
      default:
        BaseHandler.postDelayed(new Runnable() {
          @Override
          public void run() {
            if (currentState == STATE_SESSION_ESTABLISHING) {
              loadFileListTruly(fileType, startPosition, entryCount, maxThumbSize, callback);
            }
          }
        }, 1000);
        break;
    }
  }

  private void loadFileListTruly(String fileType,
                                 int startPosition,
                                 int entryCount,
                                 int maxThumbSize,
                                 final OnThetaServiceCallback callback) {
    try {
      ThetaRequest thetaRequest = ThetaRequest.create(CAMERA_LIST_FILES)
                                              .putParameter("sessionId", currentSessionId)
                                              .putParameter("entryCount", entryCount);
      createUrlFetcher(new URLFetcher.Delegate() {
        @Override
        public void onSuccess(URLFetcher source) {
          ThetaResponse<LoadFileListResult> thetaResponse =
              source.responseClass(new TypeToken<ThetaResponse<LoadFileListResult>>() {
              });
          if (thetaResponse != null) {
            callback.onThetaServiceSuccess(Params.create(PARAMS_KEY_FILE_LIST,
                                                         thetaResponse.getResults()));
          } else {
            reportIncorrectResponse(callback, source.responseUtf8String());
          }
        }

        @Override
        public void onError(String msg) {
          reportIncorrectResponse(callback, msg);
        }
      }).postJson(thetaRequest).start();
    } catch (Exception e) {
      reportError(callback, e);
    }
  }

  private URLFetcher createUrlFetcher(URLFetcher.Delegate delegate) {
    return URLFetcher.create(HttpContextBuilder.httpClient(), delegate)
                     .url(HOST)
                     .addHeader("Content-Type", "application/json;charset=utf-8")
                     .addHeader("Accept", "application/json");
  }


  public void startSession(final OnThetaServiceCallback callback) {
    App.eventBus().post(new GenericEvent(ThetaService.this, STATE_SESSION_ESTABLISHING));
    try {
      ThetaRequest thetaRequest = ThetaRequest.create(CAMERA_START_SESSION);
      createUrlFetcher(new URLFetcher.Delegate() {
        @Override
        public void onSuccess(URLFetcher source) {
          ThetaResponse<StartSessionResult> thetaResponse =
              source.responseClass(new TypeToken<ThetaResponse<StartSessionResult>>() {
              });
          if (thetaResponse != null && thetaResponse.getResults() != null) {
            currentSessionId = thetaResponse.getResults().getSessionId();
            App.eventBus().post(new GenericEvent(ThetaService.this, STATE_SESSION_ESTABLISHED));
            countDownTimeout(thetaResponse.getResults().getTimeout());
          } else {
            reportIncorrectResponse(callback, source.responseUtf8String());
          }
        }

        @Override
        public void onError(String msg) {
          reportIncorrectResponse(callback, msg);
        }
      }).postJson(thetaRequest).start();
    } catch (Exception e) {
      reportError(callback, e);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(GenericEvent event) {
    if (event.sender() == ThetaService.this) {
      currentState = event.eventType;
    }
  }

  private void countDownTimeout(int timeout) {
    if (countDownTimer != null) {
      countDownTimer.cancel();
    }
    countDownTimer = new CountDownTimer(timeout, timeout) {
      @Override
      public void onTick(long millisUntilFinished) {
      }

      @Override
      public void onFinish() {
        App.eventBus().post(new GenericEvent(ThetaService.this, STATE_IDLE));
        countDownTimer = null;
      }
    };
  }

  private void reportError(OnThetaServiceCallback callback, Exception e) {
    App.eventBus().post(new GenericEvent(ThetaService.this, STATE_IDLE));
    callback.onThetaServiceError(
        "exception: msg is " + e.getMessage() + ", cause is " + e.getCause());
  }

  private void reportIncorrectResponse(OnThetaServiceCallback callback, String msg) {
    App.eventBus().post(new GenericEvent(ThetaService.this, STATE_IDLE));
    callback.onThetaServiceError("incorrect response: " + msg);
  }

  public interface OnThetaServiceCallback {
    void onThetaServiceSuccess(Params params);

    void onThetaServiceError(String msg);
  }
}
