package com.laka.androidlib.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.laka.androidlib.R;

/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description:通用确认弹窗
 */
public class CommonConfirmDialog extends BaseDialog implements View.OnClickListener {

    private TextView mTvCancel;
    private TextView mTvSure;
    private TextView mTvMsg;
    private String mDefaultTitleTxt = "默认标题";

    public CommonConfirmDialog(Context context) {
        super(context);
    }

    public CommonConfirmDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CommonConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_common_confirm;
    }

    @Override
    protected void initView() {
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvSure = findViewById(R.id.tv_sure);
        mTvMsg = findViewById(R.id.tv_msg);
    }

    @Override
    protected void initData() {
        mTvMsg.setText(mDefaultTitleTxt);
    }

    @Override
    protected void initEvent() {
        mTvCancel.setOnClickListener(this);
        mTvSure.setOnClickListener(this);
    }

    @Override
    protected void initAnima() {
        //设置弹出收起动画
        getWindow().setWindowAnimations(R.style.commonDialogAnim);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            dismiss();
        } else if (i == R.id.tv_sure) {
            if (mOnClickSureListener != null) {
                mOnClickSureListener.onClickSure(v);
                dismiss();
            }
        }
    }

    /**
     * 设置标题
     * 调用的时机必须在show之后调用
     * 因为存在这样一种情况，有时候一个Dialog会多个地方使用。同时文案还会不一致
     * 为了节省内存，所以用一个Dialog。但是发现show之后，再改变文字是没有更新Dialog文字
     * 需要再调用TextView的setText，但是此时此刻TextView可能是空的，因为还没show就调用当前函数
     *
     * @param defaultTitleTxt
     */
    public CommonConfirmDialog setDefaultTitleTxt(String defaultTitleTxt) {
        mDefaultTitleTxt = defaultTitleTxt;
        if (defaultTitleTxt == null) {
            if (mTvMsg != null) {
                mTvMsg.setText("");
            }
        } else {
            if (mTvMsg != null) {
                mTvMsg.setText(mDefaultTitleTxt);
            }
        }
        return this;
    }

    /**
     * 设置标题
     * 调用的时机必须在show之后调用
     * 因为存在这样一种情况，有时候一个Dialog会多个地方使用。同时文案还会不一致
     * 为了节省内存，所以用一个Dialog。但是发现show之后，再改变文字是没有更新Dialog文字
     * 需要再调用TextView的setText，但是此时此刻TextView可能是空的，因为还没show就调用当前函数
     *
     * @param sureTxt
     */
    public CommonConfirmDialog setSureTxt(String sureTxt) {
        if (sureTxt == null) {
            if (mTvSure != null) {
                mTvSure.setText("");
            }
        } else {
            if (mTvSure != null) {
                mTvSure.setText(sureTxt);
            }
        }
        return this;
    }

    public CommonConfirmDialog setCancelTxt(String sureTxt) {
        if (sureTxt == null) {
            if (mTvCancel != null) {
                mTvCancel.setText("");
            }
        } else {
            if (mTvCancel != null) {
                mTvCancel.setText(sureTxt);
            }
        }
        return this;
    }

    public CommonConfirmDialog setCancelColor(int cancelColor) {
        if (mTvCancel != null) {
            mTvCancel.setTextColor(getContext().getResources().getColor(cancelColor));
        }
        return this;
    }

    private OnClickSureListener mOnClickSureListener;

    public void setOnClickSureListener(OnClickSureListener onClickSureListener) {
        mOnClickSureListener = onClickSureListener;
    }

    public interface OnClickSureListener {
        void onClickSure(View view);
    }
}
