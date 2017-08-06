package com.home77.kake.common.api.request;

/**
 * @author CJ
 */
public class UpdateUserNameRequest {
  private int user_id;
  private String newname;

  public UpdateUserNameRequest(int user_id, String newname) {
    this.user_id = user_id;
    this.newname = newname;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public String getNewname() {
    return newname;
  }

  public void setNewname(String newname) {
    this.newname = newname;
  }
}
