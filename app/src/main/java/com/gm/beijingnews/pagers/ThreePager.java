package com.gm.beijingnews.pagers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.gm.beijingnews.R;
import com.gm.beijingnews.adapter.WaresAdapter;
import com.gm.beijingnews.base.BasePager;
import com.gm.beijingnews.domain.WaresBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xutils.common.util.LogUtil;

import java.util.List;

import okhttp3.Request;

/**
 * Created by Administrator on 2016/6/29.
 * 商城界面
 */
public class ThreePager extends BasePager {
    private RecyclerView rv_three;
    private MaterialRefreshLayout mrl_three;
    private int pagerSize = 5;
    private int curPage = 1;
    private List<WaresBean.ListBean> list;
    private WaresAdapter mWaresAdapter;
    private int currentPage;
    private int totalPage;
    /**
     * 状态
     */
    public static final int REFRESH_DOWN = 1;
    public static final int REFRESH_MORE = 2;
    public static final int REFRESH_NORMAL = 3;
    private int state = REFRESH_NORMAL;
    private List<WaresBean.ListBean> data;

    public ThreePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        View view = View.inflate(context, R.layout.three_pager, null);
        mrl_three = (MaterialRefreshLayout) view.findViewById(R.id.mrl_three);
        rv_three = (RecyclerView) view.findViewById(R.id.rv_three);
        initListener();
        state = REFRESH_NORMAL;
        //getDataFromNet();
        switchRefreshState();
        fl_base.removeAllViews();
        fl_base.addView(view);
    }

    private void getDataFromNet() {
        String url = "http://112.124.22.238:8081/course_api/wares/hot?pageSize=" + pagerSize + "&curPage=" + curPage;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                LogUtil.e("onError  " + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                LogUtil.e("onResponse  " + response);
                proceessJson(response);
            }
        });
    }

    private void proceessJson(String response) {
        WaresBean bean = parseJson(response);
        totalPage = bean.getTotalPage();
        currentPage = bean.getCurrentPage();
        if (state == REFRESH_NORMAL) {
            list = bean.getList();
            initRecycler();
        }
        if (state == REFRESH_DOWN) {
            list = bean.getList();
            mWaresAdapter.refreshItem(list);
            state = REFRESH_NORMAL;
        }
        if (state == REFRESH_MORE) {
            data = bean.getList();
            mWaresAdapter.addItem(data);
            mrl_three.finishRefreshLoadMore();
            state = REFRESH_NORMAL;
        }
    }

    private WaresBean parseJson(String response) {
        return new Gson().fromJson(response, WaresBean.class);
    }

    private void initRecycler() {
        mWaresAdapter = new WaresAdapter(context, list);
        rv_three.setAdapter(mWaresAdapter);
        rv_three.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    public void switchRefreshState() {
        switch (state) {
            case REFRESH_DOWN:
                curPage = 1;
                getDataFromNet();
                break;
            case REFRESH_NORMAL:
                curPage = 1;
                getDataFromNet();
                break;
            case REFRESH_MORE:
                getDataFromNet();
                break;
        }
    }

    private void initListener() {
        //自动刷新，自动加载更多
//        mrl_three.autoRefresh();//drop-down refresh automatically
//        mrl_three.autoRefreshLoadMore();// pull up refresh automatically
        mrl_three.setMaterialRefreshListener(new MaterialRefreshListener() {
                                                 @Override
                                                 public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                                                     if (state != REFRESH_DOWN) {
                                                         state = REFRESH_DOWN;
                                                         switchRefreshState();
                                                     }
                                                     mrl_three.finishRefresh();
                                                 }

                                                 @Override
                                                 public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                                                     if (currentPage < totalPage && state != REFRESH_MORE) {
                                                         curPage += 1;
                                                         state = REFRESH_MORE;
                                                         LogUtil.e("onRefreshLoadMore()");
                                                         switchRefreshState();
                                                     } else {
                                                         mrl_three.finishRefreshLoadMore();
                                                         Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show();
                                                     }

                                                 }

                                                 @Override
                                                 public void onfinish() {
                                                     Toast.makeText(context, "刷新完成", Toast.LENGTH_SHORT).show();
                                                 }
                                             }

        );
    }
}