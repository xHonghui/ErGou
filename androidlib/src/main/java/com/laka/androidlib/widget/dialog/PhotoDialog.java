package com.laka.androidlib.widget.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.laka.androidlib.R;

import static com.laka.androidlib.util.screen.ScreenUtils.dp2px;

/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description:照片选择弹窗
 */
public class PhotoDialog extends Dialog {

    private View mContentView;
    private TextView mTvTakePhoto;
    private TextView mTvAlbum;
    private TextView mTvCancel;

    public PhotoDialog(@NonNull Context context) {
        super(context);
    }

    public PhotoDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected PhotoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置对话框布局，可以通过外部设置进来
        mContentView = getLayoutInflater().inflate(R.layout.dialog_phone_select, null);
        initView();
        setContentView(mContentView);
        // 设置LayoutParams的属性
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.verticalMargin = dp2px(30);
        params.horizontalMargin = dp2px(30);
    }

    private void initView() {
        mTvTakePhoto = mContentView.findViewById(R.id.tv_take_photo);
        mTvAlbum = mContentView.findViewById(R.id.tv_album);
        mTvCancel = mContentView.findViewById(R.id.tv_cancel);
        mTvTakePhoto.setOnClickListener(mOnClickListener);
        mTvAlbum.setOnClickListener(mOnClickListener);
        mTvCancel.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


}
