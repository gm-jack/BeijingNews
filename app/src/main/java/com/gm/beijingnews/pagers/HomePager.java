package com.gm.beijingnews.pagers;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gm.beijingnews.R;
import com.gm.beijingnews.adapter.RecyAdapter;
import com.gm.beijingnews.base.BasePager;
import com.gm.beijingnews.domain.VideoData;
import com.gm.beijingnews.utils.Constact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 * 首页
 */
public class HomePager extends BasePager {
    private RecyclerView recycler_home;
    private SwipeRefreshLayout srl_home;
    private List<VideoData> list;
    private String filename = "/2.avi";

    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        View view = View.inflate(context, R.layout.home_pager, null);
        srl_home = (SwipeRefreshLayout) view.findViewById(R.id.srl_home);
        initSwipelayout();
        recycler_home = (RecyclerView) view.findViewById(R.id.recycler_home);
        initRecycler();
        fl_base.removeAllViews();
        fl_base.addView(view);
    }

    private void initSwipelayout() {
        srl_home.setProgressViewOffset(true, 20, 100);
        srl_home.setDistanceToTriggerSync(100);
        srl_home.setColorSchemeColors(android.R.color.black, android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_green_light);
        srl_home.setRefreshing(false);
        srl_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_home.setRefreshing(false);
            }
        });
    }

    private void initRecycler() {
        recycler_home.setLayoutManager(new GridLayoutManager(context, 2));
        initVideoData();
        recycler_home.setAdapter(new RecyAdapter(context, list));
    }

    private void initVideoData() {
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new VideoData("视频" + i, Constact.BASE_URL + filename));
        }

    }
}
