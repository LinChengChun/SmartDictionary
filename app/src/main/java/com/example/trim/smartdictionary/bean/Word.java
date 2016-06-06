package com.example.trim.smartdictionary.bean;

/**
 * Created by Administrator on 2016/6/6.
 */
public class Word {

    private String translation;// 中英互译结果
    private String query;// 查询文本

    private String us_phonetic;// 美式发音
    private String phonetic;// 标准发音
    private String uk_phonetic;// 英式发音

    private String explains;
    private String web;

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getUs_phonetic() {
        return us_phonetic;
    }

    public void setUs_phonetic(String us_phonetic) {
        this.us_phonetic = us_phonetic;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getUk_phonetic() {
        return uk_phonetic;
    }

    public void setUk_phonetic(String uk_phonetic) {
        this.uk_phonetic = uk_phonetic;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
}
