package com.home77.kake.common.api.response;

import com.home77.kake.business.home.model.CloudPhoto;

import java.util.List;

/**
 * @author CJ
 */
public class PhotoListResponse extends Response<PhotoListResponse> {
  private int id;
  private String name;
  private int checked_status;
  private String url;
  private int is_public;
  private int user_id;
  private String photos_hash;
  private String created_at;
  private String updated_at;
  private String panourl;
  private List<CloudPhoto> ship_photos;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getChecked_status() {
    return checked_status;
  }

  public void setChecked_status(int checked_status) {
    this.checked_status = checked_status;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getIs_public() {
    return is_public;
  }

  public void setIs_public(int is_public) {
    this.is_public = is_public;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public String getPhotos_hash() {
    return photos_hash;
  }

  public void setPhotos_hash(String photos_hash) {
    this.photos_hash = photos_hash;
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

  public String getPanourl() {
    return panourl;
  }

  public void setPanourl(String panourl) {
    this.panourl = panourl;
  }

  public List<CloudPhoto> getShip_photos() {
    return ship_photos;
  }

  public void setShip_photos(List<CloudPhoto> ship_photos) {
    this.ship_photos = ship_photos;
  }
}
