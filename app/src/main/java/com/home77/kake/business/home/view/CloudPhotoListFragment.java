package com.home77.kake.business.home.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.component.ContextManager;
import com.home77.common.base.pattern.Instance;
import com.home77.common.ui.model.UiData;
import com.home77.common.ui.util.LayoutHelper;
import com.home77.common.ui.util.SizeHelper;
import com.home77.common.ui.widget.CommonLoadingDialog;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.PhotoSelectActivity;
import com.home77.kake.business.home.adapter.CloudPhotoListAdapter;
import com.home77.kake.business.home.model.CloudPhoto;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.utils.QRCodeUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
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
  @BindView(R.id.bottom_text_view)
  TextView bottomTextView;
  Unbinder unbinder;
  @BindView(R.id.menu_image_view)
  ImageView menuImageView;
  private List<CloudPhoto> photoList = new ArrayList<>();
  private CloudPhotoListAdapter cloudPhotoListAdapter;
  private PopupWindow popupMenu;
  private Album album;
  private CommonLoadingDialog loadingDialog;

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
                      presenter.onMessage(MsgType.CLICK_MENU_IMPORT_PHOTO, null);
                      popupMenu.dismiss();
                    }
                  });
        menulayout.findViewById(R.id.menu_item_generate_qrcode)
                  .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      presenter.onMessage(MsgType.CLICK_MENU_GENERATE_QRCODE, null);
                      popupMenu.dismiss();
                    }
                  });
        menulayout.findViewById(R.id.menu_item_share)
                  .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      presenter.onMessage(MsgType.CLICK_MENU_SHARE, null);
                      popupMenu.dismiss();
                    }
                  });
        menulayout.findViewById(R.id.menu_item_kake).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            presenter.onMessage(MsgType.CLICK_MENU_KAKE, null);
            popupMenu.dismiss();
          }
        });
        loadingDialog = new CommonLoadingDialog(getContext());
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
        if (!TextUtils.isEmpty(album.getPanourl())) {
          bottomTextView.setText("观看户型全景");
          bottomTextView.setBackgroundColor(ContextManager.resources().getColor(R.color.colorC7));
          bottomTextView.setTextColor(ContextManager.resources().getColor(R.color.colorC2));
        } else {
          if (album.getStatus() == Album.STATUS_GENERATING) {
            bottomTextView.setText("正在合成中");
            bottomTextView.setEnabled(false);
          } else {
            bottomTextView.setText("合成户型全景");
          }
          bottomTextView.setBackgroundColor(ContextManager.resources().getColor(R.color.colorC2));
          bottomTextView.setTextColor(ContextManager.resources().getColor(R.color.colorC7));
        }
        break;
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
        Intent intent = new Intent(getContext(), PhotoSelectActivity.class);
        startActivityForResult(intent, 1001);
      }
      break;
      case PHOTO_UPLOADING:
        loadingDialog.show("正在上传中...");
        loadingDialog.setCancelable(false);
        break;
      case PHOTO_UPLOAD_ERROR:
        loadingDialog.dismiss();
        Toast.showShort("上传失败");
        break;
      case PHOTO_UPLOAD_SUCCESS:
        loadingDialog.dismiss();
        Toast.showShort("上传成功");
        break;
      case SHOW_QRCODE_DIALOG: {
        String content = in.get(ParamsKey.STR);
        int width = Instance.of(UiData.class).winWidth() / 2;
        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(content, width, width);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setTextColor(ContextManager.resources().getColor(R.color.colorC6));
        textView.setText(album.getName() + "");
        linearLayout.addView(textView,
                             LayoutHelper.createLL(LayoutHelper.WRAP,
                                                   LayoutHelper.WRAP,
                                                   Gravity.CENTER_HORIZONTAL,
                                                   0,
                                                   SizeHelper.dp(16),
                                                   0,
                                                   0));
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(qrCodeBitmap);
        linearLayout.addView(imageView,
                             LayoutHelper.createLL(LayoutHelper.WRAP,
                                                   LayoutHelper.WRAP,
                                                   Gravity.CENTER_HORIZONTAL));
        new AlertDialog.Builder(getContext()).setView(linearLayout).create().show();
      }
      break;
      case MAKE_PANO_POSTING:
        loadingDialog.show("正在请求合成");
        loadingDialog.setCancelable(false);
        bottomTextView.setEnabled(false);
        break;
      case MAKE_PANO_POST_ERROR:
        loadingDialog.dismiss();
        loadingDialog.setCancelable(true);
        Toast.showShort("合成请求失败");
        bottomTextView.setEnabled(true);
        break;
      case MAKE_PANO_POST_SUCCESS:
        loadingDialog.dismiss();
        loadingDialog.setCancelable(true);
        Toast.showShort("服务器已经受理图片合成请求，稍后会通知合成结果");
        bottomTextView.setEnabled(false);
        break;
      case SHOW_PANO_PHOTO:
        Picasso.with(getContext()).load(album.getCover()).into(new Target() {
          @Override
          public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread() {
              @Override
              public void run() {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] dt = baos.toByteArray();
                BaseHandler.runOnMainThread(new Runnable() {
                  @Override
                  public void run() {
                    GLPhotoActivity.startActivityForResult(getActivity(),
                                                           dt,
                                                           album.getPanourl(),
                                                           album.getName(),
                                                           false);
                  }
                });
              }
            }.start();
          }

          @Override
          public void onBitmapFailed(Drawable errorDrawable) {

          }

          @Override
          public void onPrepareLoad(Drawable placeHolderDrawable) {

          }
        });
        break;
      case SHOW_MAKE_PUBLIC_DIALOG:
        showMakePublicDialog();
        break;
      case MAKE_PUBLIC_POSTING:
        loadingDialog.show("正在发布");
        loadingDialog.setCancelable(false);
        break;
      case MAKE_PUBLIC_SUCCESS:
        loadingDialog.dismiss();
        loadingDialog.setCancelable(true);
        Toast.showShort("发布成功");
        break;
      case MAKE_PUBLIC_ERROR:
        loadingDialog.dismiss();
        loadingDialog.setCancelable(true);
        Toast.showShort("发布失败");
        break;
    }

  }


  @OnClick({R.id.back_image_view, R.id.menu_image_view, R.id.bottom_text_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        presenter.onMessage(MsgType.CLICK_BACK, null);
        break;
      case R.id.menu_image_view:
        presenter.onMessage(MsgType.CLICK_MENU, null);
        break;
      case R.id.bottom_text_view:
        presenter.onMessage(MsgType.CLICK_BOTTOM_BUTTON, null);
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

  private void showMakePublicDialog() {
    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_make_public, null);
    final AlertDialog alertDialog =
        new AlertDialog.Builder(getContext()).setView(dialogView).create();
    dialogView.findViewById(R.id.ok_text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.onMessage(MsgType.CLICK_OK_MAKE_PUBLIC_DIALOG, null);
        alertDialog.dismiss();
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
