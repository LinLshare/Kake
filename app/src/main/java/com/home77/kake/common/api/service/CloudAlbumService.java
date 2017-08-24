package com.home77.kake.common.api.service;

import com.home77.common.base.debug.DLog;
import com.home77.common.base.util.HashHelper;
import com.home77.common.net.http.HttpContextBuilder;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.common.api.request.AddPhotoRequest;
import com.home77.kake.common.api.request.CreateAlbumRequest;

import java.io.File;
import java.io.RandomAccessFile;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.home77.kake.common.api.ServerConfig.HOST;

/**
 * @author CJ
 */
public class CloudAlbumService {

  private final static String ALBUM_LIST_URL;
  private final static String ADD_ALBUM_URL;
  private final static String UPLOAD_PHOTO_URL;
  private final static String PHOTO_LIST_URL_FORMAT;
  private final static String ADD_PHOTO_URL;
  private static final String TAG = CloudAlbumService.class.getSimpleName();

  static {
    ALBUM_LIST_URL = String.format("http://%s/api/v1/album/albumlist", HOST);
    ADD_ALBUM_URL = String.format("http://%s/api/v1/album/addalbum", HOST);
    UPLOAD_PHOTO_URL = String.format("http://%s/api/v1/album/uploadphoto", HOST);
    ADD_PHOTO_URL = String.format("http://%s/api/v1/album/addphoto", HOST);
    PHOTO_LIST_URL_FORMAT = "http://" + HOST + "/api/v1/album/%s/photolist";
  }

  private String getPhotoListUrl(String path) {
    return String.format(PHOTO_LIST_URL_FORMAT, path);
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
    urlFetcher = createUrlFetcher(callback).url(getPhotoListUrl(albumId + ""));
    urlFetcher.start();
  }

  public void addPhoto(AddPhotoRequest request, URLFetcher.Delegate callback) {
    urlFetcher = createUrlFetcher(callback).url(ADD_PHOTO_URL).postJson(request);
    urlFetcher.start();
  }

  public void uploadPhoto(String fileHash,File file, URLFetcher.Delegate callback) {

    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
    RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                                         .addFormDataPart("file",
                                                                          file.getName(),
                                                                          fileBody)
                                                         .addFormDataPart("photo_hash", fileHash)
                                                         .build();
    urlFetcher = createUrlFetcher(callback).url(UPLOAD_PHOTO_URL).postRequestBody(requestBody);
    urlFetcher.start();
  }

  private URLFetcher createUrlFetcher(URLFetcher.Delegate callback) {
    return URLFetcher.create(HttpContextBuilder.httpClient(), callback)
                     .addHeader("Authorization",
                                "Bearer " +
                                App.globalData().getString(GlobalData.KEY_ACCESS_TOKEN, ""));
  }
}
