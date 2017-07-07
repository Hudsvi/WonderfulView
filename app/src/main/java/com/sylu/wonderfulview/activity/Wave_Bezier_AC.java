package com.sylu.wonderfulview.activity;

import android.os.Bundle;

import com.sylu.wonderfulview.BaseActivity;
import com.sylu.wonderfulview.R;
import com.sylu.wonderfulview.customview.WaveBezierView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hudsvi on 2017/7/4.
 */

public class Wave_Bezier_AC extends BaseActivity {
    @BindView(R.id.cusstom_wave_bezierview)
    WaveBezierView cusstomWaveBezierview;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        cusstomWaveBezierview.startAnimation();
        List<Object> lists = new ArrayList<>();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.wave_bezier;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cusstomWaveBezierview.stopAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cusstomWaveBezierview.pauseAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cusstomWaveBezierview.resumeAnimation();
    }
}
