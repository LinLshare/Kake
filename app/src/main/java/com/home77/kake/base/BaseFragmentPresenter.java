package com.home77.kake.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;

/**
 * @author CJ
 */
public abstract class BaseFragmentPresenter
    implements BasePresenter, FragmentLifeCycle, KeyEventInterface {

  protected BaseView baseView;
  protected NavigateCallback navigateCallback;

  public BaseFragmentPresenter(BaseView baseView, NavigateCallback navigateCallback) {
    this.baseView = baseView;
    this.navigateCallback = navigateCallback;
  }

  @Override
  public void start(Params params) {
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
  public void onCreate(@Nullable Bundle savedInstanceState) {
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
