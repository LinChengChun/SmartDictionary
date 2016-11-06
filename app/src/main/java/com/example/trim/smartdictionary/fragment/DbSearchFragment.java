package com.example.trim.smartdictionary.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.activity.DetailInfoActivity;
import com.example.trim.smartdictionary.adapter.WordsAdapter;
import com.example.trim.smartdictionary.bean.WordInfo;
import com.example.trim.smartdictionary.utils.CommonUtils;
import com.example.trim.smartdictionary.utils.LogUtiles;
import com.example.trim.smartdictionary.utils.PromptManager;
import com.example.trim.smartdictionary.view.CircleNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cclin on 2016/9/26.
 */

public class DbSearchFragment extends Fragment implements View.OnClickListener, TextWatcher,
        AdapterView.OnItemClickListener, Runnable, AnimationListener {

    private static String path = "data/data/com.example.trim.smartdictionary/files/font.ttf";
    private View mDbSearchFragment = null; // 定义一个视图组件
    private Activity mActivity; // 上下文
    private EditText etInput; // 文本输入控件，需要查询的单词
    private Button btnSearch; // 查询按键
    private ListView lvDatabaseInfo;
    private List<WordInfo> mWordInfos;
    private FloatingActionButton fab; // 悬浮按钮

    private WordsAdapter adapter;
    private InputMethodManager imm; // 输入法管理器

    private String inputText; // 获取输入的文本
    private String[] searchs; // 单词数组
    private Thread searchThread; // 搜索线程
    private CircleNavigationView mCircleNavigationView; // 圆形导航控件
    private AlertDialog navigationDialog;
    private Animation mHideAnimatiion; // 导航栏显示
    private Animation mShowAnimatiion;

    private boolean isOpenUp = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mDbSearchFragment == null) {
            mDbSearchFragment = inflater.inflate(R.layout.fragment_database, container, false);
        }
        return mDbSearchFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etInput = (EditText) mDbSearchFragment.findViewById(R.id.et_input);
        btnSearch = (Button) mDbSearchFragment.findViewById(R.id.btn_search);
        lvDatabaseInfo = (ListView) mDbSearchFragment.findViewById(R.id.lv_database_info);
        fab = (FloatingActionButton) mDbSearchFragment.findViewById(R.id.fab);
        mCircleNavigationView = (CircleNavigationView) mDbSearchFragment.findViewById(R.id.cnv);

        mActivity = this.getActivity();
        imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE); // 获取系统输入法服务
        mWordInfos = new ArrayList<WordInfo>(); // 初始化集合
        adapter = new WordsAdapter(mActivity, mWordInfos);// 实例化适配器
        lvDatabaseInfo.setAdapter(adapter); // 设置适配器

        searchs = CommonUtils.getStringArray(R.array.search); // 从资源文件中读取到内存

        LogUtiles.d("searchs length = " + searchs.length);

        etInput.addTextChangedListener(this);
        btnSearch.setOnClickListener(this);
        fab.setOnClickListener(this);
        lvDatabaseInfo.setOnItemClickListener(this);
        mCircleNavigationView.setOnClickListener(new CircleNavigationView.CircleNavigationOnClickListener() {
            @Override
            public void up() {
                PromptManager.showShortToast(mActivity, "你点击了 页头");
            }

            @Override
            public void down() {
                PromptManager.showShortToast(mActivity, "你点击了 页尾");
            }

            @Override
            public void left() {
                PromptManager.showShortToast(mActivity, "你点击了 足迹");
            }

            @Override
            public void right() {
                PromptManager.showShortToast(mActivity, "你点击了 搜索");
            }

            @Override
            public void center() {
                PromptManager.showShortToast(mActivity, "你点击了 中心");
                mCircleNavigationView.startAnimation(mHideAnimatiion); // 播放隐藏动画

                Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fab_hide_rotate); // 启动动画
                anim.setFillAfter(true); // 旋转后不恢复
                fab.startAnimation(anim);
            }
        });


        mHideAnimatiion = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_hide);
        mHideAnimatiion.setFillAfter(true); // 动画完成后不恢复原状
        mHideAnimatiion.setAnimationListener(this);

        mShowAnimatiion = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_show);
        mShowAnimatiion.setFillAfter(true); // 动画完成后不恢复原状
        mShowAnimatiion.setAnimationListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mShowAnimatiion.setAnimationListener(null);
        mHideAnimatiion.setAnimationListener(null);
        mShowAnimatiion = null;
        mHideAnimatiion = null;

    }

    private synchronized void searchWordInfoFromDB(final String input) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!mWordInfos.isEmpty()) mWordInfos.clear(); // 情况集合
                LogUtiles.i("searchWordInfoFromDB start,input = " + input);
                SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
                Cursor cursor = database.rawQuery("select * from dict where sent like '%<font color=red >"
                        + input + "%';", null);
                while (cursor.moveToNext()) {

                    String symbol = cursor.getString(/*cursor.getColumnIndex("symbol")*/ 0);
                    String explain = cursor.getString(/*cursor.getColumnIndex("explain")*/ 1);
                    String audio = cursor.getString(/*cursor.getColumnIndex("audio")*/ 2);
                    String sent = cursor.getString(/*cursor.getColumnIndex("sent")*/ 3);

                    WordInfo wordInfo = new WordInfo();
                    wordInfo.setSymbol(symbol);
                    wordInfo.setExplain(explain);
                    wordInfo.setAudio(audio);
                    wordInfo.setSent(sent);

                    mWordInfos.add(wordInfo);
                }
                cursor.close();
                database.close();
                LogUtiles.i("searchWordInfoFromDB end");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataChange(mWordInfos);
                    }
                });
            }
        }).start();
    }

    /**
     * 根据数组里边的坐标从数据库导入每一条记录的数据
     *
     * @param input
     */
    private synchronized void loadWordInfoFromDB(String input) {
        if (TextUtils.isEmpty(input)) // 如果输入为空那么不进行数据库查询
            input = "a";
        if (!mWordInfos.isEmpty()) mWordInfos.clear(); // 清空集合
        long startTime = System.currentTimeMillis();
        LogUtiles.i("loadWordInfoFromDB start,input = " + input);

        SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = database.rawQuery("select * from dict", null);

        for (int i = 0; i < searchs.length; i++) {
            if (searchs[i].charAt(0) < input.charAt(0))
                continue;
            if (searchs[i].charAt(0) > input.charAt(0)) // 比较第一个字符的大小，只匹配第一字符相同的单词
                break; // 跳出扫描
            if (searchs[i].startsWith(input)) {
                cursor.moveToPosition(i); // 移动光标到某一行
                String symbol = cursor.getString(/*cursor.getColumnIndex("symbol")*/ 0);
                String explain = cursor.getString(/*cursor.getColumnIndex("explain")*/ 1);
                String audio = cursor.getString(/*cursor.getColumnIndex("audio")*/ 2);
                String sent = cursor.getString(/*cursor.getColumnIndex("sent")*/ 3);

                WordInfo wordInfo = new WordInfo();
                wordInfo.setWord(searchs[i]);
                wordInfo.setSymbol(symbol);
                wordInfo.setExplain(explain);
                wordInfo.setAudio(audio);
                wordInfo.setSent(sent);

                mWordInfos.add(wordInfo); // 把查询结果添加到集合中
            }
        }
        cursor.close();
        database.close();
        LogUtiles.i("loadWordInfoFromDB end");
        long endTime = System.currentTimeMillis();
        long diff = endTime - startTime; // 计算时间差值
        LogUtiles.d("spend " + diff + " ms");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataChange(mWordInfos);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                String input = etInput.getText().toString().trim();
                if (TextUtils.isEmpty(input)) {
                    input = "a";
                }
                hideSoftInputMethod();
                loadWordInfoFromDB(input); // 从数据库中查询
                break;
            case R.id.fab:
                LogUtiles.d("FloatingActionButton is clicked,isShown = " + mCircleNavigationView.isShown());

                if (mCircleNavigationView.isShown()){
                    mCircleNavigationView.startAnimation(mHideAnimatiion); // 播放隐藏动画
//                    mCircleNavigationView.animate()
//                            .scaleX(0.0f)
//                            .scaleY(0.0f)
////                            .translationY(view.getHeight())
////                            .alpha(0.0f)
//                            .setDuration(500)
//                            .setListener(new AnimatorListenerAdapter() {
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    super.onAnimationEnd(animation);
//                                    mCircleNavigationView.setVisibility(View.GONE);
//                                }
//                            });

                    Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fab_hide_rotate); // 启动动画
                    anim.setFillAfter(true); // 动画完成后不恢复原状
                    fab.startAnimation(anim);
                }else {
                    mCircleNavigationView.startAnimation(mShowAnimatiion); // 播放显示动画
//                    mCircleNavigationView.animate()
//                            .scaleX(1.0f)
//                            .scaleY(1.0f)
////                            .translationY(view.getHeight())
////                            .alpha(1.0f)
//                            .setDuration(500)
//                            .setListener(new AnimatorListenerAdapter() {
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    super.onAnimationEnd(animation);
//                                    mCircleNavigationView.setVisibility(View.VISIBLE);
//                                }
//                            });

                    Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.fab_show_rotate); // 启动动画
                    anim.setFillAfter(true); // 动画完成后不恢复原状
                    fab.startAnimation(anim);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏输入法
     */
    private void hideSoftInputMethod() {

        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        LogUtiles.d("isOpen = " + isOpen);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0); //强制隐藏键盘
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (this.isResumed()) {
            inputText = s.toString().toLowerCase();
            if (searchThread == null) {
                searchThread = new Thread(this);
                searchThread.start(); // 启动搜索线程
                return;
            }
            if (!searchThread.isInterrupted())
                searchThread.interrupt(); // 如果线程还没停止，应该中断
            searchThread.run();
        }
    }

    /**
     * 列表点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WordInfo wordInfo = mWordInfos.get(position);
        enterDetailInfoActivity(wordInfo, view); // 启动跳转进入详细单词信息页面
    }

    /**
     * 进入单词详细信息页面
     *
     * @param wordInfo
     * @param view
     */
    private void enterDetailInfoActivity(WordInfo wordInfo, View view) {
        Intent intent = new Intent(mActivity, DetailInfoActivity.class);
        intent.putExtra("word", ((WordsAdapter.ViewHolder) view.getTag()).tvName.getText());
        intent.putExtra("wordInfo", wordInfo);
        mActivity.startActivity(intent);
    }

    @Override
    public void run() {
        loadWordInfoFromDB(inputText); // 从数据库中查询单词
    }

    private void showNavigationDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.NavigationCustomDialog);

        navigationDialog = builder.create();
        View view = View.inflate(context, R.layout.dialog_navigation, null);
//        TextView view = new TextView(context);
//        view.setText("hello world!!");
//        view.setTextSize(20.0f);
//        view.setTextColor(Color.WHITE);
//        view.setBackgroundColor(Color.BLUE);
//        navigationDialog.setView(view,0,0,0,0);

        navigationDialog.setCanceledOnTouchOutside(true);
        navigationDialog.show();
        navigationDialog.setContentView(view);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.navigation_show);
        animation.setFillAfter(true); // 动画完成后不恢复原状
        view.startAnimation(animation);
    }

    private void dismissNavigationDialog(){
        if (navigationDialog.isShowing()) {

            Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_hide);
            animation.setFillAfter(true); // 动画完成后不恢复原状
            navigationDialog.getWindow().findViewById(R.id.cnv).startAnimation(animation);
            navigationDialog.dismiss();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        LogUtiles.d("onAnimationStart..."+animation);
        if (animation == mShowAnimatiion) {
            mCircleNavigationView.setVisibility(View.VISIBLE); // 可见的
            lvDatabaseInfo.setEnabled(false); // 屏蔽该控件
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        LogUtiles.d("onAnimationEnd..."+animation);
        if (animation == mHideAnimatiion) {
            mCircleNavigationView.clearAnimation(); // 设置属性前需要关闭动画才能起作用
            mCircleNavigationView.setVisibility(View.INVISIBLE);
            lvDatabaseInfo.setEnabled(true); // 使能该控件
        }else if (animation == mShowAnimatiion){

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}