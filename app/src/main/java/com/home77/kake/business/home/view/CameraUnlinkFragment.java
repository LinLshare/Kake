package com.home77.kake.business.home.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.home77.common.base.collection.Params;
import com.home77.common.base.debug.DLog;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.common.api.ServerConfig;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.theta360.v2.network.DeviceInfo;
import com.theta360.v2.network.HttpConnector;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class CameraUnlinkFragment extends BaseFragment {
  private static final String TAG = CameraUnlinkFragment.class.getSimpleName();
  Unbinder unbinder;
  BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (TextUtils.isEmpty(action)) {
        return;
      }
      if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        switch (wifiState) {
          case WifiManager.WIFI_STATE_ENABLED:
            DLog.d(TAG, "check device link");
            // check device
            new AsyncTask<Void, String, Boolean>() {
              @Override
              protected Boolean doInBackground(Void... params) {
                HttpConnector camera = new HttpConnector(ServerConfig.CAMERA_HOST);
                DeviceInfo deviceInfo = camera.getDeviceInfo();
                return !deviceInfo.getSerialNumber().isEmpty();
              }

              @Override
              protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                  App.eventBus()
                     .post(new BroadCastEvent(BroadCastEventConstant.CAMERA_LINKED, null));
                } else {
                  App.eventBus()
                     .post(new BroadCastEvent(BroadCastEventConstant.CAMERA_UNLINKED, null));
                }
              }
            }.execute();
            break;
          case WifiManager.WIFI_STATE_DISABLED:
            App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.CAMERA_UNLINKED, null));
            break;
        }
      }
    }
  };

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View view =
            LayoutInflater.from(getContext()).inflate(R.layout.fragment_camera_unlink, null);
        unbinder = ButterKnife.bind(this, view);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        getContext().registerReceiver(wifiReceiver, intentFilter);
        out.put(ParamsKey.VIEW, view);
        break;
      case VIEW_DESTROY:
        getContext().unregisterReceiver(wifiReceiver);
        unbinder.unbind();
        break;
      case OPEN_WIFI_SETTING:
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
        break;
    }
  }

  @OnClick(R.id.link_camera_text_view)
  public void onViewClicked() {
    presenter.onMessage(MsgType.CLICK_LINCK_CAMERA, null);
  }
}
