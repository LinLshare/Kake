package com.home77.kake.business.user.presenter;

import com.home77.common.base.collection.Params;
import com.home77.common.base.event.GenericEvent;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.bs.BaseFragmentPresenter;
import com.home77.kake.bs.BaseView;
import com.home77.kake.bs.CmdType;
import com.home77.kake.bs.MsgType;
import com.home77.kake.bs.ParamsKey;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.common.api.response.Response;
import com.home77.kake.common.api.service.UserService;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;

/**
 * @author CJ
 */
public class ProfilePresenter extends BaseFragmentPresenter {
  private UserService userService;

  public ProfilePresenter(BaseView baseView) {
    super(baseView);
    userService = Instance.of(UserService.class);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_BACK:
        handleClickBack();
        break;
      case CLICK_AVATAR:
        break;
      case CLICK_USER_NAME:
        handleClickUserName();
        break;
      case EDIT_USER_NAME_DONE:
        handleEditUserNameDone(params.get(ParamsKey.USER_NAME, ""));
        break;
      case CLICK_LOGOUT:
        handleClickLogout();
        break;
      case VIEW_REFRESH:

        break;
    }
  }

  private void handleClickBack() {
    App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_HOME));
  }

  private void handleEditUserNameDone(final String name) {
    userService.updateUserName(App.globalData().getInt(GlobalData.KEY_USER_ID, 0),
                               name,
                               new URLFetcher.Delegate() {
                                 @Override
                                 public void onSuccess(URLFetcher source) {
                                   final Response response = source.responseClass(Response.class);
                                   if (response != null) {
                                     switch (response.getCode()) {
                                       case 200:
                                         App.globalData().putString(GlobalData.KEY_USER_NAME, name);
                                         break;
                                       default:
                                         baseView.onCommand(CmdType.TOAST,
                                                            Params.create(ParamsKey.MSG,
                                                                          response.getMessage()),
                                                            null);
                                         break;
                                     }
                                   } else {
                                     baseView.onCommand(CmdType.TOAST,
                                                        Params.create(ParamsKey.MSG, "更新用户名失败[0]"),
                                                        null);
                                   }
                                 }

                                 @Override
                                 public void onError(final String msg) {
                                   baseView.onCommand(CmdType.TOAST,
                                                      Params.create(ParamsKey.MSG, "更新用户名失败[-1]"),
                                                      null);
                                 }
                               });
  }

  private void handleClickUserName() {
    if (!App.isLogin()) {
      App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_LOGIN));
    }
  }

  private void handleClickLogout() {
    App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.EVENT_LOGOUT, null));
    baseView.onCommand(CmdType.VIEW_REFRESH,
                       Params.create(ParamsKey.USER_NAME, "").put(ParamsKey.AVATAR_URL, ""),
                       null);
  }
}
