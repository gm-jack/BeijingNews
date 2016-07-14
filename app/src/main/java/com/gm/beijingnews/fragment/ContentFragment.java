package com.gm.beijingnews.fragment;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.gm.beijingnews.R;
import com.gm.beijingnews.activity.MainActivity;
import com.gm.beijingnews.base.BaseFragment;
import com.gm.beijingnews.base.BasePager;
import com.gm.beijingnews.pagers.FivePager;
import com.gm.beijingnews.pagers.FourPager;
import com.gm.beijingnews.pagers.HomePager;
import com.gm.beijingnews.pagers.NewsDetailPager;
import com.gm.beijingnews.pagers.SecondPager;
import com.gm.beijingnews.pagers.ThreePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/28.
 */
public class ContentFragment extends BaseFragment {
    private ViewPager vp_content;
    private RadioGroup rg_man_tag;
    private List<BasePager> list;
    private MainActivity mainActivity;

    private boolean isFirst = true;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.content_fragment, null);
        vp_content = (ViewPager) view.findViewById(R.id.vp_content);
        rg_man_tag = (RadioGroup) view.findViewById(R.id.rg_man_tag);
        //ListView listview = new ListView(context);
        list = new ArrayList<>();
        list.add(new HomePager(context));
        list.add(new SecondPager(context));
        list.add(new ThreePager(context));
        list.add(new FourPager(context));
        list.add(new FivePager(context));
        vp_content.setAdapter(new MyPagerAdapter());
        vp_content.addOnPageChangeListener(new MyOnPageChangeListener());
        if (isFirst) {
            //设置默认选择按钮
            rg_man_tag.check(R.id.rb_home);
            isFirst = false;
        } else {
            rg_man_tag.check(mCheckedId);
        }
        //设置默认进入界面
        switchPager();
        //设置侧边栏显示状态
        checkSlidingMenuMode();
        rg_man_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        return view;
    }

    private void checkSlidingMenuMode() {
        mainActivity = (MainActivity) context;
        SlidingMenu sm = mainActivity.getSlidingMenu();
        if (currentPosition == 1) {
            sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        } else {
            sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public void initData() {
        LogUtil.e("新闻总页面单初始化");
    }

    private int currentPosition = 0;

    public SecondPager getSecondPager() {
        return (SecondPager) list.get(1);
    }

    private int mCheckedId;

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    currentPosition = 0;
                    vp_content.setCurrentItem(currentPosition, false);
                    break;
                case R.id.rb_news:
                    currentPosition = 1;
                    vp_content.setCurrentItem(currentPosition, false);
                    break;
                case R.id.rb_smartservcie:
                    currentPosition = 2;
                    vp_content.setCurrentItem(currentPosition, false);
                    break;
                case R.id.rb_govaffair:
                    currentPosition = 3;
                    vp_content.setCurrentItem(currentPosition, false);
                    break;
                case R.id.rb_setting:
                    currentPosition = 4;
                    vp_content.setCurrentItem(currentPosition, false);
                    break;
            }
            mCheckedId = checkedId;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean isInitSecond = false;

    private void switchPager() {
        BasePager basePager = list.get(currentPosition);
        LogUtil.e("currentPosition==" + currentPosition);
        //调用pager初始化加载数据
        basePager.initData();
        if (currentPosition == 2) {
            isInitSecond = true;
        }
        if (isInitSecond) {
            SecondPager mSecondPager = (SecondPager) list.get(1);
            if (currentPosition != 1 && null != mSecondPager.getNewDeatailPager()) {
                NewsDetailPager newDeatailPager = mSecondPager.getNewDeatailPager();
                if (null != newDeatailPager) {
                    Handler handler = newDeatailPager.getNewMenuPager().getHandler();
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    }

                }
            }
        }
        if (currentPosition == 3) {
            basePager.tv_pay_edit.setVisibility(View.VISIBLE);
        }
//        SecondPager secondPager = (SecondPager) list.get(1);
//        secondPager.getNewDeatailPager();
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //选择布局
            switchPager();
            checkSlidingMenuMode();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = list.get(position);
            View rootView = basePager.rootView;
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
