package com.home77.kake;

import android.app.Application;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.component.ContextManager;
import com.home77.kake.business.theta.ThetaCameraApi;
import com.home77.kake.business.theta.ThetaCameraApiImpl;

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
    // 4). setup theta api

  }

  // setup eventbus
  private static EventBus EVENTBUS;
  private static ThetaCameraApi THETA_CAMERA_API;

  static {
    EVENTBUS = EventBus.builder().eventInheritance(false).build();
    THETA_CAMERA_API = new ThetaCameraApiImpl();
  }

  public static EventBus eventBus() {
    return EVENTBUS;
  }

  public static ThetaCameraApi thetaCameraApi() {
    return THETA_CAMERA_API;
  }

  private static GlobalData GLOBAL_DATA;

  public static GlobalData globalData() {
    return GLOBAL_DATA;
  }
}
