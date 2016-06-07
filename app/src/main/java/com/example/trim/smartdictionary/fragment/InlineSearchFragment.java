package com.example.trim.smartdictionary.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.base.BaseActivity;
import com.example.trim.smartdictionary.bean.Detail;
import com.example.trim.smartdictionary.bean.Word;
import com.example.trim.smartdictionary.config.ConstantValue;
import com.example.trim.smartdictionary.database.DictionarySQLiteOpenHelper;
import com.example.trim.smartdictionary.utils.IRequest;
import com.example.trim.smartdictionary.utils.JsonUtils;
import com.example.trim.smartdictionary.utils.LogUtiles;
import com.lidroid.xutils.http.HttpHandler;

/**
 * Created by Administrator on 2016/6/5.
 */
public class InlineSearchFragment extends Fragment implements View.OnClickListener{

    private View mInlineSearchFragment = null; // 定义一个View组件
    private EditText et_InlineSearch = null;
    private Button btn_InlineSearch = null;
    private TextView tv_InlineSearchResult = null;
    //private Detail response = null; // 查询结果保存到这个对象中
    private HttpHandler mHttpHandler = null; //xUtils 的库的
    private Context mContext = null; // 上下文

    private Detail searchResult = null; // 用来存储查询结果的对象
    private static Boolean isDataBaseUpdate = false; // 用来标记数据库是否更新的标记位

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this.getActivity(); // 初始化上下文

        searchResult = (Detail) getArguments().getSerializable("searchResult"); // 获取activity传递过来的参数
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mInlineSearchFragment == null){
            mInlineSearchFragment = inflater.inflate(R.layout.fragment_inline, container, false);// 加载布局
            et_InlineSearch = (EditText) mInlineSearchFragment.findViewById(R.id.et_InlineSearch);// 查找输入框
            btn_InlineSearch = (Button) mInlineSearchFragment.findViewById(R.id.btn_InlineSearch);// 查询按钮
            tv_InlineSearchResult = (TextView) mInlineSearchFragment.findViewById(R.id.tv_InlineSearchResult);// 显示结果

            btn_InlineSearch.setOnClickListener(this);
        }
        return mInlineSearchFragment;
    }


    @Override
    public void onClick(View v) {

        String text = et_InlineSearch.getText().toString();
        if (!TextUtils.isEmpty(text))
            sendHttpRequest(text);

    }

    /**
     * 发送HTTP请求，开始查询单词，并把数据传给回调接口
     * url:
     */
    private void sendHttpRequest(String text) {
        mHttpHandler = IRequest.get(mContext, ConstantValue.LOOKFOR_URL + text, "It is Loading！！",
                new IRequest.OnRequestStringListener() {
                    @Override
                    public void onSuccess(String result) {
                        LogUtiles.i(result);
                        Detail.clear(searchResult);
                        JsonUtils.transfer(result, searchResult);// 对json数据进行解析并赋值给对象
                        tv_InlineSearchResult.setText(searchResult.toString());

                        Word word = new Word();
                        word.setTranslation(searchResult.translation);
                        word.setQuery(searchResult.query);
                        word.setUs_phonetic(searchResult.us_phonetic);
                        word.setPhonetic(searchResult.phonetic);
                        word.setUk_phonetic(searchResult.uk_phonetic);

                        StringBuilder builder = new StringBuilder();// 实例化一个 StringBuilder对象
                        int i = 0;// 定义一个for 循环计数变量
                        for (i=0; i<searchResult.explains.size(); i++) {
                            builder.append(searchResult.explains.get(i));
                            builder.append("\n");
                        }
                        word.setExplains(builder.toString());

                        builder.delete(0, builder.length());// clear space
                        for (i=0; i<searchResult.webExplains.size(); i++){
                            builder.append(searchResult.webExplains.get(i));
                            builder.append("\n");
                        }
                        word.setWeb(builder.toString());

                        LogUtiles.i("Line[111] isDataBaseUpdate = "+isDataBaseUpdate);
                        if (!isDataBaseUpdate) // 假如目前状态是 没有更新，即更新状态
                            isDataBaseUpdate = true;
                        BaseActivity.mDataBaseAccess.saveWordData(word, DictionarySQLiteOpenHelper.TableName);
                        LogUtiles.i("Line[113] isDataBaseUpdate = "+isDataBaseUpdate);
                    }

                    @Override
                    public void onFailure() {
                        LogUtiles.i("网络异常");
                    }

                    @Override
                    public void onStart() {
                        LogUtiles.i("onStart...");
                    }
                });
    }


    public static Boolean getDataBaseUpdate() {
        return isDataBaseUpdate;
    }

    public static void setDataBaseUpdate(Boolean dataBaseUpdate) {
        isDataBaseUpdate = dataBaseUpdate;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHttpHandler!=null)
            mHttpHandler.cancel();
    }
}
