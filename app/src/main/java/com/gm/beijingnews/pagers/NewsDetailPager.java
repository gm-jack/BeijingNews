package com.gm.beijingnews.pagers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.gm.beijingnews.R;
import com.gm.beijingnews.base.MenuDetailBaePager;
import com.gm.beijingnews.domain.NewsData;
import com.gm.beijingnews.utils.DataConvert;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 * 菜单新闻详情界面
 */
public class NewsDetailPager extends MenuDetailBaePager {

    private ViewPager vp_news_detail;
    private TabPageIndicator indicator_news_detail;
    private List<NewsData.Data.Children> childrenList;
    private List<NewMenuPager> newMenuList;

    public NewsDetailPager(Context context, List<NewsData.Data.Children> children) {
        super(context);
        childrenList = children;
    }

    @Override
    public View initView() {
        LogUtil.e("新闻详情界面初始化");
        View view = View.inflate(context, R.layout.news_detail, null);
        vp_news_detail = (ViewPager) view.findViewById(R.id.vp_news_detail);
        indicator_news_detail = (TabPageIndicator) view.findViewById(R.id.indicator_news_detail);
        indicator_news_detail.setOnPageChangeListener(new MyOnPageChangeListener());
        return view;
    }

    private void initNewMenuPager() {
        newMenuList = new ArrayList<>();
        for (int i = 0; i < childrenList.size(); i++) {
            newMenuList.add(new NewMenuPager(context, childrenList.get(i)));
        }
    }

    @Override
    public void initData() {
        //初始化子视图
        initNewMenuPager();
        vp_news_detail.setAdapter(new MyPagerAdapter());
        indicator_news_detail.setViewPager(vp_news_detail);
        //LogUtil.e("newsdetailpager选择 " + DataConvert.MENU_NEWS_POSTION);
        vp_news_detail.setCurrentItem(DataConvert.MENU_NEWS_POSTION);
        indicator_news_detail.setCurrentItem(DataConvert.MENU_NEWS_POSTION);
        switchNewsMenu(DataConvert.MENU_NEWS_POSTION);
    }

    private int prePosition = 0;

    public NewMenuPager getNewMenuPager() {
        return newMenuList.get(prePosition);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //indicator_news_detail.scroll(position, positionOffset);
            //LogUtil.e("Scrolled " + "position=" + position + "  positionOffset=" + positionOffset);

        }

        @Override
        public void onPageSelected(int position) {
            LogUtil.e("prePosition=" + prePosition + "   position=" + position);
            NewMenuPager newMenuPager = newMenuList.get(prePosition);
            newMenuPager.getHandler().removeCallbacksAndMessages(null);
            LogUtil.e("NewsDetailPager   onPageSelected()  remove()");
            prePosition = position;
            DataConvert.MENU_NEWS_POSTION = position;
            //LogUtil.e("选择 " + DataConvert.MENU_NEWS_POSTION);
            switchNewsMenu(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void switchNewsMenu(int position) {
        NewMenuPager newMenuPager = newMenuList.get(position);
        newMenuPager.getHandler().removeCallbacksAndMessages(null);
        newMenuPager.initData();
    }


    class MyPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            return childrenList.get(position).getTitle();
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
            NewMenuPager newMenuPager = newMenuList.get(position);
            View rootView = newMenuPager.rootView;
            //LogUtil.e("初始化" + position);
            //newMenuPager.initData();
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
