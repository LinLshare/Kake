package com.home77.kake.business.user.service.request;

import java.io.Serializable;

/**
 * @author CJ
 */
public class RegisterRequest implements Serializable {

  /**
   * mobile : mobile
   * code : 054669
   * password : password
   * repassword : password
   */

  private String mobile;
  private String code;
  private String password;
  private String repassword;

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRepassword() {
    return repassword;
  }

  public void setRepassword(String repassword) {
    this.repassword = repassword;
  }
}
