package com.home77.kake.business.home.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.home77.kake.business.home.WebPageActivity;
import com.home77.kake.business.home.adapter.CloudPhotoListAdapter;
import com.home77.kake.business.home.model.CloudPhoto;
import com.home77.kake.common.api.response.Album;
import com.home77.kake.common.utils.QRCodeUtil;
import com.home77.kake.common.utils.Util;
import com.home77.kake.common.widget.BottomDialog;
import com.home77.kake.common.widget.CustomBottomDialog;
import com.home77.kake.common.widget.InputDialog;
import com.home77.kake.common.widget.TipDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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
  @BindView(R.id.edit_image_view)
  ImageView editImageView;
  private List<CloudPhoto> photoList = new ArrayList<>();
  private CloudPhotoListAdapter cloudPhotoListAdapter;
  private PopupWindow popupMenu;
  private Album album;
  private CommonLoadingDialog loadingDialog;
  private IWXAPI api;
  private Dialog editPhotoDialog;
  private TipDialog tipDialog;
  private TipDialog makePublicDialog;
  private InputDialog inputDialog;
  private InputDialog albumNameInputDialog;

  public CloudPhotoListFragment() {
    String appId = "wx0dfd1d91a80a49b1";
    api = WXAPIFactory.createWXAPI(getActivity(), appId, true);
  }

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
        loadingDialog = new CommonLoadingDialog(getContext());
        out.put(ParamsKey.VIEW, view);
        presenter.onMessage(MsgType.VIEW_REFRESH, null);
        break;
      case VIEW_DESTROY:
        unbinder.unbind();
        break;
      case SHOW_MENU:
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
        TextView kakeTextView = (TextView) menulayout.findViewById(R.id.kake_text_view);
        kakeTextView.setText(album.getIs_public() == 1 ? "取消发布" : "发布");
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
      case CLOUD_PHOTO_LIST_LOAD_ERROR: {
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        if (refreshLayout.isRefreshing()) {
          refreshLayout.setRefreshing(false);
        }
        final String msg = in.get(ParamsKey.MSG);
        Toast.showShort(msg);
        bottomTextView.setVisibility(View.GONE);
      }
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
          bottomTextView.setVisibility(View.GONE);
          return;
        }
        bottomTextView.setVisibility(View.VISIBLE);
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
        // show webview
        WebPageActivity.start(getContext(), album.getName(), album.getPanourl());
        break;
      case SHOW_MAKE_PUBLIC_DIALOG:
        showMakePublicDialog();
        break;
      case MAKE_PUBLIC_POSTING:
        loadingDialog.show("正在发布");
        loadingDialog.setCancelable(false);
        break;
      case MAKE_PUBLIC_SUCCESS: {
        String toast;
        if (album.getIs_public() == 1) {
          album.setIs_public(0);
          toast = "取消发布成功";
        } else {
          toast = "发布成功";
        }
        loadingDialog.dismiss();
        loadingDialog.setCancelable(true);
        Toast.showShort(toast);
      }
      break;
      case MAKE_PUBLIC_ERROR: {
        String toast;
        if (album.getIs_public() == 1) {
          album.setIs_public(0);
          toast = "取消发布失败";
        } else {
          toast = "发布失败";
        }
        loadingDialog.dismiss();
        loadingDialog.setCancelable(true);
        Toast.showShort(toast);
      }
      break;
      case SHOW_SHARE_DIALOG: {
        showShareDialog();
      }
      break;
      case SHOW_EDIT_PHOTO_DIALOG:
        showEditPhotoDialog(in);
        break;
      case SHOW_DELETE_PHOTO_CONFIRM_DIALOG:
        showDeletePhotoConfirmDialog(in);
        break;
      case SHOW_RENAME_PHOTO_DIALOG:
        showRenameDialog(in);
        break;
      case LOADING: {
        String msg = in.get(ParamsKey.MSG);
        loadingDialog.show(msg);
        loadingDialog.setCancelable(false);
      }
      break;
      case PHOTO_DELETE_ERROR:
        loadingDialog.dismiss();
        Toast.showShort("删除失败");
        break;
      case PHOTO_DELETE_SUCCESS:
        loadingDialog.dismiss();
        Toast.showShort("删除成功");
        break;
      case PHOTO_RENAME_ERROR:
        loadingDialog.dismiss();
        Toast.showShort("重命名失败，请稍后重试");
        break;
      case PHOTO_RENAME_SUCCESS:
        loadingDialog.dismiss();
        Toast.showShort("重命名成功");
        break;
      case ALBUM_RENAME_ERROR:
        Toast.showShort("重命名失败");
        break;
      case ALBUM_RENAME_SUCCESS:
        Toast.showShort("重命名成功");
        String str = in.get(ParamsKey.STR);
        titleTextView.setText(str);
        break;
    }
  }

  private void showRenameDialog(final Params in) {
    inputDialog = new InputDialog(getContext(), "重命名图片", new InputDialog.InputDialogListener() {
      @Override
      public void onClickOk(String input) {
        presenter.onMessage(MsgType.CLICK_OK_RENAME_PHOTO_DIALOG, in.put(ParamsKey.STR, input));
        inputDialog.dismiss();
      }

      @Override
      public void onClickCancel() {
        inputDialog.dismiss();
      }
    });
    inputDialog.show();
  }

  private String buildTransaction(final String type) {
    return (type == null)
        ? String.valueOf(System.currentTimeMillis())
        : type + System.currentTimeMillis();
  }

  @OnClick({
      R.id.back_image_view, R.id.menu_image_view, R.id.bottom_text_view, R.id.edit_image_view
  })
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
      case R.id.edit_image_view: {
        albumNameInputDialog =
            new InputDialog(getContext(), "相册的新名称", new InputDialog.InputDialogListener() {
              @Override
              public void onClickOk(String input) {
                if (TextUtils.isEmpty(input)) {
                  Toast.showShort("输入不合法，请重新输入");
                  return;
                }
                presenter.onMessage(MsgType.CLICK_OK_RENAME_ALBUM_DIALOG,
                                    Params.create(ParamsKey.STR, input)
                                          .put(ParamsKey.ALBUM, album));
                albumNameInputDialog.dismiss();
              }

              @Override
              public void onClickCancel() {
                albumNameInputDialog.dismiss();
              }
            });
        albumNameInputDialog.show();
      }
      break;
    }
  }

  @Override
  public void onPause() {
    if (popupMenu != null && popupMenu.isShowing()) {
      popupMenu.dismiss();
    }
    super.onPause();
  }

  private void showShareDialog() {
    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share, null);
    final Dialog alertDialog = new CustomBottomDialog(getContext());
    alertDialog.setContentView(dialogView);
    dialogView.findViewById(R.id.cancel_text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        alertDialog.dismiss();
      }
    });
    dialogView.findViewById(R.id.friend_layout).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        shareWetChat(SendMessageToWX.Req.WXSceneTimeline);
        alertDialog.dismiss();
      }
    });
    dialogView.findViewById(R.id.moment_layout).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        shareWetChat(SendMessageToWX.Req.WXSceneSession);
        alertDialog.dismiss();
      }
    });
    dialogView.findViewById(R.id.copy_layout).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ClipboardManager clipboardManager =
            (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(album.getPanourl() + "");
        Toast.showShort("已经复制到剪贴板");
        alertDialog.dismiss();
      }
    });
    alertDialog.show();
  }

  private void showEditPhotoDialog(final Params in) {
    editPhotoDialog = new BottomDialog(getContext(),
                                       new String[] {"删除", "重命名", "取消"},
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
                                               presenter.onMessage(MsgType.CLICK_RENAME_PHOTO_EDIT_DIALOG,
                                                                   in);
                                               editPhotoDialog.dismiss();
                                               break;
                                             case 2:
                                               presenter.onMessage(MsgType.CLICK_CANCEL_PHOTO_EDIT_DIALOG,
                                                                   in);
                                               editPhotoDialog.dismiss();
                                               break;
                                           }
                                         }
                                       });
    editPhotoDialog.show();
  }

  private void shareWetChat(final int scene) {
    Picasso.with(getContext()).load(album.getCover()).into(new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = album.getPanourl();

        WXMediaMessage wxMsg = new WXMediaMessage(webpageObject);
        wxMsg.title = album.getName();
        wxMsg.description = "";
        wxMsg.thumbData = Util.bmpToByteArray(bitmap, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");

        req.message = wxMsg;
        req.scene = scene;

        api.sendReq(req);
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
      }
    });
  }

  private void showDeletePhotoConfirmDialog(final Params params) {
    CloudPhoto cloudPhoto = params.get(ParamsKey.CLOUD_PHOTO);
    tipDialog = new TipDialog(getContext(),
                              "确认删除照片: " + cloudPhoto.getName() + " ?",
                              new TipDialog.OnButtonClickListener() {
                                @Override
                                public void onClickOk() {
                                  presenter.onMessage(MsgType.CLICK_OK_DELETE_PHOTO_CONFIRM_DIALOG,
                                                      params);
                                  tipDialog.dismiss();
                                }

                                @Override
                                public void onClickCancel() {
                                  tipDialog.dismiss();
                                }
                              });
    tipDialog.show();
  }

  private void showMakePublicDialog() {
    String title;
    switch (album.getIs_public()) {
      case 1:
        title = "确认取消发布到咔客圈?";
        break;
      case 0:
      default:
        title = "确认发布到咔客圈?";
        break;
    }
    makePublicDialog = new TipDialog(getContext(), title, new TipDialog.OnButtonClickListener() {
      @Override
      public void onClickOk() {
        presenter.onMessage(MsgType.CLICK_OK_MAKE_PUBLIC_DIALOG, null);
        makePublicDialog.dismiss();
      }

      @Override
      public void onClickCancel() {
        makePublicDialog.dismiss();
      }
    });
    makePublicDialog.show();
  }
}
