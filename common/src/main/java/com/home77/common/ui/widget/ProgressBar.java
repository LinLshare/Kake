package com.home77.common.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class ProgressBar extends View {
  // progress rect
  private static final int COUNT = 4;
  private static final int DURATION = 2000;

  public ProgressBar(Context context, int foreground, int background) {
    super(context);

    // color
    mForegroundColor = foreground;
    mBackgroundColor = background;

    //setup mPaint
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setColor(mForegroundColor);

    //setup circle drawer
    mRectDrawers = new ArrayList<>();
    for (int i = 0; i < COUNT; i++) {
      mRectDrawers.add(new RectDrawer());
    }
  }

  public ProgressBar(Context context) {
    this(context, Color.WHITE, Color.parseColor("#2196F3"));
  }

  private final int mForegroundColor;
  private final int mBackgroundColor;
  private final List<RectDrawer> mRectDrawers;
  private final Paint mPaint;

  private List<ObjectAnimator> mAnimators;
  boolean mIsStopped;

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    start();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    stop();
  }

  public void stop() {
    if (mAnimators == null) {
      return;
    }
    for (ObjectAnimator animator : mAnimators) {
      if (animator.isRunning()) {
        animator.cancel();
      }
    }
    mIsStopped = true;
  }

  public void start() {
    if (mAnimators != null) {
      return;
    }
    mIsStopped = false;
    if (getWidth() == 0 || getHeight() == 0) {
      return;
    }
    createAnimators();
  }


  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (!mIsStopped && mAnimators == null && getWidth() > 0 && getHeight() > 0) {
      createAnimators();
    }
    canvas.drawColor(mBackgroundColor);
    for (RectDrawer drawer : mRectDrawers) {
      drawer.draw(canvas, mPaint);
    }
  }

  private void createAnimators() {
    mAnimators = new ArrayList<>();
    for (int i = 0; i < mRectDrawers.size(); i++) {
      final int index = i;
      final RectDrawer drawer = mRectDrawers.get(i);
      postDelayed(new Runnable() {
        @Override
        public void run() {
          ObjectAnimator animator = ObjectAnimator.ofInt(drawer, "start", -getHeight(), getWidth());
          animator.setDuration(DURATION).setRepeatCount(ValueAnimator.INFINITE);
          animator.setInterpolator(new DecelerateInterpolator());
          animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
              if (index == 0) {
                invalidate();
              }
            }
          });
          animator.start();
          mAnimators.add(animator);
        }
      }, DURATION / mRectDrawers.size() * i);
    }
  }

  private class RectDrawer {
    private int mStart;

    public void setStart(int start) {
      mStart = start;
    }

    public void draw(Canvas canvas, Paint paint) {
      canvas.drawRect(mStart, 0, mStart + getHeight(), getHeight(), paint);
    }
  }
}

