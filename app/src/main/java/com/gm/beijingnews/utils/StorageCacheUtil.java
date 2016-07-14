package com.gm.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.xutils.common.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/7/5.
 */
public class StorageCacheUtil {

    //    /**
//     * 内部存储路径
//     */
//    public static final String picInPath = Environment.getExternalStorageDirectory() + "gjnews";
    private MemoryCacheUtil mMemoryCacheUtil;

    public StorageCacheUtil(MemoryCacheUtil mMemoryCacheUtil) {
        this.mMemoryCacheUtil = mMemoryCacheUtil;
        service = Executors.newFixedThreadPool(5);
    }

    public Bitmap getBitmapFromSD(String path) {
        //LogUtil.e("SD" + FileUtil.isExternalExsit());
        Bitmap bitmap = null;
        FileInputStream fis = null;
        if (FileUtil.isExternalExsit()) {
            String picName = "";
            try {
                picName = MD5Encoder.encode(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            File file = new File(Constact.PICPATH, picName);
            LogUtil.e("读取路径" + file.getAbsolutePath());
//            File parentFile = file.getParentFile();
//            if (!parentFile.exists()) {
//                parentFile.mkdirs();
//            }
            try {
                fis = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(fis);
                //存储到内存
                mMemoryCacheUtil.putBitmap2Memory(path, bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return bitmap;
    }

    //线程池
    private ExecutorService service;

    public void putBitmap2SD(final String path, final Bitmap bitmap) {
        //LogUtil.e("SD" + FileUtil.isExternalExsit());
        service.execute(new MyRunnable(path, bitmap));
    }

    class MyRunnable implements Runnable {
        private String path;
        private Bitmap bitmap;

        public MyRunnable(String path, Bitmap bitmap) {
            this.path = path;
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            if (FileUtil.isExternalExsit() && bitmap != null) {
                FileOutputStream fos = null;
                String picName = "";
                try {
                    picName = MD5Encoder.encode(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                File file = new File(Constact.PICPATH, picName);
                LogUtil.e("存储路径" + file.getAbsolutePath());
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }
}
