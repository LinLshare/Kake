package com.home77.kake.business.theta;

/**
 * @author CJ
 */
public class StartSessionResult {

  /**
   * sessionId : SID_0001
   * timeout : 180
   */

  private String sessionId;
  private int timeout;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }
}
