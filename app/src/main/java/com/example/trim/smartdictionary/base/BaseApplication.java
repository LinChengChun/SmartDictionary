package com.example.trim.smartdictionary.base;

import android.app.Application;
import android.os.Handler;


/**
 * 自定义Application
 */
public class BaseApplication extends Application {

    private static BaseApplication mApplication;
    // 保存主线程ID
    private static int mainTid;
    // 用于处理主线程消息
    private static Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mainTid = android.os.Process.myTid();
        mHandler = new Handler();

        // 1、设置视频播放SDK的log是否输出到logcat中，默认是不输出到logcat中的。
        //TVK_SDKMgr.setDebugEnable(Config.IS_DEBUG);
        // 2、初始化视频SDK
        // 视频SDK需要初始化之后才能使用，相当于一次鉴权，如果不调用，则无法进行后续的播放操作。
        //TXDeviceService.initTencentVideoSDK(this);
        // 初始化异常捕获
        //CatchExceptionHandler.getInstance().init(getApplicationContext());
    }

    public static BaseApplication getApplication() {
        return mApplication;
    }

    public static int getMainTid() {
        return mainTid;
    }

    public static Handler getHandler() {
        return mHandler;
    }

}
