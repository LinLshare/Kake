package com.home77.kake.business.user;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.home77.common.base.component.BaseHandler;
import com.home77.kake.R;
import com.home77.kake.bs.NavigateCallback;
import com.home77.kake.business.user.presenter.ForgetPasswordPresenter;
import com.home77.kake.business.user.presenter.LoginPresenter;
import com.home77.kake.business.user.presenter.ProfilePresenter;
import com.home77.kake.business.user.presenter.RegisterPresenter;
import com.home77.kake.business.user.view.ForgetPasswordFragment;
import com.home77.kake.business.user.view.LoginFragment;
import com.home77.kake.business.user.view.ProfileFragment;
import com.home77.kake.business.user.view.RegisterFragment;

/**
 * @author CJ
 */
public class UserActivity extends AppCompatActivity implements NavigateCallback {

  public static final int EVENT_TO_PROFILE = 1;
  public static final int EVENT_TO_LOGIN = 2;
  public static final int EVENT_TO_REGISTER = 3;
  public static final int EVENT_TO_HOME = 4;
  public static final int EVENT_EXIST_LOGIN = 5;
  public static final int EVENT_TO_FORGET_PSW = 6;

  private ProfileFragment profileFragment;
  private LoginFragment loginFragment;
  private ProfilePresenter profilePresenter;
  private LoginPresenter loginPresenter;
  private RegisterFragment registerFragment;
  private RegisterPresenter registerPresenter;
  private ForgetPasswordFragment forgetPasswordFragment;
  private ForgetPasswordPresenter forgetPasswordPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);

    //1). setUp view
    profileFragment = new ProfileFragment();
    loginFragment = new LoginFragment();
    registerFragment = new RegisterFragment();
    forgetPasswordFragment = new ForgetPasswordFragment();
    //2) setUp presenter
    profilePresenter = new ProfilePresenter(profileFragment, this);
    loginPresenter = new LoginPresenter(loginFragment, this);
    registerPresenter = new RegisterPresenter(registerFragment, this);
    forgetPasswordPresenter = new ForgetPasswordPresenter(forgetPasswordFragment, this);
    //3) bind presenter
    profileFragment.setPresenter(profilePresenter);
    loginFragment.setPresenter(loginPresenter);
    registerFragment.setPresenter(registerPresenter);
    forgetPasswordFragment.setPresenter(forgetPasswordPresenter);
    //4) commit init fragment
    getSupportFragmentManager().beginTransaction()
                               .add(R.id.content_layout, profileFragment)
                               .commit();
  }

  @Override
  public void onNavigate(final int eventType) {
    BaseHandler.runOnMainThread(new Runnable() {
      @Override
      public void run() {
        switch (eventType) {
          case EVENT_TO_PROFILE:
            // clear back stack
            getSupportFragmentManager().popBackStack(null,
                                                     FragmentManager.POP_BACK_STACK_INCLUSIVE);

            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.content_layout, profileFragment)
                                       .commit();
            break;
          case EVENT_TO_LOGIN:
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.content_layout, loginFragment)
                                       .addToBackStack(null)
                                       .commit();
            break;
          case EVENT_TO_REGISTER:
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.content_layout, registerFragment)
                                       .addToBackStack(null)
                                       .commit();
            break;
          case EVENT_TO_FORGET_PSW:
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.content_layout, forgetPasswordFragment)
                                       .addToBackStack(null)
                                       .commit();
            break;
          case EVENT_TO_HOME:
            UserActivity.this.finish();
            break;
        }
      }
    });
  }
}
