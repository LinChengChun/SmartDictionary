package com.example.trim.smartdictionary.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 */
public class Detail implements Serializable{

    public String translation;// 中英互译结果
    public String query;// 查询文本
    public int errorCode;// 返回码

    public String us_phonetic;// 美式发音
    public String phonetic;// 标准发音
    public String uk_phonetic;// 英式发音

    public List<String> explains;// 动名词解释

    public List<String> webExplains;// 网络解释


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n译："+translation).append("\n")
                .append("美式发音："+us_phonetic).append("\n")
                .append("中式发音："+phonetic).append("\n")
                .append("英式发音："+uk_phonetic).append("\n\n")
                .append("基本词典：").append("\n");

        for (String temp1: explains)
            builder.append(temp1).append("\n");
        builder.append("\n网络释义：").append("\n");
        for (String temp2: webExplains)
            builder.append(temp2).append("\n");
        return builder.toString();
    }

    /**
     * 对对象进行清空处理
     * @param detail
     */
    public static void clear(Detail detail){
        if (!TextUtils.isEmpty(detail.translation)){
            detail.translation = null;
        }
        if (!TextUtils.isEmpty(detail.query)){
            detail.query = null;
        }
        if (!TextUtils.isEmpty(detail.us_phonetic)){
            detail.us_phonetic = null;
        }
        if (!TextUtils.isEmpty(detail.phonetic)){
            detail.phonetic = null;
        }
        if (!TextUtils.isEmpty(detail.uk_phonetic)){
            detail.uk_phonetic = null;
        }
        if (!detail.explains.isEmpty()){
            detail.explains.clear();
        }
        if (!detail.webExplains.isEmpty()){
            detail.webExplains.clear();
        }
    }
}
