package com.home77.kake.base;

import android.support.v4.app.Fragment;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.ui.widget.Toast;

/**
 * @author CJ
 */
public abstract class BaseFragment<T> extends Fragment implements BaseView<T> {

  protected T presenter;

  @Override
  public void setPresenter(T presenter) {
    this.presenter = presenter;
  }

  @Override
  public void toast(final String content) {
    BaseHandler.post(new Runnable() {
      @Override
      public void run() {
        Toast.showShort(content);
      }
    });
  }

  @Override
  public void toast(final int contentId) {
    BaseHandler.post(new Runnable() {
      @Override
      public void run() {
        Toast.showShort(contentId);
      }
    });
  }
}
