package com.home77.common.net.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class HttpContextBuilder {
  private final static OkHttpClient HTTP_CLIENT;

  static {
    HTTP_CLIENT = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
  }

  public static OkHttpClient httpClient() {
    return HTTP_CLIENT;
  }
}
