package com.home77.kake.common.api.response;

import java.io.Serializable;

/**
 * @author CJ
 */
public class Album implements Serializable {

  /**
   * created_at : 2017-08-05 12:30:02
   * id : 12
   * name : 我是谁
   * photos_hash :
   * status : 3
   * updated_at : 2017-08-05 12:30:02
   * user_id : 196
   */

  private String created_at;
  private int id;
  private String name;
  private String photos_hash;
  private int status;
  private String updated_at;
  private int user_id;

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

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

  public String getPhotos_hash() {
    return photos_hash;
  }

  public void setPhotos_hash(String photos_hash) {
    this.photos_hash = photos_hash;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(String updated_at) {
    this.updated_at = updated_at;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }
}

