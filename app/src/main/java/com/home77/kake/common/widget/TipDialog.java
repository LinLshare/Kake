package com.home77.kake.common.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.home77.kake.R;

/**
 * @author CJ
 */
public class TipDialog extends AlertDialog {

  public TipDialog(@NonNull Context context,
                   String tip,
                   OnButtonClickListener onButtonClickListener) {
    super(context);
    this.tip = tip;
    this.onButtonClickListener = onButtonClickListener;
    init();
  }

  private void init() {
    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tip, null);
    setView(dialogView);
    dialogView.findViewById(R.id.ok_text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onButtonClickListener.onClickOk();
      }
    });
    dialogView.findViewById(R.id.cancel_text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onButtonClickListener.onClickCancel();
      }
    });
    TextView tipTextView = (TextView) dialogView.findViewById(R.id.tip_text_view);
    tipTextView.setText(tip);
  }

  private final String tip;
  private OnButtonClickListener onButtonClickListener;

  public interface OnButtonClickListener {
    void onClickOk();

    void onClickCancel();
  }
}
