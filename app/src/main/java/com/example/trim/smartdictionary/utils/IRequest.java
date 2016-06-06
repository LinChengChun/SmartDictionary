package com.example.trim.smartdictionary.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.trim.smartdictionary.view.LoadingFragment;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 发送工具类请求
 */
public class IRequest {

    /**
     * 发送Get请求,不显示Dialog
     */
    public static HttpHandler get(String url, final OnRequestStringListener onRequestStringListener) {
        return HttpHelper.getHttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (onRequestStringListener != null) {
                    onRequestStringListener.onSuccess(responseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (onRequestStringListener != null) {
                    onRequestStringListener.onFailure();
                }
            }

            @Override
            public void onStart() {
                if (onRequestStringListener != null) {
                    onRequestStringListener.onStart();
                }
            }
        });
    }

    /**
     * 发送Get请求,不显示Dialog
     */
    public static <T> HttpHandler get(String url, final Class<T> clazz, final OnRequestJsonListener<T> onRequestJsonListener) {
        Log.i("jim","1====请求url = "+url);
        return HttpHelper.getHttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (onRequestJsonListener != null) {
                    onRequestJsonListener.onSuccess((T) JsonUtils.fromJson(responseInfo.result, clazz));
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (onRequestJsonListener != null) {
                    onRequestJsonListener.onFailure();
                }
            }

            @Override
            public void onStart() {
                if (onRequestJsonListener != null) {
                    onRequestJsonListener.onStart();
                }
            }
        });
    }

    /**
     * 发送Get请求,显示Dialog
     */
    public static HttpHandler get(final Context context, String url, String progressTitle,
                                  final OnRequestStringListener onRequestStringListener) {
    	
    	
    	 Log.i("jim","2====发送Get请求,显示Dialog 请求url = "+url);
    	
        // 正在加载的Dialog
        final LoadingFragment loadingFragment = new LoadingFragment();
        if (!TextUtils.isEmpty(progressTitle)) {
            loadingFragment.setMsg(progressTitle);
        }

        return HttpHelper.getHttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (loadingFragment.getShowsDialog()) {
                    loadingFragment.dismiss();
                }
                if (onRequestStringListener != null) {
                    onRequestStringListener.onSuccess(responseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (loadingFragment.getShowsDialog()) {
                    loadingFragment.dismiss();
                }
                if (onRequestStringListener != null) {
                    onRequestStringListener.onFailure();
                }
            }

            @Override
            public void onStart() {
                loadingFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "Loading");
                if (onRequestStringListener != null) {
                    onRequestStringListener.onStart();
                }
            }

            @Override
            public void onCancelled() {
                if (loadingFragment.getShowsDialog()) {
                    loadingFragment.dismiss();
                }
            }
        });

    }

    /**
     * 发送Get请求,显示Dialog
     */
    public static <T> HttpHandler get(final Context context, String url, String progressTitle, final Class<T> clazz,
                                      final OnRequestJsonListener<T> onRequestJsonListener) {
        // 正在加载的Dialog
        final LoadingFragment loadingFragment = new LoadingFragment();
        if (!TextUtils.isEmpty(progressTitle)) {
            loadingFragment.setMsg(progressTitle);
        }

        return HttpHelper.getHttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (loadingFragment.getShowsDialog()) {
                    loadingFragment.dismiss();
                }
                if (onRequestJsonListener != null) {
                    onRequestJsonListener.onSuccess((T) JsonUtils.fromJson(responseInfo.result, clazz));
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (loadingFragment.getShowsDialog()) {
                    loadingFragment.dismiss();
                }
                if (onRequestJsonListener != null) {
                    onRequestJsonListener.onFailure();
                }
            }

            @Override
            public void onStart() {
                loadingFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "Loading");
                if (onRequestJsonListener != null) {
                    onRequestJsonListener.onStart();
                }
            }

            @Override
            public void onCancelled() {
                if (onRequestJsonListener != null) {
                    onRequestJsonListener.onFailure();
                }
            }
        });

    }

    /**
     * 请求返回字符串
     */
    public interface OnRequestStringListener {
        public void onSuccess(String result);

        public void onFailure();

        public void onStart();
    }

    /**
     * 请求返回字符串
     */
    public interface OnRequestJsonListener<T> {
        public void onSuccess(T result);

        public void onFailure();

        public void onStart();
    }

}
