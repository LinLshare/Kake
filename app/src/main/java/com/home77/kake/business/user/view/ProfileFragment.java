package com.home77.kake.business.user.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.home77.common.base.collection.Params;
import com.home77.common.ui.widget.CircleImageView;
import com.home77.kake.R;
import com.home77.kake.bs.BaseFragment;
import com.home77.kake.bs.CmdType;
import com.home77.kake.bs.MsgType;
import com.home77.kake.bs.ParamsKey;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class ProfileFragment extends BaseFragment {
  @BindView(R.id.avatar_image_view)
  CircleImageView avatarImageView;
  @BindView(R.id.name_text_view)
  EditText nameTextView;
  Unbinder unbinder;
  @BindView(R.id.exist_login_text_view)
  TextView existLoginTextView;

  private KeyListener keyListener;

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View contentView =
            LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, null, false);
        unbinder = ButterKnife.bind(this, contentView);
        keyListener = nameTextView.getKeyListener();
        nameTextView.setKeyListener(null);
        nameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
              keyListener = nameTextView.getKeyListener();
              nameTextView.setKeyListener(null);
              presenter.onMessage(MsgType.EDIT_USER_NAME_DONE,
                                  Params.create(ParamsKey.USER_NAME, v.getText().toString()));
              return true;
            }
            return false;
          }
        });
        out.put(ParamsKey.VIEW, contentView);
        break;
      case VIEW_DESTROY:
        unbinder.unbind();
        break;
      case VIEW_REFRESH:
        handleViewRefresh(in);
        break;
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
      case R.id.edit_image_view:
        nameTextView.setKeyListener(keyListener);
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
    String userName = params.get(ParamsKey.USER_NAME, "");
    String imgUrl = params.get(ParamsKey.AVATAR_URL, "");
    if (!TextUtils.isEmpty(userName)) {
      nameTextView.setText(userName);
      existLoginTextView.setVisibility(View.VISIBLE);
    } else {
      nameTextView.setText("请登录");
      existLoginTextView.setVisibility(View.GONE);
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
    } else {
      avatarImageView.setImageResource(R.drawable.user_avatar_def);
    }
  }
}
