package com.home77.kake.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.home77.common.ui.util.SizeHelper;
import com.home77.kake.R;

/**
 * @author CJ
 */
public class BottomDialog extends Dialog {

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

  public BottomDialog(@NonNull final Context context,
                      final String[] datas,
                      final OnItemClickListener listener) {
    super(context, R.style.Theme_Light_NoTitle_Dialog);

    ListView listView = new ListView(context);
    listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT));
    listView.setDivider(new ColorDrawable(getContext().getResources().getColor(R.color.colorC3)));
    listView.setDividerHeight(SizeHelper.dp(1));
    listView.setAdapter(new BaseAdapter() {
      @Override
      public int getCount() {
        return datas.length;
      }

      @Override
      public Object getItem(int position) {
        return datas[position];
      }

      @Override
      public long getItemId(int position) {
        return position;
      }

      @Override
      public View getView(final int position, View convertView, ViewGroup parent) {
        View layout =
            LayoutInflater.from(context).inflate(R.layout.item_bottom_dialog, parent, false);
        TextView textView = (TextView) layout.findViewById(R.id.text_view);
        final String data = datas[position];
        textView.setText(data);
        layout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            listener.onItemClick(position, data);
          }
        });

        return layout;
      }
    });
    setContentView(listView);
  }

  public interface OnItemClickListener {
    void onItemClick(int position, String data);
  }
}
