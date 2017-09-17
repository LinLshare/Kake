package com.home77.common.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.home77.common.R;
import com.home77.common.ui.util.SizeHelper;

/**
 * @author CJ
 */
public class CommonLoadingDialog extends Dialog {
  private Context context;
  private Window dialogWindow;
  private int widthAndHeight;
  private TextView tipTextView;

  public CommonLoadingDialog(Context context) {
    super(context);
    this.context = context;
    init();
  }

  private void init() {
    setContentView(R.layout.layout_common_loading_dialog);
    tipTextView = (TextView) findViewById(R.id.tip_text_view);
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

  public void show(String tip) {
    tipTextView.setText(tip);
    show();
  }

  @Override
  public void hide() {
    super.hide();
  }
}
