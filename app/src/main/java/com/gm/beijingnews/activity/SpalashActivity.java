package com.gm.beijingnews.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.gm.beijingnews.R;
import com.gm.beijingnews.utils.DataConvert;

import cn.jpush.android.api.JPushInterface;

public class SpalashActivity extends AppCompatActivity {
    private RelativeLayout rl_spalash;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startActivity(new Intent(SpalashActivity.this, GuideActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(SpalashActivity.this, MainActivity.class));
                    break;
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash);
        rl_spalash = (RelativeLayout) findViewById(R.id.rl_spalash);
        ObjectAnimator animator = new ObjectAnimator();
        animator.ofFloat(rl_spalash, "rotationX", 0.0f, 360f).setDuration(3000).start();
        if (DataConvert.getData(this, DataConvert.GUIDE))
            handler.sendEmptyMessageDelayed(1, 3000);
        else
            handler.sendEmptyMessageDelayed(0, 3000);
    }



    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
