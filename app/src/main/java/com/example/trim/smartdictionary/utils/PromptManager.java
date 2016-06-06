package com.example.trim.smartdictionary.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.trim.smartdictionary.config.Config;


/**
 * 提示信息的管理
 */
public class PromptManager {

    /**
     * 显示一个短oast
     */
    public static void showShortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示一个短Toast
     */
    public static void showShortToast(Context context, int msgResId) {
        Toast.makeText(context, msgResId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示一个长oast
     */
    public static void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示一个长Toast
     */
    public static void showLongToast(Context context, int msgResId) {
        Toast.makeText(context, msgResId, Toast.LENGTH_LONG).show();
    }

    /**
     * 测试用,在正式投入市场：删
     */
    public static void showToastTest(Context context, String msg) {
        if (Config.IS_DEBUG) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
