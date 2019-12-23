package com.laka.androidlib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.laka.androidlib.eventbus.EventBusManager;
import com.laka.androidlib.eventbus.BaseEventName;

/**
 * @Author:Rayman
 * @Date:2018/5/31
 * @Description:
 */

public class SoftKeyBoardUtil {

    // 用于监听软键盘的打开状态
    private static View sDecorView;
    private static boolean sSoftKeyBoardOpened = false;
    private static ViewTreeObserver.OnGlobalLayoutListener sOnGlobalLayoutListener;

    /**
     * 显示软键盘
     *
     * @param context
     * @param view
     */
    public static boolean showInputMethodForQuery(final Context context, final View view) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.showSoftInput(view, 0);
        }

        return false;
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideInputMethod(@NonNull final View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * 隐藏软键盘
     * 适合Activity页面，但是不适用于Fragment
     *
     * @param activity 当前Activity
     */
    public static void hideInputMethod(final Activity activity) {
        hideInputMethod(activity, activity.getCurrentFocus());
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
    public static void hideInputMethod(final Context context, final View view) {
        InputMethodManager imm = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 监听软键盘开启和关闭
     * 但是有个不准的是，第一监听会回调invisible，但是高度不一定为0
     *
     * @param activity
     * @param listener
     */
    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    listener.onSoftKeyBoardChange(keyboardHeight, !hide);
                }
                previousKeyboardHeight = height;
            }
        });
    }


    /**
     * 必须调用{@link #unObserveSoftKeyboard()}在Activity的onDestroy里面。
     * 软键盘的变化，由EventBus事件接收 ,EventName.SOFT_KEYBOARD_CHANGED.
     * 附带一个bool值。
     */
    public static void observeSoftKeyboard(Activity activity) {

        // 注册监听时，修改状态
        sSoftKeyBoardOpened = false;

        sDecorView = activity.getWindow().getDecorView();

        sOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                sDecorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = sDecorView.getHeight();
                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {

                    boolean visible = (double) displayHeight / height < 0.8;

                    if (visible != sSoftKeyBoardOpened) {
                        sSoftKeyBoardOpened = visible;
                        EventBusManager.postEvent(BaseEventName.SOFT_KEYBOARD_CHANGED, visible);
                    }
                }
                previousKeyboardHeight = height;
            }
        };

        sDecorView.getViewTreeObserver().addOnGlobalLayoutListener(sOnGlobalLayoutListener);
    }

    /**
     * 取消注册监听键盘变化
     */
    public static void unObserveSoftKeyboard() {

        if (sDecorView != null) {

            if (sOnGlobalLayoutListener != null) {
                sDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(sOnGlobalLayoutListener);
            }
            sDecorView = null;
        }
    }

    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeyboardHeight, boolean visible);
    }
}
