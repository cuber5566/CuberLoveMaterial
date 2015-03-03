package com.cuber.cuberlovematerial.views;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.cuber.cuberlovematerial.R;

public class FlatButton extends Button {

    final int ANIMATION_DURATION_DISABLED = 500;
    final int ANIMATION_DURATION_FOCUS = 2500;
    final int ANIMATION_DURATION_PRESS = 250;
    final int ANIMATION_DURATION_UP = 400;

    float radius = 48;
    float cur_radius;
    float max_radius;
    Paint ripplePaint, backgroundPaint;

    float x, y;

    int height, width;

    int pressColor = 0xFFFFFFFF;
    int rippleColor = 0xFFFFFFFF;
    RectF rectF;

    boolean isClicked = false;

    int paddingTop = 16;
    int paddingBottom = 16;
    int paddingLeft = 32;
    int paddingRight = 32;

    public FlatButton(Context context) {
        this(context, null);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        radius = getResources().getDisplayMetrics().density * radius;
        paddingTop = (int) getResources().getDisplayMetrics().density * paddingTop;
        paddingBottom = (int) getResources().getDisplayMetrics().density * paddingBottom;
        paddingLeft = (int) getResources().getDisplayMetrics().density * paddingLeft;
        paddingRight = (int) getResources().getDisplayMetrics().density * paddingRight;
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        setAttributes(context, attrs);
        setGravity(Gravity.CENTER);
        setPaint();
        rectF =  new RectF();

    }

    private void setAttributes(Context context, AttributeSet attrs) {
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.FlatButton, 0, 0);
            if (typedArray != null) {

                int n = typedArray.getIndexCount();

                for (int i = 0; i < n; i++) {
                    int attr = typedArray.getIndex(i);

                    switch (attr) {
                        case R.styleable.FlatButton_button_flat_press_color:
                            pressColor = typedArray.getColor(i, 0xFFFFFFFF);
                            break;
                        case R.styleable.FlatButton_button_flat_ripple_color:
                            rippleColor = typedArray.getColor(i, 0xFFFFFFFF);
                            break;
                    }
                }
                typedArray.recycle();
            }
        }
    }

    private void setPaint() {
        ripplePaint = new Paint() {
            {
                setAntiAlias(true);
            }
        };

        backgroundPaint = new Paint() {
            {
                setAntiAlias(true);
            }
        };
        backgroundPaint.setColor(0x00000000);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        changeTextColor(enabled);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled())
            return false;

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
                start_radius = cur_radius;
                changeBackgroundColor(false);

                if (0 < x && x < width && 0 < y && y < height) {
                    changeRippleColor(false);
                    changeRippleRadius(false);
                    postDelayed(clickRunnable, ANIMATION_DURATION_UP - 200);

                } else {

                    if (radiusAnimation != null)
                        radiusAnimation.cancel();
                    ripplePaint.setColor(Color.argb((int) (255 * 0.0), Color.red(rippleColor), Color.green(rippleColor), Color.blue(rippleColor)));
                    isClicked = false;
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
    Runnable clickRunnable = new Runnable() {
        @Override
        public void run() {
            performClick();
            isClicked = false;
        }
    };
    @Override
    protected void onDraw(Canvas canvas) {

        height = canvas.getHeight();
        width = canvas.getWidth();
        rectF.set(0, 0, width, height);

        canvas.drawRoundRect(rectF, getResources().getDisplayMetrics().density * 2, getResources().getDisplayMetrics().density * 2, backgroundPaint);
        canvas.drawCircle(x, y, cur_radius, ripplePaint);

        super.onDraw(canvas);
    }

    private void changeTextColor(boolean enabled) {
        ValueAnimator colorAnimation;
        int textColor = getTextColors().getDefaultColor();
        int disabledTextColor = Color.argb((int) (255 * 0.3), Color.red(textColor), Color.green(textColor), Color.blue(textColor));
        int normalTextColor = Color.argb((int) (255 * 1.0), Color.red(textColor), Color.green(textColor), Color.blue(textColor));

        if (enabled) {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), disabledTextColor, normalTextColor);

        } else {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), normalTextColor, disabledTextColor);
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
//                setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
                backgroundPaint.setColor((Integer) valueAnimator.getAnimatedValue());
                invalidate();
            }
        });
        colorAnimation.setInterpolator(new DecelerateInterpolator());
        colorAnimation.setDuration(ANIMATION_DURATION_DISABLED);
        colorAnimation.start();
    }

    float start_radius;
    private void changeRippleColor(final boolean visible) {

        ValueAnimator colorAnimation;
        final float max_x = x > width / 2 ? x : width - x;
        final float max_y = y > width / 2 ? y : width - y;

        int normalBackgroundColor = Color.argb((int) (255 * 0.0), Color.red(rippleColor), Color.green(rippleColor), Color.blue(rippleColor));
        int pressBackgroundColor = Color.argb((int) (255 * 0.3), Color.red(rippleColor), Color.green(rippleColor), Color.blue(rippleColor));

        if (visible) {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), normalBackgroundColor, pressBackgroundColor);
            colorAnimation.setDuration(ANIMATION_DURATION_PRESS);
        } else {
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), pressBackgroundColor, normalBackgroundColor);
            colorAnimation.setDuration(ANIMATION_DURATION_UP);
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
                    cur_radius = start_radius + up * Math.abs((Color.alpha(color) / (255 * 0.3f)) - 1);

                }
                invalidate();
            }
        });
        colorAnimation.setInterpolator(new DecelerateInterpolator());
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
