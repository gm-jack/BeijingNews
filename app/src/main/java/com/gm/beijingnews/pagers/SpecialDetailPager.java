package com.gm.beijingnews.pagers;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gm.beijingnews.R;
import com.gm.beijingnews.base.MenuDetailBaePager;
import com.gm.beijingnews.domain.NewsData;
import com.gm.beijingnews.utils.DataConvert;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 * 专题新闻详情界面
 */
public class SpecialDetailPager extends MenuDetailBaePager {
    private ViewPager vp_news_detail;
    private TabLayout tl_news_detail;
    private List<NewsData.Data.Children> childrenList;
    private List<NewMenuRefreshPager> newMenuList;
    private MyPagerAdapter myPagerAdapter;

    public SpecialDetailPager(Context context, List<NewsData.Data.Children> children) {
        super(context);
        childrenList = children;
    }

    @Override
    public View initView() {
        LogUtil.e("专题新闻详情界面初始化");
        View view = View.inflate(context, R.layout.news_detail_tablayout, null);
        vp_news_detail = (ViewPager) view.findViewById(R.id.tl_vp_news_detail);
        tl_news_detail = (TabLayout) view.findViewById(R.id.tl_news_detail);
        //indicator_news_detail.setOnPageChangeListener(new MyOnPageChangeListener());
        vp_news_detail.addOnPageChangeListener(new MyOnPageChangeListener());
        return view;
    }

    private void initNewMenuPager() {
        newMenuList = new ArrayList<>();
        for (int i = 0; i < childrenList.size(); i++) {
            newMenuList.add(new NewMenuRefreshPager(context, childrenList.get(i)));
        }
    }

    @Override
    public void initData() {
        initNewMenuPager();
        LogUtil.e("选择 " + DataConvert.MENU_NEWS_POSTION);
        vp_news_detail.setCurrentItem(DataConvert.MENU_NEWS_POSTION, false);
        myPagerAdapter = new MyPagerAdapter();
        vp_news_detail.setAdapter(myPagerAdapter);

        //设置模式
        tl_news_detail.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tl_news_detail.setTabGravity(TabLayout.GRAVITY_CENTER);
        tl_news_detail.setupWithViewPager(vp_news_detail);
       setTab();
        //tl_news_detail.setTabsFromPagerAdapter(myPagerAdapter);
        //vp_news_detail.setCurrentItem(DataConvert.MENU_NEWS_POSTION);
    }

    private void setTab() {
        for (int i = 0; i < tl_news_detail.getTabCount(); i++) {
            TabLayout.Tab tab = tl_news_detail.getTabAt(i);
            tab.setCustomView(myPagerAdapter.getTabView(i));
//            tl_news_detail.addTab(tl_news_detail.newTab().setCustomView(myPagerAdapter.getTabView(i)));
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            DataConvert.MENU_NEWS_POSTION = position;
            LogUtil.e("选择 " + DataConvert.MENU_NEWS_POSTION);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private LayoutInflater mLayoutInflate;

    class MyPagerAdapter extends PagerAdapter {
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return childrenList.get(position).getTitle();
//        }


        public View getTabView(int position) {
            mLayoutInflate = LayoutInflater.from(context);
            View view = mLayoutInflate.inflate(R.layout.item_tab, null);
            ImageView iv_tab_image = (ImageView) view.findViewById(R.id.iv_tab_image);
            iv_tab_image.setImageResource(R.mipmap.ic_launcher);
            TextView tv_tab_title = (TextView) view.findViewById(R.id.tv_tab_title);
            tv_tab_title.setText(childrenList.get(position).getTitle());
            return view;
        }

        @Override
        public int getCount() {
            return newMenuList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NewMenuRefreshPager newMenuPager = newMenuList.get(position);
            View rootView = newMenuPager.rootView;
            newMenuPager.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
