package com.example.trim.smartdictionary.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DbSearchFragment extends Fragment {

    private static String path = "data/data/com.example.trim.smartdictionary/files/font.ttf";
    private View mDbSearchFragment = null; // 定义一个视图组件
    private Activity mActivity; // 上下文
    private EditText etInput;
    private Button btnSearch;
    private ListView lvDatabaseInfo;
    private List<WordInfo> mWordInfos;
    private WordsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mDbSearchFragment == null){
            mDbSearchFragment = inflater.inflate(R.layout.fragment_database, container, false);
            etInput = (EditText) mDbSearchFragment.findViewById(R.id.et_input);
            btnSearch = (Button) mDbSearchFragment.findViewById(R.id.btn_search);
            lvDatabaseInfo = (ListView) mDbSearchFragment.findViewById(R.id.lv_database_info);
            mActivity = this.getActivity();
            loadWordInfoFromDB();

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
                mWordInfos = new ArrayList<WordInfo>(); // 初始化集合
                SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
                Cursor cursor = database.rawQuery("select * from dict where sent like '%<font color=red >"+"b"+"%';", null);
                while (cursor.moveToNext()){

                    String symbol = cursor.getString(cursor.getColumnIndex("symbol"));
                    String explain = cursor.getString(cursor.getColumnIndex("explain"));
                    String audio = cursor.getString(cursor.getColumnIndex("audio"));
                    String sent = cursor.getString(cursor.getColumnIndex("sent"));

                    LogUtiles.i("symbol = "+symbol);
                    LogUtiles.i("explain = "+explain);
                    LogUtiles.i("audio = "+audio);
                    LogUtiles.i("sent = "+sent);

                    WordInfo wordInfo = new WordInfo();
                    wordInfo.setSymbol(symbol);
                    wordInfo.setExplain(explain);
                    wordInfo.setAudio(audio);
                    wordInfo.setSent(sent);

                    mWordInfos.add(wordInfo);
                }
                cursor.close();
                database.close();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new WordsAdapter(mActivity, mWordInfos);// 实例化适配器
                        lvDatabaseInfo.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }
}
