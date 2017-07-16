package com.home77.common.base.event;

public class GenericEvent {
  public final int eventType;

  public GenericEvent(Object sender) {
    this.eventType = -1;
    this.sender = sender;
  }

  public GenericEvent(Object sender, int eventType) {
    this.eventType = eventType;
    this.sender = sender;
  }

  // sender

  private final Object sender;

  public Object sender() {
    return sender;
  }

  public boolean isSameSender(Object target) {
    return (sender == target) || (sender != null && sender.equals(target));
  }
}
