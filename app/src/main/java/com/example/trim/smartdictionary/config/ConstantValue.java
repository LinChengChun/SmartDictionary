package com.example.trim.smartdictionary.config;

/**
 * Created by Administrator on 2016/6/4.
 */
public interface ConstantValue {

    public String APIkey = "594260767";
    public String keyfrom = "SmartDictionary";
    public String COMMON_URL = "http://fanyi.youdao.com/openapi.do?";
    public String doctype_xml = "xml";
    public String doctype_json = "json";
    public String doctype_jsonp = "jsonp";


//http://fanyi.youdao.com/openapi.do?keyfrom=SmartDictionary&key=594260767&type=data&doctype=<doctype>
// &version=1.1&q=要翻译的文本
    // 组装链接，只需加上 要翻译的文本 即可
    public String LOOKFOR_URL = COMMON_URL +"keyfrom="+keyfrom+"&key="+APIkey+"&type=data"+
        "&doctype="+doctype_json+"&version=1.1&q=";


}
