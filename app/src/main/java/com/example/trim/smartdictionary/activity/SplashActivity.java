package com.example.trim.smartdictionary.activity;

import android.content.Intent;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.base.BaseActivity;
import com.example.trim.smartdictionary.utils.LogUtiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cclin on 2016/9/26.
 */

public class SplashActivity extends BaseActivity{

    @Override
    protected void initWindow() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                copyDB("font.ttf"); // 拷贝单词数据库
                long endTime = System.currentTimeMillis();
                long diff = endTime - startTime; // 计算时间差值

                if (diff < 1000) {
                    try {
                        Thread.sleep(1000 - diff);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enterHome(); // 进入主页
                    }
                });
            }
        }).start();

    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initView() {
//        CommonUtils.setTranslucent(this);// 使状态栏透明
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_splash;
    }

    /**
     * 进入主页
     */
    private void enterHome() {
        LogUtiles.i("this is enterHome");
        Intent intent = new Intent(this, ViewPagerActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 把address.db拷贝到data/data/com.mobilesafe/files/address.db
     */
    private void  copyDB(String fileName){
        // 假如数据库已经存在了，则不再拷贝该数据库
        try {
            File file = new File(getFilesDir(), fileName);
            if (file.exists() && file.length()>0){
                LogUtiles.i("file "+fileName+" is exists");
            }else {
                // 开始从assets目录下拷贝数据库到data安装目录下
                LogUtiles.i("copyDB is running");
                InputStream is = getAssets().open(fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len = 0;
                while ( (len = is.read(buf, 0, buf.length)) >= 0){
                    fos.write(buf, 0, len);
                }
                fos.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
