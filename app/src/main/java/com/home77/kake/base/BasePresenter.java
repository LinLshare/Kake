package com.home77.kake.base;

import com.home77.common.base.collection.Params;

/**
 * @author CJ
 */
public interface BasePresenter {
  void onMessage(MsgType msgType, Params params);
}
