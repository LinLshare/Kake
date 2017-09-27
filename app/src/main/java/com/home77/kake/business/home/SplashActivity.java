package com.home77.kake.business.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.home77.kake.R;

public class SplashActivity extends AppCompatActivity {

  private CountDownTimer countDownTimer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                         WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
    setContentView(R.layout.activity_splash);
  }

  @Override
  protected void onResume() {
    super.onResume();
    countDownTimer = new CountDownTimer(1500, 1500) {
      @Override
      public void onTick(long millisUntilFinished) {
      }

      @Override
      public void onFinish() {
        SplashActivity.this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        SplashActivity.this.finish();
      }
    };
    countDownTimer.start();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (countDownTimer != null) {
      countDownTimer.cancel();
    }
  }
}
