package com.home77.kake.business.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.home77.common.base.component.BaseHandler;
import com.home77.kake.R;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.business.home.presenter.CloudPhotoListPresenter;
import com.home77.kake.business.home.view.CloudPhotoListFragment;
import com.home77.kake.common.api.response.Album;

public class CloudPhotoActivity extends AppCompatActivity implements NavigateCallback {

  public static final int EVENT_EXIST = 1;
  private CloudPhotoListPresenter cloudPhotoListPresenter;

  public static final String EXTRA_ALBUM = "extra_album";
  private Album album;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cloud_photo);
    album = (Album) getIntent().getSerializableExtra(EXTRA_ALBUM);

    CloudPhotoListFragment cloudPhotoListFragment = new CloudPhotoListFragment();
    cloudPhotoListPresenter = new CloudPhotoListPresenter(cloudPhotoListFragment, this);
    cloudPhotoListPresenter.setParam(album);
    cloudPhotoListFragment.setPresenter(cloudPhotoListPresenter);
    getSupportFragmentManager().beginTransaction()
                               .add(R.id.content_layout, cloudPhotoListFragment)
                               .commit();
  }

  @Override
  public void onNavigate(final int eventType) {
    BaseHandler.runOnMainThread(new Runnable() {
      @Override
      public void run() {
        switch (eventType) {
          case EVENT_EXIST:
            CloudPhotoActivity.this.finish();
            break;
        }
      }
    });
  }
}
