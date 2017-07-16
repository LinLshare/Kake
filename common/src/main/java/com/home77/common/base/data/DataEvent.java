package com.home77.common.base.data;

import com.home77.common.base.collection.Params;
import com.home77.common.base.event.GenericEvent;

public class DataEvent extends GenericEvent {
  public final int eventId;
  public final Params values;

  public DataEvent(Object sender, int id, Params p) {
    super(sender);
    eventId = id;
    values = p;
  }
}
