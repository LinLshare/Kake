package com.home77.kake.business.user.service.request;

import java.io.Serializable;

/**
 * @author CJ
 */
public class TokenValidateRequest implements Serializable {

  /**
   * grant_type : password
   * client_id : 3
   * client_secret : V59K95xzfdhjNlcq3TZaW9KWJwufgp6w8RH8uFU4
   * username : 13660243896
   * password : aaa111
   * scope :
   */

  private String grant_type;
  private int client_id;
  private String client_secret;
  private String username;
  private String password;
  private String scope;

  public TokenValidateRequest(String grant_type,
                              int client_id,
                              String client_secret,
                              String username,
                              String password,
                              String scope) {
    this.grant_type = grant_type;
    this.client_id = client_id;
    this.client_secret = client_secret;
    this.username = username;
    this.password = password;
    this.scope = scope;
  }

  public String getGrant_type() {
    return grant_type;
  }

  public void setGrant_type(String grant_type) {
    this.grant_type = grant_type;
  }

  public int getClient_id() {
    return client_id;
  }

  public void setClient_id(int client_id) {
    this.client_id = client_id;
  }

  public String getClient_secret() {
    return client_secret;
  }

  public void setClient_secret(String client_secret) {
    this.client_secret = client_secret;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}
