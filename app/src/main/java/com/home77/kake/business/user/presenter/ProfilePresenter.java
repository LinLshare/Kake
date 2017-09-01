package com.home77.kake.business.user.presenter;

import com.google.gson.reflect.TypeToken;
import com.home77.common.base.collection.Params;
import com.home77.common.base.debug.DLog;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
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

import java.io.File;

/**
 * @author CJ
 */
public class ProfilePresenter extends BaseFragmentPresenter {
  private static final String TAG = ProfilePresenter.class.getSimpleName();
  private UserService userService;

  public ProfilePresenter(BaseView baseView, NavigateCallback callback) {
    super(baseView, callback);
    userService = Instance.of(UserService.class);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_BACK:
        handleClickBack();
        break;
      case CLICK_AVATAR:
        baseView.onCommand(CmdType.SHOW_AVATAR_SELECT_DIALOG, null, null);
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
      case CLICK_ABOUT:
        navigateCallback.onNavigate(UserActivity.EVENT_TO_ABOUT, null);
        break;
      case CLICK_CLOUD_ALBUM:
        navigateCallback.onNavigate(UserActivity.EVENT_TO_HOME,
                                    Params.create(ParamsKey.FLAG, true));
        break;
      case CLICK_HELP:
        navigateCallback.onNavigate(UserActivity.EVENT_TO_HELP, null);
        break;
      case TAKE_AVATAR_FILE: {
        File file = params.get(ParamsKey.FILE);

        baseView.onCommand(CmdType.AVATAR_UPDATING, null, null);
        userService.updateAvatar(file, new URLFetcher.Delegate() {
          @Override
          public void onSuccess(URLFetcher source) {
            Response response = source.responseClass(Response.class);
            if (response != null && response.getCode() == 200) {
              baseView.onCommand(CmdType.AVATAR_UPDATE_SUCCESS, null, null);
            } else {
              baseView.onCommand(CmdType.AVATAR_UPDATE_ERROR, null, null);
            }
          }

          @Override
          public void onError(String msg) {
            DLog.d(TAG, msg + "");
            baseView.onCommand(CmdType.AVATAR_UPDATE_ERROR, null, null);
          }
        });
      }
      break;
    }
  }

  private void handleClickBack() {
    navigateCallback.onNavigate(UserActivity.EVENT_TO_HOME, null);
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
                                         baseView.onCommand(CmdType.RENAME_SUCCESS, null, null);
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
      navigateCallback.onNavigate(UserActivity.EVENT_TO_LOGIN, null);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    userService.getUserInfo(new URLFetcher.Delegate() {
      @Override
      public void onSuccess(URLFetcher source) {
        final Response<UserResponse> userResponse =
            source.responseClass(new TypeToken<Response<UserResponse>>() {
            });
        if (userResponse != null) {
          userService.saveUserInfo(userResponse.getData());
        }
        baseView.onCommand(CmdType.VIEW_REFRESH, null, null);
      }

      @Override
      public void onError(String msg) {
        //        baseView.onCommand(CmdType.VIEW_REFRESH, null, null);
      }
    });

  }

  private void handleClickLogout() {
    App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.EVENT_LOGOUT, null));
    baseView.onCommand(CmdType.VIEW_REFRESH,
                       Params.create(ParamsKey.USER_NAME, "").put(ParamsKey.AVATAR_URL, ""),
                       null);
  }
}
