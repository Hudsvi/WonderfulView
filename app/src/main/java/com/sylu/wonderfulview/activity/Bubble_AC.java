package com.sylu.wonderfulview.activity;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sylu.wonderfulview.BaseActivity;
import com.sylu.wonderfulview.R;
import com.sylu.wonderfulview.sets.CircleSets;
import com.sylu.wonderfulview.utils.DisplayUtil;
import com.sylu.wonderfulview.customview.BubbleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Hudsvi on 2017/7/3.
 */

public class Bubble_AC extends BaseActivity {
    @BindView(R.id.bubbleview)
    BubbleView bubbleview;
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.tv_bubble)
    TextView tvBubble;
    private List<CircleSets> circleSetsList;

    @Override
    protected void initData() {
        circleSetsList = new ArrayList<>();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initPointF();
        bubbleview.setCircleSets(circleSetsList);
    }

    private void initPointF() {
        int height = DisplayUtil.getDisplayHight(this);
        int width = DisplayUtil.getDisplayWidth(this);

        int centerX = width / 2;
        int centerY = height / 2;


        Log.d(TAG, "initPoint: " + centerX + "----" + centerY);
        ImageView ima = new ImageView(this);
        PopupWindow up = new PopupWindow(ima, 12, 12);
        CircleSets circleBean = new CircleSets(
                new PointF((float) (-width / 5.1), (float) (height / 1.5)),
                new PointF(centerX - 30, (float) (height / 1.5)),
                new PointF((float) (width / 2.4), (float) (height / 3.4)),
                new PointF(width / 6, centerY - 120),
                new PointF((float) (width / 7.2), -height / 128),
                (float) (width / 14.4), 60);
        CircleSets circleBean2 = new CircleSets(
                new PointF(-width / 4, (float) (height / 1.3)),
                new PointF(centerX - 20, height * 3 / 5),
                new PointF((float) (width / 2.1), (float) (height / 2.5)),
                new PointF(width / 3, centerY - 10),
                new PointF(width / 4, (float) (-height / 5.3)),
                width / 4, 60);
        // (w,h)----->(90,1745),(440,1280),(318,955),(0,1055),(0,0),radius=45,alpha=60
        CircleSets circleBean3 = new CircleSets(
                new PointF(-width / 12, (float) (height / 1.1)),
                new PointF(centerX - 100, height * 2 / 3),
                new PointF((float) (width / 3.4), height / 2),
                new PointF(0, centerY + 100),
                new PointF(0, 0),
                width / 24, 60);

        CircleSets circleBean4 = new CircleSets(
                new PointF(-width / 9, (float) (height / 0.9)),
                new PointF(centerX, height * 3 / 4),
                new PointF((float) (width / 2.1), (float) (height / 2.3)),
                new PointF(width / 2, centerY),
                new PointF((float) (width / 1.5), (float) (-height / 5.6)),
                width / 4, 60);

        CircleSets circleBean5 = new CircleSets(
                new PointF((float) (width / 1.4), (float) (height / 0.9)),
                new PointF(centerX, height * 3 / 4),
                new PointF(width / 2, (float) (height / 2.37)),
                new PointF(width * 10 / 13, centerY - 20),
                new PointF(width / 2, (float) (-height / 7.1)),
                width / 4, 60);
        CircleSets circleBean6 = new CircleSets(
                new PointF((float) (width / 0.8), height),
                new PointF(centerX + 20, height * 2 / 3),
                new PointF((float) (width / 1.9), (float) (height / 2.3)),
                new PointF(width * 11 / 14, centerY + 10),

                new PointF((float) (width / 1.1), (float) (-height / 6.4)),
                (float) (width / 4), 60);
        CircleSets circleBean7 = new CircleSets(
                new PointF((float) (width / 0.9), (float) (height / 1.2)),
                new PointF(centerX + 20, height * 4 / 7),
                new PointF((float) (width / 1.6), (float) (height / 1.9)),
                new PointF(width, centerY + 10),

                new PointF(width, 0),
                (float) (width / 9.6), 60);

        circleSetsList.add(circleBean);
        circleSetsList.add(circleBean2);
        circleSetsList.add(circleBean3);
        circleSetsList.add(circleBean4);
        circleSetsList.add(circleBean5);
        circleSetsList.add(circleBean6);
        circleSetsList.add(circleBean7);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.bezierview;
    }


    @OnClick({R.id.btn_bubble,R.id.btn_bubble2,R.id.btn_bubble3})
    public void onViewClicked(View view) {
        bubbleview.setCenterImg(tvBubble);
        switch (view.getId()) {
            case R.id.btn_bubble:
                bubbleview.openAnimation(1);
                break;
            case R.id.btn_bubble2:
                bubbleview.openAnimation(2);
                break;
            case R.id.btn_bubble3:
                bubbleview.openAnimation(3);
        }    }


}
