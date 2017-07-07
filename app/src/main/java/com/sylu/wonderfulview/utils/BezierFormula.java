package com.sylu.wonderfulview.utils;

import android.graphics.PointF;

/**
 * Created by Hudsvi on 2017/7/3.
 */

public class BezierFormula {
    /**
     * 二阶贝塞尔曲线
     * B(t) = P0*(1-t)^2 + 2*t*(1-t)*p1+ p2*t^2
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点
     * @param p2 终止点
     * @return t对应的点
     */
    public static PointF QuadraticBezier(float t, PointF p0, PointF p1, PointF p2) {
        PointF pf = new PointF();
        float temp=1-t;
        pf.x=(float) (Math.pow(temp, 2) * p0.x + 2 * t * temp * p1.x + Math.pow(t, 2) * p2.x);
        pf.y=(float) (Math.pow(temp, 2) * p0.y + 2 * t * temp * p1.y + Math.pow(t, 2) * p2.y);
        return pf;
    }
    /**
     * 三阶贝塞尔曲线
     * B(t) = p0*(1-t)^3 + 3*p1*t*(1-t)^2+3*p2*t^2*(1-t)+p3*t^3,
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 终止点
     * @return t对应的点
     */
    public static PointF CubicFormula(float t,PointF p0,PointF p1,PointF p2,PointF p3){
        PointF pf = new PointF();
        float temp=1-t;
        pf.x= (float) (p0.x * Math.pow(temp, 3) + 3 * p1.x * t * Math.pow(temp, 2) + 3 * p2.x * Math.pow(t, 2) * temp + p3.x * Math.pow(t, 3));
        pf.y= (float) (p0.y * Math.pow(temp, 3) + 3 * p1.y * t * Math.pow(temp, 2) + 3 * p2.y * Math.pow(t, 2) * temp + p3.y * Math.pow(t, 3));
        return pf;
    }
}
