package com.home77.kake.business.user;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.common.ui.util.SizeHelper;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.R;
import com.home77.kake.business.user.adapter.KakeListAdapter;
import com.home77.kake.business.user.model.Kake;
import com.home77.kake.common.api.response.KakeResponse;
import com.home77.kake.common.api.service.KakeFetcher;
import com.home77.kake.common.widget.recyclerview.listener.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KakeActivity extends AppCompatActivity {

  @BindView(R.id.loading_layout)
  ProgressBar loadingLayout;
  @BindView(R.id.empty_layout)
  TextView emptyLayout;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.refresh_layout)
  SwipeRefreshLayout refreshLayout;
  private List<Kake> kakeList = new ArrayList<>();
  private KakeListAdapter kakeListAdapter;
  private int page = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_kake);
    ButterKnife.bind(this);
    init();
    loadKakeList();
  }

  private void loadKakeList() {
    if (page == 0) {
      recyclerView.setVisibility(View.GONE);
      loadingLayout.setVisibility(View.VISIBLE);
    }
    emptyLayout.setVisibility(View.GONE);
    Instance.of(KakeFetcher.class).getKakeList(page, new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        KakeResponse kakeResponse = source.responseClass(KakeResponse.class);
        if (kakeResponse != null && kakeResponse.getCode() == 200) {
          final List<Kake> data = kakeResponse.getData();
          BaseHandler.post(new Runnable() {
            @Override
            public void run() {
              if (page == 0) {
                kakeList.clear();
                recyclerView.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.GONE);
              }
              kakeList.addAll(data);
              kakeListAdapter.notifyDataSetChanged();
              refreshLayout.setRefreshing(false);
              page++;
            }
          });
        } else {
          BaseHandler.post(new Runnable() {
            @Override
            public void run() {
              refreshLayout.setRefreshing(false);
              if (page == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                loadingLayout.setVisibility(View.GONE);
              } else {
                Toast.showShort("已经到底了~");
              }
            }
          });
        }
      }

      @Override
      public void onError(String msg) {
        BaseHandler.post(new Runnable() {
          @Override
          public void run() {
            refreshLayout.setRefreshing(false);
            recyclerView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);
          }
        });
      }
    });
  }

  private void init() {
    recyclerView.setLayoutManager(new LinearLayoutManager(this,
                                                          LinearLayoutManager.VERTICAL,
                                                          false));
    recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
      @Override
      public void getItemOffsets(Rect outRect,
                                 View view,
                                 RecyclerView parent,
                                 RecyclerView.State state) {
        outRect.bottom = SizeHelper.dp(12);
      }
    });
    recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
      @Override
      public void onLoadNextPage(View view) {
        loadKakeList();
      }
    });
    kakeListAdapter = new KakeListAdapter(this, kakeList);
    recyclerView.setAdapter(kakeListAdapter);
    refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        page = 0;
        loadKakeList();
      }
    });
  }

  @OnClick(R.id.back_image_view)
  public void onViewClicked() {
    this.finish();
  }
}
