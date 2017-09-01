package com.home77.kake;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.component.ContextManager;
import com.home77.common.net.util.NetHelper;
import com.home77.common.ui.widget.LoadingDialog;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author CJ
 */
public class App extends Application {

  private static boolean IS_LOGIN;
  private static boolean IS_LINCKED_CAMERA;

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
    IS_LOGIN = GLOBAL_DATA.getInt(GlobalData.KEY_USER_ID, 0) != 0;
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
    // 7) check and connect to wifi
    checkAndConnectToWifi();
    registerReceiver(wifiChangedReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
  }

  private BroadcastReceiver wifiChangedReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (!TextUtils.isEmpty(action) && action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
        int wifiState =
            intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
        switch (wifiState) {
          case WifiManager.WIFI_STATE_ENABLED:
            hasConnectedToWifi = true;
            eventBus().post(new BroadCastEvent(BroadCastEventConstant.WIFI_CONNECTED, null));
            break;
          case WifiManager.WIFI_STATE_DISABLED:
            hasConnectedToWifi = false;
            eventBus().post(new BroadCastEvent(BroadCastEventConstant.WIFI_LOST, null));
            break;
        }
      }
    }
  };

  // setup eventbus
  private static EventBus EVENTBUS;
  private LoadingDialog loadingDialog;
  private static boolean hasConnectedToWifi = false;

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

  public static boolean isLogin() {
    return IS_LOGIN;
  }

  public static boolean isIsLinckedCamera() {
    return IS_LINCKED_CAMERA;
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.DIALOG_LOADING_SHOW:
        if (loadingDialog != null && !loadingDialog.isShowing()) {
          loadingDialog.show();
        }
        break;
      case BroadCastEventConstant.DIALOG_LOADING_DISMISS:
        if (loadingDialog != null) {
          loadingDialog.dismiss();
        }
        break;
      case BroadCastEventConstant.EVENT_LOGOUT:
        GLOBAL_DATA.clear();
        IS_LOGIN = false;
        Toast.showShort("已退出登录");
        break;
      case BroadCastEventConstant.EVENT_LOGIN:
        IS_LOGIN = true;
        break;
      case BroadCastEventConstant.CAMERA_LINKED:
        IS_LINCKED_CAMERA = true;
        break;
      case BroadCastEventConstant.CAMERA_UNLINKED:
        IS_LINCKED_CAMERA = false;
        break;
    }
  }

  public static boolean hasConnectedToWifi() {
    return hasConnectedToWifi;
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void checkAndConnectToWifi() {
    boolean wifiAvailable = NetHelper.isWifiAvailable();
    if (wifiAvailable) {
      // AP connected
      hasConnectedToWifi = true;
    } else {
      ConnectivityManager cm = (ConnectivityManager) ContextManager.appContext()
                                                                   .getSystemService(Context.CONNECTIVITY_SERVICE);
      cm.registerNetworkCallback(new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                                                             .build(),
                                 new ConnectivityManager.NetworkCallback() {
                                   @Override
                                   public void onAvailable(Network network) {
                                     super.onAvailable(network);
                                     //AP connected
                                     hasConnectedToWifi = true;
                                   }

                                   @Override
                                   public void onLost(Network network) {
                                     super.onLost(network);
                                     //Ap lost
                                     hasConnectedToWifi = false;
                                     IS_LINCKED_CAMERA = false;
                                   }
                                 });
    }
  }
}
