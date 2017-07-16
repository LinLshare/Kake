package com.home77.kake.base;

import android.support.v4.app.Fragment;

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
  public void toast(String content) {
    Toast.showShort(content);
  }

  @Override
  public void toast(int contentId) {
    Toast.showShort(contentId);
  }
}
