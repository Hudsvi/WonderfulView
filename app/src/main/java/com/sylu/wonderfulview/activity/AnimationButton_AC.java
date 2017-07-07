package com.sylu.wonderfulview.activity;

import android.os.Bundle;

import com.sylu.wonderfulview.BaseActivity;
import com.sylu.wonderfulview.R;
import com.sylu.wonderfulview.customview.AnimationButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hudsvi on 2017/7/7.
 */

public class AnimationButton_AC extends BaseActivity {
    @BindView(R.id.anim_view)
    AnimationButton animView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.buttonview;
    }

    @OnClick(R.id.anim_view)
    public void onViewClicked() {
        animView.start();
    }
}
