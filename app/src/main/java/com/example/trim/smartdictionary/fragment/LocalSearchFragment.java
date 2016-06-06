package com.example.trim.smartdictionary.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.bean.Word;
import com.example.trim.smartdictionary.database.DictionarySQLiteOpenHelper;
import com.example.trim.smartdictionary.utils.DataBaseAccess;
import com.example.trim.smartdictionary.utils.LogUtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/5.
 */
public class LocalSearchFragment extends Fragment implements View.OnClickListener{

    private View mLocalSearchFragment = null; // 定义一个视图组件
    private AutoCompleteTextView actv_LocalSearch = null;
    private Button btn_LocalSearch = null;
    private TextView tv_LocalSearchResult = null;
    private List<String> mListData = null; // 用来缓存数据
    private ArrayAdapter<String> adapter = null;
    private List<Word> mListWord = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mLocalSearchFragment == null){
            mLocalSearchFragment = inflater.inflate(R.layout.fragment_local, container, false);

            actv_LocalSearch = (AutoCompleteTextView) mLocalSearchFragment.findViewById(R.id.actv_LocalSearch);
            btn_LocalSearch = (Button) mLocalSearchFragment.findViewById(R.id.btn_LocalSearch);
            tv_LocalSearchResult = (TextView) mLocalSearchFragment.findViewById(R.id.tv_LocalSearchResult);

            mListData = new ArrayList<String>();
            for (int i=0;i<50; i++){
                mListData.add(String.valueOf(i));
            }

            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mListData);
            actv_LocalSearch.setAdapter(adapter);

            btn_LocalSearch.setOnClickListener(this);
        }

        return mLocalSearchFragment;

    }

    @Override
    public void onClick(View v) {
        LogUtiles.i(v.toString()+ "onClick...");

        mListWord = DataBaseAccess.loadWordData(DictionarySQLiteOpenHelper.TableName);
        LogUtiles.i("mListWord.size = "+mListWord.size());

        mListData.clear(); // 清空集合
        int i=0;
        for (i=0; i<mListWord.size(); i++){
            mListData.add(mListWord.get(i).getQuery());
        }

        for (i=0; i<mListData.size(); i++){
            LogUtiles.i(mListData.get(i));
        }
        adapter.notifyDataSetChanged();
    }
}
