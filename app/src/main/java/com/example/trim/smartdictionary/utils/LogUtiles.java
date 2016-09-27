package com.example.trim.smartdictionary.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/6/4.
 */
public class LogUtiles {

    private static Boolean isDebug = true;
    private final static String TAG = "cclin";
    public static void i(String msg){
        if (isDebug)
            Log.i(TAG, msg);
    }
    public static void d(String msg){
        if (isDebug)
            Log.d(TAG, msg);
    }
}
