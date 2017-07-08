package com.sylu.wonderfulview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.sylu.wonderfulview.activity.AnimationButton_AC;
import com.sylu.wonderfulview.activity.Bubble_AC;
import com.sylu.wonderfulview.activity.ProgressBar_AC;
import com.sylu.wonderfulview.activity.Wave_Bezier_AC;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private Button btn;
    private ImageView img;
    private PopupWindow p;


    @Override
    protected void initData() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }



    @OnClick({R.id.bubble,R.id.wave_bezier,R.id.btn_view,R.id.progress})
    public void onViewClicked(View v ) {
        switch (v.getId()) {
            case R.id.bubble:
                startAct(Bubble_AC.class);
                break;
            case R.id.wave_bezier:
                startAct(Wave_Bezier_AC.class);
                break;
            case R.id.btn_view:
                startAct(AnimationButton_AC.class);
                break;
            case R.id.progress:
                startAct(ProgressBar_AC.class);
        }
    }
    private void startAct(Class baseMapClass) {
        startActivity(new Intent(MainActivity.this,baseMapClass));
    }
}
