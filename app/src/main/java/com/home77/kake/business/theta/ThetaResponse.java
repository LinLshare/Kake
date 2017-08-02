package com.home77.kake.business.theta;

/**
 * @author CJ
 */
public class ThetaResponse<T> {
  private String name;
  private String state;
  private T results;

  public String getName() {
    return name;
  }

  public String getState() {
    return state;
  }

  public T getResults() {
    return results;
  }
}
