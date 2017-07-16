package com.home77.kake.business.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home77.kake.R;
import com.home77.kake.business.main.presenter.LoginPresenter;

/**
 * @author CJ
 */
public class LoginFragment extends Fragment implements LoginView {
  private LoginPresenter loginPresenter;

  @Override
  public void setPresenter(LoginPresenter presenter) {
    this.loginPresenter = presenter;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View contentView = inflater.inflate(R.layout.fragment_login, container, false);
    return contentView;
  }
}
