package com.home77.kake.business.user.presenter;

import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.business.user.view.LoginView;
import com.home77.kake.common.utils.InputChecker;

import static com.home77.kake.business.user.UserActivity.NavigateEvent.EVENT_TO_FORGET_PSW;
import static com.home77.kake.business.user.UserActivity.NavigateEvent.EVENT_TO_PROFILE;
import static com.home77.kake.business.user.UserActivity.NavigateEvent.EVENT_TO_REGISTER;

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
       .post(new UserActivity.NavigateEvent(this, EVENT_TO_PROFILE));
  }

  public void handleLoginClick(String userName, String password) {
    if (!InputChecker.isUserNameLegal(userName) || !InputChecker.isPasswordLegal(password)) {
      attachedView.toast(R.string.usr_name_or_psw_illegal);
      return;
    }
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, EVENT_TO_PROFILE));
    attachedView.toast(R.string.login_success);
  }

  public void handleForgetPasswordClick() {
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, EVENT_TO_FORGET_PSW));
  }

  public void handleRegisterUserClick() {
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, EVENT_TO_REGISTER));
  }
}
