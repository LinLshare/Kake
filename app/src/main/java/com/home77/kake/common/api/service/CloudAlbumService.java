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
    urlFetcher = createUrlFetcher(callback).url(ALBUM_LIST_URL);
    urlFetcher.start();
  }

  public void createAlbum(int userId, String albumName, URLFetcher.Delegate callback) {
    CreateAlbumRequest request = new CreateAlbumRequest(userId, albumName);
    urlFetcher = createUrlFetcher(callback).url(ADD_ALBUM_URL).postJson(request);
    urlFetcher.start();
  }

  public void getPhotoList(int albumId, URLFetcher.Delegate callback) {
    urlFetcher = createUrlFetcher(callback).url(getUrl(PHOTO_LIST_URL_FORMAT, albumId + ""));
    urlFetcher.start();
  }

  private URLFetcher createUrlFetcher(URLFetcher.Delegate callback) {
    return URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                     .addHeader("Authorization",
                                "Bearer " +
                                App.globalData().getString(GlobalData.KEY_ACCESS_TOKEN, ""));
  }
}
