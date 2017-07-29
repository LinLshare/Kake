package com.home77.kake.business.user.presenter;

import com.home77.common.base.event.GenericEvent;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.business.user.view.ForgetPasswordView;
import com.home77.kake.common.utils.InputChecker;

/**
 * @author CJ
 */
public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordView> {
  public ForgetPasswordPresenter(ForgetPasswordView attachedView) {
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

  public void handleRegisterClick(String phoneNumber, String checkCode, String password) {
    if (!InputChecker.isPhoneNumberLegal(phoneNumber)) {
      attachedView.toast(R.string.phone_number_illegal);
      return;
    }

    // check code

    if (!InputChecker.isPasswordLegal(password)) {
      attachedView.toast(R.string.psw_illegal);
      return;
    }

    App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_PROFILE));
    attachedView.toast(R.string.submit_success);
  }

  public void handleBackClick() {
    App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_LOGIN));
  }
}
