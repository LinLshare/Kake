package com.home77.kake.business.home.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
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
import com.home77.kake.business.home.PhotoSelectActivity;
import com.home77.kake.business.home.adapter.CloudPhotoListAdapter;
import com.home77.kake.business.home.model.CloudPhoto;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.home77.kake.common.utils.QRCodeUtil;
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
  @BindView(R.id.loading_layout)
  ProgressBar loadingLayout;
  @BindView(R.id.empty_layout)
  TextView emptyLayout;
  Unbinder unbinder;
  @BindView(R.id.menu_image_view)
  ImageView menuImageView;
  private List<CloudPhoto> photoList = new ArrayList<>();
  private CloudPhotoListAdapter cloudPhotoListAdapter;
  private PopupWindow popupMenu;
  private Album album;

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View view = LayoutInflater.from(getContext())
                                  .inflate(R.layout.fragment_cloud_photo_list, null, false);
        unbinder = ButterKnife.bind(this, view);

        // setup recyclerview
        cloudPhotoListAdapter = new CloudPhotoListAdapter(getContext(), photoList);
        recyclerView.setAdapter(cloudPhotoListAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
          @Override
          public int getSpanSize(int position) {
            int spanSize = 1;
            CloudPhoto cloudPhoto = cloudPhotoListAdapter.getDatas().get(position);
            if (cloudPhoto != null && cloudPhoto.isTitle()) {
              spanSize = 3;
            }
            return spanSize;
          }
        });
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
        menulayout.findViewById(R.id.menu_item_add_photo)
                  .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      presenter.onMessage(MsgType.CLICK_IMPORT_PHOTO, null);
                      popupMenu.dismiss();
                    }
                  });
        menulayout.findViewById(R.id.menu_item_generate_qrcode)
                  .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      presenter.onMessage(MsgType.CLICK_GENERATE_QRCODE, null);
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
        album = in.get(ParamsKey.ALBUM);
        titleTextView.setText(album.getName() + "");
      case CLOUD_PHOTO_LIST_LOADING: {
        loadingLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
      }
      break;
      case CLOUD_PHOTO_LIST_LOAD_ERROR:
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        String msg = in.get(ParamsKey.MSG);
        Toast.showShort(msg);
        break;
      case CLOUD_PHOTO_LIST_LOAD_SUCCESS:
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        List<CloudPhoto> photoList = in.get(ParamsKey.CLOUD_PHOTO_LIST);
        if (photoList == null || photoList.isEmpty()) {
          Toast.showShort("暂无图片");
          return;
        }
        this.photoList.clear();
        this.photoList.addAll(photoList);
        cloudPhotoListAdapter.notifyDataSetChanged();
        break;
      case SHOW_PHOTO_SELECTOR: {
        PhotoSelectActivity.start(getContext(), album);
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
      case SHOW_QRCODE_DIALOG:
        String content = in.get(ParamsKey.STR);
        int width = Instance.of(UiData.class).winWidth() / 2;
        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(content, width, width);
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(qrCodeBitmap);
        new AlertDialog.Builder(getContext()).setView(imageView).create().show();
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
