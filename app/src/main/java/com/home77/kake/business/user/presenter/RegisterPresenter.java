package com.home77.kake.business.user.presenter;

import com.home77.common.base.collection.Params;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.common.api.response.CheckcodeResponse;
import com.home77.kake.common.api.response.RegisterResponse;
import com.home77.kake.common.api.service.UserService;
import com.home77.kake.common.utils.InputChecker;

/**
 * @author CJ
 */
public class RegisterPresenter extends BaseFragmentPresenter {
  private UserService userService;

  public RegisterPresenter(BaseView baseView, NavigateCallback navigateCallback) {
    super(baseView, navigateCallback);
    userService = Instance.of(UserService.class);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_CHECK_CODE:
        handleGetCheckCodeClick(params.get(ParamsKey.PHONE_NUMBER, ""));
        break;
      case CLICK_REGISTER:
        handleRegisterClick(params.get(ParamsKey.PHONE_NUMBER, ""),
                            params.get(ParamsKey.CHECK_CODE, ""),
                            params.get(ParamsKey.PASSWORD, ""),
                            params.get(ParamsKey.PASSWORD_CONFIRM, ""));
        break;
      case CLICK_BACK:
        handleBackClick();
        break;
    }
  }

  private void handleGetCheckCodeClick(String phoneNumber) {
    if (!InputChecker.isPhoneNumberLegal(phoneNumber)) {
      baseView.onCommand(CmdType.TOAST,
                         Params.create(ParamsKey.MSG_INT, R.string.phone_number_illegal),
                         null);
      return;
    }

    baseView.onCommand(CmdType.CHECK_CODE_COUNT_DOWN,
                       Params.create(ParamsKey.CHECK_CODE_COUNT_DOWN, 60),
                       null);
    userService.gainCheckCode(phoneNumber, new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        CheckcodeResponse checkcodeResponse = source.responseClass(CheckcodeResponse.class);
        //
        baseView.onCommand(CmdType.CHECK_CODE_COUNT_DOWN,
                           Params.create(ParamsKey.CHECK_CODE_COUNT_DOWN, 60),
                           null);
        if (checkcodeResponse != null && checkcodeResponse.getMessage() != null) {
          baseView.onCommand(CmdType.TOAST,
                             Params.create(ParamsKey.MSG, checkcodeResponse.getMessage()),
                             null);
        } else {
          baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, "服务器异常，正在维护"), null);
        }
      }

      @Override
      public void onError(String msg) {
        baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, "网络异常，请稍后重试"), null);
      }
    });
  }

  private void handleRegisterClick(final String phoneNumber,
                                   String checkCode,
                                   final String password,
                                   final String confirmPassword) {
    if (!InputChecker.isPhoneNumberLegal(phoneNumber)) {
      baseView.onCommand(CmdType.TOAST,
                         Params.create(ParamsKey.MSG, R.string.phone_number_illegal),
                         null);
      return;
    }

    // check code

    if (!InputChecker.isPasswordLegal(password)) {
      baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, R.string.psw_illegal), null);
      return;
    }
    baseView.onCommand(CmdType.REGISTERING, null, null);
    userService.register(phoneNumber,
                         checkCode,
                         password,
                         confirmPassword,
                         new URLFetcher.Delegate() {
                           @Override
                           public void onSuccess(URLFetcher source) {
                             final RegisterResponse registerResponse =
                                 source.responseClass(RegisterResponse.class);
                             if (registerResponse == null) {
                               baseView.onCommand(CmdType.REGISTER_ERROR,
                                                  Params.create(ParamsKey.MSG, "注册失败"),
                                                  null);
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
                               baseView.onCommand(CmdType.REGISTER_SUCCESS, null, null);
                               navigateCallback.onNavigate(UserActivity.EVENT_TO_LOGIN, null);
                             }
                           }

                           @Override
                           public void onError(String msg) {
                             baseView.onCommand(CmdType.REGISTER_ERROR,
                                                Params.create(ParamsKey.MSG, "注册失败"),
                                                null);
                           }
                         });
  }

  private void handleBackClick() {
    navigateCallback.onNavigate(UserActivity.EVENT_TO_LOGIN, null);
  }
}
