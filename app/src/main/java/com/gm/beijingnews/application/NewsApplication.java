package com.gm.beijingnews.application;

import android.app.Application;

import com.gm.beijingnews.volley.VolleyManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/6/29.
 */
public class NewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //异常处理
//        CrashHandler mCrashHandler = CrashHandler.getInstance();
//        mCrashHandler.init(this);
        //初始化XUtils
        x.Ext.init(this);
        x.Ext.setDebug(true);
        //初始化ImageLoader
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(config);
        //初始化Volley
        VolleyManager.init(this);
        //初始化极光推送
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }
}
