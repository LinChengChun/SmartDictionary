package com.example.trim.smartdictionary.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.utils.CommonUtils;

import java.lang.reflect.Field;

/**
 * 正在加载数据的对话框
 */
public class LoadingFragment extends DialogFragment {

    private TextView mLoadingText;
    private String mMsg = "正在加载...";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = CommonUtils.inflate(R.layout.fragment_loading);
        mLoadingText = (TextView) view.findViewById(R.id.loading_text);
        mLoadingText.setText(mMsg);
        Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Field mDismissedField = DialogFragment.class.getDeclaredField("mDismissed");
            mDismissedField.setAccessible(true);
            mDismissedField.set(this, false);

            Field mShownByMeField = DialogFragment.class.getDeclaredField("mShownByMe");
            mShownByMeField.setAccessible(true);
            mShownByMeField.set(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    public void setMsg(String msg) {
        if (msg != null) {
            this.mMsg = msg;
        }
    }

}
