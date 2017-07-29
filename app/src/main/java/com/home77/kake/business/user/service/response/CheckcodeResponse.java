package com.home77.kake.business.user.service.response;

/**
 * @author CJ
 */
public class CheckcodeResponse {

  /**
   * success : true
   * type : sms_sent_success
   * message : 短信验证码发送成功，请注意查收
   */

  private boolean success;
  private String type;
  private String message;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
