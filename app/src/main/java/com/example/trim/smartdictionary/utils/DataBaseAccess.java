package com.example.trim.smartdictionary.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.trim.smartdictionary.bean.Word;
import com.example.trim.smartdictionary.database.DictionarySQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 * Created by Administrator on 2016/6/6.
 */
public class DataBaseAccess {

    public static final String DB_Name = "LocalDictionary.db";//数据库文件名称
    private static final int VERSION = 1;//数据库版本
    private static DataBaseAccess mDataBaseAccess = null;
    private static SQLiteDatabase mSQLiteDatabase = null;//数据库管理类
    private static List<Word> mListWord = null; // 一个保存数据的集合

    /**
     * 数据库辅助操作
     * */
    public static final String[] DICTIONARY_COLUMNS = new String[] {
        DictionarySQLiteOpenHelper.FIELD_ID,
        DictionarySQLiteOpenHelper.FIELD_TRANSLATION,
        DictionarySQLiteOpenHelper.FIELD_QUERY,
        DictionarySQLiteOpenHelper.FIELD_US_PHONETIC,
        DictionarySQLiteOpenHelper.FIELD_PHONETIC,
        DictionarySQLiteOpenHelper.FIELD_UK_PHONETIC,
        DictionarySQLiteOpenHelper.FIELD_EXPLAINS,
        DictionarySQLiteOpenHelper.FIELD_WEBS
    };

    public static int DICTIONARY_Column_Start = 0;

    public static interface DICTIONARY_Column_Index {
        final int id = DICTIONARY_Column_Start++;
        final int translation = DICTIONARY_Column_Start++;
        final int query = DICTIONARY_Column_Start++;
        final int us_phonetic = DICTIONARY_Column_Start++;
        final int phonetic = DICTIONARY_Column_Start++;
        final int uk_phonetic = DICTIONARY_Column_Start++;
        final int explains = DICTIONARY_Column_Start++;
        final int web = DICTIONARY_Column_Start++;
    }

    /*
     * 带参构造方法
     * 1.创建数据库
     * 2.创建 播放视频记录表
     * */
    private DataBaseAccess(Context context) {
        //实例化VideoSQLiteOpenHelper 创建数据库
        DictionarySQLiteOpenHelper dbHelper = new DictionarySQLiteOpenHelper
                (context, FileUtils.getDatabaseDir()+ File.separator+DB_Name, null, VERSION);

        //创建 播放视频记录表
        mSQLiteDatabase = dbHelper.getReadableDatabase();//如果空间不够用，创建一个只读数据库
    }

    /**
     * 获取 DataBaseAccess 的 单例模式 实类
     */
    public static DataBaseAccess getInstance(Context context){
        if(mDataBaseAccess == null){
            mDataBaseAccess = new DataBaseAccess(context);
            mListWord = new ArrayList<Word>(); // 实例化List，用于保存数据
        }

        return mDataBaseAccess;
    }

    /**
     * 插入一条记录到数据库
     * */
    public static void saveWordData(Word word, String tableName){
        if(word != null){
            LogUtiles.i("saveWordData ---> start, word is "+word);
            String translation = word.getTranslation();
            String query = word.getQuery();
            String us_phonetic = word.getUs_phonetic();
            String phonetic = word.getPhonetic();
            String uk_phonetic = word.getUk_phonetic();
            String explains = word.getExplains();
            String web = word.getWeb();

            ContentValues values = new ContentValues();
            values.put(DictionarySQLiteOpenHelper.FIELD_TRANSLATION, translation);
            values.put(DictionarySQLiteOpenHelper.FIELD_QUERY, query);
            values.put(DictionarySQLiteOpenHelper.FIELD_US_PHONETIC, us_phonetic);
            values.put(DictionarySQLiteOpenHelper.FIELD_PHONETIC, phonetic);
            values.put(DictionarySQLiteOpenHelper.FIELD_UK_PHONETIC, uk_phonetic);
            values.put(DictionarySQLiteOpenHelper.FIELD_EXPLAINS, explains);
            values.put(DictionarySQLiteOpenHelper.FIELD_WEBS, web);

            mSQLiteDatabase.insert(tableName, null, values);
        }
        LogUtiles.i("saveWordData ---> success");
    }

    /**
     * 从数据库加载记录到List中
     * */
    public static List<Word> loadWordData(String tableName){
        LogUtiles.i("loadWordData ---> start");
        Cursor cursor = mSQLiteDatabase.query(tableName, null, null, null, null, null, null);

        if (!mListWord.isEmpty())
            mListWord.clear(); // 加载数据前记得清空集合

        if(cursor.moveToFirst()){
            do{
                Word word = new Word();
                word.setTranslation(cursor.getString(DICTIONARY_Column_Index.translation));
                word.setQuery(cursor.getString(DICTIONARY_Column_Index.query));
                word.setUs_phonetic(cursor.getString(DICTIONARY_Column_Index.us_phonetic));
                word.setPhonetic(cursor.getString(DICTIONARY_Column_Index.phonetic));
                word.setUk_phonetic(cursor.getString(DICTIONARY_Column_Index.uk_phonetic));
                word.setExplains(cursor.getString(DICTIONARY_Column_Index.explains));
                word.setWeb(cursor.getString(DICTIONARY_Column_Index.web));
                LogUtiles.i("loadWordData--->"+word.getQuery());
                mListWord.add(word);
            }while (cursor.moveToNext());
        }
        LogUtiles.i("loadWordData ---> success");
        cursor.close();
        return mListWord;
    }
}
