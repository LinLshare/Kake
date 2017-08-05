package com.home77.kake.common.event;

import com.home77.common.base.collection.Params;

/**
 * @author CJ
 */
public class BroadCastEvent {
  private final int event;
  private final Params params;

  public BroadCastEvent(int event, Params params) {
    this.event = event;
    this.params = params;
  }

  public int getEvent() {
    return event;
  }

  public Params getParams() {
    return params;
  }

}
