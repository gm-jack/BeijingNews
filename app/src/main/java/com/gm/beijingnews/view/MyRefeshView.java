package com.gm.beijingnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gm.beijingnews.R;

import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/2.
 */
public class MyRefeshView extends ListView {

    private int footerHeight;
    private int headerHeight;
    private View topNew;
    private RotateAnimation upAnimation;
    private RotateAnimation downAnimation;
    private View footView;

    public MyRefeshView(Context context) {
        this(context, null);
    }

    public MyRefeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 初始化头部和底部布局
         */
        initHeaderView(context);
        initFooterView(context);
//        /**
//         * header箭头动画
//         */
//        initAnimation();
    }

    private LinearLayout ll_pull_down_refresh;
    private ImageView iv_arrow;
    private ProgressBar pb_statu;
    private TextView tv_statu;
    private TextView tv_time;

    private void initHeaderView(Context context) {
        View view = View.inflate(context, R.layout.view_header_refresh, null);
        ll_pull_down_refresh = (LinearLayout) view.findViewById(R.id.ll_pull_down_refresh);
        pb_statu = (ProgressBar) view.findViewById(R.id.pb_statu);
        tv_statu = (TextView) view.findViewById(R.id.tv_statu);
        iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_time.setText(getSystemTime());
        ll_pull_down_refresh.measure(0, 0);
        headerHeight = ll_pull_down_refresh.getMeasuredHeight();
        ll_pull_down_refresh.setPadding(0, -headerHeight, 0, 0);
        addHeaderView(view);
    }

    private String pretime;

    private String getSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private boolean isLoadMore = false;

    private void initFooterView(Context context) {
        footView = View.inflate(context, R.layout.view_footer_refresh, null);
        footView.measure(0, 0);
        footerHeight = footView.getMeasuredHeight();
        footView.setPadding(0, -footerHeight, 0, 0);
        addFooterView(footView);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                    LogUtil.e(getAdapter().getCount() + "   " + getLastVisiblePosition());
                    if (getLastVisiblePosition() == (getAdapter().getCount() - 1)) {
                        view.setPadding(0, 0, 0, 0);
                        isLoadMore = true;
                        LogUtil.e("加载更多");
                        if (mOnRefreshListListener != null)
                            mOnRefreshListListener.onMoreRefresh();
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private float lastY = 0;
    /**
     * 下拉刷新状态
     */
    private static final int REFRESH_DOWN = 1;

    /**
     * 松手刷新状态
     */
    private static final int REFRESH_DOWN_UP = 2;
    /**
     * 正在刷新状态
     */
    private static final int REFRESHING = 3;
    /**
     * 空闲状态
     */
    private static final int REFRESH_AIDL = -1;
    /**
     * 当前刷新状态
     */
    private int current_state = REFRESH_AIDL;
    /**
     * 当前手势偏移比例
     */
    private float mScrollY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (current_state == REFRESHING) {
                    break;
                }
                /**
                 * 处理当listview滑到下边，topnew不显示在屏幕上而去执行下拉刷新
                 */
                if (!isDisplayYopNews()) {
                    break;
                }
                float dy = ev.getY() - lastY;

                if (dy > 0) {
                    if (dy > headerHeight) {
                        dy = headerHeight;
                    }

                    int topPadding = (int) (-headerHeight + dy);
                    mScrollY = dy / headerHeight;
                    //LogUtil.e("headerHeight=" + headerHeight + "    mScrollY=" + mScrollY + "    dy=" + dy);
                    if (current_state != REFRESH_DOWN_UP) {
                        //LogUtil.e("移动量" + dy);
                        current_state = REFRESH_DOWN;
                        refreshAnimation(mScrollY);
                        LogUtil.e("下拉刷新");
                    }
                    if (topPadding >= 0 && current_state != REFRESH_DOWN_UP) {
                        current_state = REFRESH_DOWN_UP;
                        refreshAnimation(0);
                        LogUtil.e("松手操作");
                    }
                    //设置header位置
                    ll_pull_down_refresh.setPadding(0, topPadding, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                lastY = 0;
                if (current_state == REFRESH_DOWN) {
                    ll_pull_down_refresh.setPadding(0, -headerHeight, 0, 0);
                } else if (current_state == REFRESH_DOWN_UP) {
                    current_state = REFRESHING;
                    refreshAnimation(0);
                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);
                    if (mOnRefreshListListener != null) {
                        mOnRefreshListListener.onDownRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 保存旋转角度
     */
    private float rotateY;

//    private void initAnimation() {
//
//        upAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        upAnimation.setDuration(20);
//        upAnimation.setFillAfter(true);
//        downAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        downAnimation.setDuration(20);
//        downAnimation.setFillAfter(true);
//
//    }

    private void refreshAnimation(float lastScroolY) {
        switch (current_state) {
            case REFRESH_DOWN:
                tv_statu.setText("下拉刷新");
                if (rotateY < 0) {
                    rotateY = 0;
                }
                upAnimation = new RotateAnimation(rotateY, 180 * lastScroolY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                upAnimation.setDuration(1);
                upAnimation.setFillAfter(true);
                iv_arrow.startAnimation(upAnimation);
                //LogUtil.e("last=" + rotateY + "    new=" + 180 * lastScroolY);
                rotateY = 180 * lastScroolY;
                break;
            case REFRESH_DOWN_UP:
                tv_statu.setText("松手刷新");
                rotateY = 0;
                break;
            case REFRESHING:
                LogUtil.e("正在刷新中");
                tv_statu.setText("正在刷新中");
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(View.INVISIBLE);
                pb_statu.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void addTopNews(View topView) {
        this.topNew = topView;
        if (topView != null) {
            addHeaderView(topView);
        }
    }

    private int listviewScreenY = -1;

    /**
     * 当Listview的Y轴坐标小于topnew在屏幕的Y坐标是，topnew完全显示在屏幕上
     *
     * @return
     */
    public boolean isDisplayYopNews() {
        if (topNew != null) {
            int[] location = new int[2];
            if (listviewScreenY == -1) {
                getLocationOnScreen(location);
                listviewScreenY = location[1];
            }
            topNew.getLocationOnScreen(location);
            int topNewY = location[1];
            return topNewY >= listviewScreenY;
        } else {
            return true;
        }
    }

    public void setOnRefreshListListener(OnRefreshListListener onRefreshListListener) {
        this.mOnRefreshListListener = onRefreshListListener;
    }

    private OnRefreshListListener mOnRefreshListListener;

    public void onFinishLoad(boolean success) {
        if (isLoadMore) {
            isLoadMore = false;
            footView.setPadding(0, -footerHeight, 0, 0);
        } else {
            current_state = REFRESH_AIDL;
            ll_pull_down_refresh.setPadding(0, -headerHeight, 0, 0);
            tv_statu.setText("下拉刷新");
            pb_statu.setVisibility(View.GONE);
            iv_arrow.setVisibility(View.VISIBLE);
            if (success) {
                tv_time.setText("上次刷新时间" + pretime);
                pretime = getSystemTime();
            }

        }
    }

    public interface OnRefreshListListener {
        void onDownRefresh();

        void onMoreRefresh();
    }
}
