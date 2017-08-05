package com.home77.kake.business.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.home77.common.base.event.GenericEvent;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.business.home.presenter.CloudAlbumListPresenter;
import com.home77.kake.business.home.presenter.LocalPhotoPresenter;
import com.home77.kake.business.home.view.CloudAlbumListListFragment;
import com.home77.kake.business.home.view.LocalPhotoFragment;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.common.adapter.FragmentPagerAdapter;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.home77.kake.common.event.ParamKey;
import com.home77.kake.common.widget.ScrollConfigurableViewPager;
import com.home77.kake.common.widget.bottombar.ImageBottomItem;
import com.home77.kake.common.widget.bottombar.MainBottomBar;
import com.home77.kake.common.widget.bottombar.NormalBottomItem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity
    implements MainBottomBar.OnTabItemClickListener {

  @BindView(R.id.main_pager)
  ScrollConfigurableViewPager pagerMainTab;
  @BindView(R.id.main_bottom_bar)
  MainBottomBar mainBottomBar;
  @BindView(R.id.title_text_view)
  TextView titleTextView;
  private CloudAlbumListListFragment cloudAlbumListFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.eventBus().register(this);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);

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
    LocalPhotoPresenter localPhotoPresenter = new LocalPhotoPresenter(localPhotoFragment);
    localPhotoFragment.setPresenter(localPhotoPresenter);

    cloudAlbumListFragment = new CloudAlbumListListFragment();
    CloudAlbumListPresenter cloudAlbumListPresenter =
        new CloudAlbumListPresenter(cloudAlbumListFragment);
    cloudAlbumListFragment.setPresenter(cloudAlbumListPresenter);

    ArrayList<Fragment> fragmentList = new ArrayList<>();
    fragmentList.add(localPhotoFragment);
    fragmentList.add(new Fragment()); //占位
    fragmentList.add(cloudAlbumListFragment);
    FragmentPagerAdapter adapter =
        new FragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
    pagerMainTab.setAdapter(adapter);
    pagerMainTab.setOffscreenPageLimit(fragmentList.size());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    App.eventBus().unregister(this);
  }

  @Override
  public void onTabItemClick(int index) {
    switch (index) {
      case 0:
        titleTextView.setText(R.string.local_photo);
        pagerMainTab.setCurrentItem(index, false);
        break;
      case 1:
        break;
      case 2:
        titleTextView.setText(R.string.cloud_album);
        pagerMainTab.setCurrentItem(index, false);
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

  @OnClick(R.id.user_image_view)
  public void onViewClicked() {
    startActivity(new Intent(this, UserActivity.class));
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(GenericEvent navigateEvent) {
    if (!navigateEvent.isSameSender(this)) {
      return;
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.ACTIVITY_CLOUD_PHOTO_LIST:
        Album album = event.getParams().get(ParamKey.ALBUM);
        Intent intent = new Intent(this, CloudPhotoActivity.class);
        intent.putExtra(CloudPhotoActivity.EXTRA_ALBUM, album);
        startActivity(intent);
        break;
    }
  }
}
