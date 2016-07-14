package com.gm.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/6/28.
 */
public class DataConvert {


    public static int MENU_NEWS_POSTION = 0;
    public static final String NAME = "isfirst";
    public static final String GUIDE = "guide";
    public static final String TAG_NET = "tagnet";
    public static final String TAG_TOPVIEWPAGER = "topview";

    public static void saveData(Context context, String key, boolean isFirst) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, isFirst).commit();
    }

    public static boolean getData(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        boolean result = sp.getBoolean(key, false);
        return result;
    }

    public static void saveData(Context context, String key, String data, String spName) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putString(key, data).commit();
    }

    public static String getData(Context context, String key, String spName) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        String result = sp.getString(key, "");
        return result;
    }
//    public static void saveTopData(Context context, String key, String data) {
//        SharedPreferences sp = context.getSharedPreferences(TAG_TOPVIEWPAGER, Context.MODE_PRIVATE);
//        sp.edit().putString(key, data).commit();
//    }
//
//    public static String getTopData(Context context, String key) {
//        SharedPreferences sp = context.getSharedPreferences(TAG_TOPVIEWPAGER, Context.MODE_PRIVATE);
//        return sp.getString(key, "");
//    }

//    public static void saveTagData(Context context, String sp_name, String key, String data) {
//        SharedPreferences sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
//        sp.edit().putString(key, data).commit();
//    }
//
//    public static String getTagData(Context context, String sp_name, String key) {
//        SharedPreferences sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
//        return sp.getString(key, "");
//    }

    /**
     * 将数据保存到文件中
     *
     * @param context
     * @param key
     * @param data
     */
    public static void saveFileData(Context context, String key, String data, String sp_name) {
        String filePath;
        String fileName = key;
//        try {
//            fileName = MD5Encoder.encode(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (FileUtil.isExternalExsit()) {
            //sd卡
            filePath = Constact.PICPATH;
        } else {
            //sp存储
            SharedPreferences sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
            sp.edit().putString(key, data).commit();

            //内部存储
            filePath = context.getFilesDir() + "bjnews";
//            File file = new File(filesDir, fileName);
//            File parentFile = file.getParentFile();
        }
        File file = new File(filePath, fileName);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
//            ByteArrayInputStream bis=new ByteArrayInputStream();
//            bis.
//            ByteArrayOutputStream baos;
        try {
//                baos=new ByteArrayOutputStream();
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从文件中获取数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String getFileData(Context context, String key, String sp_name) {
        String filePath;
        String fileName = key;
//        try {
//            fileName = MD5Encoder.encode(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (FileUtil.isExternalExsit()) {
            //sd卡
            filePath = Constact.PICPATH;
        } else {

            //内部存储
            filePath = context.getFilesDir() + "bjnews";
//            File file = new File(filesDir, fileName);
//            File parentFile = file.getParentFile();
            //sp存储
            SharedPreferences sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
            return sp.getString(key, "");
        }
        File file = new File(filePath, fileName);
        if (!file.exists()) {
            return "";
        }
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            byte[] bytes = new byte[1024];
            baos = new ByteArrayOutputStream();
            fis = new FileInputStream(file);
            int len = -1;
            while ((len = fis.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        //sp存储
//        SharedPreferences sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
//        return sp.getString(key, "");
        return baos.toString();
    }
}
