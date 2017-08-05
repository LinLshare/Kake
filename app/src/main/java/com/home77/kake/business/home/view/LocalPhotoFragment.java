package com.home77.kake.business.home.view;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home77.common.base.component.BaseHandler;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.business.home.adapter.LocalPhotoListAdapter;
import com.home77.kake.business.home.model.Photo;
import com.home77.kake.business.home.presenter.LocalPhotoPresenter;
import com.home77.kake.business.theta.LoadFileListResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocalPhotoFragment extends BaseFragment<LocalPhotoPresenter>
    implements LocalPhotoView {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.refresh_layout)
  SwipeRefreshLayout refreshLayout;
  Unbinder unbinder;

  private List<Photo> photoList = new ArrayList<>();
  private LocalPhotoListAdapter localPhotoListAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_local_photo, container, false);
    unbinder = ButterKnife.bind(this, view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                                          LinearLayoutManager.VERTICAL,
                                                          false));
    localPhotoListAdapter = new LocalPhotoListAdapter(getContext(), photoList);
    recyclerView.setAdapter(localPhotoListAdapter);
//    presenter.onViewCreated();
    return view;
  }

  @Override
  public void onRefresh() {

  }

  @Override
  public void onIdle(final List<Photo> imageInfoList) {
    BaseHandler.runOnMainThread(new Runnable() {
      @Override
      public void run() {
        LocalPhotoFragment.this.photoList.addAll(imageInfoList);
        localPhotoListAdapter.notifyDataSetChanged();
      }
    });
  }

  @Override
  public void onEmpty() {

  }

  @Override
  public void onError() {

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
