package com.gm.beijingnews.utils;

/**
 * Created by Administrator on 2016/7/1.
 */
public class WebsiteUtil {
    public static String WebsiteChange(String url){
        //LogUtil.e(url.replace("10.0.2.2:8080", "172.20.43.1:8888")+"--------");
        return url.replace("10.0.2.2:8080", "192.168.129.1:8888");
    }
}
