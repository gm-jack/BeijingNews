package com.gm.beijingnews.utils;

import android.os.Environment;

/**
 * Created by Administrator on 2016/7/5.
 */
public class FileUtil {
    public static boolean isExternalExsit() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }
}
