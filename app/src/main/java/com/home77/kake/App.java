package com.home77.kake;

import android.app.Application;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.component.ContextManager;

import org.greenrobot.eventbus.EventBus;

/**
 * @author CJ
 */
public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // 1). init context
    ContextManager.init(this);
    // 2). open basehandler
    BaseHandler.open();
    // 3). restore global data
    GLOBAL_DATA = new GlobalData(this);
    GLOBAL_DATA.restore();
  }

  // setup eventbus
  private static EventBus EVENTBUS;

  static {
    EVENTBUS = EventBus.builder().eventInheritance(false).build();
  }

  public static EventBus eventBus() {
    return EVENTBUS;
  }

  private static GlobalData GLOBAL_DATA;

  public static GlobalData globalData() {
    return GLOBAL_DATA;
  }
}
