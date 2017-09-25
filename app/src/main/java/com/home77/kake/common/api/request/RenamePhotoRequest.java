package com.home77.kake.common.api.request;

/**
 * @author CJ
 */
public class RenamePhotoRequest {
  private int photo_id;
  private String newname;

  public RenamePhotoRequest(int photo_id, String newname) {
    this.photo_id = photo_id;
    this.newname = newname;
  }

  public int getPhoto_id() {
    return photo_id;
  }

  public void setPhoto_id(int photo_id) {
    this.photo_id = photo_id;
  }

  public String getNewname() {
    return newname;
  }

  public void setNewname(String newname) {
    this.newname = newname;
  }
}
