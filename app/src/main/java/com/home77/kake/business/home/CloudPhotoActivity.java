package com.home77.kake.business.home;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.model.CloudPhoto;
import com.home77.kake.business.home.presenter.CloudPhotoListPresenter;
import com.home77.kake.business.home.view.CloudPhotoListFragment;
import com.home77.kake.business.home.view.GLPhotoActivity;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;

public class CloudPhotoActivity extends AppCompatActivity implements NavigateCallback {

  public static final int EVENT_EXIST = 1;

  public static final String EXTRA_ALBUM = "extra_album";
  private CloudPhotoListFragment cloudPhotoListFragment;
  private CloudPhotoListPresenter cloudPhotoListPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cloud_photo);
    App.eventBus().register(this);
    Album album = (Album) getIntent().getSerializableExtra(EXTRA_ALBUM);

    cloudPhotoListFragment = new CloudPhotoListFragment();
    getSupportFragmentManager().beginTransaction()
                               .add(R.id.content_layout, cloudPhotoListFragment)
                               .commit();
    cloudPhotoListPresenter = new CloudPhotoListPresenter(cloudPhotoListFragment, this);
    cloudPhotoListPresenter.setParam(album);
    cloudPhotoListFragment.setPresenter(cloudPhotoListPresenter);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    App.eventBus().unregister(this);
  }

  @Override
  public void onNavigate(final int eventType, final Params params) {
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

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.CLICK_CLOUD_PHOTO: {
        showGLPhotoActivity(event.getParams());
      }
      break;
    }
  }

  private void showGLPhotoActivity(Params in) {
    final CloudPhoto cloudPhoto = in.get(ParamsKey.CLOUD_PHOTO);
    Picasso.with(this)
           .load(cloudPhoto.getImgurl())
           .resize(100, 100)
           .centerCrop()
           .into(new Target() {
             @Override
             public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
               ByteArrayOutputStream stream = new ByteArrayOutputStream();
               bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
               final byte[] byteArray = stream.toByteArray();
               BaseHandler.runOnMainThread(new Runnable() {
                 @Override
                 public void run() {
                   GLPhotoActivity.startActivityForResult(CloudPhotoActivity.this,
                                                          byteArray,
                                                          cloudPhoto.getImgurl(),
                                                          cloudPhoto.getName(),
                                                          false);
                 }
               });
             }

             @Override
             public void onBitmapFailed(Drawable errorDrawable) {

             }

             @Override
             public void onPrepareLoad(Drawable placeHolderDrawable) {

             }
           });
  }
}
