package com.example.trim.smartdictionary.activity;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.base.BaseActivity;
import com.example.trim.smartdictionary.bean.WordInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cclin on 2016/9/27.
 */

public class DetailInfoActivity extends BaseActivity {

    @BindView(R.id.tv_search)
    TextView tvSearch; // 单词本身

    @BindView(R.id.tv_symbol)
    TextView tvSymbol; // 音标

    @BindView(R.id.tv_explain)
    TextView tvExplain; // 解释

    @BindView(R.id.tv_sent)
    TextView tvSent; // 例句

    private Intent intent; // 保存传过来的意图
    private WordInfo wordInfo; // 单词信息

    @Override
    protected void initWindow() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    @Override
    protected int initLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        intent = this.getIntent(); // 获取intent传递过来的数据
        wordInfo = (WordInfo) intent.getSerializableExtra("wordInfo"); // 反序列化得到WordInfo对象

        tvSearch.setText(intent.getStringExtra("word"));
        tvSymbol.setText(Html.fromHtml(parseContent("<font color=blue >音标:</font>"+"\n"
                +wordInfo.getSymbol())));
        tvExplain.setText(Html.fromHtml(parseContent("<font color=blue >解释:</font>"+"\n"
                +wordInfo.getExplain())));
        tvSent.setText(Html.fromHtml(parseContent("<font color=blue >例句:</font>"+"\n"+wordInfo.getSent())));

    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    /**
     * 替换回车符为html可识别回车符 \n ----> <br>
     * @param content
     * @return
     */
    private String parseContent(String content) {
        if(!TextUtils.isEmpty(content)){
            content = content.replace("\n","<br>");
        }
        return content;
    }
}
