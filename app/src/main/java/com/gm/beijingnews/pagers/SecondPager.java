package com.gm.beijingnews.pagers;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.gm.beijingnews.R;
import com.gm.beijingnews.activity.MainActivity;
import com.gm.beijingnews.base.BasePager;
import com.gm.beijingnews.base.MenuDetailBaePager;
import com.gm.beijingnews.domain.NewsData;
import com.gm.beijingnews.fragment.MenuFragment;
import com.gm.beijingnews.utils.Constact;
import com.gm.beijingnews.utils.DataConvert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 * 新闻界面
 */
public class SecondPager extends BasePager {
    private List<MenuDetailBaePager> menuList;
    private int currentPosition;
    private NewsData newsData;

    public SecondPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        tv_base.setText("SecondPager");
        iv_base.setVisibility(View.VISIBLE);
        iv_base.setOnClickListener(new MyOnClickListener());
        LogUtil.e("SecondPager初始化");

        String fileData = DataConvert.getFileData(context, Constact.SECONDPAGER, "sp_" + Constact.SECONDPAGER);
        //LogUtil.e("fileData==" + fileData);
        if (!TextUtils.isEmpty(fileData)) {
            progressData(fileData);
        }
        obtainJsonData();
    }

    private void initMenu() {
        menuList = new ArrayList<>();
        menuList.add(new NewsDetailPager(context, leftData.get(0).getChildren()));
        menuList.add(new SpecialDetailPager(context, leftData.get(0).getChildren()));
        menuList.add(new GroupDetailPager(context));
        menuList.add(new InteractDetailPager(context));
    }

    private void obtainJsonData() {
        RequestParams entity = new RequestParams(Constact.JSON_NET_URL);
        x.http().get(entity, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result:  " + result);
                //保存数据到SD卡
                DataConvert.saveFileData(context, Constact.SECONDPAGER, result, "sp_" + Constact.SECONDPAGER);
                progressData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("error:  " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("cancel:  " + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished()");
            }
        });
    }

    private void progressData(String result) {
        //解析数据
        newsData = parseJson(result);
        leftData = newsData.getData();
//                LogUtil.e(newsData.toString());
//                LogUtil.e(leftData.get(0).getChildren().toString());
        //需要先初始化显示的视图
        initMenu();
        selectPager(0);
        //将得到的data数据发送侧边栏
        sendData();
    }

    public NewsDetailPager getNewDeatailPager() {
        if (null != menuList) {
            NewsDetailPager mNewsDetailPager = (NewsDetailPager) menuList.get(0);
            return mNewsDetailPager;
        }
        return null;
    }

    /**
     * 菜单选择
     *
     * @param selectPosition
     */
    public void selectPager(int selectPosition) {
        /**
         * 设置标题
         */
        if (null != leftData) {
            tv_base.setText(leftData.get(selectPosition).getTitle());
        }
        MenuDetailBaePager menuDetailBaePager = menuList.get(selectPosition);
        //设置显示视图
        View view = menuDetailBaePager.rootView;
        //设置组图切换按钮显示
        iv_switch.setVisibility(selectPosition == 2 ? View.INVISIBLE : View.GONE);
        if (selectPosition == 2) {
            iv_switch.setVisibility(View.VISIBLE);
            iv_switch.setImageResource(R.drawable.icon_pic_list_type);
            GroupDetailPager groupDetailBaePager1 = (GroupDetailPager) menuList.get(selectPosition);
            groupDetailBaePager1.selectListAndGrid(iv_switch);
        }
        if (selectPosition != 0) {
            NewsDetailPager mNewsDetailPager = (NewsDetailPager) menuList.get(0);
            Handler handler = mNewsDetailPager.getNewMenuPager().getHandler();
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        }
        //调用当前视图的initData()初始化数据加载（**）
        menuDetailBaePager.initData();
        currentPosition = selectPosition;
        fl_base.removeAllViews();
        fl_base.addView(view);

    }

    //Activity对象
    private MainActivity mainActivity;
    //侧边栏数据
    private List<NewsData.Data> leftData;

    private void sendData() {

        mainActivity = (MainActivity) context;
        //从activity获取侧边栏
        MenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        leftMenuFragment.setData(leftData);
    }

    private NewsData parseJson(String result) {
        /**
         * json对象数据
         */
        NewsData newsData = new NewsData();
        try {
            JSONObject jsonObject = new JSONObject(result);
            String retcode = jsonObject.optString("retcode");
            newsData.setRetcode(retcode);
            JSONArray extend = jsonObject.optJSONArray("extend");
            if (extend != null && extend.length() > 0) {
                /**
                 * 存储extend数据
                 */
                List<Integer> extendList = new ArrayList<>();
                newsData.setExtend(extendList);
                for (int i = 0; i < extend.length(); i++) {
                    extendList.add((Integer) extend.get(i));
                }
            }
            JSONArray data = jsonObject.optJSONArray("data");
            if (data != null && data.length() > 0) {
                /**
                 * 存储data数据
                 */
                List<NewsData.Data> dataList = new ArrayList<>();
                newsData.setData(dataList);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = data.optJSONObject(i);
                    if (item != null && data.length() > 0) {
                        NewsData.Data dataItem = new NewsData.Data();
                        dataList.add(dataItem);

                        dataItem.setId(item.optInt("id"));
                        dataItem.setTitle(item.optString("title"));
                        dataItem.setType(item.optInt("type"));
                        dataItem.setUrl(item.optString("url"));
                        dataItem.setUrl1(item.optString("url1"));
                        dataItem.setDayurl(item.optString("dayurl"));
                        dataItem.setExcurl(item.optString("excurl"));
                        dataItem.setWeekurl(item.optString("weekurl"));

                        JSONArray children = item.optJSONArray("children");
                        if (children != null && children.length() > 0) {

                            List<NewsData.Data.Children> childrenData = new ArrayList<>();
                            dataItem.setChildren(childrenData);
                            for (int j = 0; j < children.length(); j++) {
                                JSONObject childrenItem = children.optJSONObject(j);
                                if (childrenItem != null) {
                                    NewsData.Data.Children childrenDatas = new NewsData.Data.Children();
                                    childrenData.add(childrenDatas);
                                    childrenDatas.setId(childrenItem.optInt("id"));
                                    childrenDatas.setTitle(childrenItem.optString("title"));
                                    childrenDatas.setType(childrenItem.optInt("type"));
                                    childrenDatas.setUrl(childrenItem.optString("url"));
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsData;
    }

//    public NewMenuPager getNewMenuPager() {
//        return
//    }


    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mainActivity.getSlidingMenu().toggle();
        }
    }
}
