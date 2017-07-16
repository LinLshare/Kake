package com.home77.kake.business.main.presenter;

import android.text.TextUtils;

import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.main.UserActivity;
import com.home77.kake.business.main.view.LoginView;

/**
 * @author CJ
 */
public class LoginPresenter extends BasePresenter<LoginView> {
  public LoginPresenter(LoginView attachedView) {
    super(attachedView);
  }

  @Override
  public void start() {
  }

  @Override
  public void onCreateView() {
  }

  public void handleBackClick() {
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_PROFILE));
  }

  public void handleLoginClick(String userName, String password) {
    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
      attachedView.toast(R.string.usr_name_or_psw_invalid);
      return;
    }
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_PROFILE));
    attachedView.toast(R.string.login_success);
  }

  public void handleForgetPasswordClick() {
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_FORGET_PSW));
  }

  public void handleRegisterUserClick() {
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_REGISTER));
  }
}
