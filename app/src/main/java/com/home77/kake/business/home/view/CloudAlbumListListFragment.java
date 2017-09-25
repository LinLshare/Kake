package com.home77.kake.business.home.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
import com.home77.common.ui.model.UiData;
import com.home77.common.ui.util.SizeHelper;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.adapter.CloudAlbumListAdapter;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.home77.kake.common.widget.InputDialog;
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
public class CloudAlbumListListFragment extends BaseFragment {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.refresh_layout)
  SwipeRefreshLayout refreshLayout;
  Unbinder unbinder;
  @BindView(R.id.loading_layout)
  ProgressBar loadingLayout;
  @BindView(R.id.empty_layout)
  TextView emptyLayout;
  private List<Album> albumList = new ArrayList<>();
  private CloudAlbumListAdapter cloudAlbumListAdapter;
  private InputDialog inputDialog;

  public CloudAlbumListListFragment() {
    albumList.add(new Album());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    App.eventBus().unregister(this);
  }

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View view = LayoutInflater.from(getContext())
                                  .inflate(R.layout.refreshable_recycler_layout, null, false);
        unbinder = ButterKnife.bind(this, view);
        App.eventBus().register(this);
        int spanCount = 3;
        int space = SizeHelper.dp(6);
        int screenWidth = Instance.of(UiData.class).winWidth();
        int widthAndHeight = (screenWidth - (spanCount + 1) * space) / spanCount;
        recyclerView.addItemDecoration(new CloudAlbumGridItemDecoration(space));
        cloudAlbumListAdapter = new CloudAlbumListAdapter(getContext(), albumList, widthAndHeight);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        recyclerView.setAdapter(cloudAlbumListAdapter);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            presenter.onMessage(MsgType.VIEW_REFRESH, null);
          }
        });
        out.put(ParamsKey.VIEW, view);
        break;
      case CLOUD_ALBUM_CREATING:
        break;
      case CLOUD_ALBUM_LOADING: {
        loadingLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
      }
      break;
      case CLOUD_ALBUM_CREATE_ERROR:
      case CLOUD_ALBUM_LOAD_ERROR:
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        String msg = in.get(ParamsKey.MSG, "");
        if (!TextUtils.isEmpty(msg)) {
          Toast.showShort(msg);
        }
        break;
      case CLOUD_ALBUM_CREATE_SUCCESS:
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        break;
      case CLOUD_ALBUM_LOAD_SUCCESS:
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        List<Album> albumList = in.get(ParamsKey.ALBUM_LIST, null);
        this.albumList.clear();
        this.albumList.add(new Album());
        this.albumList.addAll(albumList);
        cloudAlbumListAdapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        break;
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
    inputDialog = new InputDialog(getContext(), "新建相册", new InputDialog.InputDialogListener() {

      @Override
      public void onClickOk(String input) {
        presenter.onMessage(MsgType.CLICK_CREATE_ALBUM_DIALOG_OK,
                            Params.create(ParamsKey.ALBUM_NAME, input));
        inputDialog.dismiss();
      }

      @Override
      public void onClickCancel() {
        inputDialog.dismiss();
      }
    });
    inputDialog.show();
  }
}
