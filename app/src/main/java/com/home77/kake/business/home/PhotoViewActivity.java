package com.home77.kake.business.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.github.chrisbanes.photoview.PhotoView;
import com.home77.kake.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

public class PhotoViewActivity extends AppCompatActivity {

  public static final String EXTRA_PHOTO = "extra_photo";
  private String path;
  private PhotoView photoView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_normal_photo_activity);
    init();
  }

  private void init() {
    path = getIntent().getStringExtra(EXTRA_PHOTO);
    photoView = (PhotoView) findViewById(R.id.photo_view);
    if (path.contains("http")) {
      Picasso.with(this).load(path).into(new Target() {
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
    } else {
      Picasso.with(this).load(new File(path)).into(new Target() {
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
  }

  public static void start(Context context, String path) {
    Intent intent = new Intent(context, PhotoViewActivity.class);
    intent.putExtra(EXTRA_PHOTO, path);
    context.startActivity(intent);
  }
}
