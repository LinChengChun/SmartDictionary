package com.example.trim.smartdictionary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 */
public class ResultAdapter extends AppBaseAdapter<String> {

    private List<String> mList;
    private Context mContext;

    public ResultAdapter(Context context, List<String> list) {
        super(context, list);
        this.mList = list;
        this.mContext = context;
    }


    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null, false);
        }
        return convertView;
    }
}
