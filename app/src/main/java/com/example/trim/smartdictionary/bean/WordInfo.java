package com.example.trim.smartdictionary.bean;

import java.io.Serializable;

/**
 * 用于存储某一个单词的所有信息
 * Created by cclin on 2016/9/26.
 */

public class WordInfo implements Serializable{

    private String word; // 单词
    private String symbol; // 音标
    private String explain; // 含义
    private String audio; // 播放读音链接
    private String sent; // 例句

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }
}
