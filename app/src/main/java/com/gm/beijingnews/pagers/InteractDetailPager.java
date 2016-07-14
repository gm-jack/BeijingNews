package com.gm.beijingnews.pagers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.gm.beijingnews.base.MenuDetailBaePager;

/**
 * Created by Administrator on 2016/6/29.
 * 互动详情界面
 */
public class InteractDetailPager extends MenuDetailBaePager {
    private TextView text;

    public InteractDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        text=new TextView(context);
        text.setTextSize(30);
        text.setTextColor(Color.BLUE);
        text.setGravity(Gravity.CENTER);
        return text;
    }

    @Override
    public void initData() {
        text.setText("互动详情");
    }
}
