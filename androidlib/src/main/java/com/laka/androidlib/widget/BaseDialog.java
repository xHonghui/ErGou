package com.laka.androidlib.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.laka.androidlib.util.screen.ScreenUtils;

import java.lang.ref.WeakReference;

/**
 * @Author:Rayman
 * @Date:2018/7/28
 * @Description:基类Dialog
 */

public class BaseDialog extends Dialog {

    private Context context;
    private View layoutContentView;
    private int layoutResId;
    private int styleId = -1;
    private int mWidth, mHeight;
    private boolean cancelable;
    private boolean cancelOutSide;

    private WeakReference<Context> reference;

    private BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(Builder builder) {
        super(builder.context, builder.styleId);
        this.reference = new WeakReference<>(builder.context);
        this.context = reference.get();
        this.layoutContentView = builder.layoutContentView;
        this.layoutResId = builder.layoutResId;
        this.styleId = builder.styleId;
        this.mWidth = builder.mWidth;
        this.mHeight = builder.mHeight;
        this.cancelable = builder.cancelable;
        this.cancelOutSide = builder.cancelOutSide;

        initDialog();
    }

    private void initDialog() {
        if (layoutContentView == null) {
            setContentView(layoutResId);
        } else {
            setContentView(layoutContentView);
        }

        if (styleId != -1) {
        }

        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelOutSide);
        Window window = this.getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        if (mWidth != 0 && mHeight != 0) {
            window.setLayout(mWidth, mHeight);
        } else {
            window.setLayout(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        }
    }

    public void show() {
        context = reference.get();
        if (isShowing()) {
            return;
        } else {
            super.show();
        }
    }

    public void dismiss() {
        context = reference.get();
        if (isShowing()) {
            super.dismiss();
        }
    }

    public static class Builder {

        private Context context;
        private int styleId;
        private View layoutContentView;
        private int layoutResId;
        private int mWidth, mHeight;
        private boolean cancelable;
        private boolean cancelOutSide;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setStyleId(@StyleRes int styleId) {
            this.styleId = styleId;
            return this;
        }

        public Builder setLayoutContentView(View layoutContentView) {
            this.layoutContentView = layoutContentView;
            return this;
        }

        public Builder setLayoutResId(@LayoutRes int layoutResId) {
            this.layoutResId = layoutResId;
            return this;
        }

        public Builder setWidth(int mWidth) {
            this.mWidth = mWidth;
            return this;
        }

        public Builder setHeight(int mHeight) {
            this.mHeight = mHeight;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCancelOutSide(boolean cancelOutSide) {
            this.cancelOutSide = cancelOutSide;
            return this;
        }

        public BaseDialog build() {
            return new BaseDialog(this);
        }
    }
}
