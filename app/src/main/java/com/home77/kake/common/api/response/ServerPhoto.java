package com.home77.kake.common.api.response;

import com.home77.kake.base.Mapper;
import com.home77.kake.business.home.model.LocalPhoto;

/**
 * @author CJ
 */
public class ServerPhoto implements Mapper<LocalPhoto> {
  private int id;
  private int album_id;
  private String name;
  private String imgurl;
  private String photo_hash;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  @Override
  public LocalPhoto map() {
    return new LocalPhoto(id, name, 0, 0, imgurl);
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(String updated_at) {
    this.updated_at = updated_at;
  }
}
