package com.example.trim.smartdictionary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/5.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<String> mList = null; // 适配器的数据
    private LayoutInflater mLayoutInflater = null; // 加载器
    private OnItemClickLitener mOnItemClickLitener = null;
    private int pos = 0;

    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public RecyclerViewAdapter(Context context, List<String> mList) {

        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_layout, null, false);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.iv_item);
        viewHolder.textView = (TextView) view.findViewById(R.id.tv_item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(mList.get(position));

        if (mOnItemClickLitener!=null){
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(v, position);
                    if (pos%2 == 0)
                        holder.imageView.setAlpha((float) 0.3);
                    else holder.imageView.setAlpha((float) 1.0);
                    pos++;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList != null && mList.size() > 0 ? mList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }

        TextView textView;
        ImageView imageView;
    }

    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }
}
