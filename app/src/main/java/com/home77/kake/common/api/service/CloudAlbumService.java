package com.home77.kake.common.api.service;

import com.home77.common.net.http.HttpContextBuilder;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.common.api.request.AlbumListRequest;
import com.home77.kake.common.api.request.CreateAlbumRequest;
import com.home77.kake.common.api.request.PhotoListRequest;

import static com.home77.kake.common.api.ServerConfig.HOST;

/**
 * @author CJ
 */
public class CloudAlbumService {

  private final static String ALBUM_LIST_URL;
  private final static String ADD_ALBUM_URL;
  private final static String UPLOAD_PHOTO_URL;
  private final static String PHOTO_LIST_URL;

  static {
    ALBUM_LIST_URL = String.format("http://%s/api/v1/album/albumlist", HOST);
    ADD_ALBUM_URL = String.format("http://%s/api/v1/album/addalbum", HOST);
    UPLOAD_PHOTO_URL = String.format("http://%s/api/v1/album/uploadphoto", HOST);
    PHOTO_LIST_URL = String.format("http://%s/api/v1/album/photolist", HOST);
  }

  private URLFetcher urlFetcher;

  private CloudAlbumService() {
  }

  public void getAlbumList(int userId, URLFetcher.Delegate callback) {
    AlbumListRequest request = new AlbumListRequest(userId);
    urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                           .url(ALBUM_LIST_URL)
                           .postJson(request);
    urlFetcher.start();
  }

  public void createAlbum(int userId, String albumName, URLFetcher.Delegate callback) {
    CreateAlbumRequest request = new CreateAlbumRequest(userId, albumName);
    urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                           .url(ADD_ALBUM_URL)
                           .postJson(request);
    urlFetcher.start();
  }

  public void getPhotoList(int albumId, URLFetcher.Delegate callbck) {
    PhotoListRequest request = new PhotoListRequest(albumId);
    urlFetcher = URLFetcher.create(HttpContextBuilder.httpClient(), callbck)
                           .url(PHOTO_LIST_URL)
                           .postJson(request);
    urlFetcher.start();
  }
}
