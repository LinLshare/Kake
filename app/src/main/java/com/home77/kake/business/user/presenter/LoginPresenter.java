package com.home77.kake.business.user.presenter;

import com.google.gson.reflect.TypeToken;
import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.common.api.response.Response;
import com.home77.kake.common.api.response.UserResponse;
import com.home77.kake.common.api.service.UserService;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;
import com.home77.kake.common.utils.InputChecker;

/**
 * @author CJ
 */
public class LoginPresenter extends BaseFragmentPresenter {
  private UserService userService;

  public LoginPresenter(BaseView baseView, NavigateCallback navigateCallback) {
    super(baseView, navigateCallback);
    userService = Instance.of(UserService.class);
  }

  private void handleBackClick() {
    navigateCallback.onNavigate(UserActivity.EVENT_TO_PROFILE, null);
  }

  private void handleLoginClick(final String userName, String password) {
    if (!InputChecker.isUserNameLegal(userName) || !InputChecker.isPasswordLegal(password)) {
      baseView.onCommand(CmdType.TOAST,
                         Params.create(ParamsKey.MSG_INT, R.string.usr_name_or_psw_illegal),
                         null);
      return;
    }
    baseView.onCommand(CmdType.LOGINING, null, null);
    userService.login(userName, password, new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        final Response<UserResponse> userResponse =
            source.responseClass(new TypeToken<Response<UserResponse>>() {
            });
        if (userResponse != null) {
          userService.saveUserInfo(userResponse.getData());
          baseView.onCommand(CmdType.LOGIN_SUCCESS, null, null);
          App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.EVENT_LOGIN, null));
          navigateCallback.onNavigate(UserActivity.EVENT_TO_PROFILE, null);
        } else {
          baseView.onCommand(CmdType.LOGIN_ERROR, Params.create(ParamsKey.MSG, "登录失败[0]"), null);
        }
      }

      @Override
      public void onError(final String msg) {
        baseView.onCommand(CmdType.LOGIN_ERROR, Params.create(ParamsKey.MSG, "登录失败[-1]"), null);
      }
    });
  }

  private void handleForgetPasswordClick() {
    navigateCallback.onNavigate(UserActivity.EVENT_TO_FORGET_PSW, null);
  }

  private void handleRegisterUserClick() {
    navigateCallback.onNavigate(UserActivity.EVENT_TO_REGISTER, null);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_BACK:
        handleBackClick();
        break;
      case CLICK_FORGET_PASSWORD:
        handleForgetPasswordClick();
        break;
      case CLICK_LOGIN:
        handleLoginClick(params.get(ParamsKey.PHONE_NUMBER, ""),
                         params.get(ParamsKey.PASSWORD, ""));
        break;
      case CLICK_REGISTER_NOW:
        handleRegisterUserClick();
        break;
    }
  }
}
