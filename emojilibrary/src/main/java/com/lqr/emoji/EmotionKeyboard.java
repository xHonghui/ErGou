package com.lqr.emoji;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lqr.recyclerview.CustomGridLayoutManager;
import com.lqr.recyclerview.LQRRecyclerView;
import com.lqr.utils.OsUtils;

/**
 * CSDN_LQR
 * 表情键盘协调工具
 */

public class EmotionKeyboard {
    private Activity mActivity;
    private InputMethodManager mInputManager;//软键盘管理类
    private SharedPreferences mSp;
    private View mEmotionLayout;//表情布局
    private EditText mEditText;
    private View mContentView;//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪
    private LQRRecyclerView mLQRRecyclerView;

    public EmotionKeyboard() {
    }

    public static EmotionKeyboard with(Activity activity) {
        EmotionKeyboard emotionInputDetector = new EmotionKeyboard();
        emotionInputDetector.mActivity = activity;
        emotionInputDetector.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        emotionInputDetector.mSp = activity.getSharedPreferences(OsUtils.SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return emotionInputDetector;
    }

    /**
     * 绑定内容view，此view用于固定bar的高度，防止跳闪
     */
    public EmotionKeyboard bindToContent(View contentView) {
        mContentView = contentView;
        return this;
    }

    public EmotionKeyboard bindToRecyclerView(LQRRecyclerView recyclerView) {
        this.mLQRRecyclerView = recyclerView;
        return this;
    }

    /**
     * 绑定编辑框
     */
    public EmotionKeyboard bindToEditText(EditText editText) {
        mEditText = editText;
        mEditText.requestFocus();
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && mEmotionLayout.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout(true);//隐藏表情布局，显示软件盘
                    //软件盘显示后，释放内容高度
                    mEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unlockContentHeightDelayed();
                        }
                    }, 200L);
                }
                return false;
            }
        });
        return this;
    }

    /**
     * 绑定表情按钮（可以有多个表情按钮）
     *
     * @param emotionButton
     * @return
     */
    public EmotionKeyboard bindToEmotionButton(View... emotionButton) {
        for (View view : emotionButton) {
            view.setOnClickListener(getOnEmotionButtonOnClickListener());
        }
        return this;
    }

    private long mEmotionButtonPreClickTime = 0;
    private long BUTTON_CLICK_INTEVAL = 300;

    private View.OnClickListener getOnEmotionButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //快速点击拦截
                if (System.currentTimeMillis() - mEmotionButtonPreClickTime < BUTTON_CLICK_INTEVAL) {
                    return;
                }
                mEmotionButtonPreClickTime = System.currentTimeMillis();
                if (mOnEmotionButtonOnClickListener != null) {
                    if (mOnEmotionButtonOnClickListener.onEmotionButtonOnClickListener(v)) {
                        return;
                    }
                }
                if (mEmotionLayout.isShown()) {
                    Log.i("lock", "表情面板切换到键盘");
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout(true);//隐藏表情布局，显示软件盘
                    unlockContentHeightDelayed();//软件盘显示后，释放内容高度
                } else {
                    if (isSoftInputShown()) {//同上
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showEmotionLayout();//两者都没显示，直接显示表情布局
                    }
                }
                mOnEmotionButtonOnClickListener.onEmotionUIRenderingFinish();
            }
        };
    }

    /*================== 表情按钮点击事件回调 begin ==================*/
    public interface OnEmotionButtonOnClickListener {
        /**
         * 主要是为了适用仿微信的情况，微信有一个表情按钮和一个功能按钮，这2个按钮都是控制了底部区域的显隐
         *
         * @param view
         * @return true:拦截切换输入法，false:让输入法正常切换
         */
        boolean onEmotionButtonOnClickListener(View view);

        /**
         * UI更新完成的回调，有时候需要等表情包面板更新完成才能进行某些操作，就需要使用到该回调
         * 比如：表情包面板UI更新完成后，列表需要滑动到底部
         */
        void onEmotionUIRenderingFinish();
    }

    OnEmotionButtonOnClickListener mOnEmotionButtonOnClickListener;

    public void setOnEmotionButtonOnClickListener(OnEmotionButtonOnClickListener onEmotionButtonOnClickListener) {
        mOnEmotionButtonOnClickListener = onEmotionButtonOnClickListener;
    }
    /*================== 表情按钮点击事件回调 end ==================*/

    /**
     * 设置表情内容布局
     *
     * @param emotionLayout
     * @return
     */
    public EmotionKeyboard setEmotionLayout(View emotionLayout) {
        mEmotionLayout = emotionLayout;
        return this;
    }

    public EmotionKeyboard build() {
        //设置软件盘的模式：SOFT_INPUT_ADJUST_RESIZE  这个属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
        //从而方便我们计算软件盘的高度
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //隐藏软件盘
        hideSoftInput();
        return this;
    }

    /**
     * 点击返回键时先隐藏表情布局
     *
     * @return
     */
    public boolean interceptBackPress() {
        if (mEmotionLayout.isShown()) {
            hideEmotionLayout(false);
            return true;
        }
        return false;
    }

    /**
     * mEmotionLayout：表情父布局，控制表情父控件的显隐，需要外层去控制子布局的显隐
     */
    private void showEmotionLayout() {
        int softInputHeight = mSp.getInt(OsUtils.SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, dip2Px(270));
        Log.i(OsUtils.SHARE_PREFERENCE_NAME, "显示表情面板时获取的键盘高度：" + softInputHeight);
        hideSoftInput();
        mEmotionLayout.getLayoutParams().height = softInputHeight;
        mEmotionLayout.setVisibility(View.VISIBLE);
    }

    public int dip2Px(int dip) {
        float density = mActivity.getApplicationContext().getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    private void hideEmotionLayout(boolean showSoftInput) {
        if (mEmotionLayout.isShown()) {
            mEmotionLayout.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    public void unlockContentHeightDelayed() {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    public void showSoftInput() {
        mEditText.requestFocus();
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(mEditText, 0);
            }
        });
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 是否显示软件盘
     *
     * @return
     */
    public boolean isSoftInputShown() {
        return getSupportSoftInputHeight() > 0;
    }

    /**
     * 获取软件盘的高度并保存本地
     *
     * @return
     */
    public int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度(某些机型会有误差)
        int softInputHeight = screenHeight - r.bottom;
        Log.i(OsUtils.SHARE_PREFERENCE_NAME, "screenHeight:" + screenHeight + "---------------r.bottom:" + r.bottom);
        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (OsUtils.hasNavBar(mActivity)) {
            Log.i(OsUtils.SHARE_PREFERENCE_NAME, "存在虚拟按键");
        }
        if (OsUtils.hasNavBar(mActivity)) {
            if (OsUtils.isMIUI()) {//小米适配，针对通过正常方法获取虚拟键盘不正确的机型做的适配，在键盘未显示时，通过: 屏幕高度 - 屏幕可用高度 = 虚拟按键高度  来获取
                if (OsUtils.isOpenMiUiVirtualBar(mActivity)) {
                    //开启虚拟键盘
                    int localVirtualHeight = mSp.getInt(OsUtils.SHARE_PREFERENCE_VIRTUAL_KEY_HEIGHT, 0);
                    Log.i(OsUtils.SHARE_PREFERENCE_NAME, "存储在本地的localVirtualHeight：" + localVirtualHeight);
                    if (localVirtualHeight > 0) {//有虚拟按键，需要提前获取
                        softInputHeight -= localVirtualHeight;
                    } else {
                        softInputHeight -= OsUtils.getSoftButtonsBarHeight(mActivity);
                    }
                }
            } else {
                softInputHeight -= OsUtils.getSoftButtonsBarHeight(mActivity);
            }
        }
        //保存键盘高度到本地
        if (softInputHeight > 0) {
            Log.i(OsUtils.SHARE_PREFERENCE_NAME, "通过代码获取键盘高度：" + softInputHeight);
            mSp.edit().putInt(OsUtils.SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).commit();
        }
        return softInputHeight;
    }

}
