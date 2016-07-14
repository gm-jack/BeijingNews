package com.gm.beijingnews.pagers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gm.beijingnews.R;
import com.gm.beijingnews.adapter.MyBaseAdapter;
import com.gm.beijingnews.adapter.PubViewHolder;
import com.gm.beijingnews.base.MenuDetailBaePager;
import com.gm.beijingnews.domain.NewsData;
import com.gm.beijingnews.domain.NewsMenuData;
import com.gm.beijingnews.utils.Constact;
import com.gm.beijingnews.utils.DpUtil;
import com.gm.beijingnews.utils.WebsiteUtil;
import com.gm.beijingnews.view.HackyViewPager;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Administrator on 2016/6/29.
 */
public class NewMenuRefreshPager extends MenuDetailBaePager {

    private ViewPager viewpager;
    private TextView tvTitle;
    private LinearLayout llPointGroup;
    private ListView listview;
    private PullToRefreshListView mPullToRefreshGridView;
    private NewsMenuData newsMenuData;
    private NewsData.Data.Children children;
    /**
     * topview的viewpager数据
     */
    private List<NewsMenuData.DataBean.TopnewsBean> topnews;
    private MyPagerAdapter myPagerAdapter;
    /**
     * listview数据
     */
    private MyBaseAdapter<NewsMenuData.DataBean.NewsBean> mAdapter;
    private List<NewsMenuData.DataBean.NewsBean> news;


    /**
     * 保存当前选中top标签
     */
    private int currentPosition = 0;
    private int listPosittion = 0;
    private RequestParams entity;
    public NewMenuRefreshPager(Context context, NewsData.Data.Children children) {
        super(context);
        this.children = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.news_detail_refresh_pager, null);
        View topView = View.inflate(context, R.layout.news_detail_top_tablayout, null);
        viewpager = (HackyViewPager) topView.findViewById(R.id.viewpager);
        tvTitle = (TextView) topView.findViewById(R.id.tv_title);
        llPointGroup = (LinearLayout) topView.findViewById(R.id.ll_point_group);
        mPullToRefreshGridView = (PullToRefreshListView) view.findViewById(R.id.listview);
        listview = mPullToRefreshGridView.getRefreshableView();
        listview.addHeaderView(topView);
        mPullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = false;
                getDataFromNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = true;
                getDataFromNet();
            }
        });
        //将view设置为listview的头部
//        listview.addTopNews(topView);
//        listview.setOnRefreshListListener(new MyRefeshView.OnRefreshListListener() {
//            @Override
//            public void onDownRefresh() {
//                isLoadMore = false;
//                getDataFromNet();
//            }
//
//            @Override
//            public void onMoreRefresh() {
//                isLoadMore = true;
//                getDataFromNet();
//            }
//        });
        return view;
    }

    @Override
    public void initData() {
//        String topData = DataConvert.getTopData(context, DataConvert.TAG_TOPVIEWPAGER);
//        if (!TextUtils.isEmpty(topData)) {
//            parseChildJson(topData);
//            result2Adapter();
//            result2Listview();
//        }
        getDataFromNet();
    }

    private boolean isLoadMore = false;

    /**
     * 联网获取数据
     */
    private void getDataFromNet() {
        if (isLoadMore) {
            entity = new RequestParams(Constact.JSON_NET_HOSTNAME + newsMenuData.getData().getMore());
        } else {
            entity = new RequestParams(Constact.JSON_NET_HOSTNAME + children.getUrl());
        }
        //LogUtil.e("网址 " + Constact.JSON_NET_HOSTNAME + children.getUrl() + "   MoreData=" + Constact.JSON_NET_HOSTNAME + newsMenuData.getData().getMore());
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                parseChildJson(result);
                //LogUtil.e("result= " + result);
                if (isLoadMore) {
                    mAdapter.refreshAdapter(news);
                } else {
                    //保存String数据到sp中
                    //DataConvert.saveTopData(context, DataConvert.TAG_TOPVIEWPAGER, result);
                    result2Adapter();
                    result2Listview();
                }
                // Call onRefreshComplete when the list has been refreshed.
                mPullToRefreshGridView.onRefreshComplete();
                //下拉刷新控件更新状态
                //listview.onFinishLoad(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError() " + ex.getMessage());
                //listview.onFinishLoad(false);
                // Call onRefreshComplete when the list has been refreshed.
                mPullToRefreshGridView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled() " + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished() ");
            }
        });
    }

    private void result2Listview() {
        if (isLoadMore) {
            news.addAll(newsMenuData.getData().getNews());
        } else {
            news = newsMenuData.getData().getNews();
        }
        mAdapter = new MyBaseAdapter<NewsMenuData.DataBean.NewsBean>(context, news, R.layout.item_tab_detail_pager) {
            @Override
            public void convert(PubViewHolder viewholder, int position) {
                ImageView iv_icon = viewholder.getView(R.id.iv_icon);
                TextView tv_title = viewholder.getView(R.id.tv_title);
                TextView tv_time = viewholder.getView(R.id.tv_time);
                ImageView iv_comment = viewholder.getView(R.id.iv_comment);
                NewsMenuData.DataBean.NewsBean newsBean = news.get(position);
                //异步加载图片资源
                String path = WebsiteUtil.WebsiteChange(newsBean.getListimage());
                if (null != map.get(newsBean.getListimage())) {
                    iv_icon.setImageBitmap(map.get(path));
                } else {
                    new MyAsyncTask(iv_icon, WebsiteUtil.WebsiteChange(path), Constact.TAG_LISTVIEW).execute();
                }
                tv_title.setText(newsBean.getTitle());
                tv_time.setText(newsBean.getPubdate());
            }
        };
        listview.setAdapter(mAdapter);
    }

    private void result2Adapter() {
        topnews = newsMenuData.getData().getTopnews();
        llPointGroup.removeAllViews();
        for (int i = 0; i < topnews.size(); i++) {
            ImageView point = new ImageView(context);
            point.setBackgroundResource(R.drawable.point_shape_selector);
            point.setEnabled(false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DpUtil.dp2px(context, 10), DpUtil.dp2px(context, 10));
            params.leftMargin = DpUtil.dp2px(context, 10);
            point.setLayoutParams(params);
            llPointGroup.addView(point);
        }
        viewpager.setCurrentItem(currentPosition);
        listview.setSelection(listPosittion);

        myPagerAdapter = new MyPagerAdapter();
        myPagerAdapter.notifyDataSetChanged();
        viewpager.setAdapter(myPagerAdapter);
        //加载默认
        initTop(0);
        //设置viewpager监听器
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void parseChildJson(String result) {
        newsMenuData = new Gson().fromJson(result, NewsMenuData.class);
    }


    /**
     * 初始化新闻顶部
     */
    private void initTop(int position) {
        tvTitle.setText(topnews.get(position).getTitle());
        /**
         * 方式一
         */
//        llPointGroup.getChildAt(position).setEnabled(true);
//        llPointGroup.getChildAt(currentPosition).setEnabled(false);
        /**
         * 方式二
         */
        for (int i = 0; i < topnews.size(); i++) {
            if (position == i) {
                llPointGroup.getChildAt(i).setEnabled(true);
            }
            if (position != i && llPointGroup.getChildAt(i).isEnabled()) {
                //LogUtil.e(i + "    " + llPointGroup.getChildAt(i).isEnabled());
                llPointGroup.getChildAt(i).setEnabled(false);
            }
        }
    }

    /**
     * 保存topview图片数据
     */
    private Map<String, Bitmap> maps = new HashMap<>();

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView image = new PhotoView(container.getContext());
            /**
             * 方式一
             */
//            int i = topnews.get(position).getTopimage().indexOf("/", 24);
//            String imageName = topnews.get(position).getTopimage().substring(i);
            /**
             * 方式二
             */
            //异步加载图片资源
            String path = WebsiteUtil.WebsiteChange(topnews.get(position).getTopimage());
            if (null != maps.get(path)) {
                image.setImageBitmap(maps.get(path));
            } else {
                new MyAsyncTask(image, WebsiteUtil.WebsiteChange(path), Constact.TAG_VIEWPAGER).execute();
            }
            //x.image().bind(image, WebsiteUtil.WebsiteChange(topnews.get(position).getTopimage()));
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 异步加载图片数据
     */
    private Map<String, Bitmap> map = new HashMap<>();

    class MyAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        private ImageView imageview;
        private String path;
        private Bitmap bitmap;
        private String tag;

        public MyAsyncTask(ImageView imageview, String path, String tag) {
            this.imageview = imageview;
            this.path = path;
            this.tag = tag;
        }

        @Override
        protected void onPreExecute() {
            imageview.setTag(path);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            bitmap = ImageLoader.getInstance().loadImageSync(path);
            if (tag.equals(Constact.TAG_VIEWPAGER)) {
                map.put(path, bitmap);
            } else {
                maps.put(path, bitmap);
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                if (imageview.getTag().equals(path)) {
                    imageview.setImageBitmap(bitmap);
                }
            } else {
                imageview.setImageResource(R.drawable.news_pic_default);
            }

        }
    }

    /**
     * pager改变监听器
     */
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //LogUtil.e("position====" + position);
            initTop(position);
            currentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
