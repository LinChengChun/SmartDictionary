package com.example.trim.smartdictionary.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.trim.smartdictionary.utils.LogUtiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by cclin on 2016/9/26.
 */

public class NavigationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_PROGRESS);
//        setContentView(R.layout.activity_navigation);
        TextView tvVersion = new TextView(this);
        tvVersion.setText("版本号: 1.0");
        tvVersion.setTextSize(30.0f);
        tvVersion.setTextColor(Color.WHITE);
        tvVersion.setGravity(Gravity.CENTER);
        tvVersion.setBackgroundColor(Color.TRANSPARENT);

        LinearLayout linearLayout = new LinearLayout(this);
        ProgressBar pbLoading = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        pbLoading.setBackgroundColor(Color.TRANSPARENT);
        pbLoading.setVisibility(View.VISIBLE);
        pbLoading.setIndeterminate(true);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        linearLayout.addView(pbLoading, lp);
        linearLayout.setGravity(Gravity.CENTER);
//        linearLayout.setBackgroundColor(Color.TRANSPARENT);

        ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        ViewGroup contentParent = (ViewGroup) decorView.findViewById(android.R.id.content);
        LogUtiles.d("decorView = "+decorView);
        LogUtiles.d("contentParent = "+contentParent);

        LogUtiles.d("getChildCount = "+decorView.getChildCount());
        for (int i=0; i<decorView.getChildCount(); i++){
            LogUtiles.d("getChildAt("+i+"):"+decorView.getChildAt(i));
            decorView.removeViewAt(i);
        }
        decorView.addView(linearLayout);
        decorView.addView(tvVersion);

//        decorView.addView(pbLoading, 1);
//        decorView.addView(pbLoading, 1, params);
//        View view = View.inflate(this, R.layout.activity_navigation, null);
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_navigation, decorView, true);
//        View view = getLayoutInflater().inflate(R.layout.activity_navigation, decorView, false);
//        decorView.addView(view);

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
