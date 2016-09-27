package com.example.trim.smartdictionary.fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.adapter.WordsAdapter;
import com.example.trim.smartdictionary.bean.WordInfo;
import com.example.trim.smartdictionary.utils.LogUtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cclin on 2016/9/26.
 */

public class DbSearchFragment extends Fragment implements View.OnClickListener{

    private static String path = "data/data/com.example.trim.smartdictionary/files/font.ttf";
    private View mDbSearchFragment = null; // 定义一个视图组件
    private Activity mActivity; // 上下文
    private EditText etInput;
    private Button btnSearch;
    private ListView lvDatabaseInfo;
    private List<WordInfo> mWordInfos;
    private List<WordInfo> mSearchResultWordInfos;
    private WordsAdapter adapter;
    private InputMethodManager imm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mDbSearchFragment == null){
            mDbSearchFragment = inflater.inflate(R.layout.fragment_database, container, false);
            etInput = (EditText) mDbSearchFragment.findViewById(R.id.et_input);
            btnSearch = (Button) mDbSearchFragment.findViewById(R.id.btn_search);
            lvDatabaseInfo = (ListView) mDbSearchFragment.findViewById(R.id.lv_database_info);
            mActivity = this.getActivity();
            imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE); // 获取系统输入法服务
            mWordInfos = new ArrayList<WordInfo>(); // 初始化集合
            mSearchResultWordInfos = new ArrayList<WordInfo>(); // 初始化集合
            adapter = new WordsAdapter(mActivity, mSearchResultWordInfos);// 实例化适配器
            lvDatabaseInfo.setAdapter(adapter);
            loadWordInfoFromDB();

            btnSearch.setOnClickListener(this);
        }
        return mDbSearchFragment;
    }

    /**
     * 从数据库中加载数据
     */
    private void loadWordInfoFromDB(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!mWordInfos.isEmpty()) mWordInfos.clear(); // 情况集合
                SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
//                Cursor cursor = database.rawQuery("select * from dict where sent like '%<font color=red >"+"b"+"%';", null);
                Cursor cursor = database.rawQuery("select * from dict", null);
                while (cursor.moveToNext()){

                    String symbol = cursor.getString(/*cursor.getColumnIndex("symbol")*/ 0);
                    String explain = cursor.getString(/*cursor.getColumnIndex("explain")*/ 1);
                    String audio = cursor.getString(/*cursor.getColumnIndex("audio")*/ 2);
                    String sent = cursor.getString(/*cursor.getColumnIndex("sent")*/ 3);

//                    LogUtiles.i("symbol = "+symbol);
//                    LogUtiles.i("explain = "+explain);
//                    LogUtiles.i("audio = "+audio);
//                    LogUtiles.i("sent = "+sent);

                    WordInfo wordInfo = new WordInfo();
                    wordInfo.setSymbol(symbol);
                    wordInfo.setExplain(explain);
                    wordInfo.setAudio(audio);
                    wordInfo.setSent(sent);

                    mWordInfos.add(wordInfo);
                }
                cursor.close();
                database.close();

//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                    adapter.notifyDataChange(mWordInfos);
//                    }
//                });
            }
        }).start();
    }

    private synchronized void searchWordInfoFromDB(final String input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!mWordInfos.isEmpty()) mWordInfos.clear(); // 情况集合
                LogUtiles.i("searchWordInfoFromDB start,input = "+input);
                SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
                Cursor cursor = database.rawQuery("select * from dict where sent like '%<font color=red >"
                        +input+"%';", null);
                while (cursor.moveToNext()){

                    String symbol = cursor.getString(/*cursor.getColumnIndex("symbol")*/ 0);
                    String explain = cursor.getString(/*cursor.getColumnIndex("explain")*/ 1);
                    String audio = cursor.getString(/*cursor.getColumnIndex("audio")*/ 2);
                    String sent = cursor.getString(/*cursor.getColumnIndex("sent")*/ 3);

//                    LogUtiles.i("symbol = "+symbol);
//                    LogUtiles.i("explain = "+explain);
//                    LogUtiles.i("audio = "+audio);
//                    LogUtiles.i("sent = "+sent);

                    WordInfo wordInfo = new WordInfo();
                    wordInfo.setSymbol(symbol);
                    wordInfo.setExplain(explain);
                    wordInfo.setAudio(audio);
                    wordInfo.setSent(sent);

                    mWordInfos.add(wordInfo);
                }
                cursor.close();
                database.close();
                LogUtiles.i("searchWordInfoFromDB end");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataChange(mWordInfos);
                    }
                });
            }
        }).start();
    }

    private synchronized void searchWordInfoFromRam(final String input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtiles.i("searchWordInfoFromRam start,input = "+input);
                if (!mSearchResultWordInfos.isEmpty()) mSearchResultWordInfos.clear(); // 清空集合

                for (WordInfo wordInfo : mWordInfos){
                    LogUtiles.i(wordInfo.getSent());
                    if ((wordInfo.getSent().length()>20) && getMainWord(wordInfo.getSent()).contains(input)){
                        LogUtiles.i("get it success");
                        mSearchResultWordInfos.add(wordInfo);
                    }
                }
                LogUtiles.i("searchWordInfoFromRam end");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataChange(mSearchResultWordInfos);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                String input = etInput.getText().toString().trim();
                if (TextUtils.isEmpty(input)){
                    input = "a";
                }
                hideSoftInputMethod();
                searchWordInfoFromRam(input);
                break;
        }
    }

    /**
     * 隐藏输入法
     */
    private void hideSoftInputMethod(){

        boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        LogUtiles.d("isOpen = "+isOpen);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0); //强制隐藏键盘
    }

    private String getMainWord(String text){
        String result = text.substring(text.indexOf("<font color=red >")+17  ,text.indexOf("</font>"));
        return result;
    }
}
