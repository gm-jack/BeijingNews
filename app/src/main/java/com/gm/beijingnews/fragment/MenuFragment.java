package com.gm.beijingnews.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gm.beijingnews.R;
import com.gm.beijingnews.activity.MainActivity;
import com.gm.beijingnews.adapter.MyBaseAdapter;
import com.gm.beijingnews.adapter.PubViewHolder;
import com.gm.beijingnews.base.BaseFragment;
import com.gm.beijingnews.domain.NewsData;
import com.gm.beijingnews.pagers.SecondPager;
import com.gm.beijingnews.utils.DpUtil;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/6/28.
 */
public class MenuFragment extends BaseFragment {
    private ListView listview;
    //private List<NewsData.Data> titleList;
    private int selectPosition = 0;
    private MyBaseAdapter<NewsData.Data> mAdapter;
    private MainActivity mainActivity;

    @Override
    public View initView() {
        mainActivity = (MainActivity) context;
//        View view = View.inflate(context, R.layout.left_menu_fragment, null);
        listview = new ListView(context);
        listview.setCacheColorHint(Color.TRANSPARENT);//设置listview在低版本下没有按下效果
        listview.setDividerHeight(0);
        listview.setPadding(DpUtil.dp2px(context, 10), DpUtil.dp2px(context, 20), DpUtil.dp2px(context, 10), 0);
        listview.setSelector(android.R.color.transparent);//设置listview按下是没有效果
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                mAdapter.refreshAdapter();
                //点击关闭侧边栏(关闭-打开；；打开-关闭)
                mainActivity.getSlidingMenu().toggle();
                switchNews();

            }
        });
        return listview;
    }

    private void switchNews() {
        ContentFragment content = mainActivity.getContentFragment();
        SecondPager secondPager = content.getSecondPager();
        secondPager.selectPager(selectPosition);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单初始化");
        mAdapter = new MyBaseAdapter<NewsData.Data>(context, leftData, R.layout.item_menu) {
            @Override
            public void convert(PubViewHolder viewholder, int position) {
                TextView view = viewholder.getView(R.id.tv_menu);
                view.setText(leftData.get(position).getTitle());
                view.setEnabled(selectPosition == position);
            }
        };
        listview.setAdapter(mAdapter);
        //switchNews();
    }

    private List<NewsData.Data> leftData;

    /**
     * 为侧边栏设置数据
     *
     * @param leftData
     */
    public void setData(List<NewsData.Data> leftData) {
        this.leftData = leftData;
        initData();
    }
}
