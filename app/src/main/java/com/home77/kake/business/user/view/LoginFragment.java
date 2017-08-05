package com.home77.kake.business.user.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.business.user.presenter.LoginPresenter;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class LoginFragment extends BaseFragment<LoginPresenter> implements LoginView {

  @BindView(R.id.user_name_edit_text)
  EditText userNameEditText;
  @BindView(R.id.psw_edit_text)
  EditText pswEditText;
  Unbinder unbinder;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, container, false);
    unbinder = ButterKnife.bind(this, view);
    presenter.onViewCreated();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @OnClick({
      R.id.back_image_view, R.id.login_text_view, R.id.forget_psw_text_view,
      R.id.register_usr_text_view
  })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        presenter.handleBackClick();
        break;
      case R.id.login_text_view:
        presenter.handleLoginClick(userNameEditText.getText().toString(),
                                   pswEditText.getText().toString());
        break;
      case R.id.forget_psw_text_view:
        presenter.handleForgetPasswordClick();
        break;
      case R.id.register_usr_text_view:
        presenter.handleRegisterUserClick();
        break;
    }
  }

  @Override
  public void onLogin() {
    App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_SHOW, null));
  }

  @Override
  public void onLoginSuccess() {
    App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
    toast(R.string.login_success);
  }

  @Override
  public void onLoginError(String msg) {
    App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
    toast(msg);
  }
}
