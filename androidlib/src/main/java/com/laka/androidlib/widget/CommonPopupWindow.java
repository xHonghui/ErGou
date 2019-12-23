package com.laka.androidlib.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.androidlib.R;
import com.laka.androidlib.interfaces.BasePopup;

/**
 * @Author:Rayman
 * @Date:2018/5/4
 * @Description:公共PopupWindow
 */

public class CommonPopupWindow extends BasePopupWindow implements BasePopupWindow.OnItemClickListener {

    private TextView mTvTitle;
    private ImageView mIvClose;
    private LinearLayout mLlContent;
    private TextView mTvContent;
    private LinearLayout mLlControl;
    private TextView mTvCancel;
    private TextView mTvConfirm;
    private View mDivider;
    private OnPopupItemClickListener onPopItemClickListener;

    private CommonPopupWindow(Builder builder) {
        this(builder.context);
        enableClose(builder.enableClose);
        enableSingleControl(builder.enableSingle);
        setTitle(builder.title);
        setContent(builder.content);
        setCancelText(builder.cancel);
        setConfirmText(builder.confirm);
    }

    private CommonPopupWindow(Context context) {
        super(context);
        initView();
        initEvent();
    }

    private void initView() {
        mTvTitle = (TextView) getView(R.id.tv_pop_title);
        mIvClose = (ImageView) getView(R.id.iv_pop_close);
        mLlContent = (LinearLayout) getView(R.id.ll_pop_content);
        mTvContent = (TextView) getView(R.id.tv_pop_content);
        mLlControl = (LinearLayout) getView(R.id.ll_pop_confirm);
        mTvCancel = (TextView) getView(R.id.tv_pop_cancel);
        mTvConfirm = (TextView) getView(R.id.tv_pop_confirm);
        mDivider = getView(R.id.view_control_divider);
    }

    private void initEvent() {
        setOnClickListener(mIvClose);
        setOnClickListener(mTvCancel);
        setOnClickListener(mTvConfirm);
        setOnItemClickListener(this);
    }

    public void setTitle(String title) {
        if (checkNull(title)) {
            return;
        }
        mTvTitle.setText(title);
    }

    public void setContent(String content) {
        if (checkNull(content)) {
            return;
        }
        mTvContent.setText(content);
    }

    public void setCancelText(String cancelText) {
        if (checkNull(cancelText)) {
            return;
        }
        mTvCancel.setText(cancelText);
    }

    public void setConfirmText(String confirmText) {
        if (checkNull(confirmText)) {
            return;
        }
        mTvConfirm.setText(confirmText);
    }

    public void enableSingleControl(boolean isSingle) {
        if (isSingle) {
            mTvCancel.setVisibility(View.GONE);
            mDivider.setVisibility(View.GONE);
        }
    }

    public void enableClose(boolean isEnable) {
        mIvClose.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    @NonNull
    @Override
    public int initLayout() {
        return R.layout.popup_common;
    }

    @Override
    public int initContentView() {
        return R.id.rl_pop_content;
    }

    @Override
    public int initAnimationView() {
        return R.id.ll_pop_anim;
    }

    @Override
    public int initEnterAnimation() {
        return 0;
    }

    @Override
    public int initExitAnimation() {
        return 0;
    }

    @Override
    public void onClick(int resId) {
        if (resId == R.id.tv_pop_cancel) {
            if (onPopItemClickListener != null) {
                onPopItemClickListener.onClick(false);
            }
        } else if (resId == R.id.tv_pop_confirm) {
            onPopItemClickListener.onClick(true);
        }
    }

    public static class Builder {

        private Context context;
        private String title, content, cancel, confirm;
        private boolean enableClose, enableSingle;
        private CommonPopupWindow popupWindow;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder cancelText(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder confirmText(String confirm) {
            this.confirm = confirm;
            return this;
        }

        public Builder enableClose(boolean isEnable) {
            this.enableClose = isEnable;
            return this;
        }

        public Builder enableSingleControl(boolean isEnable) {
            this.enableSingle = isEnable;
            return this;
        }

        public CommonPopupWindow build() {
            popupWindow = new CommonPopupWindow(this);
            return popupWindow;
        }
    }

    private static boolean checkNull(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }

    public void setOnPopItemClickListener(OnPopupItemClickListener onPopItemClickListener) {
        this.onPopItemClickListener = onPopItemClickListener;
    }

    public interface OnPopupItemClickListener {
        /**
         * PopupWindow点击事件
         *
         * @param isPositive true为Confirm false为Cancel
         */
        void onClick(boolean isPositive);
    }
}
