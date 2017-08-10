package com.home77.kake.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;
import com.home77.common.ui.widget.Toast;

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
    View view = presenter.onCreateView(inflater, container, savedInstanceState);
    view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
      @Override
      public void onSystemUiVisibilityChange(int visibility) {
        if (visibility == View.VISIBLE) {
          presenter.onMessage(MsgType.VIEW_REFRESH, null);
        }
      }
    });
    return view;
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
        switch (cmdType) {
          case TOAST:
            String msg = in.get(ParamsKey.MSG, "");
            if (!TextUtils.isEmpty(msg)) {
              Toast.showShort(msg);
              break;
            }
            int msgInt = in.get(ParamsKey.MSG_INT, 0);
            if (msgInt != 0) {
              Toast.showShort(msgInt);
            }
            break;
          default:
            executeCommand(cmdType, in, out);
            break;
        }
      }
    });
  }

  public abstract void executeCommand(CmdType cmdType, Params in, Params out);
}
