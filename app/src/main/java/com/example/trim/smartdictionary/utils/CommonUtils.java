package com.example.trim.smartdictionary.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.trim.smartdictionary.base.BaseActivity;
import com.example.trim.smartdictionary.base.BaseApplication;

/**
 * 常用工具类
 */
public class CommonUtils {

    /**
     * 获取到字符数组
     */
    public static String[] getStringArray(int tabNames) {
        return getResource().getStringArray(tabNames);
    }

    /**
     * 获取资源实体
     */
    public static Resources getResource() {
        return BaseApplication.getApplication().getResources();
    }

    public static Context getContext() {
        return BaseApplication.getApplication();
    }

    /**
     * 把Runnable 方法提交到主线程运行
     */
    public static void runOnUiThread(Runnable runnable) {
        // 在主线程运行
        if (android.os.Process.myTid() == BaseApplication.getMainTid()) {
            runnable.run();
        } else {
            // 获取handler
            BaseApplication.getHandler().post(runnable);
        }
    }

    /**
     * 判断当前线程是否是主线程
     */
    public static boolean isRunOnMainThread() {
        return android.os.Process.myTid() == BaseApplication.getMainTid();
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    public static Drawable getDrawabe(int id) {
        return getResource().getDrawable(id);
    }

    public static int getDimens(int homePictureHeight) {
        return (int) getResource().getDimension(homePictureHeight);
    }

    public static String getString(int id) {
        return BaseApplication.getApplication().getResources().getString(id);
    }

    /**
     * 获取颜色id
     */
    public static int getColor(int colorId) {
        return BaseApplication.getApplication().getResources().getColor(colorId);
    }

    /**
     * 延迟执行 任务
     *
     * @param run  任务
     * @param time 延迟的时间
     */
    public static void postDelayed(Runnable run, int time) {
        // 调用Runable里面的run方法
        BaseApplication.getHandler().postDelayed(run, time);
    }

    /**
     * 取消任务
     */
    public static void cancel(Runnable auToRunTask) {
        BaseApplication.getHandler().removeCallbacks(auToRunTask);
    }

    /**
     * 可以打开activity
     */
    public static void startActivity(Intent intent) {
        // 如果不在activity里去打开activity 需要指定任务栈 需要设置标签
        if (BaseActivity.mActivity == null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        } else {
            BaseActivity.mActivity.startActivity(intent);
        }
    }

    /**
     * 获取当前项目包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 设置Dialog的大小
     *
     * @param dialog        目标dialog
     * @param widthPercent  与手机宽度的百分比
     * @param heightPercent 与手机高度的百分
     * @param alpha         dialog 透明度
     */
    public static void setDialogSize(Context context, Dialog dialog, float widthPercent, float heightPercent, float alpha) {
        // 设置dialog的大小
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        params.width = (int) (displayMetrics.widthPixels * widthPercent);
        params.height = (int) (displayMetrics.heightPixels * heightPercent);
        params.alpha = alpha;
        // 设置Dialog的宽度
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * long型整数转换成视频
     */
    public static String coverVideoTime(long time) {
        time /= 1000;
        long minute = time / 60;
        long hour = minute / 60;
        long second = time % 60;
        minute %= 60;
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d:%02d", minute, second);
        }
    }

    /**
     * 显示一个对话框
     *
     * @param context
     * @param titleMsg        对话框标题
     * @param alertMessage    对话框提示信息
     * @param confirmMsg      确定按钮显示内容
     * @param cancelMsg       取消按钮显示内容
     * @param confirmListener 确定点击事件处理
     * @param cancelListener  取消点击事件处理
     */
    public static void showDialog(Context context, String titleMsg, String alertMessage, String confirmMsg, String cancelMsg,
                                  DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
        dialog.setIcon(android.R.drawable.ic_menu_info_details);
        dialog.setCancelable(false);
        dialog.setTitle(titleMsg);
        dialog.setMessage(alertMessage);
        dialog.setPositiveButton(confirmMsg, confirmListener);
        dialog.setNegativeButton(cancelMsg, cancelListener);
        setDialogSize(context, dialog.show(), 0.4f, 0.6f, 1);
    }

    /**
     * 显示一个对话框
     *
     * @param context
     * @param titleResId        对话框标题
     * @param alertMessageResId 对话框提示信息
     * @param confirmMsgResId   确定按钮显示内容
     * @param cancelMsgResId    取消按钮显示内容
     * @param confirmListener   确定点击事件处理
     * @param cancelListener    取消点击事件处理
     */
    public static void showDialog(Context context, int titleResId, int alertMessageResId, int confirmMsgResId, int cancelMsgResId,
                                  DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));
        dialog.setCancelable(false);
        dialog.setIcon(android.R.drawable.ic_menu_info_details);
        dialog.setTitle(titleResId);
        dialog.setMessage(alertMessageResId);
        dialog.setPositiveButton(confirmMsgResId, confirmListener);
        dialog.setNegativeButton(cancelMsgResId, cancelListener);
        dialog.create();
        setDialogSize(context, dialog.show(), 0.4f, 0.6f, 1);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }

    public static boolean isMobileNetworkConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo.isConnected();
    }

    /**
     * 使状态栏透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param activity 需要设置的activity
     */
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     */
    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }
}