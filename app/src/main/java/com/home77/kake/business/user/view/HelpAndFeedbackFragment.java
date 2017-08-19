package com.home77.kake.business.user.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.home77.common.base.collection.Params;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.base.CmdType;
import com.home77.kake.base.MsgType;
import com.home77.kake.base.ParamsKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class HelpAndFeedbackFragment extends BaseFragment {
  Unbinder unbinder;
  @BindView(R.id.feedback_edit)
  EditText feedbackEdit;

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View view =
            LayoutInflater.from(getContext()).inflate(R.layout.fragment_help_and_feedback, null);
        out.put(ParamsKey.VIEW, view);
        unbinder = ButterKnife.bind(this, view);
        break;
      case VIEW_DESTROY:
        unbinder.unbind();
        break;
    }
  }

  @OnClick({R.id.back_image_view, R.id.submit_text_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        presenter.onMessage(MsgType.CLICK_BACK, null);
        break;
      case R.id.submit_text_view:
        Toast.showShort("提交成功");
        feedbackEdit.setText("");
        break;
    }
  }
}
