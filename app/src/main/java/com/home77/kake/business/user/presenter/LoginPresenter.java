package com.home77.kake.business.user.presenter;

import android.text.TextUtils;

import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.R;
import com.home77.kake.bs.BaseFragmentPresenter;
import com.home77.kake.bs.BaseView;
import com.home77.kake.bs.CmdType;
import com.home77.kake.bs.MsgType;
import com.home77.kake.bs.NavigateCallback;
import com.home77.kake.bs.ParamsKey;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.common.api.ServerConfig;
import com.home77.kake.common.api.response.UserResponse;
import com.home77.kake.common.api.service.UserService;
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
    navigateCallback.onNavigate(UserActivity.EVENT_TO_PROFILE);
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
        final UserResponse userResponse = source.responseClass(UserResponse.class);
        if (userResponse != null) {
          // 图片需要拼接host
          String name = userResponse.getName();
          if (TextUtils.isEmpty(name)) {
            name = userResponse.getMobile();
          }
          App.globalData()
             .putInt(GlobalData.KEY_USER_ID, userResponse.getId())
             .putString(GlobalData.KEY_USER_MOBILE, userResponse.getMobile())
             .putString(GlobalData.KEY_USER_NAME, name)
             .putString(GlobalData.KEY_USER_AVATER,
                        ServerConfig.BASE_IMG_URL + userResponse.getAvatar())
             .putString(GlobalData.KEY_USER_NAME, userResponse.getName());
          baseView.onCommand(CmdType.LOGIN_SUCCESS, null, null);
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
    navigateCallback.onNavigate(UserActivity.EVENT_TO_FORGET_PSW);
  }

  private void handleRegisterUserClick() {
    navigateCallback.onNavigate(UserActivity.EVENT_TO_REGISTER);
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
