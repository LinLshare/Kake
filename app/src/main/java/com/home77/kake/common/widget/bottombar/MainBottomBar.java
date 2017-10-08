/*
 * Copyright (c) 2016.  Admin All rights reserved.
 */

package com.home77.kake.common.widget.bottombar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.home77.common.ui.util.SizeHelper;
import com.home77.kake.R;

public class MainBottomBar extends LinearLayout implements View.OnClickListener {

  private int currentSelectedPosition = 0;
  private Animation shrinkAnimation;
  private OnTabItemClickListener onTabItemClickListener;

  public MainBottomBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    setBackgroundResource(R.color.colorC1);
    setOrientation(HORIZONTAL);
    setClipChildren(false);
    shrinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shrink);
  }

  public void addBottomBarItem(View bottomBarItem) {
    LinearLayout.LayoutParams layoutParams =
        new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
    layoutParams.weight = 1;

    if (bottomBarItem instanceof ImageBottomItem) {
      layoutParams.height = SizeHelper.dp(64);
      layoutParams.bottomMargin = SizeHelper.dp(6);
      layoutParams.gravity = Gravity.BOTTOM;
    }
    addView(bottomBarItem, layoutParams);
    requestLayout();
    bottomBarItem.setOnClickListener(this);
    if (getChildCount() == 1) { //默认选中第一项
      getChildAt(0).setSelected(true);
    }
  }

  public void selectBottomBarItem(int position) {
    if (currentSelectedPosition == position) {
      return;
    }
    for (int i = 0; i < getChildCount(); i++) {
      View child = getChildAt(i);
      if (position == i) {
        child.setSelected(true);
      } else {
        child.setSelected(false);
      }
    }
    currentSelectedPosition = position;
  }

  public int getBottomItemCount() {
    return getChildCount();
  }

  public boolean isImageBottomItem(int index) {
    if (getChildAt(index) instanceof ImageBottomItem) {
      return true;
    } else {
      return false;
    }
  }

  public void setOnTabItemClickListener(OnTabItemClickListener listener) {
    this.onTabItemClickListener = listener;
  }

  @Override
  public void onClick(View view) {
    int position = indexOfChild(view);
    if (onTabItemClickListener != null && currentSelectedPosition != position) {
      onTabItemClickListener.onTabItemClick(indexOfChild(view));
      IBottomItem bottomItem = (IBottomItem) view;
      bottomItem.shrink(shrinkAnimation);
    }
    if (position != 1) {
      selectBottomBarItem(position);
    }
  }

  public interface OnTabItemClickListener {
    void onTabItemClick(int index);
  }
}
