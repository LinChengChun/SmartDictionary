package com.example.trim.smartdictionary.activity;

import android.content.Intent;
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

    @BindView(R.id.tv_detail_info)
    TextView tvDetailInfo;

    private Intent intent; // 保存传过来的意图
    private WordInfo wordInfo; // 单词信息

    @Override
    protected int initLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        intent = this.getIntent();
        wordInfo = (WordInfo) intent.getSerializableExtra("wordInfo");

        tvDetailInfo.setText(intent.getStringExtra("word")+" "+wordInfo.getSymbol()+"\n"
                +wordInfo.getAudio()+"\n"
            +wordInfo.getExplain()+"\n"
            +wordInfo.getSent());
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }
}
