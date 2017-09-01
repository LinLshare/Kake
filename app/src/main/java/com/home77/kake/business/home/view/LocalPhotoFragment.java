package com.home77.kake.business.home.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.home77.common.base.collection.Params;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.adapter.LocalPhotoListAdapter;
import com.home77.kake.business.home.model.LocalPhoto;

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
  Unbinder unbinder;

  private List<LocalPhoto> photoList = new ArrayList<>();
  private LocalPhotoListAdapter localPhotoListAdapter;

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
        out.put(ParamsKey.VIEW, view);
        break;
      case VIEW_DESTROY:
        unbinder.unbind();
        break;
      case LOCAL_PHOTO_LIST_LOADING:
        break;
      case LOCAL_PHOTO_LIST_LOAD_SUCCESS:
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        List<LocalPhoto> photoList = in.get(ParamsKey.PHOTO_LIST, null);
        this.photoList.clear();
        this.photoList.addAll(photoList);
        localPhotoListAdapter.notifyDataSetChanged();
        break;
      case LOCAL_PHOTO_LIST_LOAD_ERROR:
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        String msg = in.get(ParamsKey.MSG, "");
        if (!TextUtils.isEmpty(msg)) {
          Toast.showShort(msg);
        }
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
        //        if (Instance.of(ImageDataStorage.class).bind(photo.getName()).getYaw() == 0L) {
        //          PhotoViewActivity.start(getActivity(), photo);
        //        } else {

        GLPhotoActivity.startActivityForResult(getActivity(), photoName, path, _thumbnail, true);
        //        }
      }
      break;
    }
  }
}
