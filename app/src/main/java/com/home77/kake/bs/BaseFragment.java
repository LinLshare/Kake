package com.home77.kake.bs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;

/**
 * @author CJ
 */
public abstract class BaseFragment extends Fragment implements BaseView {
  protected BaseFragmentPresenter presenter;

  public BaseFragment() {
  }

  public void setPresenter(BaseFragmentPresenter presenter) {
    this.presenter = presenter;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return presenter.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    presenter.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.onStop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    presenter.onDestroyView();
  }

  @Override
  public void onCommand(final CmdType cmdType, final Params in, final Params out) {
    BaseHandler.runOnMainThread(new Runnable() {
      @Override
      public void run() {
        executeCommand(cmdType, in, out);
      }
    });
  }

  public abstract void executeCommand(CmdType cmdType, Params in, Params out);
}
