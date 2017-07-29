package com.home77.kake.business.user.service.response;

/**
 * @author CJ
 */
public class UserResponse {

  /**
   * id : 1
   * oldid : 0
   * is_broker : 1
   * stores_id : 1
   * is_buinour : 0
   * name : qwer
   * mobile : 13660243896
   * email : null
   * email_check : 0
   * status : 1
   * avatar : 2017/06/05153729oHPl.jpg
   * store_img : 2017/06/08100207CL1a.jpg
   * qrcode :
   * created_at : 2017-05-09 18:34:05
   * updated_at : 2017-06-13 17:14:29
   */

  private int id;
  private int oldid;
  private int is_broker;
  private int stores_id;
  private int is_buinour;
  private String name;
  private String mobile;
  private Object email;
  private int email_check;
  private int status;
  private String avatar;
  private String store_img;
  private String qrcode;
  private String created_at;
  private String updated_at;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getOldid() {
    return oldid;
  }

  public void setOldid(int oldid) {
    this.oldid = oldid;
  }

  public int getIs_broker() {
    return is_broker;
  }

  public void setIs_broker(int is_broker) {
    this.is_broker = is_broker;
  }

  public int getStores_id() {
    return stores_id;
  }

  public void setStores_id(int stores_id) {
    this.stores_id = stores_id;
  }

  public int getIs_buinour() {
    return is_buinour;
  }

  public void setIs_buinour(int is_buinour) {
    this.is_buinour = is_buinour;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public Object getEmail() {
    return email;
  }

  public void setEmail(Object email) {
    this.email = email;
  }

  public int getEmail_check() {
    return email_check;
  }

  public void setEmail_check(int email_check) {
    this.email_check = email_check;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getStore_img() {
    return store_img;
  }

  public void setStore_img(String store_img) {
    this.store_img = store_img;
  }

  public String getQrcode() {
    return qrcode;
  }

  public void setQrcode(String qrcode) {
    this.qrcode = qrcode;
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
}
