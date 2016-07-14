package com.gm.beijingnews.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/7/4.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    public MySwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//if(ev.getY()-lastY>)
                break;
        }
        return false;
    }
}
