package com.example.trim.smartdictionary.utils;


import com.example.trim.smartdictionary.bean.Detail;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Aaron json工具类
 */
public class JsonUtils {

    private static Gson gson = new Gson();

    /**
     * 用来将JSON串转为对象，但此方法不可用来转带泛型的集合
     */
    public static <T> T object(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将对象转为JSON串
     */
    public static String toJson(Object object) {
        if (object == null) {
            return gson.toJson(JsonNull.INSTANCE);
        }
        return gson.toJson(object);
    }

    /**
     * 用来将JSON串转为对象，此方法可用来转带泛型的集合，如：Type为 new
     * TypeToken<List<T>>(){}.getType()，其它类也可以用此方法调用，就是将List<T>替换为你想要转成的类
     */
    public static Object fromJson(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 手动解析json数据，并保存到一个Detail对象中
     * @param jsonString
     * @return Detail
     */
    public static Detail transfer(String jsonString, Detail detail){

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String translation = jsonObject.getString("translation");
            String query = jsonObject.getString("query");
            int errorCode = jsonObject.getInt("errorCode");

            detail.translation = translation.substring(1, translation.length()-1);
            detail.errorCode = errorCode;
            detail.query = query;

            LogUtiles.i("translation: "+translation.substring(1, translation.length()-1));
            LogUtiles.i("query: "+query);
            LogUtiles.i("errorCode: "+errorCode);

            JSONObject jsonBasic = jsonObject.getJSONObject("basic");
            String us_phonetic = jsonBasic.getString("us-phonetic");
            String phonetic = jsonBasic.getString("phonetic");
            String uk_phonetic = jsonBasic.getString("uk-phonetic");
            String explains = jsonBasic.getString("explains");

            LogUtiles.i("us_phonetic: "+us_phonetic);
            LogUtiles.i("phonetic: "+phonetic);
            LogUtiles.i("uk_phonetic: "+uk_phonetic);

            detail.us_phonetic = us_phonetic;
            detail.phonetic = phonetic;
            detail.uk_phonetic = uk_phonetic;

            String explain[] = explains.substring(1, explains.length()-1).split(",");
            String temp;
            if (explain!=null && explain.length>0){
                if (!detail.explains.isEmpty()) // 清空集合
                    detail.explains.clear();
                for (int i=0; i< explain.length; i++){
                    temp = explain[i].subSequence(1, explain[i].length()-1).toString();
                    LogUtiles.i(temp);
                    detail.explains.add(temp);// 将每个解释添加到集合
                }
            }

            JSONArray jsonArray = jsonObject.getJSONArray("web");
            if (!detail.webExplains.isEmpty()) // 清空集合
                detail.webExplains.clear();
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonWeb = (JSONObject) jsonArray.get(i);
                String key = jsonWeb.getString("key");
                String value = jsonWeb.getString("value");

                LogUtiles.i("key: "+key);
                LogUtiles.i("value: "+value);
                String val = value.substring(1, value.length()-1).replaceAll("[\"\"]", "");
                LogUtiles.i("result value:"+val);
                detail.webExplains.add(key+": "+val);

                String values[] = value.substring(1, value.length()-1).split(",");
                if (values!=null && values.length>0){
                    for (int j=0; j< values.length; j++)
                        LogUtiles.i(values[j].subSequence(1, values[j].length()-1).toString());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detail;
    }
}
