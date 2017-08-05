package com.home77.kake;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.component.ContextManager;
import com.home77.common.ui.widget.LoadingDialog;
import com.home77.kake.business.theta.ThetaCameraApi;
import com.home77.kake.business.theta.ThetaCameraApiImpl;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    // 5). register eventBus
    eventBus().register(this);
    // 6). register lifecycle
    registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
      @Override
      public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
          loadingDialog.dismiss();
        }
        loadingDialog = new LoadingDialog(activity);
      }

      @Override
      public void onActivityStarted(Activity activity) {
      }

      @Override
      public void onActivityResumed(Activity activity) {
      }

      @Override
      public void onActivityPaused(Activity activity) {
      }

      @Override
      public void onActivityStopped(Activity activity) {
      }

      @Override
      public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
      }

      @Override
      public void onActivityDestroyed(Activity activity) {
      }
    });
  }

  // setup eventbus
  private static EventBus EVENTBUS;
  private static ThetaCameraApi THETA_CAMERA_API;
  private LoadingDialog loadingDialog;

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

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.DIALOG_LOADING_SHOW:
        if (loadingDialog != null) {
          loadingDialog.show();
        }
        break;
      case BroadCastEventConstant.DIALOG_LOADING_DISMISS:
        if (loadingDialog != null) {
          loadingDialog.dismiss();
        }
        break;
    }
  }
}
