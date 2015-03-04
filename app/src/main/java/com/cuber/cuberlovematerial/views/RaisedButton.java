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
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.cuber.cuberlovematerial.R;

public class RaisedButton extends Button {

    final int ANIMATION_DURATION_DISABLED = 500;
    final int ANIMATION_DURATION_FOCUS = 2500;
    final int ANIMATION_DURATION_PRESS = 250;
    final int ANIMATION_DURATION_UP = 400;

    int shadowColor = Color.BLACK;
    final float SHADOW_RADIUS = 6.0f;
    final float SHADOW_OFFSET_X = 0f;
    final float SHADOW_OFFSET_Y = 3f;

    float radius = 48;
    float cur_radius;
    float max_radius;
    Paint ripplePaint, backgroundPaint;

    float x, y;

    int height, width;

    int backgroundColor = Color.BLACK;
    int rippleColor = Color.BLACK;

    RectF rectF;
    float padding = 6;

    boolean isClicked = false;

    public RaisedButton(Context context) {
        this(context, null);
    }

    public RaisedButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RaisedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        radius = getResources().getDisplayMetrics().density * radius;

        rectF = new RectF();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        setAttributes(context, attrs);
        setGravity(Gravity.CENTER);
        setPaint();

    }

    private void setAttributes(Context context, AttributeSet attrs) {
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.RaisedButton, 0, 0);
            if (typedArray != null) {

                int n = typedArray.getIndexCount();

                for (int i = 0; i < n; i++) {
                    int attr = typedArray.getIndex(i);

                    switch (attr) {
                        case R.styleable.RaisedButton_button_raised_background_color:
                            backgroundColor = typedArray.getColor(i, 0xFFFFFFFF);
                            break;
                        case R.styleable.RaisedButton_button_raised_ripple_color:
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
                setStyle(Style.FILL);
                setShadowLayer(SHADOW_RADIUS, SHADOW_OFFSET_X, SHADOW_OFFSET_Y, Color.argb((int) (255 * 0.75), Color.red(shadowColor), Color.green(shadowColor), Color.blue(shadowColor)));
                setColor(backgroundColor);
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
        if (!isEnabled() || isClicked)
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
                isClicked = true;
                start_radius = cur_radius;
                changeBackgroundColor(false);


                if (0 < x && x < width && 0 < y && y < height) {
                    changeRippleColor(false);
                    changeRippleRadius(false);
                    postDelayed(clickRunnable, ANIMATION_DURATION_UP - 200);

                } else {

                    if (radiusAnimation != null)
                        radiusAnimation.cancel();
                    ripplePaint.setColor(Color.argb((int) (255 * 0.5), Color.red(rippleColor), Color.green(rippleColor), Color.blue(rippleColor)));
                    isClicked = false;
                    invalidate();
                }
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
        rectF.set(0 + padding, 0 + padding, width - padding, height - padding * 2);


        canvas.drawRoundRect(rectF, getResources().getDisplayMetrics().density * 2, getResources().getDisplayMetrics().density * 2, backgroundPaint);

        canvas.save();
        canvas.clipRect(rectF);
        canvas.drawCircle(x, y, cur_radius, ripplePaint);
        canvas.restore();

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

        if (focus) {
            colorAnimation = ValueAnimator.ofFloat(1.0f, 0.75f);
        } else {
            colorAnimation = ValueAnimator.ofFloat(0.75f, 1.0f);
        }

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float v = (float) valueAnimator.getAnimatedValue();
                float[] hsv = new float[3];
                Color.colorToHSV(backgroundColor, hsv);
                hsv[2] *= v;
                backgroundPaint.setColor(Color.HSVToColor(hsv));
                backgroundPaint.setShadowLayer(SHADOW_RADIUS + Math.abs(v - 1.0f) * 10, SHADOW_OFFSET_X, SHADOW_OFFSET_Y, Color.argb((int) (255 * Math.abs(v * 2 - 2.5)), Color.red(shadowColor), Color.green(shadowColor), Color.blue(shadowColor)));
                invalidate();
            }
        });
        colorAnimation.setInterpolator(new DecelerateInterpolator());
        colorAnimation.setDuration(ANIMATION_DURATION_DISABLED);
        colorAnimation.start();
    }

    float start_radius;

    private void changeRippleColor(final boolean visible) {

        final ValueAnimator colorAnimation;
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
