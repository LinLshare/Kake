package com.home77.kake.business.user.presenter;

import com.home77.common.base.collection.Params;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.user.UserActivity;

/**
 * @author CJ
 */
public class AboutPresenter extends BaseFragmentPresenter {
  public AboutPresenter(BaseView baseView, NavigateCallback navigateCallback) {
    super(baseView, navigateCallback);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_BACK:
        navigateCallback.onNavigate(UserActivity.EVENT_TO_PROFILE, null);
        break;
      case CLICK_UPDATE:
        baseView.onCommand(CmdType.TOAST, Params.create(ParamsKey.MSG, "已是最新版本"), null);
        break;
    }
  }
}
