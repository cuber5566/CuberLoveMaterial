package com.cuber.cuberlovematerial.views;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

public class FlatButton extends Button {

    final int ANIMATION_DURATION_DISABLED = 500;
    final int ANIMATION_DURATION_FOCUS = 2500;
    final int ANIMATION_DURATION_PRESS = 300;

    float radius = 48;
    float cur_radius;
    float max_radius;
    Paint ripplePaint;

    float x, y;

    int height, width;

    int pressColor = 0xFFFF4081;
    int rippleColor = 0xFFFFFFFF;

    public FlatButton(Context context) {
        this(context, null);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        radius = getResources().getDisplayMetrics().density * radius;
        setPaint();
    }

    private void setPaint() {
        ripplePaint = new Paint() {
            {
                setAntiAlias(true);
            }
        };

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        changeTextColor(enabled);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                changeBackgroundColor(true);
                changeRippleColor(true);
                changeRippleRadius(true);
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                changeBackgroundColor(false);
                changeRippleColor(false);
                changeRippleRadius(false);

                performClick();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = canvas.getHeight();
        width = canvas.getWidth();
        canvas.drawCircle(x, y, cur_radius, ripplePaint);

    }

    private void changeTextColor(boolean enabled) {
        ValueAnimator colorAnimation;
        int textColor = getTextColors().getDefaultColor();
        int disabledTextColor = Color.argb((int) (255 * 0.3), Color.red(textColor), Color.green(textColor), Color.blue(textColor));
        int normalTextColor = Color.argb((int) (255 * 1.0), Color.red(textColor), Color.green(textColor), Color.blue(textColor));

        if (enabled) {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), normalTextColor, disabledTextColor);
        } else {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), disabledTextColor, normalTextColor);
        }

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setTextColor((Integer) valueAnimator.getAnimatedValue());

            }
        });
        colorAnimation.setDuration(ANIMATION_DURATION_DISABLED);
        colorAnimation.start();
    }

    private void changeBackgroundColor(boolean focus) {

        ValueAnimator colorAnimation;
        int pressBackgroundColor = Color.argb((int) (255 * 1.0), Color.red(pressColor), Color.green(pressColor), Color.blue(pressColor));
        int normalBackgroundColor = Color.argb((int) (255 * 0.0), Color.red(pressColor), Color.green(pressColor), Color.blue(pressColor));

        if (focus) {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), normalBackgroundColor, pressBackgroundColor);
        } else {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), pressBackgroundColor, normalBackgroundColor);
        }

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        colorAnimation.setInterpolator(new DecelerateInterpolator());
        colorAnimation.setDuration(ANIMATION_DURATION_DISABLED);
        colorAnimation.start();
    }

    private void changeRippleColor(final boolean visible) {

        ValueAnimator colorAnimation;
        final float max_x = x > width / 2 ? x : width - x;
        final float max_y = y > width / 2 ? y : width - y;

        int normalBackgroundColor = Color.argb((int) (255 * 0.0), Color.red(rippleColor), Color.green(rippleColor), Color.blue(rippleColor));
        int pressBackgroundColor = Color.argb((int) (255 * 0.3), Color.red(rippleColor), Color.green(rippleColor), Color.blue(rippleColor));

        if (visible) {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), normalBackgroundColor, pressBackgroundColor);
        } else {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), pressBackgroundColor, normalBackgroundColor);
        }

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int color = (Integer) valueAnimator.getAnimatedValue();
                ripplePaint.setColor(color);

                if (visible) {
                    cur_radius = radius * Color.alpha(color) / (255 * 0.3f);
                } else {

                    max_radius = (float) Math.sqrt(max_x * max_x + max_y * max_y);
                    float up = max_radius - radius;
                    cur_radius = radius + up * Math.abs((Color.alpha(color) / (255 * 0.3f)) - 1);
                }
                invalidate();
            }
        });
        colorAnimation.setInterpolator(new DecelerateInterpolator());
        colorAnimation.setDuration(ANIMATION_DURATION_PRESS);
        colorAnimation.start();

        changeRippleRadius(true);
    }

    ValueAnimator radiusAnimation;

    private void changeRippleRadius(final boolean start) {

        if (radiusAnimation != null)
            radiusAnimation.cancel();

        radiusAnimation = ValueAnimator.ofFloat(0, getResources().getDisplayMetrics().density * 4);
        radiusAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (start) {
                    cur_radius = radius + (Float) animation.getAnimatedValue();
                    invalidate();
                }
            }
        });

        radiusAnimation.setInterpolator(new CycleInterpolator(1));
        radiusAnimation.setRepeatCount(Integer.MAX_VALUE);
        radiusAnimation.setStartDelay(ANIMATION_DURATION_PRESS);
        radiusAnimation.setDuration(ANIMATION_DURATION_FOCUS);
        radiusAnimation.start();
    }
}
