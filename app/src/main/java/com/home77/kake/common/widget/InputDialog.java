package com.home77.kake.common.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.home77.kake.R;

/**
 * @author CJ
 */
public class InputDialog extends AlertDialog {

  private final EditText editText;

  public InputDialog(Context context, String title, final InputDialogListener inputDialogListener) {
    super(context);
    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input, null);
    setView(dialogView);

    TextView titleTextView = (TextView) dialogView.findViewById(R.id.title_text_view);
    titleTextView.setText(title);

    editText = (EditText) dialogView.findViewById(R.id.edit_text);
    dialogView.findViewById(R.id.ok_text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        inputDialogListener.onClickOk(editText.getText().toString());
      }
    });
    dialogView.findViewById(R.id.cancel_text_view).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        inputDialogListener.onClickCancel();
      }
    });
  }

  public interface InputDialogListener {
    void onClickOk(String input);

    void onClickCancel();
  }
}
