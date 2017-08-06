package com.home77.kake.bs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CJ
 */
public class BaseActivity extends AppCompatActivity {
  List<BaseFragmentPresenter> presenterList = new ArrayList<>();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    for (BaseFragmentPresenter presenter : presenterList) {
      if (presenter.onKeyDown(keyCode, event)) {
        return true;
      }
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public void onBackPressed() {
    for (BaseFragmentPresenter presenter : presenterList) {
      presenter.onBackPressed();
    }
    super.onBackPressed();
  }
}
