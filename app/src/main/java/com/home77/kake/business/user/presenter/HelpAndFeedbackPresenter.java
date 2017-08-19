package com.home77.kake.business.user.presenter;

import com.home77.common.base.collection.Params;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.business.user.UserActivity;

/**
 * @author CJ
 */
public class HelpAndFeedbackPresenter extends BaseFragmentPresenter {
  public HelpAndFeedbackPresenter(BaseView baseView, NavigateCallback navigateCallback) {
    super(baseView, navigateCallback);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_BACK:
        navigateCallback.onNavigate(UserActivity.EVENT_TO_PROFILE, null);
        break;
    }
  }
}
