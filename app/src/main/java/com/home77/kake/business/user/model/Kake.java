package com.home77.kake.business.user.model;

/**
 * @author CJ
 */
public class Kake {

  /**
   * id : 7
   * name : 33
   * view : 0
   * user_id : 1
   * created_at : 2017-07-31 02:17:44
   * ship_user : {"id":1,"name":"glg","avatar":"http://home77.oss-cn-shenzhen.aliyuncs.com/2017/08/18105758AYOY.png"}
   * url : http://a.dev/vr/7
   */

  private int id;
  private String name;
  private int view;
  private int user_id;
  private String created_at;
  private ShipUserBean ship_user;
  private String url;
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

  public int getView() {
    return view;
  }

  public void setView(int view) {
    this.view = view;
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

  public ShipUserBean getShip_user() {
    return ship_user;
  }

  public void setShip_user(ShipUserBean ship_user) {
    this.ship_user = ship_user;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }

  public static class ShipUserBean {
    /**
     * id : 1
     * name : glg
     * avatar : http://home77.oss-cn-shenzhen.aliyuncs.com/2017/08/18105758AYOY.png
     */

    private int id;
    private String name;
    private String avatar;

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

    public String getAvatar() {
      return avatar;
    }

    public void setAvatar(String avatar) {
      this.avatar = avatar;
    }
  }
}
