package com.home77.kake.business.user;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.home77.common.base.collection.Params;
import com.home77.common.base.component.BaseHandler;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.base.NavigateCallback;
import com.home77.kake.base.ParamsKey;
import com.home77.kake.business.home.HomeActivity;
import com.home77.kake.business.user.presenter.AboutPresenter;
import com.home77.kake.business.user.presenter.ForgetPasswordPresenter;
import com.home77.kake.business.user.presenter.HelpAndFeedbackPresenter;
import com.home77.kake.business.user.presenter.LoginPresenter;
import com.home77.kake.business.user.presenter.ProfilePresenter;
import com.home77.kake.business.user.presenter.RegisterPresenter;
import com.home77.kake.business.user.view.AboutFragment;
import com.home77.kake.business.user.view.ForgetPasswordFragment;
import com.home77.kake.business.user.view.HelpAndFeedbackFragment;
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
  public static final int EVENT_TO_ABOUT = 7;
  public static final int EVENT_TO_HELP = 8;

  public static final String EXTRA_FIRST_EVENT = "extra_first_event";
  private ProfileFragment profileFragment;
  private LoginFragment loginFragment;
  private ProfilePresenter profilePresenter;
  private LoginPresenter loginPresenter;
  private RegisterFragment registerFragment;
  private RegisterPresenter registerPresenter;
  private ForgetPasswordFragment forgetPasswordFragment;
  private ForgetPasswordPresenter forgetPasswordPresenter;
  private AboutFragment aboutFragment;
  private AboutPresenter aboutPresenter;
  private HelpAndFeedbackFragment helpAndFeedbackFragment;
  private HelpAndFeedbackPresenter helpAndFeedbackPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);

    //1). setUp view
    profileFragment = new ProfileFragment();
    loginFragment = new LoginFragment();
    registerFragment = new RegisterFragment();
    forgetPasswordFragment = new ForgetPasswordFragment();
    aboutFragment = new AboutFragment();
    helpAndFeedbackFragment = new HelpAndFeedbackFragment();
    //2) setUp presenter
    profilePresenter = new ProfilePresenter(profileFragment, this);
    loginPresenter = new LoginPresenter(loginFragment, this);
    registerPresenter = new RegisterPresenter(registerFragment, this);
    forgetPasswordPresenter = new ForgetPasswordPresenter(forgetPasswordFragment, this);
    aboutPresenter = new AboutPresenter(aboutFragment, this);
    helpAndFeedbackPresenter = new HelpAndFeedbackPresenter(helpAndFeedbackFragment, this);
    //3) bind presenter
    profileFragment.setPresenter(profilePresenter);
    loginFragment.setPresenter(loginPresenter);
    registerFragment.setPresenter(registerPresenter);
    forgetPasswordFragment.setPresenter(forgetPasswordPresenter);
    aboutFragment.setPresenter(aboutPresenter);
    helpAndFeedbackFragment.setPresenter(helpAndFeedbackPresenter);
    //4) commit init fragment
    int intExtra = getIntent().getIntExtra(EXTRA_FIRST_EVENT, EVENT_TO_PROFILE);
    onNavigate(intExtra, null);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (App.isLogin()) {
      setResult(RESULT_OK);
    }
  }

  @Override
  public void onNavigate(final int eventType, final Params params) {
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
            if (params != null) {
              Boolean flag = params.get(ParamsKey.FLAG);
              if (flag != null && flag) {
                setResult(HomeActivity.RESULT_CODE_CLOUD_ALBUM);
              }
            }
            UserActivity.this.finish();
            break;
          case EVENT_TO_ABOUT:
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.content_layout, aboutFragment)
                                       .addToBackStack(null)
                                       .commit();
            break;
          case EVENT_TO_HELP:
            getSupportFragmentManager().beginTransaction()
                                       .replace(R.id.content_layout, helpAndFeedbackFragment)
                                       .addToBackStack(null)
                                       .commit();
            break;
        }
      }
    });
  }
}
