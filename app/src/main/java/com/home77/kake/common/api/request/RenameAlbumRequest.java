package com.home77.kake.common.api.request;

/**
 * @author CJ
 */
public class RenameAlbumRequest {
  private int album_id;
  private String newname;

  public RenameAlbumRequest(int album_id, String newname) {
    this.album_id = album_id;
    this.newname = newname;
  }

  public int getAlbum_id() {
    return album_id;
  }

  public void setAlbum_id(int album_id) {
    this.album_id = album_id;
  }

  public String getNewname() {
    return newname;
  }

  public void setNewname(String newname) {
    this.newname = newname;
  }
}
