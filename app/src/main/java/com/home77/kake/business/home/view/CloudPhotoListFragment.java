package com.home77.kake.business.home.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.home77.common.base.collection.Params;
import com.home77.common.ui.util.SizeHelper;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.adapter.LocalPhotoListAdapter;
import com.home77.kake.business.home.model.LocalPhoto;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
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
public class CloudPhotoListFragment extends BaseFragment {

  @BindView(R.id.title_text_view)
  TextView titleTextView;
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;
  @BindView(R.id.refresh_layout)
  SwipeRefreshLayout refreshLayout;
  Unbinder unbinder;
  @BindView(R.id.menu_image_view)
  ImageView menuImageView;
  private List<LocalPhoto> photoList = new ArrayList<>();
  private LocalPhotoListAdapter localPhotoListAdapter;
  private PopupWindow popupMenu;

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View view = LayoutInflater.from(getContext())
                                  .inflate(R.layout.fragment_cloud_photo_list, null, false);
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
            presenter.onMessage(MsgType.VIEW_REFRESH, null);
          }
        });
        //popupMenu
        View menulayout =
            LayoutInflater.from(getContext()).inflate(R.layout.menu_cloud_photo_list, null);
        popupMenu = new PopupWindow(menulayout,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMenu.setOutsideTouchable(false);
        popupMenu.setFocusable(true);
        menulayout.findViewById(R.id.add_photo_layout)
                  .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      presenter.onMessage(MsgType.CLICK_IMPORT_PHOTO, null);
                      popupMenu.dismiss();
                    }
                  });
        menulayout.findViewById(R.id.menu_layout).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            popupMenu.dismiss();
          }
        });
        out.put(ParamsKey.VIEW, view);
        presenter.onMessage(MsgType.VIEW_REFRESH, null);
        break;
      case VIEW_DESTROY:
        unbinder.unbind();
        break;
      case SHOW_MENU:
        popupMenu.showAsDropDown(menuImageView);
        break;
      case SHOW_ALBUM_INFO:
        Album album = in.get(ParamsKey.ALBUM);
        titleTextView.setText(album.getName() + "");
      case CLOUD_PHOTO_LIST_LOADING:
        break;
      case CLOUD_PHOTO_LIST_LOAD_ERROR:
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        String msg = in.get(ParamsKey.MSG);
        Toast.showShort(msg);
        break;
      case CLOUD_PHOTO_LIST_LOAD_SUCCESS:
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        List<LocalPhoto> photoList = in.get(ParamsKey.PHOTO_LIST);
        if (photoList.isEmpty()) {
          Toast.showShort("暂无图片");
          return;
        }
        this.photoList.clear();
        this.photoList.addAll(photoList);
        localPhotoListAdapter.notifyDataSetChanged();
        break;
      case SHOW_PHOTO_SELECTOR: {
        Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 9);
        startActivityForResult(intent, Constants.REQUEST_CODE);
      }
      break;
      case PHOTO_UPLOADING:
        App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_SHOW, null));
        break;
      case PHOTO_UPLOAD_ERROR:
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
        Toast.showShort("上传失败");
        break;
      case PHOTO_UPLOAD_SUCCESS:
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
        Toast.showShort("上传成功");
        break;
    }
  }

  @OnClick({R.id.back_image_view, R.id.menu_image_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        presenter.onMessage(MsgType.CLICK_BACK, null);
        break;
      case R.id.menu_image_view:
        presenter.onMessage(MsgType.CLICK_MENU, null);
        break;
    }
  }

  @Override
  public void onPause() {
    if (popupMenu.isShowing()) {
      popupMenu.dismiss();
    }
    super.onPause();
  }
}
