package com.home77.kake.common.api.request;

/**
 * @author CJ
 */
public class DeletePhotoRequest {
  public DeletePhotoRequest(int photo_id) {
    this.photo_id = photo_id;
  }

  private int photo_id;

  public int getPhoto_id() {
    return photo_id;
  }

  public void setPhoto_id(int photo_id) {
    this.photo_id = photo_id;
  }
}
