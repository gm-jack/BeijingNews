package com.gm.beijingnews.pagers;

import android.content.Context;
import android.widget.TextView;

import com.gm.beijingnews.base.BasePager;

/**
 * Created by Administrator on 2016/6/29.
 * 设置界面
 */
public class FivePager extends BasePager {
    public FivePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        tv_base.setText("FivePager");
        TextView tv = new TextView(context);
        tv.setText("FivePager");
        fl_base.addView(tv);
    }
}
