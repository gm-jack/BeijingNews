package com.gm.beijingnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gm.beijingnews.R;
import com.gm.beijingnews.utils.DataConvert;
import com.gm.beijingnews.utils.DpUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements View.OnClickListener {
    private ViewPager vpGuide;
    private Button btnGuide;
    private LinearLayout llGuide;
    private ImageView iv_guide;
    private int images[] = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private int cycleRudis;
    //保存viewpager数据的集合
    private List<ImageView> list;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-06-28 15:57:58 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        btnGuide = (Button) findViewById(R.id.btn_guide);
        llGuide = (LinearLayout) findViewById(R.id.ll_guide);
        iv_guide = (ImageView) findViewById(R.id.iv_guide);

        btnGuide.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-06-28 15:57:58 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnGuide) {
            // Handle clicks for btnGuide
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        findViews();
        cycleRudis = DpUtil.dp2px(this, 10);
        initView();
        initData();
    }

    private void initData() {
        DataConvert.saveData(this, DataConvert.GUIDE, true);
    }

    /**
     * 引导圆圈的间距
     */
    private int cycleWidth;

    private void initView() {
        list = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(images[i]);
            list.add(imageView);

            imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.guide_cycle);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cycleRudis, cycleRudis);
            if (i != 0)
                params.leftMargin = cycleRudis;
            //params.rightMargin = cycleRudis;
            llGuide.addView(imageView, params);
        }
        vpGuide.setAdapter(new MyPagerAdapter());
        //得到onlayout监听,布局监听得到圆圈间距
        vpGuide.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * viewPager滑动监听回调
             * @param position
             * @param positionOffset  滑动百分比
             * @param positionOffsetPixels  滑动像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //移动距离计算
                float scroll = cycleWidth * (positionOffset + position);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_guide.getLayoutParams();
                params.leftMargin = (int) scroll;
                iv_guide.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == list.size() - 1) {
                    btnGuide.setVisibility(View.VISIBLE);
                } else {
                    btnGuide.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            //onLayout()方法默认执行两次
            vpGuide.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            cycleWidth = llGuide.getChildAt(1).getLeft() - llGuide.getChildAt(0).getLeft();
        }
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 数据加载适配器
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = list.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
