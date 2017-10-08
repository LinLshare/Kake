package com.home77.kake.business.user.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.home77.common.base.collection.Params;
import com.home77.common.ui.widget.CircleImageView;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.user.KakeActivity;
import com.home77.kake.common.widget.BottomDialog;
import com.home77.kake.common.widget.InputDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class ProfileFragment extends BaseFragment
    implements TakePhoto.TakeResultListener, InvokeListener {
  @BindView(R.id.avatar_image_view)
  CircleImageView avatarImageView;
  @BindView(R.id.name_text_view)
  TextView nameTextView;
  Unbinder unbinder;
  @BindView(R.id.exist_login_text_view)
  TextView existLoginTextView;
  private TakePhoto takePhoto;
  private InvokeParam invokeParam;

  private Dialog bottomDialog;
  private File file;
  private InputDialog inputDialog;

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View contentView =
            LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, null, false);
        unbinder = ButterKnife.bind(this, contentView);
        out.put(ParamsKey.VIEW, contentView);
        break;
      case VIEW_DESTROY:
        unbinder.unbind();
        break;
      case VIEW_REFRESH:
        handleViewRefresh(in);
        break;
      case RENAME_SUCCESS:
        Toast.showShort("用户名更新成功");
        hideKeyboard();
        break;
      case SHOW_AVATAR_SELECT_DIALOG: {
        file = new File(Environment.getExternalStorageDirectory(),
                        "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
          file.getParentFile().mkdirs();
        }
        final Uri imageUri = Uri.fromFile(file);
        configCompress();
        configTakePhotoOpthion();
        bottomDialog = new BottomDialog(getContext(),
                                        new String[] {"拍照", "相册"},
                                        new BottomDialog.OnItemClickListener() {
                                          @Override
                                          public void onItemClick(int position, String data) {
                                            switch (position) {
                                              case 0:
                                                takePhoto.onPickFromCaptureWithCrop(imageUri,
                                                                                    getCropOptions());
                                                bottomDialog.dismiss();
                                                break;
                                              case 1:
                                                takePhoto.onPickFromGalleryWithCrop(imageUri,
                                                                                    getCropOptions());
                                                bottomDialog.dismiss();
                                                break;
                                            }
                                          }
                                        });
        bottomDialog.show();
      }
      break;
      case AVATAR_UPDATE_SUCCESS:
        Toast.showShort("头像更新成功");
        Picasso.with(getContext()).load(file).into(avatarImageView);
        break;
      case AVATAR_UPDATE_ERROR:
        Toast.showShort("头像更新失败");
        break;
      case SHOW_KAKE_ACTIVITY: {
        Intent intent = new Intent(getContext(), KakeActivity.class);
        startActivity(intent);
      }
      break;
    }
  }

  private void hideKeyboard() {
    if (nameTextView != null) {
      InputMethodManager imm =
          (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(nameTextView.getWindowToken(), 0);
    }
  }

  @OnClick({
      R.id.back_image_view, R.id.avatar_image_view, R.id.edit_image_view, R.id.list_item_kake,
      R.id.list_item_photo, R.id.list_item_help, R.id.list_item_about, R.id.name_text_view,
      R.id.exist_login_text_view
  })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        presenter.onMessage(MsgType.CLICK_BACK, null);
        break;
      case R.id.avatar_image_view:
        presenter.onMessage(MsgType.CLICK_AVATAR, null);
        break;
      case R.id.edit_image_view: {
        if (!App.isLogin()) {
          return;
        }
        inputDialog = new InputDialog(getContext(), "新昵称", new InputDialog.InputDialogListener() {
          @Override
          public void onClickOk(String input) {
            presenter.onMessage(MsgType.EDIT_USER_NAME_DONE,
                                Params.create(ParamsKey.USER_NAME, input));
            inputDialog.dismiss();
          }

          @Override
          public void onClickCancel() {
            inputDialog.dismiss();
          }
        });
        inputDialog.show();
      }
      break;
      case R.id.list_item_kake:
        presenter.onMessage(MsgType.CLICK_KAKE, null);
        break;
      case R.id.list_item_photo:
        presenter.onMessage(MsgType.CLICK_CLOUD_ALBUM, null);
        break;
      case R.id.list_item_help:
        presenter.onMessage(MsgType.CLICK_HELP, null);
        break;
      case R.id.list_item_about:
        presenter.onMessage(MsgType.CLICK_ABOUT, null);
        break;
      case R.id.name_text_view:
        presenter.onMessage(MsgType.CLICK_USER_NAME, null);
        break;
      case R.id.exist_login_text_view:
        presenter.onMessage(MsgType.CLICK_LOGOUT, null);
        break;
    }
  }

  private void handleViewRefresh(Params params) {
    String userName = App.globalData().getString(GlobalData.KEY_USER_NAME, "");
    String imgUrl = App.globalData().getString(GlobalData.KEY_USER_AVATER, "");
    if (!TextUtils.isEmpty(userName)) {
      nameTextView.setText(userName);
    } else {
      nameTextView.setText("请登录");
    }
    if (App.isLogin()) {
      existLoginTextView.setVisibility(View.VISIBLE);
    } else {
      existLoginTextView.setVisibility(View.GONE);
    }
    if (!TextUtils.isEmpty(imgUrl)) {
      Picasso.with(getContext()).load(imgUrl).resize(200, 200).centerCrop().into(new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
          if (avatarImageView != null) {
            avatarImageView.setImageBitmap(bitmap);
          }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
          if (avatarImageView != null) {
            avatarImageView.setImageResource(R.drawable.user_avatar_def);
          }
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
          if (avatarImageView != null) {
            avatarImageView.setImageResource(R.drawable.user_avatar_def);
          }
        }
      });
    } else {
      avatarImageView.setImageResource(R.drawable.user_avatar_def);
    }
  }

  @Override
  public void takeSuccess(TResult result) {
    File file = new File(result.getImage().getPath());
    presenter.onMessage(MsgType.TAKE_AVATAR_FILE, Params.create(ParamsKey.FILE, file));
  }

  @Override
  public void takeFail(TResult result, String msg) {
    Toast.showShort("获取图片失败");
  }

  @Override
  public void takeCancel() {
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    getTakePhoto().onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    getTakePhoto().onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * 获取TakePhoto实例
   */
  public TakePhoto getTakePhoto() {
    if (takePhoto == null) {
      takePhoto =
          (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
    }
    return takePhoto;
  }

  /**
   * 获取到权限
   */
  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //以下代码为处理Android6.0、7.0动态权限所需
    PermissionManager.TPermissionType type =
        PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
  }

  @Override
  public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
    PermissionManager.TPermissionType type =
        PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
    if (PermissionManager.TPermissionType.WAIT.equals(type)) {
      this.invokeParam = invokeParam;
    }
    return type;
  }

  private void configTakePhotoOpthion() {
    getTakePhoto().setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true)
                                                                     .create());
  }

  private CropOptions getCropOptions() {
    int height = 540;
    int width = 720;
    return new CropOptions.Builder().setWithOwnCrop(true)
                                    .setAspectX(width)
                                    .setAspectY(height)
                                    .setOutputX(width)
                                    .setOutputY(height)
                                    .create();
  }

  private void configCompress() {
    int maxSize = 720 * 540;
    int maxPixel = 720;
    CompressConfig config =
        new CompressConfig.Builder().setMaxSize(maxSize).setMaxPixel(maxPixel).create();
    getTakePhoto().onEnableCompress(config, true);
  }

}
