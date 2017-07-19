package com.home77.kake.business.user.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.home77.kake.R;
import com.home77.kake.base.BaseFragment;
import com.home77.kake.business.user.presenter.ForgetPasswordPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author CJ
 */
public class ForgetPasswordFragment extends BaseFragment<ForgetPasswordPresenter>
    implements ForgetPasswordView {

  @BindView(R.id.user_name_edit_text)
  EditText userNameEditText;
  @BindView(R.id.check_code_edit_text)
  EditText checkCodeEditText;
  @BindView(R.id.psw_edit_text)
  EditText pswEditText;
  Unbinder unbinder;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @OnClick({R.id.get_check_code_text_view, R.id.register_text_view, R.id.back_image_view})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.get_check_code_text_view:
        presenter.handleGetCheckCodeClick(userNameEditText.getText().toString());
        break;
      case R.id.register_text_view:
        presenter.handleRegisterClick(userNameEditText.getText().toString(),
                                      checkCodeEditText.getText().toString(),
                                      pswEditText.getText().toString());
        break;
      case R.id.back_image_view:
        presenter.handleBackClick();
        break;
    }
  }
}
