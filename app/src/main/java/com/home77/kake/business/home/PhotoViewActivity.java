package com.home77.kake.business.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.home77.kake.R;
import com.home77.kake.business.home.model.LocalPhoto;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

public class PhotoViewActivity extends AppCompatActivity {

  public static final String EXTRA_PHOTO = "extra_photo";
  private LocalPhoto mLocalPhoto;
  private PhotoView photoView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_normal_photo_activity);
    initData();
    initView();
  }

  private void initView() {
    photoView = (PhotoView) findViewById(R.id.photo_view);
    Picasso.with(this).load(new File(mLocalPhoto.getPath())).resize(800, 0).into(new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        photoView.setImageBitmap(bitmap);
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
        photoView.setImageResource(R.drawable.image_placeholder);
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
        photoView.setImageResource(R.drawable.image_placeholder);
      }
    });
  }

  private void initData() {
    mLocalPhoto = (LocalPhoto) getIntent().getSerializableExtra(EXTRA_PHOTO);
  }

  public static void start(Context context, LocalPhoto localPhoto) {
    Intent intent = new Intent(context, PhotoViewActivity.class);
    intent.putExtra(EXTRA_PHOTO, localPhoto);
    context.startActivity(intent);
  }
}
