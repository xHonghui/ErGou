package com.laka.androidlib.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.laka.androidlib.interfaces.BasePopup;
import com.laka.androidlib.util.ContextUtil;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.screen.ScreenUtils;

/**
 * @Author:Rayman
 * @Date:2018/5/4
 * @Description:PopupWindow基类
 */

public abstract class BasePopupWindow extends PopupWindow implements BasePopup {

    private final String TAG = this.getClass().getSimpleName();

    /**
     * description:页面布局
     **/
    private View mLayout;

    /**
     * description:布局填充内容
     **/
    private View mContentView;

    /**
     * description:具体动画执行
     **/
    private View mAnimView;

    /**
     * description:参数配置
     **/
    private Context context;
    private LayoutInflater inflater;
    private int mWidth, mHeight;
    private Animation enterAnimation, exitAnimation;
    private boolean isAnimRunning = false;
    private boolean isOutSideDismiss = true;
    private InputMethodManager mInputMethodManager;

    private OnDismissListener onDismissListener;
    private OnItemClickListener onItemClickListener;

    public BasePopupWindow(Context context) {
        this.context = context;
        mWidth = ScreenUtils.getScreenWidth();
        mHeight = ScreenUtils.getScreenHeight();
        mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        initPopupWindow(context);
    }

    private void initPopupWindow(Context context) {
        if (context == null) {
            return;
        }

        inflater = LayoutInflater.from(context);
        int resId = initLayout();
        if (resId == -1) {
            throw new IllegalArgumentException("Error layout id");
        }
        mLayout = inflater.inflate(resId, null);
        mContentView = mLayout.findViewById(initContentView());
        mAnimView = mLayout.findViewById(initAnimationView());

        mContentView.setPadding(mContentView.getPaddingLeft(),
                mContentView.getPaddingTop(),
                mContentView.getPaddingRight(),
                mContentView.getPaddingBottom() + ScreenUtils.getStatusBarHeight());

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOutSideDismiss) {
                    dismiss();
                }
            }
        });
        setContentView(mLayout);
        setWidth(mWidth);
        setHeight(mHeight);
        setAnimationStyle(0);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(false);
        mLayout.setFocusableInTouchMode(false);
        setOutsideTouchable(true);
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });
        initAnimation();
    }

    private void initAnimation() {
        int enterId = initEnterAnimation();
        int exitId = initExitAnimation();

        if (enterId == 0 || exitId == -1) {
            return;
        }

        if (exitId == 0 || exitId == -1) {
            return;
        }
        enterAnimation = AnimationUtils.loadAnimation(context, enterId);
        exitAnimation = AnimationUtils.loadAnimation(context, exitId);

        exitAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimRunning = false;
                boolean isContextValid = ContextUtil.isValidContext(context);
                if (!isContextValid) {
                    return;
                }

                LogUtils.info("BasePopupWindow---ExitAnimationEnd----Activity valid：" + isContextValid);

                BasePopupWindow.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void showPopupWindow() {
        showPopupWindow(Gravity.CENTER);
    }

    public void showPopupWindow(int gravity) {
        boolean isContextValid = ContextUtil.isValidContext(context);
        if (!isContextValid) {
            return;
        }

        LogUtils.info("BasePopupWindow---showPopupWindow----Activity valid：" + isContextValid);

        if (isShowing()) {
            dismiss();
        } else {
            if (!isAnimRunning) {
                mInputMethodManager.hideSoftInputFromWindow(mContentView.getWindowToken(), 0);
                if (enterAnimation != null && mAnimView != null) {
                    mAnimView.startAnimation(enterAnimation);
                }
                if (mLayout != null) {
                    super.showAtLocation(mLayout, gravity, 0, 0);
                }
            }
        }
    }

    @Override
    public void showAsDropDown(View anchorView) {
        boolean isContextValid = ContextUtil.isValidContext(context);
        if (!isContextValid) {
            return;
        }

        LogUtils.info("BasePopupWindow---showAsDropDown----Activity valid：" + isContextValid);

        if (isShowing()) {
            dismiss();
        } else {
            if (!isAnimRunning) {
                mInputMethodManager.hideSoftInputFromInputMethod(mContentView.getWindowToken(), 0);
                if (enterAnimation != null && mAnimView != null) {
                    mAnimView.startAnimation(enterAnimation);
                }
                if (mLayout != null) {
                    super.showAsDropDown(anchorView);
                }
            }
        }
    }

    @Override
    public void dismiss() {
        boolean isContextValid = ContextUtil.isValidContext(context);
        if (!isContextValid) {
            return;
        }

        LogUtils.info("BasePopupWindow---dismiss----Activity valid：" + isContextValid);

        if (isShowing()) {
            if (!isAnimRunning) {
                if (exitAnimation != null && mAnimView != null) {
                    mAnimView.startAnimation(exitAnimation);
                } else {
                    super.dismiss();
                }
            }
        }
    }

    public Context getContext() {
        return context;
    }

    @Override
    public View getContentView() {
        return mLayout;
    }

    public View getView(int resId) {
        if (resId == -1 || resId == 0) {
            return null;
        }
        return mLayout.findViewById(resId);
    }

    public void setText(int resId, String text) {
        ((TextView) getView(resId)).setText(text);
    }

    public void setTextColor(int resId, int textColor) {
        ((TextView) getView(resId)).setTextColor(textColor);
    }

    public void setTextStyle(int resId, int styleId) {
        ((TextView) getView(resId)).setTextAppearance(context, styleId);
    }

    public void setEnableOutSideDismiss(boolean enable) {
        this.isOutSideDismiss = enable;
    }

    public void setOnClickListener(final int resId) {
        setOnClickListener(getView(resId));
    }

    public void setOnClickListener(final View view) {
        if (view == null) {
            return;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view.getId());
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int resId);
    }
}
