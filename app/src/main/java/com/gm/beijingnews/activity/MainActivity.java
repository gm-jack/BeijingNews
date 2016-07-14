package com.gm.beijingnews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.gm.beijingnews.R;
import com.gm.beijingnews.fragment.ContentFragment;
import com.gm.beijingnews.fragment.MenuFragment;
import com.gm.beijingnews.utils.DpUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends SlidingFragmentActivity {

    public static final String CONTENT = "content";
    public static final String MENU = "menu";
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBehindContentView(R.layout.empty_menu);
        setContentView(R.layout.empty_content);
        SlidingMenu sm = getSlidingMenu();
        //设置模式 左侧边栏，左和右侧边栏，没有侧边栏
        sm.setMode(SlidingMenu.LEFT);
        //设置滑动区域
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置可见页面占用
        sm.setBehindOffset(DpUtil.dp2px(this, 220));
        initFragment();
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_main_content, new ContentFragment(), CONTENT);
        transaction.replace(R.id.fl_main_menu, new MenuFragment(), MENU);
        transaction.commit();
    }

    public MenuFragment getLeftMenuFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (MenuFragment) fragmentManager.findFragmentByTag(MENU);
    }

    public ContentFragment getContentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (ContentFragment) fragmentManager.findFragmentByTag(CONTENT);
    }
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
