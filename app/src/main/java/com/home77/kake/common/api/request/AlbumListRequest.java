package com.home77.kake.common.api.request;

/**
 * @author CJ
 */
public class AlbumListRequest {
  private int user_id;

  public AlbumListRequest(int user_id) {
    this.user_id = user_id;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }
}
