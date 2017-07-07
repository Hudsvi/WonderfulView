package com.sylu.wonderfulview.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.sylu.wonderfulview.R;

/**
 * Created by Hudsvi on 2017/7/7.
 */

public class AnimationButton extends View {
    private Context con;
    private RectF recF = new RectF();
    private Rect rec = new Rect();
    private Paint textPaint;
    private Paint confirmPaint;
    private Paint recPaint;
    private int width;
    private int view_center_left;
    private int height;
    private ValueAnimator firstAnim;
    private long time=1000;
    private int round_angle;
    private ValueAnimator secondAnim;
    private int center_left;
    private int text_alpha;
    private ValueAnimator thirdAnim;
    private Path confirmPath;
    private PathMeasure pathMessure;
    private boolean isShouldDrawConfirmPath = false;
    private DashPathEffect pathEffect;
    private AnimatorSet animatorSet = new AnimatorSet();

    public AnimationButton(Context context) {
        this(context,null);

    }

    public AnimationButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AnimationButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.con=context;
        initPaint();

    }

    private void initPaint() {
        confirmPath = new Path();

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40);
        textPaint.setColor(getResources().getColor(R.color.white));
        textPaint.setTextAlign(Paint.Align.CENTER);

        confirmPaint = new Paint();
        confirmPaint.setStrokeWidth(10);
        confirmPaint.setStyle(Paint.Style.STROKE);
        confirmPaint.setColor(getResources().getColor(R.color.white));
        confirmPaint.setAntiAlias(true);

        recPaint = new Paint();
        recPaint.setStrokeWidth(4);
        recPaint.setAntiAlias(true);
        recPaint.setStyle(Paint.Style.FILL);
        recPaint.setColor(getResources().getColor(R.color.purple1));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        view_center_left = (w - h) / 2;
        initConfirmPath();
        initAnimation();
    }

    private void initConfirmPath() {
        confirmPath.moveTo(view_center_left + 3*height / 8, height / 2);
        confirmPath.lineTo(view_center_left + height / 2, 3*height / 5);
        confirmPath.lineTo(view_center_left + height / 3 * 2, 2*height / 5 );

        pathMessure = new PathMeasure(confirmPath, true);

    }

    private void initAnimation() {
        initFirst();//按钮圆角动画
        initSecond();//按钮收缩及改变文字alpha
        initThird();//绘制完成后的勾型动画
        animatorSet.play(thirdAnim)
                .after(firstAnim)
                .after(secondAnim);
    }



    private void initThird() {
        thirdAnim = ObjectAnimator.ofFloat(1, 0);
        thirdAnim.setDuration(time);
        thirdAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                isShouldDrawConfirmPath = true;
                float frac = (float) animation.getAnimatedValue();
                pathEffect = new DashPathEffect(new float[]{pathMessure.getLength(), pathMessure.getLength()}, pathMessure.getLength() * frac);
                confirmPaint.setPathEffect(pathEffect);
                invalidate();
            }
        });

    }

    private void initSecond() {
        secondAnim = ObjectAnimator.ofInt(0, view_center_left);
        secondAnim.setDuration(time);
        secondAnim.setInterpolator(new LinearInterpolator());
        secondAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                center_left = (int) animation.getAnimatedValue();
                text_alpha = 255 - 255 * center_left / view_center_left;
                textPaint.setAlpha(text_alpha);
                invalidate();
            }
        });
    }

    private void initFirst() {
        firstAnim = ObjectAnimator.ofInt(0, height / 2);
        firstAnim.setDuration(time);
        firstAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                round_angle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawButton(canvas);
        drawText(canvas);
        if (isShouldDrawConfirmPath) {
            canvas.drawPath(confirmPath, confirmPaint);
        }
    }

    private void drawButton(Canvas c) {

        recF.left = center_left;
        recF.top = 0;
        recF.right = width - center_left;
        recF.bottom = height;
        c.drawRoundRect(recF, round_angle, round_angle, recPaint);

    }

    private void drawText(Canvas c) {
        rec.left = 0;
        rec.bottom = height;
        rec.top = 0;
        rec.right = width;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (rec.top + rec.bottom - fontMetrics.bottom - fontMetrics.top) / 2;
        c.drawText("完成订单", rec.centerX(), baseline, textPaint);
    }

    public void start() {
        animatorSet.start();
    }
}
