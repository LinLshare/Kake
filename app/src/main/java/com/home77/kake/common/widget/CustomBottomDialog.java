package com.home77.kake.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.home77.kake.R;

/**
 * @author CJ
 */
public class CustomBottomDialog extends Dialog {

  public CustomBottomDialog(@NonNull Context context) {
    super(context, R.style.Theme_Light_NoTitle_Dialog);
  }

  @Override
  public void show() {
    super.show();
    Window window = getWindow();
    window.setGravity(Gravity.BOTTOM);
    window.getDecorView().setPadding(0, 0, 0, 0);
    WindowManager.LayoutParams attributes = window.getAttributes();
    attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
    window.setAttributes(attributes);
  }

}
