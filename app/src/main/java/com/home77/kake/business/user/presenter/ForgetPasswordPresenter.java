package com.home77.kake.business.user.presenter;

import com.home77.common.base.collection.Params;
import com.home77.common.base.event.GenericEvent;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.bs.BaseFragmentPresenter;
import com.home77.kake.bs.BaseView;
import com.home77.kake.bs.CmdType;
import com.home77.kake.bs.MsgType;
import com.home77.kake.bs.ParamsKey;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.common.api.response.CheckcodeResponse;
import com.home77.kake.common.api.response.Response;
import com.home77.kake.common.api.service.UserService;
import com.home77.kake.common.utils.InputChecker;

/**
 * @author CJ
 */
public class ForgetPasswordPresenter extends BaseFragmentPresenter {
  private UserService userService;

  public ForgetPasswordPresenter(BaseView baseView) {
    super(baseView);
    userService = Instance.of(UserService.class);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_BACK:
        App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_LOGIN));
        break;
      case CLICK_CHECK_CODE:
        handleClickCheckCode(params.get(ParamsKey.PHONE_NUMBER, ""));
        break;
      case CLICK_SUBMIT:
        handleClickSubmit(params.get(ParamsKey.PHONE_NUMBER, ""),
                          params.get(ParamsKey.CHECK_CODE, ""),
                          params.get(ParamsKey.PASSWORD, ""));
        break;
    }
  }

  private void handleClickCheckCode(String phoneNumber) {
    if (!InputChecker.isPhoneNumberLegal(phoneNumber)) {
      baseView.onCommand(CmdType.TOAST,
                         Params.create(ParamsKey.MSG_INT, R.string.phone_number_illegal),
                         null);
      return;
    }
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

  private void handleClickSubmit(String phoneNumber, String checkCode, String password) {
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
    baseView.onCommand(CmdType.PASSWORD_RESETING, null, null);
    userService.resetPassword(phoneNumber, checkCode, password, new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        final Response response = source.responseClass(Response.class);
        if (response == null || response.getCode() != 0) {
          baseView.onCommand(CmdType.PASSWORD_RESET_ERROR,
                             Params.create(ParamsKey.MSG, "重置密码失败"),
                             null);
        } else {
          baseView.onCommand(CmdType.PASSWORD_RESET_SUCCESS, null, null);
        }
      }

      @Override
      public void onError(String msg) {
        baseView.onCommand(CmdType.PASSWORD_RESET_ERROR,
                           Params.create(ParamsKey.MSG, "重置密码失败"),
                           null);
      }
    });
  }
}
