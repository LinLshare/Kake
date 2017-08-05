package com.home77.kake.business.home.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.home77.common.base.pattern.Instance;
import com.home77.common.ui.model.UiData;
import com.home77.common.ui.util.SizeHelper;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.business.home.adapter.CloudAlbumListAdapter;
import com.home77.kake.business.home.presenter.CloudAlbumPresenter;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.home77.kake.common.widget.recyclerview.CloudAlbumGridItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class CloudAlbumFragment extends BaseFragment<CloudAlbumPresenter>
    implements CloudAlbumView {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.refresh_layout)
  SwipeRefreshLayout refreshLayout;
  Unbinder unbinder;
  private List<Album> albumList = new ArrayList<>();
  private CloudAlbumListAdapter cloudAlbumListAdapter;
  private AlertDialog alertDialog;

  {
    albumList.add(new Album());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.refreshable_recycler_layout, container, false);
    unbinder = ButterKnife.bind(this, view);
    App.eventBus().register(this);
    int spanCount = 3;
    int outerSpace = SizeHelper.dp(12);
    int innerSpace = SizeHelper.dp(6);
    int screenWidth = getResources().getDisplayMetrics().widthPixels;
    int widthAndHeight = (screenWidth - outerSpace * 2 - (spanCount - 1) * innerSpace) / spanCount;
    recyclerView.addItemDecoration(new CloudAlbumGridItemDecoration(outerSpace, innerSpace));
    cloudAlbumListAdapter = new CloudAlbumListAdapter(getContext(), albumList, widthAndHeight);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
    recyclerView.setAdapter(cloudAlbumListAdapter);

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
    App.eventBus().unregister(this);
  }

  @Override
  public void onAlbumUpdated(List<Album> albumList) {
    this.albumList.clear();
    this.albumList.add(new Album());
    this.albumList.addAll(albumList);
    cloudAlbumListAdapter.notifyDataSetChanged();
    if (refreshLayout.isRefreshing()) {
      refreshLayout.setRefreshing(false);
    }
  }

  @Override
  public void onAlbumError(String msg) {
    toast(msg + "");
    if (refreshLayout.isRefreshing()) {
      refreshLayout.setRefreshing(false);
    }
  }

  @Override
  public void onAlbumCreating(String albumName) {
    alertDialog.dismiss();
    App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_SHOW, null));
  }

  @Override
  public void onAlbumCreated(String albumName, String msg) {
    App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
    toast(msg);
    if (refreshLayout.isRefreshing()) {
      refreshLayout.setRefreshing(false);
    }
  }

  @Override
  public void onAlbumCreateFail(String albumName, String error) {
    App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
    toast(error);
    if (refreshLayout.isRefreshing()) {
      refreshLayout.setRefreshing(false);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.DIALOG_UPLOAD_ALBUM:
        showUploadAlbumDialog();
        break;
    }
  }

  private void showUploadAlbumDialog() {
    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_album, null);
    alertDialog = new AlertDialog.Builder(getContext()).setView(dialogView).create();
    final EditText editText = (EditText) dialogView.findViewById(R.id.album_name_edit_text);
    dialogView.findViewById(R.id.ok_text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String name = editText.getText().toString();
        presenter.onUploadAlbumDialogClickConfirm(name);
      }
    });
    dialogView.findViewById(R.id.cancel_text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        alertDialog.dismiss();
      }
    });
    alertDialog.show();
  }
}
