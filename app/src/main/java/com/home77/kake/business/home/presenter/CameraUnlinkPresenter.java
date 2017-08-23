package com.home77.kake.business.home.presenter;

import com.home77.common.base.collection.Params;
import com.home77.kake.App;
import com.home77.kake.base.BaseFragmentPresenter;
import com.home77.kake.base.BaseView;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.NavigateCallback;

/**
 * @author CJ
 */
public class CameraUnlinkPresenter extends BaseFragmentPresenter {

  private static final String TAG = CameraUnlinkPresenter.class.getSimpleName();

  public CameraUnlinkPresenter(BaseView baseView) {
    super(baseView, null);
  }

  @Override
  public void handleMessage(MsgType msgType, Params params) {
    switch (msgType) {
      case CLICK_LINCK_CAMERA:
        baseView.onCommand(CmdType.OPEN_WIFI_SETTING, null, null);
        break;
    }
  }
}
