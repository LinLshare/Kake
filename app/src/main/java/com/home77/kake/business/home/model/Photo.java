package com.home77.kake.business.home.model;

/**
 * @author CJ
 */
public class Photo {
  public static final int TYPE_NORMAL = 0;
  public static final int TYPE_GROUP = 1;
  private int type;
  private int id;
  private int album_id;
  private String rename;
  private String imgurl;
  private String photo_hash;
  private String created_at;
  private String updated_at;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAlbum_id() {
    return album_id;
  }

  public void setAlbum_id(int album_id) {
    this.album_id = album_id;
  }

  public String getRename() {
    return rename;
  }

  public void setRename(String rename) {
    this.rename = rename;
  }

  public String getImgurl() {
    return imgurl;
  }

  public void setImgurl(String imgurl) {
    this.imgurl = imgurl;
  }

  public String getPhoto_hash() {
    return photo_hash;
  }

  public void setPhoto_hash(String photo_hash) {
    this.photo_hash = photo_hash;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(String updated_at) {
    this.updated_at = updated_at;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
