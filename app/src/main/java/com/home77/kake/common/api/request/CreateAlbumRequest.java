package com.home77.kake.common.api.request;

/**
 * @author CJ
 */
public class CreateAlbumRequest {
  private int user_id;
  private String name;

  public CreateAlbumRequest(int user_id, String name) {
    this.user_id = user_id;
    this.name = name;
  }

  public int getUser_id() {
    return user_id;
  }

  public String getName() {
    return name;
  }
}
