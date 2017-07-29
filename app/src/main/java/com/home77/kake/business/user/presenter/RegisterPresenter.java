package com.home77.kake.business.user.presenter;

import android.text.TextUtils;

import com.home77.common.base.event.GenericEvent;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.R;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.business.user.service.UserService;
import com.home77.kake.business.user.service.response.CheckcodeResponse;
import com.home77.kake.business.user.service.response.RegisterResponse;
import com.home77.kake.business.user.view.RegisterView;
import com.home77.kake.common.utils.InputChecker;

/**
 * @author CJ
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {
  private UserService userService;

  public RegisterPresenter(RegisterView attachedView) {
    super(attachedView);
    userService = Instance.of(UserService.class);
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
    userService.gainCheckCode(phoneNumber, new URLFetcher.Delegate() {
      @Override
      public void onComplete(URLFetcher source) {
        if (source.isSuccess()) {
          CheckcodeResponse checkcodeResponse = source.responseClass(CheckcodeResponse.class);
          attachedView.onCheckcodeViewCountDown(60);
          if (checkcodeResponse != null && checkcodeResponse.getMessage() != null) {
            attachedView.toast(checkcodeResponse.getMessage());
          } else {
            attachedView.toast("服务器异常，正在维护");
          }
        } else {
          attachedView.toast("网络异常，请稍后重试");
        }
      }
    });
  }

  public void handleRegisterClick(final String phoneNumber,
                                  String checkCode,
                                  final String password,
                                  final String confirmPassword) {
    if (!InputChecker.isPhoneNumberLegal(phoneNumber)) {
      attachedView.toast(R.string.phone_number_illegal);
      return;
    }

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

    userService.register(phoneNumber,
                         password,
                         confirmPassword,
                         checkCode,
                         new URLFetcher.Delegate() {
                           @Override
                           public void onComplete(URLFetcher source) {
                             if (source.isSuccess()) {
                               RegisterResponse registerResponse =
                                   source.responseClass(RegisterResponse.class);
                               if (registerResponse == null) {
                                 attachedView.toast("注册失败");
                               } else {
                                 App.globalData()
                                    .putString(GlobalData.KEY_TOKEN_TYPE,
                                               registerResponse.getToken_type())
                                    .putString(GlobalData.KEY_ACCESS_TOKEN,
                                               registerResponse.getAccess_token())
                                    .putString(GlobalData.KEY_REFRESH_TOKEN,
                                               registerResponse.getRefresh_token())
                                    .putInt(GlobalData.KEY_EXPIRE_IN,
                                            registerResponse.getExpires_in());
                                 attachedView.toast("注册成功");
                                 App.eventBus()
                                    .post(new GenericEvent(this, UserActivity.EVENT_TO_LOGIN));
                               }
                             } else {
                               attachedView.toast("注册失败");
                             }
                           }
                         });
  }

  public void handleBackClick() {
    App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_LOGIN));
  }
}
