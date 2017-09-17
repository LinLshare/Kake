package com.home77.kake.common.api.service;

import com.home77.common.net.http.HttpContextBuilder;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;

import static com.home77.kake.common.api.ServerConfig.HOST;

/**
 * @author CJ
 */
public class KakeFetcher {

  private static final String KAKE_LIST_URL;
  private URLFetcher urlFetcher;

  private KakeFetcher() {
  }

  static {
    // http://control.home77.com/api/v1/album/getpublicpanos?per_page=2&page=2
    KAKE_LIST_URL = String.format("http://%s/api/v1/album/getpublicpanos", HOST);
  }

  public void getKakeList(int page, URLFetcher.Delegate callback) {
    urlFetcher =
        createUrlFetcher(callback).url(KAKE_LIST_URL + "?per_page=" + 10 + "&page=" + page);
    urlFetcher.start();
  }

  private URLFetcher createUrlFetcher(URLFetcher.Delegate callback) {
    return URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                     .addHeader("Authorization",
                                "Bearer " +
                                App.globalData().getString(GlobalData.KEY_ACCESS_TOKEN, ""));
  }
}
