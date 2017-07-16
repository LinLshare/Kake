package com.home77.common.ui.widget;

import android.support.annotation.StringRes;

import com.home77.common.base.component.BaseHandler;
import com.home77.common.base.component.ContextManager;

public class Toast {

  private static android.widget.Toast sToast;

  private static class ShowToastRunnable implements Runnable {
    public ShowToastRunnable(int duration, CharSequence text) {
      mDuration = duration;
      mText = text;
    }

    public ShowToastRunnable(int duration, @StringRes int resId) {
      mDuration = duration;
      mText = ContextManager.resources().getText(resId);
    }

    public ShowToastRunnable(int duration, @StringRes int resId, Object... args) {
      mDuration = duration;
      mText = ContextManager.resources().getString(resId, args);
    }

    private final int mDuration;
    private final CharSequence mText;

    @Override
    public void run() {
      show(mDuration, false, mText);
    }
  }

  public static void showShortSafe(CharSequence text) {
    BaseHandler.post(new ShowToastRunnable(android.widget.Toast.LENGTH_SHORT, text));
  }


  public static void showShortSafe(@StringRes int resId) {
    BaseHandler.post(new ShowToastRunnable(android.widget.Toast.LENGTH_SHORT, resId));
  }

  public static void showShortSafe(@StringRes int resId, Object... args) {
    BaseHandler.post(new ShowToastRunnable(android.widget.Toast.LENGTH_SHORT, resId, args));
  }

  public static void showLongSafe(CharSequence text) {
    BaseHandler.post(new ShowToastRunnable(android.widget.Toast.LENGTH_LONG, text));
  }

  public static void showLongSafe(@StringRes int resId) {
    BaseHandler.post(new ShowToastRunnable(android.widget.Toast.LENGTH_LONG, resId));
  }


  public static void showLongSafe(@StringRes int resId, Object... args) {
    BaseHandler.post(new ShowToastRunnable(android.widget.Toast.LENGTH_LONG, resId, args));
  }

  public static void showShort(CharSequence text) {
    show(android.widget.Toast.LENGTH_SHORT, false, text);
  }

  public static void showShort(@StringRes int resId) {
    show(android.widget.Toast.LENGTH_SHORT, resId);
  }

  public static void showShort(@StringRes int resId, Object... args) {
    show(android.widget.Toast.LENGTH_SHORT, resId, args);
  }

  public static void showLong(CharSequence text) {
    show(android.widget.Toast.LENGTH_LONG, false, text);
  }

  public static void showLong(@StringRes int resId) {
    show(android.widget.Toast.LENGTH_LONG, resId);
  }

  public static void showLong(@StringRes int resId, Object... args) {
    show(android.widget.Toast.LENGTH_LONG, resId, args);
  }

  private static void show(int duration, @StringRes int resId) {
    show(duration, false, ContextManager.resources().getText(resId));
  }

  private static void show(int duration, @StringRes int resId, Object... args) {
    show(duration, false, ContextManager.resources().getString(resId, args));
  }

  public static void show(int duration, boolean cancelBefore, CharSequence text) {
    if (text == null || text.length() == 0) {
      return;
    }
    if (cancelBefore) {
      cancel();
    }
    if (sToast == null) {
      sToast = android.widget.Toast.makeText(ContextManager.appContext(), text, duration);
    } else {
      sToast.setText(text);
      sToast.setDuration(duration);
    }
    sToast.show();
  }

  private static void cancel() {
    if (sToast != null) {
      sToast.cancel();
      sToast = null;
    }
  }
}
