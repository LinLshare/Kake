package com.home77.kake.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.home77.kake.R;

public class ScrollConfigurableViewPager extends ViewPager {

    private Boolean isScrollable;

    public ScrollConfigurableViewPager(Context context) {
        super(context);
        init(context, null);
    }

    public ScrollConfigurableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable
                .ScrollConfigurableViewPager);
        isScrollable = typedArray.getBoolean(R.styleable.ScrollConfigurableViewPager_scrollable,
                true);
        typedArray.recycle();
    }
}
