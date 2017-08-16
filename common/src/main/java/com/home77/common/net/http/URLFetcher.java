package com.home77.common.net.http;

import com.google.gson.reflect.TypeToken;
import com.home77.common.base.debug.DLog;
import com.home77.common.base.util.TextHelper;
import com.home77.common.net.util.UserAgentHelper;
import com.home77.common.protocol.Json;
import com.home77.common.protocol.base.BasicRequest;
import com.home77.common.protocol.base.BasicResponse;
import com.home77.common.protocol.base.Info;
import com.home77.common.protocol.base.Token;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

import static com.home77.common.net.http.HttpConstants.HEADER_USER_AGENT;
import static com.home77.common.net.http.HttpConstants.MIME_APPLICATION_JSON;

public final class URLFetcher {
  private final static String TAG = URLFetcher.class.getSimpleName();

  // static

  public static URLFetcher create(OkHttpClient client, Delegate delegate) {
    return new URLFetcher(client, delegate);
  }

  // delegate

  public interface Delegate {
    void onSuccess(URLFetcher source);

    void onError(String msg);
  }

  // class

  private URLFetcher(OkHttpClient client, Delegate delegate) {
    mHttpClient = client;
    mDelegate = delegate;
    mRequestBuilder =
        new Request.Builder().addHeader(HEADER_USER_AGENT, UserAgentHelper.appUserAgent());

    mIsRunning = false;
  }

  private final Delegate mDelegate;

  private final OkHttpClient mHttpClient;
  private final Request.Builder mRequestBuilder;

  private Response mResponse;
  private IOException mLastError;

  private transient boolean mIsRunning;

  // okHttp callback
  private final Callback mCallback = new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
      onComplete(null, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
      onComplete(response, null);
    }

    private void onComplete(Response response, IOException e) {
      // ). set params
      mIsRunning = false;
      mResponse = response;
      mLastError = e;

      // ). notify upstream that URLFetcher is complete
      if (isSuccess()) {
        mDelegate.onSuccess(URLFetcher.this);
      } else {
        mDelegate.onError("response error: " + (response == null ? "null" : response.code() + ""));
      }
      // ). release |mResponse|
      if (mResponse != null) {
        mResponse.close();
      }
    }
  };

  // API

  // request relative

  public URLFetcher url(String url) {
    mRequestBuilder.url(url);
    return this;
  }

  public URLFetcher url(HttpUrl url) {
    mRequestBuilder.url(url);
    return this;
  }

  public URLFetcher addHeader(String name, String value) {
    mRequestBuilder.addHeader(name, value);
    return this;
  }

  public URLFetcher postJson(String json) {
    DLog.v(TAG, "post json [%s]", json);
    final RequestBody body = RequestBody.create(MediaType.parse(MIME_APPLICATION_JSON), json);
    mRequestBuilder.post(body);
    return this;
  }

  public URLFetcher postJson(Object obj) {
    return postJson(Json.toJson(obj));
  }

  public URLFetcher postBasicRequestJson(Info info, Token token, Object obj) {
    final BasicRequest request = BasicRequest.make(info, token, obj);
    return postJson(request);
  }

  public URLFetcher postRequestBody(RequestBody requestBody) {
    mRequestBuilder.post(requestBody);
    return this;
  }

  // actions

  public boolean start() {
    // ). check if is running
    if (mIsRunning) {
      return false;
    }

    // ). build request
    final Request request = mRequestBuilder.build();

    DLog.d(TAG, "Start URLFetcher -> (%s)[%s]", request.method(), request.url().toString());

    // ). build |Call|
    final Call call = mHttpClient.newCall(request);

    mIsRunning = true;
    call.enqueue(mCallback);

    return true;
  }

  // response relative

  private boolean isSuccess() {
    return mResponse != null && mResponse.isSuccessful();
  }

  public IOException lastError() {
    return mLastError;
  }

  public byte[] responseByteArray() {
    try {
      return mResponse.body().bytes();
    } catch (IOException e) {
      DLog.w(TAG, "read response exception -> [" + e.getMessage() + "]", e);
    }
    return null;
  }

  public String responseUtf8String() {
    String str = null;
    try {
      final ResponseBody body = mResponse.body();
      if (body != null) {
        final BufferedSource source = body.source();
        str = source.readUtf8();
        body.close();
      }
    } catch (IOException e) {
      DLog.w(TAG, "read response exception -> [" + e.getMessage() + "]", e);
    }
    DLog.v(TAG, "response -> [%s]", str);
    return str;
  }

  public <T> T responseClass(Class<T> clazz) {
    final String response = responseUtf8String();
    return TextHelper.isEmptyOrSpaces(response) ? null : Json.fromJson(response, clazz);
  }

  public <T> T responseClass(TypeToken<T> typeToken) {
    final String response = responseUtf8String();
    return TextHelper.isEmptyOrSpaces(response) ? null : Json.fromJson(response, typeToken);
  }

  public <T> BasicResponse<T> responseBasicResponseObject(Class<T> clazz) {
    final String response = responseUtf8String();
    return TextHelper.isEmptyOrSpaces(response)
        ? null
        : BasicResponse.fromJsonObject(response, clazz);
  }

  public <T> BasicResponse<List<T>> responseBasicResponseArray(Class<T> clazz) {
    final String response = responseUtf8String();
    return TextHelper.isEmptyOrSpaces(response)
        ? null
        : BasicResponse.fromJsonArray(response, clazz);
  }
}
