package com.home77.kake.business.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import com.home77.common.base.collection.Params;
import com.home77.common.base.debug.DLog;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.camera.CameraActivity;
import com.home77.kake.business.home.presenter.CameraUnlinkPresenter;
import com.home77.kake.business.home.presenter.CloudAlbumListPresenter;
import com.home77.kake.business.home.presenter.LocalPhotoPresenter;
import com.home77.kake.business.home.view.CameraUnlinkFragment;
import com.home77.kake.business.home.view.CloudAlbumListListFragment;
import com.home77.kake.business.home.view.LocalPhotoFragment;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.common.adapter.FragmentPagerAdapter;
import com.home77.kake.common.api.ServerConfig;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.home77.kake.common.widget.ScrollConfigurableViewPager;
import com.home77.kake.common.widget.bottombar.ImageBottomItem;
import com.home77.kake.common.widget.bottombar.MainBottomBar;
import com.home77.kake.common.widget.bottombar.NormalBottomItem;
import com.theta360.v2.network.DeviceInfo;
import com.theta360.v2.network.HttpConnector;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeActivity extends AppCompatActivity
    implements MainBottomBar.OnTabItemClickListener, NavigateCallback {

  public static final int NAVIGATE_TO_USER = 101;
  private static final String TAG = HomeActivity.class.getSimpleName();
  @BindView(R.id.main_pager)
  ScrollConfigurableViewPager pagerMainTab;
  @BindView(R.id.main_bottom_bar)
  MainBottomBar mainBottomBar;
  @BindView(R.id.title_text_view)
  TextView titleTextView;
  private Unbinder unbinder;
  private LocalPhotoPresenter localPhotoPresenter;
  private CloudAlbumListPresenter cloudAlbumListPresenter;
  private CameraUnlinkPresenter cameraUnlinkPresenter;
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
  private CameraUnlinkFragment cameraUnlinkFragment;

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    //    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.eventBus().register(this);
    setContentView(R.layout.activity_home);
    unbinder = ButterKnife.bind(this);
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
    registerReceiver(wifiReceiver, intentFilter);
    mainBottomBar.addBottomBarItem(new NormalBottomItem(this,
                                                        R.drawable.tab_local_album_selector,
                                                        R.string.local_photo));
    mainBottomBar.addBottomBarItem(new ImageBottomItem(this, R.drawable.tab_camera));
    mainBottomBar.addBottomBarItem(new NormalBottomItem(this,
                                                        R.drawable.tab_cloud_album_selector,
                                                        R.string.cloud_album));
    mainBottomBar.setOnTabItemClickListener(this);

    LocalPhotoFragment localPhotoFragment = new LocalPhotoFragment();
    // setup presenter
    localPhotoPresenter = new LocalPhotoPresenter(localPhotoFragment);
    localPhotoFragment.setPresenter(localPhotoPresenter);

    CloudAlbumListListFragment cloudAlbumListFragment = new CloudAlbumListListFragment();
    cloudAlbumListPresenter = new CloudAlbumListPresenter(cloudAlbumListFragment, this);
    cloudAlbumListFragment.setPresenter(cloudAlbumListPresenter);

    cameraUnlinkFragment = new CameraUnlinkFragment();
    cameraUnlinkPresenter = new CameraUnlinkPresenter(cameraUnlinkFragment);
    cameraUnlinkFragment.setPresenter(cameraUnlinkPresenter);

    ArrayList<Fragment> fragmentList = new ArrayList<>();
    fragmentList.add(localPhotoFragment);
    fragmentList.add(cameraUnlinkFragment);
    fragmentList.add(cloudAlbumListFragment);
    FragmentPagerAdapter adapter =
        new FragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
    pagerMainTab.setAdapter(adapter);
    pagerMainTab.setOffscreenPageLimit(fragmentList.size());
  }

  @Override
  protected void onResume() {
    super.onResume();

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
          App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.CAMERA_LINKED, null));
        } else {
          App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.CAMERA_UNLINKED, null));
        }
      }
    }.execute();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    App.eventBus().unregister(this);
    unregisterReceiver(wifiReceiver);
    unbinder.unbind();
  }

  @Override
  public void onTabItemClick(int index) {
    switch (index) {
      case 0:
        titleTextView.setText(R.string.local_photo);
        pagerMainTab.setCurrentItem(index, false);
        localPhotoPresenter.start(null);
        break;
      case 1: {
        if (App.isIsLinckedCamera()) {
          Intent intent = new Intent(this, CameraActivity.class);
          intent.putExtra(CameraActivity.EXTRA_LAST_PHOTO_PATH,
                          localPhotoPresenter.getRecentPhotoPath());
          startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } else {
          titleTextView.setText("咔客全景相机");
          pagerMainTab.setCurrentItem(index, false);
          mainBottomBar.selectBottomBarItem(1);
        }
      }
      break;
      case 2:
        titleTextView.setText(R.string.cloud_album);
        pagerMainTab.setCurrentItem(index, false);
        cloudAlbumListPresenter.start(null);
        break;
    }
  }

  private long exitTime = 0;

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (KeyEvent.KEYCODE_BACK == keyCode) {
      if (System.currentTimeMillis() - exitTime > 2000) {
        Toast.showShort("再按一次退出应用");
        exitTime = System.currentTimeMillis();
      } else {
        this.finish();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  private static final int REQUEST_CODE_USER = 101_1;
  private static final int REQUEST_CODE_USER_LOGIN = 101_2;
  public static final int RESULT_CODE_CLOUD_ALBUM = 201_1;
  public static final int REQUEST_CODE_CAMERA = 301_1;
  public static final int RESULT_CODE_LOCAL_ALBUM = 301_2;

  @OnClick(R.id.user_image_view)
  public void onViewClicked() {
    Intent intent = new Intent(this, UserActivity.class);
    startActivityForResult(intent, REQUEST_CODE_USER);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_CODE_USER: {
        if (resultCode == RESULT_CODE_CLOUD_ALBUM) {
          titleTextView.setText(R.string.cloud_album);
          pagerMainTab.setCurrentItem(2, false);
          mainBottomBar.selectBottomBarItem(2);
          cloudAlbumListPresenter.start(null);
        }
      }
      break;
      case REQUEST_CODE_USER_LOGIN: {
        if (resultCode == RESULT_OK) {
          cloudAlbumListPresenter.start(null);
        }
      }
      break;
      case REQUEST_CODE_CAMERA: {
        if (resultCode == RESULT_CODE_LOCAL_ALBUM) {
          titleTextView.setText(R.string.local_photo);
          pagerMainTab.setCurrentItem(0, false);
          mainBottomBar.selectBottomBarItem(0);
          localPhotoPresenter.start(null);
        }
      }
      break;
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.ACTIVITY_CLOUD_PHOTO_LIST:
        Album album = event.getParams().get(ParamsKey.ALBUM);
        Intent intent = new Intent(this, CloudPhotoActivity.class);
        intent.putExtra(CloudPhotoActivity.EXTRA_ALBUM, album);
        startActivity(intent);
        break;
      case BroadCastEventConstant.CAMERA_LINKED:
        if (cameraUnlinkFragment != null) {
          cameraUnlinkFragment.link();
        }
        break;
      case BroadCastEventConstant.CAMERA_UNLINKED:
        if (cameraUnlinkFragment != null) {
          cameraUnlinkFragment.reset();
        }
        break;
    }
  }

  @Override
  public void onNavigate(int eventType, Params params) {
    switch (eventType) {
      case NAVIGATE_TO_USER:
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(UserActivity.EXTRA_FIRST_EVENT, UserActivity.EVENT_TO_LOGIN);
        startActivityForResult(intent, REQUEST_CODE_USER_LOGIN);
        break;
    }
  }
}
