package com.home77.kake.business.user.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home77.common.ui.widget.CircleImageView;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.business.user.presenter.ProfilePresenter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class ProfileFragment extends BaseFragment<ProfilePresenter> implements ProfileView {
  @BindView(R.id.avatar_image_view)
  CircleImageView avatarImageView;
  @BindView(R.id.name_text_view)
  TextView nameTextView;
  Unbinder unbinder;
  @BindView(R.id.exist_login_text_view)
  TextView existLoginTextView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View contentView = inflater.inflate(R.layout.fragment_profile, container, false);
    unbinder = ButterKnife.bind(this, contentView);
    presenter.onViewCreated();
    return contentView;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    presenter.onViewDestroy();
  }

  @OnClick({
      R.id.back_image_view, R.id.avatar_image_view, R.id.edit_image_view, R.id.list_item_kake,
      R.id.list_item_photo, R.id.list_item_help, R.id.list_item_about, R.id.name_text_view,
      R.id.exist_login_text_view
  })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        presenter.handleBackImageViewClick();
        break;
      case R.id.avatar_image_view:
        presenter.handleAvatarClick();
        break;
      case R.id.edit_image_view:
        presenter.handleEditUserClick();
        break;
      case R.id.list_item_kake:
        presenter.handleKakeClick();
        break;
      case R.id.list_item_photo:
        presenter.handleCloudGalleyClick();
        break;
      case R.id.list_item_help:
        presenter.handleHelpFeedbackClick();
        break;
      case R.id.list_item_about:
        presenter.handleAboutClick();
        break;
      case R.id.name_text_view:
        presenter.handleNameTextClick();
        break;
      case R.id.exist_login_text_view:
        presenter.handleExistLogin();
        break;
    }
  }

  @Override
  public void bindData(String userName, String imgUrl) {
    if (!TextUtils.isEmpty(userName)) {
      nameTextView.setText(userName);
    }
    if (!TextUtils.isEmpty(imgUrl)) {
      Picasso.with(getContext()).load(imgUrl).into(new Target() {
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
    }
  }

  @Override
  public void onLogout() {
    avatarImageView.setImageResource(R.drawable.user_avatar_def);
    nameTextView.setText("请登录");
    existLoginTextView.setVisibility(View.GONE);
  }

  @Override
  public void onLogin() {
    existLoginTextView.setVisibility(View.VISIBLE);
  }
}
