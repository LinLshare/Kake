package com.home77.common.base.event;

/**
 * A loop event use in doLoop() for recording next state and return value.
 */
public final class LoopEvent extends GenericEvent {
  public final int nextState;
  public final int rv;

  public LoopEvent(Object sender, int nextState, int rv) {
    super(sender);
    this.nextState = nextState;
    this.rv = rv;
  }
}
