package com.home77.kake.business.user.presenter;

import android.text.TextUtils;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.event.GenericEvent;
import com.home77.common.base.pattern.Instance;
import com.home77.common.net.http.URLFetcher;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.business.user.view.ProfileView;
import com.home77.kake.common.api.response.Response;
import com.home77.kake.common.api.service.UserService;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author CJ
 */
public class ProfilePresenter extends BasePresenter<ProfileView> {
  private UserService userService;

  public ProfilePresenter(ProfileView attachedView) {
    super(attachedView);
    userService = Instance.of(UserService.class);
    App.eventBus().register(this);
  }

  @Override
  public void start() {
  }

  @Override
  public void onViewCreated() {
    String userName = App.globalData().getString(GlobalData.KEY_USER_NAME, "");
    if (TextUtils.isEmpty(userName)) {
      userName = App.globalData().getString(GlobalData.KEY_USER_MOBILE, "");
    }
    attachedView.bindData(userName, App.globalData().getString(GlobalData.KEY_USER_AVATER, ""));
  }

  @Override
  public void onViewDestroy() {
    App.eventBus().unregister(this);
  }

  public void handleBackImageViewClick() {
    App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_HOME));
  }

  public void handleAvatarClick() {

  }

  public void handleKakeClick() {

  }

  public void handleCloudGalleyClick() {

  }

  public void handleHelpFeedbackClick() {

  }

  public void handleAboutClick() {

  }

  public void handleEditUserClick(final String name) {
    userService.updateUserName(App.globalData().getInt(GlobalData.KEY_USER_ID, 0),
                               name,
                               new URLFetcher.Delegate() {
                                 @Override
                                 public void onSuccess(URLFetcher source) {
                                   final Response response = source.responseClass(Response.class);
                                   if (response != null && response.getCode() == 200) {
                                     App.globalData().putString(GlobalData.KEY_USER_NAME, name);
                                   }
                                   BaseHandler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                       attachedView.toast(response.getMessage() + "");
                                     }
                                   });
                                 }

                                 @Override
                                 public void onError(final String msg) {
                                   BaseHandler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                       attachedView.toast(msg + "");
                                     }
                                   });
                                 }
                               });
  }

  public void handleNameTextClick() {
    if (!App.isLogin()) {
      App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_LOGIN));
    }
  }

  public void handleExistLogin() {
    attachedView.onLogout();
    App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.EVENT_LOGOUT, null));
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(BroadCastEvent event) {
    switch (event.getEvent()) {
      case BroadCastEventConstant.EVENT_LOGIN:
        String userName = App.globalData().getString(GlobalData.KEY_USER_NAME, "");
        if (TextUtils.isEmpty(userName)) {
          userName = App.globalData().getString(GlobalData.KEY_USER_MOBILE, "");
        }
        attachedView.bindData(userName, App.globalData().getString(GlobalData.KEY_USER_AVATER, ""));
        attachedView.onLogin();
        break;
    }
  }
}
