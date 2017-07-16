package com.home77.kake.business.main.presenter;

import android.text.TextUtils;

import com.home77.kake.App;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.main.UserActivity;
import com.home77.kake.business.main.view.RegisterView;

/**
 * @author CJ
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {
  public RegisterPresenter(RegisterView attachedView) {
    super(attachedView);
  }

  @Override
  public void start() {
  }

  @Override
  public void onCreateView() {

  }

  public void handleGetCheckCodeClick(String phoneNumber) {
    if (TextUtils.isEmpty(phoneNumber)) {
      attachedView.toast("手机号不合法");
      return;
    }
  }

  public void handleRegisterClick(String phoneNumber,
                                  String checkCode,
                                  String password,
                                  String confirmPassword) {
    if (TextUtils.isEmpty(phoneNumber)) {
      attachedView.toast("手机号不合法");
      return;
    }

    // check code
    if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
      attachedView.toast("密码或确认密码不能为空");
      return;
    }

    if (!TextUtils.equals(password, confirmPassword)) {
      attachedView.toast("密码与确认密码不一致，请重新输入");
      return;
    }

    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_PROFILE));
    attachedView.toast("注册成功");
  }

  public void handleBackClick() {
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_LOGIN));
  }
}
