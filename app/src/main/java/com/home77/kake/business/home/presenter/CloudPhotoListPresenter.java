package com.home77.kake.business.home.presenter;

import com.home77.kake.App;
import com.home77.kake.base.BasePresenter;
import com.home77.kake.business.home.CloudPhotoActivity;
import com.home77.kake.business.home.view.CloudPhotoListView;

/**
 * @author CJ
 */
public class CloudPhotoListPresenter extends BasePresenter<CloudPhotoListView> {
  public CloudPhotoListPresenter(CloudPhotoListView attachedView) {
    super(attachedView);
  }

  @Override
  public void start() {

  }

  @Override
  public void onCreateView() {
  }

  public void onMenuNavClick() {

  }

  public void onRefresh() {

  }

  public void onBackNavClick() {
    CloudPhotoActivity.NavEvent navEvent =
        new CloudPhotoActivity.NavEvent(this, CloudPhotoActivity.NavEvent.CLICK_BACK, null);
    App.eventBus().post(navEvent);
  }
}
