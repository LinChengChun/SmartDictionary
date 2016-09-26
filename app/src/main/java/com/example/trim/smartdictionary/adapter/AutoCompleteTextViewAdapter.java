package com.example.trim.smartdictionary.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.trim.smartdictionary.utils.LogUtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/28.
 */
public class AutoCompleteTextViewAdapter extends AppBaseAdapter<String> implements Filterable{

    private List<String> mListData = null;//用于保存数据的一个集合，搜索自动补充数据
    private MyFilter myFilter = null;
    private final Object mLock = new Object();
    private List<String> mOriginalValues = null; // 用于缓存原始数据
    //private ArrayList<String> newValues = null; // 用于缓存查询数据结构
    private final String TAG = "AutoCompleteTextViewAdapter";
    private String value = null;

    public AutoCompleteTextViewAdapter(Context context, List<String> list) {
        super(context, list);
        this.mOriginalValues = list;
        mListData = new ArrayList<String>();
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, null, false);
            viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);//把ViewHolder作为一个标签 缓存到convertView里面
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        value = mListData.get(position);
        if (value != null)
            viewHolder.textView.setText(value);

        return convertView;
    }

    /**
     * 用于缓存 View id
     */
    static class ViewHolder{
        TextView textView;
    }

    /**
     * 自定义，被观察者数据变化时，通知到观察者
     * @param list
     */
    public void setDataNotify(List<String> list){
        this.mListData = list;
        notifyDataChange(this.mListData);
        notifyDataSetChanged();
    }


    public void setmOriginalValues(List<String> mOriginalValues) {
        this.mOriginalValues = mOriginalValues;
    }

    @Override
    public Filter getFilter() {
        if (myFilter == null)
            myFilter = new MyFilter();
        return myFilter;
    }

    class MyFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            // 持有过滤操作完成之后的数据。该数据包括过滤操作之后的数据的值以及数量。 count:数量 values包含过滤操作之后的数据的值
            FilterResults results = new FilterResults();
            if (mListData == null){
                synchronized (mLock){
                    // 将list的用户 集合转换给这个原始数据的ArrayList

                    mListData  = new ArrayList<String>();
                }
            }

            if (TextUtils.isEmpty(constraint)){
                LogUtiles.i(TAG+" constraint is empty");
            }else {
                // 做正式的筛选
                String prefixString = constraint.toString().toLowerCase();

                // 声明一个临时的集合对象 将原始数据赋给这个临时变量
                List<String> values = mOriginalValues;

                final int count = values.size();
                LogUtiles.i("values count = "+count);
                // 新的集合对象
                if (!mListData.isEmpty()) mListData.clear(); // 假如集合不为空，就清空集合
                String name;
                for (int i = 0, len = 0, time = 0; i < count; i++) {
                    // 如果姓名的前缀相符或者电话相符就添加到新的集合

                    name = values.get(i);
                    LogUtiles.i("i = "+i+"name = "+name+"; prefixString = "+prefixString);
                    for (len = 0; len<prefixString.length() && len<name.length(); len++ ){
                        LogUtiles.i("prefixString.charAt()"+len+prefixString.charAt(len));
                        LogUtiles.i("name.charAt()"+len+name.charAt(len));
                        if (prefixString.charAt(len) == name.charAt(len)){// 根据输入的字母数，自动加载相同的数据到集合中
                            time++; // 计算两个字符串之间的匹配度，即匹配个数
                        }
                    }

                    if (time == len)
                        mListData.add(name);
                    time = 0; // 归零计数器
                }
                // 然后将这个新的集合数据赋给FilterResults对象
                results.values = mListData;
                results.count = mListData.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // 重新将与适配器相关联的List重赋值一下
            mListData = (List<String>) results.values;

            if (results.count > 0) {
                setDataNotify(mListData); // 自定义，被观察者数据变化时，通知到观察者
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
