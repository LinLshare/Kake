package com.home77.kake.business.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.home77.common.base.collection.Params;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.business.home.presenter.CloudPhotoListPresenter;
import com.home77.kake.business.home.view.CloudPhotoListFragment;
import com.home77.kake.common.api.response.Album;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CloudPhotoActivity extends AppCompatActivity {

  private CloudPhotoListFragment cloudPhotoListFragment;
  private CloudPhotoListPresenter cloudPhotoListPresenter;

  public static final String EXTRA_ALBUM = "extra_album";
  private Album album;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cloud_photo);
    album = (Album) getIntent().getSerializableExtra(EXTRA_ALBUM);
    App.eventBus().register(this);
    cloudPhotoListFragment = new CloudPhotoListFragment();
    cloudPhotoListPresenter = new CloudPhotoListPresenter(cloudPhotoListFragment);
    cloudPhotoListFragment.setPresenter(cloudPhotoListPresenter);
    getSupportFragmentManager().beginTransaction()
                               .add(R.id.content_layout, cloudPhotoListFragment)
                               .commit();

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    App.eventBus().unregister(this);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(NavEvent event) {
    switch (event.getEvent()) {
      case NavEvent.CLICK_BACK:
        this.finish();
        break;
    }
  }

  public static class NavEvent {
    public static final int CLICK_BACK = 1001;
    private final Object from;
    private final int event;
    private final Params params;

    public Object getFrom() {
      return from;
    }

    public int getEvent() {
      return event;
    }

    public Params getParams() {
      return params;
    }

    public NavEvent(Object from, int event, Params params) {
      this.from = from;
      this.event = event;
      this.params = params;
    }
  }
}
