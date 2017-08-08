package com.home77.kake.business.user.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.home77.common.base.collection.Params;
import com.home77.kake.App;
import com.home77.kake.R;
import com.home77.kake.bs.BaseFragment;
import com.home77.kake.bs.CmdType;
import com.home77.kake.bs.MsgType;
import com.home77.kake.bs.ParamsKey;
import com.home77.kake.common.event.BroadCastEvent;
import com.home77.kake.common.event.BroadCastEventConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class LoginFragment extends BaseFragment {

  @BindView(R.id.user_name_edit_text)
  EditText userNameEditText;
  @BindView(R.id.psw_edit_text)
  EditText pswEditText;
  Unbinder unbinder;

  @Override
  public void executeCommand(CmdType cmdType, Params in, Params out) {
    switch (cmdType) {
      case VIEW_CREATE:
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_login, null, false);
        unbinder = ButterKnife.bind(this, view);
        out.put(ParamsKey.VIEW, view);
        break;
      case VIEW_DESTROY:
        unbinder.unbind();
        break;
      case LOGINING:
        App.eventBus().post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_SHOW, null));
        break;
      case LOGIN_SUCCESS:
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
        break;
      case LOGIN_ERROR:
        App.eventBus()
           .post(new BroadCastEvent(BroadCastEventConstant.DIALOG_LOADING_DISMISS, null));
        break;
    }
  }

  @OnClick({
      R.id.back_image_view, R.id.login_text_view, R.id.forget_psw_text_view,
      R.id.register_usr_text_view
  })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.back_image_view:
        presenter.onMessage(MsgType.CLICK_BACK, null);
        break;
      case R.id.login_text_view:
        presenter.onMessage(MsgType.CLICK_LOGIN,
                            Params.create(ParamsKey.PHONE_NUMBER,
                                          userNameEditText.getText().toString())
                                  .put(ParamsKey.PASSWORD, pswEditText.getText().toString()));
        break;
      case R.id.forget_psw_text_view:
        presenter.onMessage(MsgType.CLICK_FORGET_PASSWORD, null);
        break;
      case R.id.register_usr_text_view:
        presenter.onMessage(MsgType.CLICK_REGISTER_NOW, null);
        break;
    }
  }
}
