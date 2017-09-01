package com.home77.kake.common.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.home77.common.ui.util.SizeHelper;
import com.home77.kake.R;
import com.home77.kake.common.utils.BrightnessTools;

/**
 * @author CJ
 */
public class VerticalSlider extends FrameLayout {

  private ImageView slider;

  public VerticalSlider(@NonNull Context context,
                        @Nullable AttributeSet attrs,
                        @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public VerticalSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs, 0);
    init();
  }

  public VerticalSlider(@NonNull Context context) {
    super(context, null);
    init();
  }

  public void setPostion(float position) {
    float y = Math.abs(getTop() - getBottom() - position * getHeight());
    ObjectAnimator.ofFloat(slider,
                           "translationY",
                           slider.getTranslationY(),
                           y - slider.getHeight() / 2).start();
  }

  private void init() {
    {
      ImageView bg = new ImageView(getContext());
      bg.setImageResource(R.drawable.cam_slider);
      bg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      addView(bg,
              new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                               ViewGroup.LayoutParams.MATCH_PARENT));
    }
    {
      slider = new ImageView(getContext());
      slider.setImageResource(R.drawable.cam_slider_bt);
      slider.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      int widthAndHeight = SizeHelper.dp(24);
      LayoutParams layoutParams = new LayoutParams(widthAndHeight, widthAndHeight);
      layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
      addView(slider, layoutParams);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    float y = event.getY();
    ObjectAnimator.ofFloat(slider,
                           "translationY",
                           slider.getTranslationY(),
                           y - slider.getHeight() / 2).start();
    if (onSliderListener != null) {
      float position = Math.abs(getBottom() - y - getTop()) / getHeight();
      onSliderListener.onPositionChanged(position);
      if (event.getAction() == MotionEvent.ACTION_UP) {
        onSliderListener.onConfirmed(position);
      }
    }

    return super.onTouchEvent(event);
  }

  private OnSliderListener onSliderListener;

  public void setOnSliderListener(OnSliderListener onSliderListener) {
    this.onSliderListener = onSliderListener;
  }

  public interface OnSliderListener {
    //[0,1]
    void onPositionChanged(float position);

    void onConfirmed(float position);
  }
}
