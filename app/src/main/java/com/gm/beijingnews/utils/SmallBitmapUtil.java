package com.gm.beijingnews.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.LruCache;

/**
 * Created by Administrator on 2016/7/9.
 */
public class SmallBitmapUtil {
    private LruCache<String, Bitmap> lrucache;

    public SmallBitmapUtil() {
        int maxSize = (int) Runtime.getRuntime().maxMemory() / 1024 / 8;
        lrucache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 获取缩略图
     * 出现GC_FOR_ALLOC
     */
    public LruCache<String, Bitmap> getThumb(String path) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        Bitmap bitmap1 = bitmap.createBitmap(bitmap, 0, 0, DpUtil.dp2px(context, 100), DpUtil.dp2px(context, 100));
//        if (null != bitmap && !bitmap.isRecycled()) {
//            bitmap.recycle();
//        }
        if(bitmap!=null){
            lrucache.put(path,bitmap);
        }
        return lrucache;
    }
}
