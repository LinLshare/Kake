package com.home77.kake.common.api.response;

import java.io.Serializable;

/**
 * @author CJ
 */
public class Album implements Serializable {

  public static final int STATUS_GENERATED = 1;
  public static final int STATUS_GENERATING = 2;
  public static final int STATUS_NONE = 3;

  /**
   * id : 2
   * name : 相册B
   * status : 1
   * checked_status : 1
   * is_public : 1
   * user_id : 1
   * created_at : 2017-06-05 11:02:09
   * updated_at : 2017-09-14 16:11:02
   * panourl : http://www.manager.com/api/v1/vr/2
   * cover : http://control.home77.com/vr/2017/06/20152713TlW2.jpg
   */
  private int id;
  private String name;
  private int status;
  private int checked_status;
  private int is_public;
  private int user_id;
  private String created_at;
  private String updated_at;
  private String panourl;
  private String cover;

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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getChecked_status() {
    return checked_status;
  }

  public void setChecked_status(int checked_status) {
    this.checked_status = checked_status;
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

  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }
}

