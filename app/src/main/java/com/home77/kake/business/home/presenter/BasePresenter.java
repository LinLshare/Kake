package com.home77.kake.business.home.presenter;

import com.home77.common.base.collection.Params;

/**
 * @author CJ
 */
public interface BasePresenter extends FragmentLifeCycle {
  void onMessage(MsgType msgType, Params params);
}
