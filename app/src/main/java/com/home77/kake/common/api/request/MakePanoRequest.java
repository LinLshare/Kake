package com.home77.kake.common.api.request;

/**
 * @author CJ
 */
public class MakePanoRequest {
  private int album_id;

  public MakePanoRequest(int album_id) {
    this.album_id = album_id;
  }

  public int getAlbum_id() {
    return album_id;
  }

  public void setAlbum_id(int album_id) {
    this.album_id = album_id;
  }
}
