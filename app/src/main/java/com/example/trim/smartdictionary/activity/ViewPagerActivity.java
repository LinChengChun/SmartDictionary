package com.example.trim.smartdictionary.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.adapter.ViewPagerAdapter;
import com.example.trim.smartdictionary.base.BaseActivity;
import com.example.trim.smartdictionary.bean.Detail;
import com.example.trim.smartdictionary.fragment.InlineSearchFragment;
import com.example.trim.smartdictionary.fragment.LocalSearchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/26.
 */
public class ViewPagerActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager mViewPager = null; // 定义一个ViewPager控件
    private ViewPagerAdapter adapter = null; // ViewPager适配器
    private List<Fragment> mListFragment = null; // 存储fragment的集合
    private LocalSearchFragment mLocalSearchFragment = null; // 本地查询碎片
    private InlineSearchFragment mInlineSearchFragment = null; // 在线查询碎片
    private Detail searchResult = null; // 查询结果保存到这个对象中
    private TextView mLocalSearchTextView = null;
    private TextView mInlineSearchTextView = null;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mViewPager = retrieveView(R.id.idViewPager);
        mLocalSearchTextView = retrieveView(R.id.idLocalSearch);
        mInlineSearchTextView = retrieveView(R.id.idInlineSearch);

//        mLocalSearchTextView.setTextColor(getResources().getColor(R.color.colorAccent));
//        mInlineSearchTextView.setTextColor(getResources().getColor(android.R.color.black));
        mLocalSearchTextView.setAlpha(0.3f);
        mInlineSearchTextView.setAlpha(0.3f);
    }

    @Override
    protected void initListener() {
        mLocalSearchTextView.setOnClickListener(this);
        mInlineSearchTextView.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        mLocalSearchTextView.setAlpha( (1-positionOffset>0.3f)? 1-positionOffset: 0.3f );
                        mInlineSearchTextView.setAlpha( (positionOffset>0.3f)? positionOffset: 0.3f );
                        break;
                    case 1:
                        mInlineSearchTextView.setAlpha( (1-positionOffset>0.3f)? 1-positionOffset: 0.3f );
                        mLocalSearchTextView.setAlpha( (positionOffset>0.3f)? positionOffset: 0.3f );
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
//                    mLocalSearchTextView.setTextColor(getResources().getColor(R.color.colorAccent));
//                    mInlineSearchTextView.setTextColor(getResources().getColor(android.R.color.black));
                    mLocalSearchFragment.onStart();
                }else {
//                    mInlineSearchTextView.setTextColor(getResources().getColor(R.color.colorAccent));
//                    mLocalSearchTextView.setTextColor(getResources().getColor(android.R.color.black));
//                    mInlineSearchTextView.onStart();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        mListFragment = new ArrayList<Fragment>(); // 实例化集合
        mLocalSearchFragment = new LocalSearchFragment();
        mInlineSearchFragment = new InlineSearchFragment();
        mListFragment.add(mLocalSearchFragment);
        mListFragment.add(mInlineSearchFragment);

        searchResult = new Detail(); // 实例化查询结果对象
        searchResult.webExplains = new ArrayList<String>();
        searchResult.explains = new ArrayList<String>();

        Bundle bundle = new Bundle(); // 组装Activity向Fragment传递的参数
        bundle.putSerializable("searchResult", searchResult);
        mLocalSearchFragment.setArguments(bundle); // activity 传递参数到 fragment
        mInlineSearchFragment.setArguments(bundle); // activity 传递参数到 fragment

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getBaseContext(), mListFragment);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.idInlineSearch:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.idLocalSearch:
                mViewPager.setCurrentItem(0);
                break;
        }
    }
}
