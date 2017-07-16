package com.home77.kake;

import android.app.Application;

import org.greenrobot.eventbus.EventBus;

/**
 * @author CJ
 */
public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
  }

  // setup eventbus
  private static EventBus EVENTBUS;

  static {
    EVENTBUS = EventBus.builder().eventInheritance(false).build();
  }

  public static EventBus eventBus() {
    return EVENTBUS;
  }
}
