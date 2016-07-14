package com.gm.beijingnews.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Administrator on 2016/7/5.
 */
public class MemoryCacheUtil {
    //得到系统运行内存，转换为kb，再取1/8
    private int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
    private LruCache<String, Bitmap> mLruCache;

    public MemoryCacheUtil() {
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            /**
             * 计算每一张图片的大小
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public Bitmap getBitmapFromMemory(String path) {
        Bitmap bitmap = mLruCache.get(path);
        if (bitmap != null) {
            //LogUtil.e("内存获取");
            return bitmap;
        }
        return null;
    }

    public void putBitmap2Memory(String path, Bitmap bitmap) {
        if (bitmap != null) {
            mLruCache.put(path, bitmap);
        }
    }
}
