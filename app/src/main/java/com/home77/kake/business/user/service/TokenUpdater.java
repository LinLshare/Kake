package com.home77.kake.business.user.service;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.debug.DLog;
import com.home77.common.base.lang.RV;
import com.home77.common.net.http.HttpContextBuilder;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.business.user.service.request.TokenValidateRequest;
import com.home77.kake.business.user.service.response.TokenValidateResponse;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * run itself or called when login
 *
 * @author CJ
 */
public class TokenUpdater implements URLFetcher.Delegate {

  private static final String TAG = TokenUpdater.class.getSimpleName();
  private static final String CLIENT_SECRET = "V59K95xzfdhjNlcq3TZaW9KWJwufgp6w8RH8uFU4";
  private static final int CLIENT_ID = 3;
  private static final String GRANT_TYPE = "password";
  private static final String SCOPE = "";
  private static final int DEFAULT_EXPIRE = 7200;//s
  private ScheduledExecutorService scheduledExecutorService;
  private TimingUpdateTokenRunnable timingUpdateTokenRunnable;
  private int expire = DEFAULT_EXPIRE;
  private ScheduledFuture<?> scheduledFuture;
  private String userName;
  private String password;
  private TokenValidateResponse tokenValidateResponse;
  private Set<OnTokenCallback> onTokenCallbackSet;

  private static final String VALIDATE_URL;

  static {
    String host = "control.home77.com";
    VALIDATE_URL = String.format("http://%s/oauth/token", host);
  }

  private TokenUpdater() {
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    timingUpdateTokenRunnable = new TimingUpdateTokenRunnable();
    onTokenCallbackSet = new LinkedHashSet<>();
  }

  //cmd
  //start when login
  public void start(String userName, String password) {
    switch (currentState) {
      case STATE_IDLE:
        this.userName = userName;
        this.password = password;
        this.expire = 0;
        safeStartExecutor();
        nextState = STATE_TOKEN_FETCHING;
        doLoop(RV.OK);
        break;
    }
  }

  // state machine
  private static final int STATE_IDLE = 0;
  private static final int STATE_TOKEN_FETCHING = 1;
  private static final int STATE_TOKEN_FETCHED = 2;
  private static final int STATE_TOKEN_FETCH_ERROR = 3;
  private int currentState = STATE_IDLE;
  private int nextState = STATE_IDLE;

  private void doLoop(int result) {
    do {
      currentState = nextState;
      nextState = STATE_IDLE;
      switch (currentState) {
        case STATE_TOKEN_FETCHING:
          result = handleTokenFetching();
          break;
        case STATE_TOKEN_FETCHED:
          result = handleTokenFetched();
          break;
        case STATE_TOKEN_FETCH_ERROR:
          result = handleTokenFetchError();
          break;
      }
    } while (result != RV.PENDING && nextState != STATE_IDLE);
  }

  private int handleTokenFetching() {
    scheduleTask(expire);
    return RV.PENDING;
  }

  private int handleTokenFetched() {
    //update & notify data
    //then set next state idle
    expire = tokenValidateResponse.getExpires_in();
    nextState = STATE_TOKEN_FETCHING;
    notifyGetTokenSuccess(tokenValidateResponse.getAccess_token());
    return RV.OK;
  }

  private void notifyGetTokenSuccess(String token) {
    for (OnTokenCallback callback : onTokenCallbackSet) {
      callback.onGetTokenSuccess(token);
    }
    onTokenCallbackSet.clear();
  }

  private int handleTokenFetchError() {
    onTokenCallbackSet.clear();
    if (expire < DEFAULT_EXPIRE) {
      expire = DEFAULT_EXPIRE;
    }
    expire = (expire + 10 * 60) ^ 2;
    nextState = STATE_TOKEN_FETCHING;
    notifyGetTokenFail();
    return RV.OK;
  }

  private void notifyGetTokenFail() {
    for (OnTokenCallback callback : onTokenCallbackSet) {
      callback.onGetTokenFail();
    }
  }

  //getter
  public String getToken() {
    return tokenValidateResponse == null ? "" : tokenValidateResponse.getAccess_token();
  }

  public void getTokenAsync(OnTokenCallback onTokenCallback) {
    switch (currentState) {
      case STATE_IDLE:
        onTokenCallback.onGetTokenSuccess("");
        break;
      case STATE_TOKEN_FETCHED:
        String token = getToken();
        if (!TextUtils.isEmpty(token)) {
          onTokenCallback.onGetTokenSuccess(token);
        }
        break;
      default:
        this.onTokenCallbackSet.add(onTokenCallback);
        break;
    }
  }

  private void safeStartExecutor() {
    if (scheduledExecutorService.isShutdown()) {
      scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }
  }

  private void scheduleTask(int expire) {
    if (scheduledFuture != null) {
      scheduledFuture.cancel(false);
    }
    scheduledFuture =
        scheduledExecutorService.schedule(timingUpdateTokenRunnable, expire, TimeUnit.SECONDS);
  }

  //msg
  @Override
  public void onComplete(final URLFetcher source) {
    BaseHandler.post(new Runnable() {
      @Override
      public void run() {
        if (source.isSuccess()) {
          DLog.d(TAG, "result: " + source.responseUtf8String());
          //          TokenUpdater.t nseClass(TokenValidateResponse.class);
          nextState = STATE_TOKEN_FETCHED;
          doLoop(RV.OK);
          return;
        }
        nextState = STATE_TOKEN_FETCH_ERROR;
        doLoop(RV.OK);
      }
    });
  }

  public interface OnTokenCallback {
    void onGetTokenSuccess(String token);

    void onGetTokenFail();
  }

  private class TimingUpdateTokenRunnable implements Runnable {
    @Override
    public void run() {
      BaseHandler.post(new Runnable() {
        @Override
        public void run() {
          TokenValidateRequest request = new TokenValidateRequest(GRANT_TYPE,
                                                                  CLIENT_ID,
                                                                  CLIENT_SECRET,
                                                                  userName,
                                                                  password,
                                                                  SCOPE);

          URLFetcher.create(HttpContextBuilder.httpClient(), TokenUpdater.this)
                    .url(VALIDATE_URL)
                    .postJson(request)
                    .start();
        }
      });
    }
  }
}
