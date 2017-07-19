package com.home77.kake.business.user.presenter;

import android.text.TextUtils;

import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.business.user.view.RegisterView;
import com.home77.kake.common.utils.InputChecker;

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
    if (!InputChecker.isPhoneNumberLegal(phoneNumber)) {
      attachedView.toast(R.string.phone_number_illegal);
      return;
    }
  }

  public void handleRegisterClick(String phoneNumber,
                                  String checkCode,
                                  String password,
                                  String confirmPassword) {
    if (!InputChecker.isPhoneNumberLegal(phoneNumber)) {
      attachedView.toast(R.string.phone_number_illegal);
      return;
    }

    // TODO: 2017/7/19 check code

    if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
      attachedView.toast(R.string.psw_or_confirm_psw_not_empty);
      return;
    }

    if (!InputChecker.isPasswordLegal(password)) {
      attachedView.toast(R.string.psw_illegal);
      return;
    }

    if (!TextUtils.equals(password, confirmPassword)) {
      attachedView.toast(R.string.psw_and_confirm_psw_not_equal);
      return;
    }

    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_PROFILE));
    attachedView.toast(R.string.register_success);
  }

  public void handleBackClick() {
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_LOGIN));
  }
}
