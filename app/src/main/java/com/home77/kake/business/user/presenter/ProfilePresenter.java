package com.home77.kake.business.user.presenter;

import android.text.TextUtils;

import com.home77.common.base.event.GenericEvent;
import com.home77.kake.App;
import com.home77.kake.GlobalData;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.business.user.view.ProfileView;

/**
 * @author CJ
 */
public class ProfilePresenter extends BasePresenter<ProfileView> {

  public ProfilePresenter(ProfileView attachedView) {
    super(attachedView);
  }

  @Override
  public void start() {
  }

  @Override
  public void onCreateView() {
    String userName = App.globalData().getString(GlobalData.KEY_USER_NAME, "");
    if (TextUtils.isEmpty(userName)) {
      userName = App.globalData().getString(GlobalData.KEY_USER_MOBILE, "");
    }
    attachedView.bindData(userName, App.globalData().getString(GlobalData.KEY_USER_AVATER, ""));
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

  public void handleEditUserClick() {

  }

  public void handleNameTextClick() {
    App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_LOGIN));
  }

  public void handleExistLogin() {
    App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_EXIST_LOGIN));
  }
}
