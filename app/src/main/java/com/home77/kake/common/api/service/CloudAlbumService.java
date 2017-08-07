package com.home77.kake.common.api.service;

import com.home77.common.net.http.HttpContextBuilder;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.common.api.request.CreateAlbumRequest;

import static com.home77.kake.common.api.ServerConfig.HOST;

/**
 * @author CJ
 */
public class CloudAlbumService {

  private final static String ALBUM_LIST_URL;
  private final static String ADD_ALBUM_URL;
  private final static String UPLOAD_PHOTO_URL;
  private final static String PHOTO_LIST_URL_FORMAT;

  static {
    ALBUM_LIST_URL = String.format("http://%s/api/v1/album/albumlist", HOST);
    ADD_ALBUM_URL = String.format("http://%s/api/v1/album/addalbum", HOST);
    UPLOAD_PHOTO_URL = String.format("http://%s/api/v1/album/uploadphoto", HOST);
    PHOTO_LIST_URL_FORMAT = "http://" + HOST + "/api/v1/album/%s/photolist";
  }

  @SuppressWarnings("Format is String")
  private String getUrl(String urlFormat, String... path) {
    return String.format(urlFormat, path);
  }

  private URLFetcher urlFetcher;

  private CloudAlbumService() {
  }

  public void getAlbumList(int userId, URLFetcher.Delegate callback) {
    urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                           .url(ALBUM_LIST_URL)
                           .addHeader("Authorization",
                                      "Bearer " +
                                      App.globalData().getString(GlobalData.KEY_ACCESS_TOKEN, ""));
    urlFetcher.start();
  }

  public void createAlbum(int userId, String albumName, URLFetcher.Delegate callback) {
    CreateAlbumRequest request = new CreateAlbumRequest(userId, albumName);
    urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                           .url(ADD_ALBUM_URL)
                           .addHeader("Authorization",
                                      "Bearer " +
                                      App.globalData().getString(GlobalData.KEY_ACCESS_TOKEN, ""))
                           .postJson(request);
    urlFetcher.start();
  }

  public void getPhotoList(int albumId, URLFetcher.Delegate callbck) {
    urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callbck)
                           .url(getUrl(PHOTO_LIST_URL_FORMAT, albumId + ""))
                           .addHeader("Authorization",
                                      "Bearer " +
                                      App.globalData().getString(GlobalData.KEY_ACCESS_TOKEN, ""));
    urlFetcher.start();
  }
}
