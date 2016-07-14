package com.gm.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gm.beijingnews.R;

/**
 * Created by Administrator on 2016/6/29.
 * 主界面：首页、新闻、智慧、政要、设置界面的基类
 */
public class BasePager {
    protected Context context;
    public View rootView;
    public TextView tv_base;
    public ImageView iv_base;
    public ImageView iv_switch;
    public FrameLayout fl_base;
    public TextView tv_pay_edit;

    public BasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.basepager, null);
        tv_base = (TextView) view.findViewById(R.id.tv_base);
        iv_base = (ImageView) view.findViewById(R.id.iv_base);
        iv_switch = (ImageView) view.findViewById(R.id.iv_switch);
        tv_pay_edit = (TextView) view.findViewById(R.id.tv_pay_edit);
        fl_base = (FrameLayout) view.findViewById(R.id.fl_base);
        return view;
    }

    public void initData() {

    }
}
