package com.sylu.wonderfulview.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.sylu.wonderfulview.R;

/**
 * Created by Hudsvi on 2017/7/8.
 */

public class CircleProgressBarView extends View {
    private RectF recF = new RectF();
    private int defaultBarStrokeWidth = 10;
    private Context mContext;
    private int circleBgColor = 0xe2e2de;
    private int barStrokeWidth = defaultBarStrokeWidth;
    private int progressBgColor = 0xFFf66b12;
    private int duration = 1000;
    private int centerTextColor = 0xFFf66b12;
    private int centerTextSize = 16;
    private boolean isCenterEnabled = true;
    private Paint circlePaint;
    private Paint progressPaint;
    private Paint textPaint;
    private int center_Y;
    private int center_X;
    private int radius;
    private float progressLength = 100;
    private float currentProgress = 0;
    private float lastProgress = 0;
    private ValueAnimator animator;
    private Path circlePath;
    private PathMeasure pathMessure;
    private DashPathEffect pathEffect;

    public CircleProgressBarView(Context context) {
        this(context, null);
    }

    public CircleProgressBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        getAttrs(attrs);
        initPaint();
    }

    private void initCirclePath() {
        /**
         * 这里一定不能回到原点，即不能扫过360度，否则开始角度90/-90不会生效*/
//        circlePath.addArc(recF, 90f, 359.9f);
        circlePath.addArc(recF,-90f,359.9f);
        pathMessure = new PathMeasure();
        pathMessure.setPath(circlePath, true);
    }

    private void initPaint() {
        circlePath = new Path();
        circlePaint = getPaint(circleBgColor);
        progressPaint = getPaint(progressBgColor);

        textPaint = new Paint();
        textPaint.setColor(centerTextColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(centerTextSize);
    }

    private Paint getPaint(int bgColor) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(bgColor);
        p.setAntiAlias(true);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(barStrokeWidth);
        p.setStyle(Paint.Style.STROKE);
        return p;
    }

    public float getProgressLength() {
        return progressLength;
    }

    /**
     * 默认总的进度长为100
     */
    public void setProgressLength(int progress) {
        if (progress < 0 || progress > progressLength) {
        } else {
            this.progressLength = progress;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(center_X, center_Y, radius, circlePaint);
//        canvas.drawArc(recF, 90, (float) (currentProgress * 3.6), false, progressPaint);
        if(currentProgress!=0){
            canvas.drawPath(circlePath, progressPaint);
        }
        if (isCenterEnabled) {
            drawCenterText(canvas, "当前进度:" + getHandledProgress() + "%");
        }
    }
/**
 * 保留小数点后两位*/
    private float getHandledProgress() {
        return (float) (Math.round(currentProgress*100)/100.00);
    }

    private void clearProgressBar() {

    }

    public float getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(float cProgress) {
        if (cProgress <= 0 || cProgress > progressLength) {
            currentProgress=0;
        } else {
            lastProgress = currentProgress;
            currentProgress = cProgress;
            if (lastProgress > currentProgress) {
                lastProgress = 0;
            }
            initTrackingAnimation();
        }
    }

    /**
     * 在原来的基础上继续绘制
     */
    private void initTrackingAnimation() {
        animator = ObjectAnimator.ofFloat(lastProgress / progressLength, currentProgress / progressLength);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float frac = (float) animation.getAnimatedValue();
                pathEffect = new DashPathEffect(new float[]{pathMessure.getLength(), pathMessure.getLength()},
                        pathMessure.getLength() * (1-frac));
                progressPaint.setPathEffect(pathEffect);
                invalidate();
            }
        });

    }

    /**
     * 重新绘制进度
     */
    private void initResetAnimation() {
        animator = ObjectAnimator.ofFloat(0, currentProgress);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    private void drawCenterText(Canvas canvas, String text) {
        Paint.FontMetricsInt font = textPaint.getFontMetricsInt();
        int baseline = (int) ((recF.top + recF.bottom - font.top - font.bottom) / 2);
        canvas.drawText(text, recF.centerX(), baseline, textPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        center_X = w / 2;
        center_Y = h / 2;
        radius = (Math.min(w, h) - barStrokeWidth) / 2;
        recF.set(center_X - radius, center_Y - radius, center_X + radius, center_Y + radius);
        initCirclePath();

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray ay = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView);

        barStrokeWidth = ay.getDimensionPixelSize(R.styleable.CircleProgressBarView_strokeWidth, barStrokeWidth);
        circleBgColor = ay.getColor(R.styleable.CircleProgressBarView_circleBgColor, circleBgColor);
        progressBgColor = ay.getColor(R.styleable.CircleProgressBarView_progressBgColor, progressBgColor);
        duration = ay.getInteger(R.styleable.CircleProgressBarView_duration, duration);
        centerTextColor = ay.getColor(R.styleable.CircleProgressBarView_centerTextColor, centerTextColor);
        centerTextSize = ay.getDimensionPixelOffset(R.styleable.CircleProgressBarView_centerTextSize, sp2px(centerTextSize));
        isCenterEnabled = ay.getBoolean(R.styleable.CircleProgressBarView_isCenterEnabled, isCenterEnabled);
        ay.recycle();
    }

    private int sp2px(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, getResources().getDisplayMetrics());
    }

    public void startAnimation() {
        if (animator != null) {
            animator.start();
        }
    }

    public void pauseAnimation() {
        if (animator != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.pause();
            }
    }

    public void stopAnimation() {
        if (animator != null)
            animator.cancel();
    }
}
