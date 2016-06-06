package com.example.trim.smartdictionary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库创建类
 * Created by Administrator on 2016/6/6.
 */
public class DictionarySQLiteOpenHelper extends SQLiteOpenHelper{

    public static final String TableName = "Dictionary";
    /**
     * 主键id
     */
    public static final String FIELD_ID = "_id";//标记唯一列
    public static final String FIELD_TRANSLATION = "translation";//标记唯一列
    public static final String FIELD_QUERY = "query";//输入的文本
    public static final String FIELD_US_PHONETIC = "us_phonetic";//美式发音
    public static final String FIELD_PHONETIC = "phonetic";//发音
    public static final String FIELD_UK_PHONETIC = "uk_phonetic";//英式发音
    public static final String FIELD_EXPLAINS = "explains";//翻译
    public static final String FIELD_WEBS = "web";//网络释义

    public DictionarySQLiteOpenHelper(Context context,
                                      String name,
                                      SQLiteDatabase.CursorFactory factory,
                                      int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDictionaryTable(db, TableName);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TableName);//升级时，假如存在，就把它删掉
        onCreate(db);
    }

    //表结构
    private void createDictionaryTable(final SQLiteDatabase db, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("create table IF NOT EXISTS ").append(tableName).append("(");
        sql.append(FIELD_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
        sql.append(",").append(FIELD_TRANSLATION).append(" TEXT DEFAULT ''");
        sql.append(",").append(FIELD_QUERY).append(" TEXT DEFAULT ''");
        sql.append(",").append(FIELD_US_PHONETIC).append(" TEXT DEFAULT ''");
        sql.append(",").append(FIELD_PHONETIC).append(" TEXT DEFAULT ''");
        sql.append(",").append(FIELD_UK_PHONETIC).append(" TEXT DEFAULT ''");
        sql.append(",").append(FIELD_EXPLAINS).append(" TEXT DEFAULT ''");
        sql.append(",").append(FIELD_WEBS).append(" TEXT DEFAULT ''");
        sql.append(")");
        db.execSQL(sql.toString());
    }
}
