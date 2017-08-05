package com.home77.kake.common.api.request;

/**
 * @author CJ
 */
public class PhotoListRequest {
  private int album_id;

  public PhotoListRequest(int album_id) {
    this.album_id = album_id;
  }

  public int getAlbum_id() {
    return album_id;
  }
}
