package com.home77.kake.base;

import android.view.KeyEvent;

/**
 * @author CJ
 */
public interface KeyEventInterface {
  boolean onKeyDown(int keyCode, KeyEvent event);

  void onBackPressed();
}
