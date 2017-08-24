package com.home77.kake.common.api.request;

/**
 * @author CJ
 */
public class AddPhotoRequest {
  private int album_id;
  private String name;
  private String photo_hash;
  private String imgurl;

  public AddPhotoRequest(int album_id, String name, String photo_hash, String imgurl) {
    this.album_id = album_id;
    this.name = name;
    this.photo_hash = photo_hash;
    this.imgurl = imgurl;
  }

  public int getAlbum_id() {
    return album_id;
  }

  public void setAlbum_id(int album_id) {
    this.album_id = album_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoto_hash() {
    return photo_hash;
  }

  public void setPhoto_hash(String photo_hash) {
    this.photo_hash = photo_hash;
  }

  public String getImgurl() {
    return imgurl;
  }

  public void setImgurl(String imgurl) {
    this.imgurl = imgurl;
  }
}
