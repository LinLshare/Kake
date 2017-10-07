package com.home77.kake.business.home.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.home77.common.base.collection.Params;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.adapter.LocalPhotoListAdapter;
import com.home77.kake.business.home.model.LocalPhoto;
import com.home77.kake.common.widget.BottomDialog;
import com.home77.kake.common.widget.recyclerview.PinnedHeaderItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocalPhotoFragment extends BaseFragment {

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.refresh_layout)
  SwipeRefreshLayout refreshLayout;
  @BindView(R.id.loading_layout)
  ProgressBar loadingLayout;
  @BindView(R.id.empty_layout)
  TextView emptyLayout;
  Unbinder unbinder;

  private List<LocalPhoto> photoList = new ArrayList<>();
  private LocalPhotoListAdapter localPhotoListAdapter;
  private BottomDialog editPhotoDialog;

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View view =
            LayoutInflater.from(getContext()).inflate(R.layout.fragment_local_photo, null, false);
        unbinder = ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                                              LinearLayoutManager.VERTICAL,
                                                              false));
        localPhotoListAdapter = new LocalPhotoListAdapter(getContext(), photoList);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            presenter.onMessage(MsgType.VIEW_REFRESH, null);
          }
        });
        recyclerView.setAdapter(localPhotoListAdapter);
        recyclerView.addItemDecoration(new PinnedHeaderItemDecoration());
        out.put(ParamsKey.VIEW, view);
        presenter.handleMessage(MsgType.VIEW_REFRESH, null);
        break;
      case VIEW_DESTROY:
        unbinder.unbind();
        break;
      case LOCAL_PHOTO_LIST_LOADING:
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        break;
      case LOCAL_PHOTO_LIST_LOAD_SUCCESS:
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        List<LocalPhoto> photoList = in.get(ParamsKey.PHOTO_LIST, null);
        this.photoList.clear();
        this.photoList.addAll(photoList);
        localPhotoListAdapter.notifyDataSetChanged();
        break;
      case LOCAL_PHOTO_LIST_LOAD_ERROR:
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        break;
      case DEVICE_NOT_LINK:
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        break;
      case TO_PHOTO_VIEW_ACTIVITY: {
        byte[] _thumbnail = in.get(ParamsKey._THUMBNAIL);
        String photoName = in.get(ParamsKey.PHOTO_NAME);
        String path = in.get(ParamsKey.FILE_PATH);
        GLPhotoActivity.startActivityForResult(getActivity(), photoName, path, _thumbnail, true);
      }
      break;
      case SHOW_EDIT_PHOTO_DIALOG:
        showEditPhotoDialog(in);
        break;
    }
  }

  private void showEditPhotoDialog(final Params in) {
    editPhotoDialog = new BottomDialog(getContext(),
                                       new String[] {"删除", "取消"},
                                       new BottomDialog.OnItemClickListener() {
                                         @Override
                                         public void onItemClick(int position, String data) {
                                           switch (position) {
                                             case 0:
                                               presenter.onMessage(MsgType.CLICK_DELETE_PHOTO_EDIT_DIALOG,
                                                                   in);
                                               editPhotoDialog.dismiss();
                                               break;
                                             case 1:
                                               presenter.onMessage(MsgType.CLICK_CANCEL_PHOTO_EDIT_DIALOG,
                                                                   in);
                                               editPhotoDialog.dismiss();
                                               break;
                                           }
                                         }
                                       });
    editPhotoDialog.show();
  }
}
