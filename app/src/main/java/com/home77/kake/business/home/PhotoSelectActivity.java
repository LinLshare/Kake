package com.home77.kake.business.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.home77.common.base.debug.DLog;
import com.home77.common.ui.util.SizeHelper;
import com.home77.kake.R;
import com.home77.kake.business.home.adapter.SelectPhotoListAdapter;
import com.home77.kake.business.home.model.LocalPhoto;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.widget.recyclerview.DefaultGridItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoSelectActivity extends AppCompatActivity {

  private static final String TAG = PhotoSelectActivity.class.getSimpleName();
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.loading_layout)
  ProgressBar loadingLayout;
  @BindView(R.id.empty_layout)
  TextView emptyLayout;
  private List<LocalPhoto> cloudPhotoList = new ArrayList<>();
  private SelectPhotoListAdapter selectPhotoListAdapter;
  private static final String EXTRA_ALBUM = "extra_album";
  private Album album;

  public static void start(Context context, Album album) {
    Intent intent = new Intent(context, PhotoSelectActivity.class);
    intent.putExtra(EXTRA_ALBUM, album);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo_select);
    ButterKnife.bind(this);
    init();
    new LoadLocalPhotoListTask().execute();
  }

  private void init() {
    album = (Album) getIntent().getSerializableExtra(EXTRA_ALBUM);
    final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
    recyclerView.setLayoutManager(gridLayoutManager);

    selectPhotoListAdapter = new SelectPhotoListAdapter(this, cloudPhotoList);
    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        int spanSize = 1;
        LocalPhoto localPhoto = selectPhotoListAdapter.getDatas().get(position);
        if (localPhoto != null && localPhoto.isTitle()) {
          spanSize = 3;
        }
        return spanSize;
      }
    });
    recyclerView.setAdapter(selectPhotoListAdapter);
    recyclerView.addItemDecoration(new DefaultGridItemDecoration(SizeHelper.dp(12)));
  }

  @OnClick({R.id.back_image_view, R.id.ok_text_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        this.finish();
        break;
      case R.id.ok_text_view:
        break;
    }
  }

  private class LoadLocalPhotoListTask extends AsyncTask<Void, String, List<LocalPhoto>> {
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      emptyLayout.setVisibility(View.GONE);
      recyclerView.setVisibility(View.GONE);
      loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<LocalPhoto> doInBackground(Void... params) {
      File pictureDir =
          new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                   "kake");
      if (!pictureDir.exists()) {
        boolean b = pictureDir.mkdir();
      }
      ArrayList<LocalPhoto> temp = new ArrayList<>();
      File[] files = pictureDir.listFiles();
      if (files == null) {
        return null;
      }
      for (File f : files) {
        if (f.getName().endsWith("jpg")) {
          temp.add(new LocalPhoto(f.hashCode(),
                                  f.getName().split("\\.jpg")[0],
                                  f.length(),
                                  f.lastModified(),
                                  f.getAbsolutePath()));
          DLog.d(TAG, "jpg: " + f.getName() + " # " + f.lastModified());
        }
      }
      Collections.sort(temp, new Comparator<LocalPhoto>() {
        @Override
        public int compare(LocalPhoto o1, LocalPhoto o2) {
          return (int) (o2.getDate() - o1.getDate());
        }
      });
      List<LocalPhoto> temp2 = new ArrayList<>(temp);
      int lastDay = 0;
      int titleCount = 0;
      for (int i = 0; i < temp.size(); i++) {
        LocalPhoto photo = temp.get(i);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(photo.getDate());
        int currentDay = instance.get(Calendar.DAY_OF_MONTH);
        if (lastDay != currentDay) {
          temp2.add(i + titleCount, LocalPhoto.makeTitle(photo.getDate()));
          titleCount++;
          lastDay = currentDay;
        }
      }
      return temp2;
    }

    @Override
    protected void onPostExecute(List<LocalPhoto> localPhotos) {
      super.onPostExecute(localPhotos);
      if (localPhotos == null) {
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
      } else {
        cloudPhotoList.clear();
        cloudPhotoList.addAll(localPhotos);
        selectPhotoListAdapter.notifyDataSetChanged();
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
      }
    }
  }
}
