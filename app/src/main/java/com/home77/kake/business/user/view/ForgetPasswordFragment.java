package com.home77.kake.business.user.view;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.home77.common.base.collection.Params;
import com.home77.common.base.event.GenericEvent;
import com.home77.common.ui.widget.Toast;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.bs.BaseFragment;
import com.home77.kake.bs.CmdType;
import com.home77.kake.bs.MsgType;
import com.home77.kake.bs.ParamsKey;
import com.home77.kake.business.user.UserActivity;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class ForgetPasswordFragment extends BaseFragment {

  @BindView(R.id.user_name_edit_text)
  EditText userNameEditText;
  @BindView(R.id.check_code_edit_text)
  EditText checkCodeEditText;
  @BindView(R.id.psw_edit_text)
  EditText pswEditText;
  Unbinder unbinder;
  @BindView(R.id.get_check_code_text_view)
  TextView getCheckCodeTextView;
  private CountDownTimer countDownTimer;

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View view = LayoutInflater.from(getContext())
                                  .inflate(R.layout.fragment_forget_password, null, false);
        unbinder = ButterKnife.bind(this, view);
        out.put(ParamsKey.VIEW, view);
        break;
      case VIEW_DESTROY:
        if (countDownTimer != null) {
          countDownTimer.cancel();
        }
        unbinder.unbind();
        break;
      case CHECK_CODE_COUNT_DOWN:
        int seconds = in.get(ParamsKey.CHECK_CODE_COUNT_DOWN, 1);
        getCheckCodeTextView.setEnabled(false);
        countDownTimer = new CountDownTimer(seconds * 1000, 1000) {
          @Override
          public void onTick(long millisUntilFinished) {
            int countDown = (int) (millisUntilFinished / 1000);
            getCheckCodeTextView.setText(countDown + " s");
          }

          @Override
          public void onFinish() {
            getCheckCodeTextView.setEnabled(true);
            getCheckCodeTextView.setText("短信获取");
          }
        }.start();
        break;
      case PASSWORD_RESETING:
        App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_SHOW, null));
        break;
      case PASSWORD_RESET_ERROR:
        if (countDownTimer != null) {
          countDownTimer.cancel();
          countDownTimer.onFinish();
        }
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
        break;
      case PASSWORD_RESET_SUCCESS:
        if (countDownTimer != null) {
          countDownTimer.cancel();
          countDownTimer.onFinish();
        }
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
        Toast.showShort("重置密码成功");
        App.eventBus().post(new GenericEvent(this, UserActivity.EVENT_TO_LOGIN));
        break;
    }
  }

  @OnClick({R.id.get_check_code_text_view, R.id.submit_text_view, R.id.back_image_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.get_check_code_text_view:
        presenter.onMessage(MsgType.CLICK_CHECK_CODE,
                            Params.create(ParamsKey.PHONE_NUMBER,
                                          userNameEditText.getText().toString()));
        break;
      case R.id.submit_text_view:
        presenter.onMessage(MsgType.CLICK_SUBMIT,
                            Params.create(ParamsKey.PHONE_NUMBER,
                                          userNameEditText.getText().toString())
                                  .put(ParamsKey.CHECK_CODE, checkCodeEditText.getText().toString())
                                  .put(ParamsKey.PASSWORD, pswEditText.getText().toString()));
        break;
      case R.id.back_image_view:
        presenter.onMessage(MsgType.CLICK_BACK, null);
        break;
    }
  }
}
