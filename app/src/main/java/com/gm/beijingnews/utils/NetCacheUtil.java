package com.gm.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import org.xutils.common.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/5.
 */
public class NetCacheUtil {
    private int position;
    private Handler handler;
    private MemoryCacheUtil mMemoryCacheUtil;
    private String path;
    //线程池
    private ExecutorService service;
    private Bitmap bitmap;
    private StorageCacheUtil mStorageCacheUtil;

    public NetCacheUtil(Handler handler, int position, StorageCacheUtil mStorageCacheUtil, MemoryCacheUtil mMemoryCacheUtil) {
        this.mStorageCacheUtil = mStorageCacheUtil;
        this.mMemoryCacheUtil = mMemoryCacheUtil;
        this.handler = handler;
        this.position = position;
        service = Executors.newFixedThreadPool(10);
    }

    public void getBitmapFromNet(String path) {
        this.path = path;
        service.execute(new MyRunnable());
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            LogUtil.e("NET=" + path);
            HttpURLConnection conn = null;
            InputStream is = null;
            try {
                URL url = new URL(path);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    LogUtil.e("网络获取");
                    is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    sendMessage(Constact.CACHE_SUCCESS, position);
                    //保存到内存
                    mMemoryCacheUtil.putBitmap2Memory(path, bitmap);
                    //保存到SD卡
                    mStorageCacheUtil.putBitmap2SD(path, bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                sendMessage(Constact.CACHE_FAIL, position);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //OkHttpUtils.get().url(path).build().en(new BitmapCallback() {
//            @Override
//            public void onError(Request request, Exception e) {
//                LogUtil.e("onError()" + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Bitmap response) {
//                bitmap = response;
//                sendMessage(Constact.CACHE_SUCCESS, position);
//                //保存到内存
//                mMemoryCacheUtil.putBitmap2Memory(path, bitmap);
//                //保存到SD卡
//                mStorageCacheUtil.putBitmap2SD(path, bitmap);
//            }
//        });
        }
    }

    private void sendMessage(int cacheSuccess, int position) {
        Message msg = Message.obtain();
        msg.what = cacheSuccess;
        msg.arg1 = position;
        if (cacheSuccess == Constact.CACHE_SUCCESS) {
            msg.obj = bitmap;
        }
        handler.sendMessage(msg);
    }
}
