package com.gm.beijingnews.utils;

import android.os.Environment;

/**
 * Created by Administrator on 2016/6/29.
 */
public class Constact {
    public static final String BASE_URL = "http://192.168.129.1:8888";
    public static final String JSON_NET_HOSTNAME = "http://192.168.129.1:8888/zhbj";
    public static final String JSON_FILENAME = "/categories.json";
    public static final String JSON_NET_URL = JSON_NET_HOSTNAME + JSON_FILENAME;
    public static final String JSON_PHOTO_NAME = "/photos/photos_1.json";
    public static final String JSON_NET_PHOTOS_URL = JSON_NET_HOSTNAME + JSON_PHOTO_NAME;

    public static final String TAG_VIEWPAGER = "viewpager";
    public static final String TAG_LISTVIEW = "listview";

    public static final int CACHE_SUCCESS = 3;
    public static final int CACHE_FAIL = 4;


    /**
     * 外部存储路径
     */
    public static final String PICPATH = Environment.getExternalStorageDirectory() + "/bjnews";

    public static final String SECONDPAGER = "secondpager";
    public static final String NEWMENUPAGER_DATA = "newmenupagerdata";
    public static final String NEWMENUPAGER_POSITION = "newmenupagerposition";
//    public static final String SECONDPAGER="secondpager";
}
