package com.example.trim.smartdictionary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;

/**
 * Created by Administrator on 2016/6/6.
 */
public class ControlView extends LinearLayout implements View.OnClickListener,View.OnTouchListener{

    public final static int LOCAL_SEARCH = 0;
    public final static int INLINE_SEARCH = 1;

    private TextView tv_LocalSearch = null;
    private TextView tv_InlineSearch = null;
    private OnClickListener mOnClickListener = null; // 回调监听
    private int position = 0; // 用于标记当前在那个模块

    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public ControlView(Context context) {
        super(context);
        init();
    }

    public ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     *
     */
    private void init(){
        initView();
        initListener();
        initData();
    }

    private void initView(){
        View root = LayoutInflater.from(getContext()).inflate(R.layout.select_layout, this, true); // 创建一个View，并把它添加到 this父控件 中

        tv_LocalSearch = (TextView) root.findViewById(R.id.id_LocalSearch);
        tv_InlineSearch = (TextView) root.findViewById(R.id.id_InlineSearch);
    }

    private void initListener(){
        tv_LocalSearch.setOnClickListener(this);
        tv_InlineSearch.setOnClickListener(this);
//        tv_LocalSearch.setOnTouchListener(this);
//        tv_InlineSearch.setOnTouchListener(this);
    }

    private void initData(){

    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null){
            if (v.getId() == R.id.id_LocalSearch){
                tv_InlineSearch.setBackgroundColor(getResources().getColor(R.color.colorClickUp));
                tv_LocalSearch.setBackgroundColor(getResources().getColor(R.color.colorClickDown));
                position = LOCAL_SEARCH;
            }else {
                tv_LocalSearch.setBackgroundColor(getResources().getColor(R.color.colorClickUp));
                tv_InlineSearch.setBackgroundColor(getResources().getColor(R.color.colorClickDown));
                position = INLINE_SEARCH;
            }
            mOnClickListener.onClick(v, position);
        }
//        PromptManager.showShortToast(getContext(), v.toString()+"onClick ");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_MOVE:
                break;
            default:break;
        }

        return false;
    }

    public interface OnClickListener{
        public void onClickUp(View v);
        public void onClickDown(View v);
        public void onClick(View v, int position);
    }
}
