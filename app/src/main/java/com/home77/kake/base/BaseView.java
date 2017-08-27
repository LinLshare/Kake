package com.home77.kake.base;

import android.app.Activity;
import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.home77.common.base.collection.Params;

/**
 * @author CJ
 */
public interface BaseView {
  void onCommand(CmdType cmdType, Params in, Params out);

  Context context();

  Activity activity();
}
