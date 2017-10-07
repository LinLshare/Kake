package com.home77.kake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.jpush.android.api.JPushInterface;

/**
 * @author CJ
 */
public class PushReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    switch (action) {
      case JPushInterface.ACTION_NOTIFICATION_OPENED:
        break;
    }
  }
}
