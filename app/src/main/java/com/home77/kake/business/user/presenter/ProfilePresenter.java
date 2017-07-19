package com.home77.kake.business.user.presenter;

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
    attachedView.bindData(App.globalData().getString(GlobalData.KEY_USER_NAME, ""),
                          App.globalData().getString(GlobalData.KEY_USER_AVATER, ""));
  }

  public void handleBackImageViewClick() {
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_HOME));
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
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_TO_LOGIN));
  }

  public void handleExistLogin() {
    App.eventBus()
       .post(new UserActivity.NavigateEvent(this, UserActivity.NavigateEvent.EVENT_EXIST_LOGIN));
  }
}
