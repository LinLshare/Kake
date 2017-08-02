package com.home77.kake.business.theta;

/**
 * @author CJ
 */
public class Options {
  private int clientVersion;

  public static Options create() {
    return new Options();
  }

  public int getClientVersion() {
    return clientVersion;
  }

  public Options setClientVersion(int clientVersion) {
    this.clientVersion = clientVersion;
    return this;
  }
}
