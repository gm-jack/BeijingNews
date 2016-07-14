package com.gm.beijingnews.pagers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gm.beijingnews.R;
import com.gm.beijingnews.activity.ShowMessageActivity;
import com.gm.beijingnews.adapter.MyBaseAdapter;
import com.gm.beijingnews.adapter.PubViewHolder;
import com.gm.beijingnews.base.MenuDetailBaePager;
import com.gm.beijingnews.domain.NewsData;
import com.gm.beijingnews.domain.NewsMenuData;
import com.gm.beijingnews.utils.Constact;
import com.gm.beijingnews.utils.DataConvert;
import com.gm.beijingnews.utils.DpUtil;
import com.gm.beijingnews.utils.MemoryCacheUtil;
import com.gm.beijingnews.view.MyRefeshView;
import com.gm.beijingnews.utils.WebsiteUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class NewMenuPager extends MenuDetailBaePager {

    private ViewPager viewpager;
    private TextView tvTitle;
    private LinearLayout llPointGroup;
    private MyRefeshView listview;

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

    public NewMenuPager(Context context, NewsData.Data.Children children) {
        super(context);
        this.children = children;
    }

    private static final int IMAGE_SHOW = 0;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            viewpager.setCurrentItem((viewpager.getCurrentItem() + 1) % topnews.size());
            handler.removeMessages(IMAGE_SHOW);
            handler.sendEmptyMessageDelayed(IMAGE_SHOW, 3000);
            ////LogUtil.e("Handler   " + children.getTitle() + "  topnews.size()=" + topnews.size());
        }
    };

    public Handler getHandler() {
        return handler;
    }

    public int getPosition() {
        return currentPosition;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.news_detail_pager, null);
        View topView = View.inflate(context, R.layout.news_detail_top, null);
        viewpager = (ViewPager) topView.findViewById(R.id.viewpager);
        tvTitle = (TextView) topView.findViewById(R.id.tv_title);
        llPointGroup = (LinearLayout) topView.findViewById(R.id.ll_point_group);
        listview = (MyRefeshView) view.findViewById(R.id.listview);
        //将view设置为listview的头部
        listview.addTopNews(topView);
        listview.setOnRefreshListListener(new MyRefeshView.OnRefreshListListener() {
            @Override
            public void onDownRefresh() {
                isLoadMore = false;
                getDataFromNet();
            }

            @Override
            public void onMoreRefresh() {
                isLoadMore = true;
                getDataFromNet();
            }
        });
        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    @Override
    public void initData() {
//        handler.removeCallbacksAndMessages(null);
        String topData = DataConvert.getFileData(context, Constact.NEWMENUPAGER_DATA, "sp_" + Constact.NEWMENUPAGER_DATA);
        //LogUtil.e("newsmenupager  data==" + topData);
        if (!TextUtils.isEmpty(topData)) {
            parseChildJson(topData);
            result2Adapter();
            result2Listview();
        }
        //LogUtil.e("newsmenupager   Init" + children.getTitle());
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
                    //LogUtil.e("保存数据");
                    DataConvert.saveFileData(context, Constact.NEWMENUPAGER_DATA, result, "sp_" + Constact.NEWMENUPAGER_DATA);
//                    DataConvert.saveTopData(context, DataConvert.TAG_TOPVIEWPAGER, result);
                    result2Adapter();
                    result2Listview();
                }
                //下拉刷新控件更新状态
                listview.onFinishLoad(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError() " + ex.getMessage());
                listview.onFinishLoad(false);
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
                LogUtil.e("点击 position==" + position);
                NewsMenuData.DataBean.NewsBean newsBean = news.get(position);
//                String news_data = DataConvert.getFileData(context, Constact.NEWMENUPAGER_POSITION + newsMenuData.getData().getNews().get(position).getId(), "sp_" + Constact.NEWMENUPAGER_POSITION + newsMenuData.getData().getNews().get(position).getId());
//                LogUtil.e("news_data" + news_data);

//                String[] split = news_data.split(",");
//                for (int i = 0; i < split.length; i++) {
//                    if (split[i].contains(position+"")) {
//                        LogUtil.e("news   position==" + position);
//                        tv_title.setTextColor(Color.GRAY);
//                    } else {
//                        tv_title.setTextColor(Color.BLACK);
//                    }
//                }
                tv_title.setText(newsBean.getTitle());
                tv_time.setText(newsBean.getPubdate());
                //异步加载图片资源
                String path = WebsiteUtil.WebsiteChange(newsBean.getListimage());
                if (null != mMemoryCacheUtil.getBitmapFromMemory(path)) {
                    LogUtil.e("内存加载");
                    iv_icon.setImageBitmap(mMemoryCacheUtil.getBitmapFromMemory(path));
                } else {
                    new MyAsyncTask(iv_icon, WebsiteUtil.WebsiteChange(path), Constact.TAG_LISTVIEW).execute();
                }
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
        if (myPagerAdapter!=null) {
            myPagerAdapter.notifyDataSetChanged();
        }
        viewpager.setCurrentItem(currentPosition);
        listview.setSelection(listPosittion);
        //LogUtil.e("currentPosition==" + currentPosition);
        initTop(currentPosition);
        myPagerAdapter = new MyPagerAdapter();
        viewpager.setAdapter(myPagerAdapter);
        //设置viewpager监听器
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        //移除所有消息和任务
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(IMAGE_SHOW);
            }
        }, 3000);

        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        handler.removeCallbacksAndMessages(null);
                        LogUtil.e("NewsMenuPager   setOnTouchListener()  remove()");
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.sendEmptyMessageDelayed(IMAGE_SHOW, 3000);
                        break;
                }
                return false;
            }
        });
    }

    private void parseChildJson(String result) {
        newsMenuData = new Gson().fromJson(result, NewsMenuData.class);
    }


    /**
     * 初始化新闻顶部
     */
    private void initTop(int position) {
        LogUtil.e("currentPosition==" + currentPosition);
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
            ImageView image = new ImageView(context);
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
            if (null != mMemoryCacheUtil.getBitmapFromMemory(path)) {
                LogUtil.e("内存加载");
                image.setImageBitmap(mMemoryCacheUtil.getBitmapFromMemory(path));
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

//    /**
//     * 异步加载图片数据
//     */
//    private Map<String, Bitmap> map = new HashMap<>();
//    /**
//     * 保存topview图片数据
//     */
//    private Map<String, Bitmap> maps = new HashMap<>();

    private MemoryCacheUtil mMemoryCacheUtil = new MemoryCacheUtil();

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
            this.bitmap = ImageLoader.getInstance().loadImageSync(path);
//            if (tag.equals(Constact.TAG_VIEWPAGER)) {
//                mMemoryCacheUtil.putBitmap2Memory(path,bitmap);
//            } else {
            mMemoryCacheUtil.putBitmap2Memory(path, bitmap);
//            }

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
            LogUtil.e("position====" + currentPosition);
            currentPosition = position;
            initTop(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String oldData = DataConvert.getFileData(context, Constact.NEWMENUPAGER_POSITION + newsMenuData.getData().getNews().get(position).getId(), "sp_" + Constact.NEWMENUPAGER_POSITION + newsMenuData.getData().getNews().get(position).getId());
            if (!(oldData.equals("")) && !oldData.contains(position + "")) {
                oldData = oldData + position + ",";
            }
            DataConvert.saveFileData(context, Constact.NEWMENUPAGER_POSITION + newsMenuData.getData().getNews().get(position).getId(), oldData, "sp_" + Constact.NEWMENUPAGER_POSITION + newsMenuData.getData().getNews().get(position).getId());
            mAdapter.notifyDataSetChanged();
            LogUtil.e("位置=" + position);
            Intent intent = new Intent(context, ShowMessageActivity.class);
            intent.putExtra("messageUrl", newsMenuData.getData().getNews().get(position - 1).getUrl());
            context.startActivity(intent);
        }
    }
}
