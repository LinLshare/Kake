package com.home77.common.net.http;

import okhttp3.OkHttpClient;

public class HttpContextBuilder {
  private final static OkHttpClient HTTP_CLIENT;

  static {
    HTTP_CLIENT = new OkHttpClient();
  }

  public static OkHttpClient httpClient() {
    return HTTP_CLIENT;
  }
}
