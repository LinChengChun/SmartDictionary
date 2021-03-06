package com.example.trim.smartdictionary.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.adapter.ViewPagerAdapter;
import com.example.trim.smartdictionary.base.BaseActivity;
import com.example.trim.smartdictionary.bean.Detail;
import com.example.trim.smartdictionary.fragment.DbSearchFragment;
import com.example.trim.smartdictionary.fragment.InlineSearchFragment;
import com.example.trim.smartdictionary.fragment.LocalSearchFragment;
import com.example.trim.smartdictionary.utils.PromptManager;

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
    private DbSearchFragment mDbSearchFragment = null; // 数据库查询页面
    private Detail searchResult = null; // 查询结果保存到这个对象中
    private TextView mLocalSearchTextView = null;
    private TextView mInlineSearchTextView = null;
    private TextView mDatabaseSearchTextView = null;
    private long[] mHits = new long[2];

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
//        CommonUtils.setTranslucent(this);// 使状态栏透明

        mViewPager = retrieveView(R.id.idViewPager);
        mLocalSearchTextView = retrieveView(R.id.idLocalSearch);
        mInlineSearchTextView = retrieveView(R.id.idInlineSearch);
        mDatabaseSearchTextView = retrieveView(R.id.idDatabaseSearch);

        mLocalSearchTextView.setAlpha(0.3f);
        mInlineSearchTextView.setAlpha(0.3f);
        mDatabaseSearchTextView.setAlpha(0.3f);
    }

    @Override
    protected void initListener() {
        mLocalSearchTextView.setOnClickListener(this);
        mInlineSearchTextView.setOnClickListener(this);
        mDatabaseSearchTextView.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        mDatabaseSearchTextView.setAlpha( (1-positionOffset>0.3f)? 1-positionOffset: 0.3f );
                        mInlineSearchTextView.setAlpha( (positionOffset>0.3f)? positionOffset: 0.3f );
                        mLocalSearchTextView.setAlpha(  0.3f );
                        break;
                    case 1:
                        mDatabaseSearchTextView.setAlpha(  0.3f );
                        mInlineSearchTextView.setAlpha( (1-positionOffset>0.3f)? 1-positionOffset: 0.3f );
                        mLocalSearchTextView.setAlpha( (positionOffset>0.3f)? positionOffset: 0.3f );
                        break;
                    case 2:
                        mDatabaseSearchTextView.setAlpha(  0.3f );
                        mInlineSearchTextView.setAlpha( (positionOffset>0.3f)? positionOffset: 0.3f );
                        mLocalSearchTextView.setAlpha( (1-positionOffset>0.3f)? 1-positionOffset: 0.3f );
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
//                    mDbSearchFragment.onStart();
                }else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        mListFragment = new ArrayList<Fragment>(); // 实例化存储Fragment的集合
        mLocalSearchFragment = new LocalSearchFragment();
        mInlineSearchFragment = new InlineSearchFragment();
        mDbSearchFragment = new DbSearchFragment();
        mListFragment.add(mDbSearchFragment);
        mListFragment.add(mInlineSearchFragment);
        mListFragment.add(mLocalSearchFragment); // 把Fragment添加到集合中

        searchResult = new Detail(); // 实例化查询结果对象
        searchResult.webExplains = new ArrayList<String>();
        searchResult.explains = new ArrayList<String>();

        Bundle bundle = new Bundle(); // 组装Activity向Fragment传递的参数
        bundle.putSerializable("searchResult", searchResult);
        mLocalSearchFragment.setArguments(bundle); // activity 传递参数到 fragment
        mInlineSearchFragment.setArguments(bundle); // activity 传递参数到 fragment
//        mDbSearchFragment.setArguments(bundle); // activity 传递参数到 fragment

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getBaseContext(), mListFragment); // 实例化适配器
        mViewPager.setAdapter(adapter); // 为ViewPager设置适配器
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.idInlineSearch:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.idLocalSearch:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.idDatabaseSearch:
                mViewPager.setCurrentItem(0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
        mHits[mHits.length-1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis()-1500)) {
            super.onBackPressed();
        }else {
            PromptManager.showShortToast(this, "再按一次，退出应用");
        }
    }
}
