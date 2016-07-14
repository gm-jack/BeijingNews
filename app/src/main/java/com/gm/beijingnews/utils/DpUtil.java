package com.gm.beijingnews.utils;

import android.content.Context;

/**
 * Created by Administrator on 2016/6/7.
 */
public class DpUtil {
//    private static float scale;

    /**
     * dp转换px
     */
    public static int dp2px(Context context, float dpValue) {
//        if(scale==0){
//
//        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }

    /**
     * px转换dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
//        if(scale==0){
//
//        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);

    }
}
