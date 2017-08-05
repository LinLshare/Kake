package com.home77.common.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.home77.common.ui.util.SizeHelper;

public class LoadingDialog extends Dialog {
  private Context context;
  private Window dialogWindow;
  private int widthAndHeight;

  public LoadingDialog(Context context) {
    super(context);
    this.context = context;
    init();
  }

  private void init() {
    setContentView(new LoadingLayout(context));
    dialogWindow = getWindow();
    dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    widthAndHeight = SizeHelper.dp(120);
    setCancelable(true);
  }

  @Override
  public void show() {
    super.show();
    WindowManager.LayoutParams attributes = dialogWindow.getAttributes();
    attributes.width = widthAndHeight;
    dialogWindow.setAttributes(attributes);
  }

  @Override
  public void hide() {
    super.hide();
  }
}
