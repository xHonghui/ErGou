package com.laka.androidlib.widget.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.androidlib.R;

/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description: 通用加载弹窗
 */
public class LoadingDialog extends BaseDialog {

    private ImageView mImageViewLoading;
    private AnimationDrawable mAnimDrawable;

    public LoadingDialog(Context context) {
        this(context, R.style.commonLoadingDialog);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView() {
        setGravityType(Gravity.CENTER);
        setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mImageViewLoading = findViewById(R.id.image_view_loading);
        mAnimDrawable = (AnimationDrawable) getContext().getResources().getDrawable(R.drawable.anim_loading);
        mImageViewLoading.setImageDrawable(mAnimDrawable);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_loading;
    }

    @Override
    public void show() {
        super.show();
        if (mAnimDrawable != null) {
            mAnimDrawable.start();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mAnimDrawable != null) {
            mAnimDrawable.stop();
        }
    }
}
