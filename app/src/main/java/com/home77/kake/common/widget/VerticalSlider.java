package com.home77.kake.common.widget;

import android.content.Context;
import android.graphics.Rect;
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

  private void setPosition(float position) {
    if (position < slider.getHeight() / 2 || position > getHeight() - slider.getHeight() / 2) {
      return;
    }
    int sliderY = slider.getTop() + slider.getHeight() / 2;
    slider.setTranslationY(position - sliderY);
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
      layoutParams.gravity = Gravity.CENTER;
      addView(slider, layoutParams);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        Rect r = new Rect();
        slider.getGlobalVisibleRect(r);
        return r.contains((int) event.getRawX(), (int) event.getRawY());
      case MotionEvent.ACTION_MOVE:
        float position = event.getY();
        setPosition(position);
        onSliderListener.onSliderChanged(position / getHeight());
        return super.onTouchEvent(event);
    }
    return super.onTouchEvent(event);
  }

  private OnSliderListener onSliderListener;

  public void setOnSliderListener(OnSliderListener onSliderListener) {
    this.onSliderListener = onSliderListener;
  }

  public interface OnSliderListener {
    void onSliderChanged(float position);
  }
}
