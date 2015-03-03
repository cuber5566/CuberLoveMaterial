package com.cuber.cuberlovematerial.views;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.cuber.cuberlovematerial.R;

/**
 * Created by cuber on 2015/3/2.
 */
public class FloatingActionButton extends ImageView {

    final int ANIMATION_DURATION = 200;

    Paint backgroundPaint;
    RectF rectF;
    int height, width;
    float padding = 4;
    boolean isClicked;

    int shadowColor = Color.BLACK;
    final float SHADOW_RADIUS = 6.0f;
    final float SHADOW_OFFSET_X = 0.0f;
    final float SHADOW_OFFSET_Y = 4.0f;

    int backgroundColor = Color.BLACK;

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        padding = getResources().getDisplayMetrics().density * padding;
        setAttributes(context, attrs);
        setPaint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        rectF = new RectF();
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        Resources.Theme theme = context.getTheme();
        if (theme != null) {
            TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, 0, 0);
            if (typedArray != null) {

                int n = typedArray.getIndexCount();

                for (int i = 0; i < n; i++) {
                    int attr = typedArray.getIndex(i);

                    switch (attr) {
                        case R.styleable.FloatingActionButton_button_float_action_background_color:
                            backgroundColor = typedArray.getColor(i, 0xFFFFFFFF);
                            break;
                    }
                }
                typedArray.recycle();
            }
        }
    }

    private void setPaint() {

        backgroundPaint = new Paint() {
            {
                setAntiAlias(true);
                setStyle(Style.FILL);
                setShadowLayer(SHADOW_RADIUS, SHADOW_OFFSET_X, SHADOW_OFFSET_Y, Color.argb((int) (255 * 0.7), Color.red(shadowColor), Color.green(shadowColor), Color.blue(shadowColor)));
                setColor(backgroundColor);
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {

        height = canvas.getHeight();
        width = canvas.getWidth();
        rectF.set(0 + padding, 0 + padding, width - padding, height - padding);

        canvas.drawCircle(width / 2, height / 2, rectF.width() / 2, backgroundPaint);

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || isClicked)
            return false;

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                changeBackgroundColor(true);
                break;
            case MotionEvent.ACTION_MOVE:

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                isClicked = true;
                changeBackgroundColor(false);

                if (0 < x && x < width && 0 < y && y < height) {
                    postDelayed(clickRunnable, ANIMATION_DURATION);
                } else {
                    isClicked = false;
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
                backgroundPaint.setShadowLayer(SHADOW_RADIUS + Math.abs(v - 1.0f) * 10, SHADOW_OFFSET_X, SHADOW_OFFSET_Y, Color.argb((int) (255 * Math.abs(v - 1.75f)), Color.red(shadowColor), Color.green(shadowColor), Color.blue(shadowColor)));
                invalidate();
            }
        });
        colorAnimation.setInterpolator(new DecelerateInterpolator());
        colorAnimation.setDuration(ANIMATION_DURATION);
        colorAnimation.start();
    }

}
