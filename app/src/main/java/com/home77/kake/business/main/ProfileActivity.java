package com.home77.kake.business.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.home77.kake.R;
import com.home77.kake.business.main.presenter.LoginPresenter;
import com.home77.kake.business.main.view.LoginFragment;

/**
 * @author CJ
 */
public class ProfileActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    LoginFragment loginFragment = new LoginFragment();
    LoginPresenter loginPresenter = new LoginPresenter(loginFragment);
    getSupportFragmentManager().beginTransaction().add(R.id.content_layout, loginFragment).commit();
    loginFragment.setPresenter(loginPresenter);
    loginPresenter.start();
  }
}
