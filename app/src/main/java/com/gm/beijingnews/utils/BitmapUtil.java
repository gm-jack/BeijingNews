package com.gm.beijingnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by Administrator on 2016/7/5.
 */
public class BitmapUtil {
    private NetCacheUtil netcache;
    private StorageCacheUtil mStorageCacheUtil;
    private MemoryCacheUtil mMemoryCacheUtil;

    private String path;

    public BitmapUtil(Handler handler, String path, int currentPosition) {
        this.path = WebsiteUtil.WebsiteChange(path);
        mMemoryCacheUtil = new MemoryCacheUtil();
        mStorageCacheUtil = new StorageCacheUtil(mMemoryCacheUtil);
        netcache = new NetCacheUtil(handler, currentPosition, mStorageCacheUtil, mMemoryCacheUtil);
    }

    public Bitmap getBitmap() {
        //从内存获取
//        Bitmap bitmapFromMemory = mMemoryCacheUtil.getBitmapFromMemory(path);
//        if (bitmapFromMemory != null) {
//            return bitmapFromMemory;
//        }
        //从SD卡获取
        Bitmap bitmapFromSD = mStorageCacheUtil.getBitmapFromSD(path);
        if (bitmapFromSD != null) {
            return bitmapFromSD;
        }
        //从网络获取
//        DisplayImageOptions options = DisplayImageOptions.createSimple();
//        ImageLoader.getInstance().loadImage(path, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                if (bitmap != null) {
//                    //保存到内存
//                    mMemoryCacheUtil.putBitmap2Memory(path, bitmap);
//                    //保存到SD卡
//                    mStorageCacheUtil.putBitmap2SD(path, bitmap);
//
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//
//            }
//        });
        netcache.getBitmapFromNet(path);
        return null;
    }
}
