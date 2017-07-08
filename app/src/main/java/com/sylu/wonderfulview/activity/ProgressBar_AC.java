package com.sylu.wonderfulview.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sylu.wonderfulview.BaseActivity;
import com.sylu.wonderfulview.R;
import com.sylu.wonderfulview.customview.CircleProgressBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hudsvi on 2017/7/8.
 */

public class ProgressBar_AC extends BaseActivity {
    @BindView(R.id.circleProgressBarView)
    CircleProgressBarView circleProgressBarView;
    @BindView(R.id.et_progress_circle)
    EditText etProgressCircle;
    @BindView(R.id.btn_progress_ok)
    Button btnProgressOk;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.progressbarview;
    }


    @OnClick(R.id.btn_progress_ok)
    public void onViewClicked() {
        String pro=etProgressCircle.getText().toString();
        try {
            if(!pro.equals("")) {
                circleProgressBarView.setCurrentProgress(Float.valueOf(pro));
                circleProgressBarView.startAnimation();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (circleProgressBarView != null) {
            circleProgressBarView.stopAnimation();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (circleProgressBarView != null) {
            circleProgressBarView.pauseAnimation();
        }
    }
}
