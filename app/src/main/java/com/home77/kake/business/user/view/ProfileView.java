package com.home77.kake.business.user.view;

import com.home77.kake.base.BaseView;
import com.home77.kake.business.user.presenter.ProfilePresenter;

/**
 * @author CJ
 */
public interface ProfileView extends BaseView<ProfilePresenter> {

  void bindData(String userName, String imgUrl);

  void onLogout();

  void onLogin();
}
