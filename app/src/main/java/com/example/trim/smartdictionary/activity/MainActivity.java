package com.example.trim.smartdictionary.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.adapter.RecyclerViewAdapter;
import com.example.trim.smartdictionary.base.BaseActivity;
import com.example.trim.smartdictionary.bean.Detail;
import com.example.trim.smartdictionary.fragment.InlineSearchFragment;
import com.example.trim.smartdictionary.fragment.LocalSearchFragment;
import com.example.trim.smartdictionary.view.ControlView;

import java.util.ArrayList;
import java.util.List;

/**
 * ArrayList与LinkedList的区别
 * ArrayList:删除慢，查找快
 * LinkedList：删除快，查找慢，以双链表的方式删除，只需改变两个节点
 */

public class MainActivity extends BaseActivity implements RecyclerViewAdapter.OnItemClickLitener{

    private Detail searchResult = null; // 查询结果保存到这个对象中
    private RecyclerView mRecyclerView = null; // 定义一个RecyclerView组件
    private LocalSearchFragment mLocalSearchFragment = null; // 本地查询碎片
    private InlineSearchFragment mInlineSearchFragment = null; // 在线查询碎片
    private List<String> mList = null; // 数据集合
    private RecyclerViewAdapter adapter = null;

    private android.app.FragmentManager manager = null;
    private ControlView mControlView = null; // 自定义View 用于切换不同模式

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mRecyclerView = retrieveView(R.id.id_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mControlView = retrieveView(R.id.id_ControlView);
        mLocalSearchFragment = new LocalSearchFragment();
        mInlineSearchFragment = new InlineSearchFragment();
    }

    @Override
    protected void initListener() {
        mControlView.setmOnClickListener(new ControlView.OnClickListener() {
            @Override
            public void onClickUp(View v) {

            }

            @Override
            public void onClickDown(View v) {

            }

            @Override
            public void onClick(View view, int position) {
//                PromptManager.showShortToast(getBaseContext(), view.toString()+"onClick ");
                handlerOnClick(position); // 处理点击事件
            }
        });

    }

    @Override
    protected void initData() {

        searchResult = new Detail(); // 实例化查询结果对象
        searchResult.webExplains = new ArrayList<String>();
        searchResult.explains = new ArrayList<String>();

        Bundle bundle = new Bundle(); // 组装Activity向Fragment传递的参数
        bundle.putSerializable("searchResult", searchResult);
        mLocalSearchFragment.setArguments(bundle); // activity 传递参数到 fragment
        mInlineSearchFragment.setArguments(bundle); // activity 传递参数到 fragment

//        mList = new ArrayList<String>(); // 实例化RecyclerViewAdapter的集合，用于存储数据
//        mList.add("本地查询");
//        mList.add("在线查询");
//        adapter = new RecyclerViewAdapter(getBaseContext(), mList); // 实例化adapter适配器
//        adapter.setmOnItemClickLitener(this);
//
//        mRecyclerView.setAdapter(adapter);

        manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, mLocalSearchFragment);
        transaction.add(R.id.container, mInlineSearchFragment);

        transaction.hide(mInlineSearchFragment);
        transaction.show(mLocalSearchFragment);
        transaction.commit();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {
//        PromptManager.showShortToast(getBaseContext(), view.toString()+"onItemClick "+position);
        handlerOnClick(position);
    }

    /**
     * 根据点击位置，判断那种模式
     * @param position
     */
    private void handlerOnClick(int position){
        FragmentTransaction transaction = manager.beginTransaction();
        if (position == ControlView.LOCAL_SEARCH){
            mInlineSearchFragment.onStop();
            mLocalSearchFragment.onStart();

            transaction.hide(mInlineSearchFragment);
            transaction.show(mLocalSearchFragment);
            transaction.commit();
        }else if (position == ControlView.INLINE_SEARCH){
            mLocalSearchFragment.onStop();
            mInlineSearchFragment.onStart();

            transaction.hide(mLocalSearchFragment);
            transaction.show(mInlineSearchFragment);
            transaction.commit();
        }
    }
}
