package com.home77.common.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.home77.common.ui.util.DrawableHelper;
import com.home77.common.ui.util.SizeHelper;

public class LoadingLayout extends LinearLayout {

  private Context context;
  private ImageView loadingImageView;
  private RotateAnimation rotateAnimation;

  public LoadingLayout(Context context) {
    super(context);
    this.context = context;
    init();
    start();
  }

  private void start() {
    loadingImageView.startAnimation(rotateAnimation);
  }

  @Override
  public void onViewRemoved(View child) {
    super.onViewRemoved(child);
    if (rotateAnimation != null) {
      rotateAnimation.cancel();
    }
  }

  private void init() {
    // for top view
    {
      int bgRadius = SizeHelper.dp(8);
      setBackgroundDrawable(DrawableHelper.generateRoundRectShapeDrawable(0xee2e2e2e, bgRadius));
      setOrientation(VERTICAL);
      int padding = SizeHelper.dp(16);
      setPadding(padding, padding, padding, padding);
    }

    // loading image
    {
      loadingImageView = new ImageView(context);
      final int startAngle = -145;
      final int sweepAngle = 200;
      final int dp4 = SizeHelper.dp(4);
      final int widthAndHeight = SizeHelper.dp(48);
      final RectF rect = new RectF(dp4, dp4, widthAndHeight - dp4, widthAndHeight - dp4);

      ArcShape arcShape = new ArcShape(startAngle, sweepAngle) {
        @Override
        public void draw(Canvas canvas, Paint paint) {
          canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
        }
      };
      ShapeDrawable shapeDrawable = new ShapeDrawable(arcShape);
      Paint paint = shapeDrawable.getPaint();
      paint.setColor(Color.WHITE);
      paint.setStyle(Paint.Style.STROKE);
      paint.setAntiAlias(true);
      paint.setStrokeWidth(dp4);
      loadingImageView.setBackgroundDrawable(shapeDrawable);

      LayoutParams imageViewLp = new LayoutParams(widthAndHeight, widthAndHeight);
      imageViewLp.gravity = Gravity.CENTER;
      this.addView(loadingImageView, imageViewLp);

      rotateAnimation = new RotateAnimation(0f,
                                            720f,
                                            Animation.RELATIVE_TO_SELF,
                                            0.5f,
                                            Animation.RELATIVE_TO_SELF,
                                            0.5f);
      rotateAnimation.setDuration(1200);
      rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
      rotateAnimation.setRepeatMode(Animation.RESTART);
      rotateAnimation.setRepeatCount(Animation.INFINITE);
    }
  }
}
