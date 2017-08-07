package com.home77.kake.bs;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;
import com.home77.kake.business.home.presenter.FragmentLifeCycle;

/**
 * @author CJ
 */
public abstract class BaseFragmentPresenter
    implements BasePresenter, FragmentLifeCycle, KeyEventInterface {

  protected BaseView baseView;

  public BaseFragmentPresenter(BaseView baseView) {
    this.baseView = baseView;
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    Params result = Params.create();
    Params in = Params.create(ParamsKey.BUNDLE, savedInstanceState);
    baseView.onCommand(CmdType.VIEW_CREATE, in, result);
    return result.get(ParamsKey.VIEW);
  }

  @Override
  public void onStart() {
  }

  @Override
  public void onPause() {
  }

  @Override
  public void onResume() {
  }

  @Override
  public void onStop() {
  }

  @Override
  public void onDestroyView() {
    baseView.onCommand(CmdType.VIEW_DESTROY, null, null);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return false;
  }

  @Override
  public void onBackPressed() {
  }

  @Override
  public void onMessage(final MsgType msgType, final Params params) {
    BaseHandler.runOnMainThread(new Runnable() {
      @Override
      public void run() {
        handleMessage(msgType, params);
      }
    });
  }

  public abstract void handleMessage(MsgType msgType, Params params);
}
