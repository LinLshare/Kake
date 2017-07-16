package com.home77.kake.business.main.view;

import com.home77.kake.base.BaseView;
import com.home77.kake.business.main.presenter.ProfilePresenter;

/**
 * @author CJ
 */
public interface ProfileView extends BaseView<ProfilePresenter> {

  void bindData(String userName, String imgUrl);
}
