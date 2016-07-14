package com.gm.beijingnews.pagers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.gm.beijingnews.R;
import com.gm.beijingnews.activity.ShowImageActivity;
import com.gm.beijingnews.adapter.MyBaseAdapter;
import com.gm.beijingnews.adapter.PubViewHolder;
import com.gm.beijingnews.base.MenuDetailBaePager;
import com.gm.beijingnews.domain.GroupPhotoBean;
import com.gm.beijingnews.utils.BitmapUtil;
import com.gm.beijingnews.utils.Constact;
import com.gm.beijingnews.utils.WebsiteUtil;
import com.gm.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.common.util.LogUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 * 图组详情界面
 */
public class GroupDetailPager extends MenuDetailBaePager {
    private ListView lv_group;
    private GridView gv_group;
    private SwipeRefreshLayout srl_group;

    private MyBaseAdapter<GroupPhotoBean.DataBean.NewsBean> mAdapter;
    private boolean isListShow = true;

    public GroupDetailPager(Context context) {
        super(context);
    }

    public void selectListAndGrid(final ImageView switcher) {
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("TAG", isListShow + "");
                gv_group.setVisibility(isListShow ? View.VISIBLE : View.GONE);
                lv_group.setVisibility(isListShow ? View.GONE : View.VISIBLE);

                if (isListShow) {
                    gv_group.setAdapter(mAdapter);
                    switcher.setImageResource(R.drawable.icon_pic_grid_type);
                } else {
                    lv_group.setAdapter(mAdapter);
                    switcher.setImageResource(R.drawable.icon_pic_list_type);
                }
                isListShow = !isListShow;
            }
        });
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.group_pager, null);
        lv_group = (ListView) view.findViewById(R.id.lv_group);
        gv_group = (GridView) view.findViewById(R.id.gv_group);
        srl_group = (SwipeRefreshLayout) view.findViewById(R.id.srl_group);
        //初始化SwipeRefreshLayout
        initSwipeLayout();
        //解决SwipeRefreshLayout与Listview或者GridView的滑动冲突
        addSlidingConflict();
        return view;
    }

    private void addSlidingConflict() {
        lv_group.setOnItemClickListener(new MyOnItemClickListener(0));
        gv_group.setOnItemClickListener(new MyOnItemClickListener(1));
//        gv_group.setEmptyView(new View(context));
        for (int i = 0; i < gv_group.getNumColumns(); i++) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(1, 1);
            gv_group.addView(new View(context), i, params);
        }
        gv_group.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("TAG", "firstVisibleItem" + firstVisibleItem);
                if (firstVisibleItem == 0) {
                    srl_group.setEnabled(true);
                } else {
                    srl_group.setEnabled(false);
                }
            }
        });
        lv_group.addHeaderView(new View(context));
        lv_group.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e("TAG", "firstVisibleItem" + firstVisibleItem);
                if (firstVisibleItem == 0) {
                    srl_group.setEnabled(true);
                } else {
                    srl_group.setEnabled(false);
                }
            }
        });
    }

    private void initSwipeLayout() {
        srl_group.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromJson();
            }
        });
        //设置下拉刷新的圆圈是否缩放出现(动画)，出现位置，最大下拉位置；
        srl_group.setProgressViewOffset(true, 40, 120);
        //设置大小
        srl_group.setSize(SwipeRefreshLayout.LARGE);
        //设置圆圈颜色
        srl_group.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void initData() {
        LogUtil.e("图组详情");
        getDataFromJson();
    }

    private void getDataFromJson() {
        StringRequest request = new StringRequest(StringRequest.Method.GET, Constact.JSON_NET_PHOTOS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtil.e("onResponse()  " + s);
                processJson(s);
                srl_group.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e("onErrorResponse()" + volleyError.getMessage());
            }
        }) {
            /**
             * 解决Volley请求数据中文乱码问题
             * @param response
             * @return
             */
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String url = null;
                try {
                    url = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(url, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        //添加到队列中
        VolleyManager.addRequest(request, "photos");
    }

    private List<GroupPhotoBean.DataBean.NewsBean> photoList;

    private void processJson(String s) {
        GroupPhotoBean bean = parseJson(s);
        photoList = bean.getData().getNews();
        result2Adapter();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int position = msg.arg1;
            ImageView image = (ImageView) lv_group.findViewWithTag(position);
            switch (msg.what) {
                case Constact.CACHE_SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if (lv_group != null && lv_group.isShown()) {
                        ImageView imageView = (ImageView) lv_group.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }

                    if (gv_group != null && gv_group.isShown()) {
                        ImageView imageView = (ImageView) gv_group.findViewWithTag(position);
                        if (imageView != null && bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case Constact.CACHE_FAIL:
                    image.setImageResource(R.drawable.pic_item_list_default);
                    break;
            }
        }
    };
    private BitmapUtil mBitmapUtil;

    private void result2Adapter() {
        mAdapter = new MyBaseAdapter<GroupPhotoBean.DataBean.NewsBean>(context, photoList, R.layout.item_group) {
            @Override
            public void convert(PubViewHolder viewholder, int position) {
                ImageView iv_icon = viewholder.getView(R.id.iv_icon);
                TextView tv_title = viewholder.getView(R.id.tv_title);
                tv_title.setText(photoList.get(position).getTitle());
                //使用Volley去请求下载图片
//                loadImage(iv_icon, photoList.get(position).getListimage());
                //三级缓存加载图片
                iv_icon.setTag(position);
                mBitmapUtil = new BitmapUtil(handler, photoList.get(position).getListimage(), position);
                Bitmap bitmap = mBitmapUtil.getBitmap();
                if (null != bitmap) {
                    //加载本地和缓存图片
                    iv_icon.setImageBitmap(bitmap);
                }
            }
        };
        if (isListShow) {
            lv_group.setAdapter(mAdapter);
            lv_group.setVisibility(View.VISIBLE);
            gv_group.setVisibility(View.GONE);
        } else {
            gv_group.setAdapter(mAdapter);
            lv_group.setVisibility(View.GONE);
            gv_group.setVisibility(View.VISIBLE);
        }
    }

    private void loadImage(final ImageView iv_icon, String path) {
        path = WebsiteUtil.WebsiteChange(path);
        iv_icon.setTag(path);
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {
                    if (imageContainer.getBitmap() != null) {
                        iv_icon.setImageBitmap(imageContainer.getBitmap());
                    } else {
                        iv_icon.setImageResource(R.drawable.pic_item_list_default);
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };
        VolleyManager.getImageLoader().get(path, listener);
        /**
         * 使用请求去获取图片会影响图片的位置错乱
         */
//        StringRequest request=new StringRequest(StringRequest.Method.GET, path, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                if(iv_icon.getTag().equals(path)){
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                LogUtil.e("onErrorResponse()" + volleyError.getMessage());
//            }
//        }){
//            /**
//             * 解决Volley请求数据中文乱码问题
//             * @param response
//             * @return
//             */
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String url = null;
//                try {
//                    url = new String(response.data, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                return Response.success(url, HttpHeaderParser.parseCacheHeaders(response));
//            }
//        };
//        VolleyManager.addRequest(request,"photo");
    }

    private GroupPhotoBean parseJson(String s) {
        return new Gson().fromJson(s, GroupPhotoBean.class);
    }

//    private PopupWindow mPopupWindow;
//    /**
//     * poupwindow初始化
//     */
//    public void initPopupWindow() {
//        ImageView image= (ImageView) View.inflate(context,R.layout.popup_view,null);
//        mPopupWindow=new PopupWindow(image,ViewGroup.LayoutParams)
//    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        private int flag;

        public MyOnItemClickListener(int i) {
            flag = i;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (flag) {
                case 0:
                    //LogUtil.e("" + position);
//                    Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                case 1:
                    //LogUtil.e("" + position);
//                    Toast.makeText(context, "" + gv_group.getNumColumns(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ShowImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("photo", (ArrayList) photoList);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
            }
        }
    }

}
