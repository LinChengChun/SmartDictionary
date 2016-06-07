package com.example.trim.smartdictionary.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.adapter.AutoCompleteTextViewAdapter;
import com.example.trim.smartdictionary.bean.Word;
import com.example.trim.smartdictionary.database.DictionarySQLiteOpenHelper;
import com.example.trim.smartdictionary.utils.DataBaseAccess;
import com.example.trim.smartdictionary.utils.LogUtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/5.
 */
public class LocalSearchFragment extends Fragment implements View.OnClickListener {

    private View mLocalSearchFragment = null; // 定义一个视图组件
    private AutoCompleteTextView actv_LocalSearch = null; // 定义一个自动补全TextView
    private Button btn_LocalSearch = null; // 定义一个按键用于触发查询动作
    private TextView tv_LocalSearchResult = null;
    private List<String> mListData = null; // 用来缓存数据
//    private ArrayAdapter<String> adapter = null;
    private AutoCompleteTextViewAdapter adapter = null;
    private List<Word> mListWord = null;
    private Boolean isFirstLoad = false; // 确定是否是第一次进入应用，第一次加载数据库
    private String text = null; // 用于保存输入的文本

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mLocalSearchFragment == null){
            mLocalSearchFragment = inflater.inflate(R.layout.fragment_local, container, false);

            actv_LocalSearch = (AutoCompleteTextView) mLocalSearchFragment.findViewById(R.id.actv_LocalSearch);
            btn_LocalSearch = (Button) mLocalSearchFragment.findViewById(R.id.btn_LocalSearch);
            tv_LocalSearchResult = (TextView) mLocalSearchFragment.findViewById(R.id.tv_LocalSearchResult);

            mListData = new ArrayList<String>();

            //adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mListData);
            adapter = new AutoCompleteTextViewAdapter(getActivity(), mListData);
            actv_LocalSearch.setAdapter(adapter);
            actv_LocalSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LogUtiles.i("view = "+view+";position = "+position+";id = "+id);

//                    ListView listView = (ListView) parent.getSelectedView();
//                    String text = listView.getAdapter().getItem(position).toString();
                    text = ((TextView)view).getText().toString();
                    LogUtiles.i("text = "+text);
                }
            }); // 为AutoCompleteTextView设置监听

            btn_LocalSearch.setOnClickListener(this);

            isFirstLoad = true;
        }

        return mLocalSearchFragment;

    }

    @Override
    public void onClick(View v) {
        LogUtiles.i(v.toString()+ "onClick...");
        Word word = DataBaseAccess.query(DictionarySQLiteOpenHelper.TableName, text);
        tv_LocalSearchResult.setText(word.toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtiles.i("onStart...");
        LogUtiles.i("Line[78] isDataBaseUpdate = "+InlineSearchFragment.getDataBaseUpdate());
        if ( isFirstLoad ||InlineSearchFragment.getDataBaseUpdate()) { // 假如是第一次加载，或者数据库有更新，那么重新加载数据库里面的数据到集合
            mListWord = DataBaseAccess.loadWordData(DictionarySQLiteOpenHelper.TableName); // load data from DataBase
            mListData.clear(); // 清空集合
            for (int i = 0; i < mListWord.size(); i++) {
                mListData.add(mListWord.get(i).getQuery());
            }
            adapter.setmOriginalValues(mListData);
            isFirstLoad = false; // 取消第一次进入标志
            InlineSearchFragment.setDataBaseUpdate(false); // 取消数据库更新标志位
        }
        LogUtiles.i("Line[89] isDataBaseUpdate = "+InlineSearchFragment.getDataBaseUpdate());
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtiles.i("onResume...");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtiles.i("onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtiles.i("onStop...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtiles.i("onDestroyView...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtiles.i("onDestroy...");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtiles.i("onDetach...");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtiles.i("onAttach...");
    }

}
