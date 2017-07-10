package com.sylu.wonderfulview.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sylu.wonderfulview.BaseActivity;
import com.sylu.wonderfulview.R;
import com.sylu.wonderfulview.customview.CircleProgressBarView;
import com.sylu.wonderfulview.customview.ProgressBarView;

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
    @BindView(R.id.progressview)
    ProgressBarView progressview;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        progressview.setProgressListener(new ProgressBarView.ProgressListener() {
            @Override
            public void onUpdating(int currentProgress) {

            }

            @Override
            public void onCompete() {
                Toast.makeText(ProgressBar_AC.this, "进度完成", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.progressbarview;
    }


    @OnClick(R.id.btn_progress_ok)
    public void onViewClicked() {
        String pro = etProgressCircle.getText().toString();
        try {
            if (!pro.equals("")) {
                circleProgressBarView.setCurrentProgress(Float.valueOf(pro));
                circleProgressBarView.startAnimation();
                progressview.setCurrentProgress(Float.valueOf(pro));
                progressview.startAnimation();

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
        if(progressview!=null){
            progressview.stopAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (circleProgressBarView != null) {
            circleProgressBarView.pauseAnimation();
        }
        if(progressview!=null){
            progressview.pauseAnimation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (circleProgressBarView != null) {
            circleProgressBarView.resumeAnimation();
        }
        if(progressview!=null){
            progressview.resumeAnimation();
        }
    }
}
