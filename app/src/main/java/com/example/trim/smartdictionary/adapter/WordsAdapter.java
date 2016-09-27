package com.example.trim.smartdictionary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.bean.WordInfo;

import java.util.List;

/**
 * 用于展示单词信息的列表适配器
 * Created by cclin on 2016/9/26.
 */

public class WordsAdapter extends AppBaseAdapter<WordInfo>{

    private List<WordInfo> wordInfos; // 单词信息集合
    private Context mContext;

    public WordsAdapter(Context context, List<WordInfo> list) {
        super(context, list);
        this.mContext = context;
        this.wordInfos = list;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.list_item_word, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvExplain = (TextView) convertView.findViewById(R.id.tv_explain);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WordInfo wordInfo = wordInfos.get(position); // 获取当前位置的单词信息
        viewHolder.tvName.setText(getMainWord(wordInfo.getSent())+"  ["+wordInfo.getSymbol()+"]");
//        viewHolder.tvName.setText(Html.fromHtml()); // 利用html方式形式显示
        viewHolder.tvExplain.setText(wordInfo.getExplain());
        return convertView;
    }

    /**
     * 用于ListView优化，缓存组件
     */
    static class ViewHolder{
        TextView tvName;
        TextView tvExplain;
    }

    private String getMainWord(String text){
        String result = text.substring(text.indexOf("<font color=red >")+17  ,text.indexOf("</font>"));
        return result;
    }
}
