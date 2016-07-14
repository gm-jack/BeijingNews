package com.gm.beijingnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.PopupWindow;

import com.gm.beijingnews.R;

/**
 * Created by Administrator on 2016/7/6.
 */
public class MyPopupWindow extends PopupWindow {
    private Context context;
    public MyPopupWindow(Context context) {
        this(context, null);
    }

    public MyPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setAnimationStyle(R.style.AnimHead);
    }

}
