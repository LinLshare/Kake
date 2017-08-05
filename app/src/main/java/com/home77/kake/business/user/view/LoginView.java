package com.home77.kake.business.user.view;

import com.home77.kake.base.BaseView;
import com.home77.kake.business.user.presenter.LoginPresenter;

/**
 * @author CJ
 */
public interface LoginView extends BaseView<LoginPresenter> {
  void onLogin();

  void onLoginSuccess();

  void onLoginError(String msg);
}
