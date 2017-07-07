package com.sylu.wonderfulview.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sylu.wonderfulview.R;

/**
 * Created by Hudsvi on 2017/7/4.
 */

public class WaveBezierView extends View {
    private final int amplitute;
    private final int waveWidth;
    private int height;
    private int width;
    private int waveCount;
    private Paint paint;
    private Path path;
    private ValueAnimator vAnimator;
    private int offSet;

    public WaveBezierView(Context context) {
        this(context, null);
    }

    public WaveBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.amplitute = dp2px(10);
        this.waveWidth = dp2px(500);
        init();
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(getResources().getColor(R.color.alpha_blue2));
        paint.setAntiAlias(true);

    }
/**
 * 其本质是dp * density这种方式转换,
 * 当然，TypedValue.applyDimension该方法实现了很多常用单位之间的转换，方便我们直接调用
 * **/
    private int dp2px(float i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                i, getResources().getDisplayMetrics());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.waveCount = (int) Math.round(width / waveWidth + 1.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSinWave(canvas);
    }

    private void drawSinWave(Canvas canvas) {
        path.reset(); //TODO：调用reset()方法十分重要,一定不要忘记,否则会重复绘制
        path.moveTo(-waveWidth + offSet, amplitute);
        for (int i = 0; i < waveCount; i++) {
            path.quadTo(-3 * waveWidth / 4 + offSet + i * waveWidth, -amplitute,
                    -waveWidth / 2 + offSet + i * waveWidth, amplitute);
            path.quadTo(-waveWidth / 4 + offSet + i * waveWidth, 3 * amplitute, offSet + i * waveWidth, amplitute);
        }
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.close();
        canvas.drawPath(path, paint);
    }

    public void startAnimation() {
        initAnimation();
    }

    public void stopAnimation() {
        if (vAnimator != null) {
            vAnimator.cancel();
        }
    }

    public void pauseAnimation() {
        if (vAnimator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                vAnimator.pause();
            }

        }
    }

    private void initAnimation() {
        vAnimator = ObjectAnimator.ofInt(0, waveWidth);
        vAnimator.setDuration(2000);
        vAnimator.setStartDelay(300);
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setInterpolator(new LinearInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offSet = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        vAnimator.start();
    }

    public void resumeAnimation() {
        if (vAnimator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                vAnimator.resume();
            }
        }
    }
}
