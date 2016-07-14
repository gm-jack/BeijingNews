package com.gm.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2016/6/29.
 * 菜单：新闻、专题、组图、互动界面的基类
 */
public abstract class MenuDetailBaePager {
    public Context context;
    public View rootView;

    public MenuDetailBaePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    public abstract View initView();

    /**
     * 加载数据调用
     */
    public void initData() {

    }

}
