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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n译："+translation).append("\n")
                .append("美式发音："+us_phonetic).append("\n")
                .append("中式发音："+phonetic).append("\n")
                .append("英式发音："+uk_phonetic).append("\n\n")
                .append("基本词典：").append("\n");

        builder.append(explains);
        builder.append("\n网络释义：").append("\n");
        builder.append(web);
//        String[] explain = explains.split("")
//            builder.append(temp1).append("\n");
//        builder.append("\n网络释义：").append("\n");
//        for (String temp2: webExplains)
//            builder.append(temp2).append("\n");
        return builder.toString();
    }
}
