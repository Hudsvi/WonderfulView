package com.sylu.wonderfulview.customview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import com.sylu.wonderfulview.R;
import com.sylu.wonderfulview.sets.CircleSets;
import com.sylu.wonderfulview.utils.BezierFormula;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hudsvi on 2017/7/3.
 */

public class BubbleView extends View {
    private Paint paint;
    private int[] colors = new int[2];
    private float[] pos = new float[2];
    private View cview;
    private List<CircleSets> circleSets;
    private ValueAnimator animater;
    private AnimatorSet animatorSet;
    private final List<PathMeasure> pmList = new ArrayList<>();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animatorSet.isStarted()) {
            if (circleSets != null) {
                for (CircleSets c : circleSets) {
                    paint.setShader(getShader(c));
                    paint.setAlpha(c.getAlpha());
                    canvas.drawCircle(c.getP().x, c.getP().y, c.getRadius(), paint);
                }
            }
        }
    }

    public BubbleView(Context context) {
        super(context);
        init();

    }

    public BubbleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        animatorSet = new AnimatorSet();
        initPaint();
        initShader();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAlpha(80);
        paint.setAntiAlias(true);
//        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
    }

    /**
     *
     **/
    private void initShader() {
        colors[0] = getResources().getColor(R.color.purple1);
        colors[1] = getResources().getColor(R.color.purple1);
        pos[0] = 0;//pos介于0～1之间，可设置各个color的相对位置
        pos[1] = 1;

    }

    /**
     * 获取圆的渐变色
     *
     * @param cir 传入每个圆的Sets对象
     * @return 返回Shader
     */
    private Shader getShader(CircleSets cir) {
        float x0 = cir.getP0().x - cir.getRadius();
        float x1 = cir.getP1().x + cir.getRadius();
        float y0 = cir.getP0().y;
        float y1 = cir.getP1().y;
        Shader s = new LinearGradient(x0, y0, x1, y1, colors, pos, Shader.TileMode.MIRROR);
        return s;
    }

    public void setCenterImg(View view) {
        this.cview = view;
    }

    public View getCenterview() {
        return cview;
    }

    public List<CircleSets> getCircleSets() {
        return circleSets;
    }

    public void setCircleSets(List<CircleSets> sets) {
        this.circleSets = sets;
    }

    /**
     * 通过属性动画集AnimatorSet对象来管理各个view的动画效果
     * **/
    public void openAnimation(int type) {
        if (!animatorSet.isRunning()) {
            switch (type) {
                case 1:
                    animatorSet.play(inAnima());
                    animatorSet.start();
                    break;
                case 2:
                    animatorSet.play(inAnima()).before(floatAnim());
                    animatorSet.start();
                    break;
                case 3:
                    animatorSet.play(floatAnim()).after(inAnima()).before(outAnim());
                    animatorSet.start();
                    break;
            }

        }
    }
/**
 * 同inAnim()
 * **/
    private Animator outAnim() {
//        ValueAnimator animator=ObjectAnimator.ofFloat(0f,1f,1f,0.8f,0.8f,1f,1f,0.95f,0.95f,1f);
        ValueAnimator animator=ObjectAnimator.ofFloat(0f,1f);
        animator.setDuration(3000);
        animator.setInterpolator(new BounceInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setCenterViewAlpha(0);
                float t = (float) animation.getAnimatedValue();
                for (int i = 0; i < circleSets.size(); i++) {
                    CircleSets c = circleSets.get(i);
                    PointF pf = BezierFormula.QuadraticBezier(t, c.getP2(), c.getP3(), c.getP4());
                    c.setP(pf);
                    c.setAlpha((int) ((1 - t) * 100));
                }
                invalidate();
                if (t == 1) {
                    if (onBubbleAnimationListener != null) {
                        onBubbleAnimationListener.onCompletedAnimationListener();
                    }
                }
            }
        });
        return animator;
    }
/**
 * 通过二次贝塞尔曲线设置圆心的运动轨迹
 *
 * **/
    private Animator inAnima() {
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 0.9f, 0.4f, 1f);    //ofFloat方法中的参数代表的是曲线的比例
        animator.setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float t = (float) animation.getAnimatedValue();
                if (circleSets != null) {
                    for (CircleSets c : circleSets) {
                        PointF pf = BezierFormula.QuadraticBezier(t, c.getP0(), c.getP1(), c.getP2());
                        c.setP(pf);
                        c.setAlpha((int) (t * 120));
                        if (t > 0.2) {
                            setCenterViewAlpha(t);
                        } else {
                            setCenterViewAlpha(0);
                        }
                    }
                    invalidate();
                }
            }
        });

        return animator;
    }

    private void setCenterViewAlpha(float t) {
        if (cview != null) {
            cview.setAlpha(t);
        }
    }

/**
 * ofFloat()方法的使用，
 *并定义Path对象，设定其轨迹为圆形，并指定绘制方向；
 * 将path对象传入PathMessure中，通过getPosTan()方法获取切点和相应点的坐标
 * 最后将计算的圆心轨迹坐标添加到CircleSets中
 * **/
    private Animator floatAnim() {
        final float amplitude = 50;
        final float[] pos = new float[2];
        final float[] tan = new float[2];
        for (int i = 0; i < circleSets.size(); i++) {
            CircleSets c = circleSets.get(i);
            Path p = new Path();
            if (i % 2 == 0) {
                p.addCircle(c.getP2().x, c.getP2().y, amplitude+10*i, Path.Direction.CCW);
            } else{
                p.addCircle(c.getP2().x, c.getP2().y, amplitude+10*i , Path.Direction.CW);
            }
            PathMeasure pm = new PathMeasure();
            pm.setPath(p, true);
            pmList.add(pm);
        }
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 1);
        animator.setDuration(5000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float t = (float) animation.getAnimatedValue();
                for (int i = 0; i < circleSets.size(); i++) {
                    pmList.get(i).getPosTan(pmList.get(i).getLength() * t, pos, tan);
                    circleSets.get(i).setP(new PointF(pos[0], pos[1]));
                }
                if (t > 0.25) {
                    setCenterViewAlpha((float) (1.25 - t));
                }
                invalidate();
            }
        });
        return animator;
    }

    public OnBubbleAnimationListener onBubbleAnimationListener;

    public void setOnBubbleAnimationListener(OnBubbleAnimationListener onBubbleAnimationListener) {
        this.onBubbleAnimationListener = onBubbleAnimationListener;
    }

    public abstract static class OnBubbleAnimationListener {

        public abstract void onCompletedAnimationListener();

    }
}
