package com.home77.kake.business.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home77.common.ui.util.SizeHelper;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.business.home.adapter.LocalPhotoListAdapter;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.business.home.presenter.CloudPhotoListPresenter;
import com.home77.kake.common.widget.recyclerview.DefaultGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class CloudPhotoListFragment extends BaseFragment<CloudPhotoListPresenter>
    implements CloudPhotoListView {

  @BindView(R.id.title_text_view)
  TextView titleTextView;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.refresh_layout)
  SwipeRefreshLayout refreshLayout;
  Unbinder unbinder;
  private List<Photo> photoList = new ArrayList<>();
  private LocalPhotoListAdapter localPhotoListAdapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cloud_photo_list, container, false);
    unbinder = ButterKnife.bind(this, view);

    // setup recyclerview
    localPhotoListAdapter = new LocalPhotoListAdapter(getContext(), photoList);
    recyclerView.setAdapter(localPhotoListAdapter);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    recyclerView.addItemDecoration(new DefaultGridItemDecoration(SizeHelper.dp(12)));
    // setup refreshlayout
    refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        presenter.onRefresh();
      }
    });
    presenter.onCreateView();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @OnClick({R.id.back_image_view, R.id.menu_image_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        presenter.onBackNavClick();
        break;
      case R.id.menu_image_view:
        presenter.onMenuNavClick();
        break;
    }
  }
}
