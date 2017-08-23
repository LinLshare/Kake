package com.home77.kake.business.home.view;

import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import com.home77.common.base.collection.Params;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class CameraUnlinkFragment extends BaseFragment {
  Unbinder unbinder;

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View view =
            LayoutInflater.from(getContext()).inflate(R.layout.fragment_camera_unlink, null);
        unbinder = ButterKnife.bind(this, view);
        out.put(ParamsKey.VIEW, view);
        break;
      case VIEW_DESTROY:
        unbinder.unbind();
        break;
      case OPEN_WIFI_SETTING:
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
        break;
    }
  }

  @OnClick(R.id.link_camera_text_view)
  public void onViewClicked() {
    presenter.onMessage(MsgType.CLICK_LINCK_CAMERA, null);
  }
}
