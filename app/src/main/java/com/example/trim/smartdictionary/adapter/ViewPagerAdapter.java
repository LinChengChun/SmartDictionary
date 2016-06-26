package com.example.trim.smartdictionary.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/6/26.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter{

    private Context mContext = null;
    private List<Fragment> mListFragment = null; // 存储fragment的集合

    public ViewPagerAdapter(FragmentManager fm, Context mContext, List<Fragment> mListFragment) {
        super(fm);
        this.mContext = mContext;
        this.mListFragment = mListFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }
}
