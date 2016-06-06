package com.example.trim.smartdictionary.utils;

import com.lidroid.xutils.HttpUtils;

/**
 * Created by Aaron on 2015/11/11.
 */
public class HttpHelper {

    private static HttpUtils mHttpUtils = null;

    public static HttpUtils getHttpUtils() {
        if (mHttpUtils == null) {
            synchronized (HttpHelper.class) {
                if (mHttpUtils == null) {
                    mHttpUtils = new HttpUtils();
                    mHttpUtils.configSoTimeout(10 * 1000)//
                            .configResponseTextCharset("UTF-8")//
                            .configTimeout(10 * 1000)//
                            .configCurrentHttpCacheExpiry(500);// 设置缓存1秒,1秒内直接返回上次成功请求的结果。
                }
            }
        }
        return mHttpUtils;
    }

}
