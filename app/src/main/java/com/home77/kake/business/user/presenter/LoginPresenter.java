package com.home77.kake.business.user.presenter;

import com.home77.common.base.event.GenericEvent;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.R;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.common.api.ServerConfig;
import com.home77.kake.common.api.UserService;
import com.home77.kake.common.api.response.UserResponse;
import com.home77.kake.business.user.view.LoginView;
import com.home77.kake.common.utils.InputChecker;

import static com.home77.kake.business.user.UserActivity.EVENT_TO_FORGET_PSW;
import static com.home77.kake.business.user.UserActivity.EVENT_TO_PROFILE;
import static com.home77.kake.business.user.UserActivity.EVENT_TO_REGISTER;

/**
 * @author CJ
 */
public class LoginPresenter extends BasePresenter<LoginView> {
  private UserService userService;

  public LoginPresenter(LoginView attachedView) {
    super(attachedView);
    userService = Instance.of(UserService.class);
  }

  @Override
  public void start() {
  }

  @Override
  public void onCreateView() {
  }

  public void handleBackClick() {
    App.eventBus().post(new GenericEvent(this, EVENT_TO_PROFILE));
  }

  public void handleLoginClick(String userName, String password) {
    if (!InputChecker.isUserNameLegal(userName) || !InputChecker.isPasswordLegal(password)) {
      attachedView.toast(R.string.usr_name_or_psw_illegal);
      return;
    }
    userService.login(userName, password, new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        UserResponse userResponse = source.responseClass(UserResponse.class);
        if (userResponse != null) {
          // 图片需要拼接host
          App.globalData()
             .putString(GlobalData.KEY_USER_MOBILE, userResponse.getMobile())
             .putString(GlobalData.KEY_USER_AVATER,
                        ServerConfig.BASE_IMG_URL + userResponse.getAvatar())
             .putString(GlobalData.KEY_USER_NAME, userResponse.getName());
          App.eventBus().post(new GenericEvent(LoginPresenter.this, EVENT_TO_PROFILE));
          attachedView.toast(R.string.login_success);
        }
      }

      @Override
      public void onError(String msg) {
        attachedView.toast("登录失败");
      }
    });
  }

  public void handleForgetPasswordClick() {
    App.eventBus().post(new GenericEvent(this, EVENT_TO_FORGET_PSW));
  }

  public void handleRegisterUserClick() {
    App.eventBus().post(new GenericEvent(this, EVENT_TO_REGISTER));
  }
}
