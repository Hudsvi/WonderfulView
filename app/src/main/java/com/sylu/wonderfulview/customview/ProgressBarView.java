package com.sylu.wonderfulview.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.sylu.wonderfulview.R;

/**
 * Created by Hudsvi on 2017/7/8.
 */

public class ProgressBarView extends View {
    /**
     * 文本框矩阵
     */
    private RectF recf = new RectF();
    private Context mContext;
    /**
     * 进度条粗细
     */
    private int barThickness;
    /**
     * 文本框尺寸
     */
    private int textHeight;
    private int textWidth;
    /**
     * 文本框边框Stroke粗细
     */
    private int textTipStrokeWidth;
    /**
     * 三角形高度
     */
    private int triangleHeight;
    /**
     * 文本框圆角半径
     */
    private int textRoundRadius;
    /**
     * 字体大小
     */
    private int textSize;
    /**
     * 进度条的marginTop
     */
    private int barMarginTop;
    /**
     * view的默认高度
     */
    private int mViewHeight;
    private int mViewWidth;

    /**
     * 进度条背景画笔
     */
    private Paint barBgPaint;
    /**
     * 进度条画笔
     */
    private Paint barPaint;
    /**
     * 进度条背景
     */
    private int barBgColor = 0xe2e2de;
    /**
     * 进度条颜色
     */
    private int progressBarColor = 0x9d44dc;
    /**
     * 文本框背景
     */
    private int textBgColor = 0x9d44dc;
    /**
     * 文本框画笔
     */
    private Paint textBarPaint;
    /**
     * 文本画笔
     */
    private Paint textPaint;
    /**
     * 文本颜色
     */
    private int textColor = 0xffffff;
    /**
     * 回调事件
     */
    private ProgressListener progressListener;
    /**
     * 是否展示text进度
     */
    private boolean showText = true;
    private float currentProgress = 0;
    /**
     * 文本框的三角形
     */
    private Path trianglePath;
    private float lastProgress=0;
    private ValueAnimator animator;
    private long duration=1000;


    public ProgressBarView(Context context) {
        this(context, null);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initViewAttrs();
        getAttributeSet(context, attrs);
        initPaint();
    }

    private void getAttributeSet(Context context, AttributeSet attrs) {
        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.ProgressBarView);
        barThickness = ty.getDimensionPixelOffset(R.styleable.ProgressBarView_barThickness, barThickness);
        progressBarColor = ty.getColor(R.styleable.ProgressBarView_progressBarColor, progressBarColor);
        barBgColor = ty.getColor(R.styleable.ProgressBarView_bgBarColor, barBgColor);
        barMarginTop = ty.getDimensionPixelOffset(R.styleable.ProgressBarView_barMarginTop, barMarginTop);

        textColor = ty.getColor(R.styleable.ProgressBarView_textColor, textColor);
        textBgColor = ty.getColor(R.styleable.ProgressBarView_textBgColor, textBgColor);
        textWidth = ty.getDimensionPixelOffset(R.styleable.ProgressBarView_textWidth, textWidth);
        textHeight = ty.getDimensionPixelOffset(R.styleable.ProgressBarView_textHeight, textHeight);
        triangleHeight = ty.getDimensionPixelOffset(R.styleable.ProgressBarView_triangleHeight, triangleHeight);
        textSize = ty.getDimensionPixelOffset(R.styleable.ProgressBarView_textSize, textSize);
        showText = ty.getBoolean(R.styleable.ProgressBarView_showingText, showText);
        ty.recycle();
    }

    private void initPaint() {
        barBgPaint = getPaint(barThickness, barBgColor, Paint.Style.STROKE);
        textBarPaint = getPaint(textTipStrokeWidth, textBgColor, Paint.Style.FILL);

        barPaint = getPaint(barThickness, progressBarColor, Paint.Style.STROKE);

        textPaint();

    }

    private void textPaint() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(getPaddingLeft(),
                textHeight + barMarginTop+triangleHeight+barThickness/2,
                getWidth() - getPaddingRight()-getPaddingLeft(),
                textHeight + barMarginTop+triangleHeight+barThickness/2,
                barBgPaint);
            canvas.drawLine(getPaddingLeft(),
                    textHeight + barMarginTop+triangleHeight+barThickness/2,
                    currentProgress * (getWidth() - getPaddingRight()-getPaddingLeft()) / 100f,
                    textHeight + barMarginTop+triangleHeight+barThickness/2,
                    barPaint);
        if(currentProgress==100){
            if(progressListener!=null){
                progressListener.onCompete();//结束任务时回调
            }
        }
        if(currentProgress>=2) {
            drawTextBar(canvas);
            drawTriangle(canvas);
            drawDegreeText(canvas);
        }
    }

    private void drawDegreeText(Canvas canvas) {
        float x0 = currentProgress * (getWidth()  -
                getPaddingRight()) / 100f+textWidth/2-triangleHeight*2;
        float x0_1=x0+textWidth/2;
        if(x0_1>getWidth() - getPaddingRight()){
            x0=getWidth()-getPaddingRight()-textWidth/2;
        }
        Paint.FontMetrics font = textPaint.getFontMetrics();
        float baseline=textHeight/2-(font.bottom+font.top)/2;
        canvas.drawText(getRoundDegree(currentProgress)+"%",x0,baseline,textPaint);
    }

    private float getRoundDegree(float currentProgress) {
        return Math.round(currentProgress*10)/10.00f;
    }

    private void drawTriangle(Canvas canvas) {
        trianglePath = new Path();
        float x0=0;
        if(currentProgress<98) {
            x0 = currentProgress * (getWidth() - getPaddingRight()) / 100f;
        }else{
            x0 = 98 * (getWidth() - getPaddingRight()) / 100f;
        }
        float y0 = textHeight + triangleHeight;
        trianglePath.moveTo(x0, y0);
        trianglePath.lineTo(x0-triangleHeight,textHeight);
        trianglePath.lineTo(x0+2*triangleHeight,textHeight-triangleHeight/3);
        trianglePath.close();
        canvas.drawPath(trianglePath,textBarPaint);
    }

    private void drawTextBar(Canvas canvas) {
        float x0 = currentProgress * (getWidth()  - getPaddingRight()) / 100f-2*triangleHeight;
        float x0_1=x0+textWidth;
        if(x0_1>=getWidth() - getPaddingRight()){
            recf.set(getWidth()-getPaddingRight()-textWidth,
                    0,
                    getWidth()-getPaddingRight(),
                    textHeight);
        }
        else recf.set(x0,
                0,
                x0 + textWidth,
                textHeight);
        canvas.drawRoundRect(recf, textRoundRadius, textRoundRadius, textBarPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(getMeassuredWidth(widthMode, width)
                , getMeassuredHeight(heightMode, height));
    }
/**
 * 此处设置的height作用不是很大，至于为什么，读者可根据前面的代码自行设计符合自己要求的高度值*/
    private int getMeassuredHeight(int heightMode, int height) {
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                mViewHeight = Math.max(mViewHeight, height);
                break;
        }
        return mViewHeight;
    }

    private int getMeassuredWidth(int widthMode, int width) {
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                mViewWidth = Math.max(mViewWidth, width);
                break;
        }
        return mViewWidth;
    }

    private Paint getPaint(int strokeWidth, int color, Paint.Style style) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setStrokeWidth(strokeWidth);
        p.setStyle(style);
        p.setColor(color);
        return p;
    }

    /**
     * 默认View各个属性的初始值
     */
    private void initViewAttrs() {
        barThickness = dp2px(4);
        textHeight = dp2px(15);
        textWidth = dp2px(30);
        textTipStrokeWidth = dp2px(2);
        triangleHeight = dp2px(3);
        textRoundRadius = dp2px(2);
        textSize = sp2px(8);
        barMarginTop = dp2px(8);
        this.mViewHeight = barThickness + textHeight + textTipStrokeWidth * 2 + triangleHeight + barMarginTop;
        this.mViewWidth = dp2px(200);
    }

    private int sp2px(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, i,
                getResources().getDisplayMetrics());
    }

    private int dp2px(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i,
                getResources().getDisplayMetrics());
    }

    public interface ProgressListener {
        void onUpdating(int currentProgress);

        void onCompete();
    }

    public void setProgressListener(ProgressListener listener) {

        this.progressListener = listener;
    }
    public float getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(float progress) {
        if(progress<=0||progress>100){
            currentProgress = 0;
        }
        else{
            lastProgress=currentProgress;
            currentProgress=progress;
            if(lastProgress>currentProgress){
                lastProgress=0;
            }
            initAnimation();
        }

    }

    private void initAnimation() {
        long time= (long) (duration*(currentProgress-lastProgress)/100);
        animator = ObjectAnimator.ofFloat(lastProgress, currentProgress);
        animator.setDuration(time);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float frac= (float) animation.getAnimatedValue();
                currentProgress=frac;
                invalidate();
            }
        });
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
    public void resumeAnimation(){
        if (animator != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.resume();
            }

    }
    public void stopAnimation() {
        if (animator != null)
            animator.cancel();
    }
}

